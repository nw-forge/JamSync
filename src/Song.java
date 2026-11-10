
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Song {

    private final String title;
    private final Map<String, Integer> requiredRoles;

    public Song(String title, Map<String, Integer> requiredRoles) {
        this.title = title;
        this.requiredRoles = new LinkedHashMap<>(requiredRoles);
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Integer> getRequiredRoles() {
        return Collections.unmodifiableMap(requiredRoles);
    }
}
