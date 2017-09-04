package inc.ak.kkalcounter.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import inc.ak.kkalcounter.OnLoadMoreListener;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.activity.DescScrollingActivity;
import inc.ak.kkalcounter.model.Eating;
import inc.ak.kkalcounter.model.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> groupList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phone;
        public TextView email;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            context=view.getContext();
            img=(ImageView)view.findViewById(R.id.img_prd);
            phone = (TextView) view.findViewById(R.id.txt_phone);
            email = (TextView) view.findViewById(R.id.txt_email);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }




    public ProductAdapter(List<Product> groupList) {
        this.groupList = groupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Product contact = groupList.get(position);

        final MyViewHolder userViewHolder = (MyViewHolder) holder;
        try {
            Picasso.with(userViewHolder.img.getContext()).load(contact.getImg())
                    .into(userViewHolder.img);
        }catch (IllegalArgumentException e){

        }

        userViewHolder.phone.setText(userViewHolder.img.getContext().getString(R.string.kcal_row,contact.getKcal()));
        userViewHolder.email.setText(contact.getName());

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Product string) {
        insert(string, groupList.size());
    }

    public void insert(Product string, int position) {
        groupList.add(position, string);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        groupList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = groupList.size();
        groupList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Product> strings) {
        int startIndex = groupList.size();
        groupList.addAll(startIndex, strings);
        notifyItemRangeInserted(startIndex, strings.size());
    }

    public void setFilter(List<Product> movies){

        groupList= new ArrayList<>();
        groupList.addAll(movies);
        notifyDataSetChanged();
    }
}
