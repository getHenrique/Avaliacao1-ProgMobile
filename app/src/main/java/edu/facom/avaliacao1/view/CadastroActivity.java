package edu.facom.avaliacao1.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.viewmodel.CadastroViewModel;

public class CadastroActivity extends AppCompatActivity {

    private ImageView imgFotoPerfil;
    private EditText edtNomeUsuario;
    private EditText edtSenhaUsuario;
    private Button btnTirarFoto;
    private Button btnSalvarCadastro;

    private CadastroViewModel viewModel;
    private String caminhoAtualDaFoto = "";
    private Uri uriFotoCorrente;

    // Novo padrão do Android para receber resultados de outras Activities (Câmara)
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // A foto foi tirada com sucesso. Mostra a imagem na tela.
                    imgFotoPerfil.setImageURI(uriFotoCorrente);
                } else {
                    Toast.makeText(this, "Captura de fotografia cancelada", Toast.LENGTH_SHORT).show();
                    caminhoAtualDaFoto = ""; // Limpa o caminho se falhou
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializar componentes visuais
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        edtNomeUsuario = findViewById(R.id.edtNomeUsuario);
        edtSenhaUsuario = findViewById(R.id.edtSenhaUsuario);
        btnTirarFoto = findViewById(R.id.btnTirarFoto);
        btnSalvarCadastro = findViewById(R.id.btnSalvarCadastro);

        int idRecebido = getIntent().getIntExtra("ID_USUARIO", -1);
        if (idRecebido != -1) {
            btnSalvarCadastro.setText("Atualizar Perfil");
        }

        // Inicializar o ViewModel
        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        // Observar o resultado do salvamento
        viewModel.getCadastroSucesso().observe(this, sucesso -> {
            if (sucesso) {
                Toast.makeText(this, "Utilizador guardado com sucesso!", Toast.LENGTH_LONG).show();

                // Redireciona para a Tela Principal
                Intent intent = new Intent(CadastroActivity.this, edu.facom.avaliacao1.view.MainActivity.class);
                startActivity(intent);

                finish(); // Fecha a ecrã após o sucesso
            } else {
                Toast.makeText(this, "Erro ao guardar usuário.", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar botões
        btnTirarFoto.setOnClickListener(v -> abrirCamera());

        btnSalvarCadastro.setOnClickListener(v -> salvarDados());
    }

    private void abrirCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File arquivoFoto = null;
        try {
            arquivoFoto = criarArquivoDeImagem();
        } catch (IOException ex) {
            Toast.makeText(this, "Erro ao criar ficheiro para a foto.", Toast.LENGTH_SHORT).show();
            return; // Sai do método se falhar
        }

        if (arquivoFoto != null) {
            // Usa o provider configurado no Manifest
            uriFotoCorrente = FileProvider.getUriForFile(this,
                    "edu.facom.avaliacao1.fileprovider",
                    arquivoFoto);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoCorrente);

            try {
                // Tenta abrir a aplicação de câmara nativa
                cameraLauncher.launch(takePictureIntent);
            } catch (android.content.ActivityNotFoundException e) {
                // Se o emulador ou telemóvel não tiver nenhuma câmara instalada, cai aqui
                Toast.makeText(this, "Nenhuma aplicação de câmara encontrada neste dispositivo.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File criarArquivoDeImagem() throws IOException {
        // Cria um nome único baseado na data e hora
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nomeArquivoImagem = "JPEG_" + timeStamp + "_";
        File diretorioDeArmazenamento = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imagem = File.createTempFile(
                nomeArquivoImagem,  /* prefixo */
                ".jpg",             /* sufixo */
                diretorioDeArmazenamento /* diretório */
        );

        // Guarda o caminho absoluto para salvar na base de dados
        caminhoAtualDaFoto = imagem.getAbsolutePath();
        return imagem;
    }

    private void salvarDados() {
        String nome = edtNomeUsuario.getText().toString().trim();
        String senha = edtSenhaUsuario.getText().toString().trim();

        if (nome.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha o nome e a palavra-passe.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (caminhoAtualDaFoto.isEmpty()) {
            Toast.makeText(this, "Por favor, tire uma fotografia para o perfil.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chama o ViewModel para tratar de toda a lógica de gravação e criptografia
        viewModel.salvarUsuario(nome, senha, caminhoAtualDaFoto);
    }
}