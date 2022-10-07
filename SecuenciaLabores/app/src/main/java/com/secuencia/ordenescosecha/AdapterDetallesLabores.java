package com.secuencia.ordenescosecha;

import java.util.ArrayList;
import java.util.HashMap;

import com.secuencia.database.DatabaseHandler_;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

public class AdapterDetallesLabores {
	
	public SimpleAdapter adaptadorListaDetalles(Context ctx,String IDENCABEZADO)
	{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] {
				"tv_numeroCorrelativo",
				"tv_codnomproducto", 
				"tv_codnom_tipolabor",
				"tv_unidadmedida",
				"tv_cantidad",
				"tv_fecha_aplicacion",
				"tv_tipo_aplicacion",
				"tv_llave",
				"img_candado",
				};
		int[] to = new int[] {
				R.id.tv_numeroCorrelativo, 
				R.id.tv_codnom_producto,
				R.id.tv_codnom_tipolabor, 
				R.id.tv_unidadmedida, 
				R.id.tv_cantidad,
				R.id.tv_fecha_aplicacion, 
				R.id.tv_tipo_aplicacion,
				R.id.tv_llave,
				R.id.img_candado,
				};

        db = dbhelper.getReadableDatabase();
        String query = "SELECT "
        		+dbhelper.KEY_APDET1_ID 		+", "
        		+dbhelper.KEY_APDET2_IDECABEZADO +", "
        		+dbhelper.KEY_APDET3_TPO_APLICACION+", "
        		+dbhelper.KEY_APDET4_DOSIS +", "
        		+dbhelper.KEY_APDET5_CODPROCUCTO 	+", "
        		+dbhelper.KEY_APDET6_NOMPROCUCTO 	+", "
        		+dbhelper.KEY_APDET7_TPO_CONTROL 	+", "
        		+dbhelper.KEY_APDET8_FECHACONTROL 	+", "
        		+dbhelper.KEY_APDET9_PRESENTACION   +", "
        		+dbhelper.KEY_APDET11_LLAVE_DET     +"  "

        		+" FROM "+dbhelper.TABLE_APLICACIONES_LABORES_DETALLES+" WHERE "+dbhelper.KEY_APDET2_IDECABEZADO+" = "+IDENCABEZADO;
        
        Log.i("miquery", "MILLAVE: "+query);

        Cursor c2 = db.rawQuery(query, null);//+" ORDER BY "+dbhelper.KEY_APDET1_ID+" ", null);//, null);//
        int contador = 1;
        if(c2.moveToFirst()){
            do{
	  			HashMap<String, String> map = new HashMap<String, String>();	

				map.put("tv_numeroCorrelativo"  , contador+" -");
				map.put("tv_codnomproducto"	    , "PROD: "+c2.getString(4) +" - "+c2.getString(5));
				map.put("tv_codnom_tipolabor"	, c2.getString(6));
				map.put("tv_unidadmedida"		, c2.getString(8));
				map.put("tv_cantidad"           , c2.getString(3));
				map.put("tv_fecha_aplicacion"   , c2.getString(7));
				map.put("tv_llave"              , "LLAVE: "+c2.getString(9));
				
				map.put("tv_tipo_aplicacion"    , getArrayTipoAplicacionNombre(c2.getString(2)).toString());
				
	  			if(Integer.parseInt(c2.getString(9)) > 0 ) // SI HAY MAYOR A CERO 
	  			{
	                Log.i("El item es:", "SI hay 0");
	      			map.put("img_candado", ""+R.drawable.open);
	      			
	  			}else
	  			{
	                Log.i("El item es:", "NO hay 0");
	      			map.put("img_candado", ""+R.drawable.close);
	  			}
				
				
				map.put("IDDETALLE",c2.getString(0));
				map.put("LLAVE_DET",c2.getString(9));
				
				mylist.add(map); 
				contador++;
            }while(c2.moveToNext());
        }
		SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.rows_lista_detalles_labores, from, to);
        c2.close();
        db.close();	
        return adapter;
	}
	
	
	public String getArrayTipoAplicacionNombre(String idTipoAplicacion){
		String [][]opcionesid  = null;
		
		   opcionesid	=	new String[][]{
				   {"1","Mecanica"},{"2","Manual"},
				   {"4","Quimica"},{"5","Manual"},
				   {"6","Falso Medidor"},{"7","Diatraea"},{"8","Rata"},{"9","Aenolamia"},{"10","Termita"},{"12","fhyllophaga sp"},{"13","Chinche de encaje"}
		   			}; 
		   
		   for(int i= 0 ; i < opcionesid.length ; i++ ){
			   if(opcionesid[i][0].equals(idTipoAplicacion) ){
				   return opcionesid[i][1];
			   }
		   }
		   String nombre = "/";//DESCONOCIDO
        return nombre;
        
	}
	
	public SimpleAdapter adaptadorListaProductos(Context ctx,String codigoLabor)
	{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] {
				"tv_numeroCorrelativo",
				"tv_codnom_producto", 
				"tv_codnom_tipolabor",
				"tv_unidadmedida",
				"tv_dosis_desde",
				"tv_dosis_hasta",
				};
		int[] to = new int[] {
				R.id.tv_numeroCorrelativo, 
				R.id.tv_codnom_producto,
				R.id.tv_codnom_tipolabor, 
				R.id.tv_unidadmedida, 
				R.id.tv_dosis_desde,
				R.id.tv_dosis_hasta, 
				};


        Cursor c2 = db.rawQuery("SELECT *"
        		+ " FROM "+dbhelper.TABLE_PRODUCTOS_LABORES+" WHERE "+dbhelper.KEY_PROD4_TPO_CONTROL+" = "+codigoLabor, null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
	  			HashMap<String, String> map = new HashMap<String, String>();	

				map.put("tv_numeroCorrelativo"	, contador+" -");
				map.put("tv_codnom_producto"	,  "PROD: "+c2.getString(1) +" - "+c2.getString(2));
				map.put("tv_codnom_tipolabor"	,  c2.getString(7));
				map.put("tv_unidadmedida"		,  c2.getString(4));
				map.put("tv_dosis_desde"        , "DESDE: "+c2.getString(5));
				map.put("tv_dosis_hasta"        , "HASTA: "+c2.getString(6));
				
				
				map.put("DOSIS_DESDE"        	, c2.getString(5));
				map.put("DOSIS_HASTA"        	, c2.getString(6));
				map.put("IDPRODUCTO"			, c2.getString(0));
				
				mylist.add(map); 
				contador++;
            }while(c2.moveToNext());
        }
		SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.rows_lista_productos, from, to);
        c2.close();
        db.close();	
        return adapter;
	}
}
