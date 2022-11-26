/**
 * Tuple class used to hold two values.
 * @param <U> the type of the first value
 * @param <V> the type of the second value
 */
public class Tuple<U, V> {

  private final U first;
  private final V second;

  public Tuple(U first, V second) {
    this.first = first;
    this.second = second;
  }

  public U getFirst() {
    return this.first;
  }

  public V getSecond() {
    return second;
  }
}
