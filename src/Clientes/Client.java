package Clientes;

import java.io.IOException;
import java.net.Socket;

import Connections.Demultiplexer;
import Connections.Frame;
import Connections.TaggedConnection;
import Threads.ThreadWorker;
import Threads.ThreadsClient.Thread1;

public class Client {
    public static void main(String[] args) throws IOException {
        
        TaggedConnection tag = InitializeClient();


        Frame frame1 = new Frame(1,"hi".getBytes());
        tag.send(frame1);


        Demultiplexer demultiplexer = new Demultiplexer(tag);
        demultiplexer.start();

        ThreadWorker teste = new Thread1(demultiplexer,1);
        
        teste.start();

        try {
            teste.join();            
        } catch (Exception e) { e.printStackTrace(); }

        tag.close();
    }


    private static TaggedConnection InitializeClient() {
        Socket socketClient=null;
        try {
            socketClient= new Socket("localhost", 1234);
        } catch (IOException e) { e.printStackTrace();        }
        
        TaggedConnection tag = new TaggedConnection(socketClient);
        return tag;
    }

    // private static void test1(TaggedConnection tag)
    // {

    //  Runnable tw = new ThreadWorker(tag);
        
    //     tw.run();
    // }
}
