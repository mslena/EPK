package org.example.product;

import java.util.List;

public interface ProductDao {
    void insertProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Product product);
    List<Product> getAllProduct();
    List<Product> getProductByArticle(String articleNumber);
    List<Product> getProductByManufacturer(String manufacturer);
}
