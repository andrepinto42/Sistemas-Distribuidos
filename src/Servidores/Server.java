package Servidores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Connections.Frame;
import Connections.TaggedConnection;

public class Server {
    public static void main(String[] args) {

        ServerSocket ss = InitializeServer();

        System.out.println("Esperando por Cliente...");
        Socket clientSocket = null;
        
        try {
            clientSocket = ss.accept();
            System.out.println("Cliente foi aceito com sucesso...");
        } catch (IOException e) { e.printStackTrace();        }
        

        TaggedConnection taggedConnection = new TaggedConnection(clientSocket);
        Frame frame1 = new Frame(1,"ola cliente".getBytes());
        while(true)
        {
            System.out.println("Sending " + frame1.toString());
            taggedConnection.send(frame1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { e.printStackTrace();            }
        }
        
        // try {
        //     taggedConnection.close();
        // } catch (IOException e) { e.printStackTrace();        }
    }

    private static ServerSocket InitializeServer() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(12345);
        } catch (IOException e) { e.printStackTrace();}
        return ss;
    }
}
