import java.util.Comparator; // import Comparator interface

public class ProductPriceComparator implements Comparator<Product> { // comparator that orders products by price
    @Override 
    public int compare(Product p1, Product p2) { // compare two products by price
        return Double.compare(p1.price(), p2.price()); // compare price components using Double.compare
    }
} 