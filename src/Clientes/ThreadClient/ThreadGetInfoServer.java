package Clientes.ThreadClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Clientes.Client;
import Clientes.ClientData;
import Connections.Demultiplexer;
import Viagens.Cidade;

public class ThreadGetInfoServer extends Thread {
    private ClientData clientData;
    private Demultiplexer dm;
    
    public ThreadGetInfoServer(Demultiplexer dm)
    {
        this.dm = dm;
        clientData = Client.GetClientData();
    }
    @Override
    public void run() {
        try {
            var answer = dm.receive(3);
            int size =ParseFirstMessageShow(answer);
            // System.out.println("I received the voos from server " + new String(answer));

            answer = dm.receive(3);
            ParseSecondMessageShow(answer,size);
            // System.out.println("I received destination list from server " + new String(answer));


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nObtained all Info From Server, thread exiting...\n...");
    }

  

    private int ParseFirstMessageShow(byte[] answer) {
        Scanner sc = new Scanner(new String( answer));
        sc.useDelimiter(";");
        int size = Integer.parseInt( sc.next());
        var mapVoos = clientData.GetAllVoos();
        var allCidades = clientData.GetAllCidades();

        for (int i = 0; i < size; i++) {
            Cidade cidade = new Cidade( sc.next());
            mapVoos.put(cidade, new ArrayList<>());
            allCidades.add(cidade);    
        }
        sc.close();
        return size;
    }

    private void ParseSecondMessageShow(byte[] answer,int size) {

        var allCidades = clientData.GetAllCidades();

        Scanner sc = new Scanner(new String( answer));
        sc.useDelimiter("@");
        for (int i = 0; i < size; i++) {
            String lineDestination = sc.next();

            Scanner scVooDestino = new Scanner(lineDestination);
            // System.out.println("got line " + lineDestination);
            scVooDestino.useDelimiter(";");
            while(scVooDestino.hasNext())
            {
                String destino = scVooDestino.next();
                // System.out.println("Adding " + allCidades.get(i).getNome() + " -> " + destino);
                clientData.addVoo(allCidades.get(i) ,new Cidade( destino) );
            }
            scVooDestino.close();
        }
        sc.close();
    }
}
