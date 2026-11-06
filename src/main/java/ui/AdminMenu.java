package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** Admin dashboard: view users and update a user's role/status with full input validation. */
public class AdminMenu {

    private static final Set<String> VALID_ROLES = Set.of("STUDENT", "TEACHER", "ADMIN");
    private static final Set<String> VALID_STATUSES = Set.of("APPROVED", "PENDING");

    private final Scanner scanner;
    private final UserDao userDao;
    private final User currentAdmin;

    public AdminMenu(Scanner scanner, UserDao userDao, User currentAdmin) {
        this.scanner = scanner;
        this.userDao = userDao;
        this.currentAdmin = currentAdmin;
    }

    public void show() {
        while (true) {
            System.out.println("\n===== ADMIN MENU (" + currentAdmin.username() + ") =====");
            System.out.println("1. View all users");
            System.out.println("2. Update User");
            System.out.println("0. Logout");
            int choice = readIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> displayUsers(userDao.getAllUsers());
                case 2 -> updateUser();
                case 0 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option. Please choose 0, 1, or 2.");
            }
        }
    }

    // ---------------- Input validation wrappers ----------------

    /** Re-prompts until a whole number is typed; letters/symbols never crash the app. */
    public int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numbers only.");
            }
        }
    }

    /** Keeps asking until a valid role is entered. Returns "0" if the admin cancels. */
    public String readValidRole() {
        while (true) {
            System.out.print("Enter new role (STUDENT / TEACHER / ADMIN, 0 to cancel): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) {
                return "0";
            }
            if (VALID_ROLES.contains(input)) {
                return input;
            }
            System.out.println("Invalid role. Valid roles: STUDENT, TEACHER, ADMIN.");
        }
    }

    /** Keeps asking until a valid status is entered. Returns "0" if the admin cancels. */
    public String readValidStatus() {
        while (true) {
            System.out.print("Enter new status (APPROVED / PENDING, 0 to cancel): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) {
                return "0";
            }
            if (VALID_STATUSES.contains(input)) {
                return input;
            }
            System.out.println("Invalid status. Valid statuses: APPROVED, PENDING.");
        }
    }

    // ---------------- Features ----------------

    private void displayUsers(List<User> users) {
        String line = "+------+----------------------+------------+------------+";
        System.out.println("\n" + line);
        System.out.printf("| %-4s | %-20s | %-10s | %-10s |%n", "ID", "Username", "Role", "Status");
        System.out.println(line);
        for (User u : users) {
            System.out.printf("| %-4d | %-20s | %-10s | %-10s |%n",
                    u.id(), u.username(), u.roles(), u.status());
        }
        System.out.println(line);
    }

    private void updateUser() {
        List<User> users = userDao.getAllUsers();
        displayUsers(users);

        int targetUserId = readIntInput("Enter User ID to update (0 to cancel): ");
        if (targetUserId == 0) {
            System.out.println("Update cancelled.");
            return;
        }

        if (targetUserId == currentAdmin.id()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }

        boolean exists = users.stream().anyMatch(u -> u.id() == targetUserId);
        if (!exists) {
            System.out.println("No user found with ID " + targetUserId + ".");
            return;
        }

        String newRole = readValidRole();
        if (newRole.equals("0")) {
            System.out.println("Update cancelled.");
            return;
        }

        String newStatus = readValidStatus();
        if (newStatus.equals("0")) {
            System.out.println("Update cancelled.");
            return;
        }

        if (userDao.updateUserRoleAndStatus(targetUserId, newRole, newStatus)) {
            System.out.println("SUCCESS: User ID " + targetUserId + " updated to role "
                    + newRole + " and status " + newStatus + ".");
        } else {
            System.out.println("Update failed. Please try again.");
        }
    }
}
