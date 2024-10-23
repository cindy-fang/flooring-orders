package org.example.flooringmastery.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class TaxDaoFileImplTest {

    TaxDao taxDaoTest;

    public TaxDaoFileImplTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
        String testFile = "testTax.txt";
        new FileWriter(testFile);
        taxDaoTest = new TaxDaoFileImpl(testFile);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addStateTax() {
    }

    @Test
    void getAllStateTaxes() {
    }

    @Test
    void getState() {
    }

    @Test
    void getStateTaxRate() {
    }

    @Test
    void removeState() {
    }
}