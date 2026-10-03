package edu.facom.avaliacao1.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsuarioDao {

    @Insert
    void inserirUsuario(Usuario usuario);

    @Update
    void atualizarUsuario(Usuario usuario);

    // Método útil para verificar se já existe o usuário ou fazer login futuro
    @Query("SELECT * FROM usuarios WHERE nomeUsuario = :nome LIMIT 1")
    Usuario buscarUsuarioPorNome(String nome);
    // Método para validar login de usuario
    @Query("SELECT * FROM usuarios WHERE nomeUsuario = :nome AND senha = :senhaCriptografada LIMIT 1")
    Usuario validarLogin(String nome, String senhaCriptografada);

    @Query("SELECT * FROM usuarios WHERE sessaoAtiva = 1 LIMIT 1")
    Usuario buscarUsuarioLogado();

    @Query("UPDATE usuarios SET sessaoAtiva = 0")
    void encerrarSessoes();
}
