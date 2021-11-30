package com.example.mvvmreal.presentacion.recyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmreal.R;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.databinding.ActivityMainBinding;
import com.example.mvvmreal.presentacion.recyclerView.viewModel.ListadoFacturasViewModel;
import com.example.mvvmreal.util.Constantes;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FacturaAdapter.OnFacturaListener {

    private ActivityMainBinding binding;
    private ListadoFacturasViewModel facturasViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bind();
    }

    private void opcionesLoading(String opcion) {
        if (opcion.equals(Constantes.MostrarLoader)) {
            binding.contenido.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else if(opcion.equals(Constantes.OcultarLoader)){
            binding.contenido.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }


    //Inicialización de Toolbar
    private void bind() {
        facturasViewModel = new ListadoFacturasViewModel(getApplicationContext());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        //Observador lista de facturas
        facturasViewModel.facturas.observe(this, facturas -> {
            initRecyclerView(facturasViewModel.facturas.getValue());
            facturasViewModel.getMaxImporte();
        });


        //Volver
        binding.toolbar.setNavigationOnClickListener(onFiltrosIconClickListener -> finish());
    }


    //Inicialización del menu del toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        return super.onCreateOptionsMenu(menu);
    }


    //Listener de opciones del menu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(item);
        if (item.getItemId() == R.id.icono_filtro) {
            int LAUNCH_SECOND_ACTIVITY = 1;
            Intent i = new Intent(this, FiltradoActivity.class);
            i.putExtra(Constantes.WrapperMain, facturasViewModel.getWrapperParsedToJson());
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void initRecyclerView(List<Factura> f3) {
        RecyclerView recyclerView = findViewById(R.id.recycler_facturas);
        FacturaAdapter adapter = new FacturaAdapter(f3, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL)
        );

        opcionesLoading(Constantes.OcultarLoader);
        adapter.notifyDataSetChanged();

    }

    //Lógica para generar y mostrar Dialog en View
    public void mostrarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titutloDialogFactura);
        builder.setMessage(R.string.cuerpoDialogFactura);
        builder.setPositiveButton(R.string.neutralButtonDialogFactura, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Bindeo a cada factura
    @Override
    public void onFacturaClick() {
        mostrarDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 1) {
            assert data != null;
            opcionesLoading(Constantes.MostrarLoader);
            this.facturasViewModel.setWrapperJson(data.getStringExtra(Constantes.WrapperFiltro));
            this.facturasViewModel.getFacturas();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}