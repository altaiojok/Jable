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

    final IndexedTable<Person> table;

    final Person rdb = new Person("Brainard", "Ryan",  28);
    final Person elb = new Person("Brainard", "Erin",  31);
    final Person jml = new Person("Lee",      "Joomi", 31);

    public FieldIndexedTableTest() throws Exception {
        table = new FieldIndexedTable<Person>(Person.class);
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
        table.add(elb);
        table.add(rdb);
        table.add(jml);

        assertEquals(Sets.newHashSet(rdb, elb), table.getByIndex("lastName", rdb.lastName));
        assertEquals(Sets.newHashSet(rdb, elb), table.getByIndex("lastName", elb.lastName));
        assertEquals(Sets.newHashSet(jml),      table.getByIndex("lastName", jml.lastName));

        assertEquals(Sets.newHashSet(rdb),      table.getByIndex("age", rdb.age));
        assertEquals(Sets.newHashSet(jml, elb), table.getByIndex("age", elb.age));
        assertEquals(Sets.newHashSet(jml, elb), table.getByIndex("age", jml.age));
    }

    @Test
    public void testGettingCollectionsForNonExistentColumn() throws Exception {
        try {
            table.getByIndex("birthday", "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testGettingCollectionsForNonIndexedField() throws Exception {
        try {
            table.getByIndex("firstName", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for firstName. Be sure to annotate field as @Indexed.", npe.getMessage());
        }
    }
}
