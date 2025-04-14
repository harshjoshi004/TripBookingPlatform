package observerPattern;

import models.Trip;

/**
 * Observer - Interface for objects that receive notifications
 */
public interface Observer {
    void update(Trip trip);
}
