package com.secuencia.ordenescosecha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.secuencia.database.DatabaseHandler_;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterLotesCheck {
    ArrayList<HashMap<String, String>> myListActivosOcaciones = new ArrayList<HashMap<String, String>>();
    public SimpleAdapter adapterLotesCheck (Context ctx, String fincaId, String Zafra, String SOLIC_ID){
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();

        String query = "SELECT "
                + "DISTINCT(a."+dbhelper.KEY_SEC10_CODLOTE +"), "
                + " a."+dbhelper.KEY_SEC11_NOMLOTE			+", "
                + " a."+dbhelper.KEY_SEC8_CODFINCA			+", "
                + " a."+dbhelper.KEY_SEC8_CODFINCA			+" AS finca, "
                + " '' AS estatus, "
                + " a."+dbhelper.KEY_SEC10_CODLOTE			+", "
                + " 0  AS areaContratada, "
                + " ifnull(b."+dbhelper.K_LSEL11_CANT_SOLIC	+" , 0 ) AS CANT, "//+ " a."+dbhelper.K_LOTE11_CANT_SOLIC		+"  "
                + " ifnull(b."+dbhelper.K_LSEL0_ID		    +" , 0 ) AS LSELID, "
                + " a."+dbhelper.KEY_SEC16_ZAFRA			+", "
                + " a."+dbhelper.KEY_SEC9_NOMFINCA			+"  "

                + " FROM   "+dbhelper.TABLE_SECUENCIA_PARALABORES+" AS a"
                + " LEFT OUTER JOIN "+dbhelper.TABLE_LOTES_SELECT+" AS b ON a."+dbhelper.KEY_SEC10_CODLOTE+" = b."+ dbhelper.K_LSEL1_LOTEID+"  AND b."+dbhelper.K_LSEL12_SOLIC_ID+" = '"+SOLIC_ID+"' AND b."+ dbhelper.K_LSEL14_DELETE+" = '0' "
                + " WHERE "
                + "  ( CASE WHEN '"+fincaId+"' = '' THEN a."+dbhelper.KEY_SEC8_CODFINCA+" = a."+dbhelper.KEY_SEC8_CODFINCA+" ELSE a."+dbhelper.KEY_SEC8_CODFINCA+" = '"+fincaId+"' END ) "
                + " ORDER BY "+ " a."+dbhelper.KEY_SEC11_NOMLOTE
                ;

        Log.e("QUERY", "QUERY: " + query);

        String[] fromActivos = new String[] {
                "tvLoteTitulo",
                "tvProvSub1",
                "avatar",
                "tv_cantidad_solicitada",
        };

        int[] toActivos = new int[] {
                R.id.tvLoteTitulo,
                R.id.tvProvSub1,
                R.id.avatar,
                R.id.tv_cantidad_solicitada,
        };

        Cursor cFrentes = db.rawQuery(query, null);

        if(cFrentes.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tvLoteTitulo",     cFrentes.getString(5)+" / "+cFrentes.getString(1));
                map.put("tvProvSub1",       "" );
                map.put("LoteId",           cFrentes.getString(0));
                map.put("codLote",          cFrentes.getString(5));
                map.put("nomLote",          cFrentes.getString(1));
                map.put("codFinca",         cFrentes.getString(2));
                map.put("nomFinca",         cFrentes.getString(10));
                map.put("zafra",            cFrentes.getString(9));
                map.put("LselId",           cFrentes.getString(8));
                map.put("LoteEstatus",      cFrentes.getString(4));
                map.put("AreaSolicitada",   cFrentes.getString(7));
                map.put("tv_cantidad_solicitada", "");
                map.put("cant_solicitada",  cFrentes.getString(7));
                map.put("cant_contratada",  cFrentes.getString(6));

                if(cFrentes.getString(8).toString().equals("0")){
                    map.put("avatar"  , ""+R.mipmap.ic_check_off);
                }else{
                    map.put("avatar"  , ""+R.mipmap.ic_check_on);
                }

                myListActivosOcaciones.add(map);

            }while(cFrentes.moveToNext());
        }
        Log.i("Cursor", "Cursor: " + cFrentes);

        cFrentes.close();
        db.close();

        SimpleAdapter adapterlistProveedores = new SimpleAdapter(ctx, myListActivosOcaciones,R.layout.list_item_lotes, fromActivos, toActivos);

        return adapterlistProveedores;
    }

    public boolean updateListaAdaper( int position, HashMap<String, String> itemList){
        //myListActivosOcaciones.remove(position);
        if(itemList.get("avatar").toString().equals(""+ R.mipmap.ic_check_on)) {
            itemList.put("avatar", "" + R.mipmap.ic_check_off);
        }else{
            itemList.put("avatar", "" + R.mipmap.ic_check_on);
        }
        myListActivosOcaciones.set(position, itemList);//.set(position,map);
        return true;
    }

    public boolean addNuevoLoteSelect(Context ctx, String ZAFRA, String codLote, String CantidadSolicitada, String Solic_Id, String idLoteChek){
        ClassDescargarInicioApp classDescargarInicioApp = new ClassDescargarInicioApp(ctx);
        SQLiteDatabase db;



        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();
        limpiarValorLote(ctx, codLote,Solic_Id );

        String Sel_Delete = "0";
        if (idLoteChek.equals("0")) {
            Sel_Delete = "0";
        }else{
            Sel_Delete = "1";
        }
            String query = "INSERT INTO " + dbhelper.TABLE_LOTES_SELECT + " (" +
                    dbhelper.K_LSEL1_LOTEID + ", " +
                    dbhelper.K_LSEL2_NOMLOTE + ", " +
                    dbhelper.K_LSEL3_CODFINCA + ", " +
                    dbhelper.K_LSEL4_IDOIP + ", " +
                    dbhelper.K_LSEL5_ENCID_MOV + ", " +
                    dbhelper.K_LSEL6_LLAVE + ", " +
                    dbhelper.K_LSEL7_IDREQ + ", " +
                    dbhelper.K_LSEL8_CONTRATO + ", " +
                    dbhelper.K_LSEL9_ZAFRA + ", " +
                    dbhelper.K_LSEL10_AREA + ", " +
                    dbhelper.K_LSEL11_CANT_SOLIC + ", " +
                    dbhelper.K_LSEL12_SOLIC_ID + ", " +
                    dbhelper.K_LSEL13_LLAVE_ENC + ", " +
                    dbhelper.K_LSEL14_DELETE + "  " +
                    ") " +
                    "SELECT "
                    + " a." + dbhelper.KEY_SEC10_CODLOTE + ",  "
                    + " a." + dbhelper.KEY_SEC11_NOMLOTE + ",  "
                    + " a." + dbhelper.KEY_SEC8_CODFINCA + ",  "
                    + " '0', "
                    + " '0', "
                    + " '0', "
                    + " '0', "
                    + " '0', "
                    + " a." + dbhelper.KEY_SEC16_ZAFRA + ",  "
                    + " '0',  "
                    + " '" + CantidadSolicitada + "',  "
                    + " '" + Solic_Id   + "', "
                    + " '0', "
                    + " '" + Sel_Delete + "' "
                    + " FROM  " + dbhelper.TABLE_SECUENCIA_PARALABORES + " AS a "
                    + " WHERE a." + dbhelper.KEY_SEC10_CODLOTE + " = '" + codLote + "' "
                    ;

            Log.e("addNuevaMuestra", "" + query);
            db = dbhelper.getReadableDatabase();


            db.execSQL(query);

        db.close();

        return true;
    }

    public boolean limpiarValorLote(Context ctx, String codLote, String SOLIC_ID){
        SQLiteDatabase db;
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();
        String query = "DELETE FROM " + dbhelper.TABLE_LOTES_SELECT + " WHERE  " +
                dbhelper.K_LSEL1_LOTEID 		    	+" = '" +codLote+"' AND "+
                dbhelper.K_LSEL12_SOLIC_ID				+" = '"+SOLIC_ID+"' "
                ;

        Log.e("addNuevaMuestra", "" + query);
        db = dbhelper.getReadableDatabase();
        db.execSQL(query);
        db.close();

        return true;
    }
/*
    public String getTotalHaSolcitada(Context ctx, String SOLIC_ID){
        SQLiteDatabase db;
        String totalHaSolcitada = "";
        DatabaseHandler_ dbhelper = new DatabaseHandler_(ctx);
        db = dbhelper.getReadableDatabase();
        String query = "SELECT "+
                " ifnull(SUM("+dbhelper.K_LSEL11_CANT_SOLIC+"),0.0) AS total "+
                " FROM " + dbhelper.TABLE_LOTES_SELECT + " WHERE  " +
                //dbhelper.K_LSEL8_CONTRATO 		    	+" = '"+contrato+"' AND "+
                dbhelper.K_LSEL12_SOLIC_ID				+" = '"+SOLIC_ID+"' "
                ;

        Cursor cFrentes = db.rawQuery(query, null);
        if(cFrentes.moveToFirst()){
            do{
                //HashMap<String, String> map = new HashMap<String, String>();
                totalHaSolcitada = cFrentes.getString(0);

            }while(cFrentes.moveToNext());
        }
        Log.i("Cursor", "Cursor: " + cFrentes);

        cFrentes.close();
        db.close();

        return totalHaSolcitada;
    }
  */
}