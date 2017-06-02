package main.java.controller.server;


import main.java.model.Card;
import main.java.model.CardsOnTable;
import main.java.model.Room;
import main.java.network.message.client.Play;
import main.java.network.message.server.*;
import main.java.network.server.ClientThread;
import main.java.model.Player;
import main.java.network.message.server.End;
import main.java.network.message.server.Get;
import main.java.network.message.server.Next;
import main.java.network.message.server.NextRound;

import java.util.ArrayList;
import java.util.LinkedList;

public class RoomController {

    public RoomController(Room pRoom, LinkedList<ClientThread> pClients) {
        mRoom = pRoom;
        mTalonController = new TalonController(mRoom.mTalon);
        mPCController = new PlayersCollectionController(mRoom.mPlayerArrayList, mRoom.mCardsOnTable, mRoom.mMaxPlayers);
        mClients = pClients;
        mPassedAttackers = mPCController.playersInGame - 1;
    }

    public boolean isFull() {
        return mPCController.numberOfPlayers() == mRoom.mMaxPlayers;
    }

    public String getRoomName() {
        return mRoom.mLobbyName;
    }

    public void startGame() {
        mTalonController.shuffle();
        mRoom.mPlayersReady.forEach(playerId -> mPCController.addPlayer(playerId));
        ArrayList<Integer> availableCards = new ArrayList<>();
        for(int i = 0; i < 5; ++i) {
            availableCards.add(i);
        }
        mClients.forEach(player -> {
            player.sendMessage(new Start());
            player.sendMessage(new Get(mTalonController.dealCards(mPCController.findPlayer(player.getID()))));
            player.sendMessage(new Next(mPCController.getDefender().id, availableCards, 4));
        });
    }

    public void updateGameStatus(ClientThread pClientThread, Play pPlay) {
        if(isDefender(pClientThread.getID())) {
            updateDefenderGameStatus(pClientThread, pPlay);
            return;
        }



    }

    public void updateDefenderGameStatus(ClientThread pClientThread, Play pPlay) {
    if(isDefender(pClientThread.getID())) {
        playDefenderSide(pClientThread, pPlay);
    }
    else {
        playAttackerSide(pClientThread, pPlay);
    }
    if(pPlay.getCardNumber().isEmpty()) {
            refuse(isStillDefending());
            return;
        }
        addDefendingCard(pClientThread.getID(), pPlay.getCardNumber().get(0));
        if(outOfCards(pClientThread.getID())) {

        }
        if(isStillDefending()) {
            ArrayList<Integer> availableCards = getAvailableCards(pClientThread.getID(), false);
            mClients.forEach(player -> {
                player.sendMessage(new main.java.network.message.server.Play(getLastDefendingCard(), false, player.getID()));
                if(player.getID() == pClientThread.getID()) {
                    player.sendMessage(new Next(mPCController.getDefender().id, availableCards, maxOffensiveCardsToGive()));
                }
                else {

                }

            });
        }
    }

    public void playAttackerSide(ClientThread pClientThread, Play pPlay) {
        if(pPlay.getCardNumber().isEmpty()) {
            mPCController.getNextAttacker(1);
            --mPassedAttackers;
            if(mPassedAttackers == 0) {
                int attackerid = mPCController.getNextDefender(false);
                mClients.forEach(player -> {
                    player.sendMessage(new NextRound(false, pClientThread.getID()));
                    player.sendMessage(new Get(mTalonController.dealCards(mPCController.findPlayer(player.getID()))));
                    if (player.getID() == pClientThread.getID()) {
                        player.sendMessage(new Next(mPCController.getNextAttacker(0).id, getAvailableCards(mPCController.getNextAttacker(0).id, false), maxOffensiveCardsToGive()));
                    } else {
                        player.sendMessage(new Next(mPCController.getNextAttacker(0).id, null, maxOffensiveCardsToGive()));
                    }
                });
                return;
            }
            mClients.forEach(player -> {
                if(player.getID() == pClientThread.getID()) {
                    player.sendMessage(new Next(mPCController.getNextAttacker(0).id, mPCController.getNextAttacker(0).mPlayersDeck.getAvailableCards(
                            (Card) null), maxOffensiveCardsToGive()
                    ));
                }
                else {
                    player.sendMessage(new Next(mPCController.getNextAttacker(0).id, null, maxOffensiveCardsToGive()));
                }
            });
            return;
        }
        mPassedAttackers = mPCController.playersInGame - 1;
        mClients.forEach(player -> {
            player.sendMessage(new main.java.network.message.server.Play(mPCController.playCard(pClientThread.getID(), pPlay.getCardNumber(), true), true, pClientThread.getID()));
            if (player.getID() == pClientThread.getID()) {
                player.sendMessage(new Next(mPCController.getDefender().id, getAvailableCards(mPCController.getDefender().id, false), maxOffensiveCardsToGive()));
            } else {
                player.sendMessage(new Next(mPCController.getDefender().id, null, maxOffensiveCardsToGive()));
            }
        });
        if(mPCController.getNextAttacker(0).mPlayersDeck.playersDeck.isEmpty() && mTalonController.mTalon.deck.isEmpty()) {
            if(checkIfGameEnds(mPCController.findPlayer((pClientThread.getID())))) {
                resetRoom();
                return;
            }
        }

    }

