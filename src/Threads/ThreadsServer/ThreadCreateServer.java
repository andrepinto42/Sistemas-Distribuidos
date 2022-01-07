package Threads.ThreadsServer;

import java.io.IOException;

import Connections.Demultiplexer;
import Connections.TaggedConnection;

public class ThreadCreateServer extends Thread {
    TaggedConnection taggedConnection;
    public ThreadCreateServer(TaggedConnection taggedConnection)
    {
        this.taggedConnection = taggedConnection;
    }

    @Override
    public void run() {
        
        Demultiplexer demultiplexer = new Demultiplexer(taggedConnection);
        demultiplexer.start();

        String data = null;
        do {
      
        demultiplexer.send(1, "start".getBytes());
        try {
            byte[] arr = demultiplexer.receive(1);
            data = new String(arr);
            System.out.println("Recebi isto chefe " + data);
        } catch (IOException | InterruptedException e1) { e1.printStackTrace();}
        } while (!data.equals("ok"));
        // Thread t = new ThreadServerAutentication(demultiplexer, 1);
        // t.start();

        // try {
        //     t.join();
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        
        demultiplexer.close();
    }
}
