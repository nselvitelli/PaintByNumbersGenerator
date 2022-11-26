import java.util.List;

/**
 * Partitions a List of Points into two lists based on the value at the given dimension of each Point.
 */
public interface Partition {

  /**
   * Partitions the given list of points using some partition method on the given dimension of each
   * Point.
   * @param points the points to partition
   * @param dimension the dimension to partition the points on
   * @return a tuple containing two lists of points. The first list contains all points partitioned
   * before the pivot point. The second list contains all points partitioned
   * after the pivot point.
   */
  Tuple<List<Point>, List<Point>> partitionPoints(List<Point> points, int dimension);

}
