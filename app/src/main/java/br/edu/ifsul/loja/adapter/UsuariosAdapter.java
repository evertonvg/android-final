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
import br.edu.ifsul.loja.model.Usuario;

public class UsuariosAdapter extends ArrayAdapter<Usuario> {
    private final Context context;

    public UsuariosAdapter(@NonNull Context context, @NonNull List<Usuario> usuarios){
        super(context, 0, usuarios);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        //infla a view
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_usuario_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final Usuario usuario = getItem(position);

        String nomeCompleto = usuario.getNome().concat(" ").concat(usuario.getSobrenome());

        holder.tvNomeUsuarioAdapter.setText(nomeCompleto);
        holder.tvEmailUsuarioAdapter.setText(usuario.getEmail());
        holder.tvFuncaoUsuarioAdapter.setText(usuario.getFuncao());

        return convertView;
    }

    private class ViewHolder{
        final TextView tvNomeUsuarioAdapter;
        final TextView tvEmailUsuarioAdapter;
        final TextView tvFuncaoUsuarioAdapter;

        private ViewHolder(View view){
            tvNomeUsuarioAdapter = view.findViewById(R.id.tvNomeUsuarioAdapter);
            tvEmailUsuarioAdapter = view.findViewById(R.id.tvEmailUsuarioAdapter);
            tvFuncaoUsuarioAdapter = view.findViewById(R.id.tvFuncaoUsuarioAdapter);
        }
    }
}
