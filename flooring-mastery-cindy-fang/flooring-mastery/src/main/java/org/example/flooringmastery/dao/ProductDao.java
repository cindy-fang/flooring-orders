package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.Product;
import java.util.*;


public interface ProductDao {
    Product addProduct(String productId, Product product)
            throws PersistenceException;

    Map<String, Product> getAllProducts()
            throws PersistenceException;

    Product getProduct(String productId)
            throws PersistenceException;

    Product removeProduct(String productId)
            throws PersistenceException;
}