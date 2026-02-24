package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService service;

    @Mock
    Model model;

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("test-id");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        when(service.create(any(Product.class))).thenReturn(product);

        String viewName = productController.createProductPost(product, model);
        assertEquals("redirect:list", viewName);
        verify(service, times(1)).create(product);
    }

    @Test
    void testProductListPage() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        when(service.findAll()).thenReturn(products);

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
        verify(model, times(1)).addAttribute("products", products);
        verify(service, times(1)).findAll();
    }

    @Test
    void testEditProductPage() {
        when(service.findById("test-id")).thenReturn(product);

        String viewName = productController.editProductPage("test-id", model);
        assertEquals("editProduct", viewName);
        verify(model, times(1)).addAttribute("product", product);
        verify(service, times(1)).findById("test-id");
    }

    @Test
    void testEditProductPost() {
        String viewName = productController.editProductPost(product);
        assertEquals("redirect:list", viewName);
        verify(service, times(1)).update("test-id", product);
    }

    @Test
    void testDeleteProduct() {
        String viewName = productController.deleteProduct("test-id");
        assertEquals("redirect:../list", viewName);
        verify(service, times(1)).delete("test-id");
    }
}
