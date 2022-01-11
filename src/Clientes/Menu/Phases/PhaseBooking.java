package Clientes.Menu.Phases;

import java.util.List;
import java.util.Map;
import java.util.Stack;

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
        
        try {
            boolean isValid = HandleVooConnections(origem,destino,dia);
            if (isValid)
            {
                String sucessMessage = "Voo de "+ origin + " para o destino " + destiny + " foi adicionada com sucesso!\n";
                return new PhaseMainMenu(dm,sucessMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    private boolean HandleVooConnections(Cidade origem, Cidade destino, String dia) throws Exception {
        Stack<Cidade> caminhoStack = null;
        
        caminhoStack = BreadthFirst.BFS(clientData.GetAllVoos(), origem, destino);

        StringBuilder sb = new StringBuilder();
        while(!caminhoStack.empty())
        {
            sb.append(caminhoStack.pop().getNome()).append(";");
        }
        String message = sb.toString();
        System.out.println(message);

        dm.send(4,message.getBytes());
        var response = dm.receive(4);
        if (new String(response).equals("200"))
            return true;
        else
            return false;
    }


    private String ConvertToUpperCase(String s1) {
        return Character.toUpperCase(s1.charAt(0)) + s1.substring(1);
    }
}
