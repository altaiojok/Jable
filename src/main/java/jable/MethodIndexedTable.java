package jable;

import com.google.common.collect.Sets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTable<E> extends AbstractIndexedTable<E> {

    public MethodIndexedTable(Class<E> clazz) {
        super(clazz);
    }

    Collection<IndexDefinition<E>> buildIndexDefinitions() {
        final Collection<IndexDefinition<E>> indexedMethods = Sets.newHashSet();

        for (final Method method : clazz.getMethods()) {
            if (method.getAnnotation(Indexed.class) != null) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException("Methods with parameters cannot be indexed.");
                }

                if (method.getReturnType().getName().equals("void")) {
                    throw new IllegalArgumentException("Methods without return values cannot be indexed");
                }

                indexedMethods.add(new IndexDefinition<E>(method.getName(), method.getAnnotation(Indexed.class).isUnique()) {
                    @Override
                    public Object getIndexableValue(E e) {
                        try {
                            return method.invoke(e);
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        } catch (InvocationTargetException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        }

        return indexedMethods;
    }
}
