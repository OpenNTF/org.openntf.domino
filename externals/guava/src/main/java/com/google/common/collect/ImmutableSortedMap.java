/*
 * Copyright (C) 2009 The Guava Authors
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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.keyOrNull;

import com.google.common.annotations.GwtCompatible;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Nullable;

/**
 * An immutable {@link SortedMap}. Does not permit null keys or values.
 *
 * <p>Unlike {@link Collections#unmodifiableSortedMap}, which is a <i>view</i>
 * of a separate map which can still change, an instance of {@code
 * ImmutableSortedMap} contains its own data and will <i>never</i> change.
 * {@code ImmutableSortedMap} is convenient for {@code public static final} maps
 * ("constant maps") and also lets you easily make a "defensive copy" of a map
 * provided to your class by a caller.
 *
 * <p><b>Note:</b> Although this class is not final, it cannot be subclassed as
 * it has no public or protected constructors. Thus, instances of this class are
 * guaranteed to be immutable.
 *
 * <p>See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">
 * immutable collections</a>.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 * @since 2.0 (imported from Google Collections Library; implements {@code
 *        NavigableMap} since 12.0)
 */
@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSortedMap<K, V>
    extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
  /*
   * TODO(kevinb): Confirm that ImmutableSortedMap is faster to construct and
   * uses less memory than TreeMap; then say so in the class Javadoc.
   */
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();

  private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP =
      new EmptyImmutableSortedMap<Comparable, Object>(NATURAL_ORDER);

  static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
    if (Ordering.natural().equals(comparator)) {
      return of();
    } else {
      return new EmptyImmutableSortedMap<K, V>(comparator);
    }
  }

  static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(
      Comparator<? super K> comparator,
      int size,
      Entry<K, V>[] entries) {
    if (size == 0) {
      return emptyMap(comparator);
    }

    ImmutableList.Builder<K> keyBuilder = ImmutableList.builder();
    ImmutableList.Builder<V> valueBuilder = ImmutableList.builder();
    for (int i = 0; i < size; i++) {
      Entry<K, V> entry = entries[i];
      keyBuilder.add(entry.getKey());
      valueBuilder.add(entry.getValue());
    }

    return new RegularImmutableSortedMap<K, V>(
        new RegularImmutableSortedSet<K>(keyBuilder.build(), comparator),
        valueBuilder.build());
  }

  static <K, V> ImmutableSortedMap<K, V> from(
      ImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
    if (keySet.isEmpty()) {
      return emptyMap(keySet.comparator());
    } else {
      return new RegularImmutableSortedMap<K, V>(
          (RegularImmutableSortedSet<K>) keySet,
          valueList);
    }
  }

  /**
   * Returns the empty sorted map.
   */
  @SuppressWarnings("unchecked")
  // unsafe, comparator() returns a comparator on the specified type
  // TODO(kevinb): evaluate whether or not of().comparator() should return null
  public static <K, V> ImmutableSortedMap<K, V> of() {
    return (ImmutableSortedMap<K, V>) NATURAL_EMPTY_MAP;
  }

  /**
   * Returns an immutable map containing a single entry.
   */
  public static <K extends Comparable<? super K>, V>
      ImmutableSortedMap<K, V> of(K k1, V v1) {
    return from(ImmutableSortedSet.of(k1), ImmutableList.of(v1));
  }

  /**
   * Returns an immutable sorted map containing the given entries, sorted by the
   * natural ordering of their keys.
   *
   * @throws IllegalArgumentException if the two keys are equal according to
   *     their natural ordering
   */
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V>
      of(K k1, V v1, K k2, V v2) {
    return fromEntries(Ordering.natural(), false, 2, entryOf(k1, v1), entryOf(k2, v2));
  }

  /**
   * Returns an immutable sorted map containing the given entries, sorted by the
   * natural ordering of their keys.
   *
   * @throws IllegalArgumentException if any two keys are equal according to
   *     their natural ordering
   */
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V>
      of(K k1, V v1, K k2, V v2, K k3, V v3) {
    return fromEntries(Ordering.natural(), false, 3, entryOf(k1, v1), entryOf(k2, v2), 
        entryOf(k3, v3));
  }

  /**
   * Returns an immutable sorted map containing the given entries, sorted by the
   * natural ordering of their keys.
   *
   * @throws IllegalArgumentException if any two keys are equal according to
   *     their natural ordering
   */
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V>
      of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return fromEntries(Ordering.natural(), false, 4, entryOf(k1, v1), entryOf(k2, v2), 
        entryOf(k3, v3), entryOf(k4, v4));
  }

  /**
   * Returns an immutable sorted map containing the given entries, sorted by the
   * natural ordering of their keys.
   *
   * @throws IllegalArgumentException if any two keys are equal according to
   *     their natural ordering
   */
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V>
      of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    return fromEntries(Ordering.natural(), false, 5, entryOf(k1, v1), entryOf(k2, v2), 
        entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
  }

  /**
   * Returns an immutable map containing the same entries as {@code map}, sorted
   * by the natural ordering of the keys.
   *
   * <p>Despite the method name, this method attempts to avoid actually copying
   * the data when it is safe to do so. The exact circumstances under which a
   * copy will or will not be performed are undocumented and subject to change.
   *
   * <p>This method is not type-safe, as it may be called on a map with keys
   * that are not mutually comparable.
   *
   * @throws ClassCastException if the keys in {@code map} are not mutually
   *         comparable
   * @throws NullPointerException if any key or value in {@code map} is null
   * @throws IllegalArgumentException if any two keys are equal according to
   *         their natural ordering
   */
  public static <K, V> ImmutableSortedMap<K, V> copyOf(
      Map<? extends K, ? extends V> map) {
    // Hack around K not being a subtype of Comparable.
    // Unsafe, see ImmutableSortedSetFauxverideShim.
    @SuppressWarnings("unchecked")
    Ordering<K> naturalOrder = (Ordering<K>) Ordering.<Comparable>natural();
    return copyOfInternal(map, naturalOrder);
  }

  /**
   * Returns an immutable map containing the same entries as {@code map}, with
   * keys sorted by the provided comparator.
   *
   * <p>Despite the method name, this method attempts to avoid actually copying
   * the data when it is safe to do so. The exact circumstances under which a
   * copy will or will not be performed are undocumented and subject to change.
   *
   * @throws NullPointerException if any key or value in {@code map} is null
   * @throws IllegalArgumentException if any two keys are equal according to the
   *         comparator
   */
  public static <K, V> ImmutableSortedMap<K, V> copyOf(
      Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
    return copyOfInternal(map, checkNotNull(comparator));
  }

  /**
   * Returns an immutable map containing the same entries as the provided sorted
   * map, with the same ordering.
   *
   * <p>Despite the method name, this method attempts to avoid actually copying
   * the data when it is safe to do so. The exact circumstances under which a
   * copy will or will not be performed are undocumented and subject to change.
   *
   * @throws NullPointerException if any key or value in {@code map} is null
   */
  @SuppressWarnings("unchecked")
  public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(
      SortedMap<K, ? extends V> map) {
    Comparator<? super K> comparator = map.comparator();
    if (comparator == null) {
      // If map has a null comparator, the keys should have a natural ordering,
      // even though K doesn't explicitly implement Comparable.
      comparator = (Comparator<? super K>) NATURAL_ORDER;
    }
    return copyOfInternal(map, comparator);
  }

  private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(
      Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
    boolean sameComparator = false;
    if (map instanceof SortedMap) {
      SortedMap<?, ?> sortedMap = (SortedMap<?, ?>) map;
      Comparator<?> comparator2 = sortedMap.comparator();
      sameComparator = (comparator2 == null)
          ? comparator == NATURAL_ORDER
          : comparator.equals(comparator2);
    }

    if (sameComparator && (map instanceof ImmutableSortedMap)) {
      // TODO(kevinb): Prove that this cast is safe, even though
      // Collections.unmodifiableSortedMap requires the same key type.
      @SuppressWarnings("unchecked")
      ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap<K, V>) map;
      if (!kvMap.isPartialView()) {
        return kvMap;
      }
    }

    // "adding" type params to an array of a raw type should be safe as
    // long as no one can ever cast that same array instance back to a
    // raw type.
    @SuppressWarnings("unchecked")
    Entry<K, V>[] entries = map.entrySet().toArray(new Entry[0]);

    return fromEntries(comparator, sameComparator, entries.length, entries);
  }
  
  static <K, V> ImmutableSortedMap<K, V> fromEntries(
      Comparator<? super K> comparator, boolean sameComparator, int size, Entry<K, V>... entries) {
    for (int i = 0; i < size; i++) {
      Entry<K, V> entry = entries[i];
      entries[i] = entryOf(entry.getKey(), entry.getValue());
    }
    if (!sameComparator) {
      sortEntries(comparator, size, entries);
      validateEntries(size, entries, comparator);
    }

    return fromSortedEntries(comparator, size, entries);
  }

  private static <K, V> void sortEntries(
      final Comparator<? super K> comparator, int size, Entry<K, V>[] entries) {
    Arrays.sort(entries, 0, size, Ordering.from(comparator).<K>onKeys());
  }

  private static <K, V> void validateEntries(int size, Entry<K, V>[] entries,
      Comparator<? super K> comparator) {
    for (int i = 1; i < size; i++) {
      checkNoConflict(comparator.compare(entries[i - 1].getKey(), entries[i].getKey()) != 0, "key",
          entries[i - 1], entries[i]);
    }
  }

  /**
   * Returns a builder that creates immutable sorted maps whose keys are
   * ordered by their natural ordering. The sorted maps use {@link
   * Ordering#natural()} as the comparator.
   */
  public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
    return new Builder<K, V>(Ordering.natural());
  }

  /**
   * Returns a builder that creates immutable sorted maps with an explicit
   * comparator. If the comparator has a more general type than the map's keys,
   * such as creating a {@code SortedMap<Integer, String>} with a {@code
   * Comparator<Number>}, use the {@link Builder} constructor instead.
   *
   * @throws NullPointerException if {@code comparator} is null
   */
  public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
    return new Builder<K, V>(comparator);
  }

  /**
   * Returns a builder that creates immutable sorted maps whose keys are
   * ordered by the reverse of their natural ordering.
   */
  public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
    return new Builder<K, V>(Ordering.natural().reverse());
  }

  /**
   * A builder for creating immutable sorted map instances, especially {@code
   * public static final} maps ("constant maps"). Example: <pre>   {@code
   *
   *   static final ImmutableSortedMap<Integer, String> INT_TO_WORD =
   *       new ImmutableSortedMap.Builder<Integer, String>(Ordering.natural())
   *           .put(1, "one")
   *           .put(2, "two")
   *           .put(3, "three")
   *           .build();}</pre>
   *
   * <p>For <i>small</i> immutable sorted maps, the {@code ImmutableSortedMap.of()}
   * methods are even more convenient.
   *
   * <p>Builder instances can be reused - it is safe to call {@link #build}
   * multiple times to build multiple maps in series. Each map is a superset of
   * the maps created before it.
   *
   * @since 2.0 (imported from Google Collections Library)
   */
  public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
    private final Comparator<? super K> comparator;

    /**
     * Creates a new builder. The returned builder is equivalent to the builder
     * generated by {@link ImmutableSortedMap#orderedBy}.
     */
    @SuppressWarnings("unchecked")
    public Builder(Comparator<? super K> comparator) {
      this.comparator = checkNotNull(comparator);
    }

    /**
     * Associates {@code key} with {@code value} in the built map. Duplicate
     * keys, according to the comparator (which might be the keys' natural
     * order), are not allowed, and will cause {@link #build} to fail.
     */
    @Override public Builder<K, V> put(K key, V value) {
      super.put(key, value);
      return this;
    }

    /**
     * Adds the given {@code entry} to the map, making it immutable if
     * necessary. Duplicate keys, according to the comparator (which might be
     * the keys' natural order), are not allowed, and will cause {@link #build}
     * to fail.
     *
     * @since 11.0
     */
    @Override public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
      super.put(entry);
      return this;
    }

    /**
     * Associates all of the given map's keys and values in the built map.
     * Duplicate keys, according to the comparator (which might be the keys'
     * natural order), are not allowed, and will cause {@link #build} to fail.
     *
     * @throws NullPointerException if any key or value in {@code map} is null
     */
    @Override public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
      super.putAll(map);
      return this;
    }

    /**
     * Returns a newly-created immutable sorted map.
     *
     * @throws IllegalArgumentException if any two keys are equal according to
     *     the comparator (which might be the keys' natural order)
     */
    @Override public ImmutableSortedMap<K, V> build() {
      return fromEntries(comparator, false, size, entries);
    }
  }

  ImmutableSortedMap() {
  }

  ImmutableSortedMap(ImmutableSortedMap<K, V> descendingMap) {
    this.descendingMap = descendingMap;
  }

  @Override
  public int size() {
    return values().size();
  }

  @Override public boolean containsValue(@Nullable Object value) {
    return values().contains(value);
  }

  @Override boolean isPartialView() {
    return keySet().isPartialView() || values().isPartialView();
  }

  /**
   * Returns an immutable set of the mappings in this map, sorted by the key
   * ordering.
   */
  @Override public ImmutableSet<Entry<K, V>> entrySet() {
    return super.entrySet();
  }

  /**
   * Returns an immutable sorted set of the keys in this map.
   */
  @Override public abstract ImmutableSortedSet<K> keySet();

  /**
   * Returns an immutable collection of the values in this map, sorted by the
   * ordering of the corresponding keys.
   */
  @Override public abstract ImmutableCollection<V> values();

  /**
   * Returns the comparator that orders the keys, which is
   * {@link Ordering#natural()} when the natural ordering of the keys is used.
   * Note that its behavior is not consistent with {@link TreeMap#comparator()},
   * which returns {@code null} to indicate natural ordering.
   */
  @Override
  public Comparator<? super K> comparator() {
    return keySet().comparator();
  }

  @Override
  public K firstKey() {
    return keySet().first();
  }

  @Override
  public K lastKey() {
    return keySet().last();
  }

  /**
   * This method returns a {@code ImmutableSortedMap}, consisting of the entries
   * whose keys are less than {@code toKey}.
   *
   * <p>The {@link SortedMap#headMap} documentation states that a submap of a
   * submap throws an {@link IllegalArgumentException} if passed a {@code toKey}
   * greater than an earlier {@code toKey}. However, this method doesn't throw
   * an exception in that situation, but instead keeps the original {@code
   * toKey}.
   */
  @Override
  public ImmutableSortedMap<K, V> headMap(K toKey) {
    return headMap(toKey, false);
  }

  /**
   * This method returns a {@code ImmutableSortedMap}, consisting of the entries
   * whose keys are less than (or equal to, if {@code inclusive}) {@code toKey}.
   *
   * <p>The {@link SortedMap#headMap} documentation states that a submap of a
   * submap throws an {@link IllegalArgumentException} if passed a {@code toKey}
   * greater than an earlier {@code toKey}. However, this method doesn't throw
   * an exception in that situation, but instead keeps the original {@code
   * toKey}.
   *
   * @since 12.0
   */
  @Override
  public abstract ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive);

  /**
   * This method returns a {@code ImmutableSortedMap}, consisting of the entries
   * whose keys ranges from {@code fromKey}, inclusive, to {@code toKey},
   * exclusive.
   *
   * <p>The {@link SortedMap#subMap} documentation states that a submap of a
   * submap throws an {@link IllegalArgumentException} if passed a {@code
   * fromKey} less than an earlier {@code fromKey}. However, this method doesn't
   * throw an exception in that situation, but instead keeps the original {@code
   * fromKey}. Similarly, this method keeps the original {@code toKey}, instead
   * of throwing an exception, if passed a {@code toKey} greater than an earlier
   * {@code toKey}.
   */
  @Override
  public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
    return subMap(fromKey, true, toKey, false);
  }

  /**
   * This method returns a {@code ImmutableSortedMap}, consisting of the entries
   * whose keys ranges from {@code fromKey} to {@code toKey}, inclusive or
   * exclusive as indicated by the boolean flags.
   *
   * <p>The {@link SortedMap#subMap} documentation states that a submap of a
   * submap throws an {@link IllegalArgumentException} if passed a {@code
   * fromKey} less than an earlier {@code fromKey}. However, this method doesn't
   * throw an exception in that situation, but instead keeps the original {@code
   * fromKey}. Similarly, this method keeps the original {@code toKey}, instead
   * of throwing an exception, if passed a {@code toKey} greater than an earlier
   * {@code toKey}.
   *
   * @since 12.0
   */
  @Override
  public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey,
      boolean toInclusive) {
    checkNotNull(fromKey);
    checkNotNull(toKey);
    checkArgument(comparator().compare(fromKey, toKey) <= 0,
        "expected fromKey <= toKey but %s > %s", fromKey, toKey);
    return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
  }

  /**
   * This method returns a {@code ImmutableSortedMap}, consisting of the entries
   * whose keys are greater than or equals to {@code fromKey}.
   *
   * <p>The {@link SortedMap#tailMap} documentation states that a submap of a
   * submap throws an {@link IllegalArgumentException} if passed a {@code
   * fromKey} less than an earlier {@code fromKey}. However, this method doesn't
   * throw an exception in that situation, but instead keeps the original {@code
   * fromKey}.
   */
  @Override
  public ImmutableSortedMap<K, V> tailMap(K fromKey) {
    return tailMap(fromKey, true);
  }

  /**
   * This method returns a {@code ImmutableSortedMap}, consisting of the entries
   * whose keys are greater than (or equal to, if {@code inclusive})
   * {@code fromKey}.
   *
   * <p>The {@link SortedMap#tailMap} documentation states that a submap of a
   * submap throws an {@link IllegalArgumentException} if passed a {@code
   * fromKey} less than an earlier {@code fromKey}. However, this method doesn't
   * throw an exception in that situation, but instead keeps the original {@code
   * fromKey}.
   *
   * @since 12.0
   */
  @Override
  public abstract ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive);

  @Override
  public Entry<K, V> lowerEntry(K key) {
    return headMap(key, false).lastEntry();
  }

  @Override
  public K lowerKey(K key) {
    return keyOrNull(lowerEntry(key));
  }

  @Override
  public Entry<K, V> floorEntry(K key) {
    return headMap(key, true).lastEntry();
  }

  @Override
  public K floorKey(K key) {
    return keyOrNull(floorEntry(key));
  }

  @Override
  public Entry<K, V> ceilingEntry(K key) {
    return tailMap(key, true).firstEntry();
  }

  @Override
  public K ceilingKey(K key) {
    return keyOrNull(ceilingEntry(key));
  }

  @Override
  public Entry<K, V> higherEntry(K key) {
    return tailMap(key, false).firstEntry();
  }

  @Override
  public K higherKey(K key) {
    return keyOrNull(higherEntry(key));
  }

  @Override
  public Entry<K, V> firstEntry() {
    return isEmpty() ? null : entrySet().asList().get(0);
  }

  @Override
  public Entry<K, V> lastEntry() {
    return isEmpty() ? null : entrySet().asList().get(size() - 1);
  }

  /**
   * Guaranteed to throw an exception and leave the map unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated
  @Override
  public final Entry<K, V> pollFirstEntry() {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the map unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated
  @Override
  public final Entry<K, V> pollLastEntry() {
    throw new UnsupportedOperationException();
  }

  private transient ImmutableSortedMap<K, V> descendingMap;

  @Override
  public ImmutableSortedMap<K, V> descendingMap() {
    ImmutableSortedMap<K, V> result = descendingMap;
    if (result == null) {
      result = descendingMap = createDescendingMap();
    }
    return result;
  }

  abstract ImmutableSortedMap<K, V> createDescendingMap();

  @Override
  public ImmutableSortedSet<K> navigableKeySet() {
    return keySet();
  }

  @Override
  public ImmutableSortedSet<K> descendingKeySet() {
    return keySet().descendingSet();
  }

  /**
   * Serialized type for all ImmutableSortedMap instances. It captures the
   * logical contents and they are reconstructed using public factory methods.
   * This ensures that the implementation types remain as implementation
   * details.
   */
  private static class SerializedForm extends ImmutableMap.SerializedForm {
    private final Comparator<Object> comparator;
    @SuppressWarnings("unchecked")
    SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
      super(sortedMap);
      comparator = (Comparator<Object>) sortedMap.comparator();
    }
    @Override Object readResolve() {
      Builder<Object, Object> builder = new Builder<Object, Object>(comparator);
      return createMap(builder);
    }
    private static final long serialVersionUID = 0;
  }

  @Override Object writeReplace() {
    return new SerializedForm(this);
  }

  // This class is never actually serialized directly, but we have to make the
  // warning go away (and suppressing would suppress for all nested classes too)
  private static final long serialVersionUID = 0;
}
