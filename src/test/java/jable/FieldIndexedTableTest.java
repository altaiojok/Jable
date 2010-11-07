package jable;

import org.testng.annotations.BeforeTest;

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

    public void testIndexAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("lastName"));
        assertFalse(personTable.getIndexNames().contains("firstName"));
        assertTrue(personTable.getIndexNames().contains("age"));
    }
}
