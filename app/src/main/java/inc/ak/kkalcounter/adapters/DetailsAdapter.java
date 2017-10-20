package inc.ak.kkalcounter.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.model.Eating;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {
    private List<Eating> groupList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, prot,carb,fat,kcal,list;

        public MyViewHolder(View view) {
            super(view);
            context=view.getContext();
            title = (TextView) view.findViewById(R.id.title);
            fat = (TextView) view.findViewById(R.id.fat);
            prot = (TextView) view.findViewById(R.id.prot);
            carb = (TextView) view.findViewById(R.id.carb);
            kcal = (TextView) view.findViewById(R.id.kcal);
        }
    }


    public DetailsAdapter(List<Eating> groupList) {
        this.groupList = groupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Eating wash = groupList.get(position);
        holder.title.setText(wash.getName());
try {
holder.itemView.setTag(position);

    holder.prot.setText(context.getString(R.string.prot, String.format(Locale.US, "%.1f", Double.parseDouble(wash.getProt()))));
    holder.fat.setText(context.getString(R.string.fat, String.format(Locale.US, "%.1f", Double.parseDouble(wash.getFat()))));
    holder.carb.setText(context.getString(R.string.carb, String.format(Locale.US, "%.1f", Double.parseDouble(wash.getCarb()))));
    holder.kcal.setText(context.getString(R.string.kcal, String.format(Locale.US, "%.1f", Double.parseDouble(wash.getKcal()))));

}catch (NullPointerException e){

}
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AcceptActivity.class).putExtra("fl",1));

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void setFilter(List<Eating> movies){

        groupList= new ArrayList<>();
        groupList.addAll(movies);
        notifyDataSetChanged();
    }
}
