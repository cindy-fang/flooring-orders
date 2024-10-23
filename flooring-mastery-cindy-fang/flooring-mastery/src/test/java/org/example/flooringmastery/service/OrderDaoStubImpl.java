package org.example.flooringmastery.service;

import org.example.flooringmastery.dao.*;
import org.example.flooringmastery.model.*;

import java.time.LocalDate;
import java.util.*;
import java.math.*;

public class OrderDaoStubImpl implements OrderDao {

    private final Map<LocalDate, Map<Integer, Order>> orderStorage = new HashMap<>();

    public OrderDaoStubImpl() {
        // Adding a sample order for testing purposes
        Order sampleOrder = new Order("2024-11-21");
        sampleOrder.setOrderDate(LocalDate.now().toString());
        sampleOrder.setCustomerName("Cindy");
        sampleOrder.setStateId("TX");
        sampleOrder.setProductType("Wood");
        BigDecimal area = new BigDecimal(100);
        sampleOrder.setArea(area);
        sampleOrder.setMaterialCost(new BigDecimal("200.00"));
        sampleOrder.setLabourCost(new BigDecimal("150.00"));
        sampleOrder.setTaxTotal(new BigDecimal("28.00"));
        sampleOrder.setTotal(new BigDecimal("378.00"));

        // Adding sample order to the stub
        addOrder(sampleOrder);
    }

    @Override
    public void addOrder(Order order) throws PersistenceException {
        LocalDate orderDate = LocalDate.parse(order.getOrderDate());
        Map<Integer, Order> ordersForDate = orderStorage.computeIfAbsent(orderDate, k -> new HashMap<>());

        if (ordersForDate.containsKey(order.getOrderId())) {
            throw new PersistenceException("Order with ID " + order.getOrderId() + " already exists for date " + orderDate);
        }
        ordersForDate.put(order.getOrderId(), order);
    }

    @Override
    public Order getOrder(LocalDate orderDate, Integer orderId, boolean addOrder) throws PersistenceException {
        Map<Integer, Order> ordersForDate = orderStorage.get(orderDate);
        if (ordersForDate == null || !ordersForDate.containsKey(orderId)) {
            throw new PersistenceException("Order with ID " + orderId + " does not exist for date " + orderDate);
        }
        return ordersForDate.get(orderId);
    }

    @Override
    public List<Order> getAllOrders() throws PersistenceException {
        List<Order> allOrders = new ArrayList<>();
        for (Map<Integer, Order> ordersForDate : orderStorage.values()) {
            allOrders.addAll(ordersForDate.values());
        }
        if (allOrders.isEmpty()) {
            throw new PersistenceException("No orders available.");
        }
        return allOrders;
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate orderDate) throws PersistenceException {
        Map<Integer, Order> ordersOnDate = orderStorage.getOrDefault(orderDate, Collections.emptyMap());
        List<Order> ordersList = new ArrayList<>(ordersOnDate.values());
        if (ordersList.isEmpty()) {
            throw new PersistenceException("No orders found for date " + orderDate);
        }
        return ordersList;
    }

    @Override
    public void editOrder(LocalDate orderDate, Order updatedOrder) throws PersistenceException {
        Order existingOrder = getOrder(orderDate, updatedOrder.getOrderId(), false);
        if (existingOrder != null) {
            existingOrder.setCustomerName(updatedOrder.getCustomerName());
            existingOrder.setStateId(updatedOrder.getStateId());
            existingOrder.setProductType(updatedOrder.getProductType());
            existingOrder.setArea(updatedOrder.getArea());
            existingOrder.setMaterialCost(updatedOrder.getMaterialCost());
            existingOrder.setLabourCost(updatedOrder.getLabourCost());
            existingOrder.setTaxTotal(updatedOrder.getTaxTotal());
            existingOrder.setTotal(updatedOrder.getTotal());
        } else {
            throw new PersistenceException("Order with ID " + updatedOrder.getOrderId() + " not found for date " + orderDate);
        }
    }

    @Override
    public Order removeOrder(LocalDate orderDate, Integer orderId) throws PersistenceException {
        Map<Integer, Order> ordersOnDate = orderStorage.get(orderDate);
        if (ordersOnDate != null) {
            Order removedOrder = ordersOnDate.remove(orderId);
            if (removedOrder == null) {
                throw new PersistenceException("Order with ID " + orderId + " does not exist for date " + orderDate);
            }
            return removedOrder;
        }
        throw new PersistenceException("No orders found for date " + orderDate);
    }
}
