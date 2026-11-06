package ui;

import dao.UserDao;
import model.User;

import java.util.Scanner;

/** Entry menu: login, register, exit. Routes each role to its own dashboard. */
public class LoginMenu {

    private final Scanner scanner;
    private final UserDao userDao;

    public LoginMenu(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void show() {
        while (true) {
            System.out.println("\n===== USER MANAGEMENT SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register (creates a STUDENT account, pending approval)");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> login();
                case "2" -> register();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please choose 0, 1, or 2.");
            }
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = userDao.login(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        if (!user.status().equalsIgnoreCase("APPROVED")) {
            System.out.println("Your account is " + user.status() + ". Please wait for an admin to approve it.");
            return;
        }

        System.out.println("Welcome, " + user.username() + "! (" + user.roles() + ")");
        if (user.roles().equalsIgnoreCase("ADMIN")) {
            new AdminMenu(scanner, userDao, user).show();
        } else {
            new UserMenu(scanner, user).show();
        }
    }

    private void register() {
        System.out.print("Choose a username (no spaces, 0 to cancel): ");
        String username = scanner.nextLine().trim();
        if (username.equals("0")) {
            System.out.println("Registration cancelled.");
            return;
        }
        if (username.isEmpty() || username.contains(" ")) {
            System.out.println("Username cannot be empty or contain spaces.");
            return;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("That username is already taken.");
            return;
        }

        System.out.print("Choose a password (at least 4 characters): ");
        String password = scanner.nextLine().trim();
        if (password.length() < 4) {
            System.out.println("Password is too short.");
            return;
        }

        if (userDao.register(username, password)) {
            System.out.println("Registered! Your account is PENDING until an admin approves it.");
        } else {
            System.out.println("Registration failed.");
        }
    }
}
