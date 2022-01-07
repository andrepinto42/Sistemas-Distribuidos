package Threads.ThreadsServer;

import java.io.IOException;

import Connections.Demultiplexer;
import Connections.TaggedConnection;

public class ThreadCreateServer extends Thread {
    TaggedConnection taggedConnection;
    private static final String SucessCode = "200";
    
    public ThreadCreateServer(TaggedConnection taggedConnection)
    {
        this.taggedConnection = taggedConnection;
    }

    @Override
    public void run() {
        
        Demultiplexer demultiplexer = new Demultiplexer(taggedConnection);
        demultiplexer.start();

        demultiplexer.send(1, "start".getBytes());

        String data = null;
        do {
                //Enviar ao cliente para iniciar a fase de Autenticação

            try {
                byte[] arr = demultiplexer.receive(1);
                data = new String(arr);
            } catch (IOException | InterruptedException e1) { e1.printStackTrace();}
        
        } while (CheckValidUser(data));
        
        demultiplexer.send(1, SucessCode.getBytes());
        
        try {
            var nextAnswer = demultiplexer.receive(2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        demultiplexer.close();
    }

    //TODO
    private boolean CheckValidUser(String data) {
        return !data.equals("andre;123;");
    }
}
