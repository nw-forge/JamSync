
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // Define musician availability
        Set<String> monday = Set.of(
            "Mon 10:00", "Mon 14:00"
        );

        Set<String> mondayTuesday = Set.of(
            "Mon 10:00", "Tue 10:00"
        );

        Set<String> mondayWednesday = Set.of(
            "Mon 10:00", "Wed 14:00"
        );

        Set<String> allSlots = Set.of(
            "Mon 10:00",
            "Mon 14:00",
            "Tue 10:00",
            "Wed 14:00",
            "Thu 16:00"
        );

        // Create musicians
        List<Musician> musicians = new ArrayList<>();

        musicians.add(new Musician(
            "M1", "Aarav",
            Set.of("Vocals"), monday
        ));

        musicians.add(new Musician(
            "M2", "Mira",
            Set.of("Guitar"), mondayTuesday
        ));

        musicians.add(new Musician(
            "M3", "Kabir",
            Set.of("Drums"), mondayWednesday
        ));

        musicians.add(new Musician(
            "M4", "Riya",
            Set.of("Guitar", "Vocals"), allSlots
        ));

        // Create rehearsal rooms
        List<RehearsalRoom> rooms = new ArrayList<>();

        rooms.add(new RehearsalRoom(
            "Room A",
            Set.of("Microphone", "Amplifier", "Drum Kit"),
            allSlots
        ));

        rooms.add(new RehearsalRoom(
            "Room B",
            Set.of("Microphone", "Amplifier"),
            mondayWednesday
        ));

        // Define song requirements
        Map<String, Integer> roles = new LinkedHashMap<>();

        roles.put("Vocals", 1);
        roles.put("Guitar", 1);
        roles.put("Drums", 1);

        Song song = new Song("Midnight Echo", roles);

        Set<String> equipmentNeeded = Set.of(
            "Microphone",
            "Amplifier",
            "Drum Kit"
        );

        // Initialize scheduling engine
        ScheduleEngine engine = new ScheduleEngine(
            musicians, rooms
        );

        try {

            // Generate and confirm the best schedule
            RehearsalSession session =
                engine.schedule(song, equipmentNeeded);

            // Display schedule
            engine.printSchedule(session);

            // Create output directory
            File outputDirectory = new File("output");

            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            // Save schedule
            File output = new File(
                outputDirectory, "jamsync_schedule.csv"
            );

            engine.saveSchedule(output);

            System.out.println(
                "\nSchedule saved to: "
                + output.getAbsolutePath()
            );

        } catch (Exception exception) {

            System.out.println(
                "Scheduling failed: " + exception.getMessage()
            );
        }
    }
}
