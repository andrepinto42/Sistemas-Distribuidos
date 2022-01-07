package Threads.ThreadsServer;

import Connections.Demultiplexer;
import Threads.ThreadWorker;

public class ThreadServerAutentication extends ThreadWorker{

    public ThreadServerAutentication(Demultiplexer dm,int tag)
    {
        super(dm, tag);
    }
    @Override
    public void HandleMessage(String data) {
        System.out.println("Received " + data);        
    }
    
}
