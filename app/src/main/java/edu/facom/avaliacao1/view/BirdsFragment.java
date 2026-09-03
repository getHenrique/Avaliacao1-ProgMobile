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
// import edu.facom.avaliacao1.DetailsActivity; // Descomente no Passo 7

public class BirdsFragment extends Fragment {

    private RegionViewModel regionViewModel;
    private GridView gridView;
    private BirdAdapter adapter;
    private List<Bird> birds;
    private List<Bird> filteredBirds;

    public BirdsFragment() {
        super(R.layout.fragment_birds);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = view.findViewById(R.id.grid_view_birds);

        // 1. Instanciar dados fictícios
        populateData();

        filteredBirds = new ArrayList<>();
        adapter = new BirdAdapter(requireContext(), filteredBirds);
        gridView.setAdapter(adapter);

        // 2. Conectar ao Shared ViewModel
        regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        // 3. Observar alterações na região selecionada
        regionViewModel.getRegion().observe(getViewLifecycleOwner(), selectedRegion -> {
            filteredBirds.clear();
            for (Bird bird : birds) {
                if (bird.getRegion().equals(selectedRegion)) {
                    filteredBirds.add(bird);
                }
            }
            adapter.notifyDataSetChanged();
        });

        // 4. Clique na ave da grade
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Bird clickedBird = filteredBirds.get(position);

            Intent intent = new Intent(requireContext(), DetailsActivity.class);

            intent.putExtra("TIPO", "AVE");
            intent.putExtra("NOME", clickedBird.getName());
            intent.putExtra("IMAGEM_ID", clickedBird.getImageResId());
            intent.putExtra("SOM_ID", clickedBird.getSoundResId());

            startActivity(intent);
        });
    }

    private void populateData() {
        birds = new ArrayList<>();

        birds.add(new Bird("Bem-te-vi", "Pantanal", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Pantanal", R.drawable.bemtevi, R.raw.bemtevi));

        birds.add(new Bird("Bem-te-vi", "Rota Norte", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Rota Norte", R.drawable.bemtevi, R.raw.bemtevi));

        birds.add(new Bird("Bem-te-vi", "Costa Leste e Vale do Aporé", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Costa Leste e Vale do Aporé", R.drawable.bemtevi, R.raw.bemtevi));

        birds.add(new Bird("Bem-te-vi", "Bonito / Serra da Bodoquena", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Bonito / Serra da Bodoquena", R.drawable.bemtevi, R.raw.bemtevi));

        birds.add(new Bird("Bem-te-vi", "Caminho dos Ipês", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Caminho dos Ipês", R.drawable.bemtevi, R.raw.bemtevi));

        birds.add(new Bird("Bem-te-vi", "Caminhos da Fronteira e Grande Dourados", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Caminhos da Fronteira e Grande Dourados", R.drawable.bemtevi, R.raw.bemtevi));

        birds.add(new Bird("Bem-te-vi", "Vale das Águas e Cone Sul", R.drawable.bemtevi, R.raw.bemtevi));
        birds.add(new Bird("Bem-te-vi", "Vale das Águas e Cone Sul", R.drawable.bemtevi, R.raw.bemtevi));
    }
}