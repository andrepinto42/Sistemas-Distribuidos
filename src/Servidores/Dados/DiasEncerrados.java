package Servidores.Dados;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiasEncerrados {
    List<LocalDate> diasEncerrados = new ArrayList<>(); //dias encerrados pelo adm


    Lock lockDiasEncerrados = new ReentrantLock();  
    
    public void addDiaEncerrado(LocalDate data){
        try{
            lockDiasEncerrados.lock();
            diasEncerrados.add(data);
        }finally {
            lockDiasEncerrados.unlock();
        }
    }
}
