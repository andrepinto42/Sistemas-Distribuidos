package Servidores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import Viagens.Cidade;

public class GrafoCidades {
    List<Cidade> allCidades = new ArrayList<>();
    Map<Cidade,List<Cidade>> allVoos = new HashMap<>();
    Lock lock = new ReentrantLock();
    


    public void addCidade(Cidade cidade)
    {
        try {
            lock.lock();
            allCidades.add(cidade);
        } finally  {lock.unlock();}
    }
     //Se pudemos fazer um voo de origem para destino tambem pudemos fazer um de destino para origem
     public void addVoo(Cidade origem,Cidade destino)
     {
         try{
             lock.lock();
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
         finally{ lock.unlock();}
     }


     public List<Cidade> GetPossibleVoo(Cidade origem)
     {
         try{
             lock.lock();
             return allVoos.get(origem);
         }finally{ lock.unlock();}
     }

     public List<Cidade> GetAllCidades()
     {
         try{
             lock.lock();
             return allVoos.keySet().stream().collect(Collectors.toList());
         }finally{ lock.unlock();}
     }


     public void PrintVoos()
     {
         try
         {
             lock.lock();
             System.out.println("-------------------------\nPrinting All Voos");
             for (var entry : allVoos.entrySet()) {
                 System.out.println("Cidade origem -> " + entry.getKey().getNome());
                 System.out.print("Cidades destino -> ");
                 for (Cidade cidade : entry.getValue()) {
                     System.out.print(cidade.getNome() + " | ");
                 }
                 System.out.print("\n-------------------------\n");
             }
 
         }finally{ lock.unlock();}
     }
}
