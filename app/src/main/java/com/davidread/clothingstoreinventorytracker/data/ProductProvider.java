package com.davidread.clothingstoreinventorytracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * {@link ProductProvider} is a class that defines a content provider for products data.
 */
public class ProductProvider extends ContentProvider {

    /**
     * URI matcher code for a content URI referring to all products.
     */
    private static final int URI_CODE_ALL_PRODUCTS = 100;

    /**
     * URI matcher code for a content URI referring to a single product.
     */
    private static final int URI_CODE_SINGLE_PRODUCT = 101;

    /**
     * {@link UriMatcher} for matching a {@link Uri} to a URI matcher code.
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initialization of {@link #uriMatcher}.
    static {
        uriMatcher.addURI(
                ProductContract.CONTENT_AUTHORITY,
                ProductContract.PATH_PRODUCTS, URI_CODE_ALL_PRODUCTS
        );
        uriMatcher.addURI(
                ProductContract.CONTENT_AUTHORITY,
                ProductContract.PATH_PRODUCTS + "/#", URI_CODE_SINGLE_PRODUCT
        );
    }

    /**
     * {@link ProductDbHelper} for getting SQLite database references.
     */
    private ProductDbHelper productDbHelper;

    /**
     * Callback method invoked on this content provider's startup. It simply initializes
     * {@link #productDbHelper}.
     *
     * @return Whether this content provider was successfully loaded.
     */
    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    /**
     * Returns the MIME type of the data stored in this content provider that matches with the
     * passed {@link Uri}.
     *
     * @param uri Content URI to query.
     * @return A MIME type {@link String}. Is null if there is no type.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case URI_CODE_ALL_PRODUCTS:
                return ProductContract.ProductEntry.CONTENT_LIST_TYPE;
            case URI_CODE_SINGLE_PRODUCT:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    /**
     * Insert new data into this content provider.
     *
     * @param uri    Content URI of the insertion request.
     * @param values A set of column name/value pairs to add.
     * @return The content URI for the newly inserted data. Is null if the insert request fails.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues values) {

        // Return null if ContentValues are invalid.
        if (values.size() != 5 || !hasValidContentValues(values)) {
            return null;
        }

        // Perform the appropriate insert operation given the passed content URI.
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        long insertId;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case URI_CODE_ALL_PRODUCTS:
                insertId = db.insert(
                        ProductContract.ProductEntry.TABLE_NAME,
                        null,
                        values
                );
                break;
            default:
                insertId = -1;
        }

        // Return null if the insertion operation failed.
        if (insertId == -1) {
            return null;
        }

        // Notify listeners that the data at this content URI has changed.
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, insertId);
    }

    /**
     * Query data from this content provider.
     *
     * @param uri           Content URI of the query request.
     * @param projection    The list of columns to put into the cursor. If null, then all columns
     *                      are included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings. This value may be null.
     * @param sortOrder     How the rows in the cursor should be sorted. If null, then the default
     *                      sort is applied.
     * @return A {@link Cursor} containing the queried data. Is null if the query request failed.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Perform the appropriate query operation given the passed content URI.
        SQLiteDatabase db = productDbHelper.getReadableDatabase();
        Cursor cursor;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case URI_CODE_ALL_PRODUCTS:
                cursor = db.query(
                        ProductContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_CODE_SINGLE_PRODUCT:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(
                        ProductContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                cursor = null;
        }

        // Register this content URI to listen for changes if the query operation succeeded.
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    /**
     * Update data in this content provider.
     *
     * @param uri           Content URI of the update request.
     * @param values        A set of column name/value pairs to update.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings. This value may be null.
     * @return The number of rows updated. Is -1 if the update request failed.
     */
    @Override
    public int update(@NonNull Uri uri, @NonNull ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        // Return 0 if ContentValues is empty.
        if (values.size() == 0) {
            return 0;
        }

        // Return -1 if ContentValues are invalid.
        if (!hasValidContentValues(values)) {
            return -1;
        }

        // Perform the appropriate update operation given the passed content URI.
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        int countRowsUpdated;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case URI_CODE_ALL_PRODUCTS:
                countRowsUpdated = db.update(
                        ProductContract.ProductEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case URI_CODE_SINGLE_PRODUCT:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                countRowsUpdated = db.update(
                        ProductContract.ProductEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                countRowsUpdated = -1;
        }

        /* Notify listeners that the data at this content URI has changed if the update operation
         * succeeded. */
        if (countRowsUpdated != -1 && countRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return countRowsUpdated;
    }

    /**
     * Delete data from this content provider.
     *
     * @param uri           Content URI of the delete request.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings. This value may be null.
     * @return The number of rows deleted. Is -1 if the delete request failed.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        // Perform the appropriate delete operation given the passed content URI.
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        int countRowsDeleted;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case URI_CODE_ALL_PRODUCTS:
                countRowsDeleted = db.delete(
                        ProductContract.ProductEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case URI_CODE_SINGLE_PRODUCT:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                countRowsDeleted = db.delete(
                        ProductContract.ProductEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                countRowsDeleted = -1;
        }

        /* Notify listeners that the data at this content URI has changed if the delete operation
         * succeeded. */
        if (countRowsDeleted != -1 && countRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return countRowsDeleted;
    }

    /**
     * Returns true if a {@link ContentValues} is passed that can be put into this content provider.
     *
     * @param values {@link ContentValues} to query.
     * @return Whether the {@link ContentValues} can be put into this content provider.
     */
    private boolean hasValidContentValues(@NonNull ContentValues values) {

        // Verify that the data in the name column is a nonempty String.
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_NAME)) {
            Object name = values.get(ProductContract.ProductEntry.COLUMN_NAME);
            if (!(name instanceof String)
                    || ((String) name).isEmpty()) {
                return false;
            }
        }

        // Verify that the data in the price column is a non-negative Integer.
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRICE)) {
            Object price = values.get(ProductContract.ProductEntry.COLUMN_PRICE);
            if (!(price instanceof Integer)
                    || ((Integer) price) < 0) {
                return false;
            }
        }

        // Verify that the data in the quantity column is a non-negative Integer.
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_QUANTITY)) {
            Object quantity = values.get(ProductContract.ProductEntry.COLUMN_QUANTITY);
            if (!(quantity instanceof Integer)
                    || ((Integer) quantity) < 0) {
                return false;
            }
        }

        // Verify that the data in the supplier column is a nonempty String.
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_SUPPLIER)) {
            Object supplier = values.get(ProductContract.ProductEntry.COLUMN_SUPPLIER);
            if (!(supplier instanceof String)
                    || ((String) supplier).isEmpty()) {
                return false;
            }
        }

        // Verify that the data in the picture column is a byte array.
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PICTURE)) {
            Object picture = values.get(ProductContract.ProductEntry.COLUMN_PICTURE);
            if (!(picture instanceof byte[])) {
                return false;
            }
        }

        return true;
    }
}
