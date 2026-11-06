# User Management System (Admin Update Activity)

Java 17 + Maven + SQLite (`students.db`, created automatically on first run).

## Structure
```
src/main/java
├── Main.java
├── model/User.java
├── db/DBConnection.java
├── dao/UserDao.java        <- updateUserRoleAndStatus(...)
└── ui/
    ├── LoginMenu.java
    ├── AdminMenu.java      <- Update User, readIntInput, readValidRole, readValidStatus
    └── UserMenu.java
```

## Run in IntelliJ
1. File > Open > select the folder containing `pom.xml`, then "Trust Project".
2. Wait for Maven to download dependencies (or click the Maven reload icon).
3. Right-click `Main.java` > Run 'Main.main()'.

## Seed accounts
| Username | Password  | Role    | Status   |
|----------|-----------|---------|----------|
| admin    | admin123  | ADMIN   | APPROVED |
| student1 | pass123   | STUDENT | PENDING  |
| student2 | pass123   | STUDENT | APPROVED |
| teacher1 | pass123   | TEACHER | APPROVED |

## Demo for the screenshots
Login as admin > 2 (Update User) > type `abc` (validation) > `1` (self-edit warning)
> `2` > `BOSS` (invalid role) > `teacher` > `approved` > SUCCESS.
Then open students.db in DB Browser for SQLite and show the row for user ID 2.
