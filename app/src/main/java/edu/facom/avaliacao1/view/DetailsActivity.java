package edu.facom.avaliacao1.view;

import android.media.MediaPlayer;
import android.net.Uri;
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
    private String soundUriStr = null;

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

        // Uso de string resource para o botão de voltar/encerrar
        btnFinish.setText(getString(R.string.btn_exit));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("TIPO", "");
            String title = extras.getString("NOME", "");
            String imageUriStr = extras.getString("IMAGEM_URI", "");

            txtTitle.setText(title);

            if (!imageUriStr.isEmpty()) {
                // Carrega a imagem a partir do URI da base de dados
                imgDetail.setImageURI(Uri.parse(imageUriStr));
            }

            if ("TURISMO".equals(type)) {
                String description = extras.getString("DESCRICAO", "");
                txtDescription.setText(description);
                layoutAudioPlayer.setVisibility(View.GONE);
            } else if ("AVE".equals(type)) {
                soundUriStr = extras.getString("SOM_URI", "");
                // Idealmente coloque esta string no strings.xml e chame com getString()
                txtDescription.setText("Espécie nativa da região. Clique abaixo para ouvir o seu canto característico.");
                layoutAudioPlayer.setVisibility(View.VISIBLE);
                setupMediaPlayer();
            }
        }

        btnFinish.setOnClickListener(v -> finish());
    }

    private void setupMediaPlayer() {
        btnPlay.setOnClickListener(v -> {
            if (soundUriStr != null && !soundUriStr.isEmpty()) {
                if (mediaPlayer == null) {
                    // Carrega o áudio a partir do URI
                    mediaPlayer = MediaPlayer.create(this, Uri.parse(soundUriStr));
                    if (mediaPlayer == null) {
                        Toast.makeText(this, "Erro ao carregar o áudio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mediaPlayer.setOnCompletionListener(mp -> {
                        btnPlay.setText("Ouvir Canto"); // Sugestão: colocar no strings.xml
                        mp.seekTo(0);
                    });
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setText("Ouvir Canto");
                } else {
                    mediaPlayer.start();
                    btnPlay.setText("Pausar Canto"); // Sugestão: colocar no strings.xml
                }
            } else {
                Toast.makeText(this, "Canto não disponível para esta ave", Toast.LENGTH_SHORT).show();
            }
        });
    }

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