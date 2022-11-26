import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partitions a List of Points using the Median of Medians algorithm.
 */
public class MedianOfMedians implements Partition {

  public static void main(String[] args) {
    List<Point> points = IntStream.generate(() -> new Random().nextInt(255))
//        .limit(1920 * 1080) // 1080p image size
        .limit(3840 * 2160) // 4k image size
        .boxed()
        .map(Point::new).collect(Collectors.toList());

    Tuple<List<Point>, List<Point>> split = new MedianOfMedians().partitionPoints(points, 0);
    System.out.println(split.getFirst().size() + "\n\n");
    System.out.println(split.getSecond().size());
  }

  @Override
  public Tuple<List<Point>, List<Point>> partitionPoints(List<Point> points, int dimension) {
    int size = points.size();

    int median = select(points, dimension, 0, size - 1, size / 2);

    List<Point> before = points.subList(0, median);
    List<Point> after = points.subList(median + 1, size);

    return new Tuple<>(before, after);
  }

  /*
  function select(list, left, right, n)
    loop
        if left = right then
            return left
        pivotIndex := pivot(list, left, right)
        pivotIndex := partition(list, left, right, pivotIndex, n)
        if n = pivotIndex then
            return n
        else if n < pivotIndex then
            right := pivotIndex - 1
        else
            left := pivotIndex + 1
  */
  public static int select(List<Point> points, int dimension, int left, int right, int n) {
    while(left != right) {
      int pivotIndex = pivot(points, dimension, left, right);
      pivotIndex = partition(points, dimension, left, right, pivotIndex, n);
      if(n == pivotIndex) {
        return n;
      }
      else if(n < pivotIndex) {
        right = pivotIndex - 1;
      }
      else {
        left = pivotIndex + 1;
      }
    }
    return left;
  }

  /*
  function pivot(list, left, right)
    // for 5 or less elements just get median
    if right − left < 5 then
        return partition5(list, left, right)
    // otherwise move the medians of five-element subgroups to the first n/5 positions
    for i from left to right in steps of 5
        // get the median position of the i'th five-element subgroup
        subRight := i + 4
        if subRight > right then
            subRight := right
        median5 := partition5(list, i, subRight)
        swap list[median5] and list[left + floor((i − left)/5)]

    // compute the median of the n/5 medians-of-five
    mid := (right − left) / 10 + left + 1
    return select(list, left, left + floor((right − left) / 5), mid)
  */
  private static int pivot(List<Point> points, int dimension, int left, int right) {
    if(right - left < 5) {
      return partition5(points, dimension, left, right);
    }
    for(int i = left; i <= right; i = i + 5) {
      int subRight = i + 4;
      if(subRight > right) {
        subRight = right;
      }
      int median5 = partition5(points, dimension, i, subRight);
      swap(points, median5, left + ((i - left) / 5));
    }
    int mid = ((right - left) / 10) + left + 1;
    return select(points, dimension, left, left + ((right - left) / 5), mid);
  }

  /*
  function partition(list, left, right, pivotIndex, n)
    pivotValue := list[pivotIndex]
    swap list[pivotIndex] and list[right]  // Move pivot to end
    storeIndex := left
    // Move all elements smaller than the pivot to the left of the pivot
    for i from left to right − 1 do
        if list[i] < pivotValue then
            swap list[storeIndex] and list[i]
            increment storeIndex
    // Move all elements equal to the pivot right after
    // the smaller elements
    storeIndexEq = storeIndex
    for i from storeIndex to right − 1 do
        if list[i] = pivotValue then
            swap list[storeIndexEq] and list[i]
            increment storeIndexEq
    swap list[right] and list[storeIndexEq]  // Move pivot to its final place
    // Return location of pivot considering the desired location n
    if n < storeIndex then
        return storeIndex  // n is in the group of smaller elements
    if n ≤ storeIndexEq then
        return n  // n is in the group equal to pivot
    return storeIndexEq // n is in the group of larger elements
  */
  private static int partition(List<Point> points, int dimension, int left, int right, int pivotIndex, int n) {
    int pivotValue = points.get(pivotIndex).getDimValue(dimension);
    swap(points, pivotIndex, right);

    int storeIndex = left;
    for(int i = left; i < right; i++) {
      if(points.get(i).getDimValue(dimension) < pivotValue) {
        swap(points, storeIndex, i);
        storeIndex++;
      }
    }

    int storeIndexEq = storeIndex;
    for(int i = storeIndex; i < right; i++) {
      if(points.get(i).getDimValue(dimension) == pivotValue) {
        swap(points, storeIndexEq, i);
        storeIndexEq++;
      }
    }

    swap(points, right, storeIndexEq);
    if(n < storeIndex) {
      return storeIndex;
    }
    if(n <= storeIndexEq) {
      return n;
    }
    return storeIndexEq;
  }

  /*
  function partition5( list, left, right)
    i := left + 1
    while i ≤ right
        j := i
        while j > left and list[j−1] > list[j] do
            swap list[j−1] and list[j]
            j := j − 1
        i :=  i + 1

    return floor((left + right) / 2)
  */
  private static int partition5(List<Point> points, int dimension, int left, int right) {

    int i = left + 1;
    while(i <= right) {
      int j = i;
      while(j > left && points.get(j - 1).getDimValue(dimension) > points.get(j).getDimValue(dimension)) {
        swap(points, j - 1, j);
        j = j - 1;
      }
      i = i + 1;
    }
    return ((left + right) / 2);
  }


  private static void swap(List<Point> points, int first, int second) {
    Point temp = points.get(first);
    points.set(first, points.get(second));
    points.set(second, temp);
  }


}
