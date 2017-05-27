package model;


public class Room {

    public Room() {
        this.state = State.ADDING;
        this.isStarted = false;
    }
    public enum State {
        ADDING, PLAYING
    }
    public State state;
    public boolean isStarted;

}
