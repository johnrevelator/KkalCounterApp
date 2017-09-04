package inc.ak.kkalcounter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.model.Product;

public class TextAdapter extends RecyclerView.Adapter<TextHolder> {

    private List<Product> data;

    public void setData(List<Product> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<Product> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public TextHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        TextView textView = (TextView) View.inflate(viewGroup.getContext(), android.R.layout.simple_list_item_1, null);
        return new TextHolder(textView);
    }

    @Override
    public void onBindViewHolder(TextHolder textHolder, int position) {
        Product contact = (Product) data.get(position);
        Picasso.with(textHolder.img.getContext()).load(contact.getImg())
                .into(textHolder.img);
        textHolder.phone.setText(contact.getKcal());
        textHolder.email.setText(contact.getName());

        textHolder.student = contact;    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public Product getLastId() {
        return data == null ? null : data.get(data.size() - 1);
    }
}

class TextHolder extends RecyclerView.ViewHolder {
    public TextView phone;
    public TextView email;
    public ImageView img;
    public Product student;
    public TextHolder(View v){
        super(v);
        img=(ImageView)v.findViewById(R.id.img_prd);
        phone = (TextView) v.findViewById(R.id.txt_phone);
        email = (TextView) v.findViewById(R.id.txt_email);

        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "OnClick :" + student.getName() ,
                        Toast.LENGTH_SHORT).show();

            }
        });
    }


}