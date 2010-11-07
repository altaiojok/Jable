package jable;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public final class Person {
    @Indexed public final String lastName;
    public final String firstName ;
    @Indexed public final Integer age;
    @Indexed(isUnique = true) public final Integer ssn;


    Person(String lastName, String firstName, Integer age, Integer ssn) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
        this.ssn = ssn;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getSsn() {
        return ssn;
    }

    @Indexed
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                '}';
    }
}
