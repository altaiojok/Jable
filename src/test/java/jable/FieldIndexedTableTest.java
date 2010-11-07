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

    public void testIndexAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("lastName"));
        assertFalse(personTable.getIndexNames().contains("firstName"));
        assertTrue(personTable.getIndexNames().contains("age"));
    }

    public void testGetByAnnotatedFieldIndexName() throws Exception {
        personTable.add(JS);
        personTable.add(AS);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getBy("lastName", JS.lastName));
    }
}
