package com.pluralsight;
import java.time.LocalDate;
import java.time.LocalTime;


import java.time.LocalDateTime;

// transaction class representing a single financial record in the ledger.
// encapsulates transaction date, time, description, vendor, and dollar amount

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // constructor to initialize a complete transaction object.
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }


    public String toCsvRow() {
        return String.format("%s|%s|%s|%s|%.2f", date, time, description, vendor, amount);

    }
}