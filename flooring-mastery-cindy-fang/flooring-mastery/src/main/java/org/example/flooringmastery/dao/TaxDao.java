package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.TaxState;
import java.util.*;

public interface TaxDao {

    TaxState getStateTaxRate(String stateId)
            throws PersistenceException;

    TaxState addStateTax(String stateId,  TaxState state)
            throws PersistenceException;

    Map<String, TaxState> getAllStateTaxes()
            throws PersistenceException;

    TaxState getState(String stateId)
            throws PersistenceException;

    TaxState removeState(String stateId)
            throws PersistenceException;
}
