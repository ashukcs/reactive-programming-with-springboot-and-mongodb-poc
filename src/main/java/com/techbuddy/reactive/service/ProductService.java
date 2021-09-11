package com.techbuddy.reactive.service;

import com.techbuddy.reactive.dto.ProductDto;
import com.techbuddy.reactive.entity.Product;
import com.techbuddy.reactive.repository.ProductRepository;
import com.techbuddy.reactive.utils.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> getProducts(){
        return productRepository.findAll().map(ProductUtil::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id){
        return productRepository.findById(id).map(ProductUtil::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max){
        return productRepository.findByPriceBetween(Range.closed(min,max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(ProductUtil::dtoToEntity).flatMap(productRepository::insert).map(ProductUtil::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return productRepository.findById(id).flatMap(p->productDtoMono.map(ProductUtil::dtoToEntity)
                .doOnNext(e->e.setId(id))
                .flatMap(productRepository::save))
                .map(ProductUtil::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return productRepository.deleteById(id);
    }
}
