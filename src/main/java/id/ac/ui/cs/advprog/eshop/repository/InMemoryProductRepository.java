package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public Product findById(String productId) {
        for (Product product : productData) {
            if (productId != null && productId.equals(product.getProductId())) {
                return product;
            }
        }
        return null;
    }

    @Override
    public Product update(String productId, Product product) {
        Product existingProduct = findById(productId);
        if (existingProduct == null) {
            return null;
        }
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductQuantity(product.getProductQuantity());
        return existingProduct;
    }

    @Override
    public void delete(String productId) {
        productData.removeIf(currentProduct -> productId != null && productId.equals(currentProduct.getProductId()));
    }
}
