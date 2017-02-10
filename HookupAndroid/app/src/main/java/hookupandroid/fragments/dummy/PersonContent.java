package hookupandroid.fragments.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hookupandroid.model.Person;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PersonContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Person> ITEMS = new ArrayList<Person>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Person> ITEM_MAP = new HashMap<String, Person>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Person item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static Person createDummyItem(int position) {
        return new Person("Pera"+position, "Peric"+position);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
//    public static class PersonExample {
//        public final String id;
//        public final String content;
//        public final String details;
////        public final String firstname;
////        public final String lastname;
////        public final String age;
////        public final String address;
////        public final String city;
////        public final String country;
////        public final int image_id;
//
//
////        public Person(String id, String content, String details, String firstname, String lastname, String age, String address, String city, String country, int image_id) {
////            this.id = id;
////            this.content = content;
////            this.details = details;
////            this.firstname = firstname;
////            this.lastname = lastname;
////            this.age = age;
////            this.address = address;
////            this.city = city;
////            this.country = country;
////            this.image_id = image_id;
////        }
//
//        public PersonExample(String id, String content, String details) {
//            this.id = id;
//            this.content = content;
//            this.details = details;
//        }
//
//        @Override
//        public String toString() {
//            return content;
//        }
//    }
}
