import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ProductRepo productRepository = new ProductRepo();
        ProductSrvcs productService = new ProductSrvcs(productRepository);
        productRepository.addProduct(new Product("ERP",50,3 , "Software",1));
        productRepository.addProduct(new Product("Product",30,1 , "Hardware",2));
        productRepository.addProduct(new Product("Java",100,5 , "Software",3));
        productRepository.addProduct(new Product("Book",5000,10 , "Hardware",4));



        while (true) {
            System.out.println("\n1. Display All Products");
            System.out.println("2. Search by ID");
            System.out.println("3. Sort by Price");
            System.out.println("4. Sort by Name");
            System.out.println("5. Filter by Category");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    displayAllProducts(productService);
                    break;
                case 2:
                    System.out.print("Enter Product ID to search: ");
                    long productId = scanner.nextLong();
                    scanner.nextLine();
                    searchProductById(productService, productId);
                    break;
                case 3:
                    sortProductsByPrice(productService);
                    break;
                case 4:
                    sortProductsByName(productService);
                    break;
                case 5:
                    System.out.print("Enter category to filter by: ");
                    String category = scanner.nextLine();
                    filterProductsByCategory(productService, category);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void displayAllProducts(ProductSrvcs productService) {
        List<Product> products = productService.getAllProductList();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    private static void searchProductById(ProductSrvcs productService, long productId) {
        Product product = productService.searchProductById(productId);
        if (product != null) {
            System.out.println(product);
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void sortProductsByPrice(ProductSrvcs productService) {
        List<Product> products = productService.sortByPrice();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void sortProductsByName(ProductSrvcs productService) {
        List<Product> products = productService.sortProductByName();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void filterProductsByCategory(ProductSrvcs productService, String category) {
        List<Product> products = productService.filterProductByCategory(category);
        if (products.isEmpty()) {
            System.out.println("No products found in this category.");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
}
