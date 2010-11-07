package jable;

import com.google.common.collect.Sets;

import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTable<E> extends AbstractIndexedTable<E> {

    public MethodIndexedTable(Class<E> clazz) {
        super(ElementType.METHOD, clazz);
    }

    Collection<IndexDefinition> findIndexedDefinitions() {
        final Collection<IndexDefinition> indexedMethods = Sets.newHashSet();

        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Indexed.class) != null) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException("Methods with parameters cannot be indexed.");
                }

                if (method.getReturnType() == null) {
                    throw new IllegalArgumentException("Methods without return values cannot be indexed");
                }

                indexedMethods.add(new IndexDefinition(method.getName(),
                                                       method.getAnnotation(Indexed.class).isUnique(),
                                                       ElementType.METHOD));
            }
        }

        return indexedMethods;
    }

    Object getIndexableValue(E e, String indexBy) {
        try {
            return clazz.getMethod(indexBy).invoke(e);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
}
