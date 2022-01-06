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
    public Lock lock ;
    private  Map<Integer,Entry> bufferMensagens = new HashMap<>();

    private class Entry{
        public final Condition cond = lock.newCondition();
        public final ArrayDeque<byte[]> queue = new ArrayDeque<>();
        int waiters = 0;
    }

    private Entry get(int tag){
        Entry e = bufferMensagens.get(tag);
        return e;
    }

    public Demultiplexer(TaggedConnection taggedConnection) {
        tagConnection = taggedConnection;
        lock = new ReentrantLock();
    }

    public void start() {
        //Por enquanto só lesse do buffer mensagens com um 1
        bufferMensagens.put(1, new Entry());

        Thread t = new Thread(() -> {
            try  {
                while(true)
                {
                   var f = tagConnection.receive();
                    System.out.println("Received from server " + f.toString());
                   lock.lock();
                   try{
                        //Adicionar a frame à nossa queue
                        Entry e = get(f.tag);
                        e.queue.add(f.data);
                        // System.out.println("Dados da queue");
                        // for (var v : e.queue) {
                        //     System.out.print( new String(v) + " ");
                        // }
                        //Acordar thread que está à espera da informaçao
                        e.cond.notify();
                   }finally{
                       lock.unlock();
                   }
                }
            }  catch (Exception e) {
                bufferMensagens.forEach((k,v) -> v.cond.signalAll());
            }
        });

        t.start();

    }

    public void send(int i, byte[] bytes) {
        tagConnection.send(i, bytes);
    }

    public byte[] receive(int i)throws IOException, InterruptedException {
        
        System.out.println("Hello tehre");
        Entry e = get(i);
        e.waiters +=1;
        try{
            lock.lock();
            for(;;){
                if (! e.queue.isEmpty())
                {
                    byte[] b = e.queue.poll();
                    e.waiters -=1;
                
                    //Por enquanto nao se remove nada
                    // if(e.queue.isEmpty() && e.waiters == 0)
                    //     bufferMensagens.remove(i);
                    return b;
                }
                System.out.println("Going to wait for someone to wake me up");
                e.cond.await();
            }

        }finally { lock.unlock();}
    }

    public void close() {
        try {
            tagConnection.close();
        } catch (Exception e) {}
    }
    
}

