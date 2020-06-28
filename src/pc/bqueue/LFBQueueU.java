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
    addElementFlag.set(false);
  }

  @Override
  public int capacity() {
    return UNBOUNDED;
  }
  
  @Override
  public int size() {
	  rooms.enter(2);
	  int sz = tail.get() - head.get();
		rooms.leave(2);
	    return sz;
  }

  @Override
  public void add(E elem) {
	  while(true) {
		  rooms.enter(0);
		  if(addElementFlag.get()) {
			  rooms.leave(0);
			  if(useBackoff) {
		        	Backoff.delay();
		        }
			  continue;
		  }
		  addElementFlag.set(true);
		  int p = tail.getAndIncrement();
      if (p - head.get() < array.length) {
        array[p % array.length] = elem;
        addElementFlag.set(false);
        rooms.leave(0);
        break;
      } else {
    	  
 		 @SuppressWarnings("unchecked")
 		 E[] array2 = (E[]) new Object[(array.length)*2];
 		 for(int i =head.get() ; i < tail.get();i++) {
 			 array2[i%(array2.length)] = array[i%(array.length)];
 		 }
 		 array = array2;
 		 array[p % array.length] = elem;
        addElementFlag.set(false);
        rooms.leave(0);
        break;
      }
    }
	  if(useBackoff) {
			 Backoff.reset();
		 }
	 
  }
  
  @Override
  public E remove() {   
	  E elem = null;
	     while(true) {
	    	 rooms.enter(1);
	    	 int p = head.getAndIncrement();
	 
	       if (p < tail.get()) {

	    	   int pos = p % array.length;
	        elem = array[pos];
	        array[pos] = null;
	        rooms.leave(1);
	        break;
	      } else {
	    	 
	    	  head.getAndDecrement();
	    	  if(useBackoff) {
		          	Backoff.delay();
		          }
	    	  rooms.leave(1);
	      } 
	    }
	     if(useBackoff) {
	        	Backoff.reset();
	        }
	    return elem;
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
