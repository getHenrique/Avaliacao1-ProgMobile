package edu.facom.avaliacao1.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.viewmodel.RegionViewModel;

public class FilterFragment extends Fragment {

    private RegionViewModel regionViewModel;
    private ImageView imgRegion;

    public FilterFragment() {
        super(R.layout.fragment_filter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        // 1. Vincular a ImageView
        imgRegion = view.findViewById(R.id.region_map);
        Spinner spinnerRegions = view.findViewById(R.id.spinner_regions);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.regions,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegions.setAdapter(adapter);

        // 2. Escutar a seleção e trocar a imagem + atualizar o ViewModel
        spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = parent.getItemAtPosition(position).toString();

                // Troca a imagem exibida conforme a posição escolhida
                int imageResId = getRegionImageResource(position);
                imgRegion.setImageResource(imageResId);

                if (position > 0) {
                    regionViewModel.setRegion(selectedRegion);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private int getRegionImageResource(int position) {
        switch (position) {
            case 1:
                return R.drawable.ic_launcher_background; // Substitua pelo seu R.drawable.img_rota_norte
            case 2:
                return R.drawable.ic_launcher_background; // Substitua pelo seu R.drawable.img_pantanal
            case 3:
                return R.drawable.ic_launcher_background;
            case 4:
                return R.drawable.ic_launcher_background;
            case 5:
                return R.drawable.ic_launcher_background;
            case 6:
                return R.drawable.ic_launcher_background;
            case 7:
                return R.drawable.ic_launcher_background;
            default:
                return R.drawable.mapa_atual_do_ms;
        }
    }
}