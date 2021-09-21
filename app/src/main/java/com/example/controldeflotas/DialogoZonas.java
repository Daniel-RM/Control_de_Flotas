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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


//Clase que maneja el cuadro de diálogo que recoge los datos necesarios para crear una zona o editarla y guarda los datos en el servidor
public class DialogoZonas {

    final String PLANTA = "Planta Hormigón";
    final String OBRA = "Obra Cliente";
    final String OFICINA = "Oficina Central";
    final String ZONA = "Zona 4";
    final String TALLERES = "Talleres";

    String posiciones="";

    URL url = null;
    URLConnection connection = null;

    public DialogoZonas(Context context, Zona zona, List<LatLng> listaPuntos, boolean edicion){
        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        //dialogo.setContentView(R.layout.dialogo_zonas);
        dialogo.setContentView(R.layout.dialogo_zonas_prueba);

//        Button btnCancelar, btnGuardar;
//        EditText etCodigo, etDescripcion, etDireccion;
//        Spinner spTipo;

        Button btnSalir, btnNueva;
        EditText edtCodigo, edtDescripcion, edtDireccion;
        Spinner spiTipo;

//        btnCancelar = dialogo.findViewById(R.id.btnCancelar);
//        btnGuardar = dialogo.findViewById(R.id.btnGuardar);
//
//        etCodigo = dialogo.findViewById(R.id.etCodigo);
//        etDescripcion = dialogo.findViewById(R.id.etDescripcion);
//        etDireccion = dialogo.findViewById(R.id.etDireccion);
//        spTipo = dialogo.findViewById(R.id.spTipo);

        btnSalir = dialogo.findViewById(R.id.btnSalir);
        btnNueva = dialogo.findViewById(R.id.btnNueva);

        edtCodigo = dialogo.findViewById(R.id.edtCodigo);
        edtDescripcion = dialogo.findViewById(R.id.edtDescripcion);
        edtDireccion = dialogo.findViewById(R.id.edtDireccion);
        spiTipo = dialogo.findViewById(R.id.spiTipo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.tiposZonas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spTipo.setAdapter(adapter);
        spiTipo.setAdapter(adapter);

        if(zona.getCodigo()!=null){
            edtCodigo.setText(zona.getCodigo());
            edtDescripcion.setText(zona.getDescripcion());
            edtDireccion.setText(zona.getDireccion());
            switch (zona.getTipo()){
                case PLANTA:
                    spiTipo.setSelection(0);
                    break;
                case OBRA:
                    spiTipo.setSelection(1);
                    break;
                case OFICINA:
                    spiTipo.setSelection(2);
                    break;
                case ZONA:
                    spiTipo.setSelection(3);
                    break;
                case TALLERES:
                    spiTipo.setSelection(4);
                    break;
            }
        }

        if(zona.getDireccion()!=null){
            edtDireccion.setText(zona.getDireccion());
        }

        //Botón que esconde el cuadro de diálogo
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        //Botón que guarda la zona creada o editada
        btnNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                alerta.setMessage("Guardar los datos de " + edtCodigo.getText().toString() + "?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.cancel();
                            }
                        })
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(edtCodigo.getText().toString().isEmpty() || edtDescripcion.getText().toString().isEmpty() || edtDireccion.getText().toString().isEmpty()){
                                    Toast.makeText(context, "Por favor, no deje ningún dato vacío", Toast.LENGTH_SHORT).show();
                                }else {
                                    dialogo.dismiss();

                                    zona.setCodigo(edtCodigo.getText().toString());
                                    zona.setDescripcion(edtDescripcion.getText().toString());
                                    zona.setDireccion(edtDireccion.getText().toString());
                                    zona.setTipo(trataTipo(spiTipo.getSelectedItem().toString()));


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

    //Método que convierte el tipo de zona, en un código numérico que maneja el programa (no lo hace con el texto)
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
