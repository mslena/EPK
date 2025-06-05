package org.example.product.typeProduct;

import java.util.List;

public interface TypeProductService {
    String insertTypeProduct(List<TypeProduct> product);
    String updateTypeProduct(TypeProduct product);
    String deleteTypeProduct(TypeProduct product);
    List<TypeProduct> getAllTypeProduct();
    TypeProduct getTypeProductById(int id);
}
