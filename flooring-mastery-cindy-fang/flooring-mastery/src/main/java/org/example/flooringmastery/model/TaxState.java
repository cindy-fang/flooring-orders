package org.example.flooringmastery.model;

import java.math.BigDecimal;

public class TaxState {
    private String stateId;
    private String stateName;
    private BigDecimal taxRate;

    // constructor
    public TaxState(String stateId) {
        this.stateId = stateId;
    }

    // getters and setters
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }
    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public BigDecimal getStateTaxRate() {
        return taxRate;
    }
    public void setStateTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}