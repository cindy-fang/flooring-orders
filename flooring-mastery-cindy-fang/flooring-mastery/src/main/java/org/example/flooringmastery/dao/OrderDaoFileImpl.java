package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDaoFileImpl implements OrderDao {
    private Map<LocalDate, Map<Integer, Order>> allOrders = new HashMap<>();

    // add order to hashmap
    @Override
    public void addOrder(Order order) throws PersistenceException {
        // create a new order instance and the inner map for this date
        String orderDateString = order.getOrderDate();
        LocalDate orderDate = Order.stringToDate(orderDateString);
        Map<Integer, Order> newOrderForDate = allOrders.computeIfAbsent(orderDate, k -> new HashMap<>());

        // check if the order ID already exists
        if (newOrderForDate.containsKey(order.getOrderId())) {
            throw new PersistenceException("Order with ID " + order.getOrderId() + " already exists for date " + orderDate);
        }

        // add the order (id, order) to the inner map after finding date key
        newOrderForDate.put(order.getOrderId(), order);
    }

    // get order from hashmap
    @Override
    public Order getOrder(LocalDate orderDate, Integer orderId, boolean addOrder) throws PersistenceException {
        Map<Integer, Order> ordersForDate = allOrders.get(orderDate);

        if (ordersForDate != null) {
            Order order = ordersForDate.get(orderId);
            if (order == null) {
                throw new PersistenceException("Order with ID " + orderId + " does not exist for date " + orderDate);
            }
            return order;
        } else {
            if (!addOrder){
                throw new PersistenceException("No orders found for date " + orderDate);
            }
            return null;
        }
    }

    // get all orders from hashmap
    @Override
    public List<Order> getAllOrders() throws PersistenceException {
        List<Order> orderList = allOrders.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toList());

        if (orderList.isEmpty()) {
            throw new PersistenceException("No orders available.");
        }
        return new ArrayList<>(orderList);
    }

    // get all orders from a passed in date
    @Override
    public List<Order> getOrdersByDate(LocalDate orderDate) throws PersistenceException {
        List<Order> ordersOnDate = allOrders.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(orderDate))
                .flatMap(entry -> entry.getValue().values().stream())
                .collect(Collectors.toList());

        if (ordersOnDate.isEmpty()) {
            throw new PersistenceException("No orders found for date " + orderDate);
        }

        return ordersOnDate;
    }

    // edit an order
    @Override
    public void editOrder(LocalDate orderDate, Order updatedOrder) throws PersistenceException {
        // get the existing order
        Order orderToUpdate = getOrder(orderDate, updatedOrder.getOrderId(), false);

        // if the order exists, update it
        if (orderToUpdate != null) {
            orderToUpdate.setCustomerName(updatedOrder.getCustomerName());
            orderToUpdate.setStateId(updatedOrder.getStateId());
            orderToUpdate.setProductType(updatedOrder.getProductType());
            orderToUpdate.setArea(updatedOrder.getArea());

            // recalculate and update order costs
            orderToUpdate.setMaterialCost(updatedOrder.getMaterialCost());
            orderToUpdate.setLabourCost(updatedOrder.getLabourCost());
            orderToUpdate.setTaxTotal(updatedOrder.getTaxTotal());
            orderToUpdate.setTotal(updatedOrder.getTotal());
        } else {
            throw new PersistenceException("Order with ID " + updatedOrder.getOrderId() + " not found for date " + orderDate);
        }
    }

    // remove an order from hashmap
    @Override
    public Order removeOrder(LocalDate orderDate, Integer orderId) throws PersistenceException {
        if (orderId != null) {
            Map<Integer, Order> ordersOnDate = allOrders.get(orderDate);
            if (ordersOnDate != null) {
                Order removedOrder = ordersOnDate.remove(orderId);
                if (removedOrder == null) {
                    throw new PersistenceException("Order with ID " + orderId + " does not exist for date " + orderDate);
                }
                return removedOrder;
            } else {
                throw new PersistenceException("No orders found for date " + orderDate);
            }
        }
        throw new PersistenceException("Order ID cannot be null.");
    }
}