package com.secuencia.ordenescosecha;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.secuencia.database.DatabaseHandler_;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DetallesLabores extends Activity {
	ListView lv_detalles_labores;
	TextView tv_codnom_cliente, tv_codnom_finca, tv_codnom_lote, tv_codnom_fecha;
	//public static String estatusEncabezadoReq , estatusLocal;
	private ProgressDialog pDialog;


	SQLiteDatabase db;
	DatabaseHandler_ dbhelper;
	AlertDialog d, ocacionesUpdate;
	AlertDialog.Builder builder;
	AlertDialog  AlertDialogUpdate;
	DatabaseHandler_ dbhandler;
	//BEGIN VARIABLES QUE PASAN POR EL INTENT
	public static String CODNOM_CLIENTE, CODNOM_FINCA, CODNOM_LOTE, CODNOM_FECHA, LLAVE, FECHA, ID;
	//END VARIABLES QUE PASAN POR EL INTENT
	SwipeListViewTouchListener touchListener;
	Context ctx = this;
	private int mYear, mMonth, mDay, mHour, mMinute;
	ClassDescargarInicioApp ClassInicioApp;
	int estatusInteger = 0; //CODIGO IDENTIFICAR EL ESTATUS DE LA VISTA
	TableLayout tl_header;
	AdapterLaboresLotes adpLaboresLotes = new AdapterLaboresLotes();
	AdapterDetallesLabores adpDetallesLabores = new AdapterDetallesLabores();

    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalles_labores);
		lv_detalles_labores = (ListView)findViewById(R.id.lv_detalles_labores);
		tl_header 			=  (TableLayout)findViewById(R.id.tl_header);
		
		dbhelper      = new DatabaseHandler_(this);
		ClassInicioApp = new ClassDescargarInicioApp(this);
		
        tl_header.setBackgroundColor(getResources().getColor(R.color.azul_fuerte));        		

		llenarEncabazado();

	      //View headerOpciones = getLayoutInflater().inflate(R.layout.rows_lista_secuencia, null);
	      //lv_detalles_labores.addHeaderView(headerOpciones);
	      lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));
	      
          Log.i("MILLAVE", "MILLAVE: "+ID);
          
  		//DESLIZAR PARA ELIMINAR
  		touchListener =new SwipeListViewTouchListener(lv_detalles_labores, new SwipeListViewTouchListener.OnSwipeCallback() {        
  			@Override
  			public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {

  				HashMap<?, ?> itemList = (HashMap<?, ?>) lv_detalles_labores.getItemAtPosition(reverseSortedPositions[0]); 
  				
                  Log.i("El item es:", itemList.get("IDDETALLE").toString());
                  cuadroAlertaBorrar("Esta seguro que desea eliminar esta Actividad", "Advertencia", "Eliminar",Integer.parseInt(itemList.get("IDDETALLE").toString()));
       
  			}
  			
  			@Override
  			public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
  		        HashMap<?, ?> itemList = (HashMap<?, ?>) lv_detalles_labores.getItemAtPosition(reverseSortedPositions[0]); 
  				
                final Calendar c = Calendar.getInstance();
                final String IDDETALLE = itemList.get("IDDETALLE").toString(); 
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // FECHA PICKER
                DatePickerDialog dpd = new DatePickerDialog(ctx,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                            	
	                            	CharSequence strDate = null;
	                                Time chosenDate = new Time();        
	                                chosenDate.set(dayOfMonth, monthOfYear, year);
	                                long dtDob = chosenDate.toMillis(true);
	
	                                strDate = DateFormat.format("dd-MM-yyyy", dtDob);
	                            	String fechaNewAplicacion = strDate.toString();
                            	updateCampoString (
                            			dbhelper.TABLE_APLICACIONES_LABORES_DETALLES,
                            			dbhelper.KEY_APDET8_FECHACONTROL, 
                            			fechaNewAplicacion, 
                            			dbhelper.KEY_APDET1_ID,
                            			IDDETALLE);
    			            	lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));
                            }
                        }, mYear, mMonth, mDay);
                    dpd.show();

  			}
  			
  		},true, false);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detalles_labores, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        
        case android.R.id.home:
        	//imgbtn_barra_sincronizar();
            return true; 
             
        case R.id.menu_detalle_labor_add:
			addDialogListaLabores();

            return true;  
            
        case R.id.menu_detalle_labor_edit:

        	if(estatusInteger == 0 ){
	            tl_header.setBackgroundColor(getResources().getColor(R.color.rojo_fuerte));        		
        		lv_detalles_labores.setBackgroundColor(getResources().getColor(R.color.sombra));
	        	ActionBar bar = getActionBar();
	        	bar.setBackgroundDrawable(new ColorDrawable( Color.parseColor("#B40404")));
	        	bar.setDisplayShowTitleEnabled(false); 
	        	bar.setDisplayShowTitleEnabled(true); 
	        	estatusInteger = 1;
	        	
	    		//ESCUCHADORES DE LISTVIEW

	        	lv_detalles_labores.setOnTouchListener(touchListener);
	        	lv_detalles_labores.setOnScrollListener(touchListener.makeScrollListener());  		
    		
        	}else{
        		startActivity(getIntent());
        		finish();
        	}
        	
        	
            return true;  	        	

        case R.id.menu_detalle_labor_guardar:
        	opcMenuGuardarSecuenciaLabores();
            return true;  	
            
        default:
            return super.onOptionsItemSelected(item);
    }

	}
	
	public void llenarEncabazado()
	{
		tv_codnom_cliente   = (TextView)findViewById(R.id.tv_codnom_cliente); 
		tv_codnom_finca 	= (TextView)findViewById(R.id.tv_codnom_finca); 
		tv_codnom_lote		= (TextView)findViewById(R.id.tv_codnom_lote); 
		tv_codnom_fecha     = (TextView)findViewById(R.id.tv_codnom_fecha);
	
		ID 	  = getIntent().getExtras().getString("ID");
		HashMap<String, String> rowData = (HashMap<String, String>) adpLaboresLotes.adaptadorListaLabores(ctx, ID, "").getItem(0); //gets HashMap of a specific row
		
		LLAVE = rowData.get("LLAVE").toString();
		FECHA = rowData.get("FECHA").toString();
		
		CODNOM_CLIENTE = rowData.get("tv_codnomcliente").toString();
		CODNOM_FINCA   = rowData.get("tv_codnomfinca").toString();
		CODNOM_LOTE    = rowData.get("tv_codnomlote").toString();
		CODNOM_FECHA   = rowData.get("tv_fecha").toString();
		
    	tv_codnom_cliente.setText(CODNOM_CLIENTE); 
    	tv_codnom_finca.setText(CODNOM_FINCA); 
    	tv_codnom_lote.setText(CODNOM_LOTE);
    	tv_codnom_fecha.setText(CODNOM_FECHA);
	}
	
	public void addNuevoDetalle(final String IDLABOR, final String zafra){
		Log.d("ADD ACTIVIDADES"," 1 "); 
    
		final ListView dialodListLabores = new ListView(this);
		final Spinner tipoReporte = new Spinner(this);
		
        LinearLayout layout = new LinearLayout(this);
        builder = new AlertDialog.Builder(this);
	    layout.setOrientation(LinearLayout.VERTICAL);
		   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,      
					LinearLayout.LayoutParams.WRAP_CONTENT);

		   layoutParams.setMargins(30, 30, 30, 30);	 

        String[] opciones = null;
        final int []opcionesid  = getArrayTipoAplicacion(IDLABOR);
        if(IDLABOR.equals("1")){
		   opciones		=	new String[]{"Mecanica","Manual"};
        }
        
        if(IDLABOR.equals("2")){
		   opciones		=	new String[]{"Quimico","Manual"};
        }
        
        if(IDLABOR.equals("3")){
		   opciones		=	new String[]{"Falso Medidor","Diatraea","Rata","Aenolamia","Termita","fhyllophaga sp","Chinche de encaje"};
        }

        Toast.makeText(ctx,"labor: "+IDLABOR ,Toast.LENGTH_LONG).show(); 

        dialodListLabores.setAdapter(adpDetallesLabores.adaptadorListaProductos(ctx,""+IDLABOR));
        if (Integer.parseInt(IDLABOR) < 4){
            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, R.layout.row_spinner_tipofalla, opciones);
            tipoReporte.setAdapter(adapterSpinner);
            layout.addView(tipoReporte);
        }

	    layout.addView(dialodListLabores);
		//
	    builder.setView(layout);		
		AlertDialogUpdate = builder.create();

		AlertDialogUpdate.setTitle("Seleccione una Actividad");
		AlertDialogUpdate.setButton(RESULT_OK, "Cancel", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
                // Cancelado no hacemos nada.

             }
       });
		AlertDialogUpdate.show();
        final Intent i= new Intent(this, ListaSecuenciaLabores.class); 
        dialodListLabores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	
	    	   @Override
	    	   public void onItemClick(AdapterView<?> arg0, View vista,
	    	     int posicion, long arg3) {
	    		    AlertDialogUpdate.cancel();
		    		HashMap<?, ?> itemList = (HashMap<?, ?>) dialodListLabores.getItemAtPosition(posicion);

		            Log.i("TIPO LABOR", "LABOR: "+tipoReporte.getSelectedItemPosition());
		            if (Integer.parseInt(IDLABOR) < 4){
	
		            		dialogCampoRangoDosis(itemList.get("IDPRODUCTO").toString(), Integer.toString(opcionesid[tipoReporte.getSelectedItemPosition()]), itemList.get("tv_codnom_producto").toString(), itemList.get("DOSIS_DESDE").toString(), itemList.get("DOSIS_HASTA").toString(), itemList.get("tv_unidadmedida").toString(), zafra);

		            }else{
			            if(insertNewDetalle(itemList.get("IDPRODUCTO").toString(), ID, "0", "0", FECHA, zafra)){
			            	lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));
			            }		            	
		            }	
	    	   }
	    	  });	
	   
	}
	
	public int[] getArrayTipoAplicacion(String idLabor){
		int []opcionesid  = null;
        if(idLabor.equals("1")){
		   opcionesid	=	new int[]{1,2};   
        }
        
        if(idLabor.equals("2")){
		   opcionesid	=	new int[]{4,5};
        }
        
        if(idLabor.equals("3")){
		   opcionesid	=	new int[]{6,7,8,9,10,12,13};  
        }
        return opcionesid;
	}
	
	
	private void dialogCampoRangoDosis(final String IDPRODUCTO,final String esHoraExtra, String nombreProducto, String DOSIS_DESDE, String DOSIS_HASTA, String UNIDAD_MEDIDA,final String zafra)
	{	
		   final AlertDialog.Builder alert = new AlertDialog.Builder(this);  
	       alert.setMessage("Digite la dosis DESDE: "+DOSIS_DESDE+" HASTA: "+DOSIS_HASTA+" "+UNIDAD_MEDIDA);  
	       
	       SeekBar seek = new SeekBar(this);
	       final EditText resultado = ClassConfig.txtCapturaNumDecimal(ctx, "0.0", "");//new EditText(this);
	       seek.setMax(Integer.parseInt(DOSIS_HASTA)*100);

	       seek.setOnSeekBarChangeListener(
	                new OnSeekBarChangeListener() {
	                	double progress = 0;
	                      @Override
	                    public void onProgressChanged(SeekBar seekBar, 
	                    int progresValue, boolean fromUser) {
	                    	  double value = ((float)progresValue / 100.0);
	                      progress = value;
	                      resultado.setText(""+progress);
	                    }
	                    @Override
	                    public void onStartTrackingTouch(SeekBar seekBar) {
	                      // touching the seekbar
	                    }
	                    @Override
	                    public void onStopTrackingTouch(SeekBar seekBar) {

	                    }
	                  
	                });
	           
	       alert.setTitle(nombreProducto);
	       
	       LinearLayout layout = new LinearLayout(this);
	        builder = new AlertDialog.Builder(this);
		    layout.setOrientation(LinearLayout.VERTICAL);
		    

		   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,      
					LinearLayout.LayoutParams.WRAP_CONTENT);

		   layoutParams.setMargins(30, 30, 30, 30);	 
			
		   resultado.setTextSize(25);
		   
		   
		   layout.addView(resultado, layoutParams);
		   

		   layout.addView(seek);	       
		       
		   alert.setView(layout);
       
	       alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {
	       	      	 
			            if(insertNewDetalle(IDPRODUCTO, ID, esHoraExtra, resultado.getText().toString(), FECHA, zafra)){

			            	lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));
			            }

		            return;
		            	
	             }
	       }); 
	       alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {
	
		             return;
		            	
	             }
	       }); 
	       alert.show();		
			
	}

	
	public void addDialogListaLabores(){
		Log.d("ADD ACTIVIDADES", " 1 ");
    
		final ListView dialodListLabores = new ListView(this);   

        LinearLayout layout = new LinearLayout(this);
        builder = new AlertDialog.Builder(this);
	    layout.setOrientation(LinearLayout.VERTICAL);
		final String[] opciones =	new String[]{"2015-2016","2016-2017","2017-2018"};

		final Spinner spinnerControl = ClassConfig.spinnerControl(ctx,opciones);

        dialodListLabores.setAdapter(adaptadorListaLabores());
		layout.addView(spinnerControl);
	    layout.addView(dialodListLabores);	       
	       
	    builder.setView(layout);		
		AlertDialogUpdate = builder.create();

		AlertDialogUpdate.setTitle("Seleccione una Actividad");
		AlertDialogUpdate.setButton(RESULT_OK, "Cancel", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
                // CANCELADO NO HACEMOS NADA. 
             }
       });
		AlertDialogUpdate.show();
        final Intent i= new Intent(this, ListaSecuenciaLabores.class); 
        dialodListLabores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	
	    	   @Override
	    	   public void onItemClick(AdapterView<?> arg0, View vista,
	    	     int posicion, long arg3) {
	    		    AlertDialogUpdate.cancel();
		    		HashMap<?, ?> itemList = (HashMap<?, ?>) dialodListLabores.getItemAtPosition(posicion);
		    		addNuevoDetalle(itemList.get("ID").toString(),""+opciones[spinnerControl.getSelectedItemPosition()]);

	    	   }
	    	  });	   
	}
	
	public SimpleAdapter adaptadorListaLabores()
	{
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] {
				"tv_numeroCorrelativo",
				"tv_codnomtipolabor", 
				};
		int[] to = new int[] {
				R.id.tv_numeroCorrelativo, 
				R.id.tv_codnom_producto,
				};

        db = dbhelper.getReadableDatabase();
        
        Cursor c2 = db.rawQuery("SELECT "
        		+" DISTINCT ( "+dbhelper.KEY_PROD8_NOMCONTROL +" ), "
        		+dbhelper.KEY_PROD4_TPO_CONTROL +" "
        		+ " FROM "+dbhelper.TABLE_PRODUCTOS_LABORES+" ORDER BY "+dbhelper.KEY_PROD4_TPO_CONTROL, null);
        int contador = 1;
        if(c2.moveToFirst()){
            do{
	  			HashMap<String, String> map = new HashMap<String, String>();	

				map.put("tv_numeroCorrelativo"	, c2.getString(1) +" - ");
				map.put("tv_codnomtipolabor"	, c2.getString(0));
				map.put("ID"                    , c2.getString(1));
				

				mylist.add(map); 
				contador++;
            }while(c2.moveToNext());
        }
		SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.rows_lista_labores, from, to);
        c2.close();
        db.close();	
        return adapter;
	}
	
	public boolean insertNewDetalle(String IDPRODUCTO, String IDENCABEZADO, String TPO_APLICACION, String DOSIS, String FECHA, String zafra){
		try{
        db = dbhelper.getReadableDatabase();
        db.execSQL("INSERT INTO "+dbhelper.TABLE_APLICACIONES_LABORES_DETALLES
        		+" ( "
				+ dbhelper.KEY_APDET2_IDECABEZADO		+ " ," 	
				+ dbhelper.KEY_APDET3_TPO_APLICACION 	+ " ,"
				+ dbhelper.KEY_APDET4_DOSIS				+ " ," 		
				+ dbhelper.KEY_APDET5_CODPROCUCTO		+ " ," 		
				+ dbhelper.KEY_APDET6_NOMPROCUCTO		+ " ," 		
				+ dbhelper.KEY_APDET7_TPO_CONTROL		+ " ," 	
				+ dbhelper.KEY_APDET8_FECHACONTROL		+ " ," 		    
				+ dbhelper.KEY_APDET9_PRESENTACION 		+ " ,"
				+ dbhelper.KEY_APDET10_COD_TPOCONTROL   + " ,"
				+ dbhelper.KEY_APDET11_LLAVE_DET        + " ,"
				+ dbhelper.KEY_APDET12_ZAFRA        	+ "  "
        		+" ) SELECT "
        		+ "  "+IDENCABEZADO						+ "  ," 	        			
        		+ " '"+TPO_APLICACION	 				+ "' ," 	
        		+ " '"+DOSIS							+ "' ," 	
        		+ "  "+dbhelper.KEY_PROD2_CODPROCUCTO	+ "  ," 	
        		+ "  "+dbhelper.KEY_PROD3_NOMPROCUCTO	+ "  ," 	
        		+ "  "+dbhelper.KEY_PROD8_NOMCONTROL	+ "  ," 	
        		+ " '"+FECHA	                    	+ "' ," 	
        		+ "  "+dbhelper.KEY_PROD5_PRESENTACION	+ "  ," 	
        		+ "  "+dbhelper.KEY_PROD4_TPO_CONTROL	+ "  ,"
        		+ " '0', "
				+ " '"+zafra							+ "'  "
				+" FROM "+dbhelper.TABLE_PRODUCTOS_LABORES+" WHERE "+dbhelper.KEY_PROD1_ID+" = "+IDPRODUCTO+" ");
        db.close();
        
		}catch (Exception  e){
		      //throw new RuntimeException(e);
			return false;
		}
        return true;
	}
	private void cuadroAlertaBorrar(String mensajeAlert, String tituloAlert, String tituloButtonAceptar,final int idRegistro)
	{	
		   AlertDialog.Builder alert = new AlertDialog.Builder(this);  
	       alert.setMessage(mensajeAlert);      
	           
	       alert.setTitle(tituloAlert);
       
	       alert.setPositiveButton(tituloButtonAceptar, new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 deleteCampoTabla(dbhelper.TABLE_APLICACIONES_LABORES_DETALLES, dbhelper.KEY_APDET1_ID, ""+idRegistro);
	       	      	 lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));

		            return;
		            	
	             }
	       }); 
	       alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {
	
		             return;
		            	
	             }
	       }); 
	       alert.show();		
			
	}
	
	public void deleteCampoTabla (String TABLA, String CAMPO, String FILTRO)
	{
        db = dbhelper.getReadableDatabase();
        db.execSQL("DELETE FROM "+TABLA+" WHERE "+CAMPO+" = "+FILTRO);     //  
        db.close();
	}
	
	public void updateCampoString (String TABLA, String CAMPO, String VALUE, String IDCAMPO, String IDVALUE){
        db = dbhelper.getReadableDatabase();
        db.execSQL("UPDATE "+TABLA+" SET "+CAMPO+" = '"+VALUE+"' WHERE "+IDCAMPO+" = "+IDVALUE);  
        db.close();      	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
	        Intent i= new Intent(this, ListaSecuenciaLabores.class);

			startActivity(i);
			finish();
			return true;                                           		
		}
	    return super.onKeyDown(keyCode, event);
	}
	
	//clase que se ejecuta mientras se ejecuta la consulta el web service y el  array
	private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {

		Context ctx;
		int numActividad;
		
		
		public MiTareaAsincronaDialog(Context ctx, int numActividad)
		{
			this.ctx = ctx;
			this.numActividad = numActividad;

		}
		
		 
	    @Override
	    protected Boolean doInBackground(Void... params) 
	    {   	
	    	
	    	if (ClassInicioApp.addNuevaRequisicion(Integer.parseInt(ID))){ 
	    		return true;
	    	}else{
	    		return false;
	    	}
	    }

	    @Override
	    protected void onProgressUpdate(Integer... values) 
	    {
	        int progreso = values[0].intValue();

	        pDialog.setProgress(progreso);
	    }

	    @Override
	    protected void onPreExecute() 
	    {
	        pDialog.setOnCancelListener(new OnCancelListener() 
	        {
	        @Override
		        public void onCancel(DialogInterface dialog) {
		            MiTareaAsincronaDialog.this.cancel(true);
		        }
	        });
	        pDialog.setProgress(0);
	        pDialog.setMax(5000);
	        pDialog.show();	    	
	    }

	    @Override
	    protected void onPostExecute(Boolean result) 
	    {
	        if(result)
	        {
	            pDialog.dismiss();
	            Toast.makeText(this.ctx, "Requisicion guardada exitosamente!",Toast.LENGTH_SHORT).show();
	     		Log.d("ClienteWeb =","onPostExecute, BIEN Tarea de descarga de secuencia finalizada exitosamente"); 
	  	        lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));

	             
	        }else{
	        	pDialog.dismiss();
	            Toast.makeText(this.ctx, "No se pudo guardar la Requisicion",Toast.LENGTH_SHORT).show();            	
	     		Log.d("ClienteWeb =","onPostExecute, ERROR Tarea de descarga no fue completada exitosamente");                
	     		lv_detalles_labores.setAdapter(adpDetallesLabores.adaptadorListaDetalles(ctx,ID));
	        	
	        }
	    }

	    @Override
	    protected void onCancelled() {
	        Toast.makeText(this.ctx, "Tarea cancelada!",
	        Toast.LENGTH_SHORT).show();
	    
	    }

	}	
	
	public void opcMenuGuardarSecuenciaLabores(){
        if (ClassConfig.verificaConexion(this)){
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Iniciando Tarea....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);		
	       
			new MiTareaAsincronaDialog(this,1).execute();
        }else{
            Toast.makeText(this, "Verifica tu conexion a Internet",Toast.LENGTH_SHORT).show();	        	

        }
	}
	
}

