package org.example.product.typeProduct;

import java.util.List;

public interface TypeProductDao {
    void insertTypeProduct(TypeProduct product);
    void updateTypeProduct(TypeProduct product);
    void deleteTypeProduct(TypeProduct product);
    List<TypeProduct> getAllTypeProduct();
   TypeProduct findById(int id);
}
