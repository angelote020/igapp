package com.angelote.testimagenes.Vistas.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.angelote.testimagenes.Clases.Photos;
import com.angelote.testimagenes.R;
import com.angelote.testimagenes.Vistas.DialogInformacion;
import com.angelote.testimagenes.databinding.ItemImagenSolaBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterImagenesUsuario extends RecyclerView.Adapter<AdapterImagenesUsuario.Imagenes> {
    private ArrayList<Photos> photos;
    private Context context;
    private DialogInformacion dialogInformacion;

    public AdapterImagenesUsuario(ArrayList<Photos> photos, Context context, DialogInformacion dialogInformacion) {
        this.photos = photos;
        this.context = context;
        this.dialogInformacion = dialogInformacion;
    }

    @NonNull
    @NotNull
    @Override
    public Imagenes onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Imagenes(ItemImagenSolaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Imagenes holder, int position) {
        Glide.with(context).load(photos.get(position).getUrls().getSmall()).placeholder(R.drawable.loading).dontAnimate().into(holder.binding.ivFoto);
        holder.binding.ivFoto.setOnClickListener(v -> {
            dialogInformacion.mostrarImagen(photos.get(position).getUrls().getRegular(), holder.binding.ivFoto);
        });
    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class Imagenes extends RecyclerView.ViewHolder {
        ItemImagenSolaBinding binding;

        public Imagenes(ItemImagenSolaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
