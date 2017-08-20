package main.java.controller.server;

//zrobić porządny playerlayout
//chat
//wygląd
//kniec gierki pewnie dalej nie działa no bo nie działał jeszcze za starych dobrych czasów

import main.java.model.server.*;
import main.java.network.client.Client;
import main.java.network.message.client.Play;
import main.java.network.message.server.*;
import main.java.network.server.ClientThread;
import main.java.network.message.server.End;
import main.java.network.message.server.Get;
import main.java.network.message.server.Next;
import main.java.network.message.server.NextRound;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @brief class modifying data in Room, responsible for conducting game and sending info to players
 */
public class RoomController {

    public Room mRoom;
    public TalonController mTalonController;
    public PlayersCollectionController mPCController;
    private LinkedList<ClientThread> mClients;
    private int mPassedAttackers;

    public RoomController() {
        mRoom = new Room("Name", 4);
        mTalonController = new TalonController(mRoom.mTalon);
        mPCController = new PlayersCollectionController(mRoom.mPlayerArrayList, mRoom.mCardsOnTable, mRoom.mMaxPlayers);
        mPassedAttackers = mRoom.getNumberOfPlayersInGame() - 1;
    }

    /**
     *
     * @param pRoom contains all data about game
     * @param pClients list of players participating in the game
     */
    public RoomController(Room pRoom, LinkedList<ClientThread> pClients) {
        mRoom = pRoom;
        mTalonController = new TalonController(mRoom.mTalon);
        mPCController = new PlayersCollectionController(mRoom.mPlayerArrayList, mRoom.mCardsOnTable, mRoom.mMaxPlayers);
        mClients = pClients;
        mPassedAttackers = mRoom.getNumberOfPlayersInGame() - 1;
    }

    /**
     * @return true if there is max number of players in the room
     */
    public boolean isFull() {
        return mClients.size() == mRoom.mMaxPlayers;
    }

    /**
     *
     * @return name of the room
     */
    public String getRoomName() {
        return mRoom.mLobbyName;
    }

    /**
     * @brief shuffles cards in talon, then adds all players to game and sends start messages (start, get and next)
     */
    public void startGame() {
        mRoom.isStarted = true;
        mTalonController.shuffle();
        mRoom.setNumberOfPlayersInGame(mRoom.mMaxPlayers);
        Card.setTrump(mTalonController.mTalon.get(0).mSuit);
        mRoom.mPlayersReady.forEach(playerId -> mPCController.addPlayer(playerId, findClient(playerId)));
        sendGameInitialMessages();
    }

    private String findClient(int pClientID) {
        for(ClientThread clientThread : mClients) {
            if(clientThread.getID() == pClientID) {
                return clientThread.getUsername();
            }
        }
        return null;
    }

    /**
     * @brief gets all player's cards' indices so he can play whatever card he wants to
     *
     * @return all cards player can use when he's a first player in a turn
     */
    private ArrayList<Integer> getInitialAvailableCardsIndices() {
        ArrayList<Integer> availableCards = new ArrayList<>();
        for(int i = 0; i < 5; ++i) {
            availableCards.add(i);
        }
        return availableCards;
    }

    /**
     * @brief sends three types of messages that mean to inform players about game starting, the cards they are given and who is attacking first
     */
    private void sendGameInitialMessages() {
        mClients.forEach(player -> {
            player.sendMessage(new Start(mTalonController.mTalon.get(0)));
            sendNewCardsMessages(player);
        });
        mClients.forEach(player -> {
            player.sendMessage(new NextRound(false, attacker(0).id, mRoom.mTalon.deck.size()));
            player.sendMessage(new Next(attacker(0).id, getInitialAvailableCardsIndices(), true));
        });
    }

    /**
     * @brief method handling all play-type messages from clients
     *
     * @param pPlayerID ID ot message's author
     * @param pPlay
     */
    public void updateGameStatus(int pPlayerID, Play pPlay) {
        if(defender().id == pPlayerID) {
            playDefenderSide(pPlayerID, pPlay);
        }
        else if(attacker(0).id == pPlayerID) {
            playAttackerSide(pPlayerID, pPlay);
        }
    }

