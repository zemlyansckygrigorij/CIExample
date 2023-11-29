package com.example.ciexample.controller;

import com.example.ciexample.entity.Product;
import com.example.ciexample.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService service;
    @GetMapping()
    public List<Product> getAll() {
        return service.getAll();
    }
    @GetMapping("/{id}")
    public Product findById( @PathVariable(name = "id")int id) throws Exception {
        return service.findByIdOrDie(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody String name) throws Exception {
        return service.addProduct(name);
    }

    @DeleteMapping("/{id}")
    public List<Product>  deleteProductById(@PathVariable(name = "id")int id) throws Exception {
        return service.deleteProductById(id);
    }
    @PutMapping("/{id}")
    public Product putProduct(@PathVariable(name = "id")int id,String name) throws Exception {
        return service.putProduct(id,name);
    }
}
