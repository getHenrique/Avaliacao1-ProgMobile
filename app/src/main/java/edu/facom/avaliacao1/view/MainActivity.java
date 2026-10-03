package edu.facom.avaliacao1.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ImageView imgAvatarToolbar;
    private int idUsuarioLogado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgAvatarToolbar = findViewById(R.id.imgAvatarToolbar);

        //BottomNav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_filter) {
                selectedFragment = new FilterFragment();
            } else if (itemId == R.id.nav_attractions) {
                selectedFragment = new AttractionsFragment();
            } else if (itemId == R.id.nav_birds) {
                selectedFragment = new BirdsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_filter);// Inicia no fragmento de filtro
        }

        // Inicializa o MainViewModel corretamente para gerenciar a sessão
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getUsuarioAtivo().observe(this, usuario -> {
            if (usuario == null) {
                // Sem sessão: Oculta o menu e bloqueia o acesso aos 3 fragmentos, redirecionando para o Login
                bottomNav.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            } else {
                // Com sessão: Liberta o acesso, mostra o menu de navegação
                bottomNav.setVisibility(View.VISIBLE);
                
                // Armazena o ID do usuário para uso na edição do perfil
                idUsuarioLogado = usuario.id;

                // Lógica para atualizar a Toolbar com o avatar em URI
                if (usuario.caminhoFoto != null && !usuario.caminhoFoto.isEmpty()) {
                    File arquivoFoto = new File(usuario.caminhoFoto);
                    if (arquivoFoto.exists()) {
                        imgAvatarToolbar.setImageURI(Uri.fromFile(arquivoFoto));
                    }
                }

                // Redireciona para o fragmento principal (FilterFragment) apenas se o ecrã atual for o LoginFragment
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof LoginFragment || currentFragment == null) {
                    bottomNav.setSelectedItemId(R.id.nav_filter);
                }
            }
        });

        // Engatilha a verificação da sessão ativa no banco de dados ao iniciar a activity
        viewModel.verificarSessao();
    }

    //Uso do Toolbar e troca de modos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.action_toggle_theme) {
            toggleTheme();
            return true;
        } else if (itemId == R.id.action_editar_perfil) {
            // Verifica se há usuário logado e redireciona para a tela de edição
            if (idUsuarioLogado != -1) {
                Intent intent = new Intent(this, CadastroActivity.class);
                intent.putExtra("ID_USUARIO", idUsuarioLogado);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.action_logout) {
            // Executa o encerramento da sessão no banco
            viewModel.fazerLogout();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}