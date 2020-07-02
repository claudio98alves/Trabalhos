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
public class Parque extends Local {

    /**
     * Construtor da classe Parque.
     * @param coordenadas Tipo: String - As coordenadas do Local.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     * @param nome Tipo: String - Nome do Local.
     */
    public Parque(String coordenadas, int nInscritos, String nome) {
        super(coordenadas, nInscritos, nome);
    }
}
