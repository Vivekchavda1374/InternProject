import java.util.List; // import List interface
import java.util.Scanner; // import Scanner for console input
import java.util.concurrent.atomic.AtomicBoolean; // import AtomicBoolean to coordinate loader/reporter

public class Main { 
    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in); // user input 

        ProductRepo productRepository = new ProductRepo(); // create product repository
        ProductSrvcs productService = new ProductSrvcs(productRepository); // create service layer backed by repository

        AtomicBoolean loaderDone = new AtomicBoolean(false); // flag to signal when loader has finished

        Runnable loader = () -> { // loader runnable that will add products to the repository
            List<Product> seed = List.of( // define seed products using List factory
                    new Product("ERP", 50, 3, "Software", 1), 
                    new Product("Product", 30, 1, "Hardware", 2), 
                    new Product("Java", 100, 5, "Software", 3), 
                    new Product("Book", 5000, 10, "Hardware", 4) 
            ); 
            seed.forEach(p -> { // add each product and repository's thread-safe add
                productRepository.addProduct(p); // add product to repository
                try { // try to sleep to simulate staggered loading
                    Thread.sleep(200); 
                } catch (InterruptedException e) { // catch interruption
                    Thread.currentThread().interrupt(); // restore interrupt status
                } 
            });
            loaderDone.set(true); // mark loader as done after adding all products
        }; 

        Runnable reporter = () -> { // reporter runnable that periodically computes totals and prints a report
            while (!loaderDone.get()) { // while loader hasn't finished yet
                List<Product> snap = productService.getAllProductList(); // take a snapshot of current products
                double total = snap.stream().mapToDouble(p -> p.price() * p.quantity()).sum(); // compute total inventory value using streams
                System.out.println("--- Inventory Report ---\nTotal inventory value: " + total + "\nItems: " + snap.size()); // print a report
                try { // sleep between reports to avoid busy-waiting
                    Thread.sleep(500); // report every 500ms
                } catch (InterruptedException e) { // handle interruption
                    Thread.currentThread().interrupt(); // restore interrupt status
                    return; 
                } 
            } 
            List<Product> finalSnap = productService.getAllProductList(); // take final snapshot after loader finished
            double finalTotal = finalSnap.stream().mapToDouble(p -> p.price() * p.quantity()).sum(); // compute final total
            System.out.println("--- Final Inventory Report ---\nTotal inventory value: " + finalTotal + "\nItems: " + finalSnap.size()); // print final report
        };

        Thread loaderThread = new Thread(loader, "Loader"); // create loader thread
        Thread reporterThread = new Thread(reporter, "Reporter"); // create reporter thread
        loaderThread.start(); // start loader thread
        reporterThread.start(); // start reporter thread

        while (true) { // main loop for user interaction
            System.out.println("\n1. Display All Products"); 
            System.out.println("2. Search by ID"); 
            System.out.println("3. Sort by Price");
            System.out.println("4. Sort by Name"); 
            System.out.println("5. Filter by Category"); 
            System.out.println("6. Exit"); 
            System.out.print("Choose an option: "); 

            int option = scanner.nextInt(); // read user's choice
            scanner.nextLine(); // consume the remaining newline

            switch (option) { // dispatch based on chosen option
                case 1: // choice: display all
                    displayAllProducts(productService); // invoke display helper
                    break; 
                case 2: // choice: search by id
                    System.out.print("Enter Product ID to search: "); 
                    long productId = scanner.nextLong(); // read id
                    scanner.nextLine(); // consume newline
                    searchProductById(productService, productId); // perform search
                    break; 
                case 3: // choice: sort by price
                    sortProductsByPrice(productService); // perform sort and display
                    break; 
                case 4: // choice: sort by name
                    sortProductsByName(productService); // perform sort and display
                    break; 
                case 5: // choice: filter by category
                    System.out.print("Enter category to filter by: ");
                    String category = scanner.nextLine(); 
                    filterProductsByCategory(productService, category); // perform filter and display
                    break; 
                case 6: // choice: exit program
                    System.out.println("Exiting..."); // notify exit
                    scanner.close(); // close scanner resource
                    return; 
                default: // invalid choice falls here
                    System.out.println("Invalid option. Try again."); // notify invalid input
            } 
        } 
    } 
    private static void displayAllProducts(ProductSrvcs productService) { // helper to display all products 
        List<Product> products = productService.getAllProductList(); // get snapshot from service
        if (products.isEmpty()) { // if no products
            System.out.println("No products available."); // notify empty
        } else { // otherwise
            products.forEach(p -> System.out.println(p)); // print each product 
        } 
    } 

    private static void searchProductById(ProductSrvcs productService, long productId) { // helper to search by id
        Product product = productService.searchProductById(productId); // delegate to service
        if (product != null) { // if found
            System.out.println(product); // print product
        } else { // if not found
            System.out.println("Product not found."); // notify not found
        } 
    } 

    private static void sortProductsByPrice(ProductSrvcs productService) { // helper to sort by price and display
        List<Product> products = productService.sortByPrice(); // get sorted list
        products.forEach(System.out::println); // print each product using method reference
    } 

    private static void sortProductsByName(ProductSrvcs productService) { // helper to sort by name and display
        List<Product> products = productService.sortProductByName(); // get sorted list
        products.forEach(System.out::println); // print each product using method reference
    } 

    private static void filterProductsByCategory(ProductSrvcs productService, String category) { // helper to filter and display
        List<Product> products = productService.filterProductByCategory(category); // get filtered list
        if (products.isEmpty()) { // if none found
            System.out.println("No products found in this category."); // notify empty
        } else { // otherwise
            products.forEach(System.out::println); // print each matching product
        }
    } 
} 