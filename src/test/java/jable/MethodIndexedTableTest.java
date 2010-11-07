package jable;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.testng.annotations.Test;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTableTest extends TestCase {

    final IndexedTable<PrivatePerson> privatePersonTable;

    final PrivatePerson JS = new PrivatePerson("Smith", "Joanna", 28);
    final PrivatePerson AB = new PrivatePerson("Smith", "Angela", 31);
    final PrivatePerson MB = new PrivatePerson("Black", "Mary",   31);

    public MethodIndexedTableTest() throws Exception {
        privatePersonTable = new MethodIndexedTable<PrivatePerson>(PrivatePerson.class);
    }

    @Test
    public void testIndexAnnotation() throws Exception {
        assertTrue(privatePersonTable.getIndexNames().contains("getLastName"));
        assertFalse(privatePersonTable.getIndexNames().contains("getFirstName"));
        assertTrue(privatePersonTable.getIndexNames().contains("getAge"));
    }

    @Test
    public void testGettingCollections() throws Exception {
        privatePersonTable.add(AB);
        privatePersonTable.add(JS);
        privatePersonTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AB), privatePersonTable.getByIndex("getLastName", JS.getLastName()));
        assertEquals(Sets.newHashSet(JS, AB), privatePersonTable.getByIndex("getLastName", AB.getLastName()));
        assertEquals(Sets.newHashSet(MB),     privatePersonTable.getByIndex("getLastName", MB.getLastName()));

        assertEquals(Sets.newHashSet(JS),     privatePersonTable.getByIndex("getAge", JS.getAge()));
        assertEquals(Sets.newHashSet(MB, AB), privatePersonTable.getByIndex("getAge", AB.getAge()));
        assertEquals(Sets.newHashSet(MB, AB), privatePersonTable.getByIndex("getAge", MB.getAge()));
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(privatePersonTable.add(JS));
        assertTrue(privatePersonTable.add(AB));
        assertFalse(privatePersonTable.add(AB));
    }

    @Test
    public void testAddAll() throws Exception {
        assertTrue(privatePersonTable.addAll(Sets.newHashSet(AB, JS, MB)));
        assertFalse(privatePersonTable.addAll(Sets.newHashSet(AB, JS, MB)));
    }

    @Test
    public void testGettingCollectionsForNonExistentColumn() throws Exception {
        try {
            privatePersonTable.getByIndex("birthday", "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testGettingCollectionsForNonIndexedMethod() throws Exception {
        try {
            privatePersonTable.getByIndex("getFirstName", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for getFirstName. Be sure to annotate method as @Indexed.", npe.getMessage());
        }
    }
}
