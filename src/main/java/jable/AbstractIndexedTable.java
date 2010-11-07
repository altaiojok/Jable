package jable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Brainard
 * @since 2010-11-06
 */
abstract class AbstractIndexedTable<E> implements IndexedTable<E> {
    final Class<E> clazz;
    private final Map<String, IndexDefinition<E>> indexDefinitionsByName;
    private final Map<IndexDefinition<E>, Map<Object, Collection<E>>> indexes;

    AbstractIndexedTable(Class<E> clazz) {
        this.clazz = clazz;
        this.indexes = Maps.newHashMap();
        this.indexDefinitionsByName = Maps.newHashMap();

        for (IndexDefinition<E> indexDef : buildIndexDefinitions()) {
            indexDefinitionsByName.put(indexDef.getName(), indexDef);
            indexes.put(indexDef, new HashMap<Object, Collection<E>>());
        }
    }

    abstract Collection<IndexDefinition<E>> buildIndexDefinitions();

    public boolean add(E e) {
        boolean hasChanged = false;

        for(Map.Entry<IndexDefinition<E>, Map<Object, Collection<E>>> indexEntry : indexes.entrySet()) {
            final IndexDefinition<E> indexDef = indexEntry.getKey();
            final Object indexableValue = indexDef.getIndexableValue(e);

            Collection<E> indexedElements = indexEntry.getValue().get(indexableValue);
            if (indexedElements == null) {
                indexedElements = Sets.newHashSet();
            } else if (indexDef.isUnique()
                       && !indexedElements.contains(e)
                       && indexEntry.getValue().containsKey(indexableValue)) {
                throw new UniqueConstraintViolation(indexDef.getName(), indexableValue.toString());
            }

            hasChanged |= indexedElements.add(e);
            indexEntry.getValue().put(indexableValue, indexedElements);
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

    public Collection<E> getByIndex(String indexName, Object whereValueIs) {
        return Preconditions.checkNotNull(indexes.get(indexDefinitionsByName.get(indexName)),
                                          "No index found for " + indexName + ".").get(whereValueIs);
    }

    public Collection<String> getIndexNames() {
        return indexDefinitionsByName.keySet();
    }

    public Collection<IndexDefinition<E>> getIndexDefinitions() {
        return indexes.keySet();
    }
}
