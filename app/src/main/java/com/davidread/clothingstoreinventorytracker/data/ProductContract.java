package com.davidread.clothingstoreinventorytracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * {@link ProductContract} is a class that defines constants to help work with the content URIs,
 * column names, and other features of the {@link ProductProvider}.
 */
public final class ProductContract {

    /**
     * {@link String} that uniquely identifies the {@link ProductProvider}.
     */
    public static final String CONTENT_AUTHORITY = "com.davidread.clothingstoreinventorytracker";

    /**
     * Base content {@link Uri} for using the {@link ProductProvider}.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * {@link String} to append to {@link #BASE_CONTENT_URI} to access product data
     * from the {@link ProductProvider}.
     */
    public static final String PATH_PRODUCTS = "products";

    /**
     * Private constructor to prevent accidental instantiation of {@link ProductContract}.
     */
    private ProductContract() {
    }

    /**
     * {@link ProductEntry} is a class defines constants to help work with product data from the
     * {@link ProductProvider}.
     */
    public static class ProductEntry implements BaseColumns {

        /**
         * Content {@link Uri} for using product data from the {@link ProductProvider}.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * {@link String} specifying the MIME type of {@link #CONTENT_URI} for a single piece of
         * product data.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * {@link String} specifying the MIME type of {@link #CONTENT_URI} for a list of product
         * data.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * {@link String} constants defining the products table's name and columns.
         */
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER = "supplier";
        public static final String COLUMN_PICTURE = "picture";
    }
}
