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

    // Funcionamento do botão de áudio
    private void setupMediaPlayer() {
        btnPlay.setOnClickListener(v -> {
            if (soundResId != 0) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, soundResId);
                    if (mediaPlayer == null) {
                        Toast.makeText(this, "Erro ao carregar o áudio", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    mediaPlayer.setOnCompletionListener(mp -> {
                        btnPlay.setText("Ouvir Canto");
                        mp.seekTo(0);
                    });
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setText("Ouvir Canto");
                } else {
                    mediaPlayer.start();
                    btnPlay.setText("Pausar Canto");
                }
            } else {
                Toast.makeText(this, "Canto não disponível para esta ave", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Quando a atividade é destruída, o mediaplayer também é
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}