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

    public class FoodExpense extends Expense {
        private static ArrayList<FoodExpense> expenseList = new ArrayList<>();

        public FoodExpense(double amount) {
            super(amount);
            expenseList.add(this);
        }

        public static double getTotalAmount() {
            double total = 0;
            for (FoodExpense expense : expenseList) {
                total += expense.getAmount();
            }
            return total;
        }


        public static void clearAllExpenses(){

                expenseList.clear();
        }
    }

