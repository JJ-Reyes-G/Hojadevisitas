package com.secuencia.ordenescosecha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.secuencia.database.DatabaseHandler_;
import com.secuencia.ordenescosecha.R;

import java.util.HashMap;

public class ActLotesSelect extends Activity {



    ListView lv_lotes;
    AdapterLotesCheck adpLotesCheck = new AdapterLotesCheck();
    Context ctx;
    ClassDescargarInicioApp classDescargarInicioApp;
    DatabaseHandler_ dbhelper;
    int listPosicion = 0;
    String ZAFRA, SOLICITUD_ID, CODFINCA;
    SimpleAdapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_lotes_select);

        ctx = this;
        classDescargarInicioApp = new ClassDescargarInicioApp(this);
        dbhelper = new DatabaseHandler_(ctx);

        lv_lotes = (ListView)findViewById(R.id.lv_lista_lotes);
        ZAFRA        = "";
        SOLICITUD_ID = getIntent().getExtras().getString("LLAVE");
        CODFINCA     = getIntent().getExtras().getString("codFinca");

        Toast.makeText(ctx, "" + ZAFRA, Toast.LENGTH_SHORT).show();
        adp = adpLotesCheck.adapterLotesCheck(ctx, CODFINCA, ZAFRA, SOLICITUD_ID);
        lv_lotes.setAdapter(adp);

        lv_lotes.setSelection(listPosicion);
        lv_lotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View vista,
                                    int posicion, long arg3) {
                final HashMap<String, String> itemList = (HashMap<String, String>) lv_lotes.getItemAtPosition(posicion);
                if(adpLotesCheck.addNuevoLoteSelect(ctx, "ZAFRA", itemList.get("codLote").toString(), "0", SOLICITUD_ID, itemList.get("LselId").toString())){
                    try {
                        adpLotesCheck.updateListaAdaper(posicion, itemList);
                        adp.notifyDataSetChanged();
                    } catch (NullPointerException e) {
                        Log.e("ERROR", "actualizar valor check genera errror", e);
                    }
                    //lv_lotes.setSelection(listPosicion);
                }
                //setCantidadHaLote("Digite las Ha", true, posicion, vista);
            }
        });
    }

    public void setCantidadHaLote(String mensajeAlerta, final boolean alertaIncon, final int
            posicion, final View vista){
        final HashMap<?, ?> itemList = (HashMap<?, ?>) lv_lotes.getItemAtPosition(posicion);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final Double cantContratada = Double.parseDouble(itemList.get("cant_contratada").toString());

        final EditText txtCantidadAsignada = ClassConfig.txtCapturaNumDecimal("Digite cantidad en Ha", ""+cantContratada , this);
        txtCantidadAsignada.setPadding(10, 10, 10, 10);
        TextView miTextView = new TextView(getApplicationContext());
        miTextView.setPadding(10, 10, 10, 10);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        layout.addView(miTextView);
        layout.addView(txtCantidadAsignada);

        builder.setView(layout);
        final AlertDialog AlertDialogUpdate = builder.create();

        AlertDialogUpdate.setTitle("Cantidad Articulo");
        AlertDialogUpdate.setMessage(mensajeAlerta);
        /*
        if(alertaIncon){
            AlertDialogUpdate.setIcon(R.mipmap.ic_alert_icon);
        }
        */
        miTextView.setText("Cantidad: ");
        miTextView.setTextColor(Color.parseColor("#1E1D1C"));

        AlertDialogUpdate.setButton(RESULT_OK, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (txtCantidadAsignada.getText().toString().equals("") || Double.parseDouble(txtCantidadAsignada.getText().toString()) < 0 || Double.parseDouble(txtCantidadAsignada.getText().toString()) > cantContratada) {
                    modalCuadroAlerta("Valor ingresado es incorrecto");
                } else {
                    AdapterLotesCheck adpLotesCheck = new AdapterLotesCheck();
                    ClassLotesSelect LotesSelect = new ClassLotesSelect();
                    LotesSelect.setCodLote(itemList.get("codLote").toString());
                    LotesSelect.setCodFinca(itemList.get("codFinca").toString());
                    LotesSelect.setZafra(itemList.get("zafra").toString());

                    if(adpLotesCheck.addNuevoLoteSelect(ctx, ZAFRA, itemList.get("codLote").toString(), txtCantidadAsignada.getText().toString(), SOLICITUD_ID, itemList.get("LselId").toString())){
                        lv_lotes.setAdapter(adpLotesCheck.adapterLotesCheck(ctx, CODFINCA, ZAFRA, SOLICITUD_ID));
                        lv_lotes.setSelection(listPosicion);
                        //Toast.makeText(ctx, "prueba", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        AlertDialogUpdate.show();
    }

    public void modalCuadroAlerta(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        final AlertDialog AlertDialogUpdate = builder.create();

        AlertDialogUpdate.setTitle("Alerta");
        AlertDialogUpdate.setMessage(mensaje);

        AlertDialogUpdate.setButton(RESULT_OK, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // CANCELADO NO HACEMOS NADA

            }
        });
        AlertDialogUpdate.show();
    }
    public void intentRESULT_OK(){
        Intent i= getIntent();
        i.putExtra("SOLICITUD_ID", SOLICITUD_ID);
        setResult(this.RESULT_OK, i);
        finish();
    }

}
