package Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Connections.Demultiplexer;
import Menu.Phases.Phase;
import Menu.Phases.PhaseAutenticaçao;


public class Interpreter {
    Scanner sc;
    boolean alive = true;
    boolean userLoggedIn = false;
    List<String> CommandsByUser = new ArrayList<String>();
    
    public Demultiplexer dm;

    public Interpreter(Demultiplexer dm)
    {
        sc = new Scanner(System.in);
        this.dm = dm;
    }
    public void Initialize()
    {
        Phase phaseNow = new PhaseAutenticaçao(dm);
        
        try {
            byte[] answerFromServer = dm.receive(1);
            System.out.println("Received First message " + new String(answerFromServer));
        } catch (IOException | InterruptedException e) {e.printStackTrace();        }
        
        String comandoInput = "";
        while(alive)
        {
            //Por enquanto deixar em comentario para ver o programa a dar debug
            //ShowMenu.ClearScreen();

            comandoInput = phaseNow.Show(sc);
            if (comandoInput.equalsIgnoreCase("quit"))
            {
                if (phaseNow instanceof PhaseAutenticaçao)
                    break;
                if (!userLoggedIn)
                    break;
                phaseNow = new PhaseAutenticaçao(dm);
                continue;
            }

            CommandsByUser.add(comandoInput);
            
            for (int i = 0; i < phaseNow.numberStages-1 ; i++) {
               String comand =  phaseNow.Next(sc, i);
               CommandsByUser.add(comand);
            }
            
            Phase newPhase = phaseNow.HandleCommand(CommandsByUser);

            System.out.println(CommandsByUser +" \n\n");
            
            CommandsByUser.clear();

            if (newPhase == null) continue;
            phaseNow = newPhase;

        }
        sc.close();
    }

    public boolean isUserLogged()
    {
        return userLoggedIn;
    }
    public void SetUserLogged(boolean value)
    {
        userLoggedIn = value;
    }
}
