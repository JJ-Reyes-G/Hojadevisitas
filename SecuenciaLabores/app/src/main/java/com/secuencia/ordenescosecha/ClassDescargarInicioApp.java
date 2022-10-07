package com.secuencia.ordenescosecha;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.telephony.TelephonyManager;

import com.secuencia.database.DatabaseHandler_;

import test.Droidlogin.library.Httppostaux;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ClassDescargarInicioApp{
	ClassConfig conf ;
	
    private	String ws_bajarSecuenciaParaLabores = ClassConfig.ws_bajarSecuenciaParaLabores;
	private String ws_bajarConfiguracionDispositivo 	= ClassConfig.ws_bajarConfiguracionDispositivo;
	private String ws_bajarControlProductosLabores  	= ClassConfig.ws_bajarControlProductosLabores;
	private String ws_addNuevaAplicacionLabores     	= ClassConfig.ws_addNuevaAplicacionLabores;
	private String ws_cambiarPasswordUsuario			= ClassConfig.ws_cambiarPasswordUsuario;
	private String ws_bajarSecuenciaParaLaboresPorZona 	= ClassConfig.ws_bajarSecuenciaParaLaboresPorZona;
	private String ws_addNuevaVisitaCampo 				= ClassConfig.ws_addNuevaVisitaCampo;
	private String ws_bajarUltimasVisitas 				= ClassConfig.ws_bajarUltimasVisitas;
	private String ws_bajarTipoVisitas 					= ClassConfig.ws_bajarTipoVisitas;
	private String Hv_HojaVisitas_Lotes_Insert			= ClassConfig.Hv_HojaVisitas_Lotes_Insert;

	static HttppPeticiones post;
	//static Httppostaux post;
	SQLiteDatabase db;
	static DatabaseHandler_ dbhelper;
	TelephonyManager tm;
	String imei;
	
	public ClassDescargarInicioApp(Context ctx) 
	{
		
		//post=new Httppostaux();
		post=new HttppPeticiones();
		dbhelper = new DatabaseHandler_(ctx);
		tm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
		imei = "";//tm.getDeviceId();
		conf = new ClassConfig();
	}


	public boolean iniciarDescargaInicioDispositivo(String usuario, String password)
	{
		if( descargarConfiguracionDispositivo(usuario, password) && ws_bajarTipoVisitas(usuario) && bajarControlProductosLabores(usuario))
		{	
			Log.d("ClassDescargaInicoApp","Descargas exitosas"); 
			return true;
		}
		
		return false;
		
	}

	public boolean descargarConfiguracionDispositivo( String user, String pass )
	{
		
		try{
			Log.d("Metodo"," descargarConfiguracionDispositivo");
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();	
		    postparameters2send.add(new BasicNameValuePair("USER",user));
		    postparameters2send.add(new BasicNameValuePair("PASS",pass));	
		    postparameters2send.add(new BasicNameValuePair("IMEI",imei));	
		    postparameters2send.add(new BasicNameValuePair("NOMBREAPP" ,conf.key_nombreApp));
		    postparameters2send.add(new BasicNameValuePair("APPVERSION",conf.key_versionApp));
		    postparameters2send.add(new BasicNameValuePair("DBVERSION",Integer.toString(dbhelper.DATABASE_VERSION)));
		    
		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarConfiguracionDispositivo);

		    Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs); 
			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send); 
			if (datosjs != null && datosjs.length() > 0) {
				for(int i=0;i<datosjs.length();i++){
		  			JSONObject e = datosjs.getJSONObject(i);
		  			
			        db = dbhelper.getReadableDatabase();
			        db.execSQL("INSERT INTO "+dbhelper.TABLE_ADMINITRACION+" VALUES ("
			        			+""+e.getString("USERID")  	         +"  , " //0 CORRELATIVO QUE VIENE DEL WEB SERVICE
			        			+"'"+e.getString("USERNOMBRE")       +"' , " //1	
			        			+"'"+e.getString("USERPASS")         +"' , " //2
			        			+"'"+e.getString("REGID")    	     +"' , " //3
			        			+"'"+e.getString("FECHAREGISTRO")    +"' , " //4
			        			+"'"+"190.86.190.200"    	         +"' , " //5
			        			+""+1    	                         +"  , " //6
			        			+"'"+"SI"    	         			 +"' , " //7
			        			+"'"+"LIMITADO"    	         		 +"'   " //8
			        			+")");  
			        db.close();
				}
				ClassConfig.key_codTecnico = user;
				return true;
			}else{
				return false;
			}			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}


	public boolean Hv_HojaVisitas_Lotes_Insert(String SOLIC_ID)
	{
		Log.d("Metodo ","que guarda la nueva visita");
		String IDLLAVE = "0";

		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		db = dbhelper.getReadableDatabase();
		String query = "SELECT " +
				"a." +dbhelper.K_LSEL0_ID   		 		+" , "+         //0
				"a." +dbhelper.K_LSEL12_SOLIC_ID   		 	+" , "+         //0
				"a." +dbhelper.K_LSEL1_LOTEID	 			+" ,  "+ 	   	//1
				"a." +dbhelper.K_LSEL14_DELETE	 			+"   "+ 	   	//1

				"  FROM "+dbhelper.TABLE_LOTES_SELECT+ " a "
				+" WHERE a."+dbhelper.K_LSEL12_SOLIC_ID+" = "+SOLIC_ID+" AND a."+dbhelper.K_LSEL13_LLAVE_ENC+" = '0' ";//AND CAST(a."+dbhelper.KEY_APDET11_LLAVE_DET+" AS INTEGER) <= 0
		Log.d("Metodo 3","que guarda la nueva visita "+query);
		Cursor c = db.rawQuery(query, null);
		try{
			if(c.moveToFirst()){
				do{Log.d("Metodo 4 ", "que guarda la nueva requisicion" + query);

					postparameters2send.add(new BasicNameValuePair("visId",     "" + c.getString(1)));		//
					postparameters2send.add(new BasicNameValuePair("LoteId", 	"" + c.getString(2)));		//
					postparameters2send.add(new BasicNameValuePair("SelDelete", "" + c.getString(3)));		//

					IDLLAVE = c.getString(0);//
					JSONArray datosjs = post.getserverdata(postparameters2send,Hv_HojaVisitas_Lotes_Insert);
					Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs);
					Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send);
					if (datosjs != null && datosjs.length() > 0) {
						for(int i=0;i<datosjs.length();i++){
							JSONObject e = datosjs.getJSONObject(i);
							updateCampoString (
									dbhelper.TABLE_LOTES_SELECT,
									dbhelper.K_LSEL13_LLAVE_ENC,
									e.getString("Corr"),
									dbhelper.K_LSEL0_ID,
									IDLLAVE);
						}
					}
				}while(c.moveToNext());
			}
			c.close();
			db.close();


		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public boolean bajarControlProductosLabores( String codTecnico )
	{
		try{
			Log.d("Metodo ", "descargarOrdenesActivas descargarConfiguracionDispositivo");
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();	
		    postparameters2send.add(new BasicNameValuePair("CODTECNICO",codTecnico));	
 
		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarControlProductosLabores);//ws_descargarOrdenesActivas);
			Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs); 
			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send); 
			if (datosjs != null && datosjs.length() > 0) {
				//deleteCanaOrdenes ();
				for(int i=0;i<datosjs.length();i++){
		  			JSONObject e = datosjs.getJSONObject(i);
		  			
			        db = dbhelper.getReadableDatabase();			        
			        
			        db.execSQL("INSERT INTO "+dbhelper.TABLE_PRODUCTOS_LABORES
			        		/*
						 	 KEY_PROD1_ID 			= "ID";
							 KEY_PROD2_CODPROCUCTO 	= "CODPROCUCTO";
							 KEY_PROD3_NOMPROCUCTO 	= "NOMPROCUCTO";
							 KEY_PROD4_TPO_CONTROL  = "TPO_CONTROL";
							 KEY_PROD5_PRESENTACION = "PRESENTACION";
							 KEY_PROD6_DOSISDESDE 	= "DOSISDESDE";
							 KEY_PROD7_DOSISHASTA 	= "DOSISHASTA";
							 KEY_PROD8_NOMCONTROL	= "NOMCONTROL";
						 	*/
		        			+" VALUES ("
		        			+" '"+e.getString("CORRELATIVO")	+"' , "
		        			+" '"+e.getString("CODPRODUC")    	+"' , "	
		        			+" '"+e.getString("NOMPRODUC")   	+"' , "        			
		        			+" '"+e.getString("TIPOCONTROL")  	+"' , "	        			
		        			+" '"+e.getString("PRESENTACION")  	+"' , "
		        			+" '"+e.getString("DOSISDESDE")    	+"' , "
		        			+" '"+e.getString("DOSISHASTA")  	+"' , "
		        			+" '"+e.getString("NOMCONTROL")     +"'   "
		        			+")"); 
		        			
			        db.close();
				}
				return true;
			}else{
				return false;
			}			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public boolean ws_bajarUltimasVisitas( String codTecnico )
	{
		try{
			Log.d("Metodo ", "descargar las ultimas visitas de campo");

			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("CODTECNICO",codTecnico));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarUltimasVisitas);//ws_descargarOrdenesActivas);
			Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs);
			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {

				deleteAllTablaWhereCampo(dbhelper.TABLE_VISITAS, dbhelper.KEY_VIS7_LLAVE, "0","<>");
				//deleteCanaOrdenes ();
				for(int i=0;i<datosjs.length();i++){
					JSONObject e = datosjs.getJSONObject(i);

					db = dbhelper.getReadableDatabase();

					db.execSQL("INSERT INTO "+dbhelper.TABLE_VISITAS
							+" ( "
							+ dbhelper.KEY_VIS1_RAZON  			+ " ,"
							+ dbhelper.KEY_VIS2_RECOMENDACION	+ " ,"
							+ dbhelper.KEY_VIS3_ZONA 			+ " ,"
							+ dbhelper.KEY_VIS4_FECHA			+ " ,"
							+ dbhelper.KEY_VIS5_NOMCONTACTO		+ " ,"
							+ dbhelper.KEY_VIS6_CODAGRONOMO		+ " ,"
							+ dbhelper.KEY_VIS7_LLAVE			+ " ,"
							+ dbhelper.KEY_VIS8_IDLOTE			+ " ,"
							+ dbhelper.KEY_VIS9_DESCLOTE 		+ " ,"
							+ dbhelper.KEY_VIS10_ZAFRA			+ " ,"
							+ dbhelper.KEY_VIS11_TIPOVISITA 	+ "  "
							+") VALUES ("
							+" '"+e.getString("visRazon")		+"', "
							+" '"+e.getString("visRecomendacion") +"', "
							+" '"+e.getString("visZona")   		+"', "
							+" '"+e.getString("visFecha")  		+"', "
							+" '"+e.getString("visContacto")  	+"', "
							+" '"+e.getString("visCodTecnico")  +"', "
							+" '"+e.getString("visId")  		+"', "
							+" '"+e.getString("visCorrLote")    +"', "
							+" '"+e.getString("visLoteDesc")    +"', "
							+" '"+e.getString("visZafra")     	+"', "
							+" '"+e.getString("visTipoVisita")  +"'  "
							+")");


					db.close();
				}
				return true;
			}else{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}


	public boolean ws_bajarTipoVisitas( String codTecnico )
	{
		try{
			Log.d("Metodo ", "descargar las ultimas visitas de campo");

			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("CODTECNICO",codTecnico));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarTipoVisitas);//ws_descargarOrdenesActivas);
			Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs);
			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {

				deleteAllTablaWhereCampo(dbhelper.TABLE_TIPOVISITAS, "0", "0","=");

				for(int i=0;i<datosjs.length();i++){
					JSONObject e = datosjs.getJSONObject(i);

					db = dbhelper.getReadableDatabase();

					db.execSQL("INSERT INTO "+dbhelper.TABLE_TIPOVISITAS
							+" ( "
							+ dbhelper.K_TIP1_CODTIPO  			+ " ,"
							+ dbhelper.K_TIP2_DESCTIPO			+ " ,"
							+ dbhelper.K_TIP3_CODMOTIVO 		+ " ,"
							+ dbhelper.K_TIP4_DESCMOTIVO		+ " ,"
							+ dbhelper.K_TIP5_CODRESULT			+ " ,"
							+ dbhelper.K_TIP6_DESCRESULT		+ "  "
							+") VALUES ("
							+" '"+e.getString("TipoId")			+"', "
							+" '"+e.getString("TipoNombre") 	+"', "
							+" '"+e.getString("MotivoId")   	+"', "
							+" '"+e.getString("MotivoName")  	+"', "
							+" '"+e.getString("ResultadoId")  	+"', "
							+" '"+e.getString("Resultado")  	+"'  "
							+")");

					db.close();
				}
				return true;
			}else{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	

	public boolean ws_bajarSecuenciaParaLaboresPorZona( String codTecnico, String numeroZona )
	{
		try{
			Log.d("Metodo descargar ","secuencuencia ws_bajarSecuenciaParaLabores");

			ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
			postparameters2send.add(new BasicNameValuePair("CODTECNICO",codTecnico));
			postparameters2send.add(new BasicNameValuePair("numeroZona",numeroZona));

			JSONArray datosjs = post.getserverdata(postparameters2send,ws_bajarSecuenciaParaLaboresPorZona);
			Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs);
			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send);
			if (datosjs != null && datosjs.length() > 0) {
				//deleteAllTabla (dbhelper.TABLE_SECUENCIA_PARALABORES);
				deleteAllTablaWhereCampo(dbhelper.TABLE_SECUENCIA_PARALABORES, dbhelper.KEY_SEC17_ZONA, numeroZona,"=");
				for(int i=0;i<datosjs.length();i++){
					JSONObject e = datosjs.getJSONObject(i);

					db = dbhelper.getReadableDatabase();
					db.execSQL("INSERT INTO "+dbhelper.TABLE_SECUENCIA_PARALABORES+" ( "+
							//dbhelper.KEY_SEC1_ID   			+" , "+        //0
							dbhelper.KEY_SEC2_NUM   		+" , "+        //0
							dbhelper.KEY_SEC3_LLAVE	 		+" , "+ 	   	//1
							dbhelper.KEY_SEC4_FRENTE	 	+" , "+ 		//2
							dbhelper.KEY_SEC5_NOMFRENTE	 	+" , "+ 		//3
							dbhelper.KEY_SEC6_CODCLIENTE	+" , "+ 		//4
							dbhelper.KEY_SEC7_NOMCLIENTE	+" , "+ 		//5
							dbhelper.KEY_SEC8_CODFINCA		+" , "+ 		//6
							dbhelper.KEY_SEC9_NOMFINCA 		+" , "+ 		//7
							dbhelper.KEY_SEC10_CODLOTE 		+" , "+         //8
							dbhelper.KEY_SEC11_NOMLOTE 		+" , "+     //9
							dbhelper.KEY_SEC12_ESTATUS 		+" , "+     //10
							dbhelper.KEY_SEC13_COUNT 		+" , "+     //10
							dbhelper.KEY_SEC14_TONESTIMA 	+" , "+     //10
							dbhelper.KEY_SEC15_AGRONOMO 	+" , "+     //10
							dbhelper.KEY_SEC16_ZAFRA 		+" , "+     //10
							dbhelper.KEY_SEC17_ZONA 		+"   "
							+" ) "
							+" VALUES ("
							//+e.getString("CORRELATIVO")  	  +"  , " //0 CORRELATIVO QUE VIENE DEL WEB SERVICE
							+e.getString("SECUENCIA")         +"  , " //1
							+" 0 "                       	  +"  , " //2 LLAVE DE LA ORDEN  DE COSECHA
							+e.getString("CODFRENTE")    	  +"  , " //3
							+"'"+e.getString("FRENTE")  	  +"' , "//4
							+"'"+e.getString("PROVEEDOR")     +"' , "//5
							+"'"+e.getString("NOMPROVEEDOR")  +"' , "//6
							+e.getString("FINCA")  			  +"  , " //7
							+"'"+e.getString("NOMFINCA")      +"' , "//8
							+e.getString("LOTE")  			  +"  , " //9
							+"'"+e.getString("NOMLOTE")       +"' , "//10
							+"'"+e.getString("ESTATUS")       +"' , "//11 ESTATUS CUANDO SEA 0 NO SE HA CREADO ORDEN
							+e.getString("COUNTSECUENCIA")    +"  , "   //12
							+e.getString("TONESTIMA")    	  +"  , "   //13
							+"'"+e.getString("AGRONOMO")      +"' , "   //14
							+"'"+e.getString("ZAFRA_ACTUAL")  +"' , "   //15
							+"'"+e.getString("ZONA")  		  +"'   "   //16
							+")");
					db.close();
				}
				return true;
			}else{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public boolean addNuevaRequisicion(int idRequisicion)
	{	
			Log.d("Metodo ","que guarda la nueva requisicion addNuevaRequisicion");
			String IDLLAVE = "0";
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();	
	        db = dbhelper.getReadableDatabase();
	        String query = "SELECT " +
	        		"b." +dbhelper.KEY_APENC1_ID   		 		+" , "+        //0
	        		"b." +dbhelper.KEY_APENC2_CODCLIENTE	 	+" , "+ 	   	//1
	        		"b." +dbhelper.KEY_APENC4_CODFINCA	 		+" , "+ 		//2
	        		"b." +dbhelper.KEY_APENC6_CODLOTE	 		+" , "+ 		//3
	        		"b." +dbhelper.KEY_APENC11_CODUSUARIO	 	+" , "+ 		//4
	        		"a." +dbhelper.KEY_APDET12_ZAFRA		 	+" , "+ 		//5
	        		"a." +dbhelper.KEY_APDET10_COD_TPOCONTROL	+" , "+ 		//6
	        		"a." +dbhelper.KEY_APDET5_CODPROCUCTO 		+" , "+ 		//7
	        		"a." +dbhelper.KEY_APDET3_TPO_APLICACION 	+" , "+         //8
	        		"a." +dbhelper.KEY_APDET4_DOSIS 		 	+" , "+     //9
	        		"a." +dbhelper.KEY_APDET8_FECHACONTROL 		+" , "+     //10	        		
	        		"a." +dbhelper.KEY_APDET1_ID 		        +"   "+     //11	        		
	        		"  FROM "+dbhelper.TABLE_APLICACIONES_LABORES_DETALLES+ " a "
	        		+" INNER JOIN "+dbhelper.TABLE_APLICACIONES_LABORES_ENCABEZADO+ " b "
	        		+" ON    a."+dbhelper.KEY_APDET2_IDECABEZADO+" = b."+dbhelper.KEY_APENC1_ID
	        		+" WHERE b."+dbhelper.KEY_APENC1_ID+" = "+idRequisicion+"  AND CAST(a."+dbhelper.KEY_APDET11_LLAVE_DET+" AS INTEGER) <= 0";
	        Log.d("Metodo 3","que guarda la nueva requisicion"+query);

	        Cursor c = db.rawQuery(query, null); 
	        
	        Log.d("Metodo 2","que guarda la nueva requisicion addNuevaRequisicion");
	        Log.d("Metodo 3","que guarda la nueva requisicion"+query);
	        try{
	        if(c.moveToFirst()){
	            do{
	    	        Log.d("Metodo 4 ","que guarda la nueva requisicion" +query);

	           	 	postparameters2send.add(new BasicNameValuePair("CODCLIENTE",	""+c.getString(1)));		//
	           	 	postparameters2send.add(new BasicNameValuePair("CODFINCA",	  	""+c.getString(2)));		//          	 	
	           	 	postparameters2send.add(new BasicNameValuePair("CODLOTE",	  	""+c.getString(3)));		//
	           	 	postparameters2send.add(new BasicNameValuePair("ZAFRA",	  		""+c.getString(5))); 		//
	           	 	postparameters2send.add(new BasicNameValuePair("FECHA",	  		""+c.getString(10)));		//
	           	 	postparameters2send.add(new BasicNameValuePair("PRODUCTO",	  	""+c.getString(7)));		//
	           	 	postparameters2send.add(new BasicNameValuePair("TIPOCONTROL",	""+c.getString(6)));		//
	           	 	postparameters2send.add(new BasicNameValuePair("DOSIS", 		"" + c.getString(9)));//"1308"));
	           	    postparameters2send.add(new BasicNameValuePair("TIPOAPLICACION",""+c.getString(8)));        //
	           	    IDLLAVE = c.getString(11);//
	    		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_addNuevaAplicacionLabores);
	    			Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs); 
	    			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send); 
	    			if (datosjs != null && datosjs.length() > 0) {
	    				for(int i=0;i<datosjs.length();i++){
	    		  			JSONObject e = datosjs.getJSONObject(i);
                        	updateCampoString (
                        			dbhelper.TABLE_APLICACIONES_LABORES_DETALLES, 
                        			dbhelper.KEY_APDET11_LLAVE_DET, 
                        			e.getString("LLAVE"), 
                        			dbhelper.KEY_APDET1_ID, 
                        			IDLLAVE);
	    				}
	    			}
	            }while(c.moveToNext());
	        }
		    c.close();
		    db.close();
		    

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public boolean addNuevaVisita(String idVisita)
	{
		Log.d("Metodo ","que guarda la nueva visita");
		String IDLLAVE = "0";

		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		db = dbhelper.getReadableDatabase();
		String query = "SELECT " +
				"a." +dbhelper.KEY_VIS0_ID   		 		+" , "+         //0
				"a." +dbhelper.KEY_VIS1_RAZON	 			+" , "+ 	   	//1
				"a." +dbhelper.KEY_VIS2_RECOMENDACION	 	+" , "+ 		//2
				"a." +dbhelper.KEY_VIS3_ZONA	 			+" , "+ 		//3
				"a." +dbhelper.KEY_VIS4_FECHA	 			+" , "+ 		//4
				"a." +dbhelper.KEY_VIS5_NOMCONTACTO		 	+" , "+ 		//5
				"a." +dbhelper.KEY_VIS6_CODAGRONOMO			+" , "+ 		//6
				"a." +dbhelper.KEY_VIS7_LLAVE 				+" , "+ 		//7
				"a." +dbhelper.KEY_VIS8_IDLOTE 				+" , "+         //8
				"a." +dbhelper.KEY_VIS9_DESCLOTE 		 	+" , "+         //9
				"a." +dbhelper.KEY_VIS10_ZAFRA 				+" , "+         //10
				"a." +dbhelper.KEY_VIS11_TIPOVISITA 		+" , "+         //11
				"a." +dbhelper.KEY_VIS12_CODTIPO 			+" , "+         //12
				"a." +dbhelper.KEY_VIS13_CODMOTIVO 			+" , "+         //13
				"a." +dbhelper.KEY_VIS14_CODRESULTADO 		+" , "+         //14
				"a." +dbhelper.KEY_VIS15_QUIENRECIBE 		+" , "+         //15
				"a." +dbhelper.KEY_VIS16_HORAVISITA 		+" , "+         //15
				"a." +dbhelper.KEY_VIS17_CODFINCA 			+"   "+         //15

				"  FROM "+dbhelper.TABLE_VISITAS+ " a "
				+" WHERE a."+dbhelper.KEY_VIS0_ID+" = "+idVisita+" AND a."+dbhelper.KEY_VIS7_LLAVE+" = '0' ";//AND CAST(a."+dbhelper.KEY_APDET11_LLAVE_DET+" AS INTEGER) <= 0
		Log.d("Metodo 3","que guarda la nueva visita"+query);
		Cursor c = db.rawQuery(query, null);
		try{
			if(c.moveToFirst()){
				do{Log.d("Metodo 4 ","que guarda la nueva requisicion" +query);

					postparameters2send.add(new BasicNameValuePair("visRazon", "" + c.getString(1)));		//
					postparameters2send.add(new BasicNameValuePair("visContacto", 		""+c.getString(5)));		//
					postparameters2send.add(new BasicNameValuePair("visRecomendacion",	""+c.getString(2)));		//
					postparameters2send.add(new BasicNameValuePair("visCorrLote",	  	""+c.getString(8))); 		//
					postparameters2send.add(new BasicNameValuePair("visFecha",	  		""+c.getString(4)));		//
					postparameters2send.add(new BasicNameValuePair("visCodTecnico",	  	""+c.getString(6)));		//
					postparameters2send.add(new BasicNameValuePair("visZafra",	  		""+c.getString(10)));		//
					postparameters2send.add(new BasicNameValuePair("visTipoVisita",	  	""+c.getString(11)));		//
					postparameters2send.add(new BasicNameValuePair("CODTIPO",	  		""+c.getString(12)));		//
					postparameters2send.add(new BasicNameValuePair("CODMOTIVO",	  		""+c.getString(13)));		//
					postparameters2send.add(new BasicNameValuePair("CODRESULTADO",	  	""+c.getString(14)));		//
					postparameters2send.add(new BasicNameValuePair("QUIENRECIBE",	  	""+c.getString(15)));		//
					postparameters2send.add(new BasicNameValuePair("HORAVISITA",	  	""+c.getString(16)));		//
					postparameters2send.add(new BasicNameValuePair("codFinca",	  		""+c.getString(17)));

					IDLLAVE = c.getString(0);//
					JSONArray datosjs = post.getserverdata(postparameters2send,ws_addNuevaVisitaCampo);
					Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs);
					Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send);
					if (datosjs != null && datosjs.length() > 0) {
						for(int i=0;i<datosjs.length();i++){
							JSONObject e = datosjs.getJSONObject(i);
							updateCampoString (
									dbhelper.TABLE_VISITAS,
									dbhelper.KEY_VIS7_LLAVE,
									e.getString("visId"),
									dbhelper.KEY_VIS0_ID,
									IDLLAVE);
						}
					}
				}while(c.moveToNext());
			}
			c.close();
			db.close();


		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public void updateEstatusRowOrden (int idorden){
        db = dbhelper.getReadableDatabase();
        db.execSQL("UPDATE ordenes SET ordnestatusrowlist = 0");  
        db.close();
        db = dbhelper.getReadableDatabase();
        db.execSQL("UPDATE ordenes SET ordnestatusrowlist = 1 WHERE ordnid = " + idorden);
        db.close();        	
	}
	
	public String getFecha (){
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
		
	}

	public void deleteAllTablaWhereCampo (String nombreTabla, String campoCondicion, String valorCampo, String condicion){
		db = dbhelper.getReadableDatabase();
		String query ="DELETE FROM "+nombreTabla+ " WHERE "+campoCondicion+" "+condicion+" '"+valorCampo+"' ";
		Log.d("DELETE:", "DELETE:" + query);
		db.execSQL(query);
		db.close();
	}

	public void updateCampoString (String TABLA, String CAMPO, String VALUE, String IDCAMPO, String IDVALUE){
        db = dbhelper.getReadableDatabase();
        db.execSQL("UPDATE "+TABLA+" SET "+CAMPO+" = '"+VALUE+"' WHERE "+IDCAMPO+" = "+IDVALUE);  
        db.close();      	
	}
	
	public boolean descargarCambiarPasswordUsuario( String USER, String PASS , String NEWPASS)
	{
		try{
			Log.d("Metodo","para cambiar la contraseña descargarCambiarPasswordUsuario");
			
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();	
		    postparameters2send.add(new BasicNameValuePair("USER",USER));
		    postparameters2send.add(new BasicNameValuePair("PASS",PASS));	
		    postparameters2send.add(new BasicNameValuePair("NEWPASS",NEWPASS));	
		    postparameters2send.add(new BasicNameValuePair("IMEI",imei));	
		    postparameters2send.add(new BasicNameValuePair("NOMBREAPP" ,conf.key_nombreApp));
		    postparameters2send.add(new BasicNameValuePair("APPVERSION",conf.key_versionApp));
		    postparameters2send.add(new BasicNameValuePair("DBVERSION",Integer.toString(dbhelper.DATABASE_VERSION)));
		    
		    JSONArray datosjs = post.getserverdata(postparameters2send,ws_cambiarPasswordUsuario);
			Log.d(" Informe JSONArray","Valor que contiene JSONArray los datos: "+datosjs); 
			Log.d(" Informe JSONArray","Valor que contiene POST: "+postparameters2send); 
			if (datosjs != null && datosjs.length() > 0) {
				for(int i=0;i<datosjs.length();i++){
		  			JSONObject e = datosjs.getJSONObject(i);
		  			
			        setUpdateCampo(
			        		dbhelper.TABLE_ADMINITRACION, 
			        		dbhelper.KEY_ADMIN3_PASSUSER, 
			        		"'"+e.getString("USERPASS")+"'", 
			        		dbhelper.KEY_ADMIN1_ID, 
			        		e.getString("USERID")
			        		);
			        ClassConfig.KEY_PASSWORD = e.getString("USERPASS");
				}
				return true;
			}else{
				return false;
			}			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public void setUpdateCampo(String nombreTabla, String campoTablaUpdate, String miCampoValue, String campoTablaCondicion, String miCondicion)
	{
        db = dbhelper.getReadableDatabase();
        db.execSQL("UPDATE "+nombreTabla+" SET "+
        		campoTablaUpdate+"  = "+miCampoValue+" "+
        		" WHERE "+campoTablaCondicion+" = "+miCondicion);
        db.close();
          
	}

}