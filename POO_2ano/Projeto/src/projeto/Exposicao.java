/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

/**
 *
 * @author Diogo Semedo, Cláudio Alves
 */
public class Exposicao extends Local {
    private String forma;
    private double custo;

    /**
     * Construtor da classe Exposicao.
     * @param forma Tipo: String - O tipo da Exposição.
     * @param custo Tipo: double - O custo de entrada na Exposição.
     * @param coordenadas Tipo: String - As coordenadas do Local.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     * @param nome Tipo: String - Nome do Local.
     */
    public Exposicao(String forma, double custo, String coordenadas, int nInscritos, String nome) {
        super(coordenadas, nInscritos, nome);
        this.forma = forma;
        this.custo = custo;
    }

    /**
     * Método de acesso à variável forma.
     * @return String
     */
    public String getForma() {
        return forma;
    }

    /**
     * Método de acesso à variável custo.
     * @return double
     */
    public double getCusto() {
        return custo;
    }
    @Override
    public String visualizar(){
        return super.visualizar() + " Tipo: " + forma+"\n-------------------------------\n";
    }

    
}
