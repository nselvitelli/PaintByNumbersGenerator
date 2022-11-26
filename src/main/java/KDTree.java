import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KDTree {

  private Optional<Node> tree;
  private List<List<Point>> partitions;
  private int nPartitions;

  /**
   * Builds a KD-Tree of the given list of points with at least n nodes
   * @param points the points to add to the tree
   * @param n the number of partitions to make
   */
  public KDTree(List<Point> points, int n) {
    this.nPartitions = n;
    int depth = (int)Math.ceil(Math.log(n + 1) / Math.log(2));
    buildTree(points, depth);
  }

  private void buildTree(List<Point> points, int depth) {
    this.partitions = new ArrayList<>();
    if(points.isEmpty()) {
      this.tree = Optional.empty();
    }
    else {
      int numDimensions = points.get(0).numberOfDimensions();
      this.tree = buildTree(points, depth, 0, numDimensions);
    }
  }

  private Optional<Node> buildTree(List<Point> points, int depth, int dimension, int maxDimensions) {
    if(depth == 0) {
      if(this.partitions.size() < this.nPartitions) {
        this.partitions.add(points);
      }
      return Optional.empty();
    }
    else if(depth == 1 && this.partitions.size() + 1 == this.nPartitions) {
      this.partitions.add(points);
      return Optional.empty();
    }

    int size = points.size();
    int medianIndex = MedianOfMedians.select(points, dimension, 0, size - 1, size / 2);

    Point median = points.get(medianIndex);
    List<Point> before = points.subList(0, medianIndex);
    List<Point> after = points.subList(medianIndex + 1, size);

    Optional<Node> left = buildTree(before, depth - 1, (dimension + 1) % maxDimensions, maxDimensions);
    Optional<Node> right = buildTree(after, depth - 1, (dimension + 1) % maxDimensions, maxDimensions);

    return Optional.of(new Node(median, left, right));
  }

  /*

  h = # levels
  2^h - 1 = # nodes

  n = # nodes
  # levels = Math.ceil(log2(n + 1))
  */

  /**
   * Returns the list of points that are the N most important points. The K-D Tree has partitioned the
   * given list of points into n groups. The important point from each group is the average of all points
   * in the group.
   * @return the top n points
   */
  public List<Point> topNPoints() {
    List<Point> points = new ArrayList<>();
    for(List<Point> partitionPoints : this.partitions) {
      points.add(Point.getAveragePoint(partitionPoints));
    }
    return points;
  }


  private class Node {

    private final Point value;
    private final Optional<Node> left;
    private final Optional<Node> right;

    public Node(Point value, Optional<Node> left, Optional<Node> right) {
      this.value = value;
      this.left = left;
      this.right = right;
    }

  }

}
