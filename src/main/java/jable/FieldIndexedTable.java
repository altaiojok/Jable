package jable;

import com.google.common.collect.Sets;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class FieldIndexedTable<E> extends AbstractIndexedTable<E> {

    public FieldIndexedTable(Class<E> clazz) {
        super(ElementType.FIELD, clazz);
    }

     Collection<IndexDefinition> findIndexedDefinitions() {
        final Collection<IndexDefinition> indexedFields = Sets.newHashSet();

         for (Field field : clazz.getFields()) {
            if (field.getAnnotation(Indexed.class) != null) {
                indexedFields.add(new IndexDefinition(field.getName(),
                                                      field.getAnnotation(Indexed.class).isUnique(),
                                                      ElementType.FIELD));
            }
        }

        return indexedFields;
    }

    Object getIndexableValue(E e, String indexBy) {
        try {
            return clazz.getField(indexBy).get(e);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }
    }
}
