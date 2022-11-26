import java.util.List;

/**
 * A N-dimensional point that has N values.
 */
public class Point {

  private final int[] values;
  private final int length;

  public Point(int... points) {
    this.values = points;
    this.length = values.length;
  }

  /**
   * Get the number of dimensions in this point.
   * @return the number of dimensions this point has
   */
  public int numberOfDimensions() {
    return this.length;
  }

  /**
   * Retrieves the value at the given dimension.
   * @param dimension the dimension of the value to retrieve
   * @return the value at the given dimension
   * @throws IllegalArgumentException if this point does not have the given dimension
   */
  public int getDimValue(int dimension) throws IllegalArgumentException {
    if(dimension >= 0 && dimension < this.length) {
      return this.values[dimension];
    }
    throw new IllegalArgumentException("Dimension does not exist");
  }

  /**
   *
   * @param other
   * @return
   */
  public int squareDistance(Point other) {
    if(other.numberOfDimensions() != this.numberOfDimensions()) {
      throw new IllegalArgumentException("Number of dimensions mismatch.");
    }
    int sqrDist = 0;
    for(int i = 0; i < this.length; i++) {
      sqrDist += Math.pow(other.getDimValue(0) - this.getDimValue(0), 2);
    }
    return sqrDist;
  }

  /**
   *
   * @param points
   * @return
   */
  public static Point getAveragePoint(List<Point> points) {
    if(points.isEmpty()) {
      throw new IllegalArgumentException("No Points given");
    }
    int dimensions = points.get(0).numberOfDimensions();
    int[] values = new int[dimensions];
    for(Point point : points) {
      for(int i = 0; i < dimensions; i++) {
        values[i] += point.getDimValue(i);
      }
    }
    for (int i = 0; i < dimensions; i++) {
      values[i] /= points.size();
    }
    return new Point(values);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("(");
    for(int i = 0; i < this.length; i++) {
      builder.append(this.values[i]);
      if(i != this.length - 1) {
        builder.append(", ");
      }
    }
    builder.append(")");
    return builder.toString();
  }
}
