package com.secuencia.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler_ extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	public static final int DATABASE_VERSION = 23;

	// Database Name
	private static final String DATABASE_NAME = "secuenciaLabores";

	// ADMINISTRACION table name
	public static final String TABLE_ADMINITRACION = "administracion";
	// ADMINISTRACION Table Columns names
	public static final String KEY_ADMIN1_ID            = "adminid";
	public static final String KEY_ADMIN2_NOMBREUSER    = "adminnombreuser";
	public static final String KEY_ADMIN3_PASSUSER      = "adminpassuser";
	public static final String KEY_ADMIN4_IDDISPOSITIVO = "adminiddispositivo";	
	public static final String KEY_ADMIN5_FECHA         = "adminfecha";
	public static final String KEY_ADMIN6_IDSERVIDOR    = "adminidservidor";
	public static final String KEY_ADMIN7_COUNT 		= "countt";
	public static final String KEY_ADMIN8_SINC_AUTO     = "SINC_AUTO";
	public static final String KEY_ADMIN9_TIPO_PERMISO	= "TIPO_PERMISO";
	
	// SECUENCIAS table name
	//public static final String TABLE_SECUENCIAS = "secuencias";
	
	public static final String TABLE_SECUENCIA_PARALABORES = "secuencia_labores";
	// SECUENCIAS Table Columns names
	public static final String KEY_SEC1_ID 				= "secuenciaid";
	public static final String KEY_SEC2_NUM 			= "secnum";
	public static final String KEY_SEC3_LLAVE 			= "secllave";
	public static final String KEY_SEC4_FRENTE 			= "secfrente";
	public static final String KEY_SEC5_NOMFRENTE 		= "secnomfrente";
	public static final String KEY_SEC6_CODCLIENTE 		= "seccodcliente";
	public static final String KEY_SEC7_NOMCLIENTE 		= "secnomcliente";
	public static final String KEY_SEC8_CODFINCA 		= "seccodfinca";
	public static final String KEY_SEC9_NOMFINCA 		= "secnomfinca";
	public static final String KEY_SEC10_CODLOTE 		= "seccodlote";
	public static final String KEY_SEC11_NOMLOTE 		= "secnomlote";
	public static final String KEY_SEC12_ESTATUS 		= "secestatus";
	public static final String KEY_SEC13_COUNT 			= "seccount";
	public static final String KEY_SEC14_TONESTIMA 		= "sectonestima";
	public static final String KEY_SEC15_AGRONOMO 		= "secagronomo";
	public static final String KEY_SEC16_ZAFRA 			= "ZAFRA";
	public static final String KEY_SEC17_ZONA 			= "ZONA";
	
	public static final String TABLE_APLICACIONES_LABORES_ENCABEZADO = "APLICACIONES_LABORES_ENCABEZADO";	
	// APLICACIONES_LABORES Table Columns names
	public static final String KEY_APENC1_ID 			= "ID";
	public static final String KEY_APENC2_CODCLIENTE 	= "CODCLIENTE";
	public static final String KEY_APENC3_NOMCLIENTE 	= "NOMCLIENTE";
	public static final String KEY_APENC4_CODFINCA 		= "CODFINCA";
	public static final String KEY_APENC5_NOMFINCA 		= "NOMFINCA";
	public static final String KEY_APENC6_CODLOTE 		= "CODLOTE";
	public static final String KEY_APENC7_NOMLOTE 		= "NOMLOTE";
	public static final String KEY_APENC8_FECHA 		= "FECHA";
	public static final String KEY_APENC9_ESTATUS 		= "ESTATUS";
	public static final String KEY_APENC10_LLAVE 		= "LLAVE";	
	public static final String KEY_APENC11_CODUSUARIO 	= "CODUSUARIO";
	public static final String KEY_APENC12_ZAFRA 	    = "ZAFRA";
	public static final String KEY_APENC13_IDSECUENCIA  = "IDSECUENCIA";

	public static final String TABLE_TIPOVISITAS = "TIPOVISITAS";
	// APLICACIONES_LABORES Table Columns names
	public static final String K_TIP0_ID 				= "ID";
	public static final String K_TIP1_CODTIPO 			= "CODTIPO";
	public static final String K_TIP2_DESCTIPO 			= "DESCTIPO";
	public static final String K_TIP3_CODMOTIVO 		= "CODFINCA";
	public static final String K_TIP4_DESCMOTIVO 		= "DESCMOTIVO";
	public static final String K_TIP5_CODRESULT 		= "CODRESULT";
	public static final String K_TIP6_DESCRESULT 		= "DESCRESULT";

	public static final String TABLE_APLICACIONES_LABORES_DETALLES = "APLICACIONES_LABORES_DETALLES";	
	// APLICACIONES_LABORES Table Columns names
	public static final String KEY_APDET1_ID 			= "ID";
	public static final String KEY_APDET2_IDECABEZADO   = "IDECABEZADO";
	public static final String KEY_APDET3_TPO_APLICACION= "TPO_APLICACION";
	public static final String KEY_APDET4_DOSIS 	    = "DOSIS";
	public static final String KEY_APDET5_CODPROCUCTO 	= "CODPROCUCTO";
	public static final String KEY_APDET6_NOMPROCUCTO 	= "NOMPROCUCTO";
	public static final String KEY_APDET7_TPO_CONTROL   = "TPO_CONTROL";
	public static final String KEY_APDET8_FECHACONTROL  = "FECHACONTROL";
	public static final String KEY_APDET9_PRESENTACION  = "PRESENTACION";
	public static final String KEY_APDET10_COD_TPOCONTROL = "COD_TPOCONTROL";
	public static final String KEY_APDET11_LLAVE_DET	= "LLAVE_DET";
	public static final String KEY_APDET12_ZAFRA		= "ZAFRA";
	
	
	public static final String TABLE_PRODUCTOS_LABORES = "PRODUCTOS_LABORES";	
	// PRODUCTOS_LABORES Table Columns names
	public static final String KEY_PROD1_ID 			= "ID";
	public static final String KEY_PROD2_CODPROCUCTO 	= "CODPROCUCTO";
	public static final String KEY_PROD3_NOMPROCUCTO 	= "NOMPROCUCTO";
	public static final String KEY_PROD4_TPO_CONTROL    = "TPO_CONTROL";
	public static final String KEY_PROD5_PRESENTACION 	= "PRESENTACION";
	public static final String KEY_PROD6_DOSISDESDE 	= "DOSISDESDE";
	public static final String KEY_PROD7_DOSISHASTA 	= "DOSISHASTA";
	public static final String KEY_PROD8_NOMCONTROL		= "NOMCONTROL";

	// VISITAS table name
	public static final String TABLE_VISITAS = "visitas";
	// VISITAS Table Columns names
	public static final String KEY_VIS0_ID 				= "VisId";
	public static final String KEY_VIS1_RAZON 			= "VisRazon";
	public static final String KEY_VIS2_RECOMENDACION 	= "VisRecomendacion";
	public static final String KEY_VIS3_ZONA		 	= "VisZona";
	public static final String KEY_VIS4_FECHA		 	= "VisFecha";
	public static final String KEY_VIS5_NOMCONTACTO		= "VisNomContacto";
	public static final String KEY_VIS6_CODAGRONOMO		= "VisCodAgronomo";
	public static final String KEY_VIS7_LLAVE			= "VisLlave";
	public static final String KEY_VIS8_IDLOTE			= "VisIdLote";
	public static final String KEY_VIS9_DESCLOTE		= "VisDescLote";
	public static final String KEY_VIS10_ZAFRA			= "VisZafra";
	public static final String KEY_VIS11_TIPOVISITA		= "VisTipoVisita";
	public static final String KEY_VIS12_CODTIPO		= "CODTIPO";
	public static final String KEY_VIS13_CODMOTIVO		= "CODMOTIVO";
	public static final String KEY_VIS14_CODRESULTADO	= "CODRESULTADO";
	public static final String KEY_VIS15_QUIENRECIBE	= "QUIENRECIBE";
	public static final String KEY_VIS16_HORAVISITA		= "HORAVISITA";
	public static final String KEY_VIS17_CODFINCA		= "CODFINCA";
	public static final String KEY_VIS18_NOMFINCA		= "NOMFINCA";
	public static final String KEY_VIS19_CODPROV		= "CODPROV";
	public static final String KEY_VIS20_NOMPROV		= "NOMPROV";

	//TABLE LOTES_SELECT
	public final String TABLE_LOTES_SELECT		= "LOTES_SELECT";

	public final String K_LSEL0_ID 				= "K_LSEL0_ID";
	public final String K_LSEL1_LOTEID 			= "LOTEID";
	public final String K_LSEL2_NOMLOTE 		= "NOMLOTE";
	public final String K_LSEL3_CODFINCA 		= "CODFINCA";
	public final String K_LSEL4_IDOIP 			= "IDOIP";
	public final String K_LSEL5_ENCID_MOV 		= "ENCID_MOV";
	public final String K_LSEL6_LLAVE			= "LLAVE";
	public final String K_LSEL7_IDREQ			= "IDREQ";
	public final String K_LSEL8_CONTRATO		= "CONTRATO";
	public final String K_LSEL9_ZAFRA			= "ZAFRA";
	public final String K_LSEL10_AREA			= "AREA";
	public final String K_LSEL11_CANT_SOLIC		= "CANT_SOLIC";
	public final String K_LSEL12_SOLIC_ID		= "SOLIC_ID";
	public final String K_LSEL13_LLAVE_ENC		= "LLAVE_ENC";
	public final String K_LSEL14_DELETE			= "SEL_DELETE";




	public DatabaseHandler_(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables 
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_LOTES_SELECT_TABLE = "CREATE TABLE " + TABLE_LOTES_SELECT + "("
				+ K_LSEL0_ID		    	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ K_LSEL1_LOTEID 			+ " TEXT, "
				+ K_LSEL2_NOMLOTE 			+ " TEXT, "
				+ K_LSEL3_CODFINCA 			+ " TEXT, "
				+ K_LSEL4_IDOIP 			+ " TEXT, "
				+ K_LSEL5_ENCID_MOV 		+ " TEXT, "
				+ K_LSEL6_LLAVE 			+ " TEXT, "
				+ K_LSEL7_IDREQ 			+ " TEXT, "
				+ K_LSEL8_CONTRATO 			+ " TEXT, "
				+ K_LSEL9_ZAFRA				+ " TEXT, "
				+ K_LSEL10_AREA				+ " TEXT, "
				+ K_LSEL11_CANT_SOLIC		+ " TEXT, "
				+ K_LSEL12_SOLIC_ID			+ " TEXT, "
				+ K_LSEL13_LLAVE_ENC		+ " TEXT, "
				+ K_LSEL14_DELETE			+ " TEXT  "

				+")";
		db.execSQL(CREATE_LOTES_SELECT_TABLE);


		String CREATE_TABLE_SECUENCIA_DELABORES_TABLE = "CREATE TABLE " + TABLE_SECUENCIA_PARALABORES + "("
				+ KEY_SEC1_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_SEC2_NUM 				+ " INTEGER,"
				+ KEY_SEC3_LLAVE 			+ " INTEGER,"
				+ KEY_SEC4_FRENTE 			+ " INTEGER,"
				+ KEY_SEC5_NOMFRENTE 		+ " TEXT,"
				+ KEY_SEC6_CODCLIENTE 		+ " TEXT,"
				+ KEY_SEC7_NOMCLIENTE 		+ " TEXT,"
				+ KEY_SEC8_CODFINCA 		+ " INTEGER,"
				+ KEY_SEC9_NOMFINCA 		+ " TEXT,"
				+ KEY_SEC10_CODLOTE 		+ " INTEGER,"
				+ KEY_SEC11_NOMLOTE 		+ " TEXT,"
				+ KEY_SEC12_ESTATUS 		+ " INTEGER,"				
				+ KEY_SEC13_COUNT 			+ " INTEGER, "
				+ KEY_SEC14_TONESTIMA 		+ " DECIMAL(8,2), "
				+ KEY_SEC15_AGRONOMO 		+ " TEXT, "
				+ KEY_SEC16_ZAFRA			+ " TEXT, "
				+ KEY_SEC17_ZONA			+ " TEXT "
				+")";
		db.execSQL(CREATE_TABLE_SECUENCIA_DELABORES_TABLE);
		 
		String CREATE_TABLE_PRODUCTOS_LABORES = "CREATE TABLE " + TABLE_PRODUCTOS_LABORES + "("
				+ KEY_PROD1_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_PROD2_CODPROCUCTO 	+ " TEXT,"
				+ KEY_PROD3_NOMPROCUCTO 	+ " TEXT,"
				+ KEY_PROD4_TPO_CONTROL   	+ " TEXT,"
				+ KEY_PROD5_PRESENTACION 	+ " TEXT,"
				+ KEY_PROD6_DOSISDESDE 		+ " DECIMAL(8,2), "
				+ KEY_PROD7_DOSISHASTA		+ " DECIMAL(8,2), "
				+ KEY_PROD8_NOMCONTROL   	+ " TEXT "				
				+")";
		db.execSQL(CREATE_TABLE_PRODUCTOS_LABORES);


		String CREATE_TABLE_TIPOVISITAS = "CREATE TABLE " + TABLE_TIPOVISITAS + "("
				+ K_TIP0_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ K_TIP1_CODTIPO 		+ " TEXT, "
				+ K_TIP2_DESCTIPO 		+ " TEXT, "
				+ K_TIP3_CODMOTIVO   	+ " TEXT, "
				+ K_TIP4_DESCMOTIVO 	+ " TEXT, "
				+ K_TIP5_CODRESULT 		+ " TEXT, "
				+ K_TIP6_DESCRESULT   	+ " TEXT  "
				+")";
		db.execSQL(CREATE_TABLE_TIPOVISITAS);

		
		String CREATE_TABLE_APLICACIONES_LABORES = "CREATE TABLE " + TABLE_APLICACIONES_LABORES_ENCABEZADO + "("
				+ KEY_APENC1_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_APENC2_CODCLIENTE		+ " TEXT," 	
				+ KEY_APENC3_NOMCLIENTE 	+ " TEXT,"
				+ KEY_APENC4_CODFINCA		+ " TEXT," 		
				+ KEY_APENC5_NOMFINCA		+ " TEXT," 		
				+ KEY_APENC6_CODLOTE		+ " TEXT," 		
				+ KEY_APENC7_NOMLOTE		+ " TEXT," 	
				+ KEY_APENC8_FECHA			+ " TEXT," 		    
				+ KEY_APENC9_ESTATUS 		+ " TEXT,"
				+ KEY_APENC10_LLAVE 		+ " TEXT,"
				+ KEY_APENC11_CODUSUARIO 	+ " TEXT,"
				+ KEY_APENC12_ZAFRA 	  	+ " TEXT,"
				+ KEY_APENC13_IDSECUENCIA   + " INTEGER "
				+")";
		db.execSQL(CREATE_TABLE_APLICACIONES_LABORES);
		
		String CREATE_TABLE_APLICACIONES_LABORES_DETALLES = "CREATE TABLE " + TABLE_APLICACIONES_LABORES_DETALLES + "("		
				+ KEY_APDET1_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_APDET2_IDECABEZADO    + " INTEGER,"
				+ KEY_APDET3_TPO_APLICACION	+ " TEXT,"
				+ KEY_APDET4_DOSIS 	    	+ " TEXT,"
				+ KEY_APDET5_CODPROCUCTO 	+ " TEXT,"
				+ KEY_APDET6_NOMPROCUCTO 	+ " TEXT,"
				+ KEY_APDET7_TPO_CONTROL   	+ " TEXT,"
				+ KEY_APDET8_FECHACONTROL   + " TEXT,"
				+ KEY_APDET9_PRESENTACION   + " TEXT,"
				+ KEY_APDET10_COD_TPOCONTROL+ " TEXT,"
				+ KEY_APDET11_LLAVE_DET     + " TEXT, "
				+ KEY_APDET12_ZAFRA     	+ " TEXT "
				+")";
		db.execSQL(CREATE_TABLE_APLICACIONES_LABORES_DETALLES);
		
		String CREATE_ADMINISTRACION_TABLE = "CREATE TABLE " + TABLE_ADMINITRACION + "("
				+ KEY_ADMIN1_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ KEY_ADMIN2_NOMBREUSER 	+ " TEXT, "
				+ KEY_ADMIN3_PASSUSER 		+ " TEXT, "
				+ KEY_ADMIN4_IDDISPOSITIVO 	+ " TEXT, "
				+ KEY_ADMIN5_FECHA 			+ " DATE, "
				+ KEY_ADMIN6_IDSERVIDOR 	+ " TEXT, "
				+ KEY_ADMIN7_COUNT 			+ " INTEGER, "
				+ KEY_ADMIN8_SINC_AUTO      + " TEXT, "
				+ KEY_ADMIN9_TIPO_PERMISO	+ " TEXT "
				+")";
		db.execSQL(CREATE_ADMINISTRACION_TABLE);
		
		String CREATE_VISITAS_TABLE = "CREATE TABLE " + TABLE_VISITAS + "("
				+ KEY_VIS0_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ KEY_VIS1_RAZON 			+ " TEXT, "
				+ KEY_VIS2_RECOMENDACION 	+ " TEXT, "
				+ KEY_VIS3_ZONA 			+ " TEXT, "
				+ KEY_VIS4_FECHA 			+ " DATE, "
				+ KEY_VIS5_NOMCONTACTO 		+ " TEXT, "
				+ KEY_VIS6_CODAGRONOMO		+ " TEXT, "
				+ KEY_VIS7_LLAVE 			+ " TEXT, "
				+ KEY_VIS8_IDLOTE 			+ " TEXT, "
				+ KEY_VIS9_DESCLOTE			+ " TEXT, "
				+ KEY_VIS10_ZAFRA 			+ " TEXT, "
				+ KEY_VIS11_TIPOVISITA		+ " TEXT, "
				+ KEY_VIS12_CODTIPO			+ " TEXT, "
				+ KEY_VIS13_CODMOTIVO		+ " TEXT, "
				+ KEY_VIS14_CODRESULTADO	+ " TEXT, "
				+ KEY_VIS15_QUIENRECIBE		+ " TEXT, "
				+ KEY_VIS16_HORAVISITA		+ " TEXT, "
				+ KEY_VIS17_CODFINCA		+ " TEXT,  "
				+ KEY_VIS18_NOMFINCA		+ " TEXT, "
				+ KEY_VIS19_CODPROV			+ " TEXT, "
				+ KEY_VIS20_NOMPROV			+ " TEXT  "
				+")";

		db.execSQL(CREATE_VISITAS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINITRACION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOVISITAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECUENCIA_PARALABORES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APLICACIONES_LABORES_ENCABEZADO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APLICACIONES_LABORES_DETALLES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS_LABORES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOTES_SELECT);

		onCreate(db);
	}

	public void onDeleteTablas(SQLiteDatabase db) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINITRACION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOVISITAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECUENCIA_PARALABORES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APLICACIONES_LABORES_ENCABEZADO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APLICACIONES_LABORES_DETALLES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS_LABORES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOTES_SELECT);

	}

}