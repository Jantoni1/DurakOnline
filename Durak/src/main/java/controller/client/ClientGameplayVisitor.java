package main.java.controller.client;
import main.java.model.client.AnotherPlayer;
import main.java.model.client.Room;
import main.java.model.server.Card;
import main.java.model.server.RoomInfo;
import main.java.network.client.Client;
import main.java.network.message.client.CreateRoom;
import main.java.network.message.server.*;
import main.java.view.LobbyScene;
import main.java.view.LoginScene;

import java.util.ArrayList;

/**
 * Created by Kuba on 28.05.2017.
 */
public class ClientGameplayVisitor extends BaseClientVisitor implements Client.MessageListener {

    private final Client mClient;
    private final ClientManager mClientManager;
    private final LobbyScene mLobbyScene;
    private Room mRoom;
    private static final int STANDARD_NUMBER_OF_PLAYER_CARDS = 5;

    public ClientGameplayVisitor(Client pClient, ClientManager pClientManager, LobbyScene pLobbyScene, LoginScene pLoginScene) {
        mClient = pClient;
        mClientManager = pClientManager;
        mLobbyScene = pLobbyScene;
    }

    public void visit(Enter pEnter) {
        if(!pEnter.ismIfFailed()) {
            mRoom = new Room(pEnter.getmRoomName(), pEnter.getmMaxplyaers(), pEnter.getmPlayers(), mClientManager.getPlayerData().getmUserID());
            mClientManager.createRoomScene(pEnter.getmMaxplyaers());
            mClientManager.updateMultiplePlayersView(mRoom.getmOtherPlayers(), true);
            mClientManager.setReadyPanelVisible(mRoom.getmOtherPlayers().size());
            mClientManager.showRoomScene();
        }
    }

    public void visit(Add pAdd) {
        if(pAdd.getmAnotherPlayer().getmUserID() != mClientManager.getPlayerData().getmUserID()) {
            mRoom.addPlayer(pAdd.getmAnotherPlayer());
            mClientManager.updateRoomScene(pAdd.getmAnotherPlayer(), true);
            mClientManager.setReadyPanelVisible(mRoom.getmOtherPlayers().size());
        }
    }

    public void visit(Ready pReady) {
        mClientManager.setReady(pReady.ismReadyorUnready());
    }

    public void visit(Leave pLeave) {
        mRoom.removePlayer(pLeave.playerId);
        mClientManager.updateMultiplePlayersView(mRoom.getmOtherPlayers(), true);
        mClientManager.setReadyPanelVisible(mRoom.getmOtherPlayers().size());
    }

    public void visit(Start pStart) {
        mClientManager.setReadyPanelVisible(0);
        giveOtherPlayersStartingCards();
        mClientManager.setTrumpCard(pStart.getCard());
        mClientManager.updateMultiplePlayersView(mRoom.getmOtherPlayers(), true);
    }

    public void visit(End pEnd) {
        mRoom.getmOtherPlayers().forEach(player -> {
            player.setmNumberOfCards(0);
            if(player.getPlayerCards() != null) {
                player.getPlayerCards().clear();
            }
        });
        mRoom.clearCardsOnTable();
        mClientManager.hideTrumpCard();
        mClientManager.updateCardsOnTable(mRoom.getmAttackingCards(), mRoom.getmDefendingCards());
    }

    public void visit(NextRound pNextRound) {
        if(pNextRound.isTaking()) {
            addCardsToPlayer(pNextRound.getPlayerId());
            mClientManager.updateMultiplePlayersView(mRoom.getmOtherPlayers(), true);
        }
        mRoom.clearCardsOnTable();
        mClientManager.updateCardsOnTable(mRoom.getmAttackingCards(), mRoom.getmDefendingCards());
    }

    private void addCardsToPlayer(int pPlayerID) {
        mRoom.getmOtherPlayers().forEach(player -> {
            if(player.getmUserID() == pPlayerID) {
                checkWhoGetsCards(player);
            }
        });
    }

    void checkWhoGetsCards(AnotherPlayer pPlayer) {
        if(thisIsMe(pPlayer.getmPositionOnTable())) {
            pPlayer.addMultipleCards(mRoom.getmAttackingCards());
            pPlayer.addMultipleCards(mRoom.getmDefendingCards());
        }
        else {
            pPlayer.setmNumberOfCards(pPlayer.getmNumberOfCards() + mRoom.getmAttackingCards().size() + mRoom.getmDefendingCards().size() );
        }
    }

    private boolean thisIsMe(int pPositionOnTable) {
        return pPositionOnTable == 0;
    }

    private void giveOtherPlayersStartingCards() {
        for(AnotherPlayer player : mRoom.getmOtherPlayers()) {
            if(player.getmPositionOnTable() != 0) {
                player.setmNumberOfCards(STANDARD_NUMBER_OF_PLAYER_CARDS);
            }
        }
    }

    private int findMe() {
        AnotherPlayer me = null;
        for(AnotherPlayer player : mRoom.getmOtherPlayers()) {
            if(player.getmPositionOnTable() == 0) {
                me = player;
            }
        }
        return mRoom.getmOtherPlayers().indexOf(me);
    }

    public void visit(Get pGet) {
        mRoom.getmOtherPlayers().get(findMe()).addMultipleCards(pGet.getCardArrayList());
        addMissingCardsToPlayers();
        mClientManager.updateRoomScene(mRoom.getmOtherPlayers().get(findMe()), mRoom.isTurnBeginning());
    }

    private void addMissingCardsToPlayers() {
        mRoom.getmOtherPlayers().forEach(player -> {
            if(player.getmNumberOfCards() < STANDARD_NUMBER_OF_PLAYER_CARDS) {
                player.setmNumberOfCards(STANDARD_NUMBER_OF_PLAYER_CARDS);
            }
        });
    }

    public void visit(Play pPlay) {
        if(pPlay.ismIfAttacking()) {
            mRoom.getmAttackingCards().add(pPlay.getmCard());
        }
        else {
            mRoom.getmDefendingCards().add(pPlay.getmCard());
        }
        mClientManager.updateCardsOnTable(mRoom.getmAttackingCards(), mRoom.getmDefendingCards());
    }

    public void visit(Next pNext) {
        lightPlayersNick(pNext.getmPlayerId());
        if(isMyID(pNext.getmPlayerId())) {
            setUpMyTurn(pNext.getmAvailableCards());
        }
        mClientManager.updateMultiplePlayersView(mRoom.getmOtherPlayers(), mRoom.isTurnBeginning());
    }

    private void lightPlayersNick(int playersID) {
        mRoom.getmOtherPlayers().forEach(player -> {
            if(player.getmUserID() == playersID) {
                player.setmIsMyTurn(true);
            }
            else {
                player.setmIsMyTurn(false);
            }
        });
    }

    private void setUpMyTurn(ArrayList<Integer> pAvailableCards) {
        mRoom.setmAvailableCards(pAvailableCards);
    }


    private boolean isMyID(int givenID) {
        return givenID == mClientManager.getPlayerData().getmUserID();
    }

    public void visit(Chat pChat) {
        //TODO SHOW CHAT MESSAGES
    }


    public void onClientMessage(BaseServerMessage pServerMessage) {
        pServerMessage.accept(this);
    }

    public Room getmRoom() {
        return mRoom;
    }
}
