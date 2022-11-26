import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestKDTree {


  @Disabled
  @Test
  public void test2Groups() {

    int n = 2;
    Random random = new Random();

    List<Point> points = IntStream.generate(() -> random.nextInt(255))
        .limit(1920*1080)
        .boxed()
        .map(Point::new)
        .collect(Collectors.toList());

    List<Point> expected = new ArrayList<>(points);

    KDTree tree = new KDTree(points, n);

    Tuple<List<Point>, List<Point>> split = new MedianOfMedians().partitionPoints(expected, 0);
    Point firstExpectedAvg = Point.getAveragePoint(split.getFirst());
    Point secondExpectedAvg = Point.getAveragePoint(split.getSecond());

    List<Point> topNPoints = tree.topNPoints();
    assertEquals(2, topNPoints.size());
    assertEquals(firstExpectedAvg.getDimValue(0), topNPoints.get(0).getDimValue(0));
    assertEquals(secondExpectedAvg.getDimValue(0), topNPoints.get(1).getDimValue(0));
  }

}
