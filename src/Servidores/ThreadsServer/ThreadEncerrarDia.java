package Servidores.ThreadsServer;

import Connections.Demultiplexer;
import Servidores.Server;
import Servidores.ServerData;
import Viagens.Reserva;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class ThreadEncerrarDia extends Thread {
    Demultiplexer dm;
    private ServerData db;

    public ThreadEncerrarDia(Demultiplexer demultiplexer) {
        this.dm = demultiplexer;
        db = Server.getDataBase();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = new String(dm.receive(10));
                HandleEncerraDia(message);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
    }

    private void HandleEncerraDia(String message) throws IOException, InterruptedException {
        Scanner sc = new Scanner(message);
        LocalDate date = LocalDate.parse(sc.next());

        boolean canClose = Close(date);
        if (canClose) {
            dm.send(4, "200".getBytes());
        } else
            dm.send(4, "100".getBytes());
    }

    private boolean Close(LocalDate date) {

        if(date.isAfter(LocalDate.now()) || date.equals(LocalDate.now())){
            db.addDiaEncerrado(date);
            return true;
        }

        return false;
    }
}