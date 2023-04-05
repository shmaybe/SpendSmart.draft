/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java2budgetproject;

import java.util.ArrayList;

/**
 *
 * @author Natalee Bui
 */
public class Expense {

    protected double amount;
    protected ArrayList<Expense> expenses;

    public Expense(double amount) {
        this.amount = amount;
        this.expenses = new ArrayList<>();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public double getAmount() {
        return amount;
    }

    public boolean isEmpty() {
        return expenses.isEmpty();
    }

    public void addAllExpenses(ArrayList<Expense> expenses) {
        for (Expense expense : expenses) {
            addExpense(expense);
        }
    }

    

}
