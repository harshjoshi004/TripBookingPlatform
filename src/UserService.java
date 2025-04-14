import models.Admin;
import models.Passenger;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users = new ArrayList<>();

    public Admin registerAdmin(String name, String email, String phoneNumber) {
        Admin admin = new Admin(name, email, phoneNumber);
        users.add(admin);
        return admin;
    }

    public Passenger registerPassenger(String name, String email, String phoneNumber) {
        Passenger passenger = new Passenger(name, email, phoneNumber);
        users.add(passenger);
        return passenger;
    }

    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return users;
    }
}

