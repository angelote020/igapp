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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.angelote.testimagenes.Clases.ImagenesServicio;
import com.angelote.testimagenes.Interfaces.ApiService;
import com.angelote.testimagenes.Interfaces.IComunicador;
import com.angelote.testimagenes.Utilerias.DatabaseSqlite;
import com.angelote.testimagenes.Utilerias.RequestManager;
import com.angelote.testimagenes.Utilerias.UTBaseFragment;
import com.angelote.testimagenes.Vistas.Adapters.AdapterImagenes;
import com.angelote.testimagenes.databinding.PrincipalFragmentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrincipalFragment extends UTBaseFragment {

    private PrincipalFragmentBinding binding;
    private ArrayList<ImagenesServicio> fotos;
    private IComunicador interfaz;
    private ArrayList<String> Ids;
    private AdapterImagenes adapterImagenes;
    private int contador = 2;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = PrincipalFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        fotos = (ArrayList<ImagenesServicio>) getArguments().getSerializable("fotos");
        VerificarBD();
        binding.tvMostrarMas.setOnClickListener(v -> {
            contador++;
            CargarImagenes();
        });
    }

    private void CargarImagenes() {
        interfaz.mostrarProgress(true);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("client_id", "VVNWIewJbn2Tndw7EoVue2hi75lZjjQdpy5KOyN4Mao");
        parametros.put("page", String.valueOf(contador));
        RequestManager requestManager = new RequestManager("https://api.unsplash.com/");
        requestManager.create(ApiService.class).ObtenerImagenes(parametros).enqueue(new Callback<ArrayList<ImagenesServicio>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<ImagenesServicio>> call, @NotNull Response<ArrayList<ImagenesServicio>> response) {
                assert response.body() != null;
                ArrayList<String> filtro = new ArrayList<>();
                fotos.addAll(response.body());
                adapterImagenes.notifyDataSetChanged();
                for (ImagenesServicio foto : fotos) {
                    if (!filtro.contains(foto.getUser().getUsername())) {
                        filtro.add(foto.getUser().getUsername());
                    }
                }
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
                interfaz.mostrarProgress(false);
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<ImagenesServicio>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), "Error de conexión.", Toast.LENGTH_SHORT).show();
                interfaz.mostrarProgress(false);
            }
        });
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
                            foto.setFavorito(true);
                        }
                        if (!filtro.contains(foto.getUser().getUsername())) {
                            filtro.add(foto.getUser().getUsername());
                        }
                    }
                }
            }
        }

        adapterImagenes = new AdapterImagenes(fotos, (ArrayList<ImagenesServicio>) fotos.clone(), getContext(), getChildFragmentManager(), interfaz);
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
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof IComunicador) {
            interfaz = (IComunicador) context;
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }
}
