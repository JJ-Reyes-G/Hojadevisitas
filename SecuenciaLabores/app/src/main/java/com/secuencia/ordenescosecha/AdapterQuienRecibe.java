package com.secuencia.ordenescosecha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.secuencia.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterQuienRecibe {
    public SimpleAdapter adapterQuienRecibe(Context ctx)
    {
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        String[] from = new String[] { "tv_descripcion_row", };
        int[] to = new int[] { R.id.tv_descripcion_row, };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("codQuienRecibe", "Proveedor");
        map.put("tv_descripcion_row", "Proveedor");
        mylist.add(map);

        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("codQuienRecibe", "Encargado");
        map2.put("tv_descripcion_row", "Encargado");

        mylist.add(map2);


        SimpleAdapter adapter = new SimpleAdapter(ctx, mylist, R.layout.row_style_spinner, from, to);

        return adapter;
    }
}