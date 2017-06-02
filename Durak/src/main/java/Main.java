package main.java;

import main.java.network.client.Client;
import main.java.network.server.Server;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String [ ] args)  throws IOException
    {
        try {
            Server server = new Server(3000, 10);
            server.start();
            Scanner scanner = new Scanner(System.in);
            while(!scanner.next().equals("Exit")) {} //FIXME add event handlet
            server.close();
            try {
                server.join();
            }
            catch(InterruptedException e) {
                System.out.println("Could not join server thread.");
            }
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
