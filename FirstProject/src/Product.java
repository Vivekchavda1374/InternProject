// record defines an immutable Product type with compact syntax
public record Product(String productName, double price, double quantity, String category, long productId) { // declare record components and their types

    @Override // indicate this method overrides the generated toString
    public String toString() { // override built-in toString to tailor output
        return "productName='" + productName + '\'' + ", price=" + price + ", quantity=" + quantity + ", category='" + category + '\'' + ", productId=" + productId; // build and return formatted product string
    }
} 