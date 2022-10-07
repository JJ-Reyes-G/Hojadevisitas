package com.secuencia.ordenescosecha;

import java.util.ArrayList;
import java.util.HashMap;

import com.secuencia.database.DatabaseHandler_;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

public class AdapterVisitasFincas {
	public SimpleAdapter adapterListaVisitas(Context ctx, String IDTABLA, String FILTRO)
	{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] {
				"img_estatus",
				"tv_fecha_creacion",
				"tv_codNumVisita",
				"tv_razon",
				"tv_recomendacion",
				"tv_nombreContacto",
				"tv_codProvFinca",
				"tv_zona",
				"tv_llave",
				"tv_correlativo",
				"tv_lotes_pendientes",
				"img_estatus_lotes",
				"tv_lotes_seleccionados",
				};
		int[] to = new int[] {
				R.id.img_estatus,
				R.id.tv_fecha_creacion,
				R.id.tv_codNumVisita,
				R.id.tv_razon,
				R.id.tv_recomendacion,
				R.id.tv_nombreContacto,
				R.id.tv_codProvFinca,
				R.id.tv_zona,
				R.id.tv_llave,
				R.id.tv_correlativo,
				R.id.tv_lotes_pendientes,
				R.id.img_estatus_lotes,
				R.id.tv_lotes_seleccionados,
		};

        db = dbhelper.getReadableDatabase();

		String queryIntro1 = "( SELECT COUNT(*) "
			+ " FROM   "+dbhelper.TABLE_LOTES_SELECT+" AS b"
			+ " WHERE  b."+dbhelper.K_LSEL12_SOLIC_ID+" = a."+dbhelper.KEY_VIS7_LLAVE+" "
			+ " AND  b."+dbhelper.K_LSEL13_LLAVE_ENC+" = '0' "
				+")  ";

		String queryIntro2 = "( SELECT COUNT(*) "
				+ " FROM   "+dbhelper.TABLE_LOTES_SELECT+" AS b"
				+ " WHERE  b."+dbhelper.K_LSEL12_SOLIC_ID+" = a."+dbhelper.KEY_VIS7_LLAVE+" "
				+ " AND  b."+dbhelper.K_LSEL14_DELETE+" = '0' "
				+ " ) ";
        
        Cursor c2 = db.rawQuery("SELECT "
        		+" a."+dbhelper.KEY_VIS19_CODPROV 		+", "
        		+" a."+dbhelper.KEY_VIS20_NOMPROV 		+", "
        		+" a."+dbhelper.KEY_VIS17_CODFINCA 		+", "
        		+" a."+dbhelper.KEY_VIS18_NOMFINCA 		+", "
        		+" a."+dbhelper.KEY_VIS0_ID      		+", "
        		+" a."+dbhelper.KEY_VIS1_RAZON 			+", "
        		+" a."+dbhelper.KEY_VIS2_RECOMENDACION 	+", "
        		+" a."+dbhelper.KEY_VIS3_ZONA 			+", "
        		+" a."+dbhelper.KEY_VIS4_FECHA      	+", "
        		+" a."+dbhelper.KEY_VIS5_NOMCONTACTO 	+", "
        		+" a."+dbhelper.KEY_VIS6_CODAGRONOMO 	+", "
        		+" a."+dbhelper.KEY_VIS7_LLAVE      	+", "
				+" a."+dbhelper.KEY_VIS8_IDLOTE      	+", "
				+" a."+dbhelper.KEY_VIS9_DESCLOTE      	+", "
				+" a."+dbhelper.KEY_VIS11_TIPOVISITA    +", "
				+" a."+dbhelper.KEY_VIS10_ZAFRA    		+", "
				+" a."+dbhelper.KEY_VIS17_CODFINCA    	+", "
				+"   "+queryIntro1+" AS lotesPendientes, "
				+"   "+queryIntro2+" AS lotesSeleccionados "

				+ " FROM "+dbhelper.TABLE_VISITAS+" AS a "

        		+" WHERE" 
        				+ " ( CASE WHEN '"+IDTABLA+"' = '' THEN a."+dbhelper.KEY_VIS0_ID+" = a."+dbhelper.KEY_VIS0_ID+" ELSE a."+dbhelper.KEY_VIS0_ID+" = '"+IDTABLA+"' END )" 

        				+ " AND ( CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_VIS20_NOMPROV+" = a."+dbhelper.KEY_VIS20_NOMPROV+" ELSE a."+dbhelper.KEY_VIS20_NOMPROV+" LIKE '%"+FILTRO+"%' END "
        				+ " OR	  CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_VIS18_NOMFINCA+" = a."+dbhelper.KEY_VIS18_NOMFINCA+" ELSE a."+dbhelper.KEY_VIS18_NOMFINCA+" LIKE '%"+FILTRO+"%' END "
        				+ " OR    CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_VIS1_RAZON+" = a."+dbhelper.KEY_VIS1_RAZON+" ELSE a."+dbhelper.KEY_VIS1_RAZON+" LIKE '%"+FILTRO+"%' END "
        				+ " OR    CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_VIS5_NOMCONTACTO+" = a."+dbhelper.KEY_VIS5_NOMCONTACTO+" ELSE a."+dbhelper.KEY_VIS5_NOMCONTACTO+" LIKE '%"+FILTRO+"%' END "   				
        				+ " OR    CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_VIS2_RECOMENDACION+" = a."+dbhelper.KEY_VIS2_RECOMENDACION+" ELSE a."+dbhelper.KEY_VIS2_RECOMENDACION+" LIKE '%"+FILTRO+"%' END )"
        				
