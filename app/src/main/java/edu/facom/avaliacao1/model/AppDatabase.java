package edu.facom.avaliacao1.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Se alterar a estrutura da entidade no futuro, aumentar a "version"
@Database(entities = {Usuario.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "banco_app_cadastro")
                            .fallbackToDestructiveMigration() // Recria o banco se a versão mudar sem script de migração
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}