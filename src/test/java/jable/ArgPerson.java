package jable;

/**
 * @author Ryan Brainard
 * @since 2010-11-07
 */
public class ArgPerson {
    @Indexed
    public Integer aMethodWithArgs(Integer a, Integer b) {
        return a + b;
    }
}
