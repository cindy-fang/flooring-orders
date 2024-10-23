package org.example.flooringmastery.service;

import org.example.flooringmastery.model.*;
import org.example.flooringmastery.dao.*;

import java.util.*;
import java.time.*;
import java.math.*;

// constructor injection
public class ServiceLayerFileImpl implements ServiceLayer {

    private OrderDao orderDao;
    private AuditDao auditDao;
    private TaxDao taxDao;
    private ProductDao productDao;

    public ServiceLayerFileImpl(OrderDao dao, AuditDao auditDao, TaxDao taxDao, ProductDao productDao) {
        this.orderDao = dao;
        this.auditDao = auditDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
    }

    @Override
    public Map<String, Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public Map<String, TaxState> getAllStates() {
        return taxDao.getAllStateTaxes();
    }

    // get all orders based on date
    @Override
    public List<Order> getOrdersByDate(LocalDate orderDate) {
        return orderDao.getOrdersByDate(orderDate);
    }

    // get all orders (not used yet)
    @Override
    public List<Order> getAllOrders() throws PersistenceException {
        return orderDao.getAllOrders();
    }

    // get order based on date and id
    @Override
    public Order getOrder(LocalDate orderDate, Integer orderId) throws PersistenceException {
        return orderDao.getOrder(orderDate, orderId, false);
    }

    // add order
    public void addOrder(Order order) throws
            DuplicateIdException,
            DataValidationException,
            PersistenceException {

        // check to see if there is already an Order associated with the given order's id
        LocalDate stringToDate = Order.stringToDate(order.getOrderDate());
        boolean addOrder = true;
        if (orderDao.getOrder(stringToDate, order.getOrderId(), addOrder) != null) {
            throw new DuplicateIdException(
                    "ERROR: Could not create Order.  Order Id " + order.getOrderId() + " already exists");
        }

        // validate all the fields on the given Order object
        validateOrderData(order);

        orderDao.addOrder(order);

        // order successfully created, write to the audit log
        auditDao.writeAuditEntry("Order " + order.getOrderId() + " CREATED.");
    }

    // edit order
    @Override
    public void editOrder(LocalDate orderDate, Order updatedOrder) throws DataValidationException, PersistenceException {
        // Validate updated order data before editing
        // I think this shouldn't be here as user could press enter to move on
        //validateOrderData(updatedOrder);

        orderDao.editOrder(orderDate, updatedOrder);
        auditDao.writeAuditEntry("Order " + updatedOrder.getOrderId() + " UPDATED.");
    }

    // remove order
    public Order removeOrder(LocalDate orderDate, Integer orderId) throws PersistenceException {
        Order removedOrder = orderDao.removeOrder(orderDate, orderId);
        // write to audit log
        auditDao.writeAuditEntry("Order " + orderId + " REMOVED.");
        return removedOrder;
    }

    // validate order data input
    private void validateOrderData(Order order) throws DataValidationException {
        if (order.getCustomerName() == null
                || order.getCustomerName().trim().length() == 0
                || order.getStateId() == null
                || order.getStateId().trim().length() == 0
                || order.getProductType() == null
                || order.getProductType().trim().length() == 0
                || order.getArea() == null || order.getArea().compareTo(BigDecimal.ZERO) <= 0 || order.getArea().compareTo(new BigDecimal("100")) < 0) { // check for positive area and 100 or larger

            throw new DataValidationException(
                    "ERROR: All fields [Customer Name, State, Product Type] are required and Area must be greater than 0.");
        }
    }

    // calculate costs after getting order input from add/edit order
    public Order calculateCosts(Order order) throws PersistenceException, DataValidationException {
        // validate order data before calculations
        validateOrderData(order);

        // get the product based on the product type
        Product product = productDao.getProduct(order.getProductType());
        if (product == null) {
            throw new PersistenceException("Product type not found.");
        }
        // get the state tax based on the state
        TaxState stateTax = taxDao.getStateTaxRate(order.getStateId());
        if (stateTax == null) {
            throw new PersistenceException("State tax not found.");
        }

        // get values from order, product, and stateTax
        BigDecimal area = order.getArea();
        BigDecimal costPerSquareFoot = product.getCostPerSquareFoot();
        BigDecimal labourCostPerSquareFoot = product.getLabourCostPerSquareFoot();
        BigDecimal taxRate = stateTax.getStateTaxRate();

        // calculate MaterialCost
        BigDecimal materialCost = area.multiply(costPerSquareFoot);
        order.setMaterialCost(materialCost);

        // calculate LabourCost
        BigDecimal labourCost = area.multiply(labourCostPerSquareFoot);
        order.setLabourCost(labourCost);

        // calculate Tax
        BigDecimal taxTotal = (materialCost.add(labourCost)).multiply(taxRate.divide(new BigDecimal("100")));
        order.setTaxTotal(taxTotal);

        // calculate Total
        BigDecimal total = materialCost.add(labourCost).add(taxTotal);
        order.setTotal(total);

        return order;
    }

    // method to apply updated field Map to order instance
    public void applyChangesToOrder(Order currentOrder, Map<String, String> updatedFields) throws DataValidationException {
        // loop through the updated fields and apply them to the order
        for (Map.Entry<String, String> entry : updatedFields.entrySet()) {
            String field = entry.getKey();
            String newValue = entry.getValue();

            switch (field) {
                case "customerName":
                    currentOrder.setCustomerName(newValue);
                    break;
                case "stateId":
                    currentOrder.setStateId(newValue);
                    break;
                case "productType":
                    currentOrder.setProductType(newValue);
                    break;
                case "area":
                    try {
                        BigDecimal area = new BigDecimal(newValue);
                        if (area.compareTo(BigDecimal.ZERO) <= 0) {
                            throw new DataValidationException("Area must be greater than 0.");
                        }
                        currentOrder.setArea(area);
                    } catch (NumberFormatException e) {
                        throw new DataValidationException("Invalid area value: " + newValue);
                    }
                    break;
                default:
                    throw new DataValidationException("Invalid field: " + field);
            }
        }
    }
}