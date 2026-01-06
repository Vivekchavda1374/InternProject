import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductSrvcs {
    private ProductRepo productRepo;
    public ProductSrvcs(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    public void addProduct(Product product){
        productRepo.addProduct(product);

    }
    public List<Product> getAllProductList(){
        return  productRepo.getAllProductList();
    }
    public Product searchProductById(long productId) {
        return productRepo.getProductById(productId);
    }
    public List<Product> sortByPrice(){
        List<Product> products = productRepo.getAllProductList();
        products.sort(new ProductPriceComparator());
        return products;

    }
    public List<Product> sortProductByName(){
        List<Product> products = productRepo.getAllProductList();
        products.sort(new ProductNameComparator());
        return products;
    }
    public List<Product> filterProductByCategory(String category){
        return productRepo.filterProductByCategory(category);

    }




}
