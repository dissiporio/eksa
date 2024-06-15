package main;

public class Global {
    public enum StateType {
        GAME
    }

    public static StateType current_state_type;
    public static GameState game_state;
    public static State current_state;
    public static long ticks = 0, frames = 0;

    //TODO implement set state
    public static void setState(State state) {}
}
