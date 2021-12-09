package com.example.mvvmreal.presentacion.listado.recyclerView;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmreal.data.model.FacturaVO;
import com.example.mvvmreal.util.Constantes;
import com.example.mvvmreal.util.Util;
import com.example.mvvmreal.databinding.RowLayoutBinding;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class FacturaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View view;
    RowLayoutBinding binding;
    StringBuilder stringBuilder= new StringBuilder();
    FacturaAdapter.OnFacturaListener onFacturaListener;
    DateTimeFormatter dia = DateTimeFormat.forPattern(Constantes.DiaPattern);
    DateTimeFormatter mes = DateTimeFormat.forPattern(Constantes.TresPrimerasLetrasMesPattern);
    DateTimeFormatter anyo = DateTimeFormat.forPattern(Constantes.AnyoPattern);

    public FacturaViewHolder(@NonNull View itemView, FacturaAdapter.OnFacturaListener facturaClickListener) {
        super(itemView);
        this.view=itemView;
        this.binding=  RowLayoutBinding.bind(view);
        this.onFacturaListener=facturaClickListener;
    }

    public void bind(FacturaVO factura) {
        Locale local = new Locale(Constantes.Idioma, Constantes.Pais);
        Locale.setDefault(local);
        DateTime fechaFormateada = DateTimeFormat.forPattern(Constantes.DefaultPatternFecha).parseDateTime(factura.fecha);

        binding.estadoFactura.setText(factura.descEstado);


        if(factura.descEstado.equals(Constantes.Opcion1)){
            binding.estadoFactura.setVisibility(View.GONE);
        }

        binding.fecha.setText(Util.concatenarConEspacios(dia.print(fechaFormateada), Util.primeraLetraToUpperCase(mes.print(fechaFormateada)), anyo.print(fechaFormateada)));

        binding.importeFactura.setText(stringBuilder.append(factura.importeOrdenacion).append(" €"));
        binding.factura.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        onFacturaListener.onFacturaClick();
    }
}


