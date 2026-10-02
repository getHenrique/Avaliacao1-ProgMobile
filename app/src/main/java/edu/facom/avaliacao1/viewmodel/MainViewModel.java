package edu.facom.avaliacao1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.facom.avaliacao1.model.AppDatabase;
import edu.facom.avaliacao1.model.Usuario;
import edu.facom.avaliacao1.model.UsuarioDao;

public class MainViewModel extends AndroidViewModel {
    private final UsuarioDao usuarioDao;
    private final ExecutorService executorService;
    private final MutableLiveData<Usuario> usuarioAtivo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutConcluido = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        usuarioDao = AppDatabase.getInstance(application).usuarioDao();
        executorService = Executors.newSingleThreadExecutor();
        carregarUsuarioLogado();
    }

    public LiveData<Usuario> getUsuarioAtivo() { return usuarioAtivo; }
    public LiveData<Boolean> getLogoutConcluido() { return logoutConcluido; }

    private void carregarUsuarioLogado() {
        executorService.execute(() -> {
            Usuario usuario = usuarioDao.buscarUsuarioLogado();
            usuarioAtivo.postValue(usuario);
        });
    }

    public void fazerLogout() {
        executorService.execute(() -> {
            usuarioDao.encerrarSessoes();
            logoutConcluido.postValue(true);
        });
    }
}