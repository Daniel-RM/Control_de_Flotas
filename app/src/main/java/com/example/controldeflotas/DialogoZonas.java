package com.example.controldeflotas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.AsynchronousByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DialogoZonas {

    final String PLANTA = "Planta Hormigón";
    final String OBRA = "Obra Cliente";
    final String OFICINA = "Oficina Central";
    final String ZONA = "Zona 4";
    final String TALLERES = "Talleres";

    String posiciones="";

    String lat0, long0, lat1, long1, lat2, long2, lat3, long3 = "";

    URL url = null;
    URLConnection connection = null;

    public DialogoZonas(Context context, Zona zona, List<LatLng> listaPuntos, boolean edicion){
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
                alerta.setMessage("Guardar los datos de " + etCodigo.getText().toString() + "?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.cancel();
                            }
                        })
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(etCodigo.getText().toString().isEmpty() || etDescripcion.getText().toString().isEmpty() || etDireccion.getText().toString().isEmpty()){
                                    Toast.makeText(context, "Por favor, no deje ningún dato vacío", Toast.LENGTH_SHORT).show();
                                }else {
                                    dialogo.dismiss();

                                    zona.setCodigo(etCodigo.getText().toString());
                                    zona.setDescripcion(etDescripcion.getText().toString());
                                    zona.setDireccion(etDireccion.getText().toString());
                                    zona.setTipo(trataTipo(spTipo.getSelectedItem().toString()));


                                    for(int cont=0; cont<listaPuntos.size();cont++){

                                        posiciones+="posiciones["+cont+"].latitud="+(listaPuntos.get(cont).latitude+"").replace(".",",")+"&";
                                        posiciones+="posiciones["+cont+"].longitud="+(listaPuntos.get(cont).longitude+"").replace(".",",")+"&";
                                    }

                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(!edicion) {
                                                    url = new URL("http://" + LoginActivity.urlFinalLocal + "/altaZonas.action?zona.nombre=" + zona.getCodigo().replace(" ", "%20") + "&zona.descripcion=" +
                                                            zona.getDescripcion().replace(" ", "%20") + "&zona.tipoZona=" + zona.getTipo() + "&zona.direccion=" + zona.getDireccion().replace(" ", "%20") + "&" + posiciones);
                                                    int x = 0;
                                                }else{
                                                    url = new URL("http://" + LoginActivity.urlFinalLocal + "/actualizarZonas.action?zona.nombre=" + zona.getCodigo().replace(" ", "%20") + "&zona.descripcion=" +
                                                            zona.getDescripcion().replace(" ", "%20") + "&zona.tipoZona=" + zona.getTipo() + "&zona.direccion=" + zona.getDireccion().replace(" ", "%20") + "&" + posiciones);

                                                }
                                                connection = url.openConnection();
                                                connection.getContent();

                                                Intent intent = new Intent(context, ZonasActivity.class);
                                                context.startActivity(intent);

                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    if(!edicion) {
                                        Toast.makeText(context, "Ha creado la zona " + zona.getCodigo(), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "Ha guardado la zona editada " + zona.getCodigo(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Guardar Zona");
                titulo.show();
            }
        });

        dialogo.show();
    }

    public String trataTipo(String tipo){
        String tipoBueno = "";
        switch (tipo){
            case "Planta Hormigón":
                tipoBueno = "1";
                break;
            case "Obra Cliente":
                tipoBueno = "2";
                break;
            case "Oficina Central":
                tipoBueno = "3";
                break;
            case "Zona 4":
                tipoBueno = "4";
                break;
            case "Talleres":
                tipoBueno = "99";
                break;
        }

        return tipoBueno;
    }
}
