package com.angelote.testimagenes.Vistas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angelote.testimagenes.Utilerias.UTBaseFragment;
import com.angelote.testimagenes.databinding.ContactoFragmentBinding;

import org.jetbrains.annotations.NotNull;

public class ContactoFragment extends UTBaseFragment {
    private ContactoFragmentBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = ContactoFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLlamar.setOnClickListener(v -> {
            if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                mPermissionResult.launch(Manifest.permission.CALL_PHONE);
            } else {
                String mobileNumber = "5628291190";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + mobileNumber));
                startActivity(intent);
            }
        });
        binding.btnEmail.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"angel_isc_021@hotmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "IGApp");
            i.putExtra(Intent.EXTRA_TEXT, "Hola miguel angel, ");
            try {
                startActivity(Intent.createChooser(i, "Enviar mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(), "No tiene aplicaciones de correo instaladas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    String mobileNumber = "5628291190";
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: " + mobileNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Favor de aceptar permisos.", Toast.LENGTH_SHORT).show();
                }
            });
}
