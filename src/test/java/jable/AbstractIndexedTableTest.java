package jable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class AbstractIndexedTableTest extends IndexedTableBaseTest {

    private static final IndexDefinition<Person> NON_UNIQUE_INDEX = new IndexDefinition<Person>("nonUniqueIndex", false){
        @Override
        Object getIndexableValue(Person person) {
            return person.getLastName();
        }
    };
    private static final IndexDefinition<Person> UNIQUE_INDEX = new IndexDefinition<Person>("uniqueIndex", true){
        @Override
        Object getIndexableValue(Person person) {
            return person.getSsn();
        }
    };

    private static final Collection<IndexDefinition<Person>> INDEX_DEFINITIONS = Sets.newHashSet(
            NON_UNIQUE_INDEX,
            UNIQUE_INDEX
    );


    private static final Collection<String> INDEX_DEFINITIONS_NAMES = Sets.newHashSet(
        NON_UNIQUE_INDEX.getName(),
        UNIQUE_INDEX.getName()
    );

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        personTable = new AbstractIndexedTable<Person>(Person.class) {
            @Override
            Collection<IndexDefinition<Person>> buildIndexDefinitions() {
                return INDEX_DEFINITIONS;
            }
        };
    }

    public void testInitializingIndexDefinitions() throws Exception {
        assertEquals(INDEX_DEFINITIONS, personTable.getIndexDefinitions());
    }

    public void testInitializingIndexDefinitionNames() throws Exception {
        assertEquals(INDEX_DEFINITIONS_NAMES, personTable.getIndexNames());
    }

    public void testInitializingDuplicateIndex() throws Exception {
        try {
            new AbstractIndexedTable<Person>(Person.class) {
                @Override
                Collection<IndexDefinition<Person>> buildIndexDefinitions() {
                    return Lists.newArrayList(NON_UNIQUE_INDEX, UNIQUE_INDEX, UNIQUE_INDEX);
                }
            };
        } catch (UniqueConstraintViolation ucv) {
            assertEquals("Record already exists with " + IndexDefinition.class.getSimpleName() + " as " + UNIQUE_INDEX.getName(), ucv.getMessage());
        }
    }

    public void testAdd() throws Exception {
        assertTrue(personTable.add(JS));
        assertTrue(personTable.add(AS));
        assertFalse(personTable.add(AS));
    }

    public void testAddAll() throws Exception {
        assertTrue(personTable.addAll(Sets.newHashSet(AS, JS)));
        assertTrue(personTable.addAll(Sets.newHashSet(AS, JS, MB)));
        assertFalse(personTable.addAll(Sets.newHashSet(AS, JS, MB)));
    }

    public void testGetByIndexName() throws Exception {
        personTable.add(AS);
        personTable.add(JS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getBy(NON_UNIQUE_INDEX.getName(), NON_UNIQUE_INDEX.getIndexableValue(JS)));
        assertEquals(Sets.newHashSet(JS, AS), personTable.getBy(NON_UNIQUE_INDEX.getName(), NON_UNIQUE_INDEX.getIndexableValue(AS)));
        assertEquals(Sets.newHashSet(MB),     personTable.getBy(NON_UNIQUE_INDEX.getName(), NON_UNIQUE_INDEX.getIndexableValue(MB)));
    }


    public void testGetByIndexDefinition() throws Exception {
        personTable.add(AS);
        personTable.add(JS);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(JS, AS), personTable.getBy(NON_UNIQUE_INDEX, NON_UNIQUE_INDEX.getIndexableValue(JS)));
        assertEquals(Sets.newHashSet(JS, AS), personTable.getBy(NON_UNIQUE_INDEX, NON_UNIQUE_INDEX.getIndexableValue(AS)));
        assertEquals(Sets.newHashSet(MB),     personTable.getBy(NON_UNIQUE_INDEX, NON_UNIQUE_INDEX.getIndexableValue(MB)));
    }

    public void testGetByIndexNameForMissingIndex() throws Exception {
        final String missingIndex = "NotHere";

        try {
            personTable.getBy(missingIndex, "");
            fail();
        } catch (NullPointerException npe) {
            assertEquals("No index found for " + missingIndex + ".", npe.getMessage());
        }
    }

    public void testAddDuplicate() throws Exception {
        personTable.add(MB);
        personTable.add(MB);

        assertEquals(Sets.newHashSet(MB), personTable.getBy(NON_UNIQUE_INDEX.getName(), NON_UNIQUE_INDEX.getIndexableValue(MB)));
    }

    public void testAddDifferentRecordWithDuplicateOnUniqueIndex() throws Exception {
        Preconditions.checkState(UNIQUE_INDEX.getIndexableValue(MB).equals(UNIQUE_INDEX.getIndexableValue(ML)));
        personTable.add(MB);

        try {
            personTable.add(ML);
            fail();
        } catch (UniqueConstraintViolation ucv) {
            assertEquals("Record already exists with " + UNIQUE_INDEX.getName() + " as " + UNIQUE_INDEX.getIndexableValue(MB), ucv.getMessage());
        }
    }
}
