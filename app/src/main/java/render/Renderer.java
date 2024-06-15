package main.render;

import main.physics.AABB;
import main.utils.vec2i;

import java.util.Arrays;

public class Renderer {
    public static final int WIDTH = 960/4;
    public static final int HEIGHT = 640/4;

    public static int[] pixels = new int[WIDTH*HEIGHT];

    public static void clear(int color) {
        Arrays.fill(pixels, color);
    }

    //rectangle
    public static void render_rect(vec2i pos, vec2i size, int color) {render_rect(pos.x, pos.y, size.x, size.y, color);}
    public static void render_rect(int x, int y, int w, int h, int color) {
        int p = 0;
        p += x;
        p += y*Renderer.WIDTH;
        for (int i=0;i<h;i++) {
            for (int j=0;j<w;j++) {
                //pixels[p+j+(i*Renderer.WIDTH)] = color;
                pixel(x+j, y+i, color);
            }
        }
    }

    //sprite
    public static void render_sprite(Sprite s, vec2i pos) {render_sprite(s, pos.x, pos.y);}
    public static void render_sprite(Sprite s, int x, int y) {
        int p = 0;
        p+=x;
        p+=y*Renderer.WIDTH;
        for (int i=0;i<s.size.y;i++) {
            for (int j=0;j<s.size.x;j++) {
                //pixels[p + (i * Renderer.WIDTH) + j] = s.pixels[(i * s.size.x) + j];
                pixel(x+j, y+i, s.pixels[(i*s.size.x)+j]);
            }
        }
    }

    //single-pixel renderer
    public static void pixel(vec2i pos, int color) {pixel(pos.x, pos.y, color);}
    public static void pixel(int x, int y, int color) {
        if ((x>=0)&&(x<Renderer.WIDTH)) {
            if ((y>=0)&&(y<Renderer.HEIGHT)) {
                int p = 0 + x + y * Renderer.WIDTH;
                pixels[p] = color;
            }
        }
    }

    //aabb
    public static void render_aabb(AABB aabb, int color) {
        render_rect(aabb.pos.exp(), aabb.size, color);
    }
}
