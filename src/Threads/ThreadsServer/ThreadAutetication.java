package Threads.ThreadsServer;

import java.io.IOException;

import Connections.Demultiplexer;
import Connections.TaggedConnection;
import Servidores.Server;

public class ThreadAutetication extends Thread {
    TaggedConnection taggedConnection;
    private static final String SucessCode = "200";
    
    private String serverMessage = "start";
    
    public ThreadAutetication(TaggedConnection taggedConnection)
    {
        this.taggedConnection = taggedConnection;
    }

    @Override
    public void run() {
        Demultiplexer demultiplexer = new Demultiplexer(taggedConnection);
        demultiplexer.start();
            String data = null;
            do {
                    demultiplexer.send(1, serverMessage.getBytes());

                    //Enviar ao cliente para iniciar a fase de Autenticação
                try {
                    byte[] arr = demultiplexer.receive(1);
                    data = new String(arr);
                } catch (IOException | InterruptedException e1) { e1.printStackTrace();}
            
            } while (CheckNonValidUser(data));
            
            demultiplexer.send(1, SucessCode.getBytes());
            
            //Passar para a fase seguinte -> Atender pedidos do utilizador
            Thread tShowMenu = new ThreadShowMenu(demultiplexer);
            tShowMenu.start();

            System.out.println("Thread de autenticação foi concluida...");
    }

    //TODO
    private boolean CheckNonValidUser(String data) {
        if (!data.equals("andre;123;"))
        {
            serverMessage = "Dados inseridos estao incorretos\n";
            return true;
        }

        return false;
    }
}
