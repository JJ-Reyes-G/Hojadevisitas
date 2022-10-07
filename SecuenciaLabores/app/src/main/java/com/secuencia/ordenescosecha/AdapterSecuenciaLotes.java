package com.secuencia.ordenescosecha;

import java.util.ArrayList;
import java.util.HashMap;

import com.secuencia.database.DatabaseHandler_;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

public class AdapterSecuenciaLotes {
	public SimpleAdapter adaptadorListaOrdenes(Context ctx, String FILTRO, String numeroZona)
	{
		SQLiteDatabase db;
		DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
		db = dbhelper.getReadableDatabase();
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] {
				"secuencia", 
				"nombreocacion",
				"idlista",
				"fechaCreacion",
				"listaPrecio",
				"listaEstatus",
				"sectonestima",
				"tv_zafra_lote",
				};
		int[] to = new int[] {
				R.id.tv_numeroSecuencia, 
				R.id.tv_variedad_ofrente, 
				R.id.tv_codigoLista, 
				R.id.tv_codnom_proveedor, 
				R.id.tv_codnom_lote, 
				R.id.tv_codnom_finca,
				R.id.tv_tonestima,
				R.id.tv_zafra_lote,
				};

        String qry = "";
        
        //if(tipoLista == 1){
            qry = "SELECT * FROM "+dbhelper.TABLE_SECUENCIA_PARALABORES+" c "
            		+ " WHERE "
					+ " (CASE WHEN '"+numeroZona+"' = '0' THEN c."+dbhelper.KEY_SEC17_ZONA+" = c."+dbhelper.KEY_SEC17_ZONA+" ELSE c."+dbhelper.KEY_SEC17_ZONA+" LIKE '%"+numeroZona+"%' END ) "
					+ " AND( CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC6_CODCLIENTE+" = c."+dbhelper.KEY_SEC6_CODCLIENTE+" ELSE c."+dbhelper.KEY_SEC6_CODCLIENTE+" LIKE '%"+FILTRO+"%' END "
					+ " OR   CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC7_NOMCLIENTE+" = c."+dbhelper.KEY_SEC7_NOMCLIENTE+" ELSE c."+dbhelper.KEY_SEC7_NOMCLIENTE+" LIKE '%"+FILTRO+"%' END "
					+ " OR   CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC9_NOMFINCA+" = c."+dbhelper.KEY_SEC9_NOMFINCA+" ELSE c."+dbhelper.KEY_SEC9_NOMFINCA+" LIKE '%"+FILTRO+"%' END "
					+ " OR   CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC11_NOMLOTE+" = c."+dbhelper.KEY_SEC11_NOMLOTE+" ELSE c."+dbhelper.KEY_SEC11_NOMLOTE+" LIKE '%"+FILTRO+"%' END "
					+ " ) "
					+" ORDER BY c."+dbhelper.KEY_SEC17_ZONA+" ASC "
					+"";


		Log.d("Metodo Query: ", ""+qry);
        Cursor c2 = db.rawQuery(qry, null); 
        int contador = 1;
        if(c2.moveToFirst()){
            do{
	  			HashMap<String, String> map = new HashMap<String, String>();
				map.put("tipoLista",  	 "LOTES");
				map.put("idlista",       c2.getString(0));
				map.put("secuencia", 	 c2.getString(0));
				map.put("nombreocacion", c2.getString(3)+" - "+c2.getString(4));
				map.put("fechaCreacion", c2.getString(5)+" - "+c2.getString(6));
				map.put("listaPrecio", 	 c2.getString(9)+" - "+c2.getString(10));
				map.put("listaEstatus",  c2.getString(7)+" - "+c2.getString(8));
				map.put("sectonestima",  "ZONA: "+c2.getString(16));
				map.put("nomProveedor",  c2.getString(6));
				map.put("codLote", 		 c2.getString(9));
				map.put("nomLote", 		 c2.getString(10));
				map.put("imagen", 		 ""+R.drawable.chekfalse);
				map.put("tv_zafra_lote", ""+c2.getString(15));
				mylist.add(map); 
				contador++;
            }while(c2.moveToNext());
        }
		SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.rows_lista_secuencia, from, to);
		
        c2.close();
        db.close();	
        return adapter;
	}
}
