/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;
import java.io.*;
import java.util.*;
/**
 *
 * @author Diogo Semedo, Cláudio Alves
 */
public class ListaElementos implements Serializable {
    private ArrayList<Elemento> listaElementos = new ArrayList<>();
    private ArrayList<Elemento> listaInscritos = new ArrayList<>();
    private double receita=0;

    /**
     * Construtor vazio da ListaElementos.
     */
    public ListaElementos() {
    }

    /**
     * Método de acesso à listaElementos.
     * @return ArrayList Elemento
     */
    public ArrayList<Elemento> getListaElementos() {
        return listaElementos;
    }

    /**
     * Método de acesso à listaInscritos.
     * @return ArrayList Elemento
     */
    public ArrayList<Elemento> getListaInscritos() {
        return listaInscritos;
    }

    /**
     * Método de acesso à receita.
     * @return Integer
     */
    public double getReceita() {
        return receita;
    }

    /**
     * Método para mudar o valor da variável receita.
     * @param receita Tipo : double - O valor da receita, soma do custo dos locais em que os elementos da listaInscritos estão inscritos.
     */
    public void setReceita(double receita) {
        this.receita = receita;
    }
    
    /**
     * Método para carregar do ficheiro de texto para a ArrayList listaElementos e lista Inscritos a informação dos Elementos quando não há ficheiros de objectos.
     * @return boolean
     * @throws IOException
     */
    public boolean carregarFicheiroTexto() throws IOException{
        Ficheiros fichElem = new Ficheiros("comunidadeDei.txt");
        Ficheiros fichVer = new Ficheiros("elementos.txt");
        if(!fichVer.verificaFicheiro()){
            if(fichElem.verificaFicheiro()){
                if(fichElem.abreLeituraTexto()){
                    String line=null;
                    line = fichElem.leLinha();
                    while(line!=null){
                        String[] arrayLine = line.split("-");
                        if(arrayLine[0].equals("Estudante")){
                            Estudante n= new Estudante(arrayLine[1],arrayLine[2],arrayLine[3],Integer.parseInt(arrayLine[4]),arrayLine[5]);
                            this.listaElementos.add(n);
                        }
                        else if(arrayLine[0].equals("Professor")){
                            Professor n = new Professor(arrayLine[1],arrayLine[2],arrayLine[3],Integer.parseInt(arrayLine[4]),arrayLine[5]);
                            this.listaElementos.add(n);
                        }
                        else if(arrayLine[0].equals("Funcionario")){
                            Funcionario n = new Funcionario(arrayLine[1],arrayLine[2],arrayLine[3],Integer.parseInt(arrayLine[4]),arrayLine[5]);
                            this.listaElementos.add(n);
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
     * Método para carregar a ArrayList listaInscritos a informação dos Elementos inscritos.
     * @return boolean
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean carregarListaInscritos() throws IOException, ClassNotFoundException{
      Ficheiros fichElem = new Ficheiros("inscritos.txt");
      if(fichElem.verificaFicheiro()){
          if(fichElem.abreLeitura()){
              listaInscritos=(ArrayList<Elemento>)fichElem.carregarObjecto();
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
     * Método para carregar a ArrayList listaElementos a informação dos Elementos.
     * @return boolean
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean carregarLista() throws IOException, ClassNotFoundException{
      Ficheiros fichElem = new Ficheiros("elementos.txt");
      if(fichElem.verificaFicheiro()){
          if(fichElem.abreLeitura()){
              listaElementos=(ArrayList<Elemento>)fichElem.carregarObjecto();
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
     * Método para escrever a ArrayList listaElementos nos ficheiros de objectos.
     * @return boolean
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean meteLista() throws IOException, ClassNotFoundException{
        Ficheiros fichElem = new Ficheiros("elementos.txt");
        if(fichElem.abreEscrita()){
            fichElem.escreveObjecto(listaElementos);
            fichElem.fechaFicheiroE();
        }
        else{
            System.out.println("Erro na abertura para leitura!!!");
            System.exit(0);
        }
        
        return false;
    }
    
    /**
     * Método para escrever a ArrayList listaInscritos nos ficheiros de objectos.
     * @return boolean
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean meteListaInscritos() throws IOException, ClassNotFoundException{
        Ficheiros fichElem = new Ficheiros("inscritos.txt");
        if(fichElem.abreEscrita()){
            fichElem.escreveObjecto(listaInscritos);
            fichElem.fechaFicheiroE();
        }
        else{
            System.out.println("Erro na abertura para leitura!!!");
            System.exit(0);
        }
        
        return false;
    }

    /**
     * Método para adicionar locais á lista de locais de um elemento da listaInscritos.
     * Retorna 0 quando não é possivel inscrever.
     * Retorna 1 quando a inscrição é bem sucedida.
     * Retorna 2 quando o Bar tem a guestList cheia.
     * @param local Tipo : String - O nome do local que se pretende adicionar à lsita de locais do elemento.
     * @param indice Tipo: Integer - O indice correspondente ao Elemento na listaElementos.
     * @param dataBaseLocais Tipo: ListaLocais - A ArrayList com todos os loca
     * @return Integer
     */
    public int alterarLocal(String local,int indice,ListaLocais dataBaseLocais){
        for(int i=0;i<dataBaseLocais.getListaLocais().size();i++){
            if(dataBaseLocais.getListaLocais().get(i).getNome().equals(local)){
                if(listaInscritos.get(indice).addLocal(dataBaseLocais.getListaLocais().get(i))){
                    dataBaseLocais.getListaLocais().get(i).incrementa();
                    if(dataBaseLocais.getListaLocais().get(i) instanceof Bar){
                                //tratamento das guests
                        int aux=(((Bar) dataBaseLocais.getListaLocais().get(i))).getGuest().adicionaGuest(listaInscritos.get(listaInscritos.size()-1),((Bar) dataBaseLocais.getListaLocais().get(i)),this);
                        if(aux==1){
                             receita= receita+((Bar) dataBaseLocais.getListaLocais().get(i)).getcMin();

                        }
                        //quando é substituido na guest list
                        else if(aux==2){
                            dataBaseLocais.getListaLocais().get(i).decrementa();
                        }
                        // a inscriçao no bar falhou
                        else{
                            listaInscritos.get(listaInscritos.size()-1).removeLocal(dataBaseLocais.getListaLocais().get(i));
                            dataBaseLocais.getListaLocais().get(i).decrementa();
                            return 2;

                        }    
                    }
                    else if(dataBaseLocais.getListaLocais().get(i) instanceof Exposicao){
                        if(listaInscritos.get(indice) instanceof Estudante){
                            receita=receita+(0.9*((Exposicao) dataBaseLocais.getListaLocais().get(i)).getCusto());
                        }
                        else{
                            receita=receita+((Exposicao) dataBaseLocais.getListaLocais().get(i)).getCusto();
                        }
                    }
                }
                else{
                    return 0;
                }
            }
        }
        return 1;
    }   

    /**
     * Método usado para inscrever, ou seja copiar o elemento da listaElementos para a listaInscritos adicionando os locais com os nomes
     * que são recebidos através da String locais.
     * Retorna -1 quando o elemento já está inscrito. Retorna 1 quando é inscrito com sucesso.
     * @param locais Tipo: String[] - O vetor de Strings que são recolhidas das jComboBox, contendo locais válidos ou "Nenhum local.".
     * @param indice Tipo: Integer - O indice correspondente ao Elemento na listaElementos.
     * @param dataBaseLocais Tipo: ListaLocais - A ArrayList com todos os locais.
     * @return Integer
     */
    public int inscrever(String[] locais,int indice,ListaLocais dataBaseLocais){
        //So nos permite inscrever uma vez cada elemento
        for(int i=0;i<listaInscritos.size();i++){
            if(listaInscritos.get(i).getId()==listaElementos.get(indice).getId()){
                return -1;
            }
        }
        listaInscritos.add(listaElementos.get(indice));
        for(int i=0;i<locais.length;i++){
            //Seleciona um local válido
            if(!(locais[i].equals("Nenhum local."))){
                //obtém objeto local correspondente ao nome
                for(int n=0;n<dataBaseLocais.getListaLocais().size();n++){
                    //encontra o objecto Local correspondente ao nome da String[] locais
                    if(dataBaseLocais.getListaLocais().get(n).getNome().equals(locais[i])){
                        //tenta adicionar o local á lista de locais do ultimo elemento na lista de inscritos
                        if(listaInscritos.get(listaInscritos.size()-1).addLocal(dataBaseLocais.getListaLocais().get(n))){
                            dataBaseLocais.getListaLocais().get(n).incrementa();
                            //verifica se o local é da classe Bar
                            if(dataBaseLocais.getListaLocais().get(n) instanceof Bar){
                                //tratamento das guests
                               int aux=(((Bar) dataBaseLocais.getListaLocais().get(n))).getGuest().adicionaGuest(listaInscritos.get(listaInscritos.size()-1),((Bar) dataBaseLocais.getListaLocais().get(n)),this);
                               if(aux==1){
                                    receita= receita+((Bar) dataBaseLocais.getListaLocais().get(n)).getcMin();
                                    
                               }
                               //quando é substituido na guest list
                               else if(aux==2){
                                   dataBaseLocais.getListaLocais().get(n).decrementa();
                               }
                               // a inscriçao no bar falhou
                               else{
                                   listaInscritos.get(listaInscritos.size()-1).removeLocal(dataBaseLocais.getListaLocais().get(n));
                                   dataBaseLocais.getListaLocais().get(n).decrementa();
                                   
                               }    
                            }
                            //verifica se o local é da classe Exposicao
                            else if(dataBaseLocais.getListaLocais().get(n) instanceof Exposicao){
                                if(listaInscritos.get(listaInscritos.size()-1) instanceof Estudante){
                                    receita=receita+(0.9*((Exposicao) dataBaseLocais.getListaLocais().get(n)).getCusto());
                                }
                                else{
                                    receita=receita+((Exposicao) dataBaseLocais.getListaLocais().get(n)).getCusto();
                                }
                            }
                        }
                    }
                }    
            }        
        }
        //se este if retornar 0 é porque o elemento se tentou inscrever num ou mais bares sem sucesso
        if(listaInscritos.get(listaInscritos.size()-1).getLocais().size()==0){
            return 0;
        }
        // -1 já foi inscrito 1 inscreveu e adicionou local 0 quando a inscrição não foi possivel
        return 1;
    }
    
    /**
     * Método para quando o programa é iniciado calcular a receita verificando todos os locais em que cada Elemento está inscrito,
     */
    public void calcularReceita(){
        Ficheiros fichElem = new Ficheiros("inscritos.txt");
        if(fichElem.verificaFicheiro()){
            for(int i=0;i<listaInscritos.size();i++){
                for(int j=0;j<listaInscritos.get(i).getLocais().size();j++){
                    if(listaInscritos.get(i).getLocais().get(j) instanceof Bar){
                        receita= receita+((Bar) listaInscritos.get(i).getLocais().get(j)).getcMin();
                    }
                    else if(listaInscritos.get(i).getLocais().get(j) instanceof Exposicao){
                        if(listaInscritos.get(i) instanceof Estudante){
                            receita=receita+(0.9*((Exposicao) listaInscritos.get(i).getLocais().get(j)).getCusto());
                        }
                        else{
                            receita=receita+((Exposicao) listaInscritos.get(i).getLocais().get(j)).getCusto();
                        }
                    }
                }
            
            }
        
        }
        
    }
    
    
    
}