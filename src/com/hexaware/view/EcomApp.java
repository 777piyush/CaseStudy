package com.hexaware.view;

import java.util.Scanner;

import com.hexaware.controller.Controller;
import com.hexaware.exception.CustomerNotFoundException;
import com.hexaware.exception.ProductNotFoundException;

/**
 * The main class representing the Ecommerce Application.
 */
public class EcomApp {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main method to execute the Ecommerce Application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Controller con = new Controller();

        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    con.registerCustomer();
                    break;
                case 2:
                    con.createProduct();
                    break;
                case 3:
                    con.deleteProduct();
                    break;
                case 4:
				try {
					con.addToCart();
				} catch (CustomerNotFoundException | ProductNotFoundException e) {
					e.printStackTrace();
				}
                    break;
                case 5:
				try {
					con.viewCart();
				} catch (Exception e) {
					e.printStackTrace();
				}
                    break;
                case 6:
                    con.placeOrder();
                    break;
                case 7:
                    con.displayOrdersByCustomer();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
        System.out.println("Thanks for using our system");
    }

    /**
     * Displays the menu options for the Ecommerce Application.
     */
    private static void displayMenu() {
        System.out.println("Ecommerce Application Menu:");
        System.out.println("1. Register Customer");
        System.out.println("2. Create Product");
        System.out.println("3. Delete Product");
        System.out.println("4. Add to Cart");
        System.out.println("5. View Cart");
        System.out.println("6. Place Order");
        System.out.println("7. View Customer Order");
        System.out.println("8. Exit");
        System.out.println("Choose an option:");
    }
}
