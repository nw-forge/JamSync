
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RehearsalSession {

    private final Song song;
    private final List<Musician> musicians;
    private final RehearsalRoom room;
    private final String slot;

    public RehearsalSession(Song song,
                            List<Musician> musicians,
                            RehearsalRoom room,
                            String slot) {

        this.song = song;
        this.musicians = new ArrayList<>(musicians);
        this.room = room;
        this.slot = slot;
    }

    public Song getSong() {
        return song;
    }

    public List<Musician> getMusicians() {
        return Collections.unmodifiableList(musicians);
    }

    public RehearsalRoom getRoom() {
        return room;
    }

    public String getSlot() {
        return slot;
    }

    public boolean usesMusician(Musician musician) {
        return musicians.stream().anyMatch(
            m -> m.getId().equals(musician.getId())
        );
    }
}
