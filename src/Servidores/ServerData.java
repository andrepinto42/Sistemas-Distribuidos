package Servidores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import Viagens.Cidade;
import Viagens.Reserva;
import Viagens.Voo;

public class ServerData {

    List<Cidade> allCidades = new ArrayList<>();
    Map<Cidade,List<Cidade>> allVoos = new HashMap<>();
    Lock lockVoos = new ReentrantLock(); 
    List<Reserva> reservas = new ArrayList<>(); //reservas dos clientes | cliente pode remover se dia não tiver encerrado pelo ADM
    List<LocalDate> diasEncerrados = new ArrayList<>(); //dias encerrados pelo adm
    List<Voo> allViagensPossiveis = new ArrayList<>();
    Lock lockViagensPossiveis = new ReentrantLock();
    
   

    public ServerData()
    {
        AddCities();
        AddVoos();
    }

    private void AddVoos() {
        allViagensPossiveis.add(new Voo(new Cidade("Braga"), new Cidade("Veneza"), 2));
        allViagensPossiveis.add(new Voo(new Cidade("Veneza"),new Cidade("Turim"), 2));
        allViagensPossiveis.add(new Voo(new Cidade("Turim"),new Cidade("Nevada"), 2));
    }

    private void AddCities() {
        Cidade c1 = new Cidade("Braga");
        Cidade c2 = new Cidade("Porto");
        Cidade c3 = new Cidade("Madrid");
        Cidade c4 = new Cidade("New York");
        Cidade c5 = new Cidade("Pyongyang");
        Cidade c6 = new Cidade("Hong Kong");
        Cidade c7 = new Cidade("Nevada");
        Cidade c8 = new Cidade("Turim");
        Cidade c9 = new Cidade("Veneza");
        allCidades.add(c1 );
        allCidades.add(c2 );
        allCidades.add(c3 );
        allCidades.add(c4 );
        allCidades.add(c5 );
        allCidades.add(c6 );
        allCidades.add(c7 );
        allCidades.add(c8 );
        allCidades.add(c9 );


        addVoo(c1,c2);
        addVoo(c1,c3);
        addVoo(c3,c4);
        addVoo(c8,c9);
        addVoo(c8,c4);
        addVoo(c5,c6);
        addVoo(c8,c9);
        addVoo(c8,c7);
        addVoo(c9,c1);
    }

    

    //Se pudemos fazer um voo de origem para destino tambem pudemos fazer um de destino para origem
    public void addVoo(Cidade origem,Cidade destino)
    {
        try{
            lockVoos.lock();
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
        finally{ lockVoos.unlock();}
    }

    public List<Cidade> GetPossibleVoo(Cidade origem)
    {
        try{
            lockVoos.lock();
            return allVoos.get(origem);
        }finally{ lockVoos.unlock();}
    }

    public List<Cidade> GetAllCidades()
    {
        try{
            lockVoos.lock();
            return allVoos.keySet().stream().collect(Collectors.toList());
        }finally{ lockVoos.unlock();}
    }
    public List<Voo> GetAllVoosPossiveis()
    {
        try
        {
            lockViagensPossiveis.lock();
            return allViagensPossiveis;
        }finally {lockViagensPossiveis.unlock();}
    }

    public void PrintVoos()
    {
        try
        {
            lockVoos.lock();
            System.out.println("-------------------------\nPrinting All Voos");
            for (var entry : allVoos.entrySet()) {
                System.out.println("Cidade origem -> " + entry.getKey().getNome());
                System.out.print("Cidades destino -> ");
                for (Cidade cidade : entry.getValue()) {
                    System.out.print(cidade.getNome() + " | ");
                }
                System.out.print("\n-------------------------\n");
            }

        }finally{ lockVoos.unlock();}
    }

    public Integer getVooLugares(Cidade origem,Cidade destino)
    {
        try
        {
            lockVoos.lock();

            for(Voo v : this.allViagensPossiveis){
            if(v.getOrigem().equals(origem) && v.getDestino().equals(destino))
                return v.getLugaresLivres();
            }
            return -1;

        }finally{ lockVoos.unlock();}
        
            
    }
    public void PrintAllVoosPossiveis()
    {
        for (Voo v : allViagensPossiveis) {
            System.out.println(v.origem.getNome() + " para " + v.destino.getNome() + " lugares = " + v.lugaresLivres);
        }
    }

    public boolean DecrementVooLugares(Cidade origem,Cidade destino)
    {
        lockViagensPossiveis.lock();
        try{
            for (Voo voo : allViagensPossiveis) {
                if (voo.origem.equals(origem) && voo.destino.equals(destino))
                {
                    if (voo.lugaresLivres > 0)
                    {
                        voo.lugaresLivres--;
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }finally{lockViagensPossiveis.unlock();}
    }


     
    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<LocalDate> getDiasEncerrados() {
        return diasEncerrados;
    }

    public void setDiasEncerrados(List<LocalDate> diasEncerrados) {
        this.diasEncerrados = diasEncerrados;
    }
}
    

