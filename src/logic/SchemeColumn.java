package logic;

import structures.lists.ListaSimple;

public class SchemeColumn {
    /**
     * Verifica si existe una columna en un esquema
     * @param pScheme Esquema a buscar
     * @param pShared_secret Shared Secret del esquema a donde se realizará la bússqueda
     * @param pColumn Columna que se desea buscar
     * @return Si se encontró la columna ó no
     */
    public boolean scheme_has_column(String pScheme,String pLocation,String pShared_secret,String pColumn){
        boolean retorno =false;
        DBEngine dbEngine = new DBEngine();

        for(int i = 0;i<dbEngine.get_schemes().get_size();i++){
            ListaSimple scheme = (ListaSimple) dbEngine.get_schemes().get(i);
            if(scheme!=null){
                if(scheme.get(0).equals(pScheme)&& scheme.get(1).equals(pLocation) && scheme.get(2).equals(pShared_secret)){
                    ListaSimple columnas = (ListaSimple) scheme.get(3);
                    for(int j=0;j<columnas.get_size();j++){
                        ListaSimple columna = (ListaSimple) columnas.get(j);
                        if(columna.get(0).equals(pColumn)){
                            retorno = true;
                            break;
                        }
                    }
                }
            }
        }
        return retorno;
    }

}
