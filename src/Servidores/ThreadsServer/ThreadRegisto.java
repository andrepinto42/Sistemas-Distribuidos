package Servidores.ThreadsServer;

import Connections.Demultiplexer;
import Connections.TaggedConnection;
import Servidores.Dados.Users;
import Servidores.Server;

import java.io.IOException;
import java.util.Scanner;

public class ThreadRegisto extends Thread {
    TaggedConnection taggedConnection;
    private static final String SucessCode = "200";
    boolean isAdmin = false;

    private String serverMessage = "start";

    public ThreadRegisto(TaggedConnection taggedConnection)
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

                Scanner sc = new Scanner(data).useDelimiter(";");
                String username = sc.next();
                String password = sc.next();
                sc.close();
                Users users = Server.getDataBase().GetUsers();
                //boolean valid = users.checkUser(username, password);
                users.addUser(username,password,false);

            } catch (IOException | InterruptedException e1) { e1.printStackTrace();}

        }while();

        HandleAuteticationSucess(demultiplexer);
    }

    private void HandleAuteticationSucess(Demultiplexer demultiplexer) {
        //String messageToClient = SucessCode;
        demultiplexer.send(1, "200");

        //Passar para a fase seguinte -> Atender pedidos do utilizador
        Thread tShowMenu = new ThreadShowMenu(demultiplexer);
        tShowMenu.start();
        Thread tHandlevoos = new ThreadHandleVoos(demultiplexer);
        tHandlevoos.start();

        System.out.println("Thread de Registo foi concluida...");
    }

}
