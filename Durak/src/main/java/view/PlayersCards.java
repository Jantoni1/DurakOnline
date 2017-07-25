package main.java.view;

import javafx.scene.layout.BorderPane;
import main.java.network.client.MessageBox;

import java.util.ArrayList;

/**
 * Created by Kuba on 24.07.2017.
 */
public class PlayersCards extends BorderPane {

    private CardsOnTable mCardsOnTable;
    private PlayersPropertyDisplay mLeftPlayer;
    private PlayersPropertyDisplay mRightPlayer;
    private PlayersPropertyDisplay mTopPlayer;
    private PlayersPropertyDisplay mBottomPlayer;
    private ArrayList<PlayersPropertyDisplay> mPlayers;
    private Model mModel;


    public PlayersCards(Model pModel, int pNumberOfPlayers, MessageBox pMessageBox) {
        mModel = pModel;
        mCardsOnTable = new CardsOnTable(pModel.getAttackingCards(), pModel.getDefendingCards());
        mBottomPlayer = new PlayersPropertyDisplay(0.0, pModel.getAvailableCards(),  pMessageBox, true);
        mLeftPlayer = new PlayersPropertyDisplay(-90.0, pModel.getAvailableCards(),  pMessageBox, false);
        mRightPlayer = new PlayersPropertyDisplay(90.0, pModel.getAvailableCards(),  pMessageBox, false);
        mTopPlayer = new PlayersPropertyDisplay(0.0, pModel.getAvailableCards(),  pMessageBox, false);
        addPlayers(pNumberOfPlayers);
        setTop(mTopPlayer);
        setBottom(mBottomPlayer);
        setLeft(mLeftPlayer);
        setRight(mRightPlayer);
        setCenter(mCardsOnTable);
    }

    public void updateCardsOnTable() {
        mCardsOnTable.updateCardsOnTable();
    }

    public void updateOnePlayerCards(int pPlayerID) {
        mModel.getPlayers().forEach(player -> {
            if(player.getUserID() == pPlayerID) {
                mPlayers.get(player.getPositionOnTable()).updatePlayersCards();
            }
        });
    }

    public void updatePlayersCards() {
        mPlayers.forEach(player -> player.updatePlayersCards());
    }

    public void updatePlayers() {
        mModel.getPlayers().forEach(player -> mPlayers.get(player.getPositionOnTable()).setPlayer(player));

    }

    private void addPlayers(int pNumberOfPlayers) {
        mPlayers = new ArrayList<>(pNumberOfPlayers);
        mPlayers.add(mBottomPlayer);
        if(pNumberOfPlayers == 2) {
            mPlayers.set(1, mTopPlayer);
        }
        if(pNumberOfPlayers == 3) {
            mPlayers.set(1, mLeftPlayer);
            mPlayers.set(2, mRightPlayer);
        }
        if(pNumberOfPlayers == 4) {
            mPlayers.set(1, mLeftPlayer);
            mPlayers.set(2, mTopPlayer);
            mPlayers.set(3, mRightPlayer);
        }
        updatePlayers();
    }
}
