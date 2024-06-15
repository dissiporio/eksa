package main.tilemap;

import main.render.Renderer;
import main.render.Sprite;
import main.render.SpriteSheet;
import main.utils.vec2i;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TileMap {
    public String path;
    public vec2i size;
    public SpriteSheet tileset;

    private int[] map;

    public TileMap(String path, vec2i size, SpriteSheet tileset) {
        this.tileset = tileset;
        this.path = path;
        this.size = size;

        this.map = load_map();
    }

    private int[] load_map() {
        int[] integers = new int[size.x * size.y];
        int index = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                for (String part : parts) {
                    if (!part.trim().isEmpty() && index < integers.length) {
                        integers[index++] = Integer.parseInt(part.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (index != size.x * size.y) {
            System.err.println("Warning: The number of integers read does not match the expected size.");
        }

        return integers;
    }

    public void render() {
        for (int i = 0;i<size.y;i++) {
            for (int j = 0;j<size.x;j++) {
                if (map[j+i*size.x] != 0) {
                    Renderer.render_sprite(tileset.get_sprite(map[j+i*size.x]), j * 16, i * 16);
                }
            }
        }
    }
}
