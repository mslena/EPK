package org.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ProductService")
public class ProductServiceImpl implements ProductService{

    private ProductDao productDao;

    @Autowired
    public void setTypeProductDao(ProductDao productDao) {this.productDao = productDao;}

    @Override
    @Transactional(readOnly = false)
    public String insertProduct(List<Product> product) {
        String logMessage = "";
        for(Product entry : product){
            productDao.insertProduct(entry);
            logMessage = "Запись со значениями ProductId = " + entry.getId() + " Name =" + entry.getName() + " успешно добавлена";
        }
        return logMessage;
    }

    @Override
    public String updateProduct(Product product) {
        productDao.updateProduct(product);
        String logMessage = "Запись со значениями ProductId = " + product.getId() + " Name =" + product.getName()  + " успешно обновлена";
        return logMessage;
    }

    @Override
    public String deleteProduct(Product product) {
        productDao.deleteProduct(product);
        String logMessage = "Запись со значениями ProductId = " + product.getId() + " успешно удалена";
        return logMessage;
    }

    @Override
    public List<Product> getAllProduct() {
        return productDao.getAllProduct();
    }

    @Override
    public List<Product> getProductByArticle(String articleNumber) {
        return productDao.getProductByArticle(articleNumber);
    }

    @Override
    public List<Product> getProductByManufacturer(String manufacturer) {
        return productDao.getProductByManufacturer(manufacturer);
    }
}
