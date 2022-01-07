package Menu.Phases;

import java.io.IOException;
import java.util.List;

import Connections.Demultiplexer;

public class PhaseAutenticaçao extends Phase{
    
    public PhaseAutenticaçao(Demultiplexer dm)
    {
        Messages =  new String[]{
            "Autenticação",
            "",
        };
        TipForInput = "Username";

        InputForStages = new String[]{
            "Password",
        };
        numberStages = InputForStages.length +1;
        this.dm = dm;
    }

    @Override
    public Phase HandleCommand(List<String> s) {
        String username = s.get(0);
        String password = s.get(1);
        String message = username + ";" + password + ";";
        
        dm.send(1, message.getBytes());
        System.out.println("SENT TO SERVER -> " + message);
        try {
            byte[] answerFromServer = dm.receive(1);
            String answerString = new String(answerFromServer);

            if (answerString.equals("200"))
            {
                System.out.println("Everything ok");
            }
            else
            {
                System.out.println("NOT OK");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
