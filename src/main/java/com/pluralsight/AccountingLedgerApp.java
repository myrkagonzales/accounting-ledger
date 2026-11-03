package com.pluralsight;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AccountingLedgerApp {
    // constant for the csv file to storage path
    private static final String FILE_NAME = "transactions.csv";

    // Date Format constant
    private static final DateTimeFormatter TIME_FORMAT =  DateTimeFormatter.ofPattern("HH:mm:ss");

    private static void displayLedgerMenu(Scanner scanner) {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println("\n=== LEDGER MENU === ");
            System.out.println("A) All Entries");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    displayTransactions();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    displayReports(scanner);
                    break;
                case "H":
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid choice.");


            }

        }
    }

    // display reports menu and handle report selection
    private static void displayReports(Scanner scanner) {
        boolean inReports = true;
        while (inReports) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1) Month to Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayMonthToDate();
                    break;
                case "2":
                    displayPreviousMonth();
                    break;
                case "3":
                    displayYearToDate();
                    break;
                case "4":
                    displayPreviousYear();
                    break;
                case "5":
                    searchByVendor(scanner);
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid option.");

            }
        }
    }

    private static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // split line by pipe character "|"
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    LocalTime time = LocalTime.parse(parts[1].trim());
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4].trim());
                    transactions.add(new Transaction(
                            date,
                            time,
                            description,
                            vendor,
                            amount));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }


    // main entry poin for the accounting ledger application

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
                    // Route user to ledger menu
                    displayLedgerMenu(scanner);
                    break;
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
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.println("Enter your choice: ");
    }

    private static void addDeposit(Scanner scanner) {
        System.out.println("\n--- ADD DEPOSIT ===");

        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());


        // capture automated system timestamp
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Instantiate transaction and save

        Transaction deposit = new Transaction(date, time, description, vendor, amount);
        saveTransaction(deposit);
    }

    private static void makePayment(Scanner scanner) {
        System.out.println("\n--- MAKE PAYMENT ---");

        System.out.print("Enter description: ");
        String paymentDescription = scanner.nextLine().trim();

        System.out.println("Enter vendor: ");
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
            System.out.println("Error reading transactions file: " + e.getMessage());


        }
    }

    // display all ledger entries newest first
    private static void displayTransactions() {
        List<Transaction> transactions = loadTransactions();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(transactions.get(i));
        }
    }

    // show deposits only with newest transactions first
    private static void displayDeposits() {
        List<Transaction> transactions = loadTransactions();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    // show payments only with newest transaction first
    private static void displayPayments() {
        List<Transaction> transactions = loadTransactions();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }

    }

    // search and display all transactions for a specific vendor
    private static void searchByVendor(Scanner scanner)
    {
        System.out.print("Enter vendor name: ");
        String searchByVendor = scanner.nextLine().trim();

        List<Transaction> transactions = loadTransactions();
        boolean found = false;
        for (int i = transactions.size() - 1; i >= 0; i--)
        {
            Transaction transaction = transactions.get(i);
            if (transaction.getVendor().equalsIgnoreCase(searchByVendor)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for vendor: " + searchByVendor);

        }

    }

    // display transactions from the current month
    private static void displayMonthToDate() {
        LocalDate today = LocalDate.now();
        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getMonth() == today.getMonth()
                    && transaction.getDate().getYear() == today.getYear()) {
                System.out.println(transaction);
            }
        }
    }

    // display transactions from the previous month
    private static void displayPreviousMonth()
    {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);

        List<Transaction> transactions = loadTransactions();

        for(int i = transactions.size() -1; i >= 0; i--)
        {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getMonth() == previousMonth.getMonth()
                    && transaction.getDate().getYear() == previousMonth.getYear())
            {
                System.out.println(transaction);
            }
        }
    }

    // display transactions from the current year
    private static void displayYearToDate()
    {
        int currentYear = LocalDate.now().getYear();

        List<Transaction> transactions = loadTransactions();

        for(int i = transactions.size() -1; i >= 0; i --)
        {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getYear() == currentYear)
            {
                System.out.println(transaction);
            }
        }
    }
    // display transactions from the previous year
    private static void displayPreviousYear()
    {
        int previousYear = LocalDate.now().minusYears(1).getYear();

        List<Transaction> transactions = loadTransactions();
        for (int i = transactions.size() -1; i >= 0; i--)
        {
            Transaction transaction = transactions.get(i);

            if(transaction.getDate().getYear() == previousYear)
            {
                System.out.println(transaction);
            }
        }
    }

}