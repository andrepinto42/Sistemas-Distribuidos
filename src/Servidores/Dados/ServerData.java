package Servidores.Dados;


import Viagens.Cidade;
import Viagens.Voo;

public class ServerData {

    Users allUsers = new Users();
    GrafoCidades grafoCidades = new GrafoCidades();
    Reservas allReservas = new Reservas();
    ViagensPossiveis allViagensPossiveis = new ViagensPossiveis();
    DiasEncerrados allDiasEncerrados = new DiasEncerrados();
   

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
    //     allViagensPossiveis.AddVoo(new Voo(new Cidade("Braga"), new Cidade("Veneza"), 2));
    //     allViagensPossiveis.AddVoo(new Voo(new Cidade("Veneza"),new Cidade("Turim"), 2));
    //     allViagensPossiveis.AddVoo(new Voo(new Cidade("Turim"),new Cidade("Nevada"), 2));
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

        grafoCidades.addVoo(c1,c2,2);
        grafoCidades.addVoo(c1,c3,2);
        grafoCidades.addVoo(c3,c4,2);
        grafoCidades.addVoo(c8,c9,2);
        grafoCidades.addVoo(c8,c4,2);
        grafoCidades.addVoo(c5,c6,2);
        grafoCidades.addVoo(c8,c9,2);
        grafoCidades.addVoo(c8,c7,2);
        grafoCidades.addVoo(c1,c9,503);

    }

    /*

            **GETTERS**
    
    */
    public DiasEncerrados GetDiasEncerrados()
    {
        return allDiasEncerrados;
    }
    public Users GetUsers()
    {
        return allUsers;
    }
    public Reservas GetReservas()
    {
        return allReservas;
    }
    public GrafoCidades GetGrafoCidades()
    {
        return grafoCidades;
    }
    public ViagensPossiveis GetAllViagensPossiveis()
    {
        return allViagensPossiveis;
    }
}
    
