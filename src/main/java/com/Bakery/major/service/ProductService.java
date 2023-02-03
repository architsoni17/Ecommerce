package com.Bakery.major.service;

import com.Bakery.major.repository.ProductRepository;
import com.Bakery.major.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;


    public void addProduct(Product product)
    {
        productRepository.save(product);
    }

    public List<Product> getAllProduct()
    {
        return productRepository.findAll();
    }

    public void removeProductById(long id)
    {
        productRepository.deleteById(id);
    }

    public Optional<Product> getProductById(long id)
    {
        return productRepository.findById(id);
    }
    public List<Product> getAllProductsByCategoryId(int id)
    {
        return productRepository.findAllByCategory_Id(id);
    }

}
