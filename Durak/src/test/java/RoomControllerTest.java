package test.java;

import main.java.controller.server.RoomController;
        import main.java.model.server.*;
        import main.java.network.message.Message;
        import main.java.network.server.ClientThread;
        import org.junit.Before;
        import org.junit.Test;

        import java.util.LinkedList;
        import java.util.concurrent.CopyOnWriteArrayList;

        import static org.junit.Assert.assertEquals;

public class RoomControllerTest {
    RoomController mRoomController;
    LinkedList<ClientThread> clients;
    Room mRoom;
    CopyOnWriteArrayList<Message> mMessages;

    @Before
    public void setUp() {
        mMessages = new CopyOnWriteArrayList<>();
        clients = new LinkedList<>();
        mRoom = new Room("test", 4);
        for (int i = 0; i < 3; ++i) {
            mRoom.mPlayerArrayList.add(new Player(i));
            mRoom.mPlayersReady.add(i);
            mRoom.mPlayerArrayList.get(i).mPlayersDeck.playersDeck.add(new Card(Figures.ACE, Suit.DIAMONDS));
        }
        mRoom.mPlayerArrayList.add(new Player(3));
        mRoomController = new RoomController(mRoom, clients);
    }

    @Test
    public void checkIfGameEnds() {
        assertEquals(false, mRoomController.checkIfGameEnds());
    }

    @Test
    public void checkMaxNumberOfPlayers() {
        assertEquals(4, mRoomController.getMaxPlayers());
    }

    @Test
    public void checkRoomName() {
        assertEquals(mRoomController.getRoomName(), "test");
    }
}
