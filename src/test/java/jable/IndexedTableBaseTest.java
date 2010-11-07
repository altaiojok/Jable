package jable;

import junit.framework.TestCase;

/**
 * @author Ryan Brainard
 * @since 2010-11-07
 */
public class IndexedTableBaseTest extends TestCase {
    protected IndexedTable<Person> personTable;
    protected static final Person JS = new Person("Smith", "Joanna", 28, 1);
    protected static final Person AS = new Person("Smith", "Angela", 31, 2);
    protected static final Person MB = new Person("Black", "Mary",   31, 3);
    protected static final Person ML = new Person("Lee",   "Mary",   31, 3); // same SSN as MB

    public void testDummy() throws Exception {
        // TODO: remove once figuring out how to disable running of base test
    }
}
