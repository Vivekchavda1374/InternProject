import java.util.ArrayList;
import java.util.List;

public class ProductRepo {
    private List<Product> productList;
    public ProductRepo(){
        this.productList = new ArrayList<>();
    }

    public void addProduct(Product product){
        productList.add(product);
    }
    public List<Product> getAllProductList(){
        return new ArrayList<>(productList);
    }
    public Product getProductById(long productId){
        for (Product product : productList) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }
    public List<Product> filterProductByCategory(String category){
        List<Product> filterProduct = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filterProduct.add(product);
            }
        }
        return filterProduct;

    }




}
