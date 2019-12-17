package br.edu.ifsul.loja.activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;
import br.edu.ifsul.loja.R;
import br.edu.ifsul.loja.model.Usuario;
import br.edu.ifsul.loja.setup.AppSetup;

public class UserAdminActivity extends AppCompatActivity {
    private static final String TAG = "usuarioAdminActivity";
    private FirebaseAuth mAuth;
    private EditText etEmailUser, etPasswordUser, etNomeUser, etSobrenomeuser;
    private Spinner spFuncaoUser;
    private String[] FUNCAO = new String[]{"Vendedor", "Administrador", "Cliente"};
    private Button cadastrar;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_admin);
        mAuth = FirebaseAuth.getInstance();
        AppSetup.mAuth = mAuth;
        etNomeUser = findViewById(R.id.etNomeUser);
        etSobrenomeuser = findViewById(R.id.etSobrenomeUser);
        etEmailUser = findViewById(R.id.etEmailUser);
        etPasswordUser = findViewById(R.id.etPasswordUser);
        spFuncaoUser = findViewById(R.id.spFuncaoUser);
        cadastrar = findViewById(R.id.btCadastrarUser);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FUNCAO);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFuncaoUser.setAdapter(adapter);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailUser.getText().toString();
                String senha = etPasswordUser.getText().toString();
                if(!email.isEmpty() && !senha.isEmpty()){
                    signup(email, senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_user_admin), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_usuario_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menuitem_limparform_admin:
                cleanForm();
                break;
            case R.id.menuitem_produtos_cdbar:
                Intent intent = new Intent(UserAdminActivity.this, UserActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    private void signup(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                cadastrarUser();
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                if(Objects.requireNonNull(task.getException()).getMessage().contains("email")){
                    Snackbar.make(findViewById(R.id.container_activity_user_admin), R.string.email_already, Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(findViewById(R.id.container_activity_user_admin), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                }
            }
            }
        });
    }

    private void cadastrarUser(){
        usuario = new Usuario();
        usuario.setFirebaseUser(mAuth.getCurrentUser());
        usuario.setNome(etNomeUser.getText().toString());
        usuario.setSobrenome(etSobrenomeuser.getText().toString());
        usuario.setFuncao(FUNCAO[spFuncaoUser.getSelectedItemPosition()]);
        usuario.setEmail(mAuth.getCurrentUser().getEmail());
        FirebaseDatabase.getInstance().getReference().child("vendas").child("usuarios").child(usuario.getFirebaseUser().getUid()).setValue(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        cleanForm();
                        Snackbar.make(findViewById(R.id.container_activity_user_admin), R.string.user_cadastrado, Snackbar.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.container_activity_user_admin), R.string.nao_cadastro_user, Snackbar.LENGTH_LONG).show();
                    }
                });
        AppSetup.user = usuario;
    }
    private void cleanForm(){
        usuario = new Usuario();
        etNomeUser.setText(null);
        etSobrenomeuser.setText(null);
        etEmailUser.setText(null);
        etPasswordUser.setText(null);
        mAuth = null;
    }
}