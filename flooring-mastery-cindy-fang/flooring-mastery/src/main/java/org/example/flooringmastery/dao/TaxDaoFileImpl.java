package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.TaxState;

import java.io.*;
import java.math.*;
import java.util.*;

public class TaxDaoFileImpl implements TaxDao {
    public static final String DELIMITER = ",";
    private final String STATE_FILE_PATH;
    //public static final String STATE_FILE_PATH = "flooring-mastery/Taxes.txt";

    private Map<String, TaxState> allStates = new HashMap<>();

    public TaxDaoFileImpl(){
        STATE_FILE_PATH = "flooring-mastery/Taxes.txt";
    }

    public TaxDaoFileImpl(String taxesFilePath){
        STATE_FILE_PATH = taxesFilePath;
    }

    // not used yet
    @Override
    public TaxState addStateTax(String stateId, TaxState state) throws PersistenceException {
        loadFile();
        if (allStates.containsKey(stateId)) {
            throw new PersistenceException("State already exists: " + stateId);
        }
        TaxState newState = allStates.put(stateId, state);
        writeFile();
        return newState;
    }

    // get all states and taxes from hashmap
    @Override
    public Map<String, TaxState> getAllStateTaxes() throws PersistenceException {
        loadFile();
        return new HashMap<>(allStates);
    }

    // not used yet
    @Override
    public TaxState getState(String stateId) throws PersistenceException {
        loadFile();
        if (!allStates.containsKey(stateId)) {
            throw new PersistenceException("State not found: " + stateId);
        }
        return allStates.get(stateId);
    }

    // get state tax
    @Override
    public TaxState getStateTaxRate(String stateId) throws PersistenceException {
        TaxState state = allStates.get(stateId);
        return state;
    }

    // not used yet
    @Override
    public TaxState removeState(String stateId) throws PersistenceException {
        loadFile();
        if (!allStates.containsKey(stateId)) {
            throw new PersistenceException("State not found: " + stateId);
        }
        TaxState removedStudent = allStates.remove(stateId);
        writeFile();
        return removedStudent;
    }

    // load file from taxes.txt
    private void loadFile() throws PersistenceException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(STATE_FILE_PATH)));
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                TaxState currentState = unmarshallState(currentLine);
                allStates.put(currentState.getStateId(), currentState);
            }
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load state data into memory.", e);
        } catch (Exception e) {
            throw new PersistenceException("An error occurred while loading states.", e);
        } finally {
            if (scanner != null) {
                scanner.close(); // ensure scanner is closed to prevent data leaks
            }
        }
    }

    // unmarshall
    private TaxState unmarshallState(String stateAsText) {
        String[] stateTokens = stateAsText.split(DELIMITER);
        if (stateTokens.length != 3) {
            throw new PersistenceException("Invalid state data: " + stateAsText);
        }
        String stateId = stateTokens[0].trim();
        TaxState stateFromFile = new TaxState(stateId);
        String stateName = stateTokens[1].trim();
        String token2String = stateTokens[2].trim();

        try {
            BigDecimal taxRate = new BigDecimal(token2String);
            stateFromFile.setStateName(stateName);
            stateFromFile.setStateTaxRate(taxRate);
        } catch (NumberFormatException e) {
            throw new PersistenceException("Invalid number format for tax rate in state data: " + stateAsText, e);
        }
        return stateFromFile;
    }

    // marshall (not used yet)
    private String marshallState(TaxState aState) {
        return aState.getStateId() + DELIMITER +
                aState.getStateName() + DELIMITER +
                aState.getStateTaxRate();
    }

    // not used yet
    private void writeFile() throws PersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(STATE_FILE_PATH));
            Map<String, TaxState> stateList = this.getAllStateTaxes();
            for (TaxState currentState : stateList.values()) {
                String stateAsText = marshallState(currentState);
                out.println(stateAsText);
                out.flush();
            }
        } catch (IOException e) {
            throw new PersistenceException("Could not save tax data.", e);
        } catch (Exception e) {
            throw new PersistenceException("An error occurred while saving states.", e);
        } finally {
            if (out != null) {
                out.close(); // Ensure PrintWriter is closed to prevent resource leaks
            }
        }
    }
}
