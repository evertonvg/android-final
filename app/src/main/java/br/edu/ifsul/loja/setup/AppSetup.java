package br.edu.ifsul.loja.setup;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsul.loja.model.Cliente;
import br.edu.ifsul.loja.model.ItemPedido;
import br.edu.ifsul.loja.model.Pedido;
import br.edu.ifsul.loja.model.Produto;
import br.edu.ifsul.loja.model.Usuario;



public class AppSetup {
    public static List<Produto> produtos = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<ItemPedido> carrinho = new ArrayList<>();
    public static List<Pedido> pedidos = new ArrayList<>();
    public static List<ItemPedido> itemPedidos = new ArrayList<>();
    public static Cliente cliente = null;
    public static Pedido pedido = null;
    public static Usuario user = null;
    public static FirebaseAuth mAuth = null;

    public static Map<String, Bitmap> cacheProdutos = new HashMap<>();
    public static Map<String, Bitmap> cacheClientes = new HashMap<>();
}
