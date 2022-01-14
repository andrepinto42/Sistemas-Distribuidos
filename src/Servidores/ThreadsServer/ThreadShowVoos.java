package Servidores.ThreadsServer;

import Connections.Demultiplexer;
import Servidores.Server;
import Servidores.ServerData;
import Viagens.Cidade;
import Viagens.Reserva;
import Viagens.Voo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ThreadShowVoos extends Thread {
    Demultiplexer dm;
    private ServerData db;

    public ThreadShowVoos(Demultiplexer demultiplexer) {
        this.dm = demultiplexer;
        db = Server.getDataBase();
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String message =new String( dm.receive(6));
                HandleVoosFromClient();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
    }

    private void HandleVoosFromClient() throws IOException, InterruptedException {

        boolean isValid  =  ListarVoos();
        if(isValid)
        {
            dm.send(4,"200".getBytes());
        }
        else
            dm.send(4,"100".getBytes());
    }

    private boolean ListarVoos() {

        if(db.GetAllVoosPossiveis().isEmpty()) return false;

        for(Voo v : db.GetAllVoosPossiveis()){
            System.out.println("ORIGEM -> DESTINO\n");
            System.out.println(v.getOrigem().getNome() + "->" + v.getDestino().getNome() + "\n");
        }
        return true;
    }
}