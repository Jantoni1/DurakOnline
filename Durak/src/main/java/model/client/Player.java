package main.java.model.client;


public class Player {
    private String mUserName;
    private int mUserID;

    public Player() {
    }

    public void setmUserID(int mUserID) {
        this.mUserID = mUserID;
    }

    public void setmUserName(String mUserName) {

        this.mUserName = mUserName;
    }

    public String getmUserName() {

        return mUserName;
    }

    public int getmUserID() {
        return mUserID;
    }

    public Player(String mUserName, int mUserID) {

        this.mUserName = mUserName;
        this.mUserID = mUserID;
    }
}
