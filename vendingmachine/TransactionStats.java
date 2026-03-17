package vendingmachine;

import vendingmachine.product.Drink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TransactionStats {

    private int successfulTransactions;
    private final Map<Drink, Integer> drinkPurchaseCount = new HashMap<>();
    private final Map<String, Integer> failureReasons = new HashMap<>();


    String getMostCommonFailureReason() {
        return failureReasons.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Not found");
    }

    List<Drink> getTop3Purchases() {
        return drinkPurchaseCount.entrySet().stream()
                .sorted(Map.Entry.<Drink, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();
    }

    void recordFailure(String error) {
        failureReasons.merge(error, 1, Integer::sum);
    }

    void recordPurchase(Drink drink) {
        drinkPurchaseCount.merge(drink, 1, Integer::sum);
    }

    void recordSuccessfulTransaction() {
        successfulTransactions++;
    }

    void recordOutOfStockException() {
        recordFailure("Out of stock");
    }

    void recordNoChangeException() {
        recordFailure("No change");
    }

    void recordInsufficientPaymentException() {
        recordFailure("Insufficient payment");
    }

    int countFailedTransactions() {
        return failureReasons.values().stream()
                .reduce(Integer::sum)
                .orElse(0);
    }

    int countSuccessfulTransactions() {
        return successfulTransactions;
    }
}
