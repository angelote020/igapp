package com.angelote.testimagenes.Vistas.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angelote.testimagenes.Clases.ImagenesServicio;
import com.angelote.testimagenes.Clases.Photos;
import com.angelote.testimagenes.Interfaces.ApiService;
import com.angelote.testimagenes.Interfaces.IComunicador;
import com.angelote.testimagenes.R;
import com.angelote.testimagenes.Utilerias.DatabaseSqlite;
import com.angelote.testimagenes.Utilerias.RequestManager;
import com.angelote.testimagenes.Vistas.DialogInformacion;
import com.angelote.testimagenes.databinding.ItemImagenBinding;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterImagenes extends RecyclerView.Adapter<AdapterImagenes.Imagenes> {
    private final ArrayList<ImagenesServicio> fotos;
    private final ArrayList<ImagenesServicio> fotosCopia;
    private final Context context;
    private final FragmentManager childFragmentManager;
    private IComunicador interfaz;
    private ArrayList<Photos> usuario;

    public AdapterImagenes(ArrayList<ImagenesServicio> fotos, ArrayList<ImagenesServicio> fotosCopia, Context context, FragmentManager childFragmentManager, IComunicador interfaz) {
        this.fotos = fotos;
        this.context = context;
        this.childFragmentManager = childFragmentManager;
        this.fotosCopia = fotosCopia;
        this.interfaz = interfaz;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterImagenes.Imagenes onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AdapterImagenes.Imagenes(ItemImagenBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterImagenes.Imagenes holder, int position) {
        if (fotos.get(position).getFavorito()) {
            holder.binding.ivFavorito.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorito_lleno, null));
        }
        Glide.with(context).load(Objects.requireNonNull(fotos.get(position).getUrls()).getRegular()).placeholder(R.drawable.loading).dontAnimate().into(holder.binding.ivFoto);
        Glide.with(context).load(Objects.requireNonNull(Objects.requireNonNull(fotos.get(position).getUser()).getProfile_image()).getMedium()).placeholder(R.drawable.loading).into(holder.binding.ivPerfil);
        holder.binding.tvNombre.setText(Objects.requireNonNull(fotos.get(position).getUser()).getUsername());
        holder.binding.tvLikes.setText(String.format(Locale.US, "%d", fotos.get(position).getLikes()));
        holder.binding.ivFoto.setOnClickListener(v -> {
            ConsultarUsuario(fotos.get(position).getUser().getUsername());
        });
        holder.binding.ivFavorito.setOnClickListener(v -> {
            if (!fotos.get(position).getFavorito()) {
                holder.binding.ivFavorito.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorito_lleno, null));
                DatabaseSqlite databaseSqlite = new DatabaseSqlite(context);
                SQLiteDatabase db = databaseSqlite.getWritableDatabase();
                db.execSQL(String.format(Locale.US, "INSERT INTO favoritos (Id) VALUES ('%s')", fotos.get(position).getId()));
                fotos.get(position).setFavorito(true);
            } else {
                holder.binding.ivFavorito.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorito_vacio, null));
                DatabaseSqlite databaseSqlite = new DatabaseSqlite(context);
                SQLiteDatabase db = databaseSqlite.getWritableDatabase();
                db.execSQL(String.format(Locale.US, "DELETE FROM favoritos WHERE Id='%s'", fotos.get(position).getId()));
                fotos.get(position).setFavorito(false);
            }
            notifyItemChanged(position);
        });
    }

    private void ConsultarUsuario(String username) {
        interfaz.mostrarProgress(true);
        String url = String.format(Locale.US, "%s/photos?client_id=%s", username, "VVNWIewJbn2Tndw7EoVue2hi75lZjjQdpy5KOyN4Mao");
        RequestManager requestManager = new RequestManager("https://api.unsplash.com/users/");
        requestManager.create(ApiService.class).ObtenerUsuario(url).enqueue(new Callback<ArrayList<Photos>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<Photos>> call, @NotNull Response<ArrayList<Photos>> response) {
                usuario = response.body();
                DialogFragment dialogFragment = new DialogInformacion(usuario);
                dialogFragment.show(childFragmentManager, dialogFragment.getTag());
                interfaz.mostrarProgress(false);
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<Photos>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Error de conexión.", Toast.LENGTH_SHORT).show();
                interfaz.mostrarProgress(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public void filter(String palabra) {
        fotos.clear();
        if (palabra.isEmpty()) {
            fotos.addAll(fotosCopia);
        } else {
            for (ImagenesServicio item : fotosCopia) {
                if (item.getUser().getUsername().toLowerCase().contains(palabra)) {
                    if (!fotos.contains(item)) {
                        fotos.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class Imagenes extends RecyclerView.ViewHolder {
        ItemImagenBinding binding;

        public Imagenes(ItemImagenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
