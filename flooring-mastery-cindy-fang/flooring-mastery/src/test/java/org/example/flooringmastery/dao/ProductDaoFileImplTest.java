package org.example.flooringmastery.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoFileImplTest {

    ProductDao productDaoTest;

    public ProductDaoFileImplTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
        String testFile = "testProduct.txt";
        new FileWriter(testFile);
        productDaoTest = new ProductDaoFileImpl(testFile);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void addProduct() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getProduct() {
    }

    @Test
    void removeProduct() {
    }
}