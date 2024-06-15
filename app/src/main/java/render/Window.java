package main.render;

import main.utils.Keyboard;
import main.utils.Time;
import main.Global;
import main.utils.vec2i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.util.Arrays;

public class Window {
    // frame buffer image/buffer
    private static BufferedImage image;
    private static int[] buffer;

    private static JFrame frame;
    private static Canvas canvas;
    private static BufferStrategy bufferStrategy;

    // window size
    public static int width, height;

    // true if window is focused
    public static boolean focused;

    // frame/tick tracking
    private static int frames, ticks, fps, tps;
    private static long lastSecond, lastFrame, frameTime, tickTimeRemaining;

    // if true, window will exit
    private static boolean close;

    // creates a window with the specified width/height
    public static void init(String title, int width, int height) {
        Window.width = width;
        Window.height = height;

        Window.lastSecond = Time.now();
        Window.lastFrame = Time.now();

        // create graphics device compatible buffered image
        GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        Window.image = graphicsConfiguration.createCompatibleImage(
                Renderer.WIDTH, Renderer.HEIGHT, Transparency.OPAQUE);
        Window.buffer = ((DataBufferInt) Window.image.getRaster().getDataBuffer()).getData();

        // open window
        Window.frame = new JFrame(title);
        Window.frame.getContentPane().setPreferredSize(new Dimension(width, height));
        Window.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Window.frame.setResizable(true);
        Window.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Window.close = true;
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                Window.focused = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                Window.focused = false;
            }
        });
        Window.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Window.width = Window.canvas.getWidth();
                Window.height = Window.canvas.getHeight();
            }
        });
        Window.frame.setIgnoreRepaint(true);

        Window.canvas = new Canvas(graphicsConfiguration);
        Window.canvas.setPreferredSize(new Dimension(width, height));
        Window.canvas.setSize(width, height);
        Window.canvas.addKeyListener(Keyboard.getListener());
        Window.canvas.setIgnoreRepaint(true);
        Window.frame.add(canvas);
        Window.frame.pack();
        Window.frame.setLocationRelativeTo(null);

        Window.frame.setVisible(true);
        Window.canvas.setVisible(true);

        Window.canvas.createBufferStrategy(2);
        Window.bufferStrategy = Window.canvas.getBufferStrategy();

        Arrays.fill(Renderer.pixels, 0);
    }

    public static boolean hasFocus() {
        return Window.frame.isActive();
    }

    public static void close() {
        Window.close = true;
        Window.frame.dispatchEvent(new WindowEvent(Window.frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void renderFrame() {
        for (int i = 0; i< Renderer.WIDTH * Renderer.HEIGHT; i++) {
            Window.buffer[i] = Renderer.pixels[i];
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        double rendererAspect = (double) Renderer.WIDTH / (double) Renderer.HEIGHT,
                windowAspect = (double) Window.width / (double) Window.height,
                scaleFactor = windowAspect > rendererAspect ?
                        (double) Window.height / (double) Renderer.HEIGHT :
                        (double) Window.width / (double) Renderer.WIDTH;

        int rw = (int) (Renderer.WIDTH * scaleFactor), rh = (int) (Renderer.HEIGHT * scaleFactor);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Window.width, Window.height);
        graphics.drawImage(
                image, (Window.width - rw) / 2, (Window.height - rh) / 2,
                rw, rh, null
        );
        graphics.dispose();
        if (!Window.bufferStrategy.contentsLost()) {
            Window.bufferStrategy.show();
        }
    }

    static int x = 0;

    public static void loop() {
        //TODO init
        while (!Window.close) {
            long now = Time.now(), start = now;

            if (now - Window.lastSecond >= Time.NS_PER_SECOND) {
                Window.lastSecond = now;
                Window.fps = Window.frames;
                Window.tps = Window.ticks;
                Window.frames = 0;
                Window.ticks = 0;
                System.out.println("[INFO] fps:" +  Window.fps + " | tps:" + Window.tps);
            }

            Window.frameTime = now - Window.lastFrame;
            Window.lastFrame = now;

            long tickTime = Window.frameTime + Window.tickTimeRemaining;
            while (tickTime >= Time.NS_PER_TICK) {
                // TODO tick
                Global.current_state.tick();
                Keyboard.tick();
                tickTime -= Time.NS_PER_TICK;
                Window.ticks++;
            }
            Window.tickTimeRemaining = tickTime;

            Global.current_state.update();
            Keyboard.update();
            Global.current_state.render();

            Window.renderFrame();
            Window.frames++;

            // TODO: make vsync optional
            try {
                long sleepMs = ((16 * Time.NS_PER_MS) - (Time.now() - start)) / Time.NS_PER_MS;
                Thread.sleep(sleepMs < 0 ? 0 : sleepMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TODO destroy
        System.exit(0);
    }

    public static vec2i getMousePos() {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePos, Window.canvas);

        double rendererAspect = (double) Renderer.WIDTH / (double) Renderer.HEIGHT;
        double windowAspect = (double) Window.width / (double) Window.height;
        double scaleFactor = windowAspect > rendererAspect ?
                (double) Window.height / (double) Renderer.HEIGHT :
                (double) Window.width / (double) Renderer.WIDTH;

        int rw = (int) (Renderer.WIDTH * scaleFactor), rh = (int) (Renderer.HEIGHT * scaleFactor);
        int xOffset = (Window.width - rw) / 2;
        int yOffset = (Window.height - rh) / 2;

        int bufferY = (int) ((mousePos.y - yOffset) / scaleFactor);

        // Check if the coordinates are within the buffer
        if (bufferY < 0 || bufferY >= Renderer.WIDTH) {
            bufferY = -1;
        }

        int bufferX = (int) ((mousePos.x - xOffset) / scaleFactor);

        // Check if the coordinates are within the buffer
        if (bufferX < 0 || bufferX >= Renderer.WIDTH) {
            bufferX = -1;
        }

        return new vec2i(bufferX, bufferY);
    }
}

