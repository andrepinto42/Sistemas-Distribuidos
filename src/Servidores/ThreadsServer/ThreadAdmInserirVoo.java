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

public class ThreadAdmInserirVoo extends Thread {
    Demultiplexer dm;
    private ServerData db;

    public ThreadAdmInserirVoo(Demultiplexer demultiplexer) {
        this.dm = demultiplexer;
        db = Server.getDataBase();
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String message =new String( dm.receive(9));
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

        Cidade origem = new Cidade(sc.next());
        Cidade destino = new Cidade(sc.next());
        Integer capac = sc.nextInt();

        boolean isValid  =  AdicionarVoo(origem,destino,capac);
        if(isValid)
        {
            dm.send(4,"200".getBytes());
        }
        else
            dm.send(4,"100".getBytes());
    }

    private boolean AdicionarVoo(Cidade origem, Cidade destino, Integer capacidade) {

        for(Voo v : db.GetAllVoosPossiveis()){
            if(v.getOrigem().equals(origem) || v.getDestino().equals(destino)){
                System.out.println("Voo já existe!");
                return false;
            }
        }
        db.GetAllVoosPossiveis().add(new Voo(origem,destino,capacidade));
        System.out.println("Voo adicionado!");

        return true;
    }
}
