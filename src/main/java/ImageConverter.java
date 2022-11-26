import java.awt.Image;
import java.awt.image.BufferedImage;


public interface ImageConverter {

  /**
   * Given some image, convert it some way.
   * @param image the image to start from
   * @return the resulting image
   */
  BufferedImage convertImage(BufferedImage image);

}
