package jable;

import com.google.common.collect.Sets;


/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTableTest extends IndexedTableBaseTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        personTable = new MethodIndexedTable<Person>(Person.class);
    }

    public void testIndexAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("getLastName"));
        assertFalse(personTable.getIndexNames().contains("getFirstName"));
        assertTrue(personTable.getIndexNames().contains("getAge"));
    }

    public void testGetByAnnotatedMethodIndexName() throws Exception {
        personTable.add(JS);
        personTable.add(AS);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getBy("getLastName", JS.getLastName()));
    }
}