package DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Viagens.Cidade;

public class ClientData {
    List<Cidade> allCidades = new ArrayList<>();
    Map<Cidade,List<Cidade>> allVoos = new HashMap<>();
    
    public ClientData()
    {

    }

    public void addVoo(Cidade origem,Cidade destino)
    {
        List<Cidade> allVoosDestino = allVoos.get(origem);
        if (allVoosDestino == null)
        {
            List<Cidade> l = new ArrayList<Cidade>();
            l.add(destino);
            allVoos.put(origem,l);
        }
        else if (!allVoosDestino.contains(destino))
            allVoosDestino.add(destino);
        
        List<Cidade> allVoosOrigem = allVoos.get(destino);
        if (allVoosOrigem == null)
        {
            List<Cidade> l = new ArrayList<Cidade>();
            l.add(origem);
            allVoos.put(destino,l);
        }
        else if (!allVoosOrigem.contains(origem))
            allVoosOrigem.add(origem);
    }

    public Map<Cidade,List<Cidade>> GetAllVoos()
    {
        return allVoos;
    }
    
    public List<Cidade> GetAllCidades()
    {
        return allCidades;
    }
}