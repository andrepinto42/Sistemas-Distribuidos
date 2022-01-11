package Clientes;

import java.io.IOException;
import java.net.Socket;

import Clientes.Menu.Interpreter;
import Connections.Demultiplexer;
import Connections.Frame;
import Connections.TaggedConnection;

public class Client {
    private static ClientData clientData;

    public static void main(String[] args) throws IOException {
        
        TaggedConnection tag = InitializeClient();


        Demultiplexer demultiplexer = new Demultiplexer(tag);
        demultiplexer.start();
        
        Interpreter it = new Interpreter(demultiplexer);
        it.Initialize();
        
        demultiplexer.close();
        System.out.println("Client shutted down");
        tag.close();
    }


    private static TaggedConnection InitializeClient() { 
        Socket socketClient=null;
        try {
            socketClient= new Socket("localhost", 1234);
        } catch (IOException e) { e.printStackTrace();        }
        
        TaggedConnection tag = new TaggedConnection(socketClient);
        clientData = new ClientData();
        return tag;
    }

    public static ClientData GetClientData()
    {
        return clientData;
    }

    // private static void test1(TaggedConnection tag)
    // {

    //  Runnable tw = new ThreadWorker(tag);
        
    //     tw.run();
    // }
}
