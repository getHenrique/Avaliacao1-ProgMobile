package edu.facom.avaliacao1.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nomeUsuario;

    public String senha;

    // A anotação BLOB garante o armazenamento correto dos bytes da imagem no SQLite
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] fotoPerfil;

    public boolean isLogado;

    public Usuario() {
    }

    // Construtor atualizado para receber o vetor de bytes em vez da String[cite: 38, 40]
    public Usuario(String nomeUsuario, String senha, byte[] fotoPerfil) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
    }

    // Getters necessários para a MainActivity carregar o Avatar e o Nome dinamicamente
    public int getId() {
        return id;
    }

    public String getNome() {
        return nomeUsuario;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }
}