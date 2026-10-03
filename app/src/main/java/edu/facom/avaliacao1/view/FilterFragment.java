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

import java.util.ArrayList;
import java.util.List;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.model.Region;
import edu.facom.avaliacao1.viewmodel.RegionViewModel;

public class FilterFragment extends Fragment {

    private RegionViewModel regionViewModel;
    private ImageView imgRegion;

    public FilterFragment() {
        super(R.layout.fragment_filter);
    }

    // Dentro de FilterFragment.java, substitua o método onViewCreated:

    // Em FilterFragment.java, ajuste o onViewCreated:
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        imgRegion = view.findViewById(R.id.region_map);
        Spinner spinnerRegions = view.findViewById(R.id.spinner_regions);

// Inicializa o Adapter com uma lista vazia
        List<String> nomesRegioes = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nomesRegioes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegions.setAdapter(adapter);

        // Observa o banco de dados
        regionViewModel.getAllRegions().observe(getViewLifecycleOwner(), regions -> {
            if (regions != null && !regions.isEmpty()) {
                nomesRegioes.clear(); // Limpa os dados antigos
                for (Region r : regions) {
                    nomesRegioes.add(r.name); // Preenche com os nomes do banco
                }
                adapter.notifyDataSetChanged(); // Avisa o Spinner para se desenhar novamente
            }
        });

        spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Region> currentRegions = regionViewModel.getAllRegions().getValue();

                if (currentRegions != null && !currentRegions.isEmpty()) {
                    Region selectedRegion = currentRegions.get(position);
                    regionViewModel.setRegionId(selectedRegion.id);

                    if (selectedRegion.imageUri == null || selectedRegion.imageUri.isEmpty()) {
                        imgRegion.setImageResource(R.drawable.ic_launcher_background); // Imagem padrão
                    } else {
                        try {
                            imgRegion.setImageURI(android.net.Uri.parse(selectedRegion.imageUri));
                        } catch (Exception e) {
                            imgRegion.setImageResource(R.drawable.ic_launcher_background);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

}