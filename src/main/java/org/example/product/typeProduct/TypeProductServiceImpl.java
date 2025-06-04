package org.example.product.typeProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("TypeProductService")
public class TypeProductServiceImpl implements TypeProductService{

    private TypeProductDao typeProductDao;

    @Autowired
    public void setTypeProductDao(TypeProductDao typeProductDao) {this.typeProductDao = typeProductDao;}

    @Override
    @Transactional(readOnly = false)
    public String insertTypeProduct(List<TypeProduct> typeProduct) {
        String logMessage = "";
        for(TypeProduct entry : typeProduct){
            typeProductDao.insertTypeProduct(entry);
            logMessage = "Запись со значениями TypeProductId = " + entry.getId() + " Name =" + entry.getName() + " успешно добавлена";
        }
        return logMessage;
    }

    @Override
    public String updateTypeProduct(TypeProduct product) {
        typeProductDao.updateTypeProduct(product);
        String logMessage = "Запись со значениями TypeProductId = " + product.getId() + " Name =" + product.getName()  + " успешно обновлена";
        return logMessage;
    }

    @Override
    public String deleteTypeProduct(TypeProduct product) {
        typeProductDao.deleteTypeProduct(product);
        String logMessage = "Запись со значениями TypeProductId = " + product.getId() + " успешно удалена";
        return logMessage;
    }

    @Override
    public List<TypeProduct> getAllTypeProduct() {
        return typeProductDao.getAllTypeProduct();
    }

    @Override
    public TypeProduct getTypeProductById(Long id) {
        return typeProductDao.findById(id);
    }
}