        		+" ORDER BY  a."+dbhelper.KEY_VIS0_ID+" DESC ", null); //a."+dbhelper.KEY_VIS7_LLAVE+",
        int contador = 1;
        if(c2.moveToFirst()){
            do{
	  			HashMap<String, String> map = new HashMap<String, String>();


				map.put("tv_fecha_creacion"		, c2.getString(8));
				map.put("tv_codNumVisita"		, c2.getString(14));
				map.put("tv_razon"				, c2.getString(5));
				map.put("tv_recomendacion"		, c2.getString(6));
				map.put("tv_nombreContacto"     , c2.getString(9));
				map.put("tv_codProvFinca"       , c2.getString(13));//"CODIGO: "+c2.getString(0)+" / "+c2.getString(1));
				map.put("tv_zona"          		, "ZONA"+c2.getString(7));
				map.put("tv_llave"          	, "LLAVE: "+c2.getString(11));
				map.put("tv_correlativo"		, c2.getString(15)+" / ");
				map.put("tv_lotes_pendientes"      , "Cambios Pendientes Lotes: "+c2.getString(17));
				map.put("tv_lotes_seleccionados"   , "Lotes Seleccionados: "+c2.getString(18));


				map.put("visitaId"		,c2.getString(4));
				map.put("CODCLIENTE"  	,c2.getString(0));
				map.put("CODFINCA"   	,c2.getString(16));
				map.put("CODLOTE"   	,c2.getString(12));
				map.put("LLAVE"   		,c2.getString(11));
				map.put("FECHA"   		,c2.getString(8));


				if(c2.getString(17).equals("0")){
					map.put("img_estatus_lotes", ""+R.drawable.open);
				}else{
					map.put("img_estatus_lotes", ""+R.drawable.close);
				}
					//Integer.parseInt(
		  			if(c2.getString(11).equals("0")) // SI HAY MAYOR A CERO
		  			{
						Log.i("El item es", "NO hay 0 :"+c2.getString(11));
						map.put("img_estatus", ""+R.drawable.close);
		      			
		  			}else
		  			{
						Log.i("El item es", "SI hay 0 :"+c2.getString(11));
						map.put("img_estatus", ""+R.drawable.open);
		  			}
	  				//END imagen sincronizado

				mylist.add(map); 
				contador++;
            }while(c2.moveToNext());
        }
		SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.row_lista_visita, from, to);
        c2.close();
        db.close();	
        return adapter;
	}
	
	public String ultimoIdMuestra (Context ctx){
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();
		
		String query = "SELECT * FROM ( SELECT "
				+ "DISTINCT(a."+dbhelper.KEY_APENC1_ID	+"), "
        		+" a."+dbhelper.KEY_APENC2_CODCLIENTE 	+", "
        		+" a."+dbhelper.KEY_APENC3_NOMCLIENTE 	+", "
        		+" a."+dbhelper.KEY_APENC4_CODFINCA 	+", "
        		+" a."+dbhelper.KEY_APENC5_NOMFINCA 	+", "
        		+" a."+dbhelper.KEY_APENC6_CODLOTE 		+", "
        		+" a."+dbhelper.KEY_APENC7_NOMLOTE 		+", "
        		+" a."+dbhelper.KEY_APENC8_FECHA      	+", "
        		+" a."+dbhelper.KEY_APENC10_LLAVE 		+"  "

				+ " FROM   "+dbhelper.TABLE_APLICACIONES_LABORES_ENCABEZADO+" AS a "   					
				+ " ) AS TABLA ORDER BY "+ " "+dbhelper.KEY_APENC1_ID+" DESC LIMIT 1"
				;
		
		Log.i("QUERY", "QUERY: "+query);
	
        Cursor cLabores = db.rawQuery(query, null); 
        
        if(cLabores.moveToFirst()){
            do{	
    			HashMap<String, String> map = new HashMap<String, String>();
    			 return cLabores.getString(0);

            }while(cLabores.moveToNext());
        }
        
        cLabores.close();
        db.close();	
        return "0";

	}
	
	public boolean insertNewVisita( Context ctx, String CODLOTE, String RAZON, String RECOMENDACION, String FECHA, String NOMCONTACTO, String zafra, String tipoVisita, String codTipo, String codMotivo, String codResultado, String quienRecibe, String Hora, VisitaCampo visita){
		try{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();
        db.execSQL("INSERT INTO "+dbhelper.TABLE_VISITAS
        		+" ( "
        		+ dbhelper.KEY_VIS1_RAZON  			+ " ,"
				+ dbhelper.KEY_VIS2_RECOMENDACION	+ " ,"
				+ dbhelper.KEY_VIS3_ZONA 			+ " ,"
				+ dbhelper.KEY_VIS4_FECHA			+ " ,"
				+ dbhelper.KEY_VIS5_NOMCONTACTO		+ " ,"
				+ dbhelper.KEY_VIS6_CODAGRONOMO		+ " ,"
				+ dbhelper.KEY_VIS7_LLAVE			+ " ,"
				+ dbhelper.KEY_VIS8_IDLOTE			+ " ,"
				+ dbhelper.KEY_VIS9_DESCLOTE 		+ " ,"
				+ dbhelper.KEY_VIS10_ZAFRA			+ " ,"
				+ dbhelper.KEY_VIS11_TIPOVISITA 	+ " ,"
				+ dbhelper.KEY_VIS12_CODTIPO 		+ " ,"
				+ dbhelper.KEY_VIS13_CODMOTIVO 		+ " ,"
				+ dbhelper.KEY_VIS14_CODRESULTADO 	+ " ,"
				+ dbhelper.KEY_VIS15_QUIENRECIBE 	+ " ,"
				+ dbhelper.KEY_VIS16_HORAVISITA 	+ " ,"
				+ dbhelper.KEY_VIS17_CODFINCA		+ " ,"

				+ dbhelper.KEY_VIS18_NOMFINCA		+ " ,"
				+ dbhelper.KEY_VIS19_CODPROV		+ " ,"
				+ dbhelper.KEY_VIS20_NOMPROV		+ "  "
				       			
        		+" ) values ( "
        		+ " '"+RAZON						+ "' ,"
				+ " '"+RECOMENDACION				+ "' ,"
        		+ " '"+visita.getNumZona()			+ "' ,"
        		+ " '"+FECHA						+ "' ,"
        		+ " '"+NOMCONTACTO					+ "' ,"
        		+ " "+ ClassConfig.key_codTecnico	+ "  ,"
        		+ " '0',"
        		+ " '"+CODLOTE	        			+ "',"
        		+ " '"+visita.getDescripcionFinca()	+ "', "
				+ " '"+zafra						+ "', "
				+ " '"+tipoVisita					+ "', "
				+ " '"+codTipo						+ "', "
				+ " '"+codMotivo					+ "', "
				+ " '"+codResultado					+ "', "
				+ " '"+quienRecibe					+ "', "
				+ " '"+Hora							+ "', "
				+ " '"+visita.getCodFinca()			+ "', "

				+" '"+visita.getDescripcionFinca()	+ "', "
				+" '"+visita.getCodProveedor()		+ "', "
				+" '"+visita.getNombreProveedor()	+ "'  "
				+")");


        db.close();
        
		}catch (Exception  e){
		      //throw new RuntimeException(e);
			return false;
		}
        return true;
	}
}
