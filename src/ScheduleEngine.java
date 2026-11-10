
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exceptions.BookingConflictException;
import exceptions.NoFeasibleScheduleException;

public class ScheduleEngine {

    private final List<Musician> musicians;
    private final List<RehearsalRoom> rooms;
    private final List<RehearsalSession> confirmedSessions;

    public ScheduleEngine(List<Musician> musicians,
                          List<RehearsalRoom> rooms) {

        this.musicians = musicians;
        this.rooms = rooms;
        this.confirmedSessions = new ArrayList<>();
    }

    public List<RehearsalSession> getConfirmedSessions() {
        return java.util.Collections.unmodifiableList(confirmedSessions);
    }

    // Main scheduling method
    public RehearsalSession schedule(
            Song song, Set<String> requiredEquipment)
            throws NoFeasibleScheduleException {

        List<List<Musician>> candidateLineups = new ArrayList<>();

        buildLineups(
            song,
            new ArrayList<>(song.getRequiredRoles().keySet()),
            0,
            new ArrayList<>(),
            candidateLineups
        );

        if (candidateLineups.isEmpty()) {
            throw new NoFeasibleScheduleException(
                "No lineup can cover all roles for: " + song.getTitle()
            );
        }

        RehearsalSession best = null;
        int bestScore = Integer.MIN_VALUE;

        for (List<Musician> lineup : candidateLineups) {

            Set<String> commonSlots = findCommonSlots(lineup);

            for (String slot : commonSlots) {

                for (RehearsalRoom room : rooms) {

                    if (!room.isAvailable(slot)
                            || !room.supports(requiredEquipment)
                            || hasConflict(lineup, room, slot)) {
                        continue;
                    }

                    int score = scoreCandidate(lineup, slot);

                    if (score > bestScore) {
                        bestScore = score;

                        best = new RehearsalSession(
                            song, lineup, room, slot
                        );
                    }
                }
            }
        }

        if (best == null) {
            throw new NoFeasibleScheduleException(
                "No valid time/room combination found for: "
                + song.getTitle()
            );
        }

        try {
            confirm(best);
        } catch (BookingConflictException exception) {
            throw new NoFeasibleScheduleException(
                exception.getMessage()
            );
        }

        return best;
    }

    // Recursive backtracking for musician selection
    private void buildLineups(
            Song song,
            List<String> roles,
            int index,
            List<Musician> chosen,
            List<List<Musician>> results) {

        if (index == roles.size()) {
            results.add(new ArrayList<>(chosen));
            return;
        }

        String role = roles.get(index);
        int count = song.getRequiredRoles().get(role);

        // Current prototype supports one musician per role
        if (count != 1) {
            throw new IllegalArgumentException(
                "Prototype expects one musician per required role: "
                + role
            );
        }

        for (Musician musician : musicians) {

            if (musician.canPlay(role) && !chosen.contains(musician)) {

                chosen.add(musician);

                buildLineups(
                    song, roles, index + 1, chosen, results
                );

                chosen.remove(chosen.size() - 1);
            }
        }
    }

    // Find common availability among all musicians
    private Set<String> findCommonSlots(List<Musician> lineup) {

        Set<String> common = new HashSet<>(
            lineup.get(0).getAvailableSlots()
        );

        for (int i = 1; i < lineup.size(); i++) {
            common.retainAll(
                lineup.get(i).getAvailableSlots()
            );
        }

        return common;
    }

    // Check existing bookings for conflicts
    private boolean hasConflict(
            List<Musician> lineup,
            RehearsalRoom room,
            String slot) {

        for (RehearsalSession session : confirmedSessions) {

            if (session.getSlot().equals(slot)) {

                // Check whether the room is already booked
                if (session.getRoom().getName().equals(room.getName())) {
                    return true;
                }

                // Check whether any musician is already booked
                for (Musician musician : lineup) {

                    if (session.usesMusician(musician)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Rank candidate schedules
    private int scoreCandidate(
            List<Musician> lineup,
            String slot) {

        int workloadPenalty = lineup.stream()
            .mapToInt(Musician::getScheduledSessions)
            .sum();

        List<String> orderedSlots = Arrays.asList(
            "Mon 10:00",
            "Mon 14:00",
            "Tue 10:00",
            "Wed 14:00",
            "Thu 16:00"
        );

        int timePreference =
            orderedSlots.size() - orderedSlots.indexOf(slot);

        return (timePreference * 10) - (workloadPenalty * 3);
    }

    // Confirm selected schedule
    private void confirm(RehearsalSession session)
            throws BookingConflictException {

        if (hasConflict(
                session.getMusicians(),
                session.getRoom(),
                session.getSlot())) {

            throw new BookingConflictException(
                "The selected slot is already booked."
            );
        }

        confirmedSessions.add(session);

        session.getMusicians().forEach(
            Musician::incrementSessions
        );
    }

    // Display confirmed schedule
    public void printSchedule(RehearsalSession session) {

        System.out.println(
            "\n===== JAMSYNC: CONFIRMED REHEARSAL ====="
        );

        System.out.println(
            "Song: " + session.getSong().getTitle()
        );

        System.out.println(
            "Time: " + session.getSlot()
        );

        System.out.println(
            "Room: " + session.getRoom().getName()
        );

        System.out.println("Lineup:");

        session.getMusicians().forEach(
            musician -> System.out.println(" - " + musician)
        );

        System.out.println("Status: CONFIRMED");
    }

    // Save confirmed schedules to CSV
    public void saveSchedule(File file) throws IOException {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(file, false))) {

            writer.println("Song,Time,Room,Musicians");

            for (RehearsalSession session : confirmedSessions) {

                String names = session.getMusicians().stream()
                    .map(Musician::getName)
                    .reduce((a, b) -> a + " | " + b)
                    .orElse("");

                writer.println(
                    session.getSong().getTitle() + ","
                    + session.getSlot() + ","
                    + session.getRoom().getName() + ","
                    + names
                );
            }
        }
    }
}
