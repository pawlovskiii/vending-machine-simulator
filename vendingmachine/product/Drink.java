package vendingmachine.product;



public class Drink {

    private final DrinkName name;
    private final int priceInCents;

    public Drink(DrinkName name, int priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public DrinkName getName() {
        return name;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "name=" + name +
                ", priceInCents=" + priceInCents +
                '}';
    }
}
