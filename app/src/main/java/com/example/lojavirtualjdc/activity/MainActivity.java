package com.example.lojavirtualjdc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.lojavirtualjdc.R;
import com.example.lojavirtualjdc.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        inicializaComponentes();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {

                        //Verifica estado do Switch
                        if (tipoAcesso.isChecked()) {//aqui ele ira fazer o cadastro do usuario

                            autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String erroExcecao;

                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthWeakPasswordException e) {
                                                erroExcecao = "Digite uma senha mais forte!";
                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                erroExcecao = "Por favor, digite um e-mail válido";
                                            } catch (FirebaseAuthUserCollisionException e) {
                                                erroExcecao = "Esta conta já foi cadastrada";
                                            } catch (Exception e) {
                                                erroExcecao = "ao cadastrar usuário:  " + e.getMessage();
                                                e.printStackTrace();
                                            }

                                            Toast.makeText(MainActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {// aqui  ira fazer o login do usuario

                            autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Erro ao fazer o login:  " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Preencha a Senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Preencha o E-mail!", Toast.LENGTH_SHORT).show();
                }
                ;
            }
        });
    }

    private void inicializaComponentes() {
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }


}