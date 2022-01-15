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
    //Map<LocalDate,List<Voo>> todosVoos = new HashMap<>();

    Lock lockViagensPossiveis = new ReentrantLock();
    Users allUsers = new Users();
    GrafoCidades grafoCidades = new GrafoCidades();
   

    public ServerData()
    {
        AddCities();
        AddVoos();
        AddUsers();
    }

    private void AddUsers() {
        allUsers.addUser("andre","123", false);
        allUsers.addUser("rui","123", true);
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
        
        grafoCidades.addCidade(c1);
        grafoCidades.addCidade(c2);
        grafoCidades.addCidade(c3);
        grafoCidades.addCidade(c4);
        grafoCidades.addCidade(c5);
        grafoCidades.addCidade(c6);
        grafoCidades.addCidade(c7);
        grafoCidades.addCidade(c8);
        grafoCidades.addCidade(c9);

        grafoCidades.addVoo(c1,c2);
        grafoCidades.addVoo(c1,c3);
        grafoCidades.addVoo(c3,c4);
        grafoCidades.addVoo(c8,c9);
        grafoCidades.addVoo(c8,c4);
        grafoCidades.addVoo(c5,c6);
        grafoCidades.addVoo(c8,c9);
        grafoCidades.addVoo(c8,c7);
        grafoCidades.addVoo(c9,c1);

    }

    

    //Se pudemos fazer um voo de origem para destino tambem pudemos fazer um de destino para origem
    public void addVoo(Cidade origem,Cidade destino)
    {
       grafoCidades.addVoo(origem, destino);
    }

    public List<Cidade> GetPossibleVoo(Cidade origem)
    {
       return grafoCidades.GetPossibleVoo(origem);
    }

    public List<Cidade> GetAllCidades()
    {
        return grafoCidades.GetAllCidades();
    }
    
    public void PrintVoos()
    {
       grafoCidades.PrintVoos();
    }

    public List<Voo> GetAllVoosPossiveis()
    {
        try
        {
            lockViagensPossiveis.lock();
            return allViagensPossiveis;
        }finally {lockViagensPossiveis.unlock();}
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

    public Users GetUsers()
    {
        return allUsers;
    }
}
    

