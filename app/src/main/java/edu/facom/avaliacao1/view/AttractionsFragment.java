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

        // 1. Inicializar lista com dados fictícios (Placeholders)
        populateData();

        filteredAttractions = new ArrayList<>();
        adapter = new AttractionAdapter(requireContext(), filteredAttractions);
        listView.setAdapter(adapter);

        // 2. Conectar ao Shared ViewModel da Activity Principal
        regionViewModel = new ViewModelProvider(requireActivity()).get(RegionViewModel.class);

        // 3. OBSERVAR o ViewModel (A Mágica da Reatividade)
        regionViewModel.getRegion().observe(getViewLifecycleOwner(), selectedRegion -> {
            // Toda vez que o Spinner do Filtro mudar, este bloco é executado
            filteredAttractions.clear();

            for (Attraction attraction : allAttractions) {
                if (attraction.getRegion().equals(selectedRegion)) {
                    filteredAttractions.add(attraction);
                }
            }
            // Avisa o adaptador que os dados mudaram para recarregar a ListView
            adapter.notifyDataSetChanged();
        });

        // 4. Configurar Clique na Lista (INTENT)
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Attraction clickedItem = filteredAttractions.get(position);

            Intent intent = new Intent(requireContext(), DetailsActivity.class);

            // Empacotando os dados (putExtra)
            intent.putExtra("TIPO", "TURISMO");
            intent.putExtra("NOME", clickedItem.getName());
            intent.putExtra("DESCRICAO", clickedItem.getDescription());
            intent.putExtra("IMAGEM_ID", clickedItem.getImageResId());

            startActivity(intent);
        });
    }

    private void populateData() {
        allAttractions = new ArrayList<>();
        // Note que a String da região DEVE ser idêntica ao que está no strings.xml
        allAttractions.add(new Attraction("Atração A", "Monumento símbolo da cidade", "Rota Norte", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração B", "Bela queda d'água na região norte", "Rota Norte", R.drawable.guaicuru));

        allAttractions.add(new Attraction("Atração C", "Viajem em meio a natureza selvagem", "Pantanal", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração D", "Marco histórico às margens do rio", "Pantanal", R.drawable.guaicuru));

        allAttractions.add(new Attraction("Atração E", "Viajem em meio a natureza selvagem", "Costa Leste e Vale do Aporé", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração F", "Marco histórico às margens do rio", "Costa Leste e Vale do Aporé", R.drawable.guaicuru));

        allAttractions.add(new Attraction("Atração G", "Viajem em meio a natureza selvagem", "Bonito / Serra da Bodoquena", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração H", "Marco histórico às margens do rio", "Bonito / Serra da Bodoquena", R.drawable.guaicuru));

        allAttractions.add(new Attraction("Atração I", "Viajem em meio a natureza selvagem", "Caminho dos Ipês", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração J", "Marco histórico às margens do rio", "Caminho dos Ipês", R.drawable.guaicuru));

        allAttractions.add(new Attraction("Atração k", "Viajem em meio a natureza selvagem", "Caminhos da Fronteira e Grande Dourados", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração L", "Marco histórico às margens do rio", "Caminhos da Fronteira e Grande Dourados", R.drawable.guaicuru));

        allAttractions.add(new Attraction("Atração M", "Viajem em meio a natureza selvagem", "Vale das Águas e Cone Sul", R.drawable.guaicuru));
        allAttractions.add(new Attraction("Atração N", "Marco histórico às margens do rio", "Vale das Águas e Cone Sul", R.drawable.guaicuru));
    }
}