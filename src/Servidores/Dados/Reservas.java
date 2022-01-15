package Servidores.Dados;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Viagens.Cidade;
import Viagens.Voo;

class Reserva {

    public String idReserva;
    public List<Voo> travel = new ArrayList<>();
    public LocalDate data;

    public Reserva(){
        this.idReserva = "";
        this.travel = new ArrayList<>();
        this.data = LocalDate.now();
    }
    public Reserva(String id, List<Voo> v, LocalDate d){
        this.idReserva = id;
        this.travel = v;
        this.data = d;
    }
}
public class Reservas {
    List<Reserva> reservas = new ArrayList<>(); //reservas dos clientes | cliente pode remover se dia não tiver encerrado pelo ADM
    Lock lockReservas = new ReentrantLock();


    public void addReserva(Reserva r){
        try{
            lockReservas.lock();
            reservas.add(r);
        }finally {
            lockReservas.unlock();
        }
    }

    public void addReserva(String id, List<Voo> v, LocalDate d)
    {
        Reserva r = new Reserva(id, v, d);
        addReserva(r);
    }

    public List<Reserva> GetAllReservas(){
        return reservas;
    }

    public List<Voo> GetListVoosDay(LocalDate date)
    {
        try{
            lockReservas.lock();
            for (Reserva r : reservas) {
                if (r.data.equals(date))
                    return r.travel;
            }
            //Nao foi encontrado nenhuma reserva de voo para esse dia
            return null;
        }finally {
            lockReservas.unlock();
        }
    }

    public Voo DecrementLugarReserva(LocalDate date,Cidade origem,Cidade destino) throws Exception
    {
        try{
            lockReservas.lock();
            for (Reserva r : reservas) {
                if (r.data.equals(date))
                {
                    //Encontrada reserva que tem voos para o mesmo dia
                    for (Voo voo : r.travel) {
                        if (voo.origem.equals(origem) && voo.destino.equals(destino))
                        {
                            if (voo.lugaresLivres > 0)
                            {
                                voo.lugaresLivres--;
                                return voo;
                            }
                            //Se nao houver espaco no voo para decrementar entao não é possivel fazer a viagem
                            throw new Exception("Não existe espaço nesse voo");
                        }
                    }
                }
            }
            return null;           
        }finally {
            lockReservas.unlock();
        }
    }
}