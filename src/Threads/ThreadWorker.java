package Threads;


import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import Connections.Frame;
import Connections.TaggedConnection;

public class ThreadWorker implements Runnable {

    TaggedConnection tag;
    Queue<Frame> queueFrame = new PriorityQueue<Frame>();
    
    public ThreadWorker(TaggedConnection tag) {
    this.tag = tag;
    }

    @Override
    public void run()  {
        try{
        Frame frame1 = new Frame(0,"hi".getBytes());
        tag.send(frame1);
        
        while (true)
        {
            
            Frame frame2 = tag.receive();
            
            System.out.println( frame2.toString());
        }
        }catch(Exception e){e.printStackTrace();}
    }

}
