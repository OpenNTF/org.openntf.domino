/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;

import javax.annotation.Nullable;

/**
 * An immutable {@link ListMultimap} with reliable user-specified key and value
 * iteration order. Does not permit null keys or values.
 *
 * <p>Unlike {@link Multimaps#unmodifiableListMultimap(ListMultimap)}, which is
 * a <i>view</i> of a separate multimap which can still change, an instance of
 * {@code ImmutableListMultimap} contains its own data and will <i>never</i>
 * change. {@code ImmutableListMultimap} is convenient for
 * {@code public static final} multimaps ("constant multimaps") and also lets
 * you easily make a "defensive copy" of a multimap provided to your class by
 * a caller.
 *
 * <p><b>Note:</b> Although this class is not final, it cannot be subclassed as
 * it has no public or protected constructors. Thus, instances of this class
 * are guaranteed to be immutable.
 *
 * <p>See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">
 * immutable collections</a>.
 *
 * @author Jared Levy
 * @since 2.0 (imported from Google Collections Library)
 */
@GwtCompatible(serializable = true, emulated = true)
public class ImmutableListMultimap<K, V>
    extends ImmutableMultimap<K, V>
    implements ListMultimap<K, V> {

  /** Returns the empty multimap. */
  // Casting is safe because the multimap will never hold any elements.
  @SuppressWarnings("unchecked")
  public static <K, V> ImmutableListMultimap<K, V> of() {
    return (ImmutableListMultimap<K, V>) EmptyImmutableListMultimap.INSTANCE;
  }

  /**
   * Returns an immutable multimap containing a single entry.
   */
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    builder.put(k1, v1);
    return builder.build();
  }

  /**
   * Returns an immutable multimap containing the given entries, in order.
   */
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    return builder.build();
  }

  /**
   * Returns an immutable multimap containing the given entries, in order.
   */
  public static <K, V> ImmutableListMultimap<K, V> of(
      K k1, V v1, K k2, V v2, K k3, V v3) {
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    builder.put(k3, v3);
    return builder.build();
  }

  /**
   * Returns an immutable multimap containing the given entries, in order.
   */
  public static <K, V> ImmutableListMultimap<K, V> of(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    builder.put(k3, v3);
    builder.put(k4, v4);
    return builder.build();
  }

  /**
   * Returns an immutable multimap containing the given entries, in order.
   */
  public static <K, V> ImmutableListMultimap<K, V> of(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    ImmutableListMultimap.Builder<K, V> builder
        = ImmutableListMultimap.builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    builder.put(k3, v3);
    builder.put(k4, v4);
    builder.put(k5, v5);
    return builder.build();
  }

  // looking for of() with > 5 entries? Use the builder instead.

  /**
   * Returns a new builder. The generated builder is equivalent to the builder
   * created by the {@link Builder} constructor.
   */
  public static <K, V> Builder<K, V> builder() {
    return new Builder<K, V>();
  }

  /**
   * A builder for creating immutable {@code ListMultimap} instances, especially
   * {@code public static final} multimaps ("constant multimaps"). Example:
   * <pre>   {@code
   *
   *   static final Multimap<String, Integer> STRING_TO_INTEGER_MULTIMAP =
   *       new ImmutableListMultimap.Builder<String, Integer>()
   *           .put("one", 1)
   *           .putAll("several", 1, 2, 3)
   *           .putAll("many", 1, 2, 3, 4, 5)
   *           .build();}</pre>
   *
   * <p>Builder instances can be reused; it is safe to call {@link #build} multiple
   * times to build multiple multimaps in series. Each multimap contains the
   * key-value mappings in the previously created multimaps.
   *
   * @since 2.0 (imported from Google Collections Library)
   */
  public static final class Builder<K, V>
      extends ImmutableMultimap.Builder<K, V> {
    /**
     * Creates a new builder. The returned builder is equivalent to the builder
     * generated by {@link ImmutableListMultimap#builder}.
     */
    public Builder() {}

    @Override public Builder<K, V> put(K key, V value) {
      super.put(key, value);
      return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 11.0
     */
    @Override public Builder<K, V> put(
        Entry<? extends K, ? extends V> entry) {
      super.put(entry);
      return this;
    }

    @Override public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
      super.putAll(key, values);
      return this;
    }

    @Override public Builder<K, V> putAll(K key, V... values) {
      super.putAll(key, values);
      return this;
    }

    @Override public Builder<K, V> putAll(
        Multimap<? extends K, ? extends V> multimap) {
      super.putAll(multimap);
      return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 8.0
     */
    @Override
    public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
      super.orderKeysBy(keyComparator);
      return this;
    }

    /**
     * {@inheritDoc}
     *
     * @since 8.0
     */
    @Override
    public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
      super.orderValuesBy(valueComparator);
      return this;
    }

    /**
     * Returns a newly-created immutable list multimap.
     */
    @Override public ImmutableListMultimap<K, V> build() {
      return (ImmutableListMultimap<K, V>) super.build();
    }
  }

  /**
   * Returns an immutable multimap containing the same mappings as {@code
   * multimap}. The generated multimap's key and value orderings correspond to
   * the iteration ordering of the {@code multimap.asMap()} view.
   *
   * <p>Despite the method name, this method attempts to avoid actually copying
   * the data when it is safe to do so. The exact circumstances under which a
   * copy will or will not be performed are undocumented and subject to change.
   *
   * @throws NullPointerException if any key or value in {@code multimap} is
   *         null
   */
  public static <K, V> ImmutableListMultimap<K, V> copyOf(
      Multimap<? extends K, ? extends V> multimap) {
    if (multimap.isEmpty()) {
      return of();
    }

    // TODO(user): copy ImmutableSetMultimap by using asList() on the sets
    if (multimap instanceof ImmutableListMultimap) {
      @SuppressWarnings("unchecked") // safe since multimap is not writable
      ImmutableListMultimap<K, V> kvMultimap
          = (ImmutableListMultimap<K, V>) multimap;
      if (!kvMultimap.isPartialView()) {
        return kvMultimap;
      }
    }

    ImmutableMap.Builder<K, ImmutableList<V>> builder = ImmutableMap.builder();
    int size = 0;

    for (Entry<? extends K, ? extends Collection<? extends V>> entry
        : multimap.asMap().entrySet()) {
      ImmutableList<V> list = ImmutableList.copyOf(entry.getValue());
      if (!list.isEmpty()) {
        builder.put(entry.getKey(), list);
        size += list.size();
      }
    }

    return new ImmutableListMultimap<K, V>(builder.build(), size);
  }

  ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
    super(map, size);
  }

  // views

  /**
   * Returns an immutable list of the values for the given key.  If no mappings
   * in the multimap have the provided key, an empty immutable list is
   * returned. The values are in the same order as the parameters used to build
   * this multimap.
   */
  @Override public ImmutableList<V> get(@Nullable K key) {
    // This cast is safe as its type is known in constructor.
    ImmutableList<V> list = (ImmutableList<V>) map.get(key);
    return (list == null) ? ImmutableList.<V>of() : list;
  }

  private transient ImmutableListMultimap<V, K> inverse;

  /**
   * {@inheritDoc}
   *
   * <p>Because an inverse of a list multimap can contain multiple pairs with
   * the same key and value, this method returns an {@code
   * ImmutableListMultimap} rather than the {@code ImmutableMultimap} specified
   * in the {@code ImmutableMultimap} class.
   *
   * @since 11.0
   */
  @Override
  public ImmutableListMultimap<V, K> inverse() {
    ImmutableListMultimap<V, K> result = inverse;
    return (result == null) ? (inverse = invert()) : result;
  }

  private ImmutableListMultimap<V, K> invert() {
    Builder<V, K> builder = builder();
    for (Entry<K, V> entry : entries()) {
      builder.put(entry.getValue(), entry.getKey());
    }
    ImmutableListMultimap<V, K> invertedMultimap = builder.build();
    invertedMultimap.inverse = this;
    return invertedMultimap;
  }

  /**
   * Guaranteed to throw an exception and leave the multimap unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated @Override public ImmutableList<V> removeAll(Object key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the multimap unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated @Override public ImmutableList<V> replaceValues(
      K key, Iterable<? extends V> values) {
    throw new UnsupportedOperationException();
  }

  /**
   * @serialData number of distinct keys, and then for each distinct key: the
   *     key, the number of values for that key, and the key's values
   */
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    Serialization.writeMultimap(this, stream);
  }

  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    int keyCount = stream.readInt();
    if (keyCount < 0) {
      throw new InvalidObjectException("Invalid key count " + keyCount);
    }
    ImmutableMap.Builder<Object, ImmutableList<Object>> builder
        = ImmutableMap.builder();
    int tmpSize = 0;

    for (int i = 0; i < keyCount; i++) {
      Object key = stream.readObject();
      int valueCount = stream.readInt();
      if (valueCount <= 0) {
        throw new InvalidObjectException("Invalid value count " + valueCount);
      }

      Object[] array = new Object[valueCount];
      for (int j = 0; j < valueCount; j++) {
        array[j] = stream.readObject();
      }
      builder.put(key, ImmutableList.copyOf(array));
      tmpSize += valueCount;
    }

    ImmutableMap<Object, ImmutableList<Object>> tmpMap;
    try {
      tmpMap = builder.build();
    } catch (IllegalArgumentException e) {
      throw (InvalidObjectException)
          new InvalidObjectException(e.getMessage()).initCause(e);
    }

    FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
    FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
  }

  @GwtIncompatible("Not needed in emulated source")
  private static final long serialVersionUID = 0;
}
