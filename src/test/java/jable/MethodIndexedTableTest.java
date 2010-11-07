package jable;

import org.testng.annotations.BeforeTest;

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

    public void testIndexAnnotation() throws Exception {
        assertTrue(personTable.getIndexNames().contains("getLastName"));
        assertFalse(personTable.getIndexNames().contains("getFirstName"));
        assertTrue(personTable.getIndexNames().contains("getAge"));
    }
}