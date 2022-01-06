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
        public final Condition cond;
        public final ArrayDeque<byte[]> queue = new ArrayDeque<>();
        public Lock lock = new ReentrantLock();
        public Entry()
        {
            cond = this.lock.newCondition();
        }
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
                    Entry e=null;
                    try{
                         //Adicionar a frame à nossa queue
                         e = get(f.tag);  
                         //Fechar o acesso desta entry pois estamos prestes a escrever
                         e.lock.lock();

                         e.queue.add(f.data);
                         // System.out.println("Dados da queue");
                         // for (var v : e.queue) {
                         //     System.out.print( new String(v) + " ");
                         // }

                         //Acordar thread que está à espera da informaçao
                         e.cond.signal();
                    }finally{
                        e.lock.unlock();
                   }
                }
            }  
            catch (Exception e) {
                System.out.println("Servidor foi interrompido");
                for (var entrada : bufferMensagens.values()) {
                    entrada.lock.lock();
                    entrada.cond.signalAll();
              }
            }
        });

        t.start();

    }

    public void send(int i, byte[] bytes) {
        tagConnection.send(i, bytes);
    }

    public byte[] receive(int i)throws IOException, InterruptedException {
        
        Entry e = get(i);
        e.waiters +=1;
        e.lock.lock();
        for(;;){
            if (! e.queue.isEmpty())
            {
                byte[] b = e.queue.poll();
                e.waiters -=1;
            
                return b;
            }
            //Este await liberta automaticamente o lock;
            System.out.println("Waiting for a signal man");
            e.cond.await();
        }

    }

    public void close() {
        try {
            tagConnection.close();
        } catch (Exception e) {}
    }
    
}

