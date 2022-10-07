package com.secuencia.ordenescosecha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.secuencia.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Juan Jose on 19/01/2016.
 */
public class AdapterZonas {
    public SimpleAdapter adapterZonas(Context ctx)
    {
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String[] from = new String[] {
                "tv_numeroCorrelativo",
                "tv_codnom_zona"

        };
        int[] to = new int[] {
                R.id.tv_numeroCorrelativo,
                R.id.tv_codnom_zona
        };

        String qry = "SELECT " +
                    " DISTINCT c."+dbhelper.KEY_SEC17_ZONA+" "+
                    " FROM "+dbhelper.TABLE_SECUENCIA_PARALABORES+" c ";

        Log.d("Metodo Query: ", "" + qry);
        Cursor c2 = db.rawQuery(qry, null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("tv_numeroCorrelativo", "ZONA");
                map.put("tv_codnom_zona",       c2.getString(0));

                mylist.add(map);
                contador++;
            }while(c2.moveToNext());
        }
        SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.row_lista_zonas, from, to);

        c2.close();
        db.close();
        return adapter;
    }

}
