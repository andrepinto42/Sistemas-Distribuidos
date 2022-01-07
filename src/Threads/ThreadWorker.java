package Threads;


import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import Connections.Demultiplexer;
import Connections.Frame;
import Connections.TaggedConnection;

public abstract class  ThreadWorker extends Thread {
    
    Demultiplexer demultiplexer;
    int tag;
    public ThreadWorker(Demultiplexer dm,int tag)
    {
        this.demultiplexer = dm;
        this.tag = tag;
    }

    @Override
    public void run() {
        try  {
     
            while(true)
            {
                demultiplexer.send(1, "Ola meu".getBytes());
                byte[] data = demultiplexer.receive(tag);
                HandleMessage(new String(data));
                Thread.sleep(1000);
            }

        }  catch (Exception ignored) { 
            ignored.printStackTrace();
            System.out.println("Thread nao consegui terminar");
            return;
        }
    }

    public abstract void HandleMessage(String data);
}
