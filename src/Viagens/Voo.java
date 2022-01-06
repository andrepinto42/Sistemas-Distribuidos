package Viagens;

public class Voo {
    public String origem;
    public String destino;
    public Integer lugaresLivres;


    //final Integer lugares = 350;

    public Voo(String o, String d, Integer c) {
        this.origem = o;
        this.destino = d;
        this.lugaresLivres = c;
    }

    public Voo() {
        this.origem = "";
        this.destino = "";
        this.lugaresLivres = 0;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getLugaresLivres() {
        return lugaresLivres;
    }

    public void setLugaresLivres(Integer lugaresLivres) {
        this.lugaresLivres = lugaresLivres;
    }

}
