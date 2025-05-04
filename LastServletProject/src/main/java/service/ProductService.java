// service/ProductService.java
package service;

import model.Product;

import java.util.*;

public class ProductService {
    private static final Map<Integer, Product> products = new HashMap<>();
    private static int currentId = 1;

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Product findById(int id) {
        return products.get(id);
    }

    public void save(Product product) {
        product.setId(currentId++);
        products.put(product.getId(), product);
    }

    public boolean update(Product updatedProduct) {
        if (products.containsKey(updatedProduct.getId())) {
            products.put(updatedProduct.getId(), updatedProduct); // Update the product in the map
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if (products.containsKey(id)) {
            products.remove(id); // Remove the product by its ID
            return true;
        }
        return false;
    }
}
