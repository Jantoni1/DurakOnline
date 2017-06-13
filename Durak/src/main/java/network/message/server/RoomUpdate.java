package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.model.server.RoomInfo;
import main.java.network.message.Message;

/**
 * Created by Kuba on 28.05.2017.
 */
public class RoomUpdate extends Message {

    public RoomUpdate(RoomInfo pRoomInfo, boolean pIsCreated, boolean pIsDeleted) {
        mRoomInfo = pRoomInfo;
        mIsCreated = pIsCreated;
        mIsDeleted = pIsDeleted;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 22L;

    public RoomInfo getmRoomInfo() {
        return mRoomInfo;
    }

    public boolean isRoomCreated() {
        return mIsCreated;
    }

    public boolean isRoomDeleted() {
        return mIsDeleted;
    }

    private RoomInfo mRoomInfo;
    private boolean mIsCreated;
    private boolean mIsDeleted;

}
