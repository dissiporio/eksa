package main.render;

import main.utils.Color;
import main.utils.vec2i;

import java.util.ArrayList;
import java.util.List;

public class FontRenderer {
    private static char[] characters = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            ' '
    };
    public static ArrayList<Sprite> s;
    private static vec2i charSize;

    public static void init(String path) {
        s = new ArrayList<>();

        Sprite fontSprite = new Sprite(path);
        int[] pixels = fontSprite.pixels;
        int width = fontSprite.size.x;

        // Detect the height of a character
        int height = fontSprite.size.y;
        charSize = new vec2i(0, height);

        int startX = -1;
        for (int x = 0; x < width; x++) {
            boolean isRedLine = (pixels[x] >> 16 & 0xff) == 255 && (pixels[x] >> 8 & 0xff) == 0 && (pixels[x] & 0xff) == 0;
            if (isRedLine) {
                if (startX != -1) {
                    int charWidth = x - startX;
                    charSize.x = charWidth;  // Set character width
                    int[] charPixels = new int[charWidth * height];
                    for (int py = 0; py < height; py++) {
                        System.arraycopy(pixels, startX + py * width, charPixels, py * charWidth, charWidth);
                    }
                    s.add(new Sprite(charPixels, new vec2i(charWidth, height)));
                    startX = -1;
                }
            } else if (startX == -1) {
                startX = x;
            }
        }
    }

    public static void render(String string, vec2i pos) {render(string, pos.x, pos.y);}
    public static void render(String string, int x, int y) {
        int m = 0;
        for (int i=0;i<string.length();i++) {
            char c = string.charAt(i);
            int char_index = get_char_index(c);
            Renderer.render_sprite(s.get(char_index), x+i+m, y);
            m+= s.get(char_index).size.x;
        }
    }

    public static void debug() {
        for (int i=0;i<s.size();i++) {
            Renderer.render_sprite(s.get(i), 0, i*(7+1));
        }
    }

    private static int get_char_index(char c) {
        for (int i = 0;i<characters.length;i++) {
            if (characters[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
