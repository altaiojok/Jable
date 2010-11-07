package jable;

/**
 * @author Ryan Brainard
 * @since 2010-11-07
 */
public abstract class IndexDefinition<E> {
    private final String name;
    private final boolean unique;

    public IndexDefinition(String name, boolean unique) {
        this.name = name;
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    abstract Object getIndexableValue(E e);
}
