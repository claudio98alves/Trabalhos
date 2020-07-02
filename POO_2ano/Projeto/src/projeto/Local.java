/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.io.Serializable;

/**
 *
 * @author Diogo Semedo, Cláudio Alves
 */
public class Local implements Serializable,Comparable {
    protected  String coordenadas;
    protected int nInscritos;
    protected String nome;

    /**
     * Construtor da classe Local.
     * @param coordenadas Tipo: String - As coordenadas do Local.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     * @param nome Tipo: String - Nome do Local.
     */
    public Local(String coordenadas, int nInscritos, String nome) {
        this.coordenadas = coordenadas;
        this.nInscritos = nInscritos;
        this.nome= nome;
    }

    /**
     * Método de acesso à variável coordenadas.
     * @return String
     */
    public String getCoordenadas() {
        return coordenadas;
    }

    /**
     * Método de acesso à variável nInscritos.
     * @return Integer
     */
    public int getnInscritos() {
        return nInscritos;
    }

    /**
     * Método de acesso à variável nome.
     * @return String
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método para alterar a variável nInscritos.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     */
    public void setnInscritos(int nInscritos) {
        this.nInscritos = nInscritos;
    }
    
    /**
     * Método para incrementar o nInscritos em 1 unidade. 
     */
    public void incrementa(){
       this.setnInscritos(this.getnInscritos()+1);
    }

    /**
     * Método para decrementar o nInscritos em 1 unidade.
     */
    public void decrementa(){
        this.setnInscritos(this.getnInscritos()-1);
    }

    /**
     * Método usado para simplificar as listagens.
     * @return String
     */
    @Override
    public String toString() {
        return nome;
    }
    
    /**
     * Método usado para simplificar as listagens.
     * @return String
     */
    public String visualizar(){
        return nome+"  "+coordenadas+"\n"+ "Inscritos: " + nInscritos;
    }
    
    /**
     * Método usado para para ordenar a lista de locais pelo número de inscritos de froma decrescente.
     * @return Integer
     */
    @Override
    public int compareTo(Object t) {
        return (int)(((Local)t).getnInscritos()-this.nInscritos);
    }
    
    
}
