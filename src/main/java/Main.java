import dao.UserDao;
import db.DBConnection;
import ui.LoginMenu;

import java.util.Scanner;

public class 1Main {
    public static void main(String[] args) {
        DBConnection.initialize();

        try (Scanner scanner = new Scanner(System.in)) {
            new LoginMenu(scanner, new UserDao()).show();
        }
    }
}
