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

    public MainViewModel(@NonNull Application application) {
        super(application);
        usuarioDao = AppDatabase.getInstance(application).usuarioDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Usuario> getUsuarioAtivo() {
        return usuarioAtivo;
    }

    public void verificarSessao() {
        executorService.execute(() -> {
            try {
                Usuario logado = usuarioDao.buscarUsuarioLogado();
                usuarioAtivo.postValue(logado);
            } catch (Exception e) {
                usuarioAtivo.postValue(null);
            }
        });
    }

    public void fazerLogout() {
        executorService.execute(() -> {
            usuarioDao.encerrarSessoes();
            usuarioAtivo.postValue(null);
        });
    }
}