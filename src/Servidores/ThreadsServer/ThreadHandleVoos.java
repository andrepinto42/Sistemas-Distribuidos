package Servidores.ThreadsServer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import Connections.Demultiplexer;
import Connections.Entry;
import Servidores.Server;
import Servidores.ServerData;
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

    }

    private String ReservarVoo(Queue<Cidade> queue, LocalDate data) {
        Cidade origem =  queue.poll(); //BRAGA
        Cidade next = null;
        boolean found = false;

        Reserva novaReserva = new Reserva();
        List<Voo> listaVoos = new ArrayList<>();

        for(LocalDate d : db.getDiasEncerrados()){
            if(d.equals(data)) return "encerrado";
        }

        while(!queue.isEmpty())
        {
            next= queue.poll(); //VENEZA

            for(Reserva re : db.getReservas()){
                if(re.getData().equals(data)){
                    List<Voo> voos = re.getTravel();

                    for(Voo v1 : voos){
                        if( v1.origem.equals(origem) && v1.destino.equals(next)) {
                            boolean valid =db.DecrementVooLugares(origem, next);
                            if (!valid) return "-1";//Nao é possivel inserir o passageiro no voo
                            listaVoos.add(v1);
                            found = true;
                        }
                    }
                }
            }

            if(!found) {
                for (Voo v : db.GetAllVoosPossiveis()) {
                    if (v.origem.equals(origem) && v.destino.equals(next)) {
                        boolean valid = db.DecrementVooLugares(origem, next);
                        //Nao é possivel inserir o passageiro no voo
                        if (!valid) return "-1";
                        listaVoos.add(v);
                        found = true;
                    }
                }
            }
            if (!found)
            {
                //System.out.println("Esse voo nao existe man, por favor pede ao admin para inserir na base de dados");
                return null;
                // Voo newVoo = new Voo(origem, next, 4);
                // db.GetAllVoosPossiveis().add(newVoo);

                // listaVoos.add(newVoo);
            }

            origem = next;
            found = false;
        }

        StringBuilder idReserv = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {//id com 9 digitos
            idReserv.append(random.nextInt(10));}// gerar um número aleatório entre 0 e 9

        db.getReservas().add(new Reserva(idReserv.toString(),listaVoos,data));

        return idReserv.toString();
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
