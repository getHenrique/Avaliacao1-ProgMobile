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

public class CadastroViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> usuarioAtivo = new MutableLiveData<>();
    private UsuarioDao usuarioDao;
    private final ExecutorService executorService;

    // LiveData usado para avisar a View (Activity) se o salvamento deu certo ou errado
    private MutableLiveData<Boolean> cadastroSucesso = new MutableLiveData<>();

    public CadastroViewModel(@NonNull Application application) {
        super(application);
        // Inicializa o DAO através do AppDatabase
        usuarioDao = AppDatabase.getInstance(application).usuarioDao();

        // Executor para rodar as tarefas de banco de dados fora da Thread Principal (UI Thread)
        executorService = Executors.newSingleThreadExecutor();
        usuarioAtivo.setValue(null);
    }

    // A View irá "observar" esse método
    public LiveData<Boolean> getCadastroSucesso() {
        return cadastroSucesso;
    }

    public void salvarUsuario(String nome, String senha, String caminhoFoto) {
        // Executa a operação em segundo plano
        executorService.execute(() -> {
            try {
                // 1. Criptografar a senha usando a classe utilitária
                String senhaCriptografada = CriptografiaUtils.gerarHashSenha(senha);

                // 2. Criar a entidade Usuario com os dados e a senha já em hash
                Usuario novoUsuario = new Usuario(nome, senhaCriptografada, caminhoFoto);

                // 3. Salvar no banco
                usuarioDao.inserirUsuario(novoUsuario);

                // 4. Notificar a View (Activity) sobre o sucesso usando postValue (seguro para background thread)
                cadastroSucesso.postValue(true);
            } catch (Exception e) {
                // Notificar falha
                cadastroSucesso.postValue(false);
            }
        });
    }
    public LiveData<Usuario> getUsuarioAtivo() {
        return usuarioAtivo;
    }

    public void fazerLogin(String nome, String senhaDigitada) {
        executorService.execute(() -> {
            try {
                String senhaCriptografada = CriptografiaUtils.gerarHashSenha(senhaDigitada);
                Usuario usuarioEncontrado = usuarioDao.validarLogin(nome, senhaCriptografada);
                usuarioAtivo.postValue(usuarioEncontrado);
            } catch (Exception e) {
                usuarioAtivo.postValue(null);
            }
        });
    }
}