/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Semedo, Cláudio Alves
 */
public class Ficheiros {
    private ObjectInputStream iS;
    private ObjectOutputStream oS;
    private File ficheiro;
    private BufferedReader fR;

    /**
     * Construtor para a classe Ficheiros.
     * @param nomeDoFicheiro Tipo : Strin - Nome do ficheiro que se quer usar.
     */
    public Ficheiros(String nomeDoFicheiro) {
        this.ficheiro = new File(nomeDoFicheiro);
    }
    
    /**
     * Método para verificar se o ficheiro existe.
     * @return boolean
     */
    public boolean verificaFicheiro(){
        return ficheiro.exists();
    }

    /**
     * Método de abertura para leitura do ficheiro de Texto.
     * @return boolean
     */
    public boolean abreLeituraTexto(){
        boolean test = true;
        try{
            fR = new BufferedReader(new FileReader(ficheiro));
        } catch (IOException e){
            test=false;
        }
        return test;
    }
    
    /**
     * Método de abertura para leitura do ficheiro de Objectos. 
     * @return boolean
     */
    public boolean abreLeitura() {
        boolean test=true;
        try{
            iS = new ObjectInputStream(new FileInputStream(ficheiro));
        } catch (IOException e){
           test=false;
        }
        return test;
    }
    
    /**
     *  Método de abertura para escrita do ficheiro de Objectos.
     * @return boolean
     */
    public boolean abreEscrita() {
        boolean test=true;
        try{
            oS = new ObjectOutputStream(new FileOutputStream(ficheiro));
        } catch (IOException e){
            test=false;
        }
        return test;
    }
    
    /**
     * Método para leitura de uma linha do ficheiro de Texto.
     * @return String
     * @throws IOException
     */
    public String leLinha() throws IOException{
        return fR.readLine();
    }
    
    /**
     * Método utilizado ler Objectos do ficheiro de Objectos.
     * @return Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object carregarObjecto() throws IOException, ClassNotFoundException{
        return iS.readObject();
        
    }

    /**
     * Método para escrita nos ficheiros de Objectos.
     * @param o Tipo: Object - Objecto a ser escrito no ficheiro de Objectos.
     * @throws IOException
     */
    public void escreveObjecto(Object o) throws IOException{
        oS.writeObject(o);
    }

    /**
     * Método para fechar o ficheiro de Texto para leitura.
     * @throws IOException
     */
    public void fechaFicheiroTextoL() throws IOException{
        fR.close();
    }
    
    /**
     *  Método para fechar o ficheiro de Objectos para leitura.
     */
    public void fechaFicheiroL(){
        try {
            iS.close();
        } catch (IOException ex) {
            Logger.getLogger(Ficheiros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método para fechar o ficheiro de Texto para escrita.
     */
    public void fechaFicheiroE(){
        try {
            oS.close();
        } catch (IOException ex) {
            Logger.getLogger(Ficheiros.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
      
}
