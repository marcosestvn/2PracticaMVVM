package com.example.mvvmreal.presentacion.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmreal.R;
import com.example.mvvmreal.data.model.Factura;

import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {



    public interface OnFacturaListener {
        void onFacturaClick();
    }

    private final OnFacturaListener facturaClickListener;
    Context context;
    private final List<Factura> facturas;

    public FacturaAdapter(List<Factura> facturas,Context context, OnFacturaListener facturaClickListener) {
        this.facturas=facturas;
        this.context = context;
        this.facturaClickListener=facturaClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        return new FacturaViewHolder(v, this.facturaClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        FacturaViewHolder viewHolder= (FacturaViewHolder) holder;

        viewHolder.bind(facturas.get(position));
    }


    @Override
    public int getItemCount() {
        return facturas.size();
    }



}

