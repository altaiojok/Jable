package jable;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTableTest extends TestCase {

    final IndexedTable<Person> personTable;

    final Person JS = new Person("Smith", "Joanna", 28);
    final Person AB = new Person("Smith", "Angela", 31);
    final Person MB = new Person("Black", "Mary",   31);

    public FieldIndexedTableTest() throws Exception {
        personTable = new FieldIndexedTable<Person>(Person.class);
    }

    @Test
    public void testIndexAnnotation() throws Exception {
        Set<String> fieldIndexes = Sets.newHashSet();

        for(Field f : new FieldIndexedTable<Person>(Person.class).getIndexes().keySet()) {
            fieldIndexes.add(f.getName());
        }

        assertTrue(fieldIndexes.contains("lastName"));
        assertFalse(fieldIndexes.contains("firstName"));
        assertTrue(fieldIndexes.contains("age"));
    }

    @Test
    public void testGettingCollections() throws Exception {
        personTable.add(AB);
        personTable.add(JS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AB), personTable.getByIndex("lastName", JS.lastName));
        assertEquals(Sets.newHashSet(JS, AB), personTable.getByIndex("lastName", AB.lastName));
        assertEquals(Sets.newHashSet(MB),     personTable.getByIndex("lastName", MB.lastName));

        assertEquals(Sets.newHashSet(JS),     personTable.getByIndex("age", JS.age));
        assertEquals(Sets.newHashSet(MB, AB), personTable.getByIndex("age", AB.age));
        assertEquals(Sets.newHashSet(MB, AB), personTable.getByIndex("age", MB.age));
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(personTable.add(JS));
        assertTrue(personTable.add(AB));
        assertFalse(personTable.add(AB));
    }

    @Test
    public void testAddAll() throws Exception {
        assertTrue(personTable.addAll(Sets.newHashSet(AB, JS, MB)));
        assertFalse(personTable.addAll(Sets.newHashSet(AB, JS, MB)));
    }

    @Test
    public void testGettingCollectionsForNonExistentColumn() throws Exception {
        try {
            personTable.getByIndex("birthday", "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testGettingCollectionsForNonIndexedField() throws Exception {
        try {
            personTable.getByIndex("firstName", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for firstName. Be sure to annotate field as @Indexed.", npe.getMessage());
        }
    }
}
