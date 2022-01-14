package Clientes.Menu.Phases;

import Clientes.Client;
import Connections.Demultiplexer;
import Viagens.Cidade;

import java.util.List;

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

        TipForInput = "";

        InputForStages = new String[]{
                "",
        };
        numberStages = InputForStages.length +1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {

        try {
            boolean isValid = HandleAux();
            if (isValid)
            {
                String sucessMessage = "Voos apresentados no servidor!\n";
                return new PhaseMainMenu(dm,sucessMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    private boolean HandleAux() throws Exception {
        byte[] msg = new byte[0];

        dm.send(6,msg);
        var response = dm.receive(6);
        if (new String(response).equals("200"))
            return true;
        else
            return false;
    }

}