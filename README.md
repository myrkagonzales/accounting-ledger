# Evergreen Financial Ledger System
## Overview
Evergreen Financial Ledger System is a Java command-line application that allows users to manage financial transactions by recording deposits and payments, reviewing account activity, and generating financial reports.

All transaction data is stored in a CSV file and can be viewed through an interactive ledger system.

## Features
### Transaction Management
- Add Deposits
- Record Payments (Debits)
- Automatically Save Transactions
- Automatically Load Existing Transactions
### Ledger Features
- View All Transactions
- View Deposits Only
- View Payments Only
- Display Newest Transactions First
### Financial Reports
- Month-To-Date Report
- Previous Month Report
- Year-To-Date Report
- Previous Year Report
- Search Transactions By Vendor
### Additional Enhancements
- English and Spanish Language Support
- Evergreen Financial Services Branding
- Color-Coded Console Interface
- Running Account Balance Display
- Transaction Counter
- Professional Report Headers
### Technologies Used
- Java
- IntelliJ IDEA
- CSV File Storage
- File I/O
- Collections (ArrayList)
- LocalDate API
- LocalTime API
### Project Structure
AccountingLedgerApp.java
Transaction.java
transactions.csv
README.md
### How To Run
1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Run AccountingLedgerApp.java
4. Select a language
5. Follow the menu prompts
## Supported Languages
### English
- Add Deposits
- Make Payments
- Ledger Navigation
- Financial Reporting
### Español
- Agregar Depósitos
- Realizar Pagos
- Navegación de Libro Mayor
- Reportes Financieros
## Interesting Feature
One feature I implemented beyond the original project requirements is bilingual language support.

The application allows users to choose either English or Spanish when the program starts. A reusable helper method dynamically displays text based on the selected language, reducing duplicate code and making the application easier to maintain.

Example:
```
private static String getText(String english, String spanish)
{
    if(language.equals("ES"))
    {
        return spanish;
    }

      return english;
}
```
## Future Improvements
### Code Refactoring
For future development, I would like to redesign the application classes to better separate responsibilites.
This would reduce the size of the main application class and improve maintainability by following the Single Responsibility Principle (SRP)
## Author
Myrka Gonzales

Year Up United Capstone Project
