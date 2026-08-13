import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Song — ADT แทน "เพลง" หนึ่งเพลง
 *
 * immutable class ครบสูตร 6 ข้อ:
 *  1) field เป็น private final ทั้งหมด
 *  2) ไม่มี mutator (setter)
 *  3) class เป็น final (กัน subclass มา override แล้วทำลาย invariant)
 *  4) reference type field (List) ไม่ leak ตัวเอง
 *  5) defensive copy ตอนรับเข้า (constructor)
 *  6) defensive copy ตอนส่งออก (observer)
 */
public final class Song {

    private final String title;
    private final String artist;
    private final List<String> tags;

    public Song(String title, String artist, List<String> tags) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title ห้ามเป็น null หรือว่าง");
        }
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("artist ห้ามเป็น null หรือว่าง");
        }
        if (tags == null) {
            throw new IllegalArgumentException("tags ห้ามเป็น null");
        }
        for (String t : tags) {
            if (t == null || t.isBlank()) {
                throw new IllegalArgumentException("tag แต่ละตัวห้าม null หรือว่าง");
            }
        }
        this.title = title;
        this.artist = artist;
        // defensive copy ขาเข้า: กัน caller ถือลูกศรเดิมไว้แล้วมา mutate list
        // ทีหลัง ซึ่งจะทำให้ rep ของ Song เปลี่ยนไปโดยที่เราไม่รู้ตัว
        this.tags = new ArrayList<>(tags);
    }

    // ---------- observers ----------

    public String title() {
        return title;
    }

    public String artist() {
        return artist;
    }

    public List<String> tags() {
        // defensive copy ขาออก: กัน caller เอา list ที่ได้ไปแก้ แล้วกระทบ rep ภายใน
        return new ArrayList<>(tags);
    }

    // ---------- producer ----------

    /**
     * spec: คืน Song "ตัวใหม่" ที่มีแท็กเพิ่มต่อท้าย — ห้ามแก้ตัวเดิม
     * @throws IllegalArgumentException เมื่อ tag เป็น null/ว่าง
     */
    public Song withTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("tag ห้ามเป็น null หรือว่าง");
        }
        // ใช้ tags() ที่คืนสำเนามาแล้ว ปลอดภัยจะ mutate ต่อได้เลย
        List<String> newTags = this.tags();
        newTags.add(tag);
        // สร้าง Song ตัวใหม่ ไม่แตะ this เดิมเลย -> ตัวเดิมยัง immutable
        return new Song(this.title, this.artist, newTags);
    }

    // ---------- equality ----------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song that = (Song) o;
        return this.title.equals(that.title)
                && this.artist.equals(that.artist)
                && this.tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, tags);
    }

    @Override
    public String toString() {
        return title + " — " + artist + " " + tags;
    }
}