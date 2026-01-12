import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 

public class ProductRepo { // repository holding products 
    private final List<Product> productList; // internal list storing products
    public ProductRepo() { // constructor initializes internal list
        this.productList = new ArrayList<>(); // create an ArrayList for storage
    } 
    public synchronized void addProduct(Product product) { // synchronized to ensure thread-safe adds
        productList.add(product); // add the given product to the internal list
    } 
    public synchronized List<Product> getAllProductList() { // synchronized snapshot method
        return new ArrayList<>(productList); // return a defensive copy of the internal list
    } 
    public synchronized Product getProductById(long productId) { // synchronized lookup by id
        return productList.stream() // create a stream over products
                .filter(p -> p.productId() == productId) // filter products matching the id
                .findFirst() // find the first match if any
                .orElse(null); // return null when no product found
    } 
    public synchronized List<Product> filterProductByCategory(String category) { // synchronized filter by category
        return productList.stream() // stream over products
                .filter(p -> p.category().equalsIgnoreCase(category)) // keep products that match category ignoring case
                .collect(Collectors.toList()); // collect and return matching products
    } 
} 