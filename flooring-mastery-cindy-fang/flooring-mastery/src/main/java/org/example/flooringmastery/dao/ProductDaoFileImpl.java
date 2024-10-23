package org.example.flooringmastery.dao;

import org.example.flooringmastery.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoFileImpl implements  ProductDao {

    public static final String DELIMITER = ",";
    private final String PRODUCT_FILE_PATH;
    //public static final String PRODUCT_FILE_PATH = "flooring-mastery/Products.txt";

    private Map<String, Product> allProducts = new HashMap<>();

    public ProductDaoFileImpl () {
        PRODUCT_FILE_PATH = "flooring-mastery/Products.txt";
    }

    // many instances of products.txt can exist to separate production data from test data
    public ProductDaoFileImpl (String productTextFile) {
        PRODUCT_FILE_PATH = productTextFile;
    }
    // not used yet
    @Override
    public Product addProduct(String productType, Product product) throws PersistenceException {
        loadFile();
        if (allProducts.containsKey(productType)) {
            throw new PersistenceException("Product already exists: " + productType);
        }
        Product newProduct = allProducts.put(productType, product);
        writeFile();
        return newProduct;  // Return the product that was added
    }

    // get all products from hashmap
    @Override
    public Map<String, Product> getAllProducts() throws PersistenceException {
        loadFile();
        return new HashMap<>(allProducts);// Specify type
    }

    // get product
    @Override
    public Product getProduct(String productType) throws PersistenceException {
        loadFile();
        if (!allProducts.containsKey(productType)) {
            throw new PersistenceException("Product not found: " + productType);
        }
        return allProducts.get(productType);
    }

    // not used yet
    @Override
    public Product removeProduct(String productType) throws PersistenceException {
        loadFile();
        if (!allProducts.containsKey(productType)) {
            throw new PersistenceException("Product not found: " + productType);
        }
        Product removedProduct = allProducts.remove(productType);
        writeFile();
        return removedProduct;
    }

    // load products.txt file into hashmap
    private void loadFile() throws PersistenceException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE_PATH)));
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Product currentProduct = unmarshallProduct(currentLine);
                allProducts.put(currentProduct.getProductType(), currentProduct);
            }
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load Product data into memory.", e);
        } catch (Exception e) {
            throw new PersistenceException("An error occurred while loading products.", e);
        } finally {
            if (scanner != null) {
                scanner.close(); // ensure scanner is closed to prevent data leaks
            }
        }
    }

    // unmarshall line from products.txt
    private Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        if (productTokens.length != 3) {
            throw new PersistenceException("Invalid product data: " + productAsText);
        }
        String productType = productTokens[0];

        // Initialize Product object with the product type
        Product productFromFile = new Product(productType);
        try {
            String token1String = productTokens[1].trim();
            String token2String = productTokens[2].trim();
            BigDecimal costPerSquareFoot = new BigDecimal(token1String);
            BigDecimal labourCostPerSquareFoot = new BigDecimal(token2String);

            // Set the cost per square foot and labour cost per square foot
            productFromFile.setCostPerSquareFoot(costPerSquareFoot);
            productFromFile.setLabourCostPerSquareFoot(labourCostPerSquareFoot);
        } catch (NumberFormatException e) {
            throw new PersistenceException("Invalid number format in product data: " + productAsText, e);
        }
        return productFromFile;
    }

    // marshall product (not used yet)
    private String marshallProduct(Product aProduct) {
        return aProduct.getProductType() + DELIMITER +
                aProduct.getCostPerSquareFoot() + DELIMITER +
                aProduct.getLabourCostPerSquareFoot();
    }

    // not used yet
    private void writeFile() throws PersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(PRODUCT_FILE_PATH));
            Map<String, Product> productList = this.getAllProducts();
            for (Product currentProduct : productList.values()) {
                String productAsText = marshallProduct(currentProduct);
                out.println(productAsText);
                out.flush();
            }
        } catch (IOException e) {
            throw new PersistenceException("Could not save product data.", e);
        } catch (Exception e) {
            throw new PersistenceException("An error occurred while saving products.", e);
        } finally {
            if (out != null) {
                out.close(); // Ensure PrintWriter is closed to prevent resource leaks
            }
        }
    }
}