package jable;

import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.testng.annotations.Test;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class JableTest extends TestCase {

    final Person rdb = new Person("Brainard", "Ryan", 28);
    final Person elb = new Person("Brainard", "Erin", 31);
    final Person jml = new Person("Lee", "Joomi", 31);

    @Test
    public void testGettingCollections() {
    PersonTable t = new PersonTable();

        t.add(elb);
        t.add(rdb);
        t.add(jml);

        assertEquals(Sets.newHashSet(rdb, elb), t.getByLastName(rdb.lastName));
        assertEquals(Sets.newHashSet(rdb, elb), t.getByLastName(elb.lastName));
        assertEquals(Sets.newHashSet(jml), t.getByLastName(jml.lastName));
        
        assertEquals(Sets.newHashSet(rdb), t.getByAge(rdb.age));
        assertEquals(Sets.newHashSet(jml, elb), t.getByAge(elb.age));
        assertEquals(Sets.newHashSet(jml, elb), t.getByAge(jml.age));
    }


}
