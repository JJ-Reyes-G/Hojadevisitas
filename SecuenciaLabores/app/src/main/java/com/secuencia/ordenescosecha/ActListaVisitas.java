package com.secuencia.ordenescosecha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.secuencia.database.DatabaseHandler_;

import java.util.HashMap;

public class ActListaVisitas extends Activity {
    ListView lv_visitas;
    AdapterVisitasFincas adpVisitas = new AdapterVisitasFincas();

    Context ctx = this;
    private ProgressDialog pDialog;
    String idVisita, visId;
    ClassDescargarInicioApp CDesInicioApp;
    DatabaseHandler_ dbh ;
    static int numeroActividadAsincrona = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_lista_visitas);
        lv_visitas = (ListView)findViewById(R.id.lv_visitas);
        lv_visitas.setAdapter(adpVisitas.adapterListaVisitas(ctx, "", ""));

        CDesInicioApp = new ClassDescargarInicioApp(ctx);
        dbh = new DatabaseHandler_(ctx);

        registerForContextMenu(lv_visitas);

        // AL PRECIONAR UNA DE LAS FILAS DE LA LISTA
        lv_visitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View vista,
                                    int posicion, long arg3) {
                HashMap<?, ?> itemList = (HashMap<?, ?>) lv_visitas.getItemAtPosition(posicion);
                Toast.makeText(ctx, itemList.get("LLAVE").toString(), Toast.LENGTH_SHORT).show();
                if(!itemList.get("LLAVE").toString().equals("0")){
                    Intent i= new Intent(ctx, ActLotesSelect.class);
                    i.putExtra("codFinca",""+itemList.get("CODFINCA"));
                    i.putExtra("LLAVE", "" + itemList.get("LLAVE"));
                    startActivityForResult(i, 1);
                    //startActivity(i);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_lista_visitas, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                //imgbtn_barra_sincronizar();
                return true;

            case R.id.menu_add_visitas:
                addDialogListaLabores();
                return true;

            case R.id.menu_limpiar:
                imgbtn_guardarVisita(2);
                return true;
            case R.id.menu_reiniciar_sistema:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //---------------------Menu contextual--------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        if(v.getId() == R.id.lv_visitas)
        {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo)menuInfo;
            HashMap<?, ?> itemList = (HashMap<?, ?>) lv_visitas.getItemAtPosition(info.position);

            menu.setHeaderTitle(""+itemList.get("tv_nombreContacto"));
            inflater.inflate(R.menu.menu_ctx_visitas_list, menu);

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        HashMap<?, ?> itemList3 = (HashMap<?, ?>) lv_visitas.getItemAtPosition(info.position);
        idVisita = itemList3.get("visitaId").toString() ;
        visId = itemList3.get("LLAVE").toString() ;
        switch (item.getItemId()) {

            case R.id.CtxLstOpcGuardar:
                imgbtn_guardarVisita(1);
                return super.onContextItemSelected(item);

            case R.id.CtxLstOpcGuardarLotes:
                imgbtn_guardarVisita(3);
                return super.onContextItemSelected(item);

            case R.id.CtxLstOpceEliminar:
                alertMensaje(ctx, "Advertencia", "Desea borrar este elemento permanentemente.");
                return super.onContextItemSelected(item);

            case R.id.CtxLstOpcBajarVisitas:
                imgbtn_guardarVisita(2);
                return super.onContextItemSelected(item);

            default:
                return super.onContextItemSelected(item);
        }
    }
    //END MENU CONTEXTUAL

    public void alertMensaje(final Context ctx,final String titulo,final String mensaje)
    {
				AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
				alert.setMessage(mensaje);

				alert.setTitle(titulo);
				alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
                        CDesInicioApp.deleteAllTablaWhereCampo(dbh.TABLE_VISITAS, dbh.KEY_VIS0_ID, "" + idVisita, "=");
                        lv_visitas.setAdapter(adpVisitas.adapterListaVisitas(ctx, "", ""));

					}
				});
				alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Cancelado no hacemos nada.
					}
				});
				alert.show();
    }

    public void addDialogListaLabores(){
        Log.d("ADD ACTIVIDADES", " 1 ");//AlertDialog.Builder builder;
        final AlertDialog  AlertDialogUpdate;
        final ListView dialodListZonas = new ListView(this);

        LinearLayout layout = new LinearLayout(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        AdapterZonas adpZonas = new  AdapterZonas();

        dialodListZonas.setAdapter(adpZonas.adapterZonas(ctx));

        if(dialodListZonas.getCount() == 0){

            Intent i= new Intent(ctx, SecuenciaLabores.class);
            i.putExtra("numeroZona"	, "0");
            i.putExtra("tipoActividad"	, "VISITAS");
            startActivity(i);
            finish();
        }

        layout.addView(dialodListZonas);

        builder.setView(layout);
        AlertDialogUpdate = builder.create();

        AlertDialogUpdate.setTitle("Seleccione una Actividad");
        AlertDialogUpdate.setButton(RESULT_OK, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // CANCELADO NO HACEMOS NADA.

            }
        });
        AlertDialogUpdate.show();
        dialodListZonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View vista,
                                    int posicion, long arg3) {
                AlertDialogUpdate.cancel();
                HashMap<?, ?> itemList = (HashMap<?, ?>) dialodListZonas.getItemAtPosition(posicion);
                Intent i= new Intent(ctx, SecuenciaLabores.class);
                i.putExtra("numeroZona"	, itemList.get("tv_codnom_zona").toString());
                i.putExtra("tipoActividad"	, "VISITAS");
                startActivity(i);
                finish();

            }
        });
    }

    public void imgbtn_guardarVisita( int numeroTarea)
    {
        pDialog = new ProgressDialog(ctx);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Guardando visita....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        numeroActividadAsincrona = numeroTarea;

        new MiTareaAsincronaDialog().execute();
    }
    //clase que se ejecuta mientras se ejecuta la consulta el web service y el  array
    private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            switch (numeroActividadAsincrona) {

                case 1:
                    if (CDesInicioApp.addNuevaVisita(idVisita)){ // METODO ACTIVIDAD DE CLASE DESCARGA DE INICIO

                        return true;
                    }else{
                        if(isCancelled()){
                            Log.d("ListaVisitas: ","doInBackground, Actividad cancelada TRUE");
                            return false;
                        }
                        Log.d("ListaVisitas: ","doInBackground, Actividad cancelada FALSE");
                        return false;
                    }

                case 2:
                    if (CDesInicioApp.ws_bajarUltimasVisitas(ClassConfig.key_codTecnico)){ // METODO ACTIVIDAD DE CLASE DESCARGA DE INICIO

                        return true;
                    }else{
                        if(isCancelled()){
                            Log.d("ListaVisitas: ","doInBackground, Actividad cancelada TRUE");
                            return false;
                        }
                        Log.d("ListaVisitas: ","doInBackground, Actividad cancelada FALSE");
                        return false;
                    }
                case 3:
                    if (CDesInicioApp.Hv_HojaVisitas_Lotes_Insert( visId)){ // METODO ACTIVIDAD DE CLASE DESCARGA DE INICIO

                        return true;
                    }else{
                        if(isCancelled()){
                            Log.d("ListaVisitas: ","doInBackground, Actividad cancelada TRUE");
                            return false;
                        }
                        Log.d("ListaVisitas: ","doInBackground, Actividad cancelada FALSE");
                        return false;
                    }


                default:
                    return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {

            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiTareaAsincronaDialog.this.cancel(true);
                }
            });

            pDialog.setMax(100);
            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();
                Toast.makeText(ctx, "Tarea Descarga finalizada Exitosamente!",Toast.LENGTH_SHORT).show();
                Log.d("Ordenes =", "onPostExecute, BIEN Tarea de descarga de secuencia finalizada exitosamente");

                lv_visitas.setAdapter(adpVisitas.adapterListaVisitas(ctx, "",""));
            }else{
                pDialog.dismiss();
                Toast.makeText(ctx, "No se pudo realizar la descarga exitosamente",Toast.LENGTH_SHORT).show();
                Log.d("Ordenes =","onPostExecute, ERROR Tarea de descarga no fue completada exitosamente");
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(ctx, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();

        }
    }
    //FIN CLASE SINCRONIZAR
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:

                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        lv_visitas.setAdapter(adpVisitas.adapterListaVisitas(ctx, "", ""));
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
