package edu.facom.avaliacao1.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.model.Usuario;
import edu.facom.avaliacao1.viewmodel.CadastroViewModel;

public class MainActivity extends AppCompatActivity {

    private CadastroViewModel viewModel;
    private Menu toolbarMenu; // Guardar referência do menu para atualizar o avatar dinamicamente
    private Usuario usuarioAtivoAtual; // Guardar o utilizador atual para ações do menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // BottomNav
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
            bottomNav.setSelectedItemId(R.id.nav_filter);
        }

        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        // Observer que escuta o estado da sessão no Room via LiveData
        viewModel.getUsuarioAtivo().observe(this, usuario -> {
            usuarioAtivoAtual = usuario;

            if (usuario == null) {
                // Sem sessão: Oculta o menu e redireciona para o LoginFragment
                bottomNav.setVisibility(View.GONE);

                // Oculta os itens de menu se o utilizador não estiver logado
                if (toolbarMenu != null) {
                    toolbarMenu.setGroupVisible(0, false);
                }

                getSupportActionBar().setTitle(R.string.app_name); // Restaura o título padrão

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            } else {
                // Com sessão: Mostra a navegação
                bottomNav.setVisibility(View.VISIBLE);

                // Mostra os itens do menu
                if (toolbarMenu != null) {
                    toolbarMenu.setGroupVisible(0, true);
                    atualizarAvatarToolbar(usuario); // Atualiza a foto de perfil
                }

                // Atualiza o título da Toolbar com o nome do utilizador
                getSupportActionBar().setTitle("Olá, " + usuario.getNome());
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof LoginFragment || currentFragment == null) {
                    bottomNav.setSelectedItemId(R.id.nav_filter);
                }
            }
        });
    }

    // Método auxiliar para converter o byte[] do Room numa imagem visível no Menu
    private void atualizarAvatarToolbar(Usuario usuario) {
        if (toolbarMenu != null && usuario.getFotoPerfil() != null) {
            MenuItem avatarItem = toolbarMenu.findItem(R.id.action_profile_avatar);
            byte[] fotoBytes = usuario.getFotoPerfil(); // O byte[] / BLOB do banco de dados

            if (fotoBytes != null && fotoBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);

                // Redimensionar para ficar adequado ao tamanho de um ícone de Toolbar
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                Drawable iconeAvatar = new BitmapDrawable(getResources(), scaledBitmap);

                avatarItem.setIcon(iconeAvatar);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.toolbarMenu = menu;

        // Se o utilizador já foi carregado pelo Observer antes do menu ser inflado, atualiza o ícone
        if (usuarioAtivoAtual != null) {
            atualizarAvatarToolbar(usuarioAtivoAtual);
        } else {
            // Esconde até que o utilizador faça login
            menu.setGroupVisible(0, false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_toggle_theme) {
            toggleTheme();
            return true;
        }
        else if (id == R.id.action_edit_profile) {
            // Abre a CadastroActivity, cria um usuário novo!
            if (usuarioAtivoAtual != null) {
                Intent intent = new Intent(this, CadastroActivity.class);
                intent.putExtra("MODO_EDICAO", true);
                intent.putExtra("USUARIO_ID", usuarioAtivoAtual.getId());
                startActivity(intent);
            }
            return true;
        }
        else if (id == R.id.action_logout) {
            // Ação de Logout: Encerra a sessão ativa no banco de dados
            viewModel.fazerLogout(); // Você deve criar este método no seu ViewModel / DAO
            Toast.makeText(this, "Sessão encerrada.", Toast.LENGTH_SHORT).show();
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