/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java2budgetproject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Natalee Bui
 */

public class RentExpense extends Expense {
    private static ArrayList<RentExpense> expenseList = new ArrayList<>();
    
    public RentExpense(double amount) {
        super(amount);
        expenseList.add(this);
    }
    
    public static double getTotalAmount() {
        double total = 0;
        for (RentExpense expense : expenseList) {
            total += expense.getAmount();
        }
        return total;
    }

    public static void clearAllExpenses(){
            
            expenseList.clear();
    }
}