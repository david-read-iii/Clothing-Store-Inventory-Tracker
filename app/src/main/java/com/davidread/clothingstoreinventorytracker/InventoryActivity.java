package com.davidread.clothingstoreinventorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.davidread.clothingstoreinventorytracker.data.ProductContract.ProductEntry;

import java.util.Random;

/**
 * {@link InventoryActivity} is an activity class that provides a user interface for browsing a list
 * of products stored in the
 * {@link com.davidread.clothingstoreinventorytracker.data.ProductProvider}. It also provides
 * options to add dummy rows and delete all rows.
 */
public class InventoryActivity extends AppCompatActivity {

    /**
     * {@link ProductCursorAdapter} for adapting a {@link Cursor} of product data to be displayed
     * in a {@link RecyclerView}.
     */
    private ProductCursorAdapter productCursorAdapter;

    /**
     * {@link View} for displaying a message when the {@link RecyclerView} is empty.
     */
    private View emptyView;

    /**
     * Callback method invoked to initialize the activity. Initializes global helper objects, sets
     * up the activity's {@link RecyclerView}, and makes initial call to
     * {@link InventoryActivity#queryRows()}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize global ProductCursorAdapter with no Cursor to adapt yet.
        productCursorAdapter = new ProductCursorAdapter();

        // Initialize RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.inventory_recycler_view);
        recyclerView.setAdapter(productCursorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Initialize empty View.
        emptyView = findViewById(R.id.empty_view);

        // Initial call to query ProductProvider.
        queryRows();
    }

    /**
     * Callback method invoked to initialize this activity's action bar. It simply specifies the
     * action bar's layout.
     *
     * @param menu The options menu in which you place your items.
     * @return True for the menu to be displayed. False to hide it.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    /**
     * Callback method invoked when an item in the activity's action bar is selected. It specifies
     * what actions to take for each unique action button.
     *
     * @param item The menu item that was selected.
     * @return False to allow normal menu processing to proceed. True to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        /* Insert a row into the ProductProvider and re-query the ProductProvider when "Add Dummy
         * Row" is selected. */
        if (id == R.id.action_add_dummy) {
            insertDummyRow();
            queryRows();
            return true;
        }
        /* Delete all data from the ProductProvider and re-query the ProductProvider when "Delete
         * All Rows" is selected. */
        else if (id == R.id.action_delete_all) {
            deleteAllRows();
            queryRows();
            return true;
        }
        // Have superclass handle it for all other selections.
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inserts a row with random dummy attributes into the
     * {@link com.davidread.clothingstoreinventorytracker.data.ProductProvider}. It pops a toast if
     * this dummy insertion operation fails.
     */
    private void insertDummyRow() {

        // Some dummy name and supplier values.
        String[] dummyNames = {
                "Super Soft Icon T-Shirt",
                "AirFlex + Patched Stacked Skinny Jean",
                "Super Soft Icon Cargo Jogger",
                "Super Soft Vintage Vault Graphic T-Shirt",
                "Cozy Cabin Flannel",
                "Super Soft Thermal Hoodie Tee",
                "AirFlex + 5.5\" Denim Short",
                "Flex Longer Length Cargo Short",
                "Space Dye 3\" Classic Trunk Underwear",
                "Skate Sneaker"
        };

        String[] dummySuppliers = {
                "Grainger Industrial Supply",
                "Hudson Wholesale Inc.",
                "Regards Wholesale",
                "Garment Center Supplier Association",
                "eFashion Wholesale"
        };

        // Random for randomizing row attributes.
        Random random = new Random(System.currentTimeMillis());

        // Construct a ContentValues containing some random valid attributes of a dummy row.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME, dummyNames[random.nextInt(10)]);
        values.put(ProductEntry.COLUMN_PRICE, random.nextInt(10000));
        values.put(ProductEntry.COLUMN_QUANTITY, random.nextInt(1000));
        values.put(ProductEntry.COLUMN_SUPPLIER, dummySuppliers[random.nextInt(5)]);
        values.put(ProductEntry.COLUMN_PICTURE, new byte[]{
                (byte) (random.nextInt((127 - (-128)) + 1) + (-128)),
                (byte) (random.nextInt((127 - (-128)) + 1) + (-128)),
                (byte) (random.nextInt((127 - (-128)) + 1) + (-128)),
                (byte) (random.nextInt((127 - (-128)) + 1) + (-128))
        });

        // Perform the insert operation.
        Uri insertUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Pop a toast if the insertion operation fails.
        if (insertUri == null) {
            Toast.makeText(this, getString(R.string.insert_failed_message), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Queries the {@link com.davidread.clothingstoreinventorytracker.data.ProductProvider} for all
     * its data. It updates the {@link #productCursorAdapter} with the fetched {@link Cursor} and
     * pops a toast if the query operation fails.
     */
    private void queryRows() {

        // Perform the query operation.
        Cursor cursor = getContentResolver().query(
                ProductEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Pop a toast and return early if the query operation fails.
        if (cursor == null) {
            Toast.makeText(this, getString(R.string.query_failed_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // Update ProductCursorAdapter with new Cursor.
        productCursorAdapter.changeCursor(cursor);

        // Update visibility of empty View depending on the size of the Cursor.
        if (productCursorAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * Deletes all data from the
     * {@link com.davidread.clothingstoreinventorytracker.data.ProductProvider}. It pops a toast if
     * the delete operation fails.
     */
    private void deleteAllRows() {

        // Perform the delete operation.
        int countRowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);

        // Pop a toast if the delete operation fails.
        if (countRowsDeleted == -1) {
            Toast.makeText(this, getString(R.string.delete_failed_message), Toast.LENGTH_SHORT).show();
        }
    }
}