package edu.facom.avaliacao1.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.facom.avaliacao1.R;
import edu.facom.avaliacao1.viewmodel.CadastroViewModel;

public class LoginFragment extends Fragment {
    private CadastroViewModel viewModel;
    private EditText editTextUsuario;
    private EditText editTextSenha;
    private Button btnEntrar;
    private TextView textViewCadastrar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsuario = view.findViewById(R.id.edit_text_usuario);
        editTextSenha = view.findViewById(R.id.edit_text_senha);
        btnEntrar = view.findViewById(R.id.btn_entrar);
        textViewCadastrar = view.findViewById(R.id.text_view_cadastrar);

        viewModel = new ViewModelProvider(requireActivity()).get(CadastroViewModel.class);

        btnEntrar.setOnClickListener(v -> {
            String usuario = editTextUsuario.getText().toString();
            String senha = editTextSenha.getText().toString();

            if (usuario.isEmpty() || senha.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.fazerLogin(usuario, senha);
            }
        });

        textViewCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CadastroActivity.class);
            intent.putExtra("MODO_TELA", "CADASTRO");
            startActivity(intent);
        });

        return view;
    }
}