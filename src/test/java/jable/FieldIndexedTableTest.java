package jable;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTableTest extends TestCase {

    final IndexedTable<Person> personTable;

    final Person rdb = new Person("Brainard", "Ryan",  28);
    final Person elb = new Person("Brainard", "Erin",  31);
    final Person jml = new Person("Lee",      "Joomi", 31);

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
        personTable.add(elb);
        personTable.add(rdb);
        personTable.add(jml);

        assertEquals(Sets.newHashSet(rdb, elb), personTable.getByIndex("lastName", rdb.lastName));
        assertEquals(Sets.newHashSet(rdb, elb), personTable.getByIndex("lastName", elb.lastName));
        assertEquals(Sets.newHashSet(jml),      personTable.getByIndex("lastName", jml.lastName));

        assertEquals(Sets.newHashSet(rdb),      personTable.getByIndex("age", rdb.age));
        assertEquals(Sets.newHashSet(jml, elb), personTable.getByIndex("age", elb.age));
        assertEquals(Sets.newHashSet(jml, elb), personTable.getByIndex("age", jml.age));
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(personTable.add(rdb));
        assertTrue(personTable.add(elb));
        assertFalse(personTable.add(elb));
    }

    @Test
    public void testAddAll() throws Exception {
        assertTrue(personTable.addAll(Sets.newHashSet(elb)));
        assertTrue(personTable.addAll(Sets.newHashSet(elb, rdb, jml)));
        assertFalse(personTable.addAll(Sets.newHashSet(elb, rdb, jml)));
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
