
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Musician {

    private final String id;
    private final String name;
    private final Set<String> roles;
    private final Set<String> availableSlots;

    private int scheduledSessions;

    public Musician(String id, String name,
                    Set<String> roles,
                    Set<String> availableSlots) {

        this.id = id;
        this.name = name;
        this.roles = new HashSet<>(roles);
        this.availableSlots = new HashSet<>(availableSlots);
        this.scheduledSessions = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public Set<String> getAvailableSlots() {
        return Collections.unmodifiableSet(availableSlots);
    }

    public int getScheduledSessions() {
        return scheduledSessions;
    }

    public void incrementSessions() {
        scheduledSessions++;
    }

    public boolean canPlay(String role) {
        return roles.contains(role);
    }

    @Override
    public String toString() {
        return name + " (" + String.join(", ", roles) + ")";
    }
}
