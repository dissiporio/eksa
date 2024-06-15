package main.render;

import main.utils.vec2i;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class Sprite {
    public vec2i size;

    public int[] pixels;

    public Sprite(String path) {
        BufferedImage image;
        try  {
           image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new Error(e);
        }

        // Ensure the image is in ARGB format
        BufferedImage argbImage = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
        argbImage.getGraphics().drawImage(image, 0, 0, null);

        size = new vec2i(image.getWidth(), image.getHeight());

        // Retrieve the pixel data from the image
        pixels = ((DataBufferInt) argbImage.getRaster().getDataBuffer()).getData();
    }

    public Sprite(int[] pixels, vec2i size) {
        this.pixels = pixels;
        this.size = size;
    }
}
