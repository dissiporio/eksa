package main;

public interface State {
    void update();
    void render();
    void tick();
}
