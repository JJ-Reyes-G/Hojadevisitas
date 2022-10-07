package com.secuencia.ordenescosecha;

import java.util.HashMap;

import com.secuencia.database.DatabaseHandler_;

import android.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ListaSecuenciaLabores extends Activity {
	ListView lv_lista_secuencia_labores;
	SQLiteDatabase db;
	DatabaseHandler_ dbhelper;
	AlertDialog d, ocacionesUpdate;
	Context ctx = this;
	SwipeListViewTouchListener touchListener;
	int estatusInteger = 0;
	AdapterLaboresLotes adpLaboresLotes = new AdapterLaboresLotes();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_secuencia_labores);
		
		//CDesInicioApp = new ClassDescargarInicioApp(this);
        dbhelper      = new DatabaseHandler_(this);
        
        lv_lista_secuencia_labores = (ListView)findViewById(R.id.lv_lista_secuencia_labores);
        lv_lista_secuencia_labores.setAdapter(adpLaboresLotes.adaptadorListaLabores(ctx,"",""));
        
		//DESLIZAR PARA ELIMINAR
		touchListener =new SwipeListViewTouchListener(lv_lista_secuencia_labores, new SwipeListViewTouchListener.OnSwipeCallback() {        
		//SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(lv_lista_secuencia_labores, new SwipeListViewTouchListener.OnSwipeCallback() {
			@Override
			public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
				//Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda

				HashMap<?, ?> itemList = (HashMap<?, ?>) lv_lista_secuencia_labores.getItemAtPosition(reverseSortedPositions[0]); 
				
                Log.i("El item es:", itemList.get("ID").toString());
                cuadroAlertaBorrar("Esta seguro que desea eliminar esta Actividad", "Advertencia", "Eliminar",Integer.parseInt(itemList.get("ID").toString()));
     
			}
					
			@Override
			public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {

			}
					
		},true, false);
		
		//Escuchadores del listView
				
		 // AL PRECIONAR UNA DE LAS FILAS DE LA LISTA
		lv_lista_secuencia_labores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	
	    	   @Override
	    	   public void onItemClick(AdapterView<?> arg0, View vista,
	    	     int posicion, long arg3) {	    		    
		    		HashMap<?, ?> itemList = (HashMap<?, ?>) lv_lista_secuencia_labores.getItemAtPosition(posicion); 

	                Log.i("El item es:", ""+itemList);
	                
			        Intent i= new Intent(ctx, DetallesLabores.class);
			        
					i.putExtra("ID"		, itemList.get("ID").toString());
					
					startActivity(i);
					finish();

	    	   }
	    	  });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_secuencia_labores, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	        switch (item.getItemId()) {
	        
	        case android.R.id.home:
	        	//imgbtn_barra_sincronizar();
	            return true; 
	             
	        case R.id.menu_labores_add:
				addDialogListaLabores();

	            return true;  
	            
	        case R.id.menu_labores_edit:	        	
	        	
	        	if(estatusInteger == 0 ){
	        		lv_lista_secuencia_labores.setBackgroundColor(getResources().getColor(R.color.sombra));
		        	ActionBar bar = getActionBar();
		        	bar.setBackgroundDrawable(new ColorDrawable( Color.parseColor("#B40404")));
		        	bar.setDisplayShowTitleEnabled(false); 
		        	bar.setDisplayShowTitleEnabled(true); 
		        	estatusInteger = 1;
		        	
		    		//ESCUCHADORES DE LISTVIEW
	
		    		lv_lista_secuencia_labores.setOnTouchListener(touchListener);
		    		lv_lista_secuencia_labores.setOnScrollListener(touchListener.makeScrollListener());    		
	    		
	        	}else{
	        		startActivity(getIntent());
	        		finish();
	        	}

	            return true;  	        	
	            
     
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void addDialogListaLabores(){
		Log.d("ADD ACTIVIDADES"," 1 ");//AlertDialog.Builder builder;
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
			i.putExtra("tipoActividad"	, "SECUENCIA_LABORES");
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
		final Intent i= new Intent(this, ListaSecuenciaLabores.class);
		dialodListZonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View vista,
									int posicion, long arg3) {
				AlertDialogUpdate.cancel();
				HashMap<?, ?> itemList = (HashMap<?, ?>) dialodListZonas.getItemAtPosition(posicion);
				Intent i= new Intent(ctx, SecuenciaLabores.class);
				i.putExtra("numeroZona"	, itemList.get("tv_codnom_zona").toString());
				i.putExtra("tipoActividad"	, "SECUENCIA_LABORES");
				startActivity(i);
				finish();
				//addNuevoDetalle(itemList.get("tv_codnom_zona").toString());

			}
		});
	}
	

	
	private void cuadroAlertaBorrar(String mensajeAlert, String tituloAlert, String tituloButtonAceptar,final int idRegistro)
	{	
		   AlertDialog.Builder alert = new AlertDialog.Builder(this);  
	       alert.setMessage(mensajeAlert);      
	           
	       alert.setTitle(tituloAlert);
       
	       alert.setPositiveButton(tituloButtonAceptar, new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 deleteCampoTabla(dbhelper.TABLE_APLICACIONES_LABORES_ENCABEZADO, dbhelper.KEY_APENC1_ID, ""+idRegistro);
	            	 deleteCampoTabla(dbhelper.TABLE_APLICACIONES_LABORES_DETALLES, dbhelper.KEY_APDET2_IDECABEZADO, ""+idRegistro);
	            	 lv_lista_secuencia_labores.setAdapter(adpLaboresLotes.adaptadorListaLabores(ctx,"","")); 

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
        db.execSQL("DELETE FROM "+TABLA+" WHERE "+CAMPO+" = "+FILTRO);  
        db.close();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
	        Intent i= new Intent(ctx, ActividadPrincipal.class);
			startActivity(i);
			finish();
			return true;                                           		
	}
	    return super.onKeyDown(keyCode, event);
	}
	

}
