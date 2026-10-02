package edu.facom.avaliacao1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.facom.avaliacao1.view.AttractionsFragment;
import edu.facom.avaliacao1.view.BirdsFragment;
import edu.facom.avaliacao1.view.FilterFragment;
import edu.facom.avaliacao1.view.LoginFragment;
import edu.facom.avaliacao1.viewmodel.CadastroViewModel;

public class MainActivity extends AppCompatActivity {

    private CadastroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        viewModel.getUsuarioAtivo().observe(this, usuario -> {
            if (usuario == null) {
                // Sem sessão: Oculta o menu e bloqueia o acesso aos 3 fragmentos, redirecionando para o Login[cite: 1, 2]
                bottomNav.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            } else {
                // Com sessão: Liberta o acesso, mostra o menu de navegação[cite: 1, 2]
                bottomNav.setVisibility(View.VISIBLE);

                // Redireciona para o fragmento principal (FilterFragment) apenas se o ecrã atual for o LoginFragment
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof LoginFragment || currentFragment == null) {
                    bottomNav.setSelectedItemId(R.id.nav_filter);
                }

                // TODO: Adicionar lógica para atualizar a Toolbar com o avatar em bytes (BLOB) e o nome do perfil[cite: 2]
            }
        });
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