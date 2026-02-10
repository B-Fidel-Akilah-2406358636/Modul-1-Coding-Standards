package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateAndFindAll() {
        // Setup mock behavior
        when(productRepository.create(product)).thenReturn(product);
        when(productRepository.findAll()).thenReturn(List.of(product).iterator());


        productService.create(product);
        List<Product> productList = productService.findAll();

        // verifikasi
        assertFalse(productList.isEmpty());
        Product savedProduct = productList.get(0);
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        verify(productRepository, times(1)).create(product);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Setup mock agar repository mengembalikan iterator berisi produk kita
        when(productRepository.findAll()).thenReturn(List.of(product).iterator());


        Product foundProduct = productService.findById(product.getProductId());

        // verifikasi
        assertNotNull(foundProduct);
        assertEquals(product.getProductId(), foundProduct.getProductId());
    }

    @Test
    void testFindByIdNotFound() {
        // Setup mock agar repository mengembalikan iterator kosong
        when(productRepository.findAll()).thenReturn(new ArrayList<Product>().iterator());


        Product foundProduct = productService.findById("random-id");

        // verifikasi
        assertNull(foundProduct);
    }

    @Test
    void testUpdateProduct() {
        // Setup mock
        when(productRepository.findAll()).thenReturn(List.of(product).iterator());

        // data baru untuk update
        Product newProductData = new Product();
        newProductData.setProductName("Sampo Cap Usep");
        newProductData.setProductQuantity(50);

        // Exercise
        productService.update(product.getProductId(), newProductData);

        // Verify bahwa data produk asli telah berubah
        assertEquals("Sampo Cap Usep", product.getProductName());
        assertEquals(50, product.getProductQuantity());
    }

    @Test
    void testDeleteProduct() {

        productService.delete(product.getProductId());

        // Verify bahwa repository.delete dipanggil 1 kali
        verify(productRepository, times(1)).delete(product.getProductId());
    }
}