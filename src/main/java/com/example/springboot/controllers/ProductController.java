package com.example.springboot.controllers;


import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository; //Ponto de injeção para termos acessos aos métodos JPA

    //Modelo de maturidade de Leonald Richerdson (4 níveis para que uma API seja considerada Restfull
    //0º nvl: API ultilize do protocolo Http
    //1º nvl: Recursos bem definidos e o correto uso das URIs
    //2º nvl: Ultilização dos métodos hhtps de forma semântica
    //Método POST
    @PostMapping("/products") // /products é a URI |
    public ResponseEntity<ProductModel> saveProduct (@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    //Método Get All
    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        List<ProductModel> productList = productRepository.findAll();
        if (!productList.isEmpty()){
            for (ProductModel product : productList){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    //Método Gell One
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id")UUID id){
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product note found.");
        }
        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    //Método UPDATE (Atualização)
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id")UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto){
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product note found.");
        }
        var productModel = productO.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    //Método DELETE
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id")UUID id){
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product note found.");
        }
        productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

    // 4 nvl: A aplicação deve apresentar hipermidias

}
