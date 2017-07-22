package main.java.controller.client;
import javafx.application.Platform;
import main.java.controller.Visitor;
import main.java.model.client.Player;
import main.java.model.client.Room;
import main.java.model.server.Card;
import main.java.network.client.Client;
import main.java.network.client.ClientConnection;
import main.java.network.message.Message;
import main.java.network.message.server.*;
import main.java.view.LobbyScene;
import main.java.view.LoginScene;
import main.java.view.RoomScene;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kuba on 28.05.2017.
 */
public class ClientGameplayVisitor extends Visitor implements Client.MessageListener {

    private final ClientConnection mClient;
    private final ClientManager mClientManager;
    private final LobbyScene mLobbyScene;
    private RoomScene mRoomScene;
    private Room mRoom;
    private static final int STANDARD_NUMBER_OF_PLAYER_CARDS = 5;

    public ClientGameplayVisitor(ClientConnection pClient, ClientManager pClientManager, LobbyScene pLobbyScene, LoginScene pLoginScene) {
        mClient = pClient;
        mClientManager = pClientManager;
        mLobbyScene = pLobbyScene;
    }

    public void visit(Enter pEnter) {
        if(!pEnter.ismIfFailed()) {
            mRoom = new Room(pEnter.getmRoomName(), pEnter.getmMaxplyaers(), pEnter.getmPlayers(), mClientManager.getPlayerData().getmUserID());
            mRoomScene = new RoomScene(mRoom, pEnter.getmMaxplyaers(), mClient);
            updateMultiplePlayersView(mRoom.getPlayers(), true);
            setReadyPanelVisible(mRoom.getPlayers().size());
            mClientManager.showScene(mRoomScene.getRoomScene());
        }
    }

    public void visit(Add pAdd) {
        if(pAdd.getmPlayer().getmUserID() != mClientManager.getPlayerData().getmUserID()) {
            mRoom.addPlayer(pAdd.getmPlayer());
            updateRoomScene(pAdd.getmPlayer(), true);
            setReadyPanelVisible(mRoom.getPlayers().size());
        }
    }

    public void visit(Ready pReady) {
        setReady(pReady.isReady());
    }

    public void visit(Leave pLeave) {
        mRoom.removePlayer(pLeave.playerId);
        updateMultiplePlayersView(mRoom.getPlayers(), true);
        setReadyPanelVisible(mRoom.getPlayers().size());
    }

    public void visit(Start pStart) {
        setReadyPanelVisible(0);
        giveOtherPlayersStartingCards();
        setTrumpCard(pStart.getCard());
        updateMultiplePlayersView(mRoom.getPlayers(), true);
    }

    public void visit(End pEnd) {
        mRoom.getPlayers().forEach(player -> {
            player.setNumberOfCards(0);
            if(player.getPlayerCards() != null) {
                player.getPlayerCards().clear();
            }
        });
       onGameOver(pEnd.getmPlayerNick());
    }

    private void onGameOver(String pPlayersNick) {
        mRoom.clearCardsOnTable();
        hideTrumpCard();
        updateCardsOnTable(mRoom.getmAttackingCards(), mRoom.getmDefendingCards());
        showEndGameScreen(pPlayersNick);
    }

    public void visit(NextRound pNextRound) {
        if(pNextRound.isTaking()) {
            addCardsToPlayer(pNextRound.getPlayerId());
            updateMultiplePlayersView(mRoom.getPlayers(), true);
        }
        mRoom.clearCardsOnTable();
        updateCardsOnTable(mRoom.getmAttackingCards(), mRoom.getmDefendingCards());
    }

    private void addCardsToPlayer(int pPlayerID) {
        mRoom.getPlayers().forEach(player -> {
            if(player.getmUserID() == pPlayerID) {
                checkWhoGetsCards(player);
            }
        });
    }

    void checkWhoGetsCards(Player pPlayer) {
        if(thisIsMe(pPlayer.getmPositionOnTable())) {
            pPlayer.addMultipleCards(mRoom.getmAttackingCards());
            pPlayer.addMultipleCards(mRoom.getmDefendingCards());
        }
        else {
            pPlayer.setNumberOfCards(pPlayer.getmNumberOfCards() + mRoom.getmAttackingCards().size() + mRoom.getmDefendingCards().size() );
        }
    }

