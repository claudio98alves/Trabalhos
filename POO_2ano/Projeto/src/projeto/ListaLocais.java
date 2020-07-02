/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Diogo Semedo, Cláudio Alves
 */
public class ListaLocais implements Serializable {
   private ArrayList<Local> listaLocais = new ArrayList<>();

    /**
     * Construtor vazio da GuestList.
     */
    public ListaLocais() {
    }

    /**
     * Método de acesso à listalocais.
     * @return ArrayList Local
     */
    public ArrayList<Local> getListaLocais() {
        return listaLocais;
    }

    /**
     * Método para carregar do ficheiro de texto para a ArrayList listalocais a informação dos locais quando não há ficheiros de objectos.
     * @return boolean
     * @throws IOException
     */
    public boolean carregarFicheiroTexto() throws IOException{
        Ficheiros fichElem = new Ficheiros("locaisVisitar.txt");
        Ficheiros fichVer = new Ficheiros("locais.txt");
        if(!fichVer.verificaFicheiro()){
            if(fichElem.verificaFicheiro()){
                if(fichElem.abreLeituraTexto()){
                    String line=null;
                    line = fichElem.leLinha();
                    while(line!=null){
                        String[] arrayLine = line.split("/");
                        if(arrayLine[0].equals("Bar")){
                            Bar n= new Bar(Integer.parseInt(arrayLine[1]),Integer.parseInt(arrayLine[2]),arrayLine[3],Integer.parseInt(arrayLine[4]),arrayLine[5]);
                            this.listaLocais.add(n);
                        }
                        else if(arrayLine[0].equals("Jardim")){
                            Jardim n = new Jardim(Integer.parseInt(arrayLine[1]),arrayLine[2],Integer.parseInt(arrayLine[3]),arrayLine[4]);
                            this.listaLocais.add(n);
                        }
                        else if(arrayLine[0].equals("AreaDesportiva")){
                            String[] d = arrayLine[1].split(" ");
                            AreaDesportiva n = new AreaDesportiva(d,arrayLine[2],Integer.parseInt(arrayLine[3]),arrayLine[4]);
                            this.listaLocais.add(n);
                        }
                        else if(arrayLine[0].equals("Exposicao")){
                            Exposicao n = new Exposicao(arrayLine[1],Double.parseDouble(arrayLine[2]),arrayLine[3],Integer.parseInt(arrayLine[4]),arrayLine[5]);
                            this.listaLocais.add(n);
                        }
                        line=fichElem.leLinha();
                    }
                    fichElem.fechaFicheiroTextoL();
                }
                else{
                    System.out.println("Erro na abertura para leitura!!!");
                    System.exit(0);
                }
            }
            else{
              System.out.println("O ficheiro não existe!!!");
              System.exit(0);
          }
        return false;
        }
        return false;
    }
    
    /**
     * Método para carregar a ArrayList listalocais a informação dos locais.
     * @return boolean
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean carregarLista() throws IOException, ClassNotFoundException{
      Ficheiros fichElem = new Ficheiros("locais.txt");
      if(fichElem.verificaFicheiro()){
          if(fichElem.abreLeitura()){
              listaLocais=(ArrayList<Local>)fichElem.carregarObjecto();
              fichElem.fechaFicheiroL();
          }
          else{
              System.out.println("Erro na abertura para leitura!!!");
              System.exit(0);
          }
      }
      else{
          System.out.println("O ficheiro não existe!!!");
          
      }
    return false;
    }
    
    /**
     * Método para escrever a ArrayList listalocais nos ficheiros de objectos.
     * @return boolean
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean meteLista() throws IOException, ClassNotFoundException{
      Ficheiros fichElem = new Ficheiros("locais.txt");
     
          if(fichElem.abreEscrita()){
              fichElem.escreveObjecto(listaLocais);
              fichElem.fechaFicheiroE();
          }
          else{
              System.out.println("Erro na abertura para leitura!!!");
              System.exit(0);
          }
      
      
    return false;
    }
    
    /**
     * Método para ordenar a ArrayList listalocais pelo nInscritos.
     */
    public void ordenar(){
        Collections.sort(listaLocais);
    }
    
    /**
     * Método para calcular o número total de inscrições em todos os locais.
     * @return Integer
     */
    public int totalInscritos(){
        int conta=0;
        for(int i=0;i<listaLocais.size();i++){
            conta+=listaLocais.get(i).getnInscritos();
        }
        return conta;
    }
 
    
}
