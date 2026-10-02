package edu.facom.avaliacao1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.model.Bird;
import edu.facom.avaliacao1.viewmodel.RegionViewModel;

public class BirdsFragment extends Fragment {

    private RegionViewModel regionViewModel;
    private GridView gridView;
    private BirdAdapter adapter;
    private List<Bird> filteredBirds;

    public BirdsFragment() {
        super(R.layout.fragment_birds);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.grid_view_birds);

        filteredBirds = new ArrayList<>();
        adapter = new BirdAdapter(requireContext(), filteredBirds);
        gridView.setAdapter(adapter);

        regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        regionViewModel.getBirdsForRegion().observe(getViewLifecycleOwner(), birdsList -> {
            filteredBirds.clear();
            if (birdsList != null) {
                filteredBirds.addAll(birdsList);
            }
            adapter.notifyDataSetChanged();
        });

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Bird clickedBird = filteredBirds.get(position);
            Intent intent = new Intent(requireContext(), DetailsActivity.class);
            intent.putExtra("TIPO", "AVE");
            intent.putExtra("NOME", clickedBird.name);
            intent.putExtra("IMAGEM_URI", clickedBird.imageUri); // Mudou para URI
            intent.putExtra("SOM_URI", clickedBird.soundUri);    // Mudou para URI
            startActivity(intent);
        });
    }

}