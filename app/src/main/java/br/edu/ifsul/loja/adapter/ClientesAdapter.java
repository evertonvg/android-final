package br.edu.ifsul.loja.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsul.loja.R;
import br.edu.ifsul.loja.model.Cliente;

public class ClientesAdapter extends ArrayAdapter<Cliente> {
    private Context context;

    public ClientesAdapter(@NonNull Context context, @NonNull List<Cliente> clientes) {
        super(context, 0, clientes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        //infla a view
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cliente_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //bindview
        final Cliente cliente = getItem(position);

        holder.tvNome.setText(cliente.getNome() + " " + cliente.getSobrenome());
        holder.tvCPF.setText(cliente.getCpf());
        //        holder.tvSobrenome.setText(cliente.getSobrenome());


        return convertView;
    }

    private class ViewHolder {
        final TextView tvNome;
        final TextView tvCPF;
//        final TextView tvSobrenome;

        public ViewHolder(View view) {
            //mapeia os componentes da UI para vincular os dados do objeto de modelo
            tvNome = view.findViewById(R.id.tvNomeClienteAdapter);
            tvCPF = view.findViewById(R.id.tvDetalhesDoClienteAdapater);
//            tvSobrenome = view.findViewById(R.id.tv)
        }
    }
}
