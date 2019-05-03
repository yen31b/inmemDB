package logic;

import structures.lists.ListaSimple;

public class AlreadyExistScheme extends DBEngine{

    public AlreadyExistScheme() {
        super();
    }

    /**
     * Verifica si ya existe un esquema en la base de datos
     * @param pName Nombre del esquema
     * @return boolean (si existe o no)
     */
    public boolean exists_scheme(String pName, String pLocation){
        boolean retorno = false;
        for(int i = 0;i<get_schemes().get_size();i++){
            ListaSimple scheme = (ListaSimple) get_schemes().get(i);
            if(scheme!=null){
                if(scheme.get(0).equals(pName) && scheme.get(1).equals(pLocation)){
                    retorno=true;
                    break;
                }
            }
        }
        return retorno;
    }

}
