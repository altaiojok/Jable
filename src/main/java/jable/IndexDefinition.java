package jable;

import java.lang.annotation.ElementType;

/**
 * @author Ryan Brainard
 * @since 2010-11-07
 */
public final class IndexDefinition {
    private final String name;
    private final boolean unique;
    private final ElementType type;

    public IndexDefinition(String name, boolean unique, ElementType type) {
        this.name = name;
        this.unique = unique;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    public ElementType getType() {
        return type;
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
