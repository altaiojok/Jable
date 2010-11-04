package jable;

import java.util.Collection;

/**
 * A collection of elements E, which can be retrieved by a given index.
 *
 * @author Ryan Brainard
 * @since 2010-11-04
 */
public interface IndexedTable<E> {

    boolean add(E e);

    boolean addAll(Collection<? extends E> c);

    Collection<E> getByIndex(String indexName, Object value);
}
