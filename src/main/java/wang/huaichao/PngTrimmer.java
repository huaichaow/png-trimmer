package wang.huaichao;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngTrimmer {
    private static int DEFAULT_ALPHA_THRESHOLD = 0;
    private static String SUFFIX = "png";

    public void trimAll(File input, File output) throws IOException {
        trimAll(input, output, DEFAULT_ALPHA_THRESHOLD);
    }

    public void trimAll(File input, File output, int alphaThreshold) throws IOException {
        checkInput(input, output, alphaThreshold);

        BufferedImage bufferedImage = ImageIO.read(input);
        Boundary boundary = findBoundary(bufferedImage, alphaThreshold);
        BufferedImage trimmed = trimAll(bufferedImage, boundary);
        ImageIO.write(trimmed, SUFFIX, output);
    }

    public void trimX(File input, File output) throws IOException {
        trimX(input, output, DEFAULT_ALPHA_THRESHOLD);
    }

    public void trimX(File input, File output, int alphaThreshold) throws IOException {
        checkInput(input, output, alphaThreshold);

        BufferedImage bufferedImage = ImageIO.read(input);
        Boundary boundary = findBoundary(bufferedImage, alphaThreshold);
        BufferedImage trimmed = trimX(bufferedImage, boundary);
        ImageIO.write(trimmed, SUFFIX, output);
    }

    public void trimY(File input, File output) throws IOException {
        trimY(input, output, DEFAULT_ALPHA_THRESHOLD);
    }

    public void trimY(File input, File output, int alphaThreshold) throws IOException {
        checkInput(input, output, alphaThreshold);

        BufferedImage bufferedImage = ImageIO.read(input);
        Boundary boundary = findBoundary(bufferedImage, alphaThreshold);
        BufferedImage trimmed = trimY(bufferedImage, boundary);
        ImageIO.write(trimmed, SUFFIX, output);
    }

    private void checkInput(File input, File output, int alphaThreshold) {
        checkAlphaThreshold(alphaThreshold);

        String suffix = "." + SUFFIX;

        if (input == null ||
            output == null ||
            !input.getName().endsWith(suffix) ||
            !output.getName().endsWith(suffix)) {
            throw new IllegalArgumentException("accept only .png files");
        }
    }

    private void checkAlphaThreshold(int alphaThreshold) {
        if (alphaThreshold >= 0xFF) {
            throw new IllegalArgumentException("alphaThreshold should be less than 255");
        }
    }

    public Boundary getPngBoundary(File input, int alphaThreshold) throws IOException {
        checkAlphaThreshold(alphaThreshold);

        return findBoundary(
            ImageIO.read(input),
            alphaThreshold
        );
    }

    private Boundary findBoundary(BufferedImage img, int alphaThreshold) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = img.getRGB(x, y);
                int alpha = (color >> 24) & 0xFF;
                if (alpha > alphaThreshold) {
                    minX = Math.min(x, minX);
                    maxX = Math.max(x, maxX);
                    minY = Math.min(y, minY);
                    maxY = Math.max(y, maxY);
                }
            }
        }

        return new Boundary(minX, minY, maxX, maxY, img.getWidth(), img.getHeight());
    }

    private BufferedImage trimAll(BufferedImage img, Boundary boundary) {
        int width = boundary.width();
        int height = boundary.height();

        BufferedImage result = new BufferedImage(
            width,
            height,
            BufferedImage.TYPE_INT_ARGB
        );

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(
                    x,
                    y,
                    img.getRGB(x + boundary.getX1(), y + boundary.getY1())
                );
            }
        }

        return result;
    }

    private BufferedImage trimX(BufferedImage img, Boundary boundary) {
        int width = boundary.width();
        int height = img.getHeight();

        BufferedImage result = new BufferedImage(
            width,
            height,
            BufferedImage.TYPE_INT_ARGB
        );

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(
                    x,
                    y,
                    img.getRGB(x + boundary.getX1(), y)
                );
            }
        }

        return result;
    }

    private BufferedImage trimY(BufferedImage img, Boundary boundary) {
        int width = img.getWidth();
        int height = boundary.height();

        BufferedImage result = new BufferedImage(
            width,
            height,
            BufferedImage.TYPE_INT_ARGB
        );

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(
                    x,
                    y,
                    img.getRGB(x, y + boundary.getY1())
                );
            }
        }

        return result;
    }
}
