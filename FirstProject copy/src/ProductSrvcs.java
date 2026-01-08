import java.util.Comparator;
import java.util.List;

public class ProductSrvcs { // service layer for product operations
    private final ProductRepo productRepo; // repository dependency

    public ProductSrvcs(ProductRepo productRepo) { // constructor receives a repository
        this.productRepo = productRepo; // assign repository reference
    }
    public void addProduct(Product product) { // add a product via service layer
        productRepo.addProduct(product); // delegate to repository's synchronized add
    } 
    public List<Product> getAllProductList() { // get all products snapshot
        return productRepo.getAllProductList(); // delegate to repository
    }
    public Product searchProductById(long productId) { // search by id using repository
        return productRepo.getProductById(productId); // delegate lookup
    }
    public List<Product> sortByPrice() { // return products sorted by price
        List<Product> products = productRepo.getAllProductList(); // get snapshot
        products.sort(Comparator.comparingDouble(Product::price)); // sort in-place by price using method reference
        return products;
    } 
    public List<Product> sortProductByName() { // return products sorted by name 
        List<Product> products = productRepo.getAllProductList(); // get snapshot
        products.sort(Comparator.comparing(Product::productName, String.CASE_INSENSITIVE_ORDER)); // sort by name ignoring case
        return products;
    }
    public List<Product> filterProductByCategory(String category) { // filter by category via repository method
        return productRepo.filterProductByCategory(category); // delegate filtering
    } 
} 
