import java.util.Comparator; // import Comparator interface

public class ProductNameComparator implements Comparator<Product> { // comparator that orders products by name
    @Override 
    public int compare(Product p1, Product p2) { // compare two products by name ignoring case
        return p1.productName().compareToIgnoreCase(p2.productName()); // compare productName components ignoring case
    } 
}