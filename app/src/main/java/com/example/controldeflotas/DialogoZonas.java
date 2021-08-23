package com.example.controldeflotas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialogoZonas {

    public DialogoZonas(Context context, Zona zona){
        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_zonas);

        Button btnCancelar, btnGuardar;
        EditText etCodigo, etTipo, etDescripcion, etDireccion;

        btnCancelar = dialogo.findViewById(R.id.btnCancelar);
        btnGuardar = dialogo.findViewById(R.id.btnGuardar);

        etCodigo = dialogo.findViewById(R.id.etCodigo);
        etTipo = dialogo.findViewById(R.id.etTipo);
        etDescripcion = dialogo.findViewById(R.id.etDescripcion);
        etDireccion = dialogo.findViewById(R.id.etDireccion);

        if(zona!=null){
            etCodigo.setText(zona.getCodigo());
            etTipo.setText(zona.getTipo());
            etDescripcion.setText(zona.getDescripcion());
            etDireccion.setText(zona.getDireccion());
        }


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                //alerta.setMessage("Guardar los datos modificados de " +etCodigo.getText().toString() + "?")
                alerta.setMessage("Guardar los datos modificados de " +zona.getCodigo() + "?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.cancel();
                            }
                        })
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Ha seleccionado guardar la zona editada " + zona.getCodigo(), Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Modificar Zona");
                titulo.show();
            }
        });

        dialogo.show();
    }
}
