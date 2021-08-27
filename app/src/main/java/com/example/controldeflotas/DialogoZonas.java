package com.example.controldeflotas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DialogoZonas {

    final String PLANTA = "Planta Hormigón";
    final String OBRA = "Obra Cliente";
    final String OFICINA = "Oficina Central";
    final String ZONA = "Zona 4";
    final String TALLERES = "Talleres";

    public DialogoZonas(Context context, Zona zona){
        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_zonas);

        Button btnCancelar, btnGuardar;
        EditText etCodigo, etDescripcion, etDireccion;//etTipo
        Spinner spTipo;

        btnCancelar = dialogo.findViewById(R.id.btnCancelar);
        btnGuardar = dialogo.findViewById(R.id.btnGuardar);

        etCodigo = dialogo.findViewById(R.id.etCodigo);
        etDescripcion = dialogo.findViewById(R.id.etDescripcion);
        etDireccion = dialogo.findViewById(R.id.etDireccion);
        spTipo = dialogo.findViewById(R.id.spTipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.tiposZonas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);

        if(zona.getCodigo()!=null){
            etCodigo.setText(zona.getCodigo());
            etDescripcion.setText(zona.getDescripcion());
            etDireccion.setText(zona.getDireccion());
            switch (zona.getTipo()){
                case PLANTA:
                    spTipo.setSelection(0);
                    break;
                case OBRA:
                    spTipo.setSelection(1);
                    break;
                case OFICINA:
                    spTipo.setSelection(2);
                    break;
                case ZONA:
                    spTipo.setSelection(3);
                    break;
                case TALLERES:
                    spTipo.setSelection(4);
                    break;
            }
        }

        if(zona.getDireccion()!=null){
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
                alerta.setMessage("Guardar los datos modificados de " + etCodigo.getText().toString() + "?")
                //alerta.setMessage("Guardar los datos modificados de " +zona.getCodigo() + "?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.cancel();
                            }
                        })
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogo.dismiss();
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
