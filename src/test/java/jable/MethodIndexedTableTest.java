package jable;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.testng.annotations.Test;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTableTest extends TestCase {

    final IndexedTable<Person> personTable;

    final Person JS = new Person("Smith", "Joanna", 28, 1);
    final Person AB = new Person("Smith", "Angela", 31, 2);
    final Person MB = new Person("Black", "Mary",   31, 3);

    public MethodIndexedTableTest() throws Exception {
        personTable = new MethodIndexedTable<Person>(Person.class);
    }

    @Test
    public void testIndexAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("getLastName"));
        assertFalse(personTable.getIndexNames().contains("getFirstName"));
        assertTrue(personTable.getIndexNames().contains("getAge"));
    }

    @Test
    public void testGettingCollections() throws Exception {
        personTable.add(AB);
        personTable.add(JS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AB), personTable.getByIndex("getLastName", JS.getLastName()));
        assertEquals(Sets.newHashSet(JS, AB), personTable.getByIndex("getLastName", AB.getLastName()));
        assertEquals(Sets.newHashSet(MB),     personTable.getByIndex("getLastName", MB.getLastName()));

        assertEquals(Sets.newHashSet(JS),     personTable.getByIndex("getAge", JS.getAge()));
        assertEquals(Sets.newHashSet(MB, AB), personTable.getByIndex("getAge", AB.getAge()));
        assertEquals(Sets.newHashSet(MB, AB), personTable.getByIndex("getAge", MB.getAge()));
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
        } catch (NullPointerException npe) {
            assertEquals("No index found for birthday. Be sure to annotate method as @Indexed.", npe.getMessage());
        }
    }

    @Test
    public void testGettingCollectionsForNonIndexedMethod() throws Exception {
        try {
            personTable.getByIndex("getFirstName", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for getFirstName. Be sure to annotate method as @Indexed.", npe.getMessage());
        }
    }
}
