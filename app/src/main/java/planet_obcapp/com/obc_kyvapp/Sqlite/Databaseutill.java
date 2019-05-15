package planet_obcapp.com.obc_kyvapp.Sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Databaseutill extends SQLiteOpenHelper

{
	 private static String DB_PATH = "";
	    private String DB_NAME = "";
	    SQLiteDatabase myDataBase;
	    private final Context myContext;
	    private static Databaseutill dbConnection;

	    public Databaseutill(Context context) {
	        super(context, "DBName.sqlite", null, 1);
	        this.myContext = context;
	        DB_NAME = "DBName.sqlite";
	        // default db path for android
	        DB_PATH = "/data/data/"
	                + context.getApplicationContext().getPackageName()
	                + "/databases/";
	     //  File sdcard = Environment.getExternalStorageDirectory();

	      //  DB_PATH = sdcard.getAbsolutePath() + File.separator+ "download" + File.separator + DB_NAME;
	    }

	    public static synchronized Databaseutill getDBAdapterInstance(Context context) {
	        if (dbConnection == null) {
	            dbConnection = new Databaseutill(context);
	        }
	        return dbConnection;
	    }

	    public void createDatabase() throws IOException {
	        boolean dbExist = checkDataBase();
	        if (!dbExist) {
	            this.getReadableDatabase();
	           // copyDataBase();
	        }else{
	        	try {
	        		//copyDataBase();
					myDataBase=openDataBase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }

	    public boolean checkDataBase() {
	        SQLiteDatabase checkDB = null;
	        try {
	        	String myPath=DB_PATH+DB_NAME;
	            //String myPath = DB_PATH ;//+ DB_NAME;
	            checkDB = SQLiteDatabase.openDatabase(myPath, null,
	                    SQLiteDatabase.OPEN_READONLY);

	        }
	        catch (SQLiteException e)
	        {
	        	e.getMessage();
	        	//Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	        if (checkDB != null) {
	            checkDB.close();
	        }
	        boolean val=false;
	        if(checkDB != null)
	        {
	        	val=true;
	        }

	        return val;
	    }


	    public boolean tb_exist(String tb_name)
	    {
	    	boolean result=false;
	    	if(checkDataBase())
	    	{
	    	   Cursor crs;

	    	    try{
	    		     openDataBase();
	    	         String qury="select * from "+tb_name+"";
	    	         crs=myDataBase.rawQuery(qury, null);

	    	        }
	    	    catch (Exception e)
	    	        {
				     return false;
			        }
	    	    result= true;
	    	}


	    		return result;

	    }

	    public void copyDataBase() throws IOException {

	        InputStream myInput = myContext.getAssets().open(DB_NAME);
	       String outFileName = DB_PATH + DB_NAME;
	        //String outFileName = DB_PATH ;//+ DB_NAME;
	        OutputStream myOutput = new FileOutputStream(outFileName);

	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = myInput.read(buffer)) > 0) {
	            myOutput.write(buffer, 0, length);
	        }
	        myOutput.flush();
	        myOutput.close();
	        myInput.close();
	    }

	    public synchronized SQLiteDatabase openDataBase() throws SQLException {
	    	String myPath = DB_PATH + DB_NAME;
	      // String myPath = DB_PATH ;//+ DB_NAME;
	        //myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    	 if(myDataBase==null){
			        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);}
			    	else{

			    		myDataBase=this.getReadableDatabase();
			    	}
	        return myDataBase;
	    }

	    @Override
	    public synchronized void close() {
	        if (myDataBase != null)
	            myDataBase.close();
	        super.close();
	    }

	    // db create data base

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    }

	    // ----------------------- DB Operations ------------------------------

	    public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
	                                      String whereClause, String whereArgs[], String groupBy,
	                                      String having, String orderBy) {
	        return myDataBase.query(tableName, tableColumns, whereClause, whereArgs,
	                groupBy, having, orderBy);
	    }

	    public ArrayList DBAdapter(String tableName, String[] tableColumns,
	                               String whereClause, String whereArgs[], String groupBy,
	                               String having, String orderBy) {

	        ArrayList retList = new ArrayList();
	        ArrayList list;
	        Cursor cursor = myDataBase.query(tableName, tableColumns, whereClause, whereArgs,
	                groupBy, having, orderBy);
	        if (cursor.moveToFirst()) {
	            do {
	                list = new ArrayList();
	                for(int i=0; i<cursor.getColumnCount(); i++){
	                    list.add( cursor.getString(i) );
	                }
	                retList.add(list);
	            } while (cursor.moveToNext());
	        }

	        return retList;
	    }
	    public  int updateRecordsInDB(String tableName, ContentValues initialValues, String whereClause, String whereArgs[]) {

            int re = 0;
            try {
              return myDataBase.update(tableName, initialValues, whereClause, whereArgs);
            } catch (Exception e) {
            }
            return re;
        }

	    public long insertRecordsToDB(String tableName, String nullColumnHack, ContentValues contentValues) {
	        return myDataBase.insert(tableName, nullColumnHack, contentValues);
	    }
	 public synchronized String execQuery(String sql) {
		 String retval = "0";
		 try {
			 myDataBase.execSQL(sql);
			 Log.d("sql", sql);
			 retval = "1";
		 } catch (Exception e) {
			 retval = "0";
			 e.getMessage();
			 Log.d("sql", e.getMessage());
		 }
		 return retval;
	 }
	 
	 public synchronized Cursor selectData(String query)
	 { Cursor cur=null;
	 try
	 {
		openDataBase();
	    cur=myDataBase.rawQuery(query, null);
	    int s=cur.getCount();
	  //  close();
	 }
	 catch (Exception e) {
		return cur;
	}
	    return cur;
	 }
	public synchronized int count(String query)
	{ Cursor cur=null;
		int s=0;
		try
		{
			openDataBase();
			cur=myDataBase.rawQuery(query, null);
			 s=cur.getCount();
			//  close();
		}
		catch (Exception e) {
			return s;
		}
		return s;
	}
	 
	 public void delete_tbl(String query)
	 {
		 try
		 {
		 openDataBase();
		 myDataBase.execSQL(query);
		 }
		 catch (Exception e) {
			e.getMessage();
		}
		 
	 }
	    public int deleteRecordFromDB(String tableName, String whereClause,
	                                  String[] whereArgs) {
	        return myDataBase.delete(tableName, whereClause, whereArgs);
	    }
	 
	    public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
	        return myDataBase.rawQuery(query, selectionArgs);
	    }
	 
	    public ArrayList selectRecordsFromDBList(String query, String[] selectionArgs) {
	        ArrayList retList = new ArrayList();
	        ArrayList list;
	        Cursor cursor = myDataBase.rawQuery(query, selectionArgs);
	        if (cursor.moveToFirst()) {
	            do {
	                list = new ArrayList();
	                for(int i=0; i<cursor.getColumnCount(); i++){
	                    list.add( cursor.getString(i) );
	                }
	                retList.add(list);
	            } while (cursor.moveToNext());
	        }
	        if (cursor != null && !cursor.isClosed()) {
	            cursor.close();
	        }
	        return retList;
	    }
	    public synchronized void mergeDataBase(String ATTACH_DB_PATH,String ATTACH_DB_NAME,String Table,String column,int chk) throws IOException {
            String myPath = DB_PATH + DB_NAME;
            try{
               
            if(myDataBase==null){
            	
            	File fb=new File(myPath);
            	if(!fb.exists()){
            	fb.createNewFile();}
                myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                     }else{
                             myDataBase=this.getWritableDatabase();
                     }
            
            String []columnlist=column.split(",");
                                    
           try
         {
              String query="drop table "+Table;
              myDataBase.execSQL(query);

         }
         catch (Exception e)
         {
        e.getMessage();
}
                    
                    String querycr="create table "+Table+"(";                            
                    for(int strCount=0;strCount<columnlist.length;strCount++){
                                                                                                   
                                    querycr+=columnlist[strCount]+" TEXT,";
                                                                                                                           
                    }
                    querycr =querycr.substring(0,(querycr.length()-1));                    
                    querycr+=",statusl text)";
                    myDataBase.execSQL(querycr);
            //
            //SQLiteDatabase myInternalDatabase= myDataBase;
            
         
       /* myInternalDatabase.execSQL("ATTACH DATABASE '" + ATTACH_DB_PATH
                + File.separator + ATTACH_DB_NAME + "' AS New_DB");
            
        myInternalDatabase
                .execSQL("INSERT INTO "+Table+" SELECT * FROM New_DB."+Table+";");
        myInternalDatabase.close();*/
            
            }catch(Exception er)
            {
                    er.getMessage();
            }
            
            
    }

 
}
