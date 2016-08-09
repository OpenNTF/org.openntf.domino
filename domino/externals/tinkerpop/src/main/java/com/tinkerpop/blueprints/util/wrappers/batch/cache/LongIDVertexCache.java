package com.tinkerpop.blueprints.util.wrappers.batch.cache;

import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.LongObjectMap;
import com.carrotsearch.hppc.LongObjectOpenHashMap;
import com.carrotsearch.hppc.procedures.LongProcedure;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author Matthias Broecheler (http://www.matthiasb.com)
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class LongIDVertexCache implements VertexCache {

    private final LongObjectMap<Object> map;
    private final LongArrayList mapKeysInCurrentTx;
    private final LongProcedure newTransactionProcedure;

    public LongIDVertexCache() {
        map = new LongObjectOpenHashMap<Object>(AbstractIDVertexCache.INITIAL_CAPACITY);
        mapKeysInCurrentTx = new LongArrayList(AbstractIDVertexCache.INITIAL_TX_CAPACITY);
        newTransactionProcedure = new VertexConverterLP();
    }

    private static long getID(final Object externalID) {
        if (!(externalID instanceof Number)) throw new IllegalArgumentException("Number expected.");
        return ((Number) externalID).longValue();
    }

    @Override
    public Object getEntry(final Object externalId) {
        return map.get(getID(externalId));
    }

    @Override
    public void set(final Vertex vertex, final Object externalId) {
        setId(vertex, externalId);
    }

    @Override
    public void setId(final Object vertexId, final Object externalId) {
        final long id = getID(externalId);
        map.put(id, vertexId);
        mapKeysInCurrentTx.add(id);
    }

    @Override
    public boolean contains(final Object externalId) {
        return map.containsKey(getID(externalId));
    }

    @Override
    public void newTransaction() {
        mapKeysInCurrentTx.forEach(newTransactionProcedure);
        mapKeysInCurrentTx.clear();
    }

    /**
     * See {@link LongIDVertexCache#newTransaction()}
     */
    private class VertexConverterLP implements LongProcedure {
        /**
         * Retrieve the Object associated with each long from {@code map}. If it
         * is an {@code instanceof Vertex}, then replace it in the map with
         * {@link Vertex#getId()}. Otherwise, do nothing.
         */
        @Override
        public void apply(long l) {
            final Object o = map.get(l);
            assert null != o;
            if (o instanceof Vertex) {
                map.put(l, ((Vertex) o).getId());
            }
        }
    }
}