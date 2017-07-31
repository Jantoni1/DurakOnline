package main.java.model.client;


import main.java.model.server.Card;
import main.java.view.Model;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class Room implements Model {

    private ArrayList<Card> mAttackingCards;
    private ArrayList<Card> mDefendingCards;
    private CopyOnWriteArrayList<Player> mPlayers;
    private int mTalonCards;
    private  final  int mPlayerID;
    private Card mTalonBottomCard;
    private final int mMaxPlayers;
    private final String mRoomName;
    private boolean isGameStarting;
    private boolean isFirstAttack;
    ArrayList<Integer> mAvailableCards;
    int mMaxCards;

    public Room(String pRoomName, int pMaxPlayers, CopyOnWriteArrayList<Player> pOtherPlayers, int pPlayerID) {
        isFirstAttack = false;
        mPlayers = pOtherPlayers;
        mRoomName = pRoomName;
        mMaxPlayers = pMaxPlayers;
        mPlayerID = pPlayerID;
        setPositionsOnTable();
        initializeNeutralCards();
        mAvailableCards = new ArrayList<>();
    }

    public synchronized  void setAvailableCards(ArrayList<Integer> pAvailableCards) {
        if(pAvailableCards != null) {
            mAvailableCards.clear();
            mAvailableCards.addAll(pAvailableCards);
        }
    }

    public synchronized int getCardsIndex(Card pCard) {
        return findCorrectCard(pCard, getMe());
    }

//    public boolean getFirstAttack() {
//        return isFirstAttack;
//    }
//
//    public void setFirstAttack(boolean isFirstAttack) {
//        this.isFirstAttack = isFirstAttack;
//    }

    public Player getMe() {
        for(Player player : mPlayers) {
            if(player.getPositionOnTable() == 0) {
                return player;
            }
        }
        return null;
    }

    public void removeCard(int pCardIndex) {
        getMe().getPlayerCards().remove(pCardIndex);
    }

    public void addCards(int pPlayerID, ArrayList<Card> pCards, int pNumberOfCards) {
        if(pCards != null) {
            getMe().addMultipleCards(pCards);
        }
        else {
            findPlayer(pPlayerID).changeNumberOfCards(pNumberOfCards);
        }
    }

    public void removeCard(int pPlayerID, Card pCard) {
        if(getMe() == findPlayer(pPlayerID)) {
            getMe().getPlayerCards().removeIf(card -> card.equals(pCard));
        }
        else {
            findPlayer(pPlayerID).changeNumberOfCards(-1);
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
        getAttackingCards().clear();
        getDefendingCards().clear();
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
        Predicate<Player> playerToRemove = p-> p.getUserID() == pPlayerID;
        mPlayers.removeIf(playerToRemove);
        removeAllPlayersCards();
    }

    private synchronized void removeAllPlayersCards() {
        setPositionsOnTable();
        mPlayers.forEach(player -> {
            player.setNumberOfCards(0);
        });

    }
    
    public Player findPlayer(int pPlayerID) {
        Player foundPlayer = null;
        for(Player player : mPlayers) {
            if(player.getUserID() == pPlayerID) {
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
        return pPlayer.getUserID() == pMyID;
    }

    public ArrayList<Card> getAttackingCards() {
        return mAttackingCards;
    }

    public ArrayList<Card> getDefendingCards() {
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
