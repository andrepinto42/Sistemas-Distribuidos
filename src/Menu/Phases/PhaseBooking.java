package Menu.Phases;

import java.util.List;

import Clientes.Client;
import Connections.Demultiplexer;
import Connections.BreadthFirst;
import Servidores.Server;
import Viagens.Cidade;

public class PhaseBooking extends Phase {
    
    public PhaseBooking(Demultiplexer dm)
    {
        super(dm);

        List<Cidade> allCities = Client.GetClientData().GetAllCidades();

        String arr[] = new String[allCities.size() +1];
        arr[0] = "Marcar Voo";
        for (int i = 0; i < allCities.size(); i++) {
            arr[i+1] = allCities.get(i).getNome();
        }

        Messages =arr;
        
        TipForInput = "Origem";

        InputForStages = new String[]{
            "Destino",
            "Dia",
        };
        numberStages = InputForStages.length +1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        Cidade origem = new Cidade(s.get(0));
        Cidade destino = new Cidade( s.get(1));
        String dia = s.get(2);

        clientData.PrintVoos();
        
        var tree = BreadthFirst.BFS(clientData.GetAllVoos(), origem, destino);

        for (var entry : tree.entrySet()) {
            System.out.println(entry.getKey().getNome() + " e " + entry.getValue().getNome());
        }
        return null;
    }
}
