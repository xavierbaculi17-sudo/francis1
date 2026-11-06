package ui;

import model.User;

import java.util.Scanner;

/** Simple dashboard for STUDENT and TEACHER accounts. */
public class UserMenu {

    private final Scanner scanner;
    private final User user;

    public UserMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        this.user = user;
    }

    public void show() {
        while (true) {
            System.out.println("\n===== " + user.roles() + " MENU (" + user.username() + ") =====");
            System.out.println("1. View my profile");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {
                    System.out.println("\n--- My Profile ---");
                    System.out.println("ID       : " + user.id());
                    System.out.println("Username : " + user.username());
                    System.out.println("Role     : " + user.roles());
                    System.out.println("Status   : " + user.status());
                }
                case "0" -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option. Please choose 0 or 1.");
            }
        }
    }
}
