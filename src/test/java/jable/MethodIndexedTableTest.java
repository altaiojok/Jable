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

    public void testMethodIndexedAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("getFullName"));
        assertFalse(personTable.getIndexNames().contains("getLastName"));
    }

    public void testGetByAnnotatedMethodIndexName() throws Exception {
        personTable.add(JS);

        assertEquals(Sets.newHashSet(JS), personTable.getByIndex("getFullName", JS.getFullName()));
    }
}