package vendingmachine;

import vendingmachine.exception.InsufficientPaymentException;
import vendingmachine.exception.NoChangeAvailableException;
import vendingmachine.exception.OutOfStockException;
import vendingmachine.product.Drink;
import vendingmachine.product.DrinkName;

import java.util.Map;

class Main {

    private static final int CLIENTS_NUMBER = 1000;

    public static void main(String[] args) {

        VendingMachine vendingMachine = new VendingMachine(1000);
        TransactionStats stats = vendingMachine.getTransactionStats();

        Drink cola = new Drink(DrinkName.COLA, 350);
        Drink pepsi = new Drink(DrinkName.PEPSI, 350);
        Drink fanta = new Drink(DrinkName.FANTA, 400);
        Drink sprite = new Drink(DrinkName.SPRITE, 250);

        vendingMachine.addDrink(cola, 200);
        vendingMachine.addDrink(fanta, 100);
        vendingMachine.addDrink(sprite, 200);
        vendingMachine.addDrink(pepsi, 100);
        vendingMachine.addDrink(pepsi, 100);

        vendingMachine.addCoin(200, 5);
        vendingMachine.addCoin(100, 5);
        vendingMachine.addCoin(50, 5);
        vendingMachine.addCoin(20, 50);
        vendingMachine.addCoin(10, 50);

        for (int i = 0; i < CLIENTS_NUMBER; i++) {
            try {

                Drink drink = vendingMachine.selectRandomProduct();
                Map<Integer, Integer> insertedCoins = vendingMachine.insertCoins(drink);

                Map<Integer, Integer> givenChange = vendingMachine.processTransaction(drink, insertedCoins);

                stats.recordSuccessfulTransaction();
                stats.recordPurchase(drink);
            } catch (OutOfStockException e) {
                stats.recordOutOfStockException();
            } catch (InsufficientPaymentException e) {
                stats.recordInsufficientPaymentException();
            } catch (NoChangeAvailableException e) {
                stats.recordNoChangeException();
            }
        }

        int failedTransactions = stats.countFailedTransactions();
        System.out.printf("Number of failed transactions: %s%n", failedTransactions);

        int successfulTransactions = stats.countSuccessfulTransactions();
        System.out.printf("Number of successful transactions: %s%n", successfulTransactions);

        double successfulPercentageOfTransactions = ((double) successfulTransactions / CLIENTS_NUMBER) * 100;
        System.out.printf("Percentage of successful transactions: %s%n", successfulPercentageOfTransactions);

        System.out.printf("Top 3 most bought products: %s%n", stats.getTop3Purchases());

        System.out.printf("Most common exception: %s%n", stats.getMostCommonFailureReason());
        System.out.printf("Final coin inventory: %s%n", vendingMachine.getCoinWarehouse());

        System.out.printf("List of products out of stock: %s%n", vendingMachine.getOutOfStockDrinks());
    }
}
