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
public class Jardim extends Parque {
    private double area;

    /**
     * Construtor da classe Jardim.
     * @param area Tipo: double - A area em m2 (metros quadrados) do jardim.
     * @param coordenadas Tipo: String - As coordenadas do Local.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     * @param nome Tipo: String - Nome do Local.
     */
    public Jardim(double area, String coordenadas, int nInscritos, String nome) {
        super(coordenadas, nInscritos, nome);
        this.area = area;
    }
    
    /**
     * Método de acesso à variável area.
     * @return double
     */
    public double getArea() {
        return area;
    }
    
    @Override
    public String visualizar(){
        return super.visualizar() + " Area: " + (int)area +" m2"+"\n-------------------------------\n";
    }

}
