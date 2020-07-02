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
public class AreaDesportiva extends Parque{
    private String[] desportos;

    /**
     *
     * @param desportos Tipo: String[] - OS desportos praticados na area despotiva.
     * @param coordenadas Tipo: String - As coordenadas do Local.
     * @param nInscritos Tipo: Integer - Número de inscritos no local, a ser atualizado no decorrer do programa.
     * @param nome Tipo: String - Nome do Local.
     */
    public AreaDesportiva(String[] desportos, String coordenadas, int nInscritos, String nome) {
        super(coordenadas, nInscritos, nome);
        this.desportos = desportos;
    }

    /**
     * Método de acesso à variável desportos.
     * @return String[]
     */
    public String[] getDesportos() {
        return desportos;
    }
    @Override
    public String visualizar(){
        String aux="";
        for(int i=0; i< desportos.length;i++){
            aux+=desportos[i]+" ";
        }
        return super.visualizar() + " Desportos: " + aux+"\n-------------------------------\n";
    }
    
}
