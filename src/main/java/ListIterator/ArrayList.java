package ListIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Realization of a list by means of a dynamic array. This is a simplified version
 * of the java.util.ArrayList class.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class ArrayList<E> implements List<E> {
  // instance variables
  /** Default array capacity. */
  public static final int CAPACITY=16;     // default array capacity

  /** Generic array used for storage of list elements. */
  private E[] data;                        // generic array used for storage

  /** Current number of elements in the list. */
  private int size = 0;                    // current number of elements

  // constructors
  /** Creates an array list with default initial capacity. */
  public ArrayList() { this(CAPACITY); }   // constructs list with default capacity

  /** Creates an array list with given initial capacity. */
  @SuppressWarnings({"unchecked"})
  public ArrayList(int capacity) {         // constructs list with given capacity
    data = (E[]) new Object[capacity];     // safe cast; compiler may give warning
  }

  // public methods
  /**
   * Returns the number of elements in the list.
   * @return number of elements in the list
   */
  public int size() { return size; }

  /**
   * Tests whether the array list is empty.
   * @return true if the array list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the element at index i.
   * @param  i   the index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E get(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    return data[i];
  }

  /**
   * Replaces the element at the specified index, and returns the element previously stored.
   * @param  i   the index of the element to replace
   * @param  e   the new element to be stored
   * @return the previously stored element
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E set(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    data[i] = e;
    return temp;
  }

  /**
   * Inserts the given element at the specified index of the list, shifting all
   * subsequent elements in the list one position further to make room.
   * @param  i   the index at which the new element should be stored
   * @param  e   the new element to be stored
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public void add(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size + 1);
    if (size == data.length)               // not enough capacity
      resize(2 * data.length);             // so double the current capacity
    for (int k=size-1; k >= i; k--)        // start by shifting rightmost
      data[k+1] = data[k];
    data[i] = e;                           // ready to place the new element
    size++;
  }

  /**
   * Removes and returns the element at the given index, shifting all subsequent
   * elements in the list one position closer to the front.
   * @param  i   the index of the element to be removed
   * @return the element that had be stored at the given index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public E remove(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    for (int k=i; k < size-1; k++)         // shift elements to fill hole
      data[k] = data[k+1];
    data[size-1] = null;                   // help garbage collection
    size--;
    return temp;
  }

  // utility methods
  /** Checks whether the given index is in the range [0, n-1]. */
  protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
    if (i < 0 || i >= n)
      throw new IndexOutOfBoundsException("Illegal index: " + i);
  }

  /** Resizes internal array to have given capacity >= size. */
  @SuppressWarnings({"unchecked"})
  protected void resize(int capacity) {
    E[] temp = (E[]) new Object[capacity];     // safe cast; compiler may give warning
    for (int k=0; k < size; k++)
      temp[k] = data[k];
    data = temp;                               // start using the new array
  }

  //---------------- nested ArrayIterator class ----------------
  /**
   * A (nonstatic) inner class. Note well that each instance contains an implicit
   * reference to the containing list, allowing it to access the list's members.
   */
  private class ArrayIterator implements Iterator<E> {
    /** Index of the next element to report. */
    private int j = 0;                   // index of the next element to report
    private boolean removable = false;   // can remove be called at this time?

    /**
     * Tests whether the iterator has a next object.
     * @return true if there are further objects, false otherwise
     */
    public boolean hasNext() { return j < size; }   // size is field of outer instance

    /**
     * Returns the next object in the iterator.
     *
     * @return next object
     * @throws NoSuchElementException if there are no further elements
     */
    public E next() throws NoSuchElementException {
      if (j == size) throw new NoSuchElementException("No next element");
      removable = true;   // this element can subsequently be removed
      return data[j++];   // post-increment j, so it is ready for future call to next
    }

    /**
     * Removes the element returned by most recent call to next.
     * @throws IllegalStateException if next has not yet been called
     * @throws IllegalStateException if remove was already called since recent next
     */
    public void remove() throws IllegalStateException {
      if (!removable) throw new IllegalStateException("nothing to remove");
      ArrayList.this.remove(j-1);  // that was the last one returned
      j--;                         // next element has shifted one cell to the left
      removable = false;           // do not allow remove again until next is called
    }
  } //------------ end of nested ArrayIterator class ------------

  /**
   * Returns an iterator of the elements stored in the list.
   * @return iterator of the list's elements
   */
  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator();     // create a new instance of the inner class
  }

  /**
   * Produces a string representation of the contents of the indexed list.
   * This exists for debugging purposes only.
   *
   * @return textual representation of the array list
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    for (int j = 0; j < size; j++) {
      if (j > 0) sb.append(", ");
      sb.append(data[j]);
    }
    sb.append(")");
    return sb.toString();
  }

  public static void main(String[] args) {
    ArrayList<Integer> list = new ArrayList<>();

    // 测试 add 方法
    list.add(0, 10);
    list.add(1, 20);
    list.add(2, 30);
    list.add(1, 15);
    System.out.println("添加元素后列表: " + list); // 应输出 (10, 15, 20, 30)

    // 测试 get 方法
    System.out.println("索引 0 的元素: " + list.get(0)); // 应输出 10
    System.out.println("索引 1 的元素: " + list.get(1)); // 应输出 15
    System.out.println("索引 2 的元素: " + list.get(2)); // 应输出 20

    // 测试 set 方法
    list.set(1, 25);
    System.out.println("设置索引 1 的元素为 25 后列表: " + list); // 应输出 (10, 25, 20, 30)

    // 测试 remove 方法
    list.remove(2);
    System.out.println("移除索引 2 的元素后列表: " + list); // 应输出 (10, 25, 30)

    // 测试 isEmpty 方法
    System.out.println("列表是否为空: " + list.isEmpty()); // 应输出 false

    // 测试 size 方法
    System.out.println("列表大小: " + list.size()); // 应输出 3

    // 测试 iterator 方法
    System.out.println("使用迭代器遍历列表元素:");
    for (Integer i : list) {
      System.out.println(i); // 应输出 10, 25, 30
    }

    // 测试边界情况
    try {
      list.get(5); // 应抛出 IndexOutOfBoundsException
    } catch (IndexOutOfBoundsException e) {
      System.out.println("捕获异常: " + e.getMessage());
    }

    try {
      list.set(-1, 5); // 应抛出 IndexOutOfBoundsException
    } catch (IndexOutOfBoundsException e) {
      System.out.println("捕获异常: " + e.getMessage());
    }

    try {
      list.remove(5); // 应抛出 IndexOutOfBoundsException
    } catch (IndexOutOfBoundsException e) {
      System.out.println("捕获异常: " + e.getMessage());
    }
  }
}
