package models;

/**
 * Admin - User who can manage trips and system
 */
public class Admin extends User {
    public Admin(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "Admin: " + super.toString();
    }
}

