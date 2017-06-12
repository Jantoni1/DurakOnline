package main.java.model.server;


import java.io.Serializable;

public class RoomInfo implements Serializable {

    public RoomInfo() {}

    public RoomInfo(String pRoomName, int pRoomId, int pPlayersNumber, int pMaxPlayersNumber) {
        mRoomName = pRoomName;
        mRoomId = pRoomId;
        mPlayersNumber = pPlayersNumber;
        mMaxPlayerNumber = pMaxPlayersNumber;
    }

    private static final long serialVersionUID = 40L;

    public String getmRoomName() {
        return mRoomName;
    }

    public int getmRoomId() {
        return mRoomId;
    }

    public int getmPlayersNumber() {
        return mPlayersNumber;
    }

    public int getmMaxPlayerNumber() {
        return mMaxPlayerNumber;
    }

    public String toString() {return mRoomName + " (" + mPlayersNumber + "/" + mMaxPlayerNumber + ")";
    }

    private String mRoomName;
    private int mRoomId;
    private int mPlayersNumber;
    private int mMaxPlayerNumber;
}
