/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Diogo Semedo
 */
public class Elemento implements Serializable {
    protected String password;
    protected String perfil;
    protected String nome;
    protected int id;
    protected ArrayList<Local> locais = new ArrayList<>();
    
    /**
     * Construtor da classe Elemento.
     * @param perfil Tipo: String - Pode ser Boémio,Desportivo,Cultural e Poupadinho. 
     * @param nome Tipo: String - Nome do Elemento.
     * @param id Tipo: Integer - Identificador único de cada elemento.
     * @param password Tipo: String - Serve para controlar o acesso.
     */
    public Elemento(String perfil, String nome, int id,String password) {
        this.password=password;
        this.perfil = perfil;
        this.nome = nome;
        this.id = id;
    }

    /**
     * Método de acesso à lista de locais.
     * @return ArrayList Local
     */
    public ArrayList<Local> getLocais() {
        return locais;
    }
    
    /**
     * Método para auxilio à visualização da informação sobre o elemento.
     * @return String
     */
    public String toInformacao() {
        return "Nome: "+nome+"\n"+"Perfil: "+perfil+"\n"+"Id: "+id+"\n";
    }
    
    /**
     * Método para auxilio à visualização da informação sobre o elemento.
     * @return String 
     */
    @Override
    public String toString() {
        return nome+" "+perfil+" ";
    }

    /**
     * Método de acesso à variável password.
     * @return String
     */
    public String getPassword() {
        return password;
    }
    
    /**
     *  Método de acesso à variável perfil.
     * @return String
     */
    public String getPerfil() {
        return perfil;
    }

    /**
     * Método de acesso à variável nome.
     * @return String
     */
    public String getNome() {
        return nome;
    }

    /**
     *  Método de acesso à variável id.
     * @return Integer
     */
    public int getId() {
        return id;
    }

    /**
     * Método para remover o objeto local da lista de locais.
     * @param local Tipo: Local.
     */
    public void removeLocal(Local local){
        locais.remove(local);
    }

    /**
     * Método para adicionar local ao Elemento da lista de inscritos.
     * Return:
     * false - se o local já existe dentro da lista de inscritos, na lista de locais do Elemento.
     * true - se colocar dentro da lista de inscritos, na lista de locais do Elemento com sucesso.
     * @param local Tipo: Local.
     * @return Boolean: false or true
     */
    public boolean addLocal(Local local){
        //ve se o local ja existe 
        for(int i=0;i<locais.size();i++){
            if(locais.get(i).getNome().equals(local.getNome()))
                return false;
        }
        if(locais.size()<5 ){
            if(local instanceof Bar){
                if(((Bar) local).getnInscritos() < ((Bar) local).getLotacao()){
                    locais.add(local);
                    return true;
                }
            }
            else{
                locais.add(local);
                return true;
           }
        }
        return false;
    }
 
    
}
