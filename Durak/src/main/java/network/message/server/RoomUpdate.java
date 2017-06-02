package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;
import main.java.model.RoomInfo;

/**
 * Created by Kuba on 28.05.2017.
 */
public class RoomUpdate extends BaseServerMessage{

    public RoomUpdate(RoomInfo pRoomInfo, boolean pIsCreated, boolean pIsDeleted) {
        mRoomInfo = pRoomInfo;
        mIsCreated = pIsCreated;
        mIsDeleted = pIsDeleted;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    public void accept(ClientGameplayVisitor visitor) {visitor.visit(this);}

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
