package Servidores.ThreadsServer;

import Connections.Demultiplexer;
import Connections.TaggedConnection;
import Servidores.Dados.ServerData;
import Servidores.Dados.Users;
import Servidores.Server;

import java.io.IOException;
import java.util.Scanner;

public class ThreadRegisto extends Thread {
    Demultiplexer dm;
    private ServerData db;

    public ThreadRegisto(Demultiplexer demultiplexer) {
        this.dm = demultiplexer;
        db = Server.getDataBase();
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String message =new String( dm.receive(10));
                HandleRegistoUser(message);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
    }

    private void HandleRegistoUser(String message) {
        Scanner sc = new Scanner(message).useDelimiter(";");
        try {
            String username = sc.next();
            String password = sc.next();
            sc.close();
            db.GetUsers().addUser(username, password, false);
            dm.send(10, "200");
        } 
        catch (Exception e) {
            dm.send(10, "100");
        }
    }

}
