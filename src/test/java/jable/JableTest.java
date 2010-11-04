package jable;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class JableTest extends TestCase {

    final Person rdb = new Person("Brainard", "Ryan", 28);
    final Person elb = new Person("Brainard", "Erin", 31);
    final Person jml = new Person("Lee", "Joomi", 31);

    @Test
    public void testIndexAnnotation() throws Exception {
        Set<String> fieldIndexes = Sets.newHashSet();

        for(Field f : new PersonTable().getIndexes().keySet()) {
            fieldIndexes.add(f.getName());
        }

        assertTrue(fieldIndexes.contains("lastName"));
        assertFalse(fieldIndexes.contains("firstName"));
        assertTrue(fieldIndexes.contains("age"));
    }

    @Test
    public void testGettingCollections() throws Exception {
    PersonTable t = new PersonTable();

        t.add(elb);
        t.add(rdb);
        t.add(jml);

        assertEquals(Sets.newHashSet(rdb, elb), t.getByIndex(Person.class.getField("lastName"), rdb.lastName));
        assertEquals(Sets.newHashSet(rdb, elb), t.getByIndex(Person.class.getField("lastName"), elb.lastName));
        assertEquals(Sets.newHashSet(jml), t.getByIndex(Person.class.getField("lastName"), jml.lastName));

        assertEquals(Sets.newHashSet(rdb), t.getByIndex(Person.class.getField("age"), rdb.age));
        assertEquals(Sets.newHashSet(jml, elb), t.getByIndex(Person.class.getField("age"), elb.age));
        assertEquals(Sets.newHashSet(jml, elb), t.getByIndex(Person.class.getField("age"), jml.age));
    }


}
