package Menu.Phases;

import java.io.IOException;
import java.util.List;

import Connections.Demultiplexer;

public class PhaseMainMenu extends Phase{
    
    public PhaseMainMenu(Demultiplexer dm)
    {
        Messages =  new String[]{
            "Trabalho Pratico SD",
            "",
            "Menu",
            "quit -> Sair do programa"
        };
        TipForInput = "$:";
        InputForStages = new String[]{};
        numberStages = InputForStages.length +1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {
      
        return null;
    }
}
