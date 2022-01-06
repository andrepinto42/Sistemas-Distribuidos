package Clientes;

import java.io.IOException;
import java.net.Socket;

import Connections.Demultiplexer;
import Connections.Frame;
import Connections.TaggedConnection;
import Threads.ThreadWorker;

public class Client {
    public static void main(String[] args) throws IOException {
        
        TaggedConnection tag = InitializeClient();


        Frame frame1 = new Frame(1,"hi".getBytes());
        tag.send(frame1);


        Demultiplexer demultiplexer = new Demultiplexer(tag);
        demultiplexer.start();

        Thread teste = new Thread(() -> {
            try  {
                // send request
                // demultiplexer.send(1, "Ola".getBytes());

                // get reply
                while(true)
                {
                    System.out.println("Waiting for a message");
                    byte[] data = demultiplexer.receive(1);
                    System.out.println("(1) Reply: " + new String(data));
                }

            }  catch (Exception ignored) { ignored.printStackTrace();}
        });
        
        teste.start();

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

    private static void test1(TaggedConnection tag)
    {

     Runnable tw = new ThreadWorker(tag);
        
        tw.run();
    }
}
