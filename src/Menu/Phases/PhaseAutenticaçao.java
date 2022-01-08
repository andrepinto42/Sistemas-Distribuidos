package Menu.Phases;

import java.io.IOException;
import java.util.List;

import Connections.Demultiplexer;
import Threads.ThreadClient.ThreadGetInfoServer;

public class PhaseAutenticaçao extends Phase{
    
    public PhaseAutenticaçao(Demultiplexer dm)
    {
        super(dm);
        Messages =  new String[]{
            "Autenticação",
            "",
        };
        TipForInput = "Username";

        InputForStages = new String[]{
            "Password",
        };
        numberStages = InputForStages.length +1;
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

            ChangeWarningMessage(answerString);
            
            if (answerString.equals("200"))
            {
                //Cliente entrou com sucesso no servidor
                
                //Cliente demonstra que quer comunicar com o servidor
                dm.send(2, "Show".getBytes());
                Thread getInfoServer = new ThreadGetInfoServer(dm);
                getInfoServer.start();
                return new PhaseMainMenu(dm);
            }
            else
            {
                System.out.println("Autenticaçao do cliente falhou");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
