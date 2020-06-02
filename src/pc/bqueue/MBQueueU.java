package pc.bqueue;

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
    // TODO
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
