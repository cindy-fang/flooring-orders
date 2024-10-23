package org.example.flooringmastery.view;
import org.example.flooringmastery.model.*;

import java.math.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

// constructor injection
public class View {
    private UserIO io;

    public View(UserIO io) {
        this.io = io;
    }

    // display menu and get selection from console
    public int displayMenuAndGetSelection() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders (by date)");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Quit");
        return io.readInt("Please select from the above choices.", 1, 5);
    }

    // display orders, show every field on a new line
    public void displayOrders(List<Order> orderList) {
        for (Order currentOrder : orderList) {
            String orderInfo = String.format("#ID: %s\nDate: %s\nProduct: %s\nArea: %s sq ft\nCustomer Name: %s\nState: %s",
                    currentOrder.getOrderId(),
                    currentOrder.getOrderDate(),
                    currentOrder.getProductType(),
                    currentOrder.getArea(),
                    currentOrder.getCustomerName(),
                    currentOrder.getStateId());
            io.print(orderInfo);
            displayOrderCostDetails(currentOrder);
            io.print("\n");
        }
        io.print("Please hit enter to continue");
    }

    // display all products
    public void displayProductList(Map<String, Product> products) {
        io.print("Available Products:");
        for (Product product : products.values()) {
            io.print(product.getProductType() + " - Cost: " + product.getCostPerSquareFoot() + " - Labour Cost:" + product.getLabourCostPerSquareFoot());
        }
    }

    // display all states and taxes
    public void displayStateList(Map<String, TaxState> states) {
        io.print("Available States:");
        for (TaxState state : states.values()) {
            io.print(state.getStateId() + " - " + state.getStateName() + " - Tax Rate: $" + state.getStateTaxRate());
        }
    }

    public Integer getOrderIdInput() {
        return io.readInt("Please enter the Order ID.");
    }

    // get all user input required to add order
    public Order getAddOrderInput(Map<String, Product> productList, Map<String, TaxState> stateList) {
        LocalDate orderDate = getDateInputWithValidation();

        // customer name validation with empty check
        String customerName;
        do {
            // read string from user
            customerName = io.readString("Please enter Customer Name:");
            // if the value is empty, keep on asking user for a name
            if (customerName.trim().isEmpty()) {
                io.print("Customer name cannot be empty. Please try again.");
            }
        } while (customerName.trim().isEmpty());
        // validate non empty customer name
        customerName = getCustomerNameWithValidation(customerName);

        // state validation
        displayStateList(stateList);
        String state;
        do {
            state = io.readString("Please enter State:");
            if (state.trim().isEmpty()) {
                io.print("State cannot be empty. Please try again.");
            }
        } while (state.trim().isEmpty());
        state = getStateWithValidation(state, stateList);

        // product type validation
        displayProductList(productList);
        String productType;
        do {
            productType = io.readString("Please enter Product Type:");
            if (productType.trim().isEmpty()) {
                io.print("Product type cannot be empty. Please try again.");
            }
        } while (productType.trim().isEmpty());
        productType = getProductTypeWithValidation(productType, productList);

        // validate area
        BigDecimal area = null;  // Initialize the area variable
        do {
            String areaInput = io.readString("Please enter Area (in sq ft):");
            if (areaInput.trim().isEmpty()) {
                io.print("Area cannot be empty. Please try again.");
                continue;
            }
            try {
                // try to convert to BigDecimal
                area = new BigDecimal(areaInput);
            } catch (NumberFormatException e) {
                // handle invalid input, e.g. input is not a valid number
                io.print("Invalid input. Please enter a valid decimal number.");
                continue;
            }
            if (!isValidArea(area)) {
                io.print("The area must be at least 100 sq ft and cannot be negative. Please try again.");
            }
        }while (area == null || !isValidArea(area));

        // create new order instance and set all fields from user input
        Order currentOrder = new Order(orderDate.toString());
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateId(state);
        currentOrder.setProductType(productType);
        currentOrder.setOrderDate(orderDate.toString());
        currentOrder.setArea(area);

        return currentOrder;
    }

    //  validate the date with format checking
    public LocalDate getDateInputWithValidation() {
        LocalDate date = null;
        boolean isValid;

        do {
            String dateInput = io.readString("Please enter Order Date starting today or in the future (yyyy-MM-dd):");

            // Validate the date format using a regex
            isValid = dateInput.matches("\\d{4}-\\d{2}-\\d{2}");
            if (!isValid) {
                io.print("Date must be in the format YYYY-MM-DD. Please try again.");
                continue;
            }

            // try to parse the date
            try {
                date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (date.isBefore(LocalDate.now())) {
                    io.print("Date must be today or in the future. Please try again.");
                    isValid = false;
                }
            } catch (DateTimeParseException e) {
                io.print("Invalid date. Please try again.");
                isValid = false;
            }
        } while (!isValid);

        return date;
    }

    // validate customer name (after empty check is already done)
    private String getCustomerNameWithValidation(String customerName) {
        while (!customerName.matches("[a-zA-Z0-9., ]+")) {
            io.print("Customer name must contain only letters, numbers, periods, or commas. Please try again.");
            customerName = io.readString("Please enter Customer Name:"); // prompt for input again
        }
        return customerName;  // return the valid customer name
    }

    // validate state
    private String getStateWithValidation(String state, Map<String, TaxState> stateList) {
        while (!stateList.containsKey(state)) {
            io.print("State not recognized. Please select from the available states.");
            state = io.readString("Please enter State:");
        }
        return state;
    }

    // validate product type
    private String getProductTypeWithValidation(String productType, Map<String, Product> productList) {
        while (!productList.containsKey(productType)) {
            io.print("Product type not recognized. Please select from the available products.");
            productType = io.readString("Please enter Product Type:");
        }
        return productType;
    }

    // validate area
    private boolean isValidArea(BigDecimal area) {
        return area.compareTo(BigDecimal.ZERO) > 0 && area.compareTo(new BigDecimal("100")) >= 0;  // check if area is valid (not neg and 100+)
    }

    // edit order
    public Map<String, String> getEditOrderInput(Order currentOrder, Map<String, Product> productList, Map<String, TaxState> stateList) {
        Map<String, String> updatedFields = new HashMap<>(); // used to store updated fields

        // get updated values from the user, allowing them to skip by pressing Enter
        String customerName = io.readString("Please enter Customer Name (press Enter to keep current: " + currentOrder.getCustomerName() + "):");
        if (!customerName.trim().isEmpty()) {
            updatedFields.put("customerName", getCustomerNameWithValidation(customerName)); // add to map
        }

        // show states and prompt for state edit
        displayStateList(stateList);
        String state = io.readString("Please enter State (press Enter to keep current: " + currentOrder.getStateId() + "):");
        if (!state.trim().isEmpty()) {
            updatedFields.put("stateId", getStateWithValidation(state, stateList)); // add to map
        }

        // show products and prompt for productType edit
        displayProductList(productList);
        String productType = io.readString("Please enter Product Type (press Enter to keep current: " + currentOrder.getProductType() + "):");
        if (!productType.trim().isEmpty()) {
            updatedFields.put("productType", getProductTypeWithValidation(productType, productList)); // add to map
        }

        // area Validation
        BigDecimal area = null;  // Initialize the area variable
        do {
            String areaInput = io.readString("Please enter Area (in sq ft):");
            if (areaInput.trim().isEmpty()) {
                area = null;
                break; // exit the loop if input is empty, meaning no edit to area
            }
            try {
                area = new BigDecimal(areaInput); // try to convert to BigDecimal
            } catch (NumberFormatException e) {
                io.print("Invalid input. Please enter a valid decimal number.");
                continue; // skip to the next iteration of the loop
            }
            if (!isValidArea(area)) {
                io.print("The area must be at least 100 sq ft and cannot be negative. Please try again.");
            }
        } while (area == null || !isValidArea(area));

        if (area != null) {
            updatedFields.put("area", area.toString()); // add to map if there is an edit made
        }
        return updatedFields; // return the map with all updated fields
    }

    public void displayRemoveResult(Order order) {
        if(order != null){
            io.print("Order successfully removed.");
        }else{
            io.print("No such order.");
        }
        io.print("Please hit enter to continue.");
    }

    // get user confirmation for edit order
    public boolean confirmChanges() {
        String response = io.readString("Do you want to save the changes? (yes/no):");
        return response.equalsIgnoreCase("yes");
    }

    // show updated fields from edit order
    public void displayUpdatedFields(Map<String, String> updatedFields, Order currentOrder) {
        io.print("Order Details:");

        // display customer name
        if (updatedFields.containsKey("customerName")) {
            io.print("Customer Name: " + updatedFields.get("customerName") + " (Updated)");
        } else {
            io.print("Customer Name: " + currentOrder.getCustomerName());
        }

        // display state
        if (updatedFields.containsKey("stateId")) {
            io.print("State: " + updatedFields.get("stateId") + " (Updated)");
        } else {
            io.print("State: " + currentOrder.getStateId());
        }

        // display product type
        if (updatedFields.containsKey("productType")) {
            io.print("Product Type: " + updatedFields.get("productType") + " (Updated)");
        } else {
            io.print("Product Type: " + currentOrder.getProductType());
        }

        // display area
        if (updatedFields.containsKey("area")) {
            io.print("Area: " + updatedFields.get("area") + " sq ft (Updated)");
        } else {
            io.print("Area: " + currentOrder.getArea() + " sq ft");
        }
    }

    // display calculated costs
    public void displayOrderCostDetails(Order order) {
        io.print("Material Cost: $" + order.getMaterialCost().setScale(2, RoundingMode.HALF_UP));
        io.print("Labour Cost: $" + order.getLabourCost().setScale(2, RoundingMode.HALF_UP));
        io.print("Tax: $" + order.getTaxTotal().setScale(2, RoundingMode.HALF_UP));
        io.print("Total: $" + order.getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    public void displayAddOrderMessage() {
        io.print("=== Add Order ===");
    }

    public void displayAddOrderSuccess(Order order) {
        io.print("Order (ID: "  + order.getOrderId() +") successfully added. Please hit enter to continue");
    }

    public void displayEditOrderMessage() {
        io.print("=== Edit Order ===");
    }

    public void displayEditOrderSuccess() {
        io.print("Order successfully edited.  Please hit enter to continue");
    }

    public void displayRemoveOrderMessage() {
        io.print("=== Remove Order ===");
    }

    public void displayChangesDiscarded() {
        io.print("Changes have been discarded.");
    }

    public void displayUnknownCommandMessage() {
        io.print("Unknown Command");
    }

    public void displayErrorMessage(String errorMessage) {
        io.print("=== ERROR ===");
        io.print(errorMessage);
    }

    public void displayExitMessage() {
        io.print("Thanks for stopping by Flooring Orders! \nDon't run into any potholes on your way back :)");
    }
}