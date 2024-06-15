package main;

import main.render.Renderer;
import main.render.Window;
import main.utils.Color;
import main.utils.vec2f;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements State {
    Random random = new Random();

    ArrayList<vec2f[]> parts;

    public void init() {
        parts = new ArrayList<>();
    }

    @Override
    public void tick() {
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        Renderer.clear(Color.ARGB(255, 0, 0, 0));

        for (int i=0;i<9;i++) {
            vec2f[] a = new vec2f[3];
            a[0] = new vec2f((float) Window.getMousePos().x, (float) Window.getMousePos().y);
            a[1] = new vec2f(random.nextFloat(-2, 2), -2);
            a[2] = new vec2f(random.nextInt(4, 6), 0);

            parts.add(a);
        }

        for (int i = 0;i<parts.size();i++) {
            parts.get(i)[0].x += parts.get(i)[1].x;
            parts.get(i)[0].y += parts.get(i)[1].y;
            parts.get(i)[2].x -= 0.1;
            parts.get(i)[2].y = parts.get(i)[2].x;
            if (parts.get(i)[2].x <= 0) parts.remove(i);
        }
        for (int i=0;i<parts.size();i++) {
            Renderer.render_rect(parts.get(i)[0].exp(), parts.get(i)[2].exp(), Color.ARGB(255, 255, 255, 255));
        }
    }
}
