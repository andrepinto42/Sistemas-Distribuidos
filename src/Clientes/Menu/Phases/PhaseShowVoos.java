package Clientes.Menu.Phases;

import Clientes.Client;
import Clientes.ThreadClient.ThreadGetInfoServer;
import Connections.Demultiplexer;
import Viagens.Cidade;

import java.util.List;
import java.util.Scanner;

public class PhaseShowVoos extends Phase {

    public PhaseShowVoos(Demultiplexer dm)
    {
        super(dm);

        List<Cidade> allCities = Client.GetClientData().GetAllCidades();

        String arr[] = new String[allCities.size() +1];
        arr[0] = "Voos Disponiveis";
        for (int i = 0; i < allCities.size(); i++) {
            arr[i+1] = allCities.get(i).getNome();
        }

        Messages =arr;

        TipForInput = "Intruduza uma linha qualquer";

        numberStages =  1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {

        dm.send(3,"Show");
        // Thread getInfoServer = new ThreadGetInfoServer(dm);
        // getInfoServer.start();

        // try {
        //     getInfoServer.join();
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
            while(clientData.wait)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
       
        return new PhaseMainMenu(dm,clientData.PrintVoos());

    }


}