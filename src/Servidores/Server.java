package Servidores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Connections.Frame;
import Connections.TaggedConnection;

public class Server {
    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(12345);
        } catch (IOException e) { e.printStackTrace();        }

        System.out.println("Esperando por Cliente...");
        Socket clientSocket = null;
        
        try {
            clientSocket = ss.accept();
            System.out.println("Cliente foi aceito com sucesso...");
        } catch (IOException e) { e.printStackTrace();        }
        

        TaggedConnection taggedConnection = new TaggedConnection(clientSocket);
        Frame frame1 = new Frame(1,"ola cliente".getBytes());
        taggedConnection.send(frame1);
        
        try {
            taggedConnection.close();
        } catch (IOException e) { e.printStackTrace();        }
    }
}
