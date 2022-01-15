package com.davidread.clothingstoreinventorytracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.provider.ProviderTestRule;

import com.davidread.clothingstoreinventorytracker.data.ProductContract;
import com.davidread.clothingstoreinventorytracker.data.ProductProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * {@link ProductProviderTest} provides several unit tests to verify the correctness of
 * {@link ProductProvider}.
 */
@RunWith(AndroidJUnit4.class)
public class ProductProviderTest {

    /**
     * {@link ContentResolver} for accessing the methods of the {@link ProductProvider}.
     */
    private ContentResolver contentResolver;

    /**
     * {@link ProviderTestRule} for mocking {@link ProductProviderTest#contentResolver}.
     */
    @Rule
    public ProviderTestRule providerTestRule =
            new ProviderTestRule.Builder(ProductProvider.class, ProductContract.CONTENT_AUTHORITY)
                    .build();

    /**
     * Callback method invoked before each test method. It simply initializes the
     * {@link ProductProviderTest#contentResolver}.
     */
    @Before
    public void setUp() {
        contentResolver = providerTestRule.getResolver();
    }

    /**
     * Verifies that {@link ProductProvider#insert(Uri, ContentValues)} returns not null when
     * inserting valid {@link ContentValues}.
     */
    @Test
    public void insert_ValidValues_ReturnsNotNull() {

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Red T-Shirt");
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, 1000);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, 10);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, "Garment District");
        values.put(ProductContract.ProductEntry.COLUMN_PICTURE, new byte[]{0, 1, 2, 3});

        Uri insertUri = contentResolver.insert(ProductContract.ProductEntry.CONTENT_URI, values);

        assertNotNull(insertUri);
    }

    /**
     * Verifies that {@link ProductProvider#insert(Uri, ContentValues)} returns null when attempting
     * to insert invalid {@link ContentValues}.
     */
    @Test
    public void insert_InvalidValues_ReturnsNull() {

        // Verify that a row with invalid amount of attributes cannot be inserted.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Blue T-Shirt");

        Uri insertUri = contentResolver.insert(ProductContract.ProductEntry.CONTENT_URI, values);

        assertNull(insertUri);

        // Verify that a row with an invalid price cannot be inserted.
        ContentValues values1 = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Yellow T-Shirt");
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, -23);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, 55);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, "Bed Bath and Beyond");
        values.put(ProductContract.ProductEntry.COLUMN_PICTURE, new byte[]{4, 5, 6, 7});

        Uri insertUri1 = contentResolver.insert(ProductContract.ProductEntry.CONTENT_URI, values1);

        assertNull(insertUri1);
    }

    /**
     * Verifies that {@link ProductProvider#query(Uri, String[], String, String[], String)} returns
     * not null when querying all rows.
     */
    @Test
    public void query_AllRows_ReturnsNotNull() {

        Cursor cursor = contentResolver.query(
                ProductContract.ProductEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertNotNull(cursor);
    }

    /**
     * Verifies that {@link ProductProvider#update(Uri, ContentValues, String, String[])} returns
     * a non-error int when updating all rows with valid {@link ContentValues}.
     */
    @Test
    public void update_ValidValues_ReturnsNotError() {

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PICTURE, new byte[]{8, 9, 10, 11});

        int countRowsUpdated = contentResolver.update(
                ProductContract.ProductEntry.CONTENT_URI,
                values,
                null,
                null
        );

        assertNotEquals(countRowsUpdated, -1);
    }

    /**
     * Verifies that {@link ProductProvider#update(Uri, ContentValues, String, String[])} returns
     * an error int when updating all rows with invalid {@link ContentValues}.
     */
    @Test
    public void update_InvalidValues_ReturnsError() {

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, -616);

        int countRowsUpdated = contentResolver.update(
                ProductContract.ProductEntry.CONTENT_URI,
                values,
                null,
                null
        );

        assertEquals(countRowsUpdated, -1);
    }

    /**
     * Verifies that {@link ProductProvider#delete(Uri, String, String[])} returns a non-error int
     * when deleting all rows.
     */
    @Test
    public void delete_AllRows_ReturnNotError() {

        int countRowsDeleted = contentResolver.delete(
                ProductContract.ProductEntry.CONTENT_URI,
                null,
                null
        );

        assertNotEquals(countRowsDeleted, -1);
    }
}
