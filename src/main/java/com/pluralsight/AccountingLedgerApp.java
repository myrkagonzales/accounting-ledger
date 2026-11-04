package com.pluralsight;

import java.io.FileReader;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AccountingLedgerApp {

    // ======================================================
    // CONSTANTS & APPLICATION SETTINGS
    // ======================================================

    private static final String FILE_NAME = "transactions.csv";

    // ANSI Color Codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    // tracks selected language
    private static String language = "EN";

    // ======================================================
    // HELPER METHODS
    // ======================================================

    // returns English or spanish text based on selected language
    private static String getText(String english, String spanish) {
        if (language.equals("ES")) {
            return spanish;
        }
        return english;
    }

    // header helper
    private static void printHeader(String color, String english, String spanish) {
        System.out.println(color + "\n" + getText(english, spanish) + RESET);
    }

    // message helper
    private static void printTransactionSaved() {
        System.out.println(GREEN + getText("✓ Transaction successfully saved.", "✓ Transaccion guardada exitosamente.") + RESET);

    }

    private static void printInvalidOption() {
        System.out.println(getText("Invalid option.", "Opcion invalida."));
    }

    // ======================================================
    // APPLICATION ENTRY POINT
    // ======================================================

    // main entry point for the accounting ledger application

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // loop between home, ledger
        boolean running = true;

        System.out.println("=================================");
        System.out.println("Select Language / Seleccione Idioma");
        System.out.println("1) English");
        System.out.println("2) Espanol");
        System.out.print("Choice / Opcion: ");

        String languageChoice = scanner.nextLine();

        if (languageChoice.equals("2")) {
            language = "ES";
        }

        System.out.println(GREEN);
        System.out.println("Evergreen Financial Services");
        System.out.println(getText("Secure. Reliable. Professional", "Seguro, Confiable, Profesional"));
        System.out.println(RESET);

        System.out.println(BLUE);
        System.out.println("================================================");
        System.out.println("         EVERGREEN FINANCIAL SERVICES");
        System.out.println("              LEDGER SYSTEM");
        System.out.println("================================================");
        System.out.println(RESET);

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

                    System.out.println(GREEN);
                    System.out.println(getText("\nThank you for using Evergreen Financial Services.", "\nGracias por utlilizar Evergreen Financial Services."));
                    System.out.println(getText("Session ended successfully.", "Sesion finalizada exitosamente."));
                    System.out.println(RESET);

                    break;
                default:
                    printInvalidOption();
            }
        }
        scanner.close();
    }

    // ======================================================
    // HOME SCREEN
    // ======================================================

    // make the Home Screen menu options

    private static void displayHomeScreen() {

        System.out.println("\n" + getText("=== EVERGREEN HOME MENU ===", "=== MENU PRINCIPAL EVERGREEN ==="));
        System.out.println(getText("D) Add Deposit", "D) Agregar Deposito"));
        System.out.println(getText("P) Make Payment (Debit)", "P) Realizar Pago"));
        System.out.println(getText("L) Ledger", "L) Libro Mayor"));
        System.out.println(getText("X) Exit", "X) Salir"));

        System.out.println("\nSelect an option: ");
    }

    private static void addDeposit(Scanner scanner) {
        System.out.println(GREEN + "\n" + getText("===== DEPOSIT ENTRY =====", "===== REGISTRO DE DEPOSITO =====") + RESET);

        System.out.print(getText("Enter description: ", "Ingrese descripcion: "));
        String description = scanner.nextLine().trim();

        System.out.print(getText("Enter vendor: ", "Ingrese proveedor: "));

        String vendor = scanner.nextLine().trim();

        System.out.print(getText("Enter deposit amount: ", "Ingrese monto de depsosito: "));

        double amount = Double.parseDouble(scanner.nextLine().trim());


        // capture automated system timestamp
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Instantiate transaction and save

        Transaction deposit = new Transaction(date, time, description, vendor, amount);
        saveTransaction(deposit);
    }

    private static void makePayment(Scanner scanner) {
        System.out.println(RED + "\n" + getText("===== PAYMENT ENTRY =====", "===== REGISTRO DE PAGO =====") + RESET);

        System.out.print(getText("Enter description: ", "Ingrese descripcion: "));
        String paymentDescription = scanner.nextLine().trim();

        System.out.println(getText("Enter vendor: ", "Ingrese proveedor: "));
        String paymentVendor = scanner.nextLine().trim();

        System.out.print(getText("Enter amount: ", "Ingrese monto: "));
        double paymentAmount = Double.parseDouble(scanner.nextLine().trim());

        // convert payments to negative dollar value
        paymentAmount = -Math.abs(paymentAmount);

        // capture automated system timestamp
        LocalDate paymentDate = LocalDate.now();
        LocalTime paymentTime = LocalTime.now();

        // instantiate transaction and save
        Transaction payment = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
        saveTransaction(payment);
    }

    // ======================================================
    // FILE OPERATIONS
    // ======================================================


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
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }


    private static void saveTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transaction.toCsvRow());
            writer.newLine();

            printTransactionSaved();

        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());


        }
    }

    // ======================================================
    // LEDGER MENU
    // ======================================================

    private static void displayLedgerMenu(Scanner scanner) {
        boolean inLedger = true;

        while (inLedger) {

            displayBalance();

            System.out.println(GREEN + "\n" + getText("Welcome to Evergreen Financial Services", "Bienvenido a Evergreen Financial Services") + RESET);
            System.out.println(CYAN + "\n" + getText("========== EVERGREEN LEDGER ==========", "========== LIBRO MAYOR EVERGREEN ==========") + RESET);

            System.out.println(getText("A) All Entries", "A) Todas las Transacciones"));
            System.out.println(getText("D) Deposits Only", "D) Solo Depositos"));
            System.out.println(getText("P) Payments Only", "P) Solo Pagos"));
            System.out.println(getText("R) Reports", "R) Reportes"));
            System.out.println(getText("H) Home", "Inicio"

            ));
            System.out.print(getText("Enter choice: ", "Ingrese una opcion: "));

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
                    System.out.println(getText("Invalid choice.", "Opcion invalida."));


            }

        }
    }

    // display all ledger entries newest first
    private static void displayTransactions() {

        printHeader(CYAN, "===== ALL TRANSACTIONS =====", "===== TODAS LAS TRANSACCIONES =====");

        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(transactions.get(i));
        }
    }

    // show deposits only with newest transactions first
    private static void displayDeposits() {

        printHeader(CYAN, "===== DEPOSIT TRANSACTIONS =====", "===== TRANSACCIONES DE DEPOSITO =====");

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

        printHeader(CYAN, "===== PAYMENT TRANSACTIONS =====", "===== TRANSACCIONES DE PAGO =====");

        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }

    }

    // Calculate and display current account balance
    private static void displayBalance() {
        List<Transaction> transactions = loadTransactions();

        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        if (balance >= 0) {
            System.out.printf(GREEN + "\n" + getText("Current Account Balance: $%.2f\n", "Saldo Actual de la Cuenta: $%.2f\n") + RESET, balance);
        } else {
            System.out.printf(RED + "\n" + getText("Current Account Balance: $%.2f\n", "Saldo Actual de la Cuenta: $%.2f\n") + RESET, balance);
        }
        System.out.println(CYAN + getText("Total Transactions: ", "Total de Transacciones: ") + transactions.size() + RESET);

    }

    // ======================================================
    // REPORTS
    // ======================================================

    // display reports menu and handle report selection
    private static void displayReports(Scanner scanner) {
        boolean inReports = true;
        while (inReports) {
            System.out.println(BLUE + "\n" + getText("======= FINANCIAL REPORTS =======", "======= REPORTES FINANCIEROS =======") + RESET);
            System.out.println(getText("1) Month to Date", "1) Mes Actual"));
            System.out.println(getText("2) Previous Month", "2) Mes Anterior"));
            System.out.println(getText("3) Year to Date", "3) Ano Actual"));
            System.out.println(getText("4) Previous Year", "4) Ano Anterior"));
            System.out.println(getText("5) Search by Vendor", "5) Buscar por Proveedor"));
            System.out.println(getText("0) Back", "0) Regresar"));

            System.out.print(getText("Enter choice: ", "Ingrese una opcion: "));

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
                    System.out.println(getText("Invalid option.", "Opcion invalida."));

            }
        }
    }

    // search and display all transactions for a specific vendor
    private static void searchByVendor(Scanner scanner) {
        printHeader(CYAN, "===== VENDOR SEARCH =====", "===== BUSQUEDA DE PROVEEDOR =====");

        System.out.print(getText("Enter vendor name: ", "Ingrese nombre del proveedor: "));
        String searchByVendor = scanner.nextLine().trim();

        List<Transaction> transactions = loadTransactions();

        boolean found = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getVendor().equalsIgnoreCase(searchByVendor)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println(getText("No transactions found for vendor: " + searchByVendor, "No se encontraron transacciones para el proveedor: "));

        }

    }

    // display transactions from the current month
    private static void displayMonthToDate() {

        System.out.println(CYAN + "\n" + getText("===== MONTH TO DATE REPORT =====", "===== REPORTE DEL MES ACTUAL =====") + RESET);

        LocalDate today = LocalDate.now();

        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getMonth() == today.getMonth() && transaction.getDate().getYear() == today.getYear()) {
                System.out.println(transaction);
            }
        }
    }

    // display transactions from the previous month
    private static void displayPreviousMonth() {

        System.out.println(CYAN + "\n" + getText("===== PREVIOUS MONTH REPORT =====", "===== REPORTE DEL MES ANTERIOR =====") + RESET);

        LocalDate previousMonth = LocalDate.now().minusMonths(1);

        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getMonth() == previousMonth.getMonth() && transaction.getDate().getYear() == previousMonth.getYear()) {
                System.out.println(transaction);
            }
        }
    }

    // display transactions from the current year
    private static void displayYearToDate() {

        System.out.println(CYAN + "\n" + getText("===== YEAR TO DATE REPORT =====", "===== REPORTE DEL ANO ACTUAL =====") + RESET);

        int currentYear = LocalDate.now().getYear();

        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getYear() == currentYear) {
                System.out.println(transaction);
            }
        }
    }

    // display transactions from the previous year
    private static void displayPreviousYear() {

        System.out.println(CYAN + "\n" + getText("===== PREVIOUS YEAR REPORT =====", "===== REPORTE DEL ANO ANTERIOR =====") + RESET);

        int previousYear = LocalDate.now().minusYears(1).getYear();

        List<Transaction> transactions = loadTransactions();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getDate().getYear() == previousYear) {
                System.out.println(transaction);
            }
        }
    }
}
