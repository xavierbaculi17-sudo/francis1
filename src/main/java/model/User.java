package model;

/** Immutable user record mapped to the "users" table. */
public record User(int id, String username, String roles, String status) {
}
