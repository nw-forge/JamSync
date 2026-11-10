
import java.util.HashSet;
import java.util.Set;

public class RehearsalRoom {

    private final String name;
    private final Set<String> equipment;
    private final Set<String> availableSlots;

    public RehearsalRoom(String name,
                         Set<String> equipment,
                         Set<String> availableSlots) {

        this.name = name;
        this.equipment = new HashSet<>(equipment);
        this.availableSlots = new HashSet<>(availableSlots);
    }

    public String getName() {
        return name;
    }

    public boolean supports(Set<String> requiredEquipment) {
        return equipment.containsAll(requiredEquipment);
    }

    public boolean isAvailable(String slot) {
        return availableSlots.contains(slot);
    }
}
