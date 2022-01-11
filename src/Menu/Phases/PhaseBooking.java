package Menu.Phases;

import java.util.List;
import java.util.Map;

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

        //Converter braga para Braga
        String origin = ConvertToUpperCase(s.get(0));
        String destiny = ConvertToUpperCase(s.get(1));

        Cidade origem = new Cidade(origin);
        Cidade destino = new Cidade(destiny);
        String dia = s.get(2);

        
        clientData.PrintVoos();
        
        Map<Cidade, Cidade> tree = null;
        try {
            tree = BreadthFirst.BFS(clientData.GetAllVoos(), origem, destino);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Cidade end = tree.get(destino);
        int i=0;
        while(end !=null || i>20)
        {
            System.out.println(end.getNome() + " -> ");
            end = tree.get(end);
            i++;
        }

        return null;
    }

    private String ConvertToUpperCase(String s1) {
        return Character.toUpperCase(s1.charAt(0)) + s1.substring(1);
    }
}
