package Viagens;

public class Cidade {
    String nome;
    public Cidade(String nome)
    {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Cidade))
            return false;
        Cidade c = (Cidade) obj;

        return c.getNome().toLowerCase().equals( nome.toLowerCase());
    }
    
}