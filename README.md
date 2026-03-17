# Vending Machine Simulator
## Table of contents
- [General info](#general-info)
- [Features](#features)
- [Project Structure](#project-structure)
- [Setup](#setup)
  - [Run the project](#run-the-project)
- [Example Output](#example-output)

## General info
Application simulating a drink vending machine. The system handles coin-based payments, tracks product stock levels, and collects transaction statistics across a configurable number of simulated customers.

Built as a learning project to practice core Java concepts.

## Features

- **Product management** — add drinks to the machine with a configurable storage limit
- **Coin system** — accepts various denominations with a tracked coin inventory
- **Customer simulation** — each customer randomly selects a product and inserts random coins with limited patience
- **Transaction statistics** — tracks success/failure counts, top 3 purchases, most common failure reason, and out-of-stock products

## Project Structure

```
vendingmachine/
├── Main.java                        — simulation entry point with sample data
├── VendingMachine.java              — core machine logic: storage, coins, transactions
├── TransactionStats.java            — tracks purchases, failures, and rankings
├── product/
│   ├── Drink.java                   — drink with name and price in cents
│   └── DrinkName.java               — enum: PEPSI / FANTA / SPRITE / COLA
└── exception/
    ├── OutOfStockException.java
    ├── InsufficientPaymentException.java
    └── NoChangeAvailableException.java
```

## Setup

To clone and run this application, you'll need [Git](https://git-scm.com) and [JDK 17+](https://www.oracle.com/java/technologies/downloads/) installed on your computer.
```bash
# Clone this repository
$ git clone https://github.com/pawlovskiii/vending-machine-simulator
# Go into the repository
$ cd vending-machine-simulator
```

### Run the project

```bash
# Compile all source files
$ javac src/vendingmachine/**/*.java src/vendingmachine/*.java
# Run the application
$ java -cp src vendingmachine.Main
```

## Example Output

```
Number of failed transactions: 325
Number of successful transactions: 675
Percentage of successful transactions: 67.5
Top 3 most bought products: [Drink{name=SPRITE, priceInCents=250}, Drink{name=PEPSI, priceInCents=350}, Drink{name=COLA, priceInCents=350}]
Most common exception: Insufficient payment
Final coin inventory: {10=296, 20=117, 50=266, 100=455, 200=802}
List of products out of stock: [Drink{name=SPRITE, priceInCents=250}, Drink{name=FANTA, priceInCents=400}]
```
