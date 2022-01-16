package com.davidread.clothingstoreinventorytracker;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidread.clothingstoreinventorytracker.data.ProductContract;

/**
 * {@link ProductCursorAdapter} is an adapter class that provides a binding from a {@link Cursor} of
 * data received from {@link com.davidread.clothingstoreinventorytracker.data.ProductProvider} to
 * views that are displayed within a {@link RecyclerView}. The {@link Cursor} should include the
 * id, name, price, and quantity columns in its projection.
 */
public class ProductCursorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * {@link Cursor} containing product data to be adapted.
     */
    private Cursor cursor;

    /**
     * Constructs a new {@link ProductCursorAdapter} with a null
     * {@link ProductCursorAdapter#cursor}.
     */
    public ProductCursorAdapter() {
        this.cursor = null;
    }

    /**
     * Constructs a new {@link ProductCursorAdapter} with a {@link ProductCursorAdapter#cursor} of
     * product data.
     *
     * @param cursor Product data received from
     *               {@link com.davidread.clothingstoreinventorytracker.data.ProductProvider}.
     */
    public ProductCursorAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Callback method invoked when {@link RecyclerView} needs a new {@link RecyclerView.ViewHolder}
     * of a given type to represent an item.
     *
     * @param parent   The {@link ViewGroup} into which the new {@link View} will be added after it
     *                 is bound to an adapter position.
     * @param viewType The view type of the new {@link View}.
     * @return A new {@link RecyclerView.ViewHolder} that holds a {@link View} of the given view
     * type.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    /**
     * Callback method invoked when {@link RecyclerView} needs to display data at the specified
     * position.
     *
     * @param holder   The {@link RecyclerView.ViewHolder} which should be updated to represent
     *                 the contents of the item at the given position in the {@link Cursor}.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Move Cursor to specified position.
        cursor.moveToPosition(position);

        // Get indices of columns that need to be fetched.
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);

        // Get attributes from Cursor.
        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);

        // Update ViewHolder with attributes.
        ProductViewHolder productViewHolder = (ProductViewHolder) holder;
        productViewHolder.getNameTextView().setText(name);
        productViewHolder.getPriceTextView().setText(price);
        productViewHolder.getQuantityTextView().setText(quantity);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    /**
     * Return the id of the item at position.
     *
     * @param position Adapter position to query.
     * @return The id of the item at position.
     */
    @Override
    public long getItemId(int position) {

        // Throw an exception if the cursor is null.
        if (cursor == null) {
            throw new IllegalStateException("Cannot lookup item id when cursor is null");
        }

        // Throw an exception if the cursor cannot move to the appropriate position.
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Cannot move cursor to position " + position + " to get item id");
        }

        // Get column index of the id column.
        int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);

        // Throw an exception if the cursor does not have an id column.
        if (idColumnIndex == -1) {
            throw new IllegalStateException("Cursor does not have id column in its projection");
        }

        return cursor.getLong(idColumnIndex);
    }

    /**
     * Change the {@link Cursor} being adapted to a new {@link Cursor}. If there is an existing
     * {@link Cursor}, then it will be closed.
     *
     * @param newCursor The new {@link Cursor} to be used.
     */
    public void changeCursor(Cursor newCursor) {

        // Close the old Cursor.
        if (cursor != null) {
            cursor.close();
        }

        // Assign the new Cursor.
        cursor = newCursor;

        // Notify RecyclerView of data set change.
        notifyItemRangeRemoved(0, getItemCount());
        if (newCursor != null) {
            notifyItemRangeInserted(0, getItemCount());
        }
    }

    /**
     * {@link ProductViewHolder} is a model class that describes a single product item view and
     * metadata about its place within a {@link RecyclerView}.
     */
    private static class ProductViewHolder extends RecyclerView.ViewHolder {

        /**
         * {@link TextView} to hold the name of a product.
         */
        private final TextView nameTextView;

        /**
         * {@link TextView} to hold the price of a product.
         */
        private final TextView priceTextView;

        /**
         * {@link TextView} to hold the quantity of a product.
         */
        private final TextView quantityTextView;

        /**
         * {@link Button} to decrement the quantity of a product.
         */
        private final Button decrementButton;

        /**
         * {@link Button} to increment the quantity of a product.
         */
        private final Button incrementButton;

        /**
         * Constructs a new {@link ProductViewHolder}.
         *
         * @param itemView {@link View} to be held in this view holder.
         */
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.name_text_view);
            this.priceTextView = itemView.findViewById(R.id.price_text_view);
            this.quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            this.decrementButton = itemView.findViewById(R.id.decrement_button);
            this.incrementButton = itemView.findViewById(R.id.increment_button);
        }

        /**
         * Returns {@link ProductViewHolder#nameTextView}.
         */
        public TextView getNameTextView() {
            return nameTextView;
        }

        /**
         * Returns {@link ProductViewHolder#priceTextView}.
         */
        public TextView getPriceTextView() {
            return priceTextView;
        }

        /**
         * Returns {@link ProductViewHolder#quantityTextView}.
         */
        public TextView getQuantityTextView() {
            return quantityTextView;
        }

        /**
         * Returns {@link ProductViewHolder#decrementButton}.
         */
        public Button getDecrementButton() {
            return decrementButton;
        }

        /**
         * Returns {@link ProductViewHolder#incrementButton}.
         */
        public Button getIncrementButton() {
            return incrementButton;
        }
    }
}