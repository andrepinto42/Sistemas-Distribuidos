
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Viagens.Cidade;


public class BreadthFirst {

    public static Map<Cidade, Cidade> BFS( Map<Cidade,List<Cidade>> mapa,Cidade origem,Cidade destino)
    {
        Map<Cidade,Cidade> cityTree = new HashMap<>();

        //Inicializar o a estrutura de todas as cidades visitadas como falsa
        Map<Cidade,Boolean> visited = new HashMap<Cidade,Boolean>();
        for (Cidade c : mapa.keySet()) {
            visited.put(c, false);
        }

        // Create a queue for BFS
        LinkedList<Cidade> queue = new LinkedList<Cidade>();
 
        // System.out.println("Starting at " + r1.ruaNome);
        // Mark the current node as visited and enqueue it
        visited.put(origem, true);
        queue.add(origem);

        //Cidade origem não tem um pai
        cityTree.put(origem, null);
        
        Cidade current = null;
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            current = queue.poll();
            
            if (current.equals(destino))
            {
                //City of destination has been found;
                break;
            }

            for (Cidade next : mapa.get(current)) {  

                if ( ! visited.get(next) )
                {
                    visited.put(next,true);
                    queue.add(next);
                    //Adicionar a relaçao pai filho
                    cityTree.put(next,current);
                }
            }
        }
        return cityTree;
    }

    

}
