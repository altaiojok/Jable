package jable;

import com.google.common.collect.Sets;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTable<E> extends AbstractIndexedTable<E> {

    public FieldIndexedTable(Class<E> clazz) {
        super(ElementType.FIELD, clazz);
    }

     Collection<String> findIndexedMembers() {
        final Collection<String> indexedFields = Sets.newHashSet();

         for (Field field : clazz.getFields()) {
            if (field.getAnnotation(Indexed.class) != null) {
                indexedFields.add(field.getName());
            }
        }

        return indexedFields;
    }

    public boolean add(E e) {
        boolean hasChanged = false;

        for(String indexBy : indexes.keySet()) {
            final Map<Object, Collection<E>> index = indexes.get(indexBy);
            final Object indexedFieldValue;
            try {
                indexedFieldValue = clazz.getField(indexBy).get(e);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(iae);
            } catch (NoSuchFieldException nsfe) {
                throw new RuntimeException(nsfe);
            }
            Collection<E> indexedMap = index.get(indexedFieldValue);
            indexedMap = indexedMap != null ? indexedMap : new HashSet<E>();
            hasChanged |= indexedMap.add(e);
            index.put(indexedFieldValue, indexedMap);
        }

        return hasChanged;
    }

}
