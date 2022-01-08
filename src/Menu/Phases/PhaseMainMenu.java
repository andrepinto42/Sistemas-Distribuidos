package Menu.Phases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Clientes.Client;
import Connections.Demultiplexer;
import DataBase.ClientData;
import Viagens.Cidade;

public class PhaseMainMenu extends Phase{
    ClientData clientData;
    public PhaseMainMenu(Demultiplexer dm)
    {
        Messages =  new String[]{
            "Trabalho Pratico SD",
            "",
            "Menu",
            "quit -> Sair do programa",
            "show -> Ver todos os voos possiveis",
        };
        TipForInput = "$";
        InputForStages = new String[]{};
        numberStages = InputForStages.length +1;
        this.dm = dm;

        clientData = Client.GetClientData();
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        String command = s.get(0);

        switch (command) {
            case "show":
                HandleShow();
                break;
            default:
                break;
        }

        return null;
    }

    private void HandleShow() {
        dm.send(2, "Show".getBytes());

        try {
            var answer = dm.receive(2);
            int size =ParseFirstMessageShow(answer);
            System.out.println("I received the voos from server " + new String(answer));

            answer = dm.receive(2);
            ParseSecondMessageShow(answer,size);
            System.out.println("I received destination list from server " + new String(answer));


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
            
            scVooDestino.useDelimiter("|");
            while(scVooDestino.hasNext())
            {
                String destino = scVooDestino.next();

                clientData.addVoo(allCidades.get(i) ,new Cidade( destino) );
            }
            scVooDestino.close();
        }
        sc.close();
    }

}
