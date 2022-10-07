package com.secuencia.ordenescosecha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.secuencia.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterProveedores {
    public SimpleAdapter AdapterProveedores(Context ctx, String FILTRO, String numeroZona)
    {
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String[] from = new String[] {
                "tv_codnom_prov",

        };
        int[] to = new int[] {
                R.id.tv_codnom_prov,

        };

        String qry = "";

        //if(tipoLista == 1){
        qry = "SELECT "
                + " DISTINCT c."+dbhelper.KEY_SEC6_CODCLIENTE+" , "
                + " c."+dbhelper.KEY_SEC7_NOMCLIENTE+"  "
                + " FROM "+dbhelper.TABLE_SECUENCIA_PARALABORES+" c "
                + " WHERE "
                + " (CASE WHEN '"+numeroZona+"' = '0' THEN c."+dbhelper.KEY_SEC17_ZONA+" = c."+dbhelper.KEY_SEC17_ZONA+" ELSE c."+dbhelper.KEY_SEC17_ZONA+" LIKE '%"+numeroZona+"%' END ) "
                + " AND( CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC6_CODCLIENTE+" = c."+dbhelper.KEY_SEC6_CODCLIENTE+" ELSE c."+dbhelper.KEY_SEC6_CODCLIENTE+" LIKE '%"+FILTRO+"%' END "
                + " OR   CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC7_NOMCLIENTE+" = c."+dbhelper.KEY_SEC7_NOMCLIENTE+" ELSE c."+dbhelper.KEY_SEC7_NOMCLIENTE+" LIKE '%"+FILTRO+"%' END "
                //+ " OR   CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC9_NOMFINCA+" = c."+dbhelper.KEY_SEC9_NOMFINCA+" ELSE c."+dbhelper.KEY_SEC9_NOMFINCA+" LIKE '%"+FILTRO+"%' END "
                //+ " OR   CASE WHEN '"+FILTRO+"' = '' THEN c."+dbhelper.KEY_SEC11_NOMLOTE+" = c."+dbhelper.KEY_SEC11_NOMLOTE+" ELSE c."+dbhelper.KEY_SEC11_NOMLOTE+" LIKE '%"+FILTRO+"%' END "
                + " ) "
                +" ORDER BY c."+dbhelper.KEY_SEC17_ZONA+" ASC "
                +"";


        Log.d("Metodo Query: ", "" + qry);
        Cursor c2 = db.rawQuery(qry, null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tipoLista",  	    "PROVEEDORES");
                map.put("codProveedor", 	 c2.getString(0));
                map.put("nomProveedor",       c2.getString(1));

                map.put("tv_codnom_prov", c2.getString(0)+" - "+c2.getString(1));

                mylist.add(map);
                contador++;
            }while(c2.moveToNext());
        }
        SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.rows_lista_proveedores, from, to);

        c2.close();
        db.close();
        return adapter;
    }
}