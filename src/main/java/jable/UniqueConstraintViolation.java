package jable;

/**
 * @author Ryan Brainard
 * @since 2010-11-07
 */
public class UniqueConstraintViolation extends RuntimeException {
    public UniqueConstraintViolation(String constraintName, String conflictingValue) {
        super("Record already exists with " + constraintName + " as " + conflictingValue);
    }
}
