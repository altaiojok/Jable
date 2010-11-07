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

    public final String getName() {
        return name;
    }

    public final boolean isUnique() {
        return unique;
    }

    abstract Object getIndexableValue(E e);

    @Override
    public String toString() {
        return "IndexDefinition{" + name + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexDefinition that = (IndexDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
