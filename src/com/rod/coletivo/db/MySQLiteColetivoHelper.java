package com.rod.coletivo.db;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rod.coletivo.entidade.PossivelLinha;
 
public class MySQLiteColetivoHelper extends SQLiteOpenHelper {
	PossivelLinha possivelLinha = new PossivelLinha();
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "ColetivoDB";
 
    public MySQLiteColetivoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create possivelLinha table
        String CREATE_POSSIVELLINHA_TABLE = "create table if not exists possivellinha ( " +
                "idpossivellinha INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idlinha INTEGER, "+
                "numero TEXT, "+
                "nome TEXT, "+
                "datahora INTEGER, "+
                "lat TEXT, "+
                "lng TEXT, "+
                "seq INTEGER, "+
                "ida INTEGER )";
 
        // create books table
        db.execSQL(CREATE_POSSIVELLINHA_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS possivellinha");
 
        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------
 
    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
 
    // Books table name
    private static final String TABLE_POSSIVELLINHA = "possivellinha";
 
    // Books TABLE_POSSIVELLINHA Columns names
    private static final String KEY_IDPOSSIVELLINHA = "idpossivellinha";
    private static final String KEY_IDLINHA = "idlinha";
    private static final String KEY_NUMERO = "numero";
    private static final String KEY_NOME = "nome";
    private static final String KEY_DATAHORA = "datahora";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_SEQ = "seq";
    private static final String KEY_IDA = "ida";
    
    private static final String[] COLUMNS_POSSIVELLINHA = {KEY_IDPOSSIVELLINHA,KEY_IDLINHA, KEY_NUMERO, KEY_NOME, KEY_DATAHORA, KEY_LAT, KEY_LNG, KEY_SEQ, KEY_IDA};
 
    public void addPossivelLinha(PossivelLinha possivelLinha){
        //Log.d("mytag", "add->"+possivelLinha.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_IDLINHA, possivelLinha.idlinha);
        values.put(KEY_NUMERO, possivelLinha.numero);
        values.put(KEY_NOME, possivelLinha.nome);
        values.put(KEY_DATAHORA, possivelLinha.datahora);
        values.put(KEY_LAT, possivelLinha.lat);
        values.put(KEY_LNG, possivelLinha.lng);
        values.put(KEY_SEQ, possivelLinha.seq);
        values.put(KEY_IDA, possivelLinha.ida);
        
         
 
        // 3. insert
        db.insert(TABLE_POSSIVELLINHA, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close();
    }
 
    public PossivelLinha getPossivelLinha(int id){
 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor =
                db.query(TABLE_POSSIVELLINHA, // a. table
                COLUMNS_POSSIVELLINHA, // b. column names
                " idpossivelLinha = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build book object
        PossivelLinha possivelLinha = new PossivelLinha();
        possivelLinha.idpossivellinha = cursor.getInt(0);
        possivelLinha.idlinha = cursor.getInt(1);
        possivelLinha.numero = cursor.getString(2);
        possivelLinha.nome = cursor.getString(3);
        possivelLinha.datahora = cursor.getLong(4);
        possivelLinha.lat = cursor.getDouble(5);
        possivelLinha.lng = cursor.getDouble(6);
        possivelLinha.seq = cursor.getInt(7);
        possivelLinha.ida = cursor.getInt(8);
        
        //Log.d("mytag", "getbook->"+possivelLinha.toString());
        if (cursor != null)
            cursor.close();
        // 5. return book
        return possivelLinha;
    }
 
    // Get All Books
    public List<PossivelLinha> getAllPossivelLinha() {
        List<PossivelLinha> possivelLinhas = new LinkedList<PossivelLinha>();
 
        // 1. build the query
        String query = "SELECT  idpossivellinha, idlinha, numero, nome, datahora, lat, lng, seq, ida, count(*) cont FROM " + TABLE_POSSIVELLINHA + " group by idlinha order by cont desc";
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        PossivelLinha possivelLinha = null;
        if (cursor.moveToFirst()) {
            do {
            	possivelLinha = new PossivelLinha();
            	possivelLinha.idpossivellinha = cursor.getInt(0);
                possivelLinha.idlinha = cursor.getInt(1);
                possivelLinha.numero = cursor.getString(2);
                possivelLinha.nome = cursor.getString(3);
                possivelLinha.datahora = cursor.getLong(4);
                possivelLinha.lat = cursor.getDouble(5);
                possivelLinha.lng = cursor.getDouble(6);
                possivelLinha.seq = cursor.getInt(7);
                possivelLinha.ida = cursor.getInt(8);
                possivelLinha.cont = cursor.getInt(9);
                // Add book to books
                possivelLinhas.add(possivelLinha);
            } while (cursor.moveToNext());
        }
 
        //Log.d("mytag", "listBook->"+books.toString());
        if (cursor != null)
            cursor.close();
        // return books
        return possivelLinhas;
    }
    public List<PossivelLinha> getAllPossivelLinhaFull() {
        List<PossivelLinha> possivelLinhas = new LinkedList<PossivelLinha>();
 
        // 1. build the query
        String query = "SELECT idpossivellinha, idlinha, numero, nome, datahora, lat, lng, seq, ida FROM " + TABLE_POSSIVELLINHA + " order by idpossivellinha asc";
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        PossivelLinha possivelLinha = null;
        if (cursor.moveToFirst()) {
            do {
            	//Log.grava("x.csv", cursor.toString());
            	possivelLinha = new PossivelLinha();
            	possivelLinha.idpossivellinha = cursor.getInt(0);
                possivelLinha.idlinha = cursor.getInt(1);
                possivelLinha.numero = cursor.getString(2);
                possivelLinha.nome = cursor.getString(3);
                possivelLinha.datahora = cursor.getLong(4);
                possivelLinha.lat = cursor.getDouble(5);
                possivelLinha.lng = cursor.getDouble(6);
                possivelLinha.seq = cursor.getInt(7);
                possivelLinha.ida = cursor.getInt(8);
                // Add book to books
                possivelLinhas.add(possivelLinha);
            } while (cursor.moveToNext());
        }
 
        //Log.d("mytag", "listBook->"+books.toString());
        if (cursor != null)
            cursor.close();
        // return books
        return possivelLinhas;
    }
    
    public List<PossivelLinha> getSentidolLinha(int idlinha, int ida) {
        List<PossivelLinha> possivelLinhas = new LinkedList<PossivelLinha>();
 
        // 1. build the query
        String query = "SELECT * FROM " + TABLE_POSSIVELLINHA + " where idlinha = " + idlinha + " and ida = " + ida + " order by idpossivellinha";
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        PossivelLinha possivelLinha = null;
        if (cursor.moveToFirst()) {
            do {
            	possivelLinha = new PossivelLinha();
            	possivelLinha.idpossivellinha = cursor.getInt(0);
                possivelLinha.idlinha = cursor.getInt(1);
                possivelLinha.numero = cursor.getString(2);
                possivelLinha.nome = cursor.getString(3);
                possivelLinha.datahora = cursor.getLong(4);
                possivelLinha.lat = cursor.getDouble(5);
                possivelLinha.lng = cursor.getDouble(6);
                possivelLinha.seq = cursor.getInt(7);                
                possivelLinha.ida = cursor.getInt(8);
                //possivelLinha.cont = cursor.getInt(7);
                // Add book to books
                possivelLinhas.add(possivelLinha);
            } while (cursor.moveToNext());
        }
 
        //Log.d("mytag", "listBook->"+books.toString());
        if (cursor != null)
            cursor.close();
        // return books
        return possivelLinhas;
    }
    
 // Get All Books
    public PossivelLinha getUltimoRegistro() {
        String query = "SELECT * FROM " + TABLE_POSSIVELLINHA + " order by idpossivellinha desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        PossivelLinha possivelLinha = new PossivelLinha();
        
        if (cursor.getCount() > 0){
            
        	cursor.moveToFirst();
 	        possivelLinha.idpossivellinha = cursor.getInt(0);
	        possivelLinha.idlinha = cursor.getInt(1);
	        possivelLinha.numero = cursor.getString(2);
	        possivelLinha.nome = cursor.getString(3);
	        possivelLinha.datahora = cursor.getLong(4);
	        possivelLinha.lat = cursor.getDouble(5);
	        possivelLinha.lng = cursor.getDouble(6);
	        possivelLinha.seq = cursor.getInt(7);
	        possivelLinha.ida = cursor.getInt(8);
        }
        
        if (cursor != null)
            cursor.close();
        
        return possivelLinha;
    }
    
    public PossivelLinha getUltimoRegistroIdlinha(int idlinha) {
        String query = "SELECT * FROM " + TABLE_POSSIVELLINHA + " where idlinha = "+idlinha+" order by idpossivellinha desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        PossivelLinha possivelLinha = null;
        
        if (cursor.getCount() > 0){
        	possivelLinha = new PossivelLinha();
        	cursor.moveToFirst();
 	        possivelLinha.idpossivellinha = cursor.getInt(0);
	        possivelLinha.idlinha = cursor.getInt(1);
	        possivelLinha.numero = cursor.getString(2);
	        possivelLinha.nome = cursor.getString(3);
	        possivelLinha.datahora = cursor.getLong(4);
	        possivelLinha.lat = cursor.getDouble(5);
	        possivelLinha.lng = cursor.getDouble(6);
	        possivelLinha.seq = cursor.getInt(7);
	        possivelLinha.ida = cursor.getInt(8);
        }
        
        if (cursor != null)
            cursor.close();
        
        return possivelLinha;
    }
 
     // Updating single book
    public int updatePossivelLinha(PossivelLinha possivelLinha) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        //values.put(KEY_IDPOSSIVELLINHA, possivelLinha.getIdpossivelLinha());
        values.put(KEY_IDLINHA, possivelLinha.idlinha);
        values.put(KEY_NUMERO, possivelLinha.numero);
        values.put(KEY_NOME, possivelLinha.nome);
        values.put(KEY_DATAHORA, possivelLinha.datahora);
        values.put(KEY_LAT, possivelLinha.lat);
        values.put(KEY_LNG, possivelLinha.lng);
        values.put(KEY_SEQ, possivelLinha.seq);
        values.put(KEY_IDA, possivelLinha.ida);
 
        // 3. updating row
        int i = db.update(TABLE_POSSIVELLINHA, //table
                values, // column/value
                KEY_IDPOSSIVELLINHA+" = ?", // selections
                new String[] { String.valueOf(possivelLinha.idpossivellinha) }); //selection args
 
        // 4. close
        db.close();
 
        return i;
 
    }
 
 // Deleting single book
    public void deletePossivelLinha(PossivelLinha possivelLinha) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_POSSIVELLINHA,
                KEY_IDPOSSIVELLINHA+" = ?",
                new String[] { String.valueOf(possivelLinha.idpossivellinha) });
 
        // 3. close
        db.close();
 
        //Log.d("mytag", "delete->"+book.toString());
 
    }
 // Deleting single book
    public void deleteAllPossivelLinha() {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.execSQL("delete from "+TABLE_POSSIVELLINHA);
        // 3. close
        db.close();
 
       // Log.d("mytag", "delete->"+book.toString());
 
    }
}