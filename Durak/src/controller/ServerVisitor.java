package controller;


public class ServerVisitor extends Visitor {

    public ServerVisitor(RoomController roomController) {
        this.roomController = roomController;
    }

    private RoomController roomController;

}
