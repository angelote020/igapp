package com.angelote.testimagenes;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.angelote.testimagenes.Clases.ImagenesServicio;
import com.angelote.testimagenes.Interfaces.ApiService;
import com.angelote.testimagenes.Interfaces.IComunicador;
import com.angelote.testimagenes.Utilerias.DatabaseSqlite;
import com.angelote.testimagenes.Utilerias.RequestManager;
import com.angelote.testimagenes.Utilerias.UTBaseActivity;
import com.angelote.testimagenes.Vistas.ContactoFragment;
import com.angelote.testimagenes.Vistas.FavoritosFragment;
import com.angelote.testimagenes.Vistas.PrincipalFragment;
import com.angelote.testimagenes.databinding.ActivityMainBinding;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends UTBaseActivity implements IComunicador {

    private ActivityMainBinding binding;
    private ArrayList<ImagenesServicio> fotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        inicializarBD();
        binding.bottomBar.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        binding.bottomBar.add(new MeowBottomNavigation.Model(2, R.drawable.ic_favorito_vacio));
        binding.bottomBar.add(new MeowBottomNavigation.Model(3, R.drawable.ic_user));
        binding.bottomBar.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    MostrarLogin();
                    break;
                case 2:
                    MostrarFavoritos();
                    break;
                case 3:
                    MostrarPerfil();
                    break;
            }
            return null;
        });
        binding.bottomBar.show(1, true);
        CargarImagenes();
    }

    private void inicializarBD() {
        DatabaseSqlite databaseSqlite = new DatabaseSqlite(this);
        databaseSqlite.getWritableDatabase();
    }

    private void CargarImagenes() {
        mostrarProgress(true);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("client_id", "VVNWIewJbn2Tndw7EoVue2hi75lZjjQdpy5KOyN4Mao");
        RequestManager requestManager = new RequestManager("https://api.unsplash.com/");
        requestManager.create(ApiService.class).ObtenerImagenes(parametros).enqueue(new Callback<ArrayList<ImagenesServicio>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<ImagenesServicio>> call, @NotNull Response<ArrayList<ImagenesServicio>> response) {
                fotos = response.body();
                MostrarLogin();
                mostrarProgress(false);
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<ImagenesServicio>> call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de conexión.", Toast.LENGTH_SHORT).show();
                mostrarProgress(false);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void mostrarProgress(Boolean valor) {
        if (valor) {
            binding.pbCarga.setVisibility(View.VISIBLE);
            binding.pbBack.setVisibility(View.VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.pbCarga.setVisibility(View.GONE);
            binding.pbBack.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void MostrarLogin() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("fotos" ,fotos);
        mostrarFragment(PrincipalFragment.class, binding.clPrincial.getId(), bundle, false);
    }

    @Override
    public void MostrarFavoritos() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("fotos" ,fotos);
        mostrarFragment(FavoritosFragment.class, binding.clPrincial.getId(), bundle, false);
    }

    @Override
    public void MostrarPerfil() {
        mostrarFragment(ContactoFragment.class, binding.clPrincial.getId(), null, false);
    }
}