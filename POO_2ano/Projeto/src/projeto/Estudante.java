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
public class Estudante extends Elemento {
    private String curso;

    /**
     * Construtor da classe Estudante
     * @param curso Tipo: String - Nome do curso.
     * @param perfil Tipo: String - Pode ser Boémio,Desportivo,Cultural e Poupadinho. 
     * @param nome Tipo: String - Nome do Elemento.
     * @param id Tipo: Integer - Identificador único de cada elemento.
     * @param password Tipo: String - Serve para controlar o acesso.
     */
    public Estudante(String curso, String perfil, String nome, int id,String password) {
        super(perfil, nome, id,password);
        this.curso = curso;
    }

    /**
     * Método de acesso à variavél curso.
     * @return String
     */
    public String getCurso() {
        return curso;
    }
    
    @Override
    public String toString() {
        return super.toString()+curso;
    }
    
    @Override
    public String toInformacao() {
        return super.toInformacao()+"Curso: "+curso;
    }
}
