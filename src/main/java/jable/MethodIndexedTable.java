package jable;

import com.google.common.collect.Sets;

import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTable<E> extends AbstractIndexedTable<E> {

    public MethodIndexedTable(Class<E> clazz) {
        super(ElementType.METHOD, clazz);
    }

    Collection<String> findIndexedMembers() {
        final Collection<String> indexedMethods = Sets.newHashSet();

        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Indexed.class) != null) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException("Methods with parameters cannot be indexed.");
                }

                if (method.getReturnType() == null) {
                    throw new IllegalArgumentException("Methods without return values cannot be indexed");
                }

                indexedMethods.add(method.getName());
            }
        }

        return indexedMethods;
    }

    public boolean add(E e) {
        boolean hasChanged = false;

        for(String indexBy : indexes.keySet()) {
            final Map<Object, Collection<E>> index = indexes.get(indexBy);
            final Object indexedMethodValue;
            try {
                indexedMethodValue = clazz.getMethod(indexBy).invoke(e);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
            Collection<E> indexedMap = index.get(indexedMethodValue);
            indexedMap = indexedMap != null ? indexedMap : new HashSet<E>();
            hasChanged |= indexedMap.add(e);
            index.put(indexedMethodValue, indexedMap);
        }

        return hasChanged;
    }
}
