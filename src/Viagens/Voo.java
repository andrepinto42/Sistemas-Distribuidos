package Viagens;

public class Voo {
    public Cidade origem;
    public Cidade destino;
    public Integer lugaresLivres;


    //final Integer lugares = 350;

    public Voo(Cidade o, Cidade d, Integer c) {
        this.origem = o;
        this.destino = d;
        this.lugaresLivres = c;
    }

    public Voo() {
        this.origem = new Cidade();
        this.destino = new Cidade();
        this.lugaresLivres = 0;
    }

    public Cidade getOrigem() {
        return origem;
    }

    public void setOrigem(Cidade origem) {
        this.origem = origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public void setDestino(Cidade destino) {
        this.destino = destino;
    }

    public Integer getLugaresLivres() {
        return lugaresLivres;
    }

    public void setLugaresLivres(Integer lugaresLivres) {
        this.lugaresLivres = lugaresLivres;
    }

}
