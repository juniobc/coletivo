package com.rod.coletivo.db;

import java.util.LinkedList;
import java.util.List;

import com.rod.coletivo.entidade.Rota;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class MySQLiteRotaHelper extends SQLiteOpenHelper {
	Rota rota = new Rota();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ColetivoDB";
 
    public MySQLiteRotaHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create rota table
        String CREATE_ROTA_TABLE = "create table if not exists rota ( " +
                "idrota INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idlinha INTEGER, "+
                "seq INTEGER, "+
                "lat DOUBLE, "+
                "lng DOUBLE, "+                
                "ida INTEGER )";
 
        // create books table
        db.execSQL(CREATE_ROTA_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS rota");
 
        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------
 
    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
 
    // Books table name
    private static final String TABLE_ROTA = "rota";
 
    // Books Table Columns names
    private static final String KEY_IDROTA = "idrota";
    private static final String KEY_IDLINHA = "idlinha";
    private static final String KEY_SEQ = "seq";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_IDA = "ida";
 
    private static final String[] COLUMNS = {KEY_IDROTA,KEY_IDLINHA, KEY_SEQ, KEY_LAT, KEY_LNG, KEY_IDA};
 
    public void addRota(Rota rota){
        Log.d("mytag", "add->"+rota.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_IDROTA, rota.getIdrota());
        values.put(KEY_IDLINHA, rota.getIdlinha());
        values.put(KEY_SEQ, rota.getSeq());
        values.put(KEY_LAT, rota.getLat());
        values.put(KEY_LNG, rota.getLng());
        values.put(KEY_IDA, rota.getIda());
        
         
 
        // 3. insert
        db.insert(TABLE_ROTA, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close();
    }
 
    public Rota getRota(int id){
 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor =
                db.query(TABLE_ROTA, // a. table
                COLUMNS, // b. column names
                " idrota = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build book object
        Rota rota = new Rota();
        rota.setIdrota(cursor.getInt(0));
        rota.setIdlinha(cursor.getInt(1));
        rota.setSeq(cursor.getInt(2));
        rota.setLat(cursor.getDouble(3));
        rota.setLng(cursor.getDouble(4));
        rota.setIda(cursor.getInt(5));
 
        //Log.d("mytag", "getbook->"+rota.toString());
 
        // 5. return book
        return rota;
    }
 
    // Get All Books
    public List<Rota> getAllRotas() {
        List<Rota> rotas = new LinkedList<Rota>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ROTA;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        Rota rota = null;
        if (cursor.moveToFirst()) {
            do {
            	rota = new Rota();
            	rota.setIdrota(cursor.getInt(0));
                rota.setIdlinha(cursor.getInt(1));
                rota.setSeq(cursor.getInt(2));
                rota.setLat(cursor.getDouble(3));
                rota.setLng(cursor.getDouble(4));
                rota.setIda(cursor.getInt(5));
 
                // Add book to books
                rotas.add(rota);
            } while (cursor.moveToNext());
        }
 
        //Log.d("mytag", "listBook->"+books.toString());
 
        // return books
        return rotas;
    }
 
     // Updating single book
    public int updateRota(Rota rota) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        //values.put(KEY_IDROTA, rota.getIdrota());
        values.put(KEY_IDLINHA, rota.getIdlinha());
        values.put(KEY_SEQ, rota.getSeq());
        values.put(KEY_LAT, rota.getLat());
        values.put(KEY_LNG, rota.getLng());
        values.put(KEY_IDA, rota.getIda());
 
        // 3. updating row
        int i = db.update(TABLE_ROTA, //table
                values, // column/value
                KEY_IDROTA+" = ?", // selections
                new String[] { String.valueOf(rota.getIdrota()) }); //selection args
 
        // 4. close
        db.close();
 
        return i;
 
    }
 
 // Deleting single book
    public void deleteRota(Rota rota) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_ROTA,
                KEY_IDROTA+" = ?",
                new String[] { String.valueOf(rota.getIdrota()) });
 
        // 3. close
        db.close();
 
        //Log.d("mytag", "delete->"+book.toString());
 
    }
 // Deleting single book
    public void deleteAllRotas() {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.execSQL("delete from "+TABLE_ROTA);
        // 3. close
        db.close();
 
       // Log.d("mytag", "delete->"+book.toString());
 
    }
}