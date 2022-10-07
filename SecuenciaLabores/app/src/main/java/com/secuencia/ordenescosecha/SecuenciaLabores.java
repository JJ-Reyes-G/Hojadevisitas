package com.secuencia.ordenescosecha;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import test.Droidlogin.library.Httppostaux;

import com.secuencia.database.DatabaseHandler_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SecuenciaLabores extends Activity implements OnQueryTextListener{
      
    ClassDescargarInicioApp CDesInicioApp;
	ListView lv_ClientesOrdenes;
	SQLiteDatabase db;
	DatabaseHandler_ dbhelper;
	AlertDialog d, ocacionesUpdate;
	AlertDialog.Builder builder;
	DatabaseHandler_ dbhandler;
	ArrayList<String> datos; 
	SimpleAdapter adapterlistRegalos, adapterlistOcaciones;
	ListView myList;
	String nombreLista;
	//ProcesosGenerales procesosGenerales;
    ImageButton ibn_menu;
    Httppostaux post;    
    private ProgressDialog pDialog;
    static int idListOrden = 0;
    static int codigoTarea = 0; //variable encargada de asignarle a la clase que se encarga de la descarga la tarea a realizar
	static String corrQuema = "";
	static String codProveedor = "";
	static String tipoLista = "";
	static String numeroZona = "";
	String tipoActividad = "";
	String fechaNewAplicacion ="";
	ClassConfig ClassConfig;
	Context ctx = this;
	
	private SearchView mSearchView;
	SimpleAdapter simpleAdaptador;
	private int mYear, mMonth, mDay, mHour, mMinute;
	AdapterLaboresLotes adpLaboresLotes = new AdapterLaboresLotes();
	AdapterSecuenciaLotes adpSecuenciaLotes = new AdapterSecuenciaLotes();
	AdapterProveedores  	adpProveedores = new AdapterProveedores();
	AdapterFincas			adpFincas = new AdapterFincas();
	AdapterVisitasFincas 	adpVisitas = new AdapterVisitasFincas();
	AdapterTipoVisitas 		adpTipoVisitas = new AdapterTipoVisitas();
	AdapterMotivoVisitas 	adpMotivoVisitas = new AdapterMotivoVisitas();
	AdapterResultadoVisitas adpResultadoVisitas = new AdapterResultadoVisitas();
	AdapterQuienRecibe 		adpQuienRecibe = new AdapterQuienRecibe();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secuencia_labores);
		
		CDesInicioApp = new ClassDescargarInicioApp(this);
		ClassConfig   = new ClassConfig();
        dbhelper      = new DatabaseHandler_(this);

		numeroZona 	  = getIntent().getExtras().getString("numeroZona");
		tipoActividad = getIntent().getExtras().getString("tipoActividad");
        
        lv_ClientesOrdenes = (ListView)findViewById(R.id.lv_SecuenciaLabores);
		tipoLista = "PROVEEDORES";
		lv_ClientesOrdenes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		simpleAdaptador = adpProveedores.AdapterProveedores(ctx,"",numeroZona);

		lv_ClientesOrdenes.setAdapter(simpleAdaptador);

		// AL PRECIONAR UNA DE LAS FILAS DE LA LISTA
		lv_ClientesOrdenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View vista,
									int posicion, long arg3) {
				HashMap<?, ?> itemList = (HashMap<?, ?>) lv_ClientesOrdenes.getItemAtPosition(posicion);
				tipoLista = itemList.get("tipoLista").toString();
				if(itemList.get("tipoLista").toString() == "PROVEEDORES"){
					codProveedor = itemList.get("codProveedor").toString();
					lv_ClientesOrdenes.setAdapter(adpFincas.AdapterFincas(ctx, "", numeroZona, codProveedor));
				}else if(itemList.get("tipoLista").toString() == "FINCAS"){

					final VisitaCampo visitaCampo  = new VisitaCampo();
					visitaCampo.setCodFinca(itemList.get("codFinca").toString());
					visitaCampo.setDescripcionFinca(itemList.get("nomFinca").toString());
					visitaCampo.setNombreProveedor(itemList.get("nomProveedor").toString());
					visitaCampo.setCodProveedor(itemList.get("nomProveedor").toString());

					MostrarCalendario(visitaCampo);
				}else{
					Toast.makeText(ctx, "LOTES", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	public boolean onQueryTextChange(String newText) {

	     lv_ClientesOrdenes.setAdapter(null);
		 if(tipoLista=="PROVEEDORES"){
			 lv_ClientesOrdenes.setAdapter(adpProveedores.AdapterProveedores(ctx, newText, numeroZona));
		 }else if(tipoLista=="FINCAS"){
			 lv_ClientesOrdenes.setAdapter(adpFincas.AdapterFincas(ctx, newText, numeroZona, codProveedor));
		 }
		 //lv_ClientesOrdenes.setAdapter(simpleAdaptador);

		 return true;
	}
	public boolean onQueryTextSubmit(String newText) {
		/*
		 simpleAdaptador.notifyDataSetChanged();
		 lv_ClientesOrdenes.setAdapter(null);
		 lv_ClientesOrdenes.setAdapter(simpleAdaptador);
		*/
		lv_ClientesOrdenes.setAdapter(null);
		if(tipoLista=="PROVEEDORES"){
			lv_ClientesOrdenes.setAdapter(adpProveedores.AdapterProveedores(ctx, newText, numeroZona));
		}else if(tipoLista=="FINCAS"){
			lv_ClientesOrdenes.setAdapter(adpFincas.AdapterFincas(ctx, newText, numeroZona, codProveedor));
		}
		 
		 return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.secuencia_labores, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("Search...");
        mSearchView.setOnQueryTextListener(this);
		
		return true;

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
        switch (item.getItemId()) {
        
	        case android.R.id.home:
	            return true;
	             
	        case R.id.menu_secuencia_labores_sincronizar:
	    		if (ClassConfig.verificaConexion(this)==false) {
	    		    Toast.makeText(getBaseContext(), "Verifica tu conexion a Internet.", Toast.LENGTH_SHORT).show();
	    		}else{
					descargarLotesZona();

	    		}
	            return true;
			case R.id.menu_hojavisita_nuevo_cliente:
				final VisitaCampo visitaCampo  = new VisitaCampo();
				visitaCampo.setCodFinca("0");
				visitaCampo.setDescripcionFinca("0");
				visitaCampo.setNombreProveedor("VISITA NUEVO CLIENTE");
				visitaCampo.setCodProveedor("NUEVO_CLIENTE");
				MostrarCalendario(visitaCampo);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
	}
	

	//---------------------Menu contextual--------------------------
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {	
		super.onCreateContextMenu(menu, v, menuInfo);
    	
		MenuInflater inflater = getMenuInflater();
		
		if(v.getId() == R.id.lv_SecuenciaLabores)	
		{
			AdapterView.AdapterContextMenuInfo info = 
					(AdapterView.AdapterContextMenuInfo)menuInfo;
            HashMap<?, ?> itemList = (HashMap<?, ?>) lv_ClientesOrdenes.getItemAtPosition(info.position);                   
			
            menu.setHeaderTitle(itemList.get("secuencia")+" --/-- "+itemList.get("listaPrecio"));
            inflater.inflate(R.menu.menu_ctx_seclabores_list, menu);					
			
		}

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		HashMap<?, ?> itemList3 = (HashMap<?, ?>) lv_ClientesOrdenes.getItemAtPosition(info.position);
		switch (item.getItemId()) {

			case R.id.CtxLstOpcAddVisita:
				//MostrarCalendario(itemList3);
				return super.onContextItemSelected(item);
				
			default:
				return super.onContextItemSelected(item);
		}
    }
    //END MENU CONTEXTUAL
    
	public void imgbtn_barra_sincronizar(String numeroZona)
	{	

	         pDialog = new ProgressDialog(ctx);
	         pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);	                 
                
	         pDialog.setMessage("Iniciando Tarea....");
	         pDialog.setIndeterminate(false);
	         pDialog.setCancelable(false);
			 this.numeroZona = numeroZona;
	                         
	         new MiTareaAsincronaDialog().execute();
	}
	
	public void alertMensaje()
	{	
		   AlertDialog.Builder alert = new AlertDialog.Builder(this);  
	       alert.setMessage("No se pudo sincronizar. Intente nuevamente y si el problema persiste informar el inconveniente");  	       
	           
	       alert.setTitle("Error!");  
       
	       alert.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {

	                 pDialog = new ProgressDialog(ctx);
	                 pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);	                 
	                 pDialog.setMessage("Iniciando Tarea....");
	                 pDialog.setIndeterminate(false);
	                 pDialog.setCancelable(false);		
	                 new MiTareaAsincronaDialog().execute();	
		             return;
		            	
	             }
	       }); 
	       alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {  
	             public void onClick(DialogInterface dialog, int whichButton) {  
	                // Cancelado no hacemos nada.  
	             }
	       });  
	       alert.show();		
			
	}
	
	//clase que se ejecuta mientras se ejecuta la consulta el web service y el  array
	private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {
		 
        @Override
        protected Boolean doInBackground(Void... params) {
        
		     	
	    		if (CDesInicioApp.ws_bajarSecuenciaParaLaboresPorZona(ClassConfig.getKey_codTecnico(), numeroZona)){
		               
	    			return true; 	
	    		}else{ 
	    			if(isCancelled()){
		    			Log.d("ClienteWeb: ","doInBackground, Actividad cancelada TRUE");
		    			return false;
	    			}
	    			
	    			Log.d("ClienteWeb: ","doInBackground, Actividad cancelada FALSE");	
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

            pDialog.setOnCancelListener(new OnCancelListener() {
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
         		Log.d("Ordenes =","onPostExecute, BIEN Tarea de descarga de secuencia finalizada exitosamente");                

				lv_ClientesOrdenes.setAdapter(adpProveedores.AdapterProveedores(ctx, "", numeroZona));
            }else{
            	pDialog.dismiss();
            	alertMensaje();
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

	public String getFecha(){
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
		
	}

	/*
	public void deleteAllTabla (String nombreTabla){
	        db = dbhelper.getReadableDatabase();
	        db.execSQL("DELETE FROM "+nombreTabla);  
	        db.close();	
	}
	*/
	public void MostrarCalendario( final VisitaCampo visitaCampo)
	{
        final Calendar c = Calendar.getInstance();
        mYear  = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay   = c.get(Calendar.DAY_OF_MONTH);
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
                      
                    	fechaNewAplicacion = strDate.toString();
                    	
                        final Calendar c = Calendar.getInstance();
						final Intent i= new Intent(ctx, DetallesLabores.class);

						MostrarHora(fechaNewAplicacion, visitaCampo);


                    }
                }, mYear, mMonth, mDay);
		    dpd.setTitle("Seleccione la fecha");
		dpd.show();
	}

	public void MostrarHora (final String fechaNewAplicacion, final VisitaCampo visitaCampo){
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);
		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(SecuenciaLabores.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
				addNuevaVisita(fechaNewAplicacion, selectedHour + ":" + selectedMinute, visitaCampo);
			}
		}, hour, minute, true);//Yes 24 hour time
		mTimePicker.setTitle("Selecciona la Hora");
		mTimePicker.show();
	}

	public void addNuevaVisita(final String FECHA, final String Hora,  final VisitaCampo visitaCampo){
		final AlertDialog.Builder builder;

		final String[] opcionesZafra           = new String[]{"2021-2022", "2022-2023"};

		final Spinner spinnerControlZafra 	   = ClassConfig.spinnerControl(ctx,opcionesZafra);
		final Spinner spinnerTipoVisita 	   = new Spinner(ctx);
		final Spinner spinnerMotivosVisita     = new Spinner(ctx);
		final Spinner spinnerResultadosVisita  = new Spinner(ctx);

		spinnerTipoVisita.setAdapter(adpTipoVisitas.adapterTipoVisitas(ctx));
		spinnerMotivosVisita.setAdapter(null);
		spinnerResultadosVisita.setAdapter(null);

		LinearLayout layout = new LinearLayout(this);
		builder = new AlertDialog.Builder(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(30, 30, 30, 30);
		layout.addView(spinnerControlZafra);
		layout.addView(ClassConfig.tvStyleTextView(ctx, "Tipo visita:"));
		layout.addView(spinnerTipoVisita);
		layout.addView(ClassConfig.tvStyleTextView(ctx, "Motivo visita::"));
		layout.addView(spinnerMotivosVisita);
		layout.addView(ClassConfig.tvStyleTextView(ctx, "Resulatdo visita:"));
		layout.addView(spinnerResultadosVisita);

		builder.setView(layout);

		final Intent i= new Intent(ctx, ActListaVisitas.class);

		builder.setView(layout);
		builder.setMessage("LLenar hoja, Fecha:" + FECHA);
		builder.setPositiveButton("SIGUIENTE", null);
		builder.setNegativeButton("CANCELAR", null);

		final AlertDialog mAlertDialog = builder.create();

		mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						HashMap<?, ?> itemListTipoVisita 		= (HashMap<?, ?>) spinnerTipoVisita.getItemAtPosition(spinnerTipoVisita.getSelectedItemPosition());
						HashMap<?, ?> itemListMotivoVisita 		= (HashMap<?, ?>) spinnerMotivosVisita.getItemAtPosition(spinnerMotivosVisita.getSelectedItemPosition());
						HashMap<?, ?> itemListResultadoVisita 	= (HashMap<?, ?>) spinnerResultadosVisita.getItemAtPosition(spinnerResultadosVisita.getSelectedItemPosition());

							visitaCampo.setCodLote("0");
						/*
							visitaCampo.setCodFinca(itemLote.get("codFinca").toString());
							visitaCampo.setDescripcionFinca(itemLote.get("nomFinca").toString());
							visitaCampo.setNombreProveedor(itemLote.get("nomProveedor").toString());
							visitaCampo.setCodProveedor(itemLote.get("nomProveedor").toString());
						*/
							visitaCampo.setDescMotivoVisita(itemListMotivoVisita.get("tv_descripcion_row").toString());
							visitaCampo.setFecha(FECHA);
							visitaCampo.setZafra("" + opcionesZafra[spinnerControlZafra.getSelectedItemPosition()]);
							visitaCampo.setDescTipoVisita(itemListTipoVisita.get("tv_descripcion_row").toString());
							visitaCampo.setCodTipoVisita(itemListTipoVisita.get("codTipoVisita").toString());
							visitaCampo.setCodMotivoVisita(itemListMotivoVisita.get("codMotivoVisita").toString());
							visitaCampo.setCosResultadoVisita(itemListResultadoVisita.get("codResultadoVisita").toString());

							visitaCampo.setHora(Hora);

						siguiente_addNuevaVisita(visitaCampo, mAlertDialog);

					}
				});

				Button b2 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
				b2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						mAlertDialog.dismiss();
					}
				});
			}
		});


		spinnerTipoVisita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				HashMap<?, ?> itemListTipoVisita = (HashMap<?, ?>) spinnerTipoVisita.getItemAtPosition(spinnerTipoVisita.getSelectedItemPosition());
				spinnerMotivosVisita.setAdapter(adpMotivoVisitas.adapterMotivoVisitas(ctx, itemListTipoVisita.get("codTipoVisita").toString()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		spinnerMotivosVisita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				HashMap<?, ?> itemListMotivoVisita = (HashMap<?, ?>) spinnerMotivosVisita.getItemAtPosition(spinnerMotivosVisita.getSelectedItemPosition());
				spinnerResultadosVisita.setAdapter(adpResultadoVisitas.adapterResultadoVisitas(ctx, itemListMotivoVisita.get("codMotivoVisita").toString()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		mAlertDialog.show();
	}


	public void siguiente_addNuevaVisita(final VisitaCampo visita, final AlertDialog dialigAnterior){
		final AlertDialog.Builder builder;

		final Spinner spinnerQuienRecibio 	   = new Spinner(ctx);
		spinnerQuienRecibio.setAdapter(adpQuienRecibe.adapterQuienRecibe(ctx));

		final TextView tvRecibeVisita 	 = ClassConfig.tvStyleTextView(ctx, "Quien Recibió:");
		final TextView tvComentario 	 = ClassConfig.tvStyleTextView(ctx, "Comentario");
		final EditText txtComentario 	 = ClassConfig.txtCapturaTexto(ctx, 6,"Recomendacion o comentario");
		final EditText txtNombreContacto = ClassConfig.txtCapturaTexto(ctx, 3,"Digite el encargado");

		txtComentario.setText(visita.getComentario());
		LinearLayout layout = new LinearLayout(this);
		builder = new AlertDialog.Builder(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(30, 30, 30, 30);

		layout.addView(tvRecibeVisita);
		layout.addView(spinnerQuienRecibio);
		layout.addView(txtNombreContacto);
		layout.addView(tvComentario);
		layout.addView(txtComentario);
		builder.setView(layout);

		final Intent i= new Intent(ctx, ActListaVisitas.class);

		builder.setView(layout);
		builder.setMessage("Completar hoja de visita:" );
		builder.setPositiveButton("GUARDAR", null);
		builder.setNegativeButton("ATRAS", null);

		final AlertDialog mAlertDialog = builder.create();

		mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						boolean resultadoValida = true;
						HashMap<?, ?> itemListQuienRecibe = (HashMap<?, ?>) spinnerQuienRecibio.getItemAtPosition(spinnerQuienRecibio.getSelectedItemPosition());

						if (txtNombreContacto.getText().toString().equals("")) {
							Toast.makeText(ctx, "Nombre de la persona quien recibe es obligatorio", Toast.LENGTH_SHORT).show();

						} else if (txtComentario.getText().toString().equals("")) {
							Toast.makeText(ctx, "Comentario o recomendacion es obligatorio", Toast.LENGTH_SHORT).show();

						} else {
							visita.setComentario(txtComentario.getText().toString());
							visita.setNomContacto(txtNombreContacto.getText().toString());
							visita.setQuienRecibe(itemListQuienRecibe.get("codQuienRecibe").toString());
							visita.setNumZona(numeroZona);

							if (adpVisitas.insertNewVisita(ctx,
									visita.getCodLote(),
									visita.getDescMotivoVisita(),
									visita.getComentario(),
									visita.getFecha(),
									visita.getNomContacto(),
									visita.getZafra(),
									visita.getDescTipoVisita(),
									visita.getCodTipoVisita(),
									visita.getCodMotivoVisita(),
									visita.getCosResultadoVisita(),
									visita.getQuienRecibe(),
									visita.getHora(),
									visita
							)) {
								startActivity(i);
								finish();
							}

						}

					}
				});

				Button b2 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
				b2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						mAlertDialog.dismiss();
					}
				});
			}
		});




		spinnerQuienRecibio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				HashMap<?, ?> itemListTipoVisita = (HashMap<?, ?>) spinnerQuienRecibio.getItemAtPosition(spinnerQuienRecibio.getSelectedItemPosition());
				//spinnerMotivosVisita.setAdapter(adpMotivoVisitas.adapterMotivoVisitas(ctx,itemListTipoVisita.get("codTipoVisita").toString()));
				if (itemListTipoVisita.get("codQuienRecibe").toString().equals("Proveedor")) {
					txtNombreContacto.setText(visita.getNombreProveedor());
				} else {
					txtNombreContacto.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});



		mAlertDialog.show();
	}











	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:

			Intent i= new Intent(ctx, ActListaVisitas.class);

			startActivity(i);
			finish();
			return true;                                           		
	}
	    return super.onKeyDown(keyCode, event);
	}

	public void descargarLotesZona(){
		Log.i("ADD ACTIVIDADES", " 1 ");

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		final EditText txtNumeroZona = ClassConfig.txtCapturaNumDecimal("numero de Zona","", this);

		Log.i("ADD ACTIVIDADES", " 2 ");

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		layout.addView(txtNumeroZona);
		builder.setView(layout);

		//AlertDialogUpdate.setTitle("Seleccione la Cantidad");
		builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Capturamos el valor del campo de texto
				if ((Double.parseDouble(txtNumeroZona.getText().toString()) > 0.0)) {
					imgbtn_barra_sincronizar(txtNumeroZona.getText().toString());
				} else {
					Toast.makeText(ctx, "Digite un valor correcto", Toast.LENGTH_SHORT).show();
					descargarLotesZona();
				}
			}
		});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//No hacemos nada..

			}
		});

		builder.setTitle("Digite numero de zona a descargar");
		builder.show();

	}

	
	
}
