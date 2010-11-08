package jable;

import com.google.common.collect.Sets;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTableTest extends IndexedTableBaseTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        personTable = new FieldIndexedTable<Person>(Person.class);
    }

    public void testFieldIndexedAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("lastName"));
        assertFalse(personTable.getIndexNames().contains("firstName"));
        assertTrue(personTable.getIndexNames().contains("age"));
    }

    public void testGetByAnnotatedFieldIndexName() throws Exception {
        personTable.add(JS);
        personTable.add(AS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getByIndex("lastName", JS.lastName));
    }
}
