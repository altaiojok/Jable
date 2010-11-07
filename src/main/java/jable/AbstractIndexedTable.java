package jable;

import com.google.common.base.Preconditions;

import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Ryan Brainard
 * @since 2010-11-06
 */
abstract class AbstractIndexedTable<E> implements IndexedTable<E> {
    protected final Class<E> clazz;
    private final Map<String, Map<Object, Collection<E>>> indexes;
    private final ElementType indexType;

    AbstractIndexedTable(ElementType indexType, Class<E> clazz) {
        this.indexType = indexType;
        this.clazz = clazz;
        this.indexes = new HashMap<String, Map<Object, Collection<E>>>();

        for (String indexName : findIndexedMembers()) {
            indexes.put(indexName, new HashMap<Object, Collection<E>>());
        }
    }

    abstract Collection<String> findIndexedMembers();

    public boolean add(E e) {
        boolean hasChanged = false;

        for(String indexBy : indexes.keySet()) {
            final Map<Object, Collection<E>> index = indexes.get(indexBy);
            final Object indexedValue = getIndexedValue(e, indexBy);
            Collection<E> indexedMap = index.get(indexedValue);
            indexedMap = indexedMap != null ? indexedMap : new HashSet<E>();
            hasChanged |= indexedMap.add(e);
            index.put(indexedValue, indexedMap);
        }

        return hasChanged;
    }

    abstract Object getIndexedValue(E e, String indexBy);

    public boolean addAll(Collection<? extends E> c) {
        boolean hasChanged = false;

        for (E e : c) {
            hasChanged |= add(e);
        }

        return hasChanged;
    }

    public Collection<E> getByIndex(String indexName, Object value) {
        return Preconditions.checkNotNull(indexes.get(indexName),
                "No index found for " + indexName +
                ". Be sure to annotate " + indexType.name().toLowerCase() +
                " as @Indexed.").get(value);
    }

    public Collection<String> getIndexNames() {
        return indexes.keySet();
    }
}
