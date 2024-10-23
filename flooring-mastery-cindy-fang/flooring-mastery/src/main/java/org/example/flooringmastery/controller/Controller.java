package org.example.flooringmastery.controller;
import java.math.BigDecimal;
import java.util.*;
import java.time.*;
import org.example.flooringmastery.model.*;
import org.example.flooringmastery.view.*;
import org.example.flooringmastery.dao.*;
import org.example.flooringmastery.service.*;

public class Controller {
    private View view;
    private OrderDao orderDao;
    private ProductDao productDao;
    private TaxDao taxDao;
    private ServiceLayer service;

    // constructor, with dependency injection
    public Controller(ServiceLayer service, View view) {
        this.service = service;
        this.view = view;
    }
    // run method, catch for any exceptions to exit
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrdersForDate();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
        } catch (Exception e) {
            handleException(e, "menu selection");
        }
        exitMessage();
    }

    private int getMenuSelection() {
        return view.displayMenuAndGetSelection();
    }

    // add order method
    private void addOrder() {
        view.displayAddOrderMessage();
        boolean hasErrors = false;

        do {
            // get existing products list and states list
            Map<String, Product> products = service.getAllProducts();
            Map<String, TaxState> states = service.getAllStates();

            // get user input for the new order
            Order currentOrder = view.getAddOrderInput(products, states);

            try {
                // calculate costs and add to the order
                currentOrder = service.calculateCosts(currentOrder);

                // display the calculated costs to the console
                view.displayOrderCostDetails(currentOrder);

                // save the order
                service.addOrder(currentOrder);
                view.displayAddOrderSuccess(currentOrder);
                hasErrors = false; // exit loop if no errors
            } catch (Exception e) {
                hasErrors = true; // set flag to repeat input
                handleException(e, "adding the order");
            }
        } while (hasErrors);
    }

    // display all orders from date
    private void displayOrdersForDate() {
        // Step 1: Prompt the user for a date
        LocalDate orderDate = view.getDateInputWithValidation();

        try {
            // Step 2: Retrieve orders for that date
            List<Order> orders = service.getOrdersByDate(orderDate);

            // Step 3: Check for orders and display appropriate message
            if (orders.isEmpty()) {
                view.displayErrorMessage("No orders exist for the date: " + orderDate);
            } else {
                // Step 4: Display the orders
                view.displayOrders(orders);
            }
        } catch (Exception e) {
            handleException(e, "displaying order(s)");
        }
    }

    // display all existing orders to console
    private void displayOrders() throws PersistenceException {
        List<Order> orderList = service.getAllOrders();
        view.displayOrders(orderList);
    }

    // edit order method
    private void editOrder() {
        view.displayEditOrderMessage();

        try {
            // get ID and date from the user
            Integer orderId = view.getOrderIdInput();
            LocalDate orderDate = view.getDateInputWithValidation();

            // get the existing order, if it exists
            Order currentOrder = service.getOrder(orderDate, orderId);
            if (currentOrder == null) {
                view.displayErrorMessage("Order doesn't exist for ID: " + orderId + " on date: " + orderDate);
                return;
            }
            // display the products and states list:
            Map<String, Product> products = service.getAllProducts();
            Map<String, TaxState> states = service.getAllStates();

            // get updated fields as a map of field changes (without applying them yet)
            Map<String, String> updatedFields = view.getEditOrderInput(currentOrder, products, states);

            // display the proposed changes before applying them
            view.displayUpdatedFields(updatedFields, currentOrder);

            if (view.confirmChanges()) {
                // apply changes from the map to the currentOrder
                service.applyChangesToOrder(currentOrder, updatedFields);

                // calculate costs after editing the order
                currentOrder = service.calculateCosts(currentOrder);

                // save the updated order
                service.editOrder(orderDate, currentOrder);
                view.displayEditOrderSuccess();
            } else {
                // if user discards, don't modify the order
                view.displayChangesDiscarded();
            }
        } catch (Exception e) {
            handleException(e, "editing the order");
        }
    }

    // remove order
    private void removeOrder() {
        view.displayRemoveOrderMessage();
        try {
            // get order id and date from user
            Integer orderId = view.getOrderIdInput();
            LocalDate orderDate = view.getDateInputWithValidation();

            // attempt to get the order
            Order order = service.getOrder(orderDate, orderId);

            // check if the order exists
            if (order == null) {
                view.displayErrorMessage("Order with ID " + orderId + " on " + orderDate + " does not exist.");
                return; // exit the method if the order is not found
            }
            // if the order exists, remove it
            service.removeOrder(orderDate, orderId);
            view.displayRemoveResult(order);

        } catch (Exception e) {
            handleException(e, "removing the order");
        }
    }

    private void unknownCommand() {
        view.displayUnknownCommandMessage();
    }

    private void exitMessage() {
        view.displayExitMessage();
    }

    // New utility method to handle exceptions
    private void handleException(Exception e, String contextMessage) {
        if (e instanceof PersistenceException) {
            view.displayErrorMessage("Persistence error occurred: " + e.getMessage());
        } else if (e instanceof DataValidationException) {
            view.displayErrorMessage("Data validation error: " + e.getMessage());
        } else if (e instanceof DuplicateIdException) {
            view.displayErrorMessage("Duplicate order ID: " + e.getMessage());
        }else {
            view.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    }
}