package Clientes;

import java.io.IOException;
import java.net.Socket;

import Connections.Demultiplexer;
import Connections.Frame;
import Connections.TaggedConnection;
import DataBase.ClientData;
import Menu.Interpreter;

public class Client {
    private static ClientData clientData;

    public static void main(String[] args) throws IOException {
        
        TaggedConnection tag = InitializeClient();


        Frame frame1 = new Frame(1,"hi".getBytes());
        tag.send(frame1);

      

        Demultiplexer demultiplexer = new Demultiplexer(tag);
        demultiplexer.start();
        
        Interpreter it = new Interpreter(demultiplexer);
        it.Initialize();
        
        //Nao passa para baixo disto
        // ThreadWorker teste = new Thread1(demultiplexer,1);
        
        // teste.start();

        // try {
        //     teste.join();            
        // } catch (Exception e) { e.printStackTrace(); }
        
        demultiplexer.close();
        
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
