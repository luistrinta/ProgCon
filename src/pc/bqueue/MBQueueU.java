package pc.bqueue;


import pc.util.UnexpectedException;

/**
 * Monitor-based implementation of queue. 
 * 
 *
 * @param <E> Type of elements.
 */
public class MBQueueU<E> extends MBQueue<E> {

  /**
   * Constructor.
   * @param initialCapacity Initial queue capacity.
   * @throws IllegalArgumentException if {@code capacity <= 0}
   */
  public MBQueueU(int initialCapacity) {
    super(initialCapacity);
  }
  
  @Override
  public synchronized int capacity() {
    return UNBOUNDED;
  }
  
  @Override
  public synchronized void add(E elem) {
      if (size == array.length) {
        E[] temp = array;
        array = (E[]) new Object[array.length +2];
        for (int i = 0; i <temp.length; i++) {
          array[i] = temp[i];
        }
      }
      array[(head + size) % array.length] = elem;
      notifyAll();
      size++;
  }
  
  /**
   * Test instantiation.
   */
  public static final class Test extends BQueueTest {
    @Override
    <T> BQueue<T> createBQueue(int initialCapacity) {
      return new MBQueueU<>(initialCapacity);
    }
  }
}