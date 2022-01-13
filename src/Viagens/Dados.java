package Viagens;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dados {
    //List<Voo> allVoos = new ArrayList<>(); //lista de voos | ADM pode adicionar novos voos
    Map<LocalDate,List<Voo>> allVoos = new HashMap<>(); //data e voos para essa data
    List<Reserva> reservas = new ArrayList<>(); //reservas dos clientes | cliente pode remover se dia não tiver encerrado pelo ADM
    List<LocalDate> diasEncerrados = new ArrayList<>(); //dias encerrados pelo adm
    Lock lockVoos = new ReentrantLock();


    public Map<LocalDate, List<Voo>> getViagens() {
        return allVoos;
    }

    public void setViagens(Map<LocalDate, List<Voo>> viagens) {
        this.allVoos = viagens;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void PrintVoos()
    {
        System.out.println("-------------------------\nPrinting All Voos");
        for (List<Voo> lista : allVoos.values()) {

            for(Voo v : lista){
                System.out.println("Cidade origem -> " +v.getOrigem().getNome());
                System.out.print("Cidade destino -> " +v.getDestino().getNome());

            }
            System.out.print("\n-------------------------\n");
        }
    }

    public void adicionarVoo(LocalDate data, Cidade ori, Cidade dest, Integer capac){ //pelo ADM
        Voo novoVoo = new Voo(ori,dest,capac);

        if(!allVoos.containsKey(data)){
            List<Voo> novaLista = new ArrayList<>();
            novaLista.add(novoVoo);
            this.allVoos.put(data,novaLista);
        }else{
            List<Voo> list1 = this.allVoos.get(data);
            list1.add(novoVoo);
        }
    }

    public void cancelarReserva(String id){
        for(Reserva re : this.reservas){
            if(this.diasEncerrados.contains(re.getData())){ //se true dia foi encerrado pelo adm
                System.out.println("Reserva não pode ser cancelada (Dia Encerrado pelo ADM)!");
                return;
            }else{

                if(re.getIdReserva().equals(id)) reservas.remove(re);
            }
        }
    }

    public void encerrarDia(LocalDate dia){
        this.diasEncerrados.add(dia);
    }


}
