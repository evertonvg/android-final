package br.edu.ifsul.loja.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.edu.ifsul.loja.R;
import br.edu.ifsul.loja.barcode.BarcodeCaptureActivity;
import br.edu.ifsul.loja.model.Cliente;

public class ClienteAdminActivity extends AppCompatActivity {
    private static final String TAG = "clienteAdminActivity";
    private static final int RC_BARCODE_CAPTURE = 1, RC_GALERIA_IMAGE_PICK = 2;
    private EditText etCodigoDeBarras, etNome, etSobrenome, etCPF;
    private Button btSalvar, btPedidos;
    private ImageView imvFoto;
    private Cliente cliente;

    private byte[] fotoCliente = null; //foto do produto
    private Uri arquivoUri;
    private FirebaseDatabase database;
    private boolean flagInsertOrUpdate = true;
    private ProgressDialog mProgressDialog; //um modal de progressão (com uma animação da progressão)
    private ImageButton imbPesquisar;
    private ProgressBar pbFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_admin);

        database = FirebaseDatabase.getInstance();

        cliente = new Cliente();

        etCodigoDeBarras = findViewById(R.id.etCodigoDeBarras_clienteAdmin);
        etNome = findViewById(R.id.etNomeClienteTelaAdmin);
        etSobrenome = findViewById(R.id.etSobrenomeClienteTelaAdmin);
        etCPF = findViewById(R.id.etCPFClienteTelaAdmin);
        btSalvar = findViewById(R.id.btSalvarClienteTelaAdmin);
        btPedidos = findViewById(R.id.bt_verPedidos);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCodigoDeBarras.getText().toString().isEmpty() && !etNome.getText().toString().isEmpty() &&
                !etSobrenome.getText().toString().isEmpty() && !etCPF.getText().toString().isEmpty()){


                    cliente.setCodigoDeBarras(Long.valueOf(etCodigoDeBarras.getText().toString()));
                    cliente.setNome(etNome.getText().toString());
                    cliente.setSobrenome(etSobrenome.getText().toString());
                    cliente.setCpf(etCPF.getText().toString());

                    Log.d("TAG", "Objeto de cliente: " + cliente);

                    saveClient();
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_cliente_admin), R.string.snack_preencher_todos_campos, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        btPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClienteAdminActivity.this, PedidosActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_cliente_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_produtos_cdbar:{
                // launch barcode activity.
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); //true liga a funcionalidade autofoco
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false); //true liga a lanterna (fash)
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                break;
            }

            case R.id.menuitem_limparform_admin:{
                cleanForm();
                break;
            }
            case android.R.id.home:{
                finish();
                break;
            }

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    etCodigoDeBarras.setText(barcode.displayValue);

                    searchDb(Long.valueOf(etCodigoDeBarras.getText().toString()));
                }
            } else {
                Toast.makeText(this, String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void searchDb(Long codigoDeBarras){
        DatabaseReference myRef = database.getReference("clientes/");
        Log.d("TAG", "Barcode: "+codigoDeBarras);

        Query query = myRef.orderByChild("codigoDeBarras").equalTo(codigoDeBarras).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "dataSnapshot = " + dataSnapshot.getValue());
                if(dataSnapshot.getValue() != null){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        cliente = ds.getValue(Cliente.class);
                        cliente.setKey(ds.getKey());
                    }
                    flagInsertOrUpdate = false;

                    loadView();
                }else{
                    Toast.makeText(ClienteAdminActivity.this, getString(R.string.toast_produto_nao_cadastrado), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //providenciar retorno em caso de erro
            }
        });
    }

    private void loadView(){
        etCodigoDeBarras.setText(cliente.getCodigoDeBarras().toString());
        etCodigoDeBarras.setEnabled(false);
        etNome.setText(cliente.getNome());
        etSobrenome.setText(cliente.getSobrenome());
        etCPF.setText(cliente.getCpf());
    }

    private void cleanForm(){
        cliente = new Cliente();

        etCodigoDeBarras.setEnabled(true);
        etCodigoDeBarras.setText(null);
        etNome.setText(null);
        etSobrenome.setText(null);
        etCPF.setText(null);
    }

    public void saveClient(){

    }

}
