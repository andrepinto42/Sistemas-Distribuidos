package Threads.ThreadsClient;

import Connections.Demultiplexer;
import Threads.ThreadWorker;

public class Thread1 extends ThreadWorker {

    Demultiplexer demultiplexer;

    public Thread1(Demultiplexer dm,int tag)
    {
        super(dm, tag);
    }


    @Override
    public void HandleMessage(String data) {
        
    }
    
}
