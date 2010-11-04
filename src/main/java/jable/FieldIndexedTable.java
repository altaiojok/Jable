package jable;

import com.google.common.base.Preconditions;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTable<E> implements IndexedTable<E> {

    private Class<E> clazz;
    private Map<Field, Map<Object, Collection<E>>> indexes;

    public FieldIndexedTable(Class<E> clazz) {
        this.clazz = clazz;

        indexes = new HashMap<Field, Map<Object, Collection<E>>>();

        for (Field field : clazz.getFields()) {
            if (field.getAnnotation(Indexed.class) != null) {
                indexes.put(field, new HashMap<Object, Collection<E>>());
            }
        }
    }

    public boolean add(E e) {
        boolean collectionChanged = false;

        for(Field indexBy : indexes.keySet()) {
            Map<Object, Collection<E>> index = indexes.get(indexBy);
            Object indexedFieldValue = null;
            try {
                indexedFieldValue = indexBy.get(e);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
            Collection<E> indexedMap = index.get(indexedFieldValue);
            indexedMap = indexedMap != null ? indexedMap : new HashSet<E>();
            collectionChanged |= indexedMap.add(e);
            index.put(indexedFieldValue, indexedMap);
        }

        return collectionChanged;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean collectionChanged = true;

        for (E e : c) {
            collectionChanged |= add(e);
        }

        return collectionChanged;
    }

    public Collection<E> getByIndex(String indexName, Object value) {
        Field fieldIndex = null;
        try {
            fieldIndex = clazz.getField(indexName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }

        return Preconditions.checkNotNull(indexes.get(fieldIndex),
                "No index found for " + fieldIndex.getName() + ". Be sure to annotate field as @Indexed.").get(value);
    }

    Map<Field, Map<Object, Collection<E>>> getIndexes() {
        return indexes;
    }
}