    /**
     * @brief sends next-type message to given player
     *
     * @param pPlayerID current attacker's/defender's ID
     * @param pPlayer player that will receive a message
     * @param pAvailableCards indices of cards that current player can use
     * @param pTrueIfAttackingFalseIfDefending true if attacker, false if defender
     */
    private void sendNextPlayerMessage(int pPlayerID, ClientThread pPlayer, ArrayList<Integer> pAvailableCards, boolean pTrueIfAttackingFalseIfDefending) {
        pPlayer.sendMessage(new Next(pPlayerID, pAvailableCards, pTrueIfAttackingFalseIfDefending));
    }

    /**
     * @brief gets indices all cards that current player is able to play
     *
     * @param pPlayerID current player's ID
     * @param pItIsMe used to block passing information if method is used in messages to other players
     * @param pTrueIfAttackingFalseIfDefending true if current player is an attacker, false if defender
     * @return either ArrayList of indices of available cards or null
     */
    private ArrayList<Integer> getPlayersAvailableCards(int pPlayerID, boolean pItIsMe, boolean pTrueIfAttackingFalseIfDefending) {
        if(pItIsMe) {
            return getAvailableCards(pPlayerID, pTrueIfAttackingFalseIfDefending);
        }
        return null;
    }

    /**
     * @brief sends broadcast message containing information about next player to play a card
     *
     * @param pPlayerID ID of next player to play a card
     * @param pTrueIfAttackingFalseIfDefending determines whether player is an attacker or a defender
     */
    private void sendNextPlayerToAll(int pPlayerID, boolean pTrueIfAttackingFalseIfDefending) {
        mClients.forEach(player -> sendNextPlayerMessage(pPlayerID, player,
                getPlayersAvailableCards(pPlayerID, player.getID() == pPlayerID, pTrueIfAttackingFalseIfDefending), pTrueIfAttackingFalseIfDefending));
    }

    /**
     * @brief sends messages that call current defender
     */
    private void sendCallCurrentPlayerMessage(int pPlayerID, boolean pTrueIfAttackingFalseIfDefending) {
        mClients.forEach(player -> {
            ArrayList<Integer> availableCards = getPlayersAvailableCards(pPlayerID, player.getID() == pPlayerID, pTrueIfAttackingFalseIfDefending);
            sendNextPlayerMessage(pPlayerID, player, availableCards, pTrueIfAttackingFalseIfDefending);
        });
    }

    /**
     * @brief sends play message to everybody
     *
     * @param pPlayerID ID of player that played a card
     * @param pCardNumber index of the card
     * @param pTrueIfAttackingFalseOtherwise true if attacking, false otherwise
     */
    private void sendPlayMessage(int pPlayerID, int pCardNumber, boolean pTrueIfAttackingFalseOtherwise) {
        Card card = mPCController.playCard(mPCController.findPlayer(pPlayerID), pCardNumber
            ,pTrueIfAttackingFalseOtherwise);
        mClients.forEach(player -> {
            player.sendMessage(new main.java.network.message.server.Play(card, pTrueIfAttackingFalseOtherwise, pPlayerID));
        });
    }

    /**
     * @brief checks if player has definitely finished his game participation
     *
     * @param pPlayerID ID of player that will be checked
     */
    private boolean checkIfPlayerHasFinished(int pPlayerID) {
        if(mPCController.findPlayer(pPlayerID).mPlayersDeck.playersDeck.isEmpty() && mTalonController.mTalon.deck.isEmpty()) {
            if(checkIfGameEnds()) {
                resetRoom();
                return true;
            }
        }
        return false;
    }

    /**
     * @brief calls current defender
     *
      * @param pPlayerID idk
     * @param pCardNumber number of card that is played
     */
    private void callCurrentDefender(int pPlayerID, int pCardNumber) {
        sendPlayMessage(pPlayerID, pCardNumber, true);
        if(!checkIfPlayerHasFinished(pPlayerID)) {
            sendCallCurrentPlayerMessage(pPlayerID, false);
        }
    }

    /**
     * @brief Checks every possibility in case an attacker passes
     */
    private void attackerHasPassed(int pPlayerID) {
        if(defenderHasToBeatCardsOnTable()) {
            sendCallCurrentPlayerMessage(defender().id, false);
        }
        else if(allAttackersPassed()) {
            conductNextRound(false);
        }
        else {
            sendNextAttackerInfo(attacker(1).id);
        }
    }

    /**
     * @brief handles all activities that need to be done when starting new round, clearing table and sending proper messages to players
     */
    private void conductNextRound(boolean pTrueIfTakesFalseOtherwise) {
        clearTableAndGetNextAttacker(pTrueIfTakesFalseOtherwise);
        sendNextRoundMessages(pTrueIfTakesFalseOtherwise);
        sendNextAttackerInfo(attacker(0).id);
    }

