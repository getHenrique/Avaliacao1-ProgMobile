package edu.facom.avaliacao1.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nomeUsuario;

    public String senha;

    public String caminhoFoto;
    
    public boolean sessaoAtiva;


    public Usuario() {
    }

    public Usuario(String nomeUsuario, String senha, String caminhoFoto) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.caminhoFoto = caminhoFoto;
    }
}
