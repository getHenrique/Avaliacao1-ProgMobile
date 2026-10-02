package edu.facom.avaliacao1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.model.Attraction;
import edu.facom.avaliacao1.viewmodel.RegionViewModel;
// import edu.facom.avaliacao1.DetailsActivity; // Descomente no Passo 7

public class AttractionsFragment extends Fragment {

    private RegionViewModel regionViewModel;
    private ListView listView;
    private AttractionAdapter adapter;
    private List<Attraction> allAttractions;
    private List<Attraction> filteredAttractions;

    public AttractionsFragment() {
        super(R.layout.fragment_attractions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view_attractions);

        filteredAttractions = new ArrayList<>();
        adapter = new AttractionAdapter(requireContext(), filteredAttractions);
        listView.setAdapter(adapter);

        regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        // Observa as atrações já filtradas pelo banco
        regionViewModel.getAttractionsForRegion().observe(getViewLifecycleOwner(), attractions -> {
            filteredAttractions.clear();
            if (attractions != null) {
                filteredAttractions.addAll(attractions);
            }
            adapter.notifyDataSetChanged();
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Attraction clickedItem = filteredAttractions.get(position);
            Intent intent = new Intent(requireContext(), DetailsActivity.class);
            intent.putExtra("TIPO", "TURISMO");
            intent.putExtra("NOME", clickedItem.getName());
            intent.putExtra("DESCRICAO", clickedItem.getDescription());
            intent.putExtra("IMAGEM_URI", clickedItem.getImageUri()); // Mudou para URI
            startActivity(intent);
        });
    }

}