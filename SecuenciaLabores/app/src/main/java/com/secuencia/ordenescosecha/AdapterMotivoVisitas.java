package com.secuencia.ordenescosecha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.secuencia.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterMotivoVisitas {
    public SimpleAdapter adapterMotivoVisitas(Context ctx, String codTipo)
    {
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String[] from = new String[] { "tv_descripcion_row", };
        int[] to = new int[] { R.id.tv_descripcion_row, };

        String qry = "SELECT " +
                " DISTINCT c."+dbhelper.K_TIP3_CODMOTIVO+", "+
                " c."+dbhelper.K_TIP4_DESCMOTIVO+" "+
                " FROM "+dbhelper.TABLE_TIPOVISITAS+" c "+
                " WHERE c."+dbhelper.K_TIP1_CODTIPO+" = '"+codTipo+"' "
                ;

        Log.d("Metodo Query: ", "" + qry);
        Cursor c2 = db.rawQuery(qry, null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("codMotivoVisita", c2.getString(0));
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