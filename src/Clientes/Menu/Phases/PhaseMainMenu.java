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
    static boolean isAdmin= false;
    public PhaseMainMenu(Demultiplexer dm,boolean adminValue)
    {
        this(dm);

        isAdmin = adminValue;
        SetMessages();

    }
    public PhaseMainMenu(Demultiplexer dm,String sucessMessage)
    {
        this(dm);
        ChangeSucessMessage(sucessMessage);
    }

    public PhaseMainMenu(Demultiplexer dm)
    {
        super(dm);

        SetMessages();
        TipForInput = "$";
        InputForStages = new String[]{};
        numberStages = InputForStages.length +1;
    }
    private void SetMessages() {
        if (isAdmin)
        { 
            Messages = new String[]{
            "Trabalho Pratico SD",
            "",
            "Menu",
            "quit -> Sair do programa",
            "add -> adicionar um novo voo",
            "cancel -> cancelar uma reserva",
            "close -> encerrar as reservas um dia",
            };

        }
        else
        {
            Messages =  new String[]{
                "Trabalho Pratico SD",
                "",
                "Menu",
                "quit -> Sair do programa",
                "book -> fazer uma reserva",
            };
        }
        
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        String command = s.get(0);

        return HandleConsole(command,isAdmin);
    }

    public Phase HandleConsole(String command,boolean admin)
    {
        if (isAdmin){
            switch (command) {
                case "book":
                    return new PhaseBooking(dm);
                case "add":
                    return new PhaseAdmInserirVoo(dm);
                case "close":
                    return new PhaseEncerrarDia(dm);
                case "cancel":
                    return new PhaseCancelarReserva(dm);
                default:
                    break;
            }
        }
        else{
            switch (command) {
                case "book":
                    return new PhaseBooking(dm);
                default:
                    break;
            }
        }

        return null;
    }
}
