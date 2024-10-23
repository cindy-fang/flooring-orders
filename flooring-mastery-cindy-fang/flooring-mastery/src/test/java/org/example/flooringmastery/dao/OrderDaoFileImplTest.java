package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class OrderDaoFileImplTest {

    OrderDao orderDaoTest;

    public OrderDaoFileImplTest() {}

        @BeforeEach
        void setUp() throws Exception {
            orderDaoTest = new OrderDaoFileImpl();
        }

        @AfterEach
        void tearDown() {
        }

        @Test
        void addOrder() {
            // Create our method test inputs
            Order order = new Order("2024-11-21");
            order.setCustomerName("Ada");
            order.setProductType("Tile");
            order.setStateId("TX");
            BigDecimal area = new BigDecimal("100");
            order.setArea(area);

            //  Add the order to the DAO
            orderDaoTest.addOrder(order);
            // Get the order from the DAO
            LocalDate orderDate = LocalDate.parse(order.getOrderDate()); // Parse the date string to LocalDate

            Order retrievedOrder = orderDaoTest.getOrder(orderDate, order.getOrderId(), false);

            // Check the data is equal
            assertEquals(order.getOrderId(),
                    retrievedOrder.getOrderId(),
                    "Checking order id.");
            assertEquals(order.getCustomerName(),
                    retrievedOrder.getCustomerName(),
                    "Checking customer name.");
            assertEquals(order.getProductType(),
                    retrievedOrder.getProductType(),
                    "Checking product type.");
            assertEquals(order.getStateId(),
                    retrievedOrder.getStateId(),
                    "Checking state name.");
            assertEquals(order.getArea(),
                    retrievedOrder.getArea(),
                    "Checking area.");

        }

        @Test
        void getOrder() {
        }

        @Test
        void getAllOrders() {
        }

        @Test
        void getOrdersByDate() {
            // Create our first order
            Order firstOrder = new Order("2025-05-10");
            firstOrder.setCustomerName("Ada Lovelace");
            firstOrder.setStateId("CA");
            firstOrder.setProductType("Wood");
            firstOrder.setArea(new BigDecimal("100"));

            // Create our second order
            Order secondOrder = new Order("2025-05-10");
            secondOrder.setCustomerName("Margaret Hamilton");
            secondOrder.setStateId("TX");
            secondOrder.setProductType("Tile");
            secondOrder.setArea(new BigDecimal("150"));

            // Add both our orders to the DAO
            orderDaoTest.addOrder(firstOrder);
            orderDaoTest.addOrder(secondOrder);

            // Retrieve the list of all orders for the specified date
            List<Order> allOrders = orderDaoTest.getOrdersByDate(LocalDate.of(2025, 5, 10));

        // First, check the general contents of the list
            assertNotNull(allOrders, "The list of orders must not be null");
            assertEquals(2, allOrders.size(), "List of orders should have 2 orders for the given date.");

        // Then, check the specifics
            assertTrue(allOrders.contains(firstOrder), "The list of orders should include Ada's order.");
            assertTrue(allOrders.contains(secondOrder), "The list of orders should include Charles' order.");

        }

        @Test
        void editOrder() {
        }

        @Test
        void removeOrder() {
            // Create our first order
            Order firstOrder = new Order("2025-05-10");
            firstOrder.setCustomerName("Ada Lovelace");
            firstOrder.setStateId("CA");
            firstOrder.setProductType("Wood");
            firstOrder.setArea(new BigDecimal("100"));

            // Create our second order
            Order secondOrder = new Order("2025-05-10");
            secondOrder.setCustomerName("Margaret Hamilton");
            secondOrder.setStateId("TX");
            secondOrder.setProductType("Tile");
            secondOrder.setArea(new BigDecimal("150"));

            // Create our third order
            Order thirdOrder = new Order("2025-05-10");
            thirdOrder.setCustomerName("Cindy Fang");
            thirdOrder.setStateId("TX");
            thirdOrder.setProductType("Tile");
            thirdOrder.setArea(new BigDecimal("150"));

            // Add both our orders to the DAO
            orderDaoTest.addOrder(firstOrder);
            orderDaoTest.addOrder(secondOrder);
            orderDaoTest.addOrder(thirdOrder);

            // remove the first order - Ada
            LocalDate firstOrderDate = LocalDate.parse(firstOrder.getOrderDate()); // Parse the date string to LocalDate
            Order removedStudent = orderDaoTest.removeOrder(firstOrderDate,firstOrder.getOrderId());

            // Check that the correct object was removed.
            assertEquals(removedStudent, firstOrder, "The removed order should be Ada.");

            // Get all the orders
            List<Order> allOrders = orderDaoTest.getAllOrders();

            // First check the general contents of the list
            assertNotNull( allOrders, "All orders list should be not null.");
            assertEquals( 2, allOrders.size(), "All students should only have 2 orders.");

            // Then the specifics
            assertFalse( allOrders.contains(firstOrder), "All orders should NOT include Ada.");
            assertTrue( allOrders.contains(secondOrder), "All order should NOT include Margaret.");

            // Remove the second order
            LocalDate secondOrderDate = LocalDate.parse(secondOrder.getOrderDate()); // Parse the date string to LocalDate
            Order removedOrder = orderDaoTest.removeOrder(secondOrderDate,secondOrder.getOrderId());
            // Check that the correct object was removed.
            assertEquals( removedOrder, secondOrder, "The removed order should be Margaret.");

            // retrieve all of the orders again, and check the list.
            allOrders = orderDaoTest.getAllOrders();

            // Check the contents of the list - it should be 1 left
            assertEquals( 1, allOrders.size(), "All orders should only have 1 order.");

            // Try to 'get' both students by their old id - they should be null!
//            Order retrievedOrder = orderDaoTest.getOrder(firstOrderDate, firstOrder.getOrderId(), false);
//            assertNull(retrievedOrder, "Ada was removed, should be null.");
//
//            retrievedOrder = orderDaoTest.getOrder(secondOrderDate, secondOrder.getOrderId(), false);
//            assertNull(retrievedOrder, "Margaret was removed, should be null.");

        }
    }