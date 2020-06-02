package pc.bqueue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Lock-free implementation of queue - unbounded variant.
 * 
 *
 * @param <E> Type of elements.
 */
public class LFBQueueU<E>  implements BQueue<E> {

  private E[] array;
  private final AtomicInteger head;
  private final AtomicInteger tail;
  private final AtomicBoolean addElementFlag;
  private final Rooms rooms;
  private final boolean useBackoff;


  /**
   * Constructor.
   * @param initialCapacity Initial queue capacity.
   * @param backoff Flag to enable/disable the use of back-off.
   * @throws IllegalArgumentException if {@code capacity <= 0}
   */
  @SuppressWarnings("unchecked")
  public LFBQueueU(int initialCapacity, boolean backoff) {
    head = new AtomicInteger(0);
    tail = new AtomicInteger(0);new AtomicMarkableReference<>(0, false);
    addElementFlag = new AtomicBoolean();
    array = (E[]) new Object[initialCapacity];
    useBackoff = backoff;
    rooms = new Rooms(3, backoff);
  }

  @Override
  public int capacity() {
    return UNBOUNDED;
  }
  
  @Override
  public int size() {
    return tail.get() - head.get();
  }

  @Override
  public void add(E elem) {   
    // TODO
  }
  
  @Override
  public E remove() {   
    // TODO: should be the same as LFQueue
    return null;
  }

  /**
   * Test instantiation.
   */
  public static final class Test extends BQueueTest {
    @Override
    <T> BQueue<T> createBQueue(int capacity) {
      return new LFBQueueU<>(capacity, false);
    }
  }
}
