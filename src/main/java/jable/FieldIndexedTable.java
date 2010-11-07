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

        for (final Field field : clazz.getFields()) {
            if (field.getAnnotation(Indexed.class) != null) {
                indexedFields.add(new IndexDefinition<E>(field.getName(), field.getAnnotation(Indexed.class).isUnique()) {
                    @Override
                    public Object getIndexableValue(E e) {
                        try {
                            return field.get(e);
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        }

        return indexedFields;
    }
}
