package jable;

import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class PersonTable {

    private Map<Field, Map<Object, Set<Person>>> indexes;

    public PersonTable() throws NoSuchFieldException {
        indexes = new HashMap<Field, Map<Object, Set<Person>>>();

        for (Field f : Person.class.getFields()) {
            if (f.getAnnotation(Indexed.class) != null) {
                indexes.put(Person.class.getField(f.getName()), new HashMap<Object, Set<Person>>());
            }
        }
    }

    public void add(Person person) throws NoSuchFieldException, IllegalAccessException {
        index(person, person.getClass().getField("lastName"));
        index(person, person.getClass().getField("age"));
    }

    private void index(Person person, Field field) throws IllegalAccessException, NoSuchFieldException {
        Set<Person> indexedMap = indexes.get(field).get(field.get(person));
        indexedMap = indexedMap != null ? indexedMap : new HashSet<Person>();
        indexedMap.add(person);
        indexes.get(field).put(field.get(person), indexedMap);
    }

    public Set<Person> getByIndex(Field field, Object value) throws NoSuchFieldException {
        return indexes.get(field).get(value);
    }

    Map<Field, Map<Object, Set<Person>>> getIndexes() {
        return indexes;
    }
}
