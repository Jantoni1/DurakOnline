package main.java.model.client;


import main.java.model.server.Card;
import main.java.view.RoomScene;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class Room implements RoomScene.Model{

    private ArrayList<Card> mAttackingCards;
    private ArrayList<Card> mDefendingCards;
    private CopyOnWriteArrayList<Player> mPlayers;
    private int mTalonCards;
    private  final  int mPlayerID;
    private Card mTalonBottomCard;
    private final int mMaxPlayers;
    private final String mRoomName;
    private boolean isGameStarting;
    ArrayList<Integer> mAvailableCards;
    int mMaxCards;

    public Room(String pRoomName, int pMaxPlayers, CopyOnWriteArrayList<Player> pOtherPlayers, int pPlayerID) {
        mPlayers = pOtherPlayers;
        mRoomName = pRoomName;
        mMaxPlayers = pMaxPlayers;
        mPlayerID = pPlayerID;
        setPositionsOnTable();
        initializeNeutralCards();
        mAvailableCards = new ArrayList<>();
    }

    public synchronized  void setmAvailableCards(ArrayList<Integer> mAvailableCards) {
        this.mAvailableCards = mAvailableCards;
    }

    public synchronized int getCardsIndex(Card pCard) {
        return findCorrectCard(pCard, getMe());
    }

    private Player getMe() {
        for(Player player : mPlayers) {
            if(player.getmPositionOnTable() == 0) {
                return player;
            }
        }
        return null;
    }

    public void removeCard(int pCardIndex) {
        getMe().getPlayerCards().remove(pCardIndex);
    }
    public void removeCard(int pPlayerID, Card pCard) {
        if(getMe() == findPlayer(pPlayerID)) {
            getMe().getmPlayerCards().removeIf(card -> card.equals(pCard));
        }
        else {
            findPlayer(pPlayerID).playOneCard();
        }
    }

    public ArrayList<Integer> getAvailableCards() {
        return mAvailableCards;
    }

    private int findCorrectCard(Card pCard, Player pPlayer) {
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
        Predicate<Player> playerToRemove = p-> p.getmUserID() == pPlayerID;
        mPlayers.removeIf(playerToRemove);
        removeAllPlayersCards();
    }

    private synchronized void removeAllPlayersCards() {
        setPositionsOnTable();
        mPlayers.forEach(player -> {
            player.setNumberOfCards(0);
        });

    }
    
    private Player findPlayer(int pPlayerID) {
        Player foundPlayer = null;
        for(Player player : mPlayers) {
            if(player.getmUserID() == pPlayerID) {
                foundPlayer =  player;
            }
        }
        return foundPlayer;
    }

    public synchronized void addPlayer(Player player) {
        mPlayers.add(player);
        setPositionsOnTable();
        if(isRoomFull()) {
            isGameStarting = true;
        }
    }

    private synchronized boolean isRoomFull() {
        return mPlayers.size() == mMaxPlayers;
    }

    private synchronized void setPositionsOnTable() {
        int myIndex = findMyIndex();
        for(Player player : mPlayers) {
            player.setmPositionOnTable((mPlayers.indexOf(player) - myIndex + mMaxPlayers) % mMaxPlayers);
        }
    }

    private int findMyIndex() {
        for(Player player : mPlayers) {
            if(isItMe(player, mPlayerID)) {
                return mPlayers.indexOf(player);
            }
        }
        return -1;
    }

    private synchronized boolean isItMe(Player pPlayer, int pMyID) {
        return pPlayer.getmUserID() == pMyID;
    }

    public ArrayList<Card> getmAttackingCards() {
        return mAttackingCards;
    }

    public ArrayList<Card> getmDefendingCards() {
        return mDefendingCards;
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return mPlayers;
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
