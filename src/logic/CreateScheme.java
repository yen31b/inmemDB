package logic;


import org.json.JSONArray;
import org.json.JSONObject;
import structures.lists.ListaSimple;
import logic.Tools;
import logic.Join;

public class CreateScheme {

    /**
     * Crear un nuevo esquema
     * @param pJSON informacion recibida del usuario en formato JSON
     * @return Estado despues de crear (creado o los errores posibles)
     */
    public JSONObject CreateScheme(JSONObject pJSON){
        JSONObject retorno = new JSONObject();
        String name = pJSON.get("name").toString();
        String location = pJSON.get("location").toString();
        JSONArray columns = new JSONArray(pJSON.get("columns").toString());
        AlreadyExistScheme existScheme = new AlreadyExistScheme();
        ExistInScheme existInScheme = new ExistInScheme();
        DBEngine dbEngine = new DBEngine();
        //Join correctJoin = new Join();

        if(name.replace(" ", "").equals("")){
            retorno.put("status", "error");
            retorno.put("code", "The name can not be empty");
        }
        else if(location.replace(" ", "").equals("")){
            retorno.put("status", "error");
            retorno.put("code", "The location can not be empty");
        }
        else if(existScheme.exists_scheme(name,location)){
            retorno.put("status", "error");
            retorno.put("code", "The scheme already exists in the location given");
        }
        else if(columns.length()==0){
            retorno.put("status", "error");
            retorno.put("code", "The scheme should have at least one column");
        }else{
            boolean state = true;
            ListaSimple tmp = new ListaSimple();
            ListaSimple tmp2 = new ListaSimple();
            ListaSimple columns_names = new ListaSimple();
            for(int i = 0;i<columns.length();i++){
                JSONObject tmp3 = (JSONObject) columns.get(i);
                ListaSimple tmp4 = new ListaSimple();
                String column_name = tmp3.get("name").toString();
                String column_type = tmp3.get("type").toString();
                String column_length = tmp3.get("length").toString();
                if(column_name.replace(" ","").equals("")){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The name of any column can not be empty");
                }else if(existInScheme.exists_in_list(column_name,columns_names)){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "Can not be two or more columns with the same name");
                }else if(column_type.replace(" ","").equals("")){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The type's name of any column can not be empty");
                }else if(!existInScheme.exists_in_list(column_type.toLowerCase(), dbEngine.get_tipos_de_datos())){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The type of any column can not be "+column_type);
                }else if(column_type.equals("join") && dbEngine.get_schemes().get_size()==0){
                    state=false;
                    retorno.put("status", "error");//OK
                    retorno.put("code", "There is not another schemes to join");
                }else if(column_type.equals("join") && !tmp3.has("join")){///
                    state=false;
                    retorno.put("status", "error");//OK
                    retorno.put("code", "No exists information for make a join");
                }else if(tmp3.has("join") && !Join.is_correct_join(tmp3.get("join").toString())){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The join information given is not correct");
                }else if(!Tools.isInteger(column_length)){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The column's length must be a number");
                }else if(Integer.parseInt(column_length)<=0){
                    state=false;
                    retorno.put("status", "error");
                    retorno.put("code", "The column's length must be higher than 0");
                }else{
                    tmp4.insertar(column_name);//[0] Nombre de la Columna
                    tmp4.insertar(column_type.toLowerCase()); //[1] Tipo de Columna
                    tmp4.insertar(column_length); //[2] Largo de los datos de la columna
                    if(tmp3.has("join")){
                        ListaSimple tmp5 = new ListaSimple();
                        JSONObject tmp6 = new JSONObject(tmp3.get("join").toString());
                        tmp5.insertar(tmp6.get("scheme").toString());
                        tmp5.insertar(tmp6.get("busqueda").toString());
                        tmp5.insertar(tmp6.get("dato").toString());
                        tmp4.insertar(tmp5);
                    }
                    tmp2.insertar(tmp4);
                    columns_names.insertar(column_name);
                }
                if(!state){
                    break;
                }
            }
            if(state){
                tmp.insertar(name);//[0]
                tmp.insertar(location);//[1]
                //tmp.insertar(shared_secret);//[2]
                tmp.insertar(tmp2);//Columnas[3]
                tmp.insertar(new ListaSimple());//Índices[4]
                tmp.insertar(new ListaSimple());//Datos[5]
                dbEngine.get_schemes().insertar(tmp);
                retorno.put("status", "done");
                Tools.beep();
            }
        }
        return retorno;
    }
}
