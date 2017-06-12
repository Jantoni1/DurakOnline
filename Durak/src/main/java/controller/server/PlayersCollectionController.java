package main.java.controller.server;


import main.java.model.server.CardsOnTable;
import main.java.model.server.Card;
import main.java.model.server.Player;
import main.java.model.server.Room;

import java.util.ArrayList;

public class PlayersCollectionController {
    PlayersCollectionController(ArrayList<Player> pPlayerArrayList, CardsOnTable pCardsOnTable, int pMaxPlayers) {
        mPlayerArrayList = pPlayerArrayList;
        mMaxPlayers = pMaxPlayers;
        mCardsOnTable = pCardsOnTable;
        this.attackerIndex = 0;
        this.defenderIndex = 1;
        playersInGame = 0;
    }

    void addPlayer(int player_id) {
        mPlayerArrayList.add(new Player(player_id));
        System.out.println(mPlayerArrayList.size());
        ++playersInGame;
    }

    public boolean removePlayer(int player_id) {
        try {
            mPlayerArrayList.remove(findPlayer(player_id));
            return true;
        }
        catch (IndexOutOfBoundsException exception) {
            return false;
        }
    }

    public int getNextDefender(boolean pIsTaking) {
        int shift = 1;
        if(pIsTaking) {
            shift = 2;
        }
        defenderIndex += shift;
        defenderIndex %= mMaxPlayers;
        attackerIndex += defenderIndex;
        getNextAttacker(1);
        return mPlayerArrayList.get(defenderIndex).id;
    }

    public Player findPlayer(int player_id) {
        for (Player player: mPlayerArrayList) {
            if(player.getPlayer(player_id)) {
                return player;
            }
        }
        throw new IndexOutOfBoundsException("No player with given id found.");
    }

//    public int getNextPlayer(int player_id) {
//
//        while( mPlayerArrayList.indexOf(findPlayer(player_id)) % mPlayerArrayList.size();
//    }


    public Player getNextAttacker(int shift) {
        int i = 0;
        while(i<shift) {
            ++attackerIndex;
            attackerIndex %= mMaxPlayers;
            if(mPlayerArrayList.get(attackerIndex).isPlaying() && mPlayerArrayList.indexOf(mPlayerArrayList.get(attackerIndex)) != defenderIndex){
                ++i;
            }
        }
        return mPlayerArrayList.get(attackerIndex);
    }

    void resetRoom(Room pRoom) {
        mPlayerArrayList = pRoom.mPlayerArrayList;
        mCardsOnTable = pRoom.mCardsOnTable;
        mMaxPlayers = pRoom.mMaxPlayers;
    }

    public int getAttackerIndex() {
        return attackerIndex;
    }

//    public ArrayList<Card> playCard(int pPlayerId, ArrayList<Integer> pCardNumbers, boolean isAttacking) {
//        ArrayList<Card> cardArrayList = new ArrayList<>();
//        for(int cardId : pCardNumbers) {
//            cardArrayList.add(playCard(findPlayer(pPlayerId), cardId, isAttacking));
//        }
//        return cardArrayList;
//    }

    public Card playCard(Player pPlayer, int cardId, boolean isAttacking) {
        Card card = pPlayer.mPlayersDeck.playCard(cardId);
        if(!isAttacking) {
            mCardsOnTable.defendingCards.add(card);
        }
        else
        {
            mCardsOnTable.attackingCards.add(card);
        }
        return card;
    }

    public void setAttackerIndex(int attackerIndex) {
        this.attackerIndex = attackerIndex;
    }

    public int getDefenderIndex() {
        return defenderIndex;
    }

    public void setDefenderIndex(int defenderIndex) {
        this.defenderIndex = defenderIndex;
    }

    public Player getDefender() {
        return mPlayerArrayList.get(defenderIndex);
    }

    public void resetPlayersIndices() {
        attackerIndex = 0;
        defenderIndex = 1;
        --playersInGame;
    }

    public int numberOfPlayers() {
        return playersInGame;
    }

    public int getmMaxPlayers() {
        return mMaxPlayers;
    }

    private ArrayList<Player> mPlayerArrayList;
    public CardsOnTable mCardsOnTable;
    private int attackerIndex;
    private int defenderIndex;
    private  int mMaxPlayers;
    public int playersInGame;
}
