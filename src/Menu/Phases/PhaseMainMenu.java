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
    public PhaseMainMenu(Demultiplexer dm)
    {
        super(dm);

        Messages =  new String[]{
            "Trabalho Pratico SD",
            "",
            "Menu",
            "quit -> Sair do programa",
            "book -> Ver todos os voos possiveis",
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
