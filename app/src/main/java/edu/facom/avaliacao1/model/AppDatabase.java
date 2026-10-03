package edu.facom.avaliacao1.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;
import edu.facom.avaliacao1.R;

// Atualiza a anotação para incluir todas as entidades e aumenta a versão para aplicar as alterações
@Database(entities = {Usuario.class, Region.class, Bird.class, Attraction.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract BirdDao birdDao();
    public abstract AttractionDao attractionDao();
    public abstract RegionDao regionDao(); // Novo DAO

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "banco_app_cadastro")
                            .fallbackToDestructiveMigration()
                            // Callback para popular a base de dados na criação inicial
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Executa a inserção em background
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        popularBancoDeDadosInicial(AppDatabase.getInstance(context), context);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Para popular o banco de dados artificialmente
    private static void popularBancoDeDadosInicial(AppDatabase db, Context context) {
        RegionDao regionDao = db.regionDao();
        AttractionDao attractionDao = db.attractionDao();
        BirdDao birdDao = db.birdDao();
        String pkg = context.getPackageName();
        String uriPrefix = "android.resource://" + pkg + "/";
        // 1. Inserir as Regiões e conseguir os seus IDs
        regionDao.insertRegion(new Region("Nada Selecionado", uriPrefix + R.drawable.mapa_atual_do_ms));
        regionDao.insertRegion(new Region("Rota Norte", uriPrefix + R.drawable.mapa_rota_norte));
        int idRN = regionDao.getRegionIdByName("Rota Norte");
        regionDao.insertRegion(new Region("Pantanal", uriPrefix + R.drawable.mapa_pantanal));
        int idPantanal = regionDao.getRegionIdByName("Pantanal");
        regionDao.insertRegion(new Region("Costa Leste e Vale do Aporé", uriPrefix + R.drawable.mapa_costa_leste));
        int idCosta = regionDao.getRegionIdByName("Costa Leste e Vale do Aporé");
        regionDao.insertRegion(new Region("Bonito / Serra da Bodoquena", uriPrefix + R.drawable.mapa_bonito));
        int idBonito = regionDao.getRegionIdByName("Bonito / Serra da Bodoquena");
        regionDao.insertRegion(new Region("Caminho dos Ipês", uriPrefix + R.drawable.mapa_caminho_ipes));
        int idIpes = regionDao.getRegionIdByName("Caminho dos Ipês");
        regionDao.insertRegion(new Region("Caminhos da Fronteira e Grande Dourados", uriPrefix + R.drawable.mapa_grande_dourados));
        int idFronteira = regionDao.getRegionIdByName("Caminhos da Fronteira e Grande Dourados");
        regionDao.insertRegion(new Region("Vale das Águas e Cone Sul", uriPrefix + R.drawable.mapa_cone_sul));
        int idConeSul = regionDao.getRegionIdByName("Vale das Águas e Cone Sul");

        // 2. Inserir Atrações Turísticas por Região
        // Rota Norte
        attractionDao.insertAttraction(new Attraction("Cânion do Engano", "Impressionantes formações rochosas e paredões de arenito.", idRN, uriPrefix + R.drawable.ponto01caniondoengano));
        attractionDao.insertAttraction(new Attraction("Gruta do Pitoco", "Trilha ecológica fantástica com cachoeiras exuberantes.", idRN, uriPrefix + R.drawable.ponto02grutapitoco));
        // Pantanal
        attractionDao.insertAttraction(new Attraction("Estrada parque do Pantanal", "Rota cênica para observação da rica fauna e flora pantaneira.", idPantanal, uriPrefix + R.drawable.ponto01estradaparquepantanal));
        attractionDao.insertAttraction(new Attraction("Fazenda San Fracisco", "Safári fotográfico e passeios inesquecíveis de chalana.", idPantanal, uriPrefix + R.drawable.ponto02fazendasanfrancisco));
        // Costa Leste
        attractionDao.insertAttraction(new Attraction("Balneario Três Lagos", "Área de lazer com praias tranquilas de água doce.", idCosta, uriPrefix + R.drawable.ponto01balneariotreslagos));
        attractionDao.insertAttraction(new Attraction("Ponte do Ferroviaria", "Marco histórico e arquitetônico imponente sobre o Rio Paraná.", idCosta, uriPrefix + R.drawable.ponto02ponterodoferroviaria));
        // Bonito
        attractionDao.insertAttraction(new Attraction("Gruta da Lagoa Azul", "Famosa caverna com um espelho d'água de azul intenso.", idBonito, uriPrefix + R.drawable.ponto01grutadolagoazul));
        attractionDao.insertAttraction(new Attraction("Rios da Prata", "Flutuação relaxante em águas cristalinas repletas de peixes.", idBonito, uriPrefix + R.drawable.ponto02riosdaprata));
        // Caminho dos Ipês
        attractionDao.insertAttraction(new Attraction("Bioparque do Pantanal", "O maior complexo de aquários de água doce do mundo.", idIpes, uriPrefix + R.drawable.ponto01bioparquepantanal));
        attractionDao.insertAttraction(new Attraction("Parque das nações indígenas", "Ampla reserva ecológica e espaço de lazer na capital.", idIpes, uriPrefix + R.drawable.ponto02parquedasnacoesindigenas));
        // Fronteira
        attractionDao.insertAttraction(new Attraction("Salto do rio Aporé", "Belas quedas d'água cercadas pela natureza preservada.", idFronteira, uriPrefix + R.drawable.ponto01_saltodorioapore));
        attractionDao.insertAttraction(new Attraction("Parque dos Ipês", "Tradicional espaço de convivência, cultura e lazer em Dourados.", idFronteira, uriPrefix + R.drawable.ponto02parquedosipes));
        // Cone Sul
        attractionDao.insertAttraction(new Attraction("Parque estadual das Várzeas", "Importante área de conservação da bacia do rio Ivinhema.", idConeSul, uriPrefix + R.drawable.ponto01_parqueestadualdasvarzeas));
        attractionDao.insertAttraction(new Attraction("Casa do Artesão", "Centro de valorização da cultura e do belo artesanato regional.", idConeSul, uriPrefix + R.drawable.ponto02casadoartesao));

        // 3. Inserir Aves por Região
        // Pantanal
        birdDao.insertBird(new Bird("Tuiuiú", idPantanal, uriPrefix + R.drawable.avtuiuiu, uriPrefix + R.raw.cantoavetuiuiu));
        birdDao.insertBird(new Bird("Colheiro", idPantanal, uriPrefix + R.drawable.colheiro, uriPrefix + R.raw.cantocolhereiro));
        // Rota Norte
        birdDao.insertBird(new Bird("Tucano", idRN, uriPrefix + R.drawable.avetucanotoco, uriPrefix + R.raw.cantoavetucano));
        birdDao.insertBird(new Bird("Carcara", idRN, uriPrefix + R.drawable.avecarcara, uriPrefix + R.raw.avecarcara));
        // Costa Leste
        birdDao.insertBird(new Bird("Papagaio Verde", idCosta, uriPrefix + R.drawable.avepapagaioverdadeiro, uriPrefix + R.raw.cantoavepapagaioverde));
        birdDao.insertBird(new Bird("João de Barro", idCosta, uriPrefix + R.drawable.avejoaodebarro, uriPrefix + R.raw.cantojoaodebarro));
        // Bonito
        birdDao.insertBird(new Bird("Udu Coroa Azul", idBonito, uriPrefix + R.drawable.aveuducoroaazul, uriPrefix + R.raw.cantoaveuducoroaazul));
        birdDao.insertBird(new Bird("Mutum de Penacho", idBonito, uriPrefix + R.drawable.avemutumpenacho, uriPrefix + R.raw.mutumpenacho));
        // Caminho dos Ipês
        birdDao.insertBird(new Bird("Arara Caninde", idIpes, uriPrefix + R.drawable.aveararacaninde, uriPrefix + R.raw.cantoaveararacaninde));
        birdDao.insertBird(new Bird("Sabiá", idIpes, uriPrefix + R.drawable.avesabia, uriPrefix + R.raw.bemtevi));
        // Fronteira
        birdDao.insertBird(new Bird("Seriema", idFronteira, uriPrefix + R.drawable.aveseriema, uriPrefix + R.raw.cantoaveseriema));
        birdDao.insertBird(new Bird("Periquito Rico", idFronteira, uriPrefix + R.drawable.aveperiquitorico, uriPrefix + R.raw.aveperiquitorico));
        // Cone Sul
        birdDao.insertBird(new Bird("Narceja", idConeSul, uriPrefix + R.drawable.avenarceja, uriPrefix + R.raw.cantoavenarceja));
        birdDao.insertBird(new Bird("João Bobo", idConeSul, uriPrefix + R.drawable.avejoaobobo, uriPrefix + R.raw.cantoavejoaobobo));
    }
}