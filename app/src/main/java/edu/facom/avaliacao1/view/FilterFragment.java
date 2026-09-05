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

        imgRegion = view.findViewById(R.id.region_map);
        Spinner spinnerRegions = view.findViewById(R.id.spinner_regions);

        // Alimenta o spinner com as regiões
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.regions,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegions.setAdapter(adapter);

        // Ação do spinner
        spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = parent.getItemAtPosition(position).toString();

                int imageResId = getRegionImageResource(position);
                imgRegion.setImageResource(imageResId);// Troca imagem do ImageView

                // Seleção da região
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
                return R.drawable.mapa_rota_norte;
            case 2:
                return R.drawable.mapa_pantanal;
            case 3:
                return R.drawable.mapa_costa_leste;
            case 4:
                return R.drawable.mapa_bonito;
            case 5:
                return R.drawable.mapa_caminho_ipes;
            case 6:
                return R.drawable.mapa_grande_dourados;
            case 7:
                return R.drawable.mapa_cone_sul;
            default:
                return R.drawable.mapa_atual_do_ms;
        }
    }
}