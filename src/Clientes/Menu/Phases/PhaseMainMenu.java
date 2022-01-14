package Clientes.Menu.Phases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Clientes.Client;
import Clientes.ClientData;
import Connections.Demultiplexer;
import Viagens.Cidade;

public class PhaseMainMenu extends Phase{
    public PhaseMainMenu(Demultiplexer dm,String sucessMessage)
    {
        this(dm);
        ChangeSucessMessage(sucessMessage);
    }

    public PhaseMainMenu(Demultiplexer dm)
    {
        super(dm);

        Messages =  new String[]{
            "Trabalho Pratico SD",
            "",
            "Menu",
            "quit -> Sair do programa",
            "book -> fazer uma reserva",
        };
        TipForInput = "$";
        InputForStages = new String[]{};
        numberStages = InputForStages.length +1;
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        String command = s.get(0);

        switch (command) {
            case "book":
                return new PhaseBooking(dm);
            default:
                break;
        }

        return null;
    }


}