    public void playDefenderSide(ClientThread pClientThread, Play pPlay) {
        if(pPlay.getCardNumber().isEmpty()) {
            mPCController.mCardsOnTable.giveCardsToLosingDefender(mPCController.findPlayer(pClientThread.getID()));
            mPCController.getNextDefender(true);
            mClients.forEach(player -> {
                player.sendMessage(new NextRound(true, pClientThread.getID()));
                player.sendMessage(new Get(mTalonController.dealCards(mPCController.findPlayer(player.getID()))));
                if(player.getID() == pClientThread.getID()) {
                    player.sendMessage(new Next(mPCController.getNextAttacker(0).id, mPCController.getNextAttacker(0).mPlayersDeck.getAvailableCards(
                            (Card) null), maxOffensiveCardsToGive()
                    ));
                }
                else {
                    player.sendMessage(new Next(mPCController.getNextAttacker(0).id, null, maxOffensiveCardsToGive()));
                }
            });
        }
        else {
            mClients.forEach(player -> {
                player.sendMessage(new main.java.network.message.server.Play(mPCController.playCard(pClientThread.getID(), pPlay.getCardNumber(), false), false, pClientThread.getID()));
            });
            if (isStillDefending()) {
                mClients.forEach(player -> {
                    if (player.getID() == pClientThread.getID()) {
                        player.sendMessage(new Next(pClientThread.getID(), getAvailableCards(pClientThread.getID(), false), maxOffensiveCardsToGive()));
                    } else {
                        player.sendMessage(new Next(pClientThread.getID(), null, maxOffensiveCardsToGive()));
                    }
                });
            }
            else {
                if(!mPCController.findPlayer(pClientThread.getID()).isPlaying() && mTalonController.mTalon.deck.isEmpty()) {
                    if(checkIfGameEnds(mPCController.findPlayer((pClientThread.getID())))) {
                        resetRoom();
                        return;
                    }
                }
                if(!mPCController.getDefender().isPlaying() || mPCController.mCardsOnTable.defendingCards.size() == 5) {
                    mPCController.getNextDefender(false);
                    mClients.forEach(player -> {
                        player.sendMessage(new NextRound(false, pClientThread.getID()));
                        player.sendMessage(new Get(mTalonController.dealCards(mPCController.findPlayer(player.getID()))));
                        if(player.getID() == pClientThread.getID()) {
                            player.sendMessage(new Next(mPCController.getNextAttacker(0).id, mPCController.getNextAttacker(0).mPlayersDeck.getAvailableCards(
                                    (Card) null), maxOffensiveCardsToGive()
                            ));
                        }
                        else {
                            if (player.getID() == mPCController.getNextAttacker(1).id) {
                                player.sendMessage(new Next(mPCController.getNextAttacker(0).id, getAvailableCards(mPCController.getNextAttacker(0).id, true), maxOffensiveCardsToGive()));
                            } else {
                                player.sendMessage(new Next(mPCController.getNextAttacker(0).id, null, maxOffensiveCardsToGive()));
                            }
                        }
                    });
                }
            }
        }
    }

    public boolean checkIfGameEnds(Player player) {
        --mPCController.playersInGame;
        if(mPCController.playersInGame == 1) {
            for(ClientThread clientThread : mClients) {
                if(mPCController.findPlayer(clientThread.getID()).isPlaying()) {
                    final int durakId = clientThread.getID();
                    mClients.forEach(client -> client.sendMessage(new End(durakId)));
                    return true;
                }
            }

        }
        return false;
    }

    public int maxOffensiveCardsToGive() {
        return 5 - mPCController.mCardsOnTable.attackingCards.size();
    }

    public ArrayList<Card> getLastDefendingCard() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(mRoom.mCardsOnTable.defendingCards.get(mRoom.mCardsOnTable.defendingCards.size()-1));
            return cards;
    }


    public boolean outOfCards(int playedId) {
        return mPCController.findPlayer(playedId).mPlayersDeck.playersDeck.isEmpty() && mTalonController.mTalon.deck.isEmpty();
    }

    public void refuse(boolean isDefender) {

    }

    public void addDefendingCard(int playerId, int cardId) {
        mPCController.mCardsOnTable.addDefendingCard(mPCController.findPlayer(playerId).mPlayersDeck.playCard(cardId));
    }

    public boolean isDefender(int pPlayerId) {
        return mPCController.getDefenderIndex() == pPlayerId;
    }

    public boolean isStillDefending() {
        return mPCController.mCardsOnTable.attackingCards.size() > mPCController.mCardsOnTable.defendingCards.size();
    }

    public ArrayList<Integer> getAvailableCards(int pPlayerId, boolean ifAttacking) {
        ArrayList<Integer> availableCards = new ArrayList<>();
        if(ifAttacking) {
            mPCController.findPlayer(pPlayerId).mPlayersDeck.getAvailableCards(mPCController.mCardsOnTable.defendingCards);
        }
        else {
            mPCController.findPlayer(pPlayerId).mPlayersDeck.getAvailableCards(mRoom.mCardsOnTable.attackingCards.get(mRoom.mCardsOnTable.attackingCards.size() - 1));
        }
        return availableCards;
    }

    public int getRoomId() {
        return mRoom.mLobbyId;
    }

    public int getMaxPlayers() {
        return mRoom.mMaxPlayers;
    }

    public void updateRoom(Room pRoom) {
        mRoom = pRoom;
        mTalonController.mTalon = mRoom.mTalon;
        mPCController.resetRoom(mRoom);
    }

    public void resetRoom() {
        mRoom.mPlayerArrayList.clear();
        mRoom.mTalon.deck.clear();
        mRoom.mPlayersReady.clear();
        mPassedAttackers = 0;
        mRoom.mCardsOnTable = new CardsOnTable();
        mRoom.isStarted = false;
    }

    public Room mRoom;
    public TalonController mTalonController;
    public PlayersCollectionController mPCController;
    private LinkedList<ClientThread> mClients;
    private int mPassedAttackers;
}
