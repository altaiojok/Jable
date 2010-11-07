package jable;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public final class PrivatePerson {
    private final String lastName;
    private final String firstName ;
    private final Integer age;

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

    PrivatePerson(String lastName, String firstName, Integer age) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    @Override
    public String toString() {
        return "PrivatePerson{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrivatePerson person = (PrivatePerson) o;

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
