package com.example.edrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private Context context;
    private List<ItemSearch> searchResults;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItemSearch item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SearchResultAdapter(Context context, List<ItemSearch> searchResults) {
        this.context = context;
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        ItemSearch currentItem = searchResults.get(position);

        holder.textViewName.setText(currentItem.getName());
        holder.textViewDescription.setText(currentItem.getDescription());
        holder.textViewPrice.setText(currentItem.getPrice());

        Picasso.get()
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.logo)
                .into(holder.imageViewItem);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;
        TextView textViewName, textViewDescription, textViewPrice;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.resultItemImage);
            textViewName = itemView.findViewById(R.id.resultItemName);
            textViewDescription = itemView.findViewById(R.id.resultItemDescription);
            textViewPrice = itemView.findViewById(R.id.resultItemPrice);
        }
    }
}
