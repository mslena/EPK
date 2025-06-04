package org.example.product;

import java.util.List;

public interface ProductService {
    String insertProduct(List<Product> product);
    String updateProduct(Product product);
    String deleteProduct(Product product);
    List<Product> getAllProduct();
    List<Product> getProductByArticle(String articleNumber);
    List<Product> getProductByManufacturer(String manufacturer);
}
