package edu.facom.avaliacao1;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import edu.facom.avaliacao1.view.AttractionsFragment;
import edu.facom.avaliacao1.view.BirdsFragment;
import edu.facom.avaliacao1.view.FilterFragment;
import edu.facom.avaliacao1.view.CadastroActivity;
import edu.facom.avaliacao1.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private ImageView imgAvatarToolbar;
    private BottomNavigationView bottomNav;
    private int usuarioIdLogado = -1; // Guarda o ID para edição

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //BottomNav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observar o utilizador ativo
        viewModel.getUsuarioAtivo().observe(this, usuario -> {
            if (usuario != null) {
                usuarioIdLogado = usuario.id;
                carregarAvatar(usuario.caminhoFoto);
            } else {
                // Se não houver utilizador ativo (ex: primeira instalação), encerra ou vai para o login
                executarRedirecionamentoLogout();
            }
        });

        // Observar o resultado do Logout
        viewModel.getLogoutConcluido().observe(this, concluido -> {
            if (concluido) {
                executarRedirecionamentoLogout();
            }
        });

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
    }

    private void carregarAvatar(String caminhoFoto) {
        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            File arquivoFoto = new File(caminhoFoto);
            if (arquivoFoto.exists()) {
                imgAvatarToolbar.setImageURI(Uri.fromFile(arquivoFoto));
            }
        }
    }

    private void executarRedirecionamentoLogout() {
        // Oculta a barra de navegação inferior conforme solicitado
        bottomNav.setVisibility(View.GONE);

        // Substitui o fragmento atual pelo LoginFragment
        // Substitua 'LoginFragment' pelo nome real da sua classe de fragmento de login
        /*
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
        */
        Toast.makeText(this, "Sessão encerrada.", Toast.LENGTH_SHORT).show();
    }

    //Uso do Toolbar e troca de modos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_theme) {
            toggleTheme();
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