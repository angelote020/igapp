package com.angelote.testimagenes.Vistas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.angelote.testimagenes.Clases.ImagenesServicio;
import com.angelote.testimagenes.Interfaces.IComunicador;
import com.angelote.testimagenes.Utilerias.DatabaseSqlite;
import com.angelote.testimagenes.Utilerias.UTBaseFragment;
import com.angelote.testimagenes.Vistas.Adapters.AdapterImagenes;
import com.angelote.testimagenes.databinding.FavoritosFragmentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritosFragment extends UTBaseFragment {

    private FavoritosFragmentBinding binding;
    private ArrayList<String> Ids;
    private ArrayList<ImagenesServicio> fotos;
    private ArrayList<ImagenesServicio> favoritos;
    private AdapterImagenes adapterImagenes;
    private IComunicador interfaz;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FavoritosFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        fotos = (ArrayList<ImagenesServicio>) getArguments().getSerializable("fotos");
        favoritos = new ArrayList<>();
        VerificarBD();
    }

    private void VerificarBD() {
        ArrayList<String> filtro = new ArrayList<>();
        DatabaseSqlite databaseSqlite = new DatabaseSqlite(getActivity());
        SQLiteDatabase db = databaseSqlite.getWritableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM favoritos", null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                Ids = new ArrayList<>();
                do {
                    Ids.add(rs.getString(0));
                } while (rs.moveToNext());
            }
            rs.close();
        }
        db.close();
        if (Ids != null) {
            if (Ids.size() > 0) {
                assert fotos != null;
                for (ImagenesServicio foto : fotos) {
                    for (int j = 0; j < Ids.size(); j++) {
                        if (Objects.equals(foto.getId(), Ids.get(j))) {
                            favoritos.add(foto);
                        }
                        if (!filtro.contains(Objects.requireNonNull(foto.getUser()).getUsername())) {
                            filtro.add(foto.getUser().getUsername());
                        }
                    }
                }
            }
        }
        if (favoritos.size() > 0) {
            binding.rvImagenes.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.VISIBLE);
            adapterImagenes = new AdapterImagenes(favoritos, (ArrayList<ImagenesServicio>) favoritos.clone(), getContext(), getChildFragmentManager(), interfaz);
            binding.rvImagenes.setAdapter(adapterImagenes);
            binding.rvImagenes.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
            binding.searchView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, filtro));
            binding.searchView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapterImagenes.filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {
            //TODO nada lottie
            binding.tvSinFavoritos.setVisibility(View.VISIBLE);
            binding.lottie.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof IComunicador) {
            interfaz = (IComunicador) context;
        }
    }
}
