package Clientes.Menu.Phases;

import Clientes.Client;
import Connections.Demultiplexer;
import Viagens.Cidade;

import java.util.List;

public class PhaseAdmInserirVoo extends Phase {

    public PhaseAdmInserirVoo(Demultiplexer dm)
    {
        super(dm);

        List<Cidade> allCities = Client.GetClientData().GetAllCidades();

        String arr[] = new String[allCities.size() +1];
        arr[0] = "(ADM) Adicionar Novo Voo";
        for (int i = 0; i < allCities.size(); i++) {
            arr[i+1] = allCities.get(i).getNome();
        }

        Messages =arr;

        TipForInput = "Origem";

        InputForStages = new String[]{
                "Destino",
                "Capacidade",
        };
        numberStages = InputForStages.length +1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        if (s.get(0).isEmpty() || s.get(1).isEmpty() || s.get(2).isEmpty())
            return null;

        //Converter braga para Braga
        String origin = ConvertToUpperCase(s.get(0));
        String destiny = ConvertToUpperCase(s.get(1));
        Integer capaci = Integer.parseInt(s.get(2));

        Cidade origem = new Cidade(origin);
        Cidade destino = new Cidade(destiny);

        try {
            boolean isValid = HandleAux(origem,destino,capaci);
            if (isValid)
            {
                String sucessMessage = "O voo " +origin+ "->" +destiny+ " foi adicionado com sucesso!\n";
                return new PhaseMainMenu(dm,sucessMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    private boolean HandleAux(Cidade origin, Cidade destiny, Integer capaci) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append(origin.getNome()).append(";");
        sb.append(destiny.getNome()).append(";");
        sb.append(capaci).append(";");

        String message = sb.toString();

        dm.send(9,message.getBytes());
        var response = dm.receive(9);
        if (new String(response).equals("200"))
            return true;
        else
            return false;
    }
    private String ConvertToUpperCase(String s1) {
        return Character.toUpperCase(s1.charAt(0)) + s1.substring(1);
    }

}