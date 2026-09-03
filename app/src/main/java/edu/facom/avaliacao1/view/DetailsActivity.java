package edu.facom.avaliacao1.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.facom.avaliacao1.R;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imgDetail;
    private TextView txtTitle, txtDescription;
    private LinearLayout layoutAudioPlayer;
    private Button btnPlay, btnFinish;

    private MediaPlayer mediaPlayer;
    private int soundResId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Instanciar Views
        imgDetail = findViewById(R.id.img_detail);
        txtTitle = findViewById(R.id.txt_detail_title);
        txtDescription = findViewById(R.id.txt_detail_description);
        layoutAudioPlayer = findViewById(R.id.layout_audio_player);
        btnPlay = findViewById(R.id.btn_play);
        btnFinish = findViewById(R.id.btn_finish);

        // Receber dados da Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("TIPO", "");
            String title = extras.getString("NOME", "");
            int imageResId = extras.getInt("IMAGEM_ID", 0);

            txtTitle.setText(title);

            if (imageResId != 0) {
                imgDetail.setImageResource(imageResId);
            }

            if ("TURISMO".equals(type)) {
                String description = extras.getString("DESCRICAO", "");
                txtDescription.setText(description);
                layoutAudioPlayer.setVisibility(View.GONE);
            } else if ("AVE".equals(type)) {
                soundResId = extras.getInt("SOM_ID", 0);
                txtDescription.setText("Espécie nativa da região. Clique abaixo para ouvir seu canto característico e aprender sobre sua ficha técnica no ecossistema local.");
                layoutAudioPlayer.setVisibility(View.VISIBLE);
                setupMediaPlayer();
            }
        }

        // Botão Encerrar: remove a activity da pilha e retorna à tela principal
        btnFinish.setOnClickListener(v -> finish());
    }

    private void setupMediaPlayer() {
        btnPlay.setOnClickListener(v -> {
            if (soundResId != 0) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, soundResId);
                }
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } else {
                Toast.makeText(this, "Arquivo de áudio não encontrado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // REGRA OBRIGATÓRIA: Gestão do áudio no ciclo de vida
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release(); // Libera a memória alocada ao MediaPlayer
            mediaPlayer = null;
        }
    }
}