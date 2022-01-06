package Clientes;

import java.io.IOException;
import java.net.Socket;

import Connections.Frame;
import Connections.TaggedConnection;
import Threads.ThreadWorker;

public class Client {
    public static void main(String[] args) throws IOException {
        
        TaggedConnection tag = InitializeClient();

        Runnable tw = new ThreadWorker(tag);
        
        tw.run();
        
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) { e.printStackTrace();        }

        
        tag.close();
    }


    private static TaggedConnection InitializeClient() {
        Socket socketClient=null;
        try {
            socketClient= new Socket("localhost", 12345);
        } catch (IOException e) { e.printStackTrace();        }
        
        TaggedConnection tag = new TaggedConnection(socketClient);
        return tag;
    }
}
