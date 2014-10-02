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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

/**
 * An immutable hash-based multiset. Does not permit null elements.
 *
 * <p>Its iterator orders elements according to the first appearance of the
 * element among the items passed to the factory method or builder. When the
 * multiset contains multiple instances of an element, those instances are
 * consecutive in the iteration order.
 *
 * <p>See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">
 * immutable collections</a>.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 * @since 2.0 (imported from Google Collections Library)
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // we're overriding default serialization
// TODO(user): write an efficient asList() implementation
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E>
    implements Multiset<E> {

  private static final ImmutableMultiset<Object> EMPTY =
      new RegularImmutableMultiset<Object>(ImmutableMap.<Object, Integer>of(), 0);

  /**
   * Returns the empty immutable multiset.
   */
  @SuppressWarnings("unchecked") // all supported methods are covariant
  public static <E> ImmutableMultiset<E> of() {
    return (ImmutableMultiset<E>) EMPTY;
  }

  /**
   * Returns an immutable multiset containing a single element.
   *
   * @throws NullPointerException if {@code element} is null
   * @since 6.0 (source-compatible since 2.0)
   */
  @SuppressWarnings("unchecked") // generic array created but never written
  public static <E> ImmutableMultiset<E> of(E element) {
    return copyOfInternal(element);
  }

  /**
   * Returns an immutable multiset containing the given elements, in order.
   *
   * @throws NullPointerException if any element is null
   * @since 6.0 (source-compatible since 2.0)
   */
  @SuppressWarnings("unchecked") //
  public static <E> ImmutableMultiset<E> of(E e1, E e2) {
    return copyOfInternal(e1, e2);
  }

  /**
   * Returns an immutable multiset containing the given elements, in order.
   *
   * @throws NullPointerException if any element is null
   * @since 6.0 (source-compatible since 2.0)
   */
  @SuppressWarnings("unchecked") //
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
    return copyOfInternal(e1, e2, e3);
  }

  /**
   * Returns an immutable multiset containing the given elements, in order.
   *
   * @throws NullPointerException if any element is null
   * @since 6.0 (source-compatible since 2.0)
   */
  @SuppressWarnings("unchecked") //
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
    return copyOfInternal(e1, e2, e3, e4);
  }

  /**
   * Returns an immutable multiset containing the given elements, in order.
   *
   * @throws NullPointerException if any element is null
   * @since 6.0 (source-compatible since 2.0)
   */
  @SuppressWarnings("unchecked") //
  public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
    return copyOfInternal(e1, e2, e3, e4, e5);
  }

  /**
   * Returns an immutable multiset containing the given elements, in order.
   *
   * @throws NullPointerException if any element is null
   * @since 6.0 (source-compatible since 2.0)
   */
  @SuppressWarnings("unchecked") //
  public static <E> ImmutableMultiset<E> of(
      E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
    return new Builder<E>()
        .add(e1)
        .add(e2)
        .add(e3)
        .add(e4)
        .add(e5)
        .add(e6)
        .add(others)
        .build();
  }

  /**
   * Returns an immutable multiset containing the given elements.
   *
   * <p>The multiset is ordered by the first occurrence of each element. For
   * example, {@code ImmutableMultiset.copyOf([2, 3, 1, 3])} yields a multiset
   * with elements in the order {@code 2, 3, 3, 1}.
   *
   * @throws NullPointerException if any of {@code elements} is null
   * @since 6.0
   */
  public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
    return copyOf(Arrays.asList(elements));
  }

  /**
   * Returns an immutable multiset containing the given elements.
   *
   * <p>The multiset is ordered by the first occurrence of each element. For
   * example, {@code ImmutableMultiset.copyOf(Arrays.asList(2, 3, 1, 3))} yields
   * a multiset with elements in the order {@code 2, 3, 3, 1}.
   *
   * <p>Despite the method name, this method attempts to avoid actually copying
   * the data when it is safe to do so. The exact circumstances under which a
   * copy will or will not be performed are undocumented and subject to change.
   *
   * <p><b>Note:</b> Despite what the method name suggests, if {@code elements}
   * is an {@code ImmutableMultiset}, no copy will actually be performed, and
   * the given multiset itself will be returned.
   *
   * @throws NullPointerException if any of {@code elements} is null
   */
  public static <E> ImmutableMultiset<E> copyOf(
      Iterable<? extends E> elements) {
    if (elements instanceof ImmutableMultiset) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableMultiset<E> result = (ImmutableMultiset<E>) elements;
      if (!result.isPartialView()) {
        return result;
      }
    }

    Multiset<? extends E> multiset = (elements instanceof Multiset)
        ? Multisets.cast(elements)
        : LinkedHashMultiset.create(elements);

    return copyOfInternal(multiset);
  }

  private static <E> ImmutableMultiset<E> copyOfInternal(E... elements) {
    return copyOf(Arrays.asList(elements));
  }

  private static <E> ImmutableMultiset<E> copyOfInternal(
      Multiset<? extends E> multiset) {
    return copyFromEntries(multiset.entrySet());
  }

  static <E> ImmutableMultiset<E> copyFromEntries(
      Collection<? extends Entry<? extends E>> entries) {
    long size = 0;
    ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
    for (Entry<? extends E> entry : entries) {
      int count = entry.getCount();
      if (count > 0) {
        // Since ImmutableMap.Builder throws an NPE if an element is null, no
        // other null checks are needed.
        builder.put(entry.getElement(), count);
        size += count;
      }
    }

    if (size == 0) {
      return of();
    }
    return new RegularImmutableMultiset<E>(
        builder.build(), Ints.saturatedCast(size));
  }

  /**
   * Returns an immutable multiset containing the given elements.
   *
   * <p>The multiset is ordered by the first occurrence of each element. For
   * example,
   * {@code ImmutableMultiset.copyOf(Arrays.asList(2, 3, 1, 3).iterator())}
   * yields a multiset with elements in the order {@code 2, 3, 3, 1}.
   *
   * @throws NullPointerException if any of {@code elements} is null
   */
  public static <E> ImmutableMultiset<E> copyOf(
      Iterator<? extends E> elements) {
    Multiset<E> multiset = LinkedHashMultiset.create();
    Iterators.addAll(multiset, elements);
    return copyOfInternal(multiset);
  }

  ImmutableMultiset() {}

  @Override public UnmodifiableIterator<E> iterator() {
    final Iterator<Entry<E>> entryIterator = entrySet().iterator();
    return new UnmodifiableIterator<E>() {
      int remaining;
      E element;

      @Override
      public boolean hasNext() {
        return (remaining > 0) || entryIterator.hasNext();
      }

      @Override
      public E next() {
        if (remaining <= 0) {
          Entry<E> entry = entryIterator.next();
          element = entry.getElement();
          remaining = entry.getCount();
        }
        remaining--;
        return element;
      }
    };
  }

  @Override
  public boolean contains(@Nullable Object object) {
    return count(object) > 0;
  }

  @Override
  public boolean containsAll(Collection<?> targets) {
    return elementSet().containsAll(targets);
  }

  /**
   * Guaranteed to throw an exception and leave the collection unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated
  @Override
  public final int add(E element, int occurrences) {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the collection unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated
  @Override
  public final int remove(Object element, int occurrences) {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the collection unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated
  @Override
  public final int setCount(E element, int count) {
    throw new UnsupportedOperationException();
  }

  /**
   * Guaranteed to throw an exception and leave the collection unmodified.
   *
   * @throws UnsupportedOperationException always
   * @deprecated Unsupported operation.
   */
  @Deprecated
  @Override
  public final boolean setCount(E element, int oldCount, int newCount) {
    throw new UnsupportedOperationException();
  }

  @GwtIncompatible("not present in emulated superclass")
  @Override
  int copyIntoArray(Object[] dst, int offset) {
    for (Multiset.Entry<E> entry : entrySet()) {
      Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
      offset += entry.getCount();
    }
    return offset;
  }

  @Override public boolean equals(@Nullable Object object) {
    return Multisets.equalsImpl(this, object);
  }

  @Override public int hashCode() {
    return Sets.hashCodeImpl(entrySet());
  }

  @Override public String toString() {
    return entrySet().toString();
  }

  private transient ImmutableSet<Entry<E>> entrySet;

  @Override
  public ImmutableSet<Entry<E>> entrySet() {
    ImmutableSet<Entry<E>> es = entrySet;
    return (es == null) ? (entrySet = createEntrySet()) : es;
  }

  private final ImmutableSet<Entry<E>> createEntrySet() {
    return isEmpty() ? ImmutableSet.<Entry<E>>of() : new EntrySet();
  }

  abstract Entry<E> getEntry(int index);

  private final class EntrySet extends ImmutableSet<Entry<E>> {
    @Override
    boolean isPartialView() {
      return ImmutableMultiset.this.isPartialView();
    }

    @Override
    public UnmodifiableIterator<Entry<E>> iterator() {
      return asList().iterator();
    }

    @Override
    ImmutableList<Entry<E>> createAsList() {
      return new ImmutableAsList<Entry<E>>() {
        @Override
        public Entry<E> get(int index) {
          return getEntry(index);
        }

        @Override
        ImmutableCollection<Entry<E>> delegateCollection() {
          return EntrySet.this;
        }
      };
    }

    @Override
    public int size() {
      return elementSet().size();
    }

    @Override
    public boolean contains(Object o) {
      if (o instanceof Entry) {
        Entry<?> entry = (Entry<?>) o;
        if (entry.getCount() <= 0) {
          return false;
        }
        int count = count(entry.getElement());
        return count == entry.getCount();
      }
      return false;
    }

    @Override
    public int hashCode() {
      return ImmutableMultiset.this.hashCode();
    }

    // We can't label this with @Override, because it doesn't override anything
    // in the GWT emulated version.
    // TODO(cpovirk): try making all copies of this method @GwtIncompatible instead
    Object writeReplace() {
      return new EntrySetSerializedForm<E>(ImmutableMultiset.this);
    }

    private static final long serialVersionUID = 0;
  }

  static class EntrySetSerializedForm<E> implements Serializable {
    final ImmutableMultiset<E> multiset;

    EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
      this.multiset = multiset;
    }

    Object readResolve() {
      return multiset.entrySet();
    }
  }

  private static class SerializedForm implements Serializable {
    final Object[] elements;
    final int[] counts;

    SerializedForm(Multiset<?> multiset) {
      int distinct = multiset.entrySet().size();
      elements = new Object[distinct];
      counts = new int[distinct];
      int i = 0;
      for (Entry<?> entry : multiset.entrySet()) {
        elements[i] = entry.getElement();
        counts[i] = entry.getCount();
        i++;
      }
    }

    Object readResolve() {
      LinkedHashMultiset<Object> multiset =
          LinkedHashMultiset.create(elements.length);
      for (int i = 0; i < elements.length; i++) {
        multiset.add(elements[i], counts[i]);
      }
      return ImmutableMultiset.copyOf(multiset);
    }

    private static final long serialVersionUID = 0;
  }

  // We can't label this with @Override, because it doesn't override anything
  // in the GWT emulated version.
  Object writeReplace() {
    return new SerializedForm(this);
  }

  /**
   * Returns a new builder. The generated builder is equivalent to the builder
   * created by the {@link Builder} constructor.
   */
  public static <E> Builder<E> builder() {
    return new Builder<E>();
  }

  /**
   * A builder for creating immutable multiset instances, especially {@code
   * public static final} multisets ("constant multisets"). Example:
   * <pre> {@code
   *
   *   public static final ImmutableMultiset<Bean> BEANS =
   *       new ImmutableMultiset.Builder<Bean>()
   *           .addCopies(Bean.COCOA, 4)
   *           .addCopies(Bean.GARDEN, 6)
   *           .addCopies(Bean.RED, 8)
   *           .addCopies(Bean.BLACK_EYED, 10)
   *           .build();}</pre>
   *
   * <p>Builder instances can be reused; it is safe to call {@link #build} multiple
   * times to build multiple multisets in series.
   *
   * @since 2.0 (imported from Google Collections Library)
   */
  public static class Builder<E> extends ImmutableCollection.Builder<E> {
    final Multiset<E> contents;

    /**
     * Creates a new builder. The returned builder is equivalent to the builder
     * generated by {@link ImmutableMultiset#builder}.
     */
    public Builder() {
      this(LinkedHashMultiset.<E>create());
    }

    Builder(Multiset<E> contents) {
      this.contents = contents;
    }

    /**
     * Adds {@code element} to the {@code ImmutableMultiset}.
     *
     * @param element the element to add
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code element} is null
     */
    @Override public Builder<E> add(E element) {
      contents.add(checkNotNull(element));
      return this;
    }

    /**
     * Adds a number of occurrences of an element to this {@code
     * ImmutableMultiset}.
     *
     * @param element the element to add
     * @param occurrences the number of occurrences of the element to add. May
     *     be zero, in which case no change will be made.
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code element} is null
     * @throws IllegalArgumentException if {@code occurrences} is negative, or
     *     if this operation would result in more than {@link Integer#MAX_VALUE}
     *     occurrences of the element
     */
    public Builder<E> addCopies(E element, int occurrences) {
      contents.add(checkNotNull(element), occurrences);
      return this;
    }

    /**
     * Adds or removes the necessary occurrences of an element such that the
     * element attains the desired count.
     *
     * @param element the element to add or remove occurrences of
     * @param count the desired count of the element in this multiset
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code element} is null
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public Builder<E> setCount(E element, int count) {
      contents.setCount(checkNotNull(element), count);
      return this;
    }

    /**
     * Adds each element of {@code elements} to the {@code ImmutableMultiset}.
     *
     * @param elements the elements to add
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code elements} is null or contains a
     *     null element
     */
    @Override public Builder<E> add(E... elements) {
      super.add(elements);
      return this;
    }

    /**
     * Adds each element of {@code elements} to the {@code ImmutableMultiset}.
     *
     * @param elements the {@code Iterable} to add to the {@code
     *     ImmutableMultiset}
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code elements} is null or contains a
     *     null element
     */
    @Override public Builder<E> addAll(Iterable<? extends E> elements) {
      if (elements instanceof Multiset) {
        Multiset<? extends E> multiset = Multisets.cast(elements);
        for (Entry<? extends E> entry : multiset.entrySet()) {
          addCopies(entry.getElement(), entry.getCount());
        }
      } else {
        super.addAll(elements);
      }
      return this;
    }

    /**
     * Adds each element of {@code elements} to the {@code ImmutableMultiset}.
     *
     * @param elements the elements to add to the {@code ImmutableMultiset}
     * @return this {@code Builder} object
     * @throws NullPointerException if {@code elements} is null or contains a
     *     null element
     */
    @Override public Builder<E> addAll(Iterator<? extends E> elements) {
      super.addAll(elements);
      return this;
    }

    /**
     * Returns a newly-created {@code ImmutableMultiset} based on the contents
     * of the {@code Builder}.
     */
    @Override public ImmutableMultiset<E> build() {
      return copyOf(contents);
    }
  }
}
