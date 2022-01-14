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

public class ThreadCancelaResereva extends Thread {
    Demultiplexer dm;
    private ServerData db;

    public ThreadCancelaResereva(Demultiplexer demultiplexer) {
        this.dm = demultiplexer;
        db = Server.getDataBase();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = new String(dm.receive(5));
                HandleCancelaReserva(message);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
    }

    private void HandleCancelaReserva(String message) throws IOException, InterruptedException {
        Scanner sc = new Scanner(message);
        String id = sc.next();

        boolean canCancell = CancelaR(id);
        if (canCancell) {
            dm.send(4, "200".getBytes());
        } else
            dm.send(4, "100".getBytes());
    }

    private boolean CancelaR(String id) {
        Reserva reserva = null;
        boolean ident = false;

        for(Reserva r : db.getReservas()){
            if(r.getIdReserva().equals(id)){
                reserva = r;
                ident = true;
            }
        }
        if(!ident){
            System.out.println("ID de reserva incorreto!");
            return false;
        }

        for (LocalDate d : db.getDiasEncerrados()) {
            if (d.equals(reserva.getData())) return false;
        }

        return true;
    }
}