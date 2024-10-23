package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.*;

public interface OrderDao {
    void addOrder(Order order) throws
            PersistenceException;

    Order getOrder(LocalDate orderDate, Integer orderId, boolean addOrder)
            throws PersistenceException;

    List<Order> getOrdersByDate(LocalDate date) throws
            PersistenceException;

    List<Order> getAllOrders() throws
            PersistenceException;

    void editOrder(LocalDate orderDate, Order updatedOrder) throws
            PersistenceException;

    Order removeOrder(LocalDate orderDate, Integer orderId) throws
            PersistenceException;
}