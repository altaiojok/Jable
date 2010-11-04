package jable;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class PersonTable {

    private Map<String, Set<Person>> lastNameToPerson = new HashMap<String, Set<Person>>();
    private Map<Integer, Set<Person>> ageToPerson = new HashMap<Integer, Set<Person>>();

    public void add(Person person) throws NoSuchFieldException, IllegalAccessException {
        index(person, person.getClass().getField("lastName"));

        Set<Person> peopleWithSameAge = ageToPerson.get(person.age);
        peopleWithSameAge = peopleWithSameAge != null ? peopleWithSameAge : new HashSet<Person>();
        peopleWithSameAge.add(person);
        ageToPerson.put(person.age, peopleWithSameAge);
    }

    private void index(Person person, Field idx) throws IllegalAccessException {
        Set<Person> peopleWithSameLastName = lastNameToPerson.get(idx.get(person));
        peopleWithSameLastName = peopleWithSameLastName != null ? peopleWithSameLastName : new HashSet<Person>();
        peopleWithSameLastName.add(person);
        lastNameToPerson.put((String) idx.get(person), peopleWithSameLastName);
    }

    public Set<Person> getByLastName(String lastName) {
        return lastNameToPerson.get(lastName);
    }

    public Set<Person> getByAge(Integer age) {
        return ageToPerson.get(age);
    }
}
