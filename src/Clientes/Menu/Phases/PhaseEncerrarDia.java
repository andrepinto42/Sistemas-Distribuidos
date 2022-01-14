package Clientes.Menu.Phases;

import Clientes.Client;
import Connections.Demultiplexer;
import Viagens.Cidade;

import java.time.LocalDate;
import java.util.List;

public class PhaseEncerrarDia extends Phase {

    public PhaseEncerrarDia(Demultiplexer dm)
    {
        super(dm);

        List<Cidade> allCities = Client.GetClientData().GetAllCidades();

        String arr[] = new String[allCities.size() +1];
        arr[0] = "Encerrar Dia";
        for (int i = 0; i < allCities.size(); i++) {
            arr[i+1] = allCities.get(i).getNome();
        }

        Messages =arr;

        TipForInput = "Insira uma data do tipo (yyyy-mm-dd)";

        InputForStages = new String[]{
                "",
                "",
        };
        numberStages = InputForStages.length +1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        if (s.get(0).isEmpty())
            return null;

        //Converter braga para Braga
        LocalDate data = LocalDate.parse(s.get(0));

        try {
            boolean isValid = HandleAux(data);
            if (isValid)
            {
                String sucessMessage = s.get(0) + " foi encerrado com sucesso!\n";
                return new PhaseMainMenu(dm,sucessMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    private boolean HandleAux(LocalDate data) throws Exception {

        String msg = data.toString();

        dm.send(10,msg.getBytes());
        var response = dm.receive(10);
        if (new String(response).equals("200"))
            return true;
        else
            return false;
    }

}