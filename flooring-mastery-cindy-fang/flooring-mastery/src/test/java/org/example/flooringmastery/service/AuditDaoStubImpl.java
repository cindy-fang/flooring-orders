package org.example.flooringmastery.service;

import org.example.flooringmastery.dao.*;
import org.example.flooringmastery.model.*;

import java.util.*;

public class AuditDaoStubImpl implements AuditDao{
    private List<String> auditEntries = new ArrayList<>();

    // Method to write action entry to the audit list
    @Override
    public void writeAuditEntry(String entry) throws PersistenceException {
        auditEntries.add(entry);
    }

    // Method to get all audit entries for testing purposes
    public List<String> getAuditEntries() {
        return new ArrayList<>(auditEntries);
    }
}
