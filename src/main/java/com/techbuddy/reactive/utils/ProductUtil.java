package com.techbuddy.reactive.utils;

import com.techbuddy.reactive.dto.ProductDto;
import com.techbuddy.reactive.entity.Product;
import org.springframework.beans.BeanUtils;

public class ProductUtil {
    public static ProductDto entityToDto(Product product){
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product dtoToEntity(ProductDto productDto){
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
