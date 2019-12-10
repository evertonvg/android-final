package br.edu.ifsul.loja.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class Usuario {
    private FirebaseUser firebaseUser;
    private String nome;
    private String sobrenome;
    private String funcao;
    private String email;
    private String key; //apenas interno


    public Usuario(){
    }

    @Exclude
    public FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

    @Exclude
    public void setFirebaseUser (FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao){
        this.funcao = funcao;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getSobrenome(){
        return sobrenome;
    }

    public void setSobrenome(String sobrenome){
        this.sobrenome = sobrenome;
    }
}
