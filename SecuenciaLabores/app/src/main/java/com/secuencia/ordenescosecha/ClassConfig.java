package com.secuencia.ordenescosecha;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClassConfig extends Application{
	
	public static final String wsIp = "http://intranet.grupolacabana.net:8080/";
	public static final String  carpetaWebService = "wsmoviles/ws_reqmovil/";

	public static final String key_ipserver            				= wsIp+"wsmoviles/ws_reqmovil/reqmovil_ws.php";

	public static final String ws_bajarConfiguracionDispositivo 	= key_ipserver+"/bajarConfiguracionDispositivo";
	public static final String ws_bajarSecuenciaParaLabores 		= wsIp+carpetaWebService+"ws_ordencosecha.php/bajarSecuenciaParaLabores";
	public static final String ws_bajarControlProductosLabores 		= wsIp+carpetaWebService+"ws_ordencosecha.php/bajarControlProductosLabores";
	public static final String ws_addNuevaAplicacionLabores 		= wsIp+carpetaWebService+"ws_ordencosecha.php/guardarNuevaSecuenciaLabores";
	public static final String ws_cambiarPasswordUsuario			= wsIp+carpetaWebService+"reqmovil_ws.php/cambiarPasswordUsuario";
	public static final String ws_bajarSecuenciaParaLaboresPorZona 	= wsIp+carpetaWebService+"ws_ordencosecha.php/bajarSecuenciaParaLaboresPorZona";
	public static final String ws_addNuevaVisitaCampo 				= wsIp+carpetaWebService+"ws_ordencosecha.php/guardarNuevaVisitaCampo";
	public static final String ws_bajarUltimasVisitas 				= wsIp+carpetaWebService+"ws_ordencosecha.php/bajarUltimasVisitas";
	public static final String ws_bajarTipoVisitas 				    = wsIp+carpetaWebService+"ws_ordencosecha.php/bajarTipoVisitas";
	public static final String Hv_HojaVisitas_Lotes_Insert			= wsIp+carpetaWebService+"ws_ordencosecha.php/Hv_HojaVisitas_Lotes_Insert";

	public static final String key_versionApp 	= "4";


	static BlockingQueue<Boolean> blockingQueue;
	/*
	 * VERSION DE APP QUE PERMITE CONTROLAR TIPO DE PERMISOS DE USUARIO
	 * SE LE AGREGO UN BOTON QUE CONTROLA LA SINCRONIZACION AUTOMATICA
	 * SE LE AGREGO UN BOTON QUE PERMITE LA SINCRONIZACION MAS PRACTIVA
	 * SE LE AGREGARA LA IDENTIFICACION EN LA BASE DE DATOS PARA SABER SI EL REGISTRO FUE ACTUALIZADO EN ALGUN MOMENTO POR LA TABLETA
	 * AJUSTE DE BOTON ANULACION DE ORDENES
	 * CAMBIO DE CONTRASEñA EN EL DISPOSITIVO
	 */
	public static final String key_nombreApp 	= "SECUENCIALABORES";
	
	public static String key_codTecnico 		= "";	
	public static String KEY_TIPO_PERMISO 		= "";
	public static String KEY_SINC_AUTOMATICA 	= "";
	public static String KEY_PASSWORD			= "";
	
	//--Variables de session
	
	public String getKey_codTecnico() {
		return key_codTecnico;
	}

	public void setKey_codTecnico(String key_codTecnico) {
		this.key_codTecnico = key_codTecnico;
	}
	
	 public static boolean verificaConexion(Context ctx) {
		    boolean bConectado = false;
		    ConnectivityManager connec = (ConnectivityManager) ctx
		            .getSystemService(Context.CONNECTIVITY_SERVICE);
		    // No slo wifi, tambien GPRS
		    NetworkInfo[] redes = connec.getAllNetworkInfo();
		    // este bucle deberia no ser tan apa
		    for (int i = 0; i < 2; i++) {
		        // Tenemos conexion? ponemos a true
		        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
		            bConectado = true;
		        }
		    }
		    return bConectado;
	 }
	 
	    public static EditText txtCapturaNumDecimal(String hitText, String value, Context ctx){
		       final EditText input = new EditText(ctx);

		       input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		       input.setHint(hitText);
		       input.setText(value);
		       //fragmento de codigo que permite que define el numero de caracteres
		       int maxLength = 300;
		       InputFilter[] fArray = new InputFilter[1];
		       fArray[0] = new InputFilter.LengthFilter(maxLength);
		       input.setFilters(fArray);
		       input.setTextSize(20);
		       input.setTypeface(Typeface.SERIF, Typeface.ITALIC);
		       input.setSingleLine(false);
		       input.setLines(2);
		       input.setMinLines(2);
		       input.setMaxLines(2);
		       input.setGravity(Gravity.LEFT | Gravity.TOP);
		       return input;
	    }

	public static Spinner spinnerControl(Context ctx, String[] opciones){
		final Spinner spinnerControl = new Spinner(ctx);

		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(ctx, R.layout.row_spinner_tipofalla, opciones);
		spinnerControl.setAdapter(adapterSpinner);

		return spinnerControl;
	}


	    
	public static EditText txtCapturaNumDecimal(Context ctx, String hitText, String value){
		       final EditText input = new EditText(ctx);

		       input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		       input.setHint(hitText);
		       input.setText(value);
		       //fragmento de codigo que permite que define el numero de caracteres
		       int maxLength = 300;
		       InputFilter[] fArray = new InputFilter[1];
		       fArray[0] = new InputFilter.LengthFilter(maxLength);
		       input.setFilters(fArray);
		       input.setTextSize(20);
		       input.setTypeface(Typeface.SERIF, Typeface.ITALIC);
		       input.setSingleLine(false);
		       input.setLines(2);
		       input.setMinLines(2);
		       input.setMaxLines(2);
		       input.setGravity(Gravity.LEFT | Gravity.TOP);
		       return input;
	}

	public static TextView tvStyleTextView(Context ctx, String value){
		final TextView textView = new TextView(ctx);
		textView.setTextSize(10);
		textView.setPadding(10,10,10,10);
		textView.setText(value);
		return textView;
	}

	public static EditText txtCapturaTexto(Context ctx, int nuemeroLineas, String hint){
		final EditText input = new EditText(ctx);

		//input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		input.setInputType(InputType.TYPE_CLASS_TEXT );
		//input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		input.setText("");
		input.setHint(hint);
		//fragmento de codigo que permite que define el numero de caracteres
		int maxLength = 300;
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		input.setFilters(fArray);
		input.setTextSize(20);
		input.setPadding(15, 0, 0, 0);
		input.setTextColor(Color.rgb(0xff, 0, 0));
		input.setTypeface(Typeface.SERIF, Typeface.ITALIC);
		input.setSingleLine(false);
		input.setLines(nuemeroLineas);//13
		input.setMinLines(nuemeroLineas);//13
		input.setMaxLines(nuemeroLineas);//15
		input.setGravity(Gravity.LEFT | Gravity.TOP);
		input.setFocusable(false);
		input.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});

		return input;
	}

	public static boolean alertMensaje(final Context ctx,final String titulo,final String mensaje)
	{
		blockingQueue = new ArrayBlockingQueue<Boolean>(1);
		Activity activity = (Activity) ctx;

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(ctx)
						.setTitle(titulo)
						.setMessage(mensaje)
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								blockingQueue.add(true);
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								blockingQueue.add(false);
							}
						})
						.show();

			}
		});
		try {
			return blockingQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

	}
	
}
