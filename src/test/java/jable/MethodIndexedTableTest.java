package jable;

import com.google.common.collect.Sets;
import com.google.inject.internal.Preconditions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTableTest extends IndexedTableBaseTest {

    @Override
    @BeforeTest
    protected void setUp() throws Exception {
        super.setUp();
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
        personTable.add(AS);
        personTable.add(JS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getByIndex("getLastName", JS.getLastName()));
        assertEquals(Sets.newHashSet(JS, AS), personTable.getByIndex("getLastName", AS.getLastName()));
        assertEquals(Sets.newHashSet(MB),     personTable.getByIndex("getLastName", MB.getLastName()));

        assertEquals(Sets.newHashSet(JS),     personTable.getByIndex("getAge", JS.getAge()));
        assertEquals(Sets.newHashSet(MB, AS), personTable.getByIndex("getAge", AS.getAge()));
        assertEquals(Sets.newHashSet(MB, AS), personTable.getByIndex("getAge", MB.getAge()));
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
            personTable.getByIndex("getBirthday", "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for getBirthday. Be sure to annotate method as @Indexed.", npe.getMessage());
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

    @Test
    public void testAddDuplicate() throws Exception {
        personTable.add(MB);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(MB), personTable.getByIndex("getLastName", MB.lastName));
    }

    @Test
    public void testAddDifferentRecordWithDuplicateOnUniqueIndex() throws Exception {
        Preconditions.checkState(MB.ssn.equals(ML.ssn));

        personTable.add(MB);

        try {
            personTable.add(ML);
            fail();
        } catch (UniqueConstraintViolation ucv) {
            assertEquals("Record already exists with getSsn as " + MB.ssn, ucv.getMessage());
        }
    }
}
