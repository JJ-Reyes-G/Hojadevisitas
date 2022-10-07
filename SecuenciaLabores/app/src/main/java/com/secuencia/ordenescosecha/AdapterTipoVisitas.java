package com.secuencia.ordenescosecha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.secuencia.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterTipoVisitas {
    public SimpleAdapter adapterTipoVisitas(Context ctx)
    {
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String[] from = new String[] {
                "tv_descripcion_row",
        };
        int[] to = new int[] {
                R.id.tv_descripcion_row,
        };

        String qry = "SELECT " +
                " DISTINCT c."+dbhelper.K_TIP1_CODTIPO+", "+
                " c."+dbhelper.K_TIP2_DESCTIPO+" "+
                " FROM "+dbhelper.TABLE_TIPOVISITAS+" c ";

        Log.d("Metodo Query: ", "" + qry);
        Cursor c2 = db.rawQuery(qry, null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("codTipoVisita", c2.getString(0));
                map.put("tv_descripcion_row", c2.getString(1));

                mylist.add(map);
                contador++;
            }while(c2.moveToNext());
        }
        SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.row_style_spinner, from, to);

        c2.close();
        db.close();
        return adapter;
    }
}