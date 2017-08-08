package test.java;

import main.java.controller.server.RoomController;
        import main.java.model.server.*;
        import main.java.network.message.Message;
        import main.java.network.server.ClientThread;
import org.testng.annotations.Test;

import java.util.LinkedList;
        import java.util.concurrent.CopyOnWriteArrayList;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static org.testng.AssertJUnit.assertEquals;

public class RoomControllerTest {
    LinkedList<ClientThread> clients;
    Room mRoom;
    CopyOnWriteArrayList<Message> mMessages;

//    @Before
//    public void setUp() {
//        mMessages = new CopyOnWriteArrayList<>();
//        clients = new LinkedList<>();
//        mRoom = new Room("test", 4);
//        for (int i = 0; i < 3; ++i) {
//            mRoom.mPlayerArrayList.add(new PlayerData(i));
//            mRoom.mPlayersReady.add(i);
//            mRoom.mPlayerArrayList.get(i).mPlayersDeck.playersDeck.add(new CardLayout(Figures.ACE, Suit.DIAMONDS));
//        }
//        mRoom.mPlayerArrayList.add(new PlayerData(3));
//        mRoomController = new RoomController(mRoom, clients);
//    }

    @Test
    public void checkIfGameEnds() {
        Room room = new Room("test", 4);
        for(int i = 0; i < 4; ++i) {
            room.mPlayerArrayList.add(new Player(i, "test"));
            room.mPlayerArrayList.get(0).mPlayersDeck.playersDeck.add(new Card(Figures.ACE, Suit.CLUBS));
        }
        RoomController roomController = new RoomController(room, new LinkedList<>());
        room.setNumberOfPlayersInGame(2);
        System.out.println(room.getNumberOfPlayersInGame());
        assertEquals(true, roomController.checkIfGameEnds());
    }
//
//    @Test
//    public void checkMaxNumberOfPlayers() {
//        assertEquals(4, mRoomController.getMaxPlayers());
//    }
//
//    @Test
//    public void checkRoomName() {
//        assertEquals(mRoomController.getRoomName(), "test");
//    }
}
