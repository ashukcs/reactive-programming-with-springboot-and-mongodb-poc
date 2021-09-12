package com.techbuddy.reactive;

import com.techbuddy.reactive.controller.ProductController;
import com.techbuddy.reactive.dto.ProductDto;
import com.techbuddy.reactive.service.ProductService;
import javafx.beans.binding.When;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
class SpringBootReactiveMongodbCrudPocApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    public void addProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "Mobile", 1, 30000));
        when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post().uri("/products")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("102", "Mobile", 1, 30000),
                new ProductDto("103", "Laptop", 1, 50000));
        when(productService.getProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
				.exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("102", "Mobile", 1, 30000))
                .expectNext(new ProductDto("103", "Laptop", 1, 50000))
                .verifyComplete();
    }

    @Test
    public void getProductTest(){
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "Mobile", 1, 30000));
        when(productService.getProduct(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/products/102")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("102", "Mobile", 1, 30000))
                .verifyComplete();

    }

    @Test
    public void updateProduct(){
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "Mobile", 1, 30000));
        when(productService.updateProduct(productDtoMono, "102")).thenReturn(productDtoMono);

        webTestClient.put().uri("/products/102")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void deleteProduct(){
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "Mobile", 1, 30000));
        given(productService.deleteProduct(any())).willReturn(Mono.empty());
        webTestClient.delete().uri("/products/102")
                .exchange()
                .expectStatus()
                .isOk();

    }
}
