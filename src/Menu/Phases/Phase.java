package Menu.Phases;

import java.util.List;
import java.util.Scanner;

import Clientes.Client;
import Connections.Demultiplexer;
import DataBase.ClientData;
import Menu.ShowMenu;

public abstract class Phase {
    protected  String[] Messages;
    protected  String TipForInput;
    public  int numberStages = 1;
    protected  String[] InputForStages;
    protected String warningMessageTop = "";
    protected String sucessMessage = "";
    protected ClientData clientData;
    
    protected Demultiplexer dm;
    
    public Phase(Demultiplexer dm)
    {
        this.dm = dm;
        clientData = Client.GetClientData();
    }
    public Phase()
    {
        clientData = Client.GetClientData();
    }

    public String Show(Scanner sc)
    {
        if (!warningMessageTop.isEmpty())
            System.out.print(warningMessageTop);
        
        ShowMenu.Print(Messages,TipForInput,sucessMessage);
        System.out.print(":");  
        return sc.nextLine();
    }

    public String Next(Scanner sc,int i)
    {
        System.out.print(InputForStages[i] + ":");            
        return sc.nextLine();
    }

    public abstract Phase HandleCommand(List<String> s);

    protected void ChangeWarningMessage(String s) {
        warningMessageTop = s;
    }
    protected void ChangeSucessMessage(String s)
    {
        sucessMessage = s;
    }
  
}
