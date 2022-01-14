package Servidores.ThreadsServer;

import java.io.IOException;

import Connections.Demultiplexer;
import Connections.TaggedConnection;
import Servidores.Server;

public class ThreadAutetication extends Thread {
    TaggedConnection taggedConnection;
    private static final String SucessCode = "200";
    boolean isAdmin = false; 
    
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
            
            HandleAuteticationSucess(demultiplexer);
    }

    private void HandleAuteticationSucess(Demultiplexer demultiplexer) {
        String messageToClient = SucessCode + ";" + (isAdmin ? "1" : "0") + ";"; 
        demultiplexer.send(1, messageToClient);
        
        //Passar para a fase seguinte -> Atender pedidos do utilizador
        Thread tShowMenu = new ThreadShowMenu(demultiplexer);
        tShowMenu.start();
        Thread tHandlevoos = new ThreadHandleVoos(demultiplexer);
        tHandlevoos.start();

        //Resetar o valor para o admin
        isAdmin = false;
        System.out.println("Thread de autenticação foi concluida...");
    }

    //TODO
    private boolean CheckNonValidUser(String data) {
        if (data.equals("andre;123;"))
        {
            return false;
        }
        //Por enquanto o utilizador rui é um administrador
        if(data.equals("rui;123;"))
        {
            isAdmin = true;
            return false;
        }
        serverMessage = "Dados inseridos estao incorretos\n";
        return true;
    }
}
