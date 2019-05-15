package logic;

import org.json.JSONException;
import org.json.JSONObject;

public class Join {
    /**
     * Valida si la informacion de un join es correcta
     * @param pJSON Informacion del Join a validar
     * @return true or false
     */
    public static boolean is_correct_join(String pJSON){
        boolean retorno = false;
        AlreadyExistScheme existScheme = new AlreadyExistScheme();
        SchemeColumn schemeColumn = new SchemeColumn();
        try{
            JSONObject json = new JSONObject(pJSON);
            if(!(json.has("scheme") &&
                    json.has("busqueda") &&
                    json.has("dato") &&
                    json.has("location") &&
                    json.has("shared_secret"))){
                retorno = false;
            }else if(!existScheme.exists_scheme(json.get("scheme").toString(),json.get("location").toString())){
                retorno = false;
            }else if(!schemeColumn.scheme_has_column(json.get("scheme").toString(),
                    json.get("location").toString(),
                    json.get("shared_secret").toString(),
                    json.get("busqueda").toString())){
                retorno = false;
            }else if(!schemeColumn.scheme_has_column(json.get("scheme").toString(),
                    json.get("location").toString(),
                    json.get("shared_secret").toString(),
                    json.get("dato").toString())){
                retorno = false;
            }else{
                retorno = true;
            }
        }catch(JSONException e){
            retorno = false;
            //Tools.write_("5");
        }

        return retorno;
    }
}
