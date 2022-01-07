package Servidores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Connections.Demultiplexer;
import Connections.Frame;
import Connections.TaggedConnection;
import Threads.ThreadsServer.ThreadCreateServer;
import Threads.ThreadsServer.ThreadServerAutentication;

public class Server {
    public static void main(String[] args) {

        ServerSocket ss = InitializeServer();


        while(true)
        {
            System.out.println("Esperando por Cliente...");
            Socket clientSocket = null;
            
            try {
                clientSocket = ss.accept();
                System.out.println("Cliente foi aceito com sucesso...");
            } catch (IOException e) { e.printStackTrace();        }


            TaggedConnection taggedConnection = new TaggedConnection(clientSocket);
            Thread client = new ThreadCreateServer(taggedConnection);
            client.start();
        }

        //SendInfiniteOla(taggedConnection);
        
        // taggedConnection.close();
        
        // try {
        //     taggedConnection.close();
        // } catch (IOException e) { e.printStackTrace();        }
    }

    private static void SendInfiniteOla(TaggedConnection taggedConnection) {
        Frame frame1 = new Frame(1,"ola cliente".getBytes());
        while(true)
        {
            System.out.println("Sending " + frame1.toString());
            taggedConnection.send(frame1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { e.printStackTrace();            }
        }
    }

    private static ServerSocket InitializeServer() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(1234);
        } catch (IOException e) { e.printStackTrace();}
        return ss;
    }
}
