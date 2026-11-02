package com.pluralsight;

import java.util.Scanner;

public class AccountingLedgerApp {

    // main application entry point for Acoounting, Ledger and exit states Application


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // loop between home, ledger
        boolean running = true;

        // core application loop: continues executing until user selects 'X' (exit)
        while (running) {
            displayHomeScreen();
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    // ROUTE TO ADD DEPOSIT METHOD
                    System.out.println("-> Add Deposit selected.");
                    break;
                case "P":
                    // Route to make payment method
                    System.out.println("-> Make Payment selected.");
                    break;
                case "L":
                    // Route to make ledger screen
                    System.out.println("-> Leeger screen selected.");
                case "X":
                    running = false;
                    System.out.println("Exiting application. Thank you!");
                    break;
                default:
                    System.out.println("Invalid option. Please select D, P, L, or X.");
            }
        }
        scanner.close();
    }

    // make the Home Screen menu options

    private static void displayHomeScreen() {
        System.out.println("\n=== Accounts Ledger Home ===");
        System.out.println("D) Add Deposit");
        System.out.println("P Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.println("Enter your choice: ");
    }
}



