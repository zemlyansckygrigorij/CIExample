package com.example.ciexample.service;

import com.example.ciexample.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    private List<Product> list = new ArrayList<>();
    public ProductService(){
        list.add(new Product(1,"Bean"));
        list.add(new Product(2,"Potato"));
        list.add(new Product(3,"Onion"));
        list.add(new Product(4,"Milk"));
        list.add(new Product(5,"mint"));
        list.add(new Product(6,"Pea"));
        list.add(new Product(7,"Apple"));
        list.add(new Product(8,"Avocado"));
        list.add(new Product(9,"Orange"));
    }

    public List<Product> getAll(){
        return list;
    }
    public Product findByIdOrDie(int id) throws Exception {
        return list.stream().filter(it->it.getId()== id).findFirst().orElseThrow(() ->new Exception());
    }
    public Product addProduct(String name) throws Exception {
        int id = list.stream().max((Comparator.comparing(Product::getId))).orElseThrow(Exception::new).getId()+1;
        Product product = new Product(id,name);
        list.add(product);
        return product;
    }

    public List<Product>  deleteProductById(int id) throws Exception {
        Product product =   findByIdOrDie(id);
        list.remove(product);
        return list;
    }
    public Product putProduct(int id,String name) throws Exception {
        Product product = findByIdOrDie(id);
        product.setName(name);
        return product;
    }
}
