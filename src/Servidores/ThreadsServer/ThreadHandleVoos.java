package Servidores.ThreadsServer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import Connections.Demultiplexer;
import Connections.Entry;
import Servidores.Server;
import Servidores.Dados.Reservas;
import Servidores.Dados.ServerData;
import Viagens.Cidade;
import Viagens.Reserva;
import Viagens.Voo;

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
        int numeroCidades = Integer.parseInt( sc.next());

        for (int i = 0; i < numeroCidades; i++) {
            queue.add(new Cidade( sc.next() ) );
        }

        LocalDate data = LocalDate.parse( sc.next());
        // System.out.println("A data de reserva é " + data);

        boolean isValid = ValidadeVoosFromClient(new LinkedList<>(queue));
        String idRes  =  ReservarVoo(queue,data);
        if (idRes == null)
        {
            dm.send(4,"-2");
        }
        else if(isValid && !idRes.equals("-1"))
        {
            dm.send(4,idRes.getBytes());
        }
        else if(idRes.equals("encerrado")) {
            dm.send(4, "encerrado".getBytes());
        }else
            dm.send(4, "-1".getBytes());
        
            sc.close();
    }

    private String ReservarVoo(Queue<Cidade> queue, LocalDate data) {
        Cidade origem =  queue.poll(); //BRAGA
        Cidade next = null;

        List<Voo> listaVoos = new ArrayList<>();

        for(LocalDate d : db.getDiasEncerrados()){
            if(d.equals(data)) return "encerrado";
        }

        while(!queue.isEmpty())
        {
            next= queue.poll(); //VENEZA

            Reservas allReservas = db.GetReservas();
            Voo vooFoundWithReserva;
            try {
                vooFoundWithReserva = allReservas.DecrementLugarReserva(data, origem, next);
            } catch (Exception e) {
                //Nao há espaco para marcar esse voo
                System.out.println("Reserva encontra-se cheia, o voo vai ser cancelado");
                return "-1";
            }
            
            

            if(vooFoundWithReserva == null) {
                //Buscar voos possiveis e adicionar à lista
                Voo vooFound;
                try {
                    vooFound = db.GetAllViagensPossiveis().DecrementVooLugares(origem, next);
                } catch (Exception e) {
                    //Voo nao foi possivel ser decrementado, deve ser cancelada a reserva
                    System.out.println("O Voo não suporta mais passageiros vai ter de ser cancelada a reserva");
                    return "-1";
                }
                //Nao é possivel inserir o passageiro no voo
                if (vooFound == null) return "-1";
                listaVoos.add(vooFound);
            }
            else
                listaVoos.add(vooFoundWithReserva);
            
            origem = next;
        }

        StringBuilder idReserv = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {//id com 9 digitos
            idReserv.append(random.nextInt(10));}// gerar um número aleatório entre 0 e 9

        db.GetReservas().addReserva(idReserv.toString(),listaVoos, data);

        return idReserv.toString();
    }

    private boolean ValidadeVoosFromClient(Queue<Cidade> queue) {
        List<Cidade> l;
        while(true)
        {
           if  ( (l =db.GetGrafoCidades().GetPossibleVoo(queue.poll()) )  == null) return false;
           
           if (queue.peek() == null) return true;
           
           if (!l.contains(queue.peek())) return false;

        }
    }

}
