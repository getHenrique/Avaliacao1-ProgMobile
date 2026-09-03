package edu.facom.avaliacao1.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.model.Attraction;

public class AttractionAdapter extends ArrayAdapter<Attraction> {

    public AttractionAdapter(Context context, List<Attraction> attractions) {
        super(context, 0, attractions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_attraction, parent, false);
        }

        Attraction attraction = getItem(position);

        ImageView imgAttraction = convertView.findViewById(R.id.img_attraction);
        TextView txtName = convertView.findViewById(R.id.txt_attraction_name);
        TextView txtDesc = convertView.findViewById(R.id.txt_attraction_desc);

        if (attraction != null) {
            // Lembre-se de verificar se os nomes dos seus getters estão assim
            txtName.setText(attraction.getName());
            txtDesc.setText(attraction.getDescription());

            // Usamos try-catch caso a imagem do placeholder ainda não exista no drawable
            try {
                imgAttraction.setImageResource(attraction.getImageResId());
            } catch (Exception e) {
                imgAttraction.setImageResource(R.drawable.ic_launcher_background); // fallback
            }
        }

        return convertView;
    }
}