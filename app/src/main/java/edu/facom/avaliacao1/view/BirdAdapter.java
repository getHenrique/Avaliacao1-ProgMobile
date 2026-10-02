package edu.facom.avaliacao1.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.model.Bird;

public class BirdAdapter extends BaseAdapter {

    private final Context context;
    private final List<Bird> birds;

    public BirdAdapter(Context context, List<Bird> birds) {
        this.context = context;
        this.birds = birds;
    }

    @Override
    public int getCount() {
        return birds.size();
    }

    @Override
    public Object getItem(int position) {
        return birds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bird, parent, false);
        }

        Bird bird = birds.get(position);

        ImageView imgBird = convertView.findViewById(R.id.img_bird);
        TextView txtName = convertView.findViewById(R.id.txt_bird_name);

        // Em BirdAdapter.java
        if (bird != null) {
            txtName.setText(bird.name);
            try {
                imgBird.setImageURI(android.net.Uri.parse(bird.imageUri));
            } catch (Exception e) {
                imgBird.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        return convertView;
    }
}