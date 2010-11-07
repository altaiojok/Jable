package jable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

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

    private final Class<E> clazz;
    private final Map<Field, Map<Object, Collection<E>>> indexes;

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
        boolean hasChanged = false;

        for(Field indexBy : indexes.keySet()) {
            final Map<Object, Collection<E>> index = indexes.get(indexBy);
            final Object indexedFieldValue;
            try {
                indexedFieldValue = indexBy.get(e);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
            Collection<E> indexedMap = index.get(indexedFieldValue);
            indexedMap = indexedMap != null ? indexedMap : new HashSet<E>();
            hasChanged |= indexedMap.add(e);
            index.put(indexedFieldValue, indexedMap);
        }

        return hasChanged;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean hasChanged = false;

        for (E e : c) {
            hasChanged |= add(e);
        }

        return hasChanged;
    }

    public Collection<E> getByIndex(String indexName, Object value) {
        final Field fieldIndex;
        try {
            fieldIndex = clazz.getField(indexName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }

        return Preconditions.checkNotNull(indexes.get(fieldIndex),
                "No index found for " + fieldIndex.getName() + ". Be sure to annotate field as @Indexed.").get(value);
    }

    public Collection<String> getIndexNames() {
        Collection<String> indexNames = Sets.newHashSet();

        for (Field field : indexes.keySet()) {
            indexNames.add(field.getName());
        }

        return indexNames;
    }
}
