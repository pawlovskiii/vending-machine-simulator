package vendingmachine;

import vendingmachine.exception.InsufficientPaymentException;
import vendingmachine.exception.NoChangeAvailableException;
import vendingmachine.exception.OutOfStockException;
import vendingmachine.product.Drink;
import vendingmachine.product.DrinkName;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

class VendingMachine {

    private final static Random RANDOM = new Random();
    private final static int PATIENCE = 6;

    private final Map<Drink, Integer> drinkStorage = new HashMap<>();
    private final int maxStorage;
    private int currentStorage = 0;
    private final Map<Integer, Integer> coinWarehouse = new TreeMap<>(Comparator.reverseOrder());
    private final TransactionStats transactionStats = new TransactionStats();

    VendingMachine(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    TransactionStats getTransactionStats() {
        return transactionStats;
    }

    List<Drink> getOutOfStockDrinks() {
        return drinkStorage.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .toList();
    }

    Map<Integer, Integer> getCoinWarehouse() {
        return Map.copyOf(coinWarehouse);
    }

    List<DrinkName> getDrinkNames() {
        return drinkStorage.keySet().stream()
                .map(Drink::getName)
                .toList();
    }

    List<Integer> getAvailableDenominations() {
        return coinWarehouse.keySet().stream()
                .toList();
    }

    void addDrink(Drink drink, int quantity) {
        if (currentStorage + quantity <= maxStorage) {
            currentStorage += quantity;
            drinkStorage.merge(drink, quantity, Integer::sum);
        } else {
            throw new ArithmeticException("Current storage is full. Cannot add more drinks.");
        }
    }

    void addCoin(int denominationInCents, int quantity) {
        coinWarehouse.merge(denominationInCents, quantity, Integer::sum);
    }

    Drink findDrink(DrinkName name) {
        return drinkStorage.entrySet().stream()
                .filter(entry ->
                        entry.getKey().getName().equals(name) && entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new OutOfStockException("Given drink is out of stock."));
    }

    Drink selectRandomProduct() {
        List<DrinkName> drinkNames = this.getDrinkNames();
        DrinkName name = drinkNames.get(RANDOM.nextInt(0, drinkNames.size()));

        return findDrink(name);
    }

    Map<Integer, Integer> insertCoins(Drink drink) {
        int totalInserted = 0;
        Map<Integer, Integer> insertedCoins = new TreeMap<>(Comparator.reverseOrder());
        List<Integer> availableDenominations = this.getAvailableDenominations();
        for (int i = 0; i < PATIENCE; i++) {
            int randomDenomination = availableDenominations.get(RANDOM.nextInt(0, availableDenominations.size()));
            totalInserted += randomDenomination;
            insertedCoins.merge(randomDenomination, 1, Integer::sum);
            if (totalInserted >= drink.getPriceInCents()) {
                break;
            }
        }
        if (totalInserted >= drink.getPriceInCents()) {
            return insertedCoins;
        } else {
            throw new InsufficientPaymentException("Insufficient payment (cancellation).");
        }
    }

    Map<Integer, Integer> processTransaction(Drink drink, Map<Integer, Integer> insertedCoins) {
        int remainingAmount = calculateInsertedCoins(insertedCoins) - drink.getPriceInCents();
        Map<Integer, Integer> finalChange = calculateChange(remainingAmount);
        drinkStorage.computeIfPresent(drink, (k, v) -> v - 1);
        insertedCoins.forEach((key, value) -> coinWarehouse.merge(key, value, Integer::sum));
        finalChange.forEach((key, value) -> coinWarehouse.merge(key, value, (oldValue, newValue) -> oldValue - newValue));
        return finalChange;
    }

    private Map<Integer, Integer> calculateChange(int remainingAmount) {
        Map<Integer, Integer> changeBreakdown = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<Integer, Integer> entry : this.coinWarehouse.entrySet()) {
            int denomination = entry.getKey();
            int availableCount = entry.getValue();
            int coinsToUse = Math.min(remainingAmount / denomination, availableCount);
            if (coinsToUse != 0) {
                changeBreakdown.put(denomination, coinsToUse);
                remainingAmount -= denomination * coinsToUse;
            }
        }
        if (remainingAmount == 0) {
            return changeBreakdown;
        } else {
            throw new NoChangeAvailableException("No change available.");
        }
    }

    private int calculateInsertedCoins(Map<Integer, Integer> insertCoins) {
        return insertCoins.entrySet().stream()
                .mapToInt(entry -> entry.getKey() * entry.getValue())
                .sum();
    }

    @Override
    public String toString() {
        return "VendingMachine{" +
                ", coinWarehouse=" + coinWarehouse +
                '}';
    }
}
