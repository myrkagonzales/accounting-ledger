package com.pluralsight;
import java.io.FileReader;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AccountingLedgerApp {
    // constant for the csv file to storage path
    private static final String FILE_NAME = "transactions.csv";

    private static void displayLedgerMenu(Scanner scanner) {

        while (inLedger) {
            System.out.println("\n=== Ledger Menu === ");
            System.out.println("A) All Entries");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();
        }
    }


            private static List<Transaction> loadTransactions() {
                List<Transaction> transactions = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while (line = reader.readLine() != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    // split line by pipe character "|"
                    String [] parts = line.split("\\|");
                    if (parts.length == 5) {
                        LocalDate date = LocalDate.parse(parts[0].trim());
                        LocalTime time = LocalTime.parse(parts[1].trim());
                        String description = parts[2].trim();
                        String vendor = parts[3].trim();
                        double amount = Double.parseDouble(parts[4].trim());
                        transactions.add(new Transaction((date, time, description, vendor, amount));
                    }
                }

            switch (choice) {
                case "A":
                    displayTransactions();
                    break;
            }






        }

    }

    // main application entry point for Accounting, Ledger and exit states Application

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
                    // Route user to add deposit screen
                    addDeposit(scanner);
                    break;
                case "P":
                    // Route user to make payment screen
                    makePayment(scanner);
                    break;
                case "L":
                    // Route to user to ledger menue
                    displayLedgerMenu(scanner);
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

        private static void addDeposit(Scanner scanner) {
            System.out.println("\n--- ADD DEPOSIT ===");

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter vendor/payor: ");
            String vendor = scanner.nextLine().trim();

            System.out.print("Enter deposit amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            // convert payments to negative dollar value
            amount = -Math.abs(amount);

            // capture automated system timestamp
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();

            // Instantiate transaction and save

            Transaction deposit = new Transaction(date, time, description, vendor, amount);
            saveTransaction(deposit);
        }

            private static void makePayment(Scanner scanner){
                System.out.println("\n--- MAKE PAYMENT ---");

                System.out.print("Enter description: ");
                String paymentDescription = scanner.nextLine().trim();

                System.out.println("Enter vendor/payee: ");
                String paymentVendor = scanner.nextLine().trim();

                System.out.print("Enter amount: ");
                double paymentAmount = Double.parseDouble(scanner.nextLine().trim());

                // convert payments to negative dollar value
                paymentAmount = -Math.abs(paymentAmount);

                // capture automated system timestamp
                LocalDate paymentDate = LocalDate.now();
                LocalTime paymentTime = LocalTime.now();

                // instantiate transaction and save
                Transaction payment = new Transaction(
                        paymentDate,
                        paymentTime,
                        paymentDescription,
                        paymentVendor,
                        paymentAmount
                );
                saveTransaction(payment);
            }
        private static void saveTransaction(Transaction transaction) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(transaction.toCsvRow());
                writer.newLine();
                System.out.println("✓ Success: Transaction saved to " + FILE_NAME);
            } catch (IOException e) {
                System.out.println("❌ Unable to write transaction to file: " + e.getMessage());
            }
        }
    }
