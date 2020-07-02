/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

/**
 *
 * @author Diogo Semedo
 */
public class Professor extends Elemento {
    private String categoria;

    /**
     * Construtor da classe Professor.
     * @param categoria Tipo: String - Pode ser catedrático, associado ou auxiliar.
     * @param perfil Tipo: String - Pode ser Boémio,Desportivo,Cultural e Poupadinho. 
     * @param nome Tipo: String - Nome do Elemento.
     * @param id Tipo: Integer - Identificador único de cada elemento.
     * @param password Tipo: String - Serve para controlar o acesso.
     */
    public Professor(String categoria, String perfil, String nome, int id,String password) {
        super(perfil, nome, id,password);
        this.categoria = categoria;
    }

    /**
     * Método de acesso à variável categoria.
     * @return String
     */
    public String getCategoria() {
        return categoria;
    }
    
   @Override
    public String toInformacao() {
        return super.toInformacao()+"Categoria: "+categoria;
    }


    
    
}
