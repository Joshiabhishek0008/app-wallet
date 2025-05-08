import java.util.*;

class User {
    private String username;
    private double balance;
    private List<Transaction> transactions;

    public User(String username) {
        this.username = username;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

class Transaction {
    private String fromUser;
    private String toUser;
    private double amount;
    private String type;
    private Date timestamp;

    public Transaction(String fromUser, String toUser, double amount, String type) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.type = type;
        this.timestamp = new Date();
    }

    public String toString() {
        return String.format("[%s] %s -> %s | %.2f | %s", timestamp, fromUser, toUser, amount, type);
    }
}

public class Main {
    private static Map<String, User> users = new HashMap<>();
    private static List<Transaction> transactionHistory = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            showMenu();
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> topUpWallet();
                case 3 -> transferMoney();
                case 4 -> checkBalance();
                case 5 -> viewAllTransactions();
                case 6 -> viewUserTransactions();
                case 0 -> System.out.println("Exiting Wallet System. Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private static void showMenu() {
        System.out.println("\n--- Digital Wallet System ---");
        System.out.println("1. Register a user");
        System.out.println("2. Top up money into your digital wallet");
        System.out.println("3. Transfer money to another wallet");
        System.out.println("4. Check balance");
        System.out.println("5. Check all transactions (global)");
        System.out.println("6. Check all transactions for a user");
        System.out.println("0. Exit");
        System.out.print("Please choose an option: ");
    }

    private static void registerUser() {
        System.out.print("Enter username to register: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
        } else {
            users.put(username, new User(username));
            System.out.println("User registered successfully.");
        }
    }

    private static void topUpWallet() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        User user = users.get(username);

        if (user == null) {
            System.out.println("User does not exist.");
            return;
        }

        System.out.print("Enter amount to top up: ");
        double amount = readPositiveAmount();
        user.addBalance(amount);

        Transaction t = new Transaction("System", username, amount, "Top-Up");
        user.addTransaction(t);
        transactionHistory.add(t);

        System.out.println("Wallet topped up successfully.");
    }

    private static void transferMoney() {
        System.out.print("Enter your username: ");
        String senderName = scanner.nextLine();
        User sender = users.get(senderName);

        if (sender == null) {
            System.out.println("Sender does not exist.");
            return;
        }

        System.out.print("Enter recipient username: ");
        String receiverName = scanner.nextLine();
        User receiver = users.get(receiverName);

        if (receiver == null) {
            System.out.println("Recipient does not exist.");
            return;
        }

        System.out.print("Enter amount to transfer: ");
        double amount = readPositiveAmount();

        if (sender.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return;
        }

        sender.deductBalance(amount);
        receiver.addBalance(amount);

        Transaction t = new Transaction(senderName, receiverName, amount, "Transfer");
        sender.addTransaction(t);
        receiver.addTransaction(t);
        transactionHistory.add(t);

        System.out.println("Transfer successful.");
    }

    private static void checkBalance() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        User user = users.get(username);

        if (user == null) {
            System.out.println("User does not exist.");
        } else {
            System.out.printf("Your balance is: %.2f\n", user.getBalance());
        }
    }

    private static void viewAllTransactions() {
        System.out.println("\n--- All Transactions ---");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            transactionHistory.forEach(System.out::println);
        }
    }

    private static void viewUserTransactions() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        User user = users.get(username);

        if (user == null) {
            System.out.println("User does not exist.");
            return;
        }

        System.out.printf("--- Transactions for %s ---\n", username);
        List<Transaction> userTxns = user.getTransactions();

        if (userTxns.isEmpty()) {
            System.out.println("No transactions.");
        } else {
            userTxns.forEach(System.out::println);
        }
    }

    private static double readPositiveAmount() {
        double amount = -1;
        while (amount <= 0) {
            while (!scanner.hasNextDouble()) {
                System.out.print("Please enter a valid amount: ");
                scanner.next();
            }
            amount = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            if (amount <= 0) {
                System.out.print("Amount must be greater than 0. Try again: ");
            }
        }
        return amount;
    }
}
