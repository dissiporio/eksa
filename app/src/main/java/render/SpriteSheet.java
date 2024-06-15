package main.render;

import main.utils.vec2i;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {
    public vec2i size;
    public int num_sprites;
    public vec2i sprite_size;

    public int[] pixels;

    public SpriteSheet(String path, vec2i sprite_size) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new Error(e);
        }

        BufferedImage argb_image = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        argb_image.getGraphics().drawImage(image, 0, 0, null);
        size = new vec2i(image.getWidth(), image.getHeight());
        pixels = ((DataBufferInt) argb_image.getRaster().getDataBuffer()).getData();

        this.sprite_size = sprite_size;
        num_sprites = (size.x/sprite_size.x)*(size.y/sprite_size.y);
    }

    /*
    the counting starts from 0:
    (48x32 tilesize 16)
    |1|2|3|
    |-|-|-|
    |4|5|6|
     */
    public Sprite get_sprite(int index) {
        index -= 1;
        if (index >= num_sprites) {
            System.err.println("[ERROR] sprite out of bounds: " + index);
            return null;
        }

        int sprites_per_row = size.x / sprite_size.x;
        int row = index / sprites_per_row;
        int col = index % sprites_per_row;

        int startX = col * sprite_size.x;
        int startY = row * sprite_size.y;

        int[] sprite_pixels = new int[sprite_size.x * sprite_size.y];
        for (int y = 0; y < sprite_size.y; y++) {
            for (int x = 0; x < sprite_size.x; x++) {
                sprite_pixels[y * sprite_size.x + x] = pixels[(startY + y) * size.x + (startX + x)];
            }
        }

        return new Sprite(sprite_pixels, sprite_size);
    }
}
