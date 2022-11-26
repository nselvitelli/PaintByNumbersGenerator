import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class NormalizeImage implements ImageConverter {

  private final int distinctColors;
  private final boolean includeAlphaValues;

  public NormalizeImage(int distinctColors, boolean includeAlphaValues) {
    this.distinctColors = distinctColors;
    this.includeAlphaValues = includeAlphaValues;
  }

  public static void main(String[] args) throws IOException {
    String fileName = "lighthouse.jpg";
    URL imagePath = NormalizeImage.class.getResource(fileName);
    BufferedImage image = ImageIO.read(imagePath);
    BufferedImage paintByNumbers = new NormalizeImage(30, false).convertImage(image);
    ImageIO.write(paintByNumbers, "png",
        new File("/Users/nickselvitelli/Documents/Code Stuff/PaintByNumbersGenerator/src/main/resources/" + fileName + ".png"));
  }

  @Override
  public BufferedImage convertImage(BufferedImage image) {

    List<Point> points = imageToPoints(image);
    List<Point> copyPreserveOrder = new ArrayList<>(points);

    KDTree tree = new KDTree(points, this.distinctColors);

    List<Point> topNPoints = tree.topNPoints();

    copyPreserveOrder = copyPreserveOrder.stream()
        .map((x) -> mapPointToClosestInList(x, topNPoints))
        .collect(Collectors.toList());

    return pointsToImage(copyPreserveOrder, image.getWidth(), image.getHeight());
  }

  private static Point mapPointToClosestInList(Point original, List<Point> closePoints) {
    Point closest = closePoints.get(0);
    for(Point point : closePoints) {
      if(original.squareDistance(point) < original.squareDistance(closest)) {
        closest = point;
      }
    }
    return closest;
  }

  private BufferedImage pointsToImage(List<Point> points, int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        int i = y * width + x;
        int rgb = pointToRGB(points.get(i));
        image.setRGB(x, y, rgb);
      }
    }
//    for(int i = 0; i < points.size(); i++) {
//      int x = i % width;
//      int y = i / height;
//      int rgb = pointToRGB(points.get(i));
//      image.setRGB(x, y, rgb);
//    }
    return image;
  }

  private List<Point> imageToPoints(BufferedImage image) {
    List<Point> points = new ArrayList<>();
    int width = image.getWidth();
    int height = image.getHeight();

    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        points.add(RGBToPoint(rgb, this.includeAlphaValues));
      }
    }
    return points;
  }

  public static Point RGBToPoint(int rgb, boolean includeAlpha) {
    int alpha = (rgb >> 24)  & 0xFF;
    int red = (rgb >> 16)  & 0xFF;
    int green = (rgb >> 8)  & 0xFF;
    int blue = (rgb) & 0xFF;
    if(includeAlpha) {
      return new Point(red, green, blue, alpha);
    }
    return new Point(red, green, blue);
  }

  public static int pointToRGB(Point point) {
    int dimensions = point.numberOfDimensions();
    if (dimensions < 3 || dimensions > 4) {
      throw new IllegalArgumentException("Must have a dimension of 3 or 4");
    }
    int rgb = 0;

    int r = point.getDimValue(0);
    int g = point.getDimValue(1);
    int b = point.getDimValue(2);

    rgb = rgb | (r << 16) | (g << 8) | b;

    if(dimensions == 4) {
      int a = point.getDimValue(3);
      rgb = rgb | (a << 24);
    }
    return rgb;
  }
}
