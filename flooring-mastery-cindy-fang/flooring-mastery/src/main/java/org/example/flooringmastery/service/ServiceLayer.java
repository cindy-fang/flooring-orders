package org.example.flooringmastery.service;
import org.example.flooringmastery.model.*;
import org.example.flooringmastery.dao.*;

import java.util.*;
import java.time.*;

public interface ServiceLayer {

    Map<String, Product> getAllProducts()
            throws PersistenceException;

    Map<String, TaxState> getAllStates()
            throws PersistenceException;

    List<Order> getOrdersByDate(LocalDate orderDate)
            throws PersistenceException;

    List<Order> getAllOrders()
            throws PersistenceException;

    Order getOrder(LocalDate orderDate, Integer orderId)
            throws PersistenceException;

    void addOrder(Order order)
            throws
            DuplicateIdException,
            DataValidationException,
            PersistenceException;

    void editOrder(LocalDate orderDate, Order updatedOrder)
            throws
            DataValidationException,
            PersistenceException;

    Order removeOrder(LocalDate orderDate, Integer orderId)
            throws PersistenceException;

    Order calculateCosts(Order order)
            throws
            DataValidationException,
            PersistenceException;

    void applyChangesToOrder(Order currentOrder, Map<String, String> updatedFields)
            throws
            DataValidationException;
}