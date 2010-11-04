package jable;

import java.util.*;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class PersonTable {

    private Map<String, Set<Person>> lastNameToPerson = new HashMap<String, Set<Person>>();
    private Map<Integer, Set<Person>> ageToPerson = new HashMap<Integer, Set<Person>>();

    public void add(Person person) {
        Set<Person> peopleWithSameLastName = lastNameToPerson.get(person.lastName);
        peopleWithSameLastName = peopleWithSameLastName != null ? peopleWithSameLastName : new HashSet<Person>();
        peopleWithSameLastName.add(person);
        lastNameToPerson.put(person.lastName, peopleWithSameLastName);

        Set<Person> peopleWithSameAge = ageToPerson.get(person.age);
        peopleWithSameAge = peopleWithSameAge != null ? peopleWithSameAge : new HashSet<Person>();
        peopleWithSameAge.add(person);
        ageToPerson.put(person.age, peopleWithSameAge);
    }

    public Set<Person> getByLastName(String lastName) {
        return lastNameToPerson.get(lastName);
    }

    public Set<Person> getByAge(Integer age) {
        return ageToPerson.get(age);
    }
}
