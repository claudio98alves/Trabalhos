/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;
import java.io.Serializable;
import java.util.*;
/**
 *
 * @author Diogo Semedo, Cláudio Alves
 */
public class GuestList implements Serializable {
    private ArrayList<Elemento> guestList = new ArrayList<>();
    
    /**
     * Construtor vazio da GuestList.
     */
    public GuestList() {
    }

    /**
     * Método de acesso à guestList.
     * @return ArrayList Elemento
     */
    public ArrayList<Elemento> getGuestList() {
        return guestList;
    }
    
    /**
     * Método para adicionar elementos à guestList (variável do Bar).
     * Se a guestList estiver cheia e o elemento que estamos a tentar adicionar tem o perfil de Boemio, 
     * a guest é percorrida do fim para o ínicio, de forma a encontrar o 
     * último elemnto cujo o perfil seja diferente de Boemio, de forma a susbtituir.
     * Retorna 1 se o elemento foi adicionada à guest com sucesso. 
     * Retorna 0 se não for possivel adicionar o elemento à guest.
     * Retorna 2 se o elemento que foi adicionado à guestList substitui outro elemento removendo este último. 
     * @param elemento Tipo: Elemento - Elemento da lista de Inscritos que vai ser adicionado à guesList do Bar.
     * @param bar Tipo: Bar - O Bar ao qual o elemento vai ser adicionada à guestList.
     * @param lista Tipo: ListaElementos - Classe com lista dos elementos da comunidade e com a lista de inscritos.
     * @return Integer
     */
    public int adicionaGuest(Elemento elemento,Bar bar,ListaElementos lista){
        
        if(guestList.size()<bar.getnVip()){
            guestList.add(elemento);
            return 1;
        }
        else{
            if(elemento.getPerfil().equals("Boemio")){
                for(int i=guestList.size()-1;i>=0;i--){
                    if(!(guestList.get(i).getPerfil().equals("Boemio"))){
                        for(int j=0;j<lista.getListaInscritos().size();j++){
                            if(lista.getListaInscritos().get(j).getId()==guestList.get(i).getId()){
                                lista.getListaInscritos().get(j).removeLocal(bar);
                                
                            }
                        }
                        guestList.remove(i);
                        guestList.add(elemento);
                        return 2;
                    }
                }
            }
        }
        return 0;
    }
}
