package jable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Ryan Brainard
 * @since 2010-11-03
 */
public class MethodIndexedTable<E> implements IndexedTable<E> {

    private final Class<E> clazz;
    private final Map<Method, Map<Object, Collection<E>>> indexes;

    public MethodIndexedTable(Class<E> clazz) {
        this.clazz = clazz;

        indexes = new HashMap<Method, Map<Object, Collection<E>>>();

        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Indexed.class) != null) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException("Methods with paramethers cannot be indexed.");
                }

                if (method.getReturnType() == null) {
                    throw new IllegalArgumentException("Methods without return values cannot be indexed");
                }

                indexes.put(method, new HashMap<Object, Collection<E>>());
            }
        }
    }

    public boolean add(E e) {
        boolean hasChanged = false;

        for(Method indexBy : indexes.keySet()) {
            final Map<Object, Collection<E>> index = indexes.get(indexBy);
            final Object indexedMethodValue;
            try {
                indexedMethodValue = indexBy.invoke(e);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            Collection<E> indexedMap = index.get(indexedMethodValue);
            indexedMap = indexedMap != null ? indexedMap : new HashSet<E>();
            hasChanged |= indexedMap.add(e);
            index.put(indexedMethodValue, indexedMap);
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
        final Method methodIndex;
        try {
            methodIndex = clazz.getMethod(indexName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }

        return Preconditions.checkNotNull(indexes.get(methodIndex),
                "No index found for " + methodIndex.getName() + ". Be sure to annotate method as @Indexed.").get(value);
    }

    public Collection<String> getIndexNames() {
        Collection<String> indexNames = Sets.newHashSet();

        for (Method method : indexes.keySet()) {
            indexNames.add(method.getName());
        }

        return indexNames;
    }
}
