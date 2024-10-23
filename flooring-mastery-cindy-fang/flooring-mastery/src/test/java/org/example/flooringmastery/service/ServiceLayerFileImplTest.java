//package org.example.flooringmastery.service;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.example.flooringmastery.dao.*;
//import org.example.flooringmastery.model.*;
//
//import java.time.*;
//import java.math.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ServiceLayerFileImplTest {
//
//    private ServiceLayerFileImpl service;
//    private OrderDao orderDao;
//    private ProductDao productDao;
//    private TaxDao taxDao;
//    private AuditDao auditDao;
//
//    @BeforeEach
//    void setUp() {
//        orderDao = new OrderDaoStubImpl();
//        productDao = new ProductDaoStubImpl();
//        taxDao = new TaxDaoStubImpl();
//        service = new ServiceLayerFileImpl(orderDao, auditDao, taxDao, productDao);
//    }
//
//    @AfterEach
//    void tearDown() {
//        // reset the OrderDaoStubImpl to ensure a clean state for each test
//        orderDao = new OrderDaoStubImpl(); // Reinitialize stub
//        productDao = new ProductDaoStubImpl();
//        taxDao = new TaxDaoStubImpl();
//        auditDao = new AuditDaoStubImpl(); // If you have an AuditDao stub as well
//        service = new ServiceLayerFileImpl(orderDao, auditDao, taxDao, productDao);
//    }
//
//    @Test
//    void getAllProducts() {
//        Map<String, Product> products = service.getAllProducts();
//        assertNotNull(products);
//        assertFalse(products.isEmpty());
//        assertEquals(1, products.size());
//    }
//
//    @Test
//    void getAllStates() {
//        Map<String, TaxState> states = service.getAllStates();
//        assertNotNull(states);
//        assertFalse(states.isEmpty());
//        assertEquals(1, states.size());
//    }
//
//    @Test
//    void getOrdersByDate() {
//        LocalDate orderDate = LocalDate.of(2024, 11, 21);
//        List<Order> orders = service.getOrdersByDate(orderDate);
//        assertNotNull(orders);
//        assertEquals(1, orders.size());
//    }
//
//    @Test
//    void getAllOrders() {
//        List<Order> orders = service.getAllOrders();
//        assertNotNull(orders);
//        assertFalse(orders.isEmpty());
//        assertEquals(1, orders.size());
//    }
//
//    @Test
//    void getOrder() {
//        LocalDate orderDate = LocalDate.of(2024, 11, 21);
//        Order order = service.getOrder(orderDate, 0);
//        assertNotNull(order);
//        assertEquals(0, order.getOrderId().intValue());
//    }
//
//    @Test
//    void addOrder() {
//        LocalDate orderDate = LocalDate.of(2024, 11, 21);
//        Order newOrder = new Order(orderDate.toString());
//        newOrder.setOrderDate(orderDate.toString());
//        newOrder.setCustomerName("Cindai");
//        newOrder.setStateId("CA");
//        newOrder.setProductType("Tile");
//        BigDecimal area = new BigDecimal(150);
//        newOrder.setArea(area);
////        newOrder.setMaterialCost(new BigDecimal("300.00"));
////        newOrder.setLabourCost(new BigDecimal("200.00"));
////        newOrder.setTaxTotal(new BigDecimal("50.00"));
////        newOrder.setTotal(new BigDecimal("550.00"));
//
//        service.addOrder(newOrder);
//
//        // Verify the order was added
//        Order addedOrder = service.getOrder(orderDate, 1);
//        assertNotNull(addedOrder);
//        assertEquals("Cindai", addedOrder.getCustomerName());
//    }
//
//    @Test
//    void editOrder() {
//        LocalDate orderDate = LocalDate.of(2024, 11, 21);
//        Order updatedOrder = new Order(orderDate.toString());
//        updatedOrder.setCustomerName("");
//        updatedOrder.setStateId("TX");
//        updatedOrder.setProductType("Wood");
//        BigDecimal area = new BigDecimal(120);
//        updatedOrder.setArea(area);
////        updatedOrder.setMaterialCost(new BigDecimal("240.00"));
////        updatedOrder.setLabourCost(new BigDecimal("180.00"));
////        updatedOrder.setTaxTotal(new BigDecimal("30.00"));
////        updatedOrder.setTotal(new BigDecimal("450.00"));
//
//        service.editOrder(orderDate, updatedOrder);
//
//        // Verify the order was updated
//        Order order = service.getOrder(orderDate, 0);
//        assertNotNull(order);
//        assertEquals("Cindai", order.getCustomerName());
//    }
//
//    @Test
//    void removeOrder() {
//        LocalDate orderDate = LocalDate.of(2024, 11, 21);
//        Order removedOrder = service.removeOrder(orderDate, 1);
//        assertNotNull(removedOrder);
//        assertEquals(1, removedOrder.getOrderId().intValue());
//
//        // Verify the order was removed
//        Exception exception = assertThrows(PersistenceException.class, () -> {
//            service.getOrder(orderDate, 1);
//        });
//        assertEquals("Order with ID 1 does not exist for date " + orderDate, exception.getMessage());
//    }
//
//    @Test
//    void calculateCosts() {
//    }
//
//    @Test
//    void applyChangesToOrder() {
//    }
//}