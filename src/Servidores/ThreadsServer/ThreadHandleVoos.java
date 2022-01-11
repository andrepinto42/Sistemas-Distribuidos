package Servidores.ThreadsServer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import Connections.Demultiplexer;
import Servidores.Server;
import Servidores.ServerData;
import Viagens.Cidade;

public class ThreadHandleVoos extends Thread {
    Demultiplexer dm;
    private ServerData db;
    
    public ThreadHandleVoos(Demultiplexer demultiplexer) {
        this.dm = demultiplexer;
        db = Server.getDataBase();
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String message =new String( dm.receive(4));
                HandleVoosFromClient(message);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }

    }

    private void HandleVoosFromClient(String message) throws IOException, InterruptedException {
        Scanner sc = new Scanner(message);
        sc.useDelimiter(";");

        Queue<Cidade> queue = new LinkedList<>();
        while(sc.hasNext())
        {
            String s = sc.next();
            Cidade c = new Cidade(s);
            queue.add(c);
        }

        boolean isValid = ValidadeVoosFromClient(queue);
        
        if(isValid)
            dm.send(4,"200".getBytes());
        else
            dm.send(4,"100".getBytes());
    }

    private boolean ValidadeVoosFromClient(Queue<Cidade> queue) {
        List<Cidade> l;
        while(true)
        {
           if  ( (l =db.GetPossibleVoo(queue.poll()) )  == null) return false;
           
           if (queue.peek() == null) return true;
           
           if (!l.contains(queue.peek())) return false;

        }
    }

}
