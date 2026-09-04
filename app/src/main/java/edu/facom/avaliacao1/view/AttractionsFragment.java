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
        allAttractions.add(new Attraction("Cânion do Engano", "Impressionantes formações rochosas e paredões de arenito.", "Rota Norte", R.drawable.ponto01caniondoengano));
        allAttractions.add(new Attraction("Gruta do Pitoco", "Trilha ecológica fantástica com cachoeiras exuberantes.", "Rota Norte", R.drawable.ponto02grutapitoco));

        allAttractions.add(new Attraction("Estrada parque do Pantanal", "Rota cênica para observação da rica fauna e flora pantaneira.", "Pantanal", R.drawable.ponto01estradaparquepantanal));
        allAttractions.add(new Attraction("Fazenda San Fracisco", "Safári fotográfico e passeios inesquecíveis de chalana.", "Pantanal", R.drawable.ponto02fazendasanfrancisco));

        allAttractions.add(new Attraction("Balneario Três Lagos", "Área de lazer com praias tranquilas de água doce.", "Costa Leste e Vale do Aporé", R.drawable.ponto01balneariotreslagos));
        allAttractions.add(new Attraction("Ponte do Ferroviaria", "Marco histórico e arquitetônico imponente sobre o Rio Paraná.", "Costa Leste e Vale do Aporé", R.drawable.ponto02ponterodoferroviaria));

        allAttractions.add(new Attraction("Gruta da Lagoa Azul", "Famosa caverna com um espelho d'água de azul intenso.", "Bonito / Serra da Bodoquena", R.drawable.ponto01grutadolagoazul));
        allAttractions.add(new Attraction("Rios da Prata", "Flutuação relaxante em águas cristalinas repletas de peixes.", "Bonito / Serra da Bodoquena", R.drawable.ponto02riosdaprata));

        allAttractions.add(new Attraction("Bioparque do Pantanal", "O maior complexo de aquários de água doce do mundo.", "Caminho dos Ipês", R.drawable.ponto01bioparquepantanal));
        allAttractions.add(new Attraction("Parque das nações indígenas", "Ampla reserva ecológica e espaço de lazer na capital.", "Caminho dos Ipês", R.drawable.ponto02parquedasnacoesindigenas));

        allAttractions.add(new Attraction("Salto do rio Aporé", "Belas quedas d'água cercadas pela natureza preservada.", "Caminhos da Fronteira e Grande Dourados", R.drawable.ponto01_saltodorioapore));
        allAttractions.add(new Attraction("Parque dos Ipês", "Tradicional espaço de convivência, cultura e lazer em Dourados.", "Caminhos da Fronteira e Grande Dourados", R.drawable.ponto02parquedosipes));

        allAttractions.add(new Attraction("Parque estadual das Várzeas", "Importante área de conservação da bacia do rio Ivinhema.", "Vale das Águas e Cone Sul", R.drawable.ponto01_parqueestadualdasvarzeas));
        allAttractions.add(new Attraction("Casa do Artesão", "Centro de valorização da cultura e do belo artesanato regional.", "Vale das Águas e Cone Sul", R.drawable.ponto02casadoartesao));
    }
}