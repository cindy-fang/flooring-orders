package org.example.flooringmastery.model;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.*;
import java.util.Objects;

public class Order {

    private static int nextOrderId = 0; // static field to track the next order ID
    private Integer orderId = 1;
    private LocalDate orderDate;
    private String customerName;
    private String productType;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal labourCost;
    private String stateId;
    private BigDecimal taxTotal;
    private BigDecimal total;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderId(), order.getOrderId()) && Objects.equals(getOrderDate(), order.getOrderDate()) && Objects.equals(getCustomerName(), order.getCustomerName()) && Objects.equals(getProductType(), order.getProductType()) && Objects.equals(getArea(), order.getArea()) && Objects.equals(getMaterialCost(), order.getMaterialCost()) && Objects.equals(getLabourCost(), order.getLabourCost()) && Objects.equals(getStateId(), order.getStateId()) && Objects.equals(getTaxTotal(), order.getTaxTotal()) && Objects.equals(getTotal(), order.getTotal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getOrderDate(), getCustomerName(), getProductType(), getArea(), getMaterialCost(), getLabourCost(), getStateId(), getTaxTotal(), getTotal());
    }

    // constructor
    public Order(String orderDate) {
        this.orderId = nextOrderId++; // Assign and increment the static orderId
        this.orderDate = stringToDate(orderDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", customerName='" + customerName + '\'' +
                ", productType='" + productType + '\'' +
                ", area=" + area +
                ", materialCost=" + materialCost +
                ", labourCost=" + labourCost +
                ", stateId='" + stateId + '\'' +
                ", taxTotal=" + taxTotal +
                ", total=" + total +
                '}';
    }

    // static methods to access anywhere
    public static Integer getNextOrderId() {
        return nextOrderId;
    }

    // convert LocalDate type to Sting type
    public static String dateToString(LocalDate date) {
        // define the desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    // convert String type to LocalDate type
    public static LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    // getters and setters
    public Integer getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return dateToString(orderDate);
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = stringToDate(orderDate);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}