    private boolean thisIsMe(int pPositionOnTable) {
        return pPositionOnTable == 0;
    }

    private void giveOtherPlayersStartingCards() {
        for(Player player : mRoom.getPlayers()) {
            if(player.getmPositionOnTable() != 0) {
                player.setNumberOfCards(STANDARD_NUMBER_OF_PLAYER_CARDS);
            }
        }
    }

    private int findMe() {
        Player me = null;
        for(Player player : mRoom.getPlayers()) {
            if(player.getmPositionOnTable() == 0) {
                me = player;
            }
        }
        return mRoom.getPlayers().indexOf(me);
    }

    public void visit(Get pGet) {
        mRoom.getPlayers().get(findMe()).addMultipleCards(pGet.getCardArrayList());
        addMissingCardsToPlayers();
        updateRoomScene(mRoom.getPlayers().get(findMe()), mRoom.isTurnBeginning());
    }

    private void addMissingCardsToPlayers() {
        mRoom.getPlayers().forEach(player -> {
            if(player.getmNumberOfCards() < STANDARD_NUMBER_OF_PLAYER_CARDS) {
                player.setNumberOfCards(STANDARD_NUMBER_OF_PLAYER_CARDS);
            }
        });
    }

    private void getCardOnTable(boolean isAttacking, Card pCard) {
        if(isAttacking) {
            mRoom.getmAttackingCards().add(pCard);
        }
        else {
            mRoom.getmDefendingCards().add(pCard);
        }
    }
    public void visit(Play pPlay) {
        getCardOnTable(pPlay.ismIfAttacking(), pPlay.getmCard());
        mRoom.removeCard(pPlay.getPlayerID(), pPlay.getmCard());
        updateCardsOnTable(mRoom.getmAttackingCards(), mRoom.getmDefendingCards());
    }

    public void visit(Next pNext) {
        lightPlayersNick(pNext.getmPlayerId());
        setUpMyTurn(isMyID(pNext.getmPlayerId()) ? pNext.getmAvailableCards() : null);
        updateMultiplePlayersView(mRoom.getPlayers(), mRoom.isTurnBeginning());
    }

    private void lightPlayersNick(int playersID) {
        mRoom.getPlayers().forEach(player -> {
            if(player.getmUserID() == playersID) {
                player.setmIsMyTurn(true);
            }
            else {
                player.setmIsMyTurn(false);
            }
        });
    }

    private boolean isMyID(int givenID) {
        return givenID == mClientManager.getPlayerData().getmUserID();
    }

    public void visit(Chat pChat) {
        //TODO SHOW CHAT MESSAGES
    }


    private void setUpMyTurn(ArrayList<Integer> pAvailableCards) {
        mRoom.setmAvailableCards(pAvailableCards);
    }

    private void updateMultiplePlayersView(CopyOnWriteArrayList<Player> pOtherPlayers, boolean pFirstAttack) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.resetPlayersViewProperty(pOtherPlayers, pFirstAttack);
//                mStage.show();
            }
        });
    }

    private void setReadyPanelVisible(int pNumberOfPlayers) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.activateReadyPanel(pNumberOfPlayers);
//                mStage.show();
            }
        });
    }

    private void updateRoomScene(Player pPlayer, boolean pTrueIfFirstAttack) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateOtherPlayersViewProperty(pPlayer, pTrueIfFirstAttack);
            }
        });
    }

    private void setTrumpCard(Card pCard) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.setTrumpCard(pCard);
            }
        });
    }

    private void hideTrumpCard() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.hideTrumpCard();
            }
        });
    }

    private void updateCardsOnTable(ArrayList<Card> pAttackingCards, ArrayList<Card> pDefendingCards) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateCardsOnTable(pAttackingCards, pDefendingCards);
            }
        });
    }

    private void showEndGameScreen(String pPlayersNick) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.showEndGamePanel(pPlayersNick);
            }
        });
    }

    private void setReady(boolean pTrueForReadyFalseForUnready) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateReadyPlayersProperty(pTrueForReadyFalseForUnready);
            }
        });
    }



    public void onClientMessage(Message pServerMessage) {
        pServerMessage.accept(this);
    }

    public Room getmRoom() {
        return mRoom;
    }
}
