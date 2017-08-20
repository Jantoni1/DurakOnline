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
import main.java.view.lobby_scene.LobbyScene;
import main.java.view.LoginScene;
import main.java.view.room_scene.RoomScene;

import java.util.ArrayList;

/**
 * Created by Kuba on 28.05.2017.
 */
public class ClientRoomVisitor extends Visitor implements Client.MessageListener {

    private final ClientConnection mClient;
    private final ClientManager mClientManager;
    private final LobbyScene mLobbyScene;
    private RoomScene mRoomScene;
    private Room mRoom;

    public ClientRoomVisitor(ClientConnection pClient, ClientManager pClientManager, LobbyScene pLobbyScene, LoginScene pLoginScene) {
        mClient = pClient;
        mClientManager = pClientManager;
        mLobbyScene = pLobbyScene;
    }

    public void visit(Enter pEnter) {
        if(!pEnter.ismIfFailed()) {
            mRoom = new Room(pEnter.getmRoomName(), pEnter.getmMaxplyaers(), pEnter.getmPlayers(), mClientManager.getPlayerData().getUserID());
            mRoom.setmCurrentNumberOfPlayers(mRoom.getmCurrentNumberOfPlayers() + 1);
            mRoomScene = new RoomScene(mRoom, pEnter.getmMaxplyaers(), mClient);
            updateMultiplePlayersView();
            setReadyPanelVisible(mRoom.getPlayers().size());
            mClientManager.showScene(mRoomScene.getRoomScene());
        }
    }

    public void visit(Add pAdd) {
        mRoom.setmCurrentNumberOfPlayers(mRoom.getmCurrentNumberOfPlayers() + 1);
        if(pAdd.getmPlayer().getUserID() != mClientManager.getPlayerData().getUserID()) {
            mRoom.addPlayer(pAdd.getmPlayer());
            addPlayer(pAdd.getmPlayer().getUserID());
            setReadyPanelVisible(mRoom.getmCurrentNumberOfPlayers());
        }
    }

    public void visit(Ready pReady) {
        setReady(pReady.isReady());
    }

    public void visit(Leave pLeave) {
        mRoom.setmCurrentNumberOfPlayers(mRoom.getmCurrentNumberOfPlayers() - 1);
        mRoom.removePlayer(pLeave.playerId);
        mRoom.addPlayer(new Player("", -1));
        matchPlayersWithChairs();
        updateMultiplePlayersView();
        setReadyPanelVisible(mRoom.getPlayers().size());
    }

    public void visit(Start pStart) {
        mRoom.setmCurrentNumberOfPlayers(mRoom.getmMaxPlayers());
        hideReadyPanel();
        setTrumpCard(pStart.getCard());
        mRoom.setGameStarting(true);
        updateMultiplePlayersView();
    }

    public void visit(End pEnd) {
        mRoom.setGameStarting(false);
       onGameOver(pEnd.getmPlayerNick());
    }

    private void onGameOver(String pPlayersNick) {
        showEndGameScreen(pPlayersNick);
    }

    public void visit(NextRound pNextRound) {
        updateCardsOnTalon(pNextRound.getmNumberOfCardsLeft());
        if(pNextRound.isTaking()) {
            addCardsToPlayer(pNextRound.getPlayerId());
            updateMultiplePlayersView();
        }
        mRoom.clearCardsOnTable();
        updateCardsOnTable();
    }

    private void addCardsToPlayer(int pPlayerID) {
        mRoom.getPlayers().forEach(player -> {
            if(player.getUserID() == pPlayerID) {
                checkWhoGetsCards(player);
            }
        });
    }

    void checkWhoGetsCards(Player pPlayer) {
        if(thisIsMe(pPlayer.getPositionOnTable())) {
            pPlayer.addMultipleCards(mRoom.getAttackingCards());
            pPlayer.addMultipleCards(mRoom.getDefendingCards());
        }
        else {
            pPlayer.setNumberOfCards(pPlayer.getNumberOfCards() + mRoom.getAttackingCards().size() + mRoom.getDefendingCards().size() );
        }
    }

    private boolean thisIsMe(int pPositionOnTable) {
        return pPositionOnTable == 0;
    }

    private int findMe() {
        Player me = null;
        for(Player player : mRoom.getPlayers()) {
            if(player.getPositionOnTable() == 0) {
                me = player;
            }
        }
        return mRoom.getPlayers().indexOf(me);
    }

    public void visit(Get pGet) {
        mRoom.addCards(pGet.getPlayerID(), pGet.getCardArrayList(), pGet.getNumberOfCards());
        updateRoomScene(mRoom.findPlayer(pGet.getPlayerID()).getUserID());
    }

    private void getCardOnTable(boolean isAttacking, Card pCard) {
        if(isAttacking) {
            mRoom.getAttackingCards().add(pCard);
        }
        else {
            mRoom.getDefendingCards().add(pCard);
        }
    }
    public void visit(Play pPlay) {
        getCardOnTable(pPlay.ismIfAttacking(), pPlay.getmCard());
        mRoom.removeCard(pPlay.getPlayerID(), pPlay.getmCard());
        updateCardsOnTable();
    }

    public void visit(Next pNext) {
        lightPlayersNick(pNext.getmPlayerId());
        setPassButton(pNext.getmPlayerId());
        setUpMyTurn(isMyID(pNext.getmPlayerId()) ? pNext.getmAvailableCards() : null);
        updateMultiplePlayersView();
    }

    private void lightPlayersNick(int playersID) {
        mRoom.getPlayers().forEach(player -> {
            if(player.getUserID() == playersID) {
                player.setmIsMyTurn(true);
            }
            else {
                player.setmIsMyTurn(false);
            }
        });
    }

    private boolean isMyID(int givenID) {
        return givenID == mClientManager.getPlayerData().getUserID();
    }

    public void visit(Chat pChat) {
        addChatMessage(pChat.getPlayerName(), pChat.getChatMessage());
    }

    private void setPassButton(int pPlayerID) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.setPassButton(pPlayerID);
            }}
        );
    }

    private void setUpMyTurn(ArrayList<Integer> pAvailableCards) {
        mRoom.setAvailableCards(pAvailableCards);
    }

    private void updateMultiplePlayersView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.resetPlayersViewProperty();
            }
        });
    }

    private void setReadyPanelVisible(int pNumberOfPlayers) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.activateReadyPanel();
//                mStage.show();
            }
        });
    }

    private void updateRoomScene(int pPlayerID) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateOtherPlayersViewProperty(pPlayerID);
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

    private void updateCardsOnTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateCardsOnTable();
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

    private void addPlayer(int pPlayerID) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.addPlayer(pPlayerID);
            }
        });
    }

    private void addChatMessage(String pAuthor, String pContent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.addChatMessage(pAuthor, pContent);
            }
        });
    }

    private void updateCardsOnTalon(int pNumberOfCardsLeft) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.setNumberOfCards(pNumberOfCardsLeft);
            }
        });
    }

    private void hideReadyPanel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.hideActivePanel();
            }
        });
    }

    private void matchPlayersWithChairs() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.matchPlayersWithTableSpots();
            }
        });
    }


    public void onClientMessage(Message pServerMessage) {
        pServerMessage.accept(this);
    }

}
