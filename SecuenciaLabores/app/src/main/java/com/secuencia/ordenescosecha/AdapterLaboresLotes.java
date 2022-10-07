package com.secuencia.ordenescosecha;

import java.util.ArrayList;
import java.util.HashMap;

import com.secuencia.database.DatabaseHandler_;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

public class AdapterLaboresLotes {
	public SimpleAdapter adaptadorListaLabores(Context ctx, String IDTABLA, String FILTRO)
	{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] {
				"imgCheck", 
				"tv_numeroCorrelativo",
				"tv_codnomcliente", 
				"tv_codnomfinca",
				"tv_codnomlote",
				"tv_fecha",
				};
		int[] to = new int[] {
				R.id.imgCheck, 
				R.id.tv_numeroCorrelativo,
				R.id.tv_codnom_proveedor, 
				R.id.tv_codnom_finca, 
				R.id.tv_codnom_lote,
				R.id.tv_fecha, 
				};

        db = dbhelper.getReadableDatabase();
        
        Cursor c2 = db.rawQuery("SELECT "
        		+" a."+dbhelper.KEY_APENC1_ID 		+", "
        		+" a."+dbhelper.KEY_APENC2_CODCLIENTE +", "
        		+" a."+dbhelper.KEY_APENC3_NOMCLIENTE +", "
        		+" a."+dbhelper.KEY_APENC4_CODFINCA 	+", "
        		+" a."+dbhelper.KEY_APENC5_NOMFINCA 	+", "
        		+" a."+dbhelper.KEY_APENC6_CODLOTE 	+", "
        		+" a."+dbhelper.KEY_APENC7_NOMLOTE 	+", "
        		+" a."+dbhelper.KEY_APENC8_FECHA      +", "
        		+" a."+dbhelper.KEY_APENC10_LLAVE 	+", "
        		+" (SELECT COUNT(*) FROM "+dbhelper.TABLE_APLICACIONES_LABORES_DETALLES+" AS b WHERE b."+dbhelper.KEY_APDET2_IDECABEZADO+" = a."+dbhelper.KEY_APENC1_ID+" AND b."+dbhelper.KEY_APDET11_LLAVE_DET+" = '0' ) AS TOTAL_DET_NO, "
        		+" (SELECT COUNT(*) FROM "+dbhelper.TABLE_APLICACIONES_LABORES_DETALLES+" AS c WHERE c."+dbhelper.KEY_APDET2_IDECABEZADO+" = a."+dbhelper.KEY_APENC1_ID+" AND c."+dbhelper.KEY_APDET11_LLAVE_DET+" <> '0' ) AS TOTAL_DET_SI "

        		+ " FROM "+dbhelper.TABLE_APLICACIONES_LABORES_ENCABEZADO+" AS a " +
        				"WHERE" 
        				+ " ( CASE WHEN '"+IDTABLA+"' = '' THEN a."+dbhelper.KEY_APENC1_ID+" = a."+dbhelper.KEY_APENC1_ID+" ELSE a."+dbhelper.KEY_APENC1_ID+" = '"+IDTABLA+"' END )" 

        				+ " AND ( CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_APENC2_CODCLIENTE+" = a."+dbhelper.KEY_APENC2_CODCLIENTE+" ELSE a."+dbhelper.KEY_APENC2_CODCLIENTE+" LIKE '%"+FILTRO+"%' END "       					
        				+ " OR	  CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_APENC3_NOMCLIENTE+" = a."+dbhelper.KEY_APENC3_NOMCLIENTE+" ELSE a."+dbhelper.KEY_APENC3_NOMCLIENTE+" LIKE '%"+FILTRO+"%' END "       					
        				+ " OR    CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_APENC5_NOMFINCA+" = a."+dbhelper.KEY_APENC5_NOMFINCA+" ELSE a."+dbhelper.KEY_APENC5_NOMFINCA+" LIKE '%"+FILTRO+"%' END "
        				+ " OR    CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_APENC7_NOMLOTE+" = a."+dbhelper.KEY_APENC7_NOMLOTE+" ELSE a."+dbhelper.KEY_APENC7_NOMLOTE+" LIKE '%"+FILTRO+"%' END "   				
        				+ " OR    CASE WHEN '"+FILTRO+"' = '' THEN a."+dbhelper.KEY_APENC8_FECHA+" = a."+dbhelper.KEY_APENC8_FECHA+" ELSE a."+dbhelper.KEY_APENC8_FECHA+" LIKE '%"+FILTRO+"%' END )"
        				
        		+" ORDER BY "+dbhelper.KEY_APENC1_ID+" DESC ", null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
	  			HashMap<String, String> map = new HashMap<String, String>();	

				map.put("tv_numeroCorrelativo", contador+" -");
				map.put("tv_codnomcliente"	, ""+c2.getString(1) +" - "+c2.getString(2));
				map.put("tv_codnomfinca"	, "FINCA: "+c2.getString(3)+" - "+c2.getString(4));
				map.put("tv_codnomlote"		, "LOTE: "+c2.getString(5)+" - "+c2.getString(6));
				map.put("tv_fecha"          , "FECHA: "+c2.getString(7));
				
				map.put("ID",c2.getString(0));
				map.put("CODCLIENTE"  ,  c2.getString(1));
				map.put("CODFINCA"   ,c2.getString(3));
				map.put("CODLOTE"   ,c2.getString(5));
				map.put("LLAVE"   ,c2.getString(8));
				map.put("FECHA"   ,c2.getString(7));
				
	  			//BEGIN imagen sincronizado
	  			if(Integer.parseInt(c2.getString(10)) > 0 ) // SI HAY MAYOR A CERO 
	  			{
		  			if(Integer.parseInt(c2.getString(9)) > 0 ) // SI HAY MAYOR A CERO 
		  			{
		                Log.i("El item es:", "SI hay 0");
		      			map.put("imgCheck", ""+R.drawable.close);
		      			
		  			}else
		  			{
		                Log.i("El item es:", "NO hay 0");
		      			map.put("imgCheck", ""+R.drawable.open);
		  			}
	  			}else
	  			{
	                Log.i("El item es:", "SI hay 0");
	      			map.put("imgCheck", ""+R.drawable.close);
	  			}
	  			//END imagen sincronizado

				mylist.add(map); 
				contador++;
            }while(c2.moveToNext());
        }
		SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.row_lista_lista_secuencia_labores, from, to);
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
	
	public boolean insertNewAplicacion( Context ctx, String _CODSECUENCIA, String FECHA){
		try{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();
        db.execSQL("INSERT INTO "+dbhelper.TABLE_APLICACIONES_LABORES_ENCABEZADO
        		+" ( "
        		+ dbhelper.KEY_APENC13_IDSECUENCIA  + "  ," 
				+ dbhelper.KEY_APENC2_CODCLIENTE	+ " ," 	
				+ dbhelper.KEY_APENC3_NOMCLIENTE 	+ " ,"
				+ dbhelper.KEY_APENC4_CODFINCA		+ " ," 		
				+ dbhelper.KEY_APENC5_NOMFINCA		+ " ," 		
				+ dbhelper.KEY_APENC6_CODLOTE		+ " ," 		
				+ dbhelper.KEY_APENC7_NOMLOTE		+ " ," 	
				+ dbhelper.KEY_APENC8_FECHA			+ " ," 		    
				+ dbhelper.KEY_APENC9_ESTATUS 		+ " ,"
				+ dbhelper.KEY_APENC10_LLAVE 		+ " ,"
				+ dbhelper.KEY_APENC11_CODUSUARIO 	+ " ,"
				+ dbhelper.KEY_APENC12_ZAFRA 	  	+ " "
				       			
        		+" ) SELECT "
        		+ " DISTINCT("+dbhelper.KEY_SEC1_ID	+ ") , "
        		+ " "+dbhelper.KEY_SEC6_CODCLIENTE	+ " ," 	        			
        		+ " "+dbhelper.KEY_SEC7_NOMCLIENTE	+ " ," 	
        		+ " "+dbhelper.KEY_SEC8_CODFINCA	+ " ," 	
        		+ " "+dbhelper.KEY_SEC9_NOMFINCA	+ " ," 	
        		+ " "+dbhelper.KEY_SEC10_CODLOTE	+ " ," 	
        		+ " "+dbhelper.KEY_SEC11_NOMLOTE	+ " ," 	
        		+ " '"+FECHA	                    + "' ," 	
        		+ " '"+0							+ "' ," 	
        		+ " '"+0							+ "' ," 	
        		+ " "+dbhelper.KEY_SEC15_AGRONOMO	+ " ," 	
        		+ " "+dbhelper.KEY_SEC16_ZAFRA	    + " "
        		
        		+" FROM "+dbhelper.TABLE_SECUENCIA_PARALABORES+" WHERE "+dbhelper.KEY_SEC1_ID+" = "+_CODSECUENCIA+" LIMIT 1 ");  
        db.close();
        
		}catch (Exception  e){
		      //throw new RuntimeException(e);
			return false;
		}
        return true;
	}
}
