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

    // Agora o usuarioAtivo é observado diretamente do Room para ser 100% reativo
    private final LiveData<Usuario> usuarioAtivo;

    private final UsuarioDao usuarioDao;
    private final ExecutorService executorService;

    // LiveData usado para avisar a View (Activity) se o salvamento deu certo ou errado
    private final MutableLiveData<Boolean> cadastroSucesso = new MutableLiveData<>();

    public CadastroViewModel(@NonNull Application application) {
        super(application);
        // Inicializa o DAO através do AppDatabase
        usuarioDao = AppDatabase.getInstance(application).usuarioDao();

        // Executor para rodar as tarefas de banco de dados fora da Thread Principal (UI Thread)[cite: 17]
        executorService = Executors.newSingleThreadExecutor();

        // O Room retorna automaticamente o utilizador que tem isLogado == true
        // Assumindo que você criou o método getUsuarioAtivo() no UsuarioDao
        usuarioAtivo = usuarioDao.getUsuarioAtivo();
    }

    public LiveData<Boolean> getCadastroSucesso() {
        return cadastroSucesso;
    }

    // CORREÇÃO: O parâmetro caminhoFoto (String) foi alterado para fotoPerfil (byte[])
    public void salvarUsuario(String nome, String senha, byte[] fotoPerfil) {
        executorService.execute(() -> {
            try {
                // 1. Criptografar a senha usando a classe utilitária[cite: 17]
                String senhaCriptografada = CriptografiaUtils.gerarHashSenha(senha);

                // 2. Criar a entidade Usuario com os dados, a senha já em hash e o array de bytes da foto
                Usuario novoUsuario = new Usuario(nome, senhaCriptografada, fotoPerfil);

                // (Opcional) Se quiser que o utilizador já fique logado logo após o cadastro:
                // novoUsuario.isLogado = true;

                // 3. Salvar no banco[cite: 17]
                usuarioDao.inserirUsuario(novoUsuario);

                // 4. Notificar a View (Activity) sobre o sucesso[cite: 17]
                cadastroSucesso.postValue(true);
            } catch (Exception e) {
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

                if (usuarioEncontrado != null) {
                    // Atualiza a flag na base de dados para indicar que este utilizador está com a sessão ativa
                    usuarioDao.marcarComoLogado(usuarioEncontrado.getId());
                    // O LiveData 'usuarioAtivo' vai atualizar automaticamente a MainActivity
                }
            } catch (Exception e) {
                // Falha no login, pode implementar um LiveData separado para avisar a View de erro se desejar
            }
        });
    }

    public void fazerLogout() {
        executorService.execute(() -> {
            // Desmarca qualquer utilizador que esteja com a sessão ativa na base de dados
            usuarioDao.fazerLogout();
        });
    }
}