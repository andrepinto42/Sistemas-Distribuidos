package Clientes.Menu.Phases;

import Clientes.Client;
import Connections.BreadthFirst;
import Connections.Demultiplexer;
import Viagens.Cidade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Stack;

public class PhaseCancelarReserva extends Phase {

    public PhaseCancelarReserva(Demultiplexer dm)
    {
        super(dm);

        List<Cidade> allCities = Client.GetClientData().GetAllCidades();

        String arr[] = new String[allCities.size() +1];
        arr[0] = "Cancelar Reserva";
        for (int i = 0; i < allCities.size(); i++) {
            arr[i+1] = allCities.get(i).getNome();
        }

        Messages =arr;

        TipForInput = "ID de reserva";

        InputForStages = new String[]{
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
        String id = s.get(0);

        try {
            boolean isValid = HandleAux(id);
            if (isValid)
            {
                String sucessMessage = "A Reserva " +id + " foi cancelada com sucesso!\n";
                return new PhaseMainMenu(dm,sucessMessage);
            }else ChangeWarningMessage("O ID de reserva está incorreto!");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    private boolean HandleAux(String id) throws Exception {

        //System.out.println(id);

        dm.send(5,id.getBytes());
        var response = dm.receive(5);
        if (new String(response).equals("200"))
            return true;
        else
            return false;
    }

}