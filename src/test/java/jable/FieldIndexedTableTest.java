package jable;

import com.google.common.collect.Sets;
import com.google.inject.internal.Preconditions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTableTest extends IndexedTableBaseTest {

    @Override
    @BeforeTest
    protected void setUp() throws Exception {
        super.setUp();
        personTable = new FieldIndexedTable<Person>(Person.class);
    }


    @Test
    public void testIndexAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("lastName"));
        assertFalse(personTable.getIndexNames().contains("firstName"));
        assertTrue(personTable.getIndexNames().contains("age"));
    }

    @Test
    public void testGettingCollections() throws Exception {
        personTable.add(AS);
        personTable.add(JS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getByIndex("lastName", JS.lastName));
        assertEquals(Sets.newHashSet(JS, AS), personTable.getByIndex("lastName", AS.lastName));
        assertEquals(Sets.newHashSet(MB),     personTable.getByIndex("lastName", MB.lastName));

        assertEquals(Sets.newHashSet(JS),     personTable.getByIndex("age", JS.age));
        assertEquals(Sets.newHashSet(MB, AS), personTable.getByIndex("age", AS.age));
        assertEquals(Sets.newHashSet(MB, AS), personTable.getByIndex("age", MB.age));
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(personTable.add(JS));
        assertTrue(personTable.add(AS));
        assertFalse(personTable.add(AS));
    }

    @Test
    public void testAddAll() throws Exception {
        assertTrue(personTable.addAll(Sets.newHashSet(AS, JS, MB)));
        assertFalse(personTable.addAll(Sets.newHashSet(AS, JS, MB)));
    }

    @Test
    public void testGettingCollectionsForNonExistentColumn() throws Exception {
        try {
            personTable.getByIndex("birthday", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for birthday.", npe.getMessage());
        }
    }

    @Test
    public void testGettingCollectionsForNonIndexedField() throws Exception {
        try {
            personTable.getByIndex("firstName", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for firstName.", npe.getMessage());
        }
    }

    @Test
    public void testAddDuplicate() throws Exception {
        personTable.add(MB);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(MB), personTable.getByIndex("lastName", MB.lastName));
    }

    @Test
    public void testAddDifferentRecordWithDuplicateOnUniqueIndex() throws Exception {
        Preconditions.checkState(MB.ssn.equals(ML.ssn));

        personTable.add(MB);
        
        try {
            personTable.add(ML);
            fail();
        } catch (UniqueConstraintViolation ucv) {
            assertEquals("Record already exists with ssn as " + MB.ssn, ucv.getMessage());
        }
    }
}
