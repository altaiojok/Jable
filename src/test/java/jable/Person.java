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

    @Indexed
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Indexed
    public Integer getAge() {
        return age;
    }

    @Indexed(isUnique = true)
    public Integer getSsn() {
        return ssn;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != null ? !age.equals(person.age) : person.age != null) return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lastName != null ? lastName.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }
}
