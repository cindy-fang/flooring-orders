package org.example.flooringmastery.service;

import org.example.flooringmastery.dao.PersistenceException;
import org.example.flooringmastery.dao.ProductDao;
import org.example.flooringmastery.model.*;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoStubImpl implements ProductDao {

    private final Product onlyProduct;

    // Default constructor initializes with a predefined product
    public ProductDaoStubImpl() {
        this.onlyProduct = new Product("Wood");
        this.onlyProduct.setCostPerSquareFoot(new BigDecimal("5.15"));
        this.onlyProduct.setLabourCostPerSquareFoot(new BigDecimal("4.75"));
    }

    // Constructor to inject custom test product
    public ProductDaoStubImpl(Product testProduct) {
        this.onlyProduct = testProduct;
    }

    @Override
    public Product addProduct(String productType, Product product) throws PersistenceException {
        // Since this is a stub, adding a product won't actually modify anything
        if (productType.equals(onlyProduct.getProductType())) {
            return onlyProduct;
        } else {
            return null; // Or throw an exception depending on how you want to handle it
        }
    }

    @Override
    public Map<String, Product> getAllProducts() throws PersistenceException {
        // Stub returns only one product in a map
        Map<String, Product> productList = new HashMap<>();
        productList.put(onlyProduct.getProductType(), onlyProduct);
        return productList;
    }

    @Override
    public Product getProduct(String productType) throws PersistenceException {
        // Return only the predefined product
        if (productType.equals(onlyProduct.getProductType())) {
            return onlyProduct;
        } else {
            return null; // Or throw an exception if not found
        }
    }

    @Override
    public Product removeProduct(String productType) throws PersistenceException {
        // Since this is a stub, the product won't actually be removed
        if (productType.equals(onlyProduct.getProductType())) {
            return onlyProduct;
        } else {
            return null; // Or throw an exception
        }
    }
}
