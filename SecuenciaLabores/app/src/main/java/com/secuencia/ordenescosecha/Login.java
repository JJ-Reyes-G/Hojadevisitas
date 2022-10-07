package com.secuencia.ordenescosecha;

import test.Droidlogin.library.Httppostaux;

import com.secuencia.database.DatabaseHandler_;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	//String ws_datosUser = "";//10.1.1
	//String ws_datosActivos = "http://190.86.180.200:8080/wsmoviles/ws_reqmovil/reqmovil_ws.php/bajarActivos";
		
	Httppostaux post;
	SQLiteDatabase db;
	DatabaseHandler_ dbhelper;
	EditText txtuser, txtpass;
	
	Context ctx;
	Activity act;
	private ProgressDialog pDialog;
	ClassDescargarInicioApp ClassInicioApp;
	ClassConfig ClassConfig;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ctx = this;
		
		post=new Httppostaux();	
		dbhelper = new DatabaseHandler_(this);	
		txtuser = (EditText) findViewById(R.id.txtuser);
		txtpass = (EditText) findViewById(R.id.txtpass);
        ClassInicioApp = new ClassDescargarInicioApp(this);
        ClassConfig = new ClassConfig();
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void btnLogin (View v)
	{		
		int result = verificarIfcuenta(dbhelper.TABLE_ADMINITRACION);
		String user = txtuser.getText().toString();
		String pass = txtpass.getText().toString();
		validarCuenta(user , pass);

		
	}
	public void validarCuenta(String user , String pass)
	{
		if (user.equals("") || pass.equals(""))
		{
			Toast.makeText(this, "Los campos usuario y contraseña son requeridos", Toast.LENGTH_SHORT).show();
		}else{
	        db = dbhelper.getWritableDatabase();
	        String nombreUsuario = "";
	        Cursor c = db.rawQuery("SELECT " +
	        		dbhelper.KEY_ADMIN1_ID				+", " +
	        		dbhelper.KEY_ADMIN3_PASSUSER		+", " +
	        		dbhelper.KEY_ADMIN2_NOMBREUSER		+", " +
	        		dbhelper.KEY_ADMIN8_SINC_AUTO		+", " +
	        		dbhelper.KEY_ADMIN9_TIPO_PERMISO	+"  " +
	        		" FROM " +dbhelper.TABLE_ADMINITRACION +
	        		" WHERE "+dbhelper.KEY_ADMIN1_ID		+" = "+user+" " +
	        		" AND   "+dbhelper.KEY_ADMIN3_PASSUSER	+" = '"+pass+"'" , null);  
	        if(c.moveToFirst()){
	            do{
	            	nombreUsuario = c.getString(2);
	            	Toast.makeText(this, "Bienvenido "+txtuser.getText().toString()+nombreUsuario, Toast.LENGTH_SHORT).show();
	            	
	            	ClassConfig.setKey_codTecnico(txtuser.getText().toString());
	            	ClassConfig.KEY_SINC_AUTOMATICA = c.getString(3);
	            	ClassConfig.KEY_TIPO_PERMISO 	= c.getString(4);
	            	ClassConfig.KEY_PASSWORD		= c.getString(1);
	            	txtuser.setText("");
	            	txtpass.setText("");
					//Intent i= new Intent(this, ActividadPrincipal.class);

					Intent i= new Intent(this, ActListaVisitas.class);
					startActivity(i); 
	            	
	            }while(c.moveToNext());
	        }else{
	        		//Toast.makeText(this, "Acceso incorrecto Credenciales incorrectas", Toast.LENGTH_SHORT).show();
	    	        pDialog = new ProgressDialog(this);
	    	        pDialog.setMessage("Iniciando configuracion y descarga de informacion....");
	    	        pDialog.setIndeterminate(false);
	    	        pDialog.setCancelable(true);
	    			new MiTareaAsincronaDialog(this,0, 2).execute(); 
	        }
	        c.close();
	        db.close();
		}
	}
	
	public int verificarIfcuenta(String tabla)
	{
		//si devuelve -1 es que ya fue descargada la informacion
        db = dbhelper.getWritableDatabase();
        int numRows = 0;
        int numCountt = 0;
        Cursor c = db.rawQuery("SELECT COUNT(*), countt FROM "+tabla , null);  
        if(c.moveToFirst())
        {
            do{
            	numRows = Integer.parseInt(c.getString(0));
            	if (c.getString(1) != null){
            		numCountt = Integer.parseInt(c.getString(1));
            	}else{
            		numCountt = 0;
            	}
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        if(numRows > 0){
	        if (numRows  == numCountt){
	        	return -1;
	        }else{
	        	return numRows;
	        }
        }else{
        	return numRows;
        }
	}

	
	//clase que se ejecuta mientras se ejecuta la consulta el web service y el  array
	private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {

		Context ctx;
		int resulCountTabla;
		int numActividad;
		//numActividad numero de actividad 
		public MiTareaAsincronaDialog(Context ctx, int resulCountTabla, int numActividad)
		{
			this.ctx = ctx;
			this.resulCountTabla = resulCountTabla;
			this.numActividad = numActividad;

		}	
		 
	    @Override
	    protected Boolean doInBackground(Void... params) 
	    {   	
			switch (numActividad) {

				case 1:
					return false;
					
				case 2:	
					//if (ClassInicioApp.descargarConfiguracionDispositivo(txtuser.getText().toString(), txtpass.getText().toString())){ 
					if (ClassInicioApp.iniciarDescargaInicioDispositivo(txtuser.getText().toString(), txtpass.getText().toString())){ 
				           
						return true; 	
					}else{  
						if(isCancelled())
						{   
							Log.d("ClienteWeb: ","doInBackground, Actividad cancelada TRUE");
							return false;
						}	
						Log.d("ClienteWeb: ","doInBackground, Actividad cancelada FALSE");	
						return false; 	     	          	  
					} 
			default:
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
	            Toast.makeText(this.ctx, "Descarga de credenciales correctas!",Toast.LENGTH_SHORT).show();
	     		Log.d("ClienteWeb =","onPostExecute, BIEN Tarea de descarga de secuencia finalizada exitosamente"); 
	     		validarCuenta(txtuser.getText().toString() , txtpass.getText().toString());
             
	        }else{
	        	pDialog.dismiss();
	            Toast.makeText(this.ctx, "No se pudiron comprobar sus credenciales",Toast.LENGTH_SHORT).show();            	
	     		Log.d("ClienteWeb =","onPostExecute, ERROR Tarea de descarga no fue completada exitosamente");                
               
	        	
	        }
	    }

	    @Override
	    protected void onCancelled() {
	        Toast.makeText(this.ctx, "Tarea cancelada!",
	        Toast.LENGTH_SHORT).show();
	    
	    }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}