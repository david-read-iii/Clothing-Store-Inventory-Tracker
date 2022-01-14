package com.davidread.clothingstoreinventorytracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * {@link ProductDbHelper} is a helper class for SQLite database creation and version management
 * within this application.
 */
public class ProductDbHelper extends SQLiteOpenHelper {

    /**
     * {@link String} name for the database file.
     */
    private static final String DB_NAME = "products.db";

    /**
     * Int version for the database schema. Is constant because the database schema is not upgraded
     * in this application.
     */
    private static final int DB_VERSION = 1;

    /**
     * Constructs a new {@link ProductDbHelper}.
     *
     * @param context {@link Context} for the superclass.
     */
    public ProductDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Callback method invoked when the database is created for the first time. It simply
     * initializes the database by creating a new products table.
     *
     * @param db {@link SQLiteDatabase} being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " ("
                + ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductContract.ProductEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ProductContract.ProductEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ProductContract.ProductEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + ProductContract.ProductEntry.COLUMN_SUPPLIER + " TEXT NOT NULL, "
                + ProductContract.ProductEntry.COLUMN_PICTURE + " BLOB NOT NULL);";
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    /**
     * Callback method invoked when the database schema is upgraded. The database schema will not
     * be upgraded, so it does nothing.
     *
     * @param db         {@link SQLiteDatabase} being upgraded.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
