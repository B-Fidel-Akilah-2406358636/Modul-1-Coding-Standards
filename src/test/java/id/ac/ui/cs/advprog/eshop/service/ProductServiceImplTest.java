package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(productRepository.findById(product.getProductId())).thenReturn(product);

        Product foundProduct = productService.findById(product.getProductId());

        assertNotNull(foundProduct);
        assertEquals(product.getProductId(), foundProduct.getProductId());
        verify(productRepository, times(1)).findById(product.getProductId());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById("random-id")).thenReturn(null);

        Product foundProduct = productService.findById("random-id");

        assertNull(foundProduct);
        verify(productRepository, times(1)).findById("random-id");
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.update(eq(product.getProductId()), any(Product.class))).thenAnswer(invocation -> {
            Product newData = invocation.getArgument(1);
            product.setProductName(newData.getProductName());
            product.setProductQuantity(newData.getProductQuantity());
            return product;
        });

        Product newProductData = new Product();
        newProductData.setProductName("Sampo Cap Usep");
        newProductData.setProductQuantity(50);

        productService.update(product.getProductId(), newProductData);

        assertEquals("Sampo Cap Usep", product.getProductName());
        assertEquals(50, product.getProductQuantity());
        verify(productRepository, times(1)).update(product.getProductId(), newProductData);
    }

    @Test
    void testDeleteProduct() {

        productService.delete(product.getProductId());

        // Verify bahwa repository.delete dipanggil 1 kali
        verify(productRepository, times(1)).delete(product.getProductId());
    }

    @Test
    void testCreateProductWithNullId() {
        // Product tanpa ID (null) -> harus di-generate UUID
        Product newProduct = new Product();
        newProduct.setProductName("Sampo Cap Usep");
        newProduct.setProductQuantity(50);

        when(productRepository.create(newProduct)).thenReturn(newProduct);

        Product createdProduct = productService.create(newProduct);

        assertNotNull(createdProduct.getProductId());
        verify(productRepository, times(1)).create(newProduct);
    }

    @Test
    void testUpdateProductNotFound() {
        when(productRepository.update(eq("random-id"), any(Product.class))).thenReturn(null);

        Product newProductData = new Product();
        newProductData.setProductName("Sampo Cap Usep");
        newProductData.setProductQuantity(50);

        productService.update("random-id", newProductData);

        verify(productRepository, times(1)).update("random-id", newProductData);
    }
}