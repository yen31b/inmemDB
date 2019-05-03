package logic;

import structures.lists.ListaSimple;

public class ExistInScheme {
    /**
     * Verifica si un texto está en una lista
     * @param pKey Valor a buscar
     * @param pList Lista en donde buscar
     * @return True or False
     */
    public boolean exists_in_list(String pKey, ListaSimple pList){
        boolean retorno = false;
        for(int i=0;i<pList.get_size();i++){
            String tmp = (String) pList.get(i);
            if(tmp.equals(pKey)){
                retorno = true;
                break;
            }
        }
        return retorno;
    }
}
