package org.example.flooringmastery.service;

import org.example.flooringmastery.dao.*;
import org.example.flooringmastery.model.*;

import java.util.*;
import java.math.*;

public class TaxDaoStubImpl implements TaxDao {

    private final TaxState onlyState; // Stub state

    public TaxDaoStubImpl() {
        // Initialize the onlyState with test data
        onlyState = new TaxState("TX");
        onlyState.setStateName("Texas");
        onlyState.setStateTaxRate(new BigDecimal("4.45")); // 8% tax rate
    }

    @Override
    public TaxState addStateTax(String stateId, TaxState state) throws PersistenceException {
        // Simulate adding a state tax; return onlyState if it's the same ID
        if (stateId.equals(onlyState.getStateId())) {
            return onlyState; // Simulating that the state is added
        } else {
            return null; // Simulating that state does not exist
        }
    }

    @Override
    public Map<String, TaxState> getAllStateTaxes() throws PersistenceException {
        // Stub returns only one state in a map
        Map<String, TaxState> stateList = new HashMap<>();
        stateList.put(onlyState.getStateId(), onlyState);
        return stateList;
    }

    @Override
    public TaxState getState(String stateId) throws PersistenceException {
        // Return onlyState if the ID matches
        if (stateId.equals(onlyState.getStateId())) {
            return onlyState;
        } else {
            return null; // Simulate that the state was not found
        }
    }

    @Override
    public TaxState getStateTaxRate(String stateId) throws PersistenceException {
        // Return onlyState's tax rate if the ID matches
        if (stateId.equals(onlyState.getStateId())) {
            return onlyState;
        } else {
            return null; // Simulate that the state tax rate was not found
        }
    }

    @Override
    public TaxState removeState(String stateId) throws PersistenceException {
        // Simulate removing the state
        if (stateId.equals(onlyState.getStateId())) {
            return onlyState; // Simulate return of removed state
        } else {
            return null; // Simulate that state was not found for removal
        }
    }
}
