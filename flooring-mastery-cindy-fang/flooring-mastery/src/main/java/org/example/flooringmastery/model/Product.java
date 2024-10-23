package org.example.flooringmastery.model;

import java.math.BigDecimal;

public class Product {
    private String productType;
    private BigDecimal labourCostPerSquareFoot;
    private BigDecimal costPerSquareFoot;

    // constructor
    public Product(String productId) {
        this.productType = productId;
    }

    // getters and setters
    public String getProductType() {
        return productType;
    }

    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot) {
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }
}