    /**
     * @brief removes all cards from the table, sets number of players that passed to the standard number, and sets new defender
     */
    private void clearTableAndGetNextAttacker(boolean pTrueIfTakesFalseOtherwise) {
        mPassedAttackers = mRoom.mPlayerArrayList.size() - 1;
        mRoom.mCardsOnTable.endTurn();
    }

    /**
     * sends nextround message that informs if recent defender is taking cards and get message that informs about new cards player gets
     */
    private void sendNextRoundMessages(boolean pTrueIfTakesFalseOtherwise) {
        mClients.forEach(player -> {
            player.sendMessage(new NextRound(pTrueIfTakesFalseOtherwise, defender().id, mRoom.mTalon.deck.size()));
            sendNewCardsMessages(player);
        });
        mPCController.getNextDefender(pTrueIfTakesFalseOtherwise);
    }

    private void sendNewCardsMessages(ClientThread pPlayer) {
        ArrayList<Card> cards = mTalonController.dealCards(mPCController.findPlayer(pPlayer.getID()));
        mClients.forEach(player -> player.sendMessage(new Get(pPlayer.getID(), cards.size(), player.getID() == pPlayer.getID() ? cards : null)));
    }
    /**
     * @brief checks if all attackers have gave up this round
     *
     * @return true if there are no attackers left
     */
    private boolean allAttackersPassed() {
        return --mPassedAttackers == 0;
    }

    /**
     * @brief sends messages containing info about next attacker and his cards (that information passed to attacker himself exclusively)
     *
     * @param pPlayerID ID of next attacker
     */
    private void sendNextAttackerInfo(int pPlayerID) {
        sendNextPlayerToAll(pPlayerID, true);
    }

    /**
     * @brief checks whether player said empty message or not
     *
     * @param pCardNumber index of card
     * @return true if there is no card passed
     */
    private boolean playerPlayedNoCard(int pCardNumber) {
        return pCardNumber == -1;
    }

    /**
     * @brief handles attacker's play message
     *
     * @param pPlayerID ID of message's author
     * @param pPlay play-type message containing information about played card
     */
    public void playAttackerSide(int pPlayerID, Play pPlay) {
        if(playerPlayedNoCard(pPlay.getCardNumber())) {
            attackerHasPassed(pPlayerID);
            return;
        }

        if(!playAttackerCardAndCheckIfGameIsOver(pPlay.getCardNumber())) {
            findNextPlayerAfterAttackerPlayedCard();
        }
    }

    /**
     * @brief modify model and check if game over conditions occured
     *
     * @param pCardNumber number of played card's index
     * @return true if game is over
     */
    private boolean playAttackerCardAndCheckIfGameIsOver(int pCardNumber) {
        mPassedAttackers = mRoom.mPlayerArrayList.size() - 1;
        sendPlayMessage(attacker(0).id, pCardNumber, true);
        return checkIfPlayerHasFinished(attacker(0).id);
    }

    /**
     * @brief pick either current attacker or current defender to play a card
     */
    private void findNextPlayerAfterAttackerPlayedCard() {
        if(attackerCanPlayMoreCards()) {
            sendNextPlayerToAll(attacker(0).id, true);
        }
        else {
            sendNextPlayerToAll(defender().id, false);
        }
    }

    /**
     *
     * @param shift number of seats to skip, if 0, method chooses current attacker
     * @return player who attacks the defender
     */
    private Player attacker(int shift) {
        return mPCController.getNextAttacker(shift);
    }

    /**
     * @return current defender
     */
    private Player defender() {
        return mPCController.getDefender();
    }

    /**
     * @return true if attack can play any additional cards, false otherwise
     */
    private boolean attackerCanPlayMoreCards() {
        return !attacker(0).mPlayersDeck.playersDeck.isEmpty() &&
                maxOffensiveCardsToGive() > 0 &&
                mPCController.mCardsOnTable.attackingCards.size()  - mPCController.mCardsOnTable.defendingCards.size() <
                        defender().mPlayersDeck.playersDeck.size();
    }


    /**
     * @brief handles defender's play message
     *
     * @param pPlayerID ID of message's author
     * @param pPlay play-type message containing information about played card
     */
    public void playDefenderSide(int pPlayerID, Play pPlay) {
        if(!noCardPassed(pPlayerID, pPlay.getCardNumber())) {
            sendPlayMessage(defender().id, pPlay.getCardNumber(), false);
            playerPlayedACard();
        }
    }

    /**
     * @brief either choose current defender as next to play a card, start next round, or get an attacker
     */
    private void playerPlayedACard() {
        if (defenderHasToBeatCardsOnTable()) {
            sendCallCurrentPlayerMessage(defender().id, false);
            return;
        }
        chooseNextPlayerAfterDefenderPlayedACard();
    }

    /**
     * @brief either start new round or get an attacker to play a card
     */
    private void chooseNextPlayerAfterDefenderPlayedACard() {
         if(!checkIfPlayerHasFinished(defender().id)){
            if(!defender().isPlaying() || mPCController.mCardsOnTable.defendingCards.size() == 5) {
                conductNextRound(false);
            }
            else {
                sendNextAttackerInfo(attacker(0).id);
                mPassedAttackers = mRoom.mPlayerArrayList.size() - 1;
            }
        }
    }

    /**
     * @brief give cards to defender and start new round
     *
     * @param pPlayerID id of defender
     * @param pCardNumber id of card defender played
     * @return true if player sent no card - he gave up
     */
    private boolean noCardPassed(int pPlayerID, int pCardNumber) {
        if(playerPlayedNoCard(pCardNumber)) {
            mPCController.mCardsOnTable.giveCardsToLosingDefender(mPCController.findPlayer(pPlayerID));
            conductNextRound(true);
            return true;
        }
        return false;
    }

    /**
     * @brief check whether game has finished already or not
     *
     * @return true if game is over
     */
    public boolean checkIfGameEnds() {
        mRoom.setNumberOfPlayersInGame(mRoom.getNumberOfPlayersInGame() - 1);
        if(mRoom.getNumberOfPlayersInGame() == 1) {
            for(Player player : mRoom.mPlayerArrayList) {
                if (player.isPlaying()) {
                    mClients.forEach(client -> client.sendMessage(new End(player.id, player.nick)));
                    mRoom.isStarted = false;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return number of cards that attackers can play this round
     */
    public int maxOffensiveCardsToGive() {
        return 5 - mPCController.mCardsOnTable.attackingCards.size();
    }

    /**
     *
     * @return true if there are more attacking cards than defending ones on the table
     */
    public boolean defenderHasToBeatCardsOnTable() {
        return mPCController.mCardsOnTable.attackingCards.size() > mPCController.mCardsOnTable.defendingCards.size();
    }

    /**
     *
     * @param pPlayerId player's ID
     * @param ifAttacking true if player is an attacker, false if he's a defender
     * @return ArrayList of indices of cards that can be used this turn by a player
     */
    public ArrayList<Integer> getAvailableCards(int pPlayerId, boolean ifAttacking) {
        ArrayList<Integer> availableCards = new ArrayList<>();
        if(ifAttacking) {
            availableCards.addAll(mPCController.findPlayer(pPlayerId).mPlayersDeck.getAvailableCards(
                    mPCController.mCardsOnTable.attackingCards, mPCController.mCardsOnTable.defendingCards));
        }
        else {
            availableCards = mPCController.findPlayer(pPlayerId).mPlayersDeck.getAvailableCards(mRoom.mCardsOnTable.attackingCards.get(
                    mRoom.mCardsOnTable.defendingCards.size()));
        }
        return availableCards;
    }

    /**
     *
     * @return ID of the room
     */
    public int getRoomId() {
        return mRoom.mLobbyId;
    }

    /**
     *
     * @return maximum number of players in the room
     */
    public int getMaxPlayers() {
        return mRoom.mMaxPlayers;
    }

    /**
     * @brief resets room's properties in order to clear all the data from last game. Used between games in a room
     *
     * @param pRoom current room containing all the data about the game
     */
    public void updateRoom(Room pRoom) {
        mRoom = pRoom;
        mTalonController.mTalon = mRoom.mTalon;
        mPCController.resetRoom(mRoom);
    }

    /**
     * @brief clears room's data
     */
    public void resetRoom() {
        mRoom.mPlayerArrayList.clear();
        mTalonController.resetDeck();
        for(int i = 0; i < mRoom.mPlayersReady.size(); ++i) {
            mRoom.mPlayersReady.set(i, -1);
        }
        mPassedAttackers = 0;
        mRoom.mCardsOnTable.defendingCards.clear();
        mRoom.mCardsOnTable.attackingCards.clear();
        mRoom.isStarted = false;
        mPCController.resetPlayersIndices();
    }

}
