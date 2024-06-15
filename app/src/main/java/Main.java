package main;

import main.render.Window;

public class Main {
    public static void main(String args[]) {
        Window window = new Window();
        window.init("hello jesus", 960, 640);
        Global.game_state = new GameState();
        Global.game_state.init();
        Global.current_state_type = Global.StateType.GAME;
        Global.current_state = Global.game_state;
        window.loop();
    }
}