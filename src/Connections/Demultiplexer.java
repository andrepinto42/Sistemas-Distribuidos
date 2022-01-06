package Connections;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demultiplexer{
    public TaggedConnection tagConnection;
    public Lock lock;
    private final Map<Integer,Entry> buf = new HashMap<>();

    private class Entry{
        public final Condition cond = lock.newCondition();
        public final ArrayDeque<byte[]> queue = new ArrayDeque<>();
        int waiters = 0;
    }

    private Entry get(int tag){
        Entry e = buf.get(tag);
        if ( e == null)
        {
            e = new Entry();
            buf.put(tag,e);
        }
        return e;
    }

    public Demultiplexer(TaggedConnection taggedConnection) {
        tagConnection = taggedConnection;
        lock = new ReentrantLock();
    }

    public void start() {
        new Thread(() -> {
            try  {
                while(true)
                {
                   var f = tagConnection.receive();

                   lock.lock();
                   try{
                        //Adicionar a frame à nossa queue
                        Entry e = get(f.tag);
                        e.queue.add(f.data);
                        //Acordar thread que está à espera da informaçao
                        e.cond.signal();
                   }finally{
                       lock.unlock();
                   }
                }
            }  catch (Exception e) {
                buf.forEach((k,v) -> v.cond.signalAll());
            }
        });

    }

    public void send(int i, byte[] bytes) {
        tagConnection.send(i, bytes);
    }

    public byte[] receive(int i)throws IOException, InterruptedException {
        
        Entry e = get(i);
        e.waiters +=1;
        for(;;){
            if (! e.queue.isEmpty())
            {
                byte[] b = e.queue.poll();
                e.waiters -=1;
                if(e.queue.isEmpty() && e.waiters == 0)
                    buf.remove(i);
                
                return b;
            }
            e.cond.await();                
        }
    }

    public void close() {
        try {
            tagConnection.close();
        } catch (Exception e) {}
    }
    
}

