package Viagens;

import java.time.LocalDate;

public class Viagem {

    public String idReserva;
    public Voo travel;
    public LocalDate data;

    public Viagem (String id, Voo v, LocalDate d){
        this.idReserva = id;
        this.travel = new Voo();
        this.data = d;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public Voo getTravel() {
        return travel;
    }

    public void setTravel(Voo travel) {
        this.travel = travel;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
