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
public class Bar extends Local {
    private int lotacao;
    private double cMin;
    private int nVip;
    private GuestList guest;
    private int percentagem;

    /**
     * Construtor da classe Bar.
     * @param lotacao Tipo: Integer - A lotacao máxima do Local.
     * @param cMin Tipo: double - O custo mínimo de entrada no Local.
     * @param coordenadas Tipo: String - As coordenadas do Local.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     * @param nome Tipo: String - Nome do Local.
     */
    public Bar(int lotacao, double cMin, String coordenadas, int nInscritos, String nome) {
        super(coordenadas, nInscritos, nome);
        this.lotacao = lotacao;
        this.cMin = cMin;
        this.percentagem = 20;
        this.nVip = lotacao*percentagem/100;
        this.guest = new GuestList();
    }

    /**
     * Método de acesso à variável lotacao.
     * @return Integer
     */
    public int getLotacao() {
        return lotacao;
    }

    /**
     * Método de acesso à variável cMin.
     * @return double
     */
    public double getcMin() {
        return cMin;
    }

    /**
     * Método de acesso à variável nVip.
     * @return Integer
     */
    public int getnVip() {
        return nVip;
    }
    
    /**
     * Método de acesso à variável getGuest.
     * @return GuestList
     */
    public GuestList getGuest() {
        return guest;
    }
    
   
    @Override
    public String visualizar(){
        return super.visualizar() + " Lotação: " + lotacao+"\n-------------------------------\n";
    }
}
