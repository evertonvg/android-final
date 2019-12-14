package br.edu.ifsul.loja.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;
import br.edu.ifsul.loja.R;
import br.edu.ifsul.loja.model.Pedido;
public class PedidosAdapter extends ArrayAdapter<Pedido> {
    private final Context context;

    public PedidosAdapter(Context context, List<Pedido> pedido) {
        super(context, 0, pedido);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pedido_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Pedido itemPedido = getItem(position);
        holder.nomeProduto.setText(itemPedido.getCliente().getNome());
        holder.data.setText(itemPedido.getDataCriacao());
        holder.totalDoItem.setText(NumberFormat.getCurrencyInstance().format(itemPedido.getTotalPedido()));
        return convertView;
    }

    private class ViewHolder {
        TextView nomeProduto;
        TextView data;
        TextView totalDoItem;
        public ViewHolder(View convertView) {
            nomeProduto = convertView.findViewById(R.id.tvNomeProdutoCarrinhoAdapter);
            data = convertView.findViewById(R.id.tvdata_pedido);
            totalDoItem = convertView.findViewById(R.id.tvTotalItemCarrinhoAdapter);
        }
    }
}
