package main.java.model.client;


import main.java.model.server.Card;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class Room {

    private ArrayList<Card> mAttackingCards;
    private ArrayList<Card> mDefendingCards;
    private CopyOnWriteArrayList<AnotherPlayer> mOtherPlayers;
    private int mTalonCards;
    private  final  int mPlayerID;
    private Card mTalonBottomCard;
    private final int mMaxPlayers;
    private final String mRoomName;
    private boolean isGameStarting;
    ArrayList<Integer> mAvailableCards;
    int mMaxCards;

    public Room(String pRoomName, int pMaxPlayers, CopyOnWriteArrayList<AnotherPlayer> pOtherPlayers, int pPlayerID) {
        mOtherPlayers = pOtherPlayers;
        mRoomName = pRoomName;
        mMaxPlayers = pMaxPlayers;
        mPlayerID = pPlayerID;
        setPositionsOnTable();
        initializeNeutralCards();
        mAvailableCards = new ArrayList<>();
    }

    public void setmAvailableCards(ArrayList<Integer> mAvailableCards) {
        this.mAvailableCards = mAvailableCards;
    }

    public synchronized int getCardsIndex(Card pCard) {
        return findCorrectCard(pCard, getMe());
    }

    private AnotherPlayer getMe() {
        for(AnotherPlayer player : mOtherPlayers) {
            if(player.getmPositionOnTable() == 0) {
                return player;
            }
        }
        return null;
    }

    public void removeCard(int pCardIndex) {
        getMe().getPlayerCards().remove(pCardIndex);
    }

    private int findCorrectCard(Card pCard, AnotherPlayer pPlayer) {
        for(Integer index : mAvailableCards) {
            if(pPlayer.getPlayerCards().get(index).equals(pCard)) {
                return index;
            }
        }
        return -1;
    }

    public void clearCardsOnTable() {
        getmAttackingCards().clear();
        getmDefendingCards().clear();
    }

    public void setmMaxCards(int mMaxCards) {
        this.mMaxCards = mMaxCards;
    }

    public boolean isTurnBeginning() {
        return mAttackingCards.isEmpty() && mDefendingCards.isEmpty();
    }

    private void initializeNeutralCards() {
        mAttackingCards = new ArrayList<>();
        mDefendingCards = new ArrayList<>();
        isGameStarting = false;
    }

    public synchronized void removePlayer(int pPlayerID) {
        Predicate<AnotherPlayer> playerToRemove = p-> p.getmUserID() == pPlayerID;
        mOtherPlayers.removeIf(playerToRemove);
        removeAllPlayersCards();
    }

    private synchronized void removeAllPlayersCards() {
        setPositionsOnTable();
        mOtherPlayers.forEach(player -> {
            player.setmNumberOfCards(0);
        });

    }

    public synchronized void addPlayer(AnotherPlayer player) {
        mOtherPlayers.add(player);
        setPositionsOnTable();
        if(isRoomFull()) {
            isGameStarting = true;
        }
    }

    private synchronized boolean isRoomFull() {
        return mOtherPlayers.size() == mMaxPlayers;
    }

    private synchronized void setPositionsOnTable() {
        int myIndex = findMyIndex();
        for(AnotherPlayer player : mOtherPlayers) {
            player.setmPositionOnTable((mOtherPlayers.indexOf(player) - myIndex + mMaxPlayers) % mMaxPlayers);
        }
    }

    private int findMyIndex() {
        for(AnotherPlayer player : mOtherPlayers) {
            if(isItMe(player, mPlayerID)) {
                return mOtherPlayers.indexOf(player);
            }
        }
        return -1;
    }

    private synchronized boolean isItMe(AnotherPlayer pAnotherPlayer, int pMyID) {
        return pAnotherPlayer.getmUserID() == pMyID;
    }

    public ArrayList<Card> getmAttackingCards() {
        return mAttackingCards;
    }

    public ArrayList<Card> getmDefendingCards() {
        return mDefendingCards;
    }

    public CopyOnWriteArrayList<AnotherPlayer> getmOtherPlayers() {
        return mOtherPlayers;
    }

    public int getmMaxPlayers() {
        return mMaxPlayers;
    }

    public String getmRoomName() {
        return mRoomName;
    }

    public int getTalonCards() {
        return mTalonCards;
    }

    public void setTalonCards(int mTalonCards) {
        this.mTalonCards = mTalonCards;
    }

    public Card getTalonBottomCard() {
        return mTalonBottomCard;
    }

    public void setTalonBottomCard(Card mTalonBottomCard) {
        this.mTalonBottomCard = mTalonBottomCard;
    }

    public boolean isGameStarting() {
        return isGameStarting;
    }

    public int getmTalonCards() {
        return mTalonCards;
    }

    public Card getmTalonBottomCard() {
        return mTalonBottomCard;
    }

    public void setGameStarting(boolean gameStarting) {
        isGameStarting = gameStarting;
    }
}
