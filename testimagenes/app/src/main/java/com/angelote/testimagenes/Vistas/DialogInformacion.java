package com.angelote.testimagenes.Vistas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.angelote.testimagenes.Clases.Photos;
import com.angelote.testimagenes.R;
import com.angelote.testimagenes.Vistas.Adapters.AdapterImagenesUsuario;
import com.angelote.testimagenes.databinding.DialogInformacionBinding;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DialogInformacion extends DialogFragment {
    private DialogInformacionBinding binding;
    private final ArrayList<Photos> usuario;

    public DialogInformacion(ArrayList<Photos> usuario) {
        this.usuario = usuario;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DialogInformacionBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(requireContext()).load(usuario.get(0).getUser().getProfile_image().getMedium()).placeholder(R.drawable.loading).into(binding.ivPerfil);
        binding.tvNombre.setText(usuario.get(0).getUser().getUsername());
        binding.tvBio.setText(usuario.get(0).getUser().getBio());
        binding.tvMeGusta.setText(String.format(Locale.US, "%d", usuario.get(0).getUser().getTotal_likes()));
        binding.tvFotos.setText(String.format(Locale.US, "%d", usuario.get(0).getUser().getTotal_photos()));
        binding.tvColeccion.setText(String.format(Locale.US, "%d", usuario.get(0).getUser().getTotal_collections()));
        AdapterImagenesUsuario adapterImagenesUsuario = new AdapterImagenesUsuario(usuario, requireContext(), this);
        binding.rvImagenes.setAdapter(adapterImagenesUsuario);
        binding.rvImagenes.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
    }

    public void mostrarImagen(String bitmap, ImageView imageView) {
        imageView.setEnabled(false);
        imageView.setClickable(false);
        imageView.setFocusableInTouchMode(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogTheme);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.dialog_foto_zoom, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PhotoView imagenPerfil = dialogLayout.findViewById(R.id.imageView_lista_abonos_foto_perfil_preview);
        Glide.with(requireContext()).load(bitmap).placeholder(R.drawable.loading).dontAnimate().into(imagenPerfil);
        dialog.setOnDismissListener(dialog1 -> {
            imageView.setEnabled(true);
            imageView.setClickable(true);
            imageView.setFocusableInTouchMode(true);
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * .95);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * .95);
        Objects.requireNonNull(getDialog()).getWindow().setLayout(widthPixels, heightPixels);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }
}
