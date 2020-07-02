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
public class Funcionario extends Elemento {
    private String dedicacao;

    /**
     * Construto da classe Funcionario.
     * @param dedicacao Tipo: String - Pode ser parcial ou integral.
     * @param perfil Tipo: String - Pode ser Boémio,Desportivo,Cultural e Poupadinho. 
     * @param nome Tipo: String - Nome do Elemento.
     * @param id Tipo: Integer - Identificador único de cada elemento.
     * @param password Tipo: String - Serve para controlar o acesso.
     */
    public Funcionario(String dedicacao, String perfil, String nome, int id,String password) {
        super(perfil, nome, id,password);
        this.dedicacao = dedicacao;
    }

    /**
     * Método de acesso à variável decicacao.
     * @return String
     */
    public String getDedicacao() {
        return dedicacao;
    }
   @Override
    public String toInformacao() {
        return super.toInformacao()+"Dedicação: "+dedicacao;
    }
    
    
}
