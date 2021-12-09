package com.example.mvvmreal.presentacion.filtro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmreal.R;
import com.example.mvvmreal.databinding.ActivityFiltradoBinding;
import com.example.mvvmreal.util.Constantes;
import com.example.mvvmreal.util.Util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Objects;

public class FiltradoActivity extends AppCompatActivity {

    private FiltradoViewModel filtradoViewModel;
    private ActivityFiltradoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarPantalla();
        recogerIntentYPrepararWrapper();
        bind();
    }

    private void inicializarPantalla() {
        binding = ActivityFiltradoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void recogerIntentYPrepararWrapper() {
        Intent intent = getIntent();
        inicializarViewModel(intent.getStringExtra(Constantes.WrapperMain));
    }

    private void inicializarViewModel(String wrapper) {
        filtradoViewModel = new ViewModelProvider(this).get(FiltradoViewModel.class);
        this.filtradoViewModel.setWrapperFiltro(wrapper);
        filtradoViewModel.wrapper.observe(this, Observer -> pintarView());
    }


    private void pintarView() {
        //SeekBar o Slider
        binding.maxSeekbar.setText(Util.concatenarConEspacios((Objects.requireNonNull(filtradoViewModel.wrapper.getValue()).getImporteMaximo()).toString(), Constantes.Divisa));

        binding.sliderImporte.setProgress(filtradoViewModel.wrapper.getValue().getImporteFiltro());

        binding.valorSlider.setText(Util.concatenarConEspacios(filtradoViewModel.wrapper.getValue().getImporteFiltro().toString(), Constantes.Divisa));

        binding.sliderImporte.setMax(filtradoViewModel.wrapper.getValue().getImporteMaximo());

        pintarFechas();

        pintarCheckBox(filtradoViewModel.wrapper.getValue().getEstadosFiltro());

    }
    private void pintarFechas(){
        if (!Objects.requireNonNull(filtradoViewModel.wrapper.getValue()).getFecha_desde().isEmpty()) {
            binding.fechaDesde.setText(filtradoViewModel.wrapper.getValue().getFecha_desde());
        } else {
            binding.fechaDesde.setText(getString(R.string.dia_mes_año));
        }

        if (!filtradoViewModel.wrapper.getValue().getFecha_hasta().isEmpty()) {
            binding.fechaHasta.setText(filtradoViewModel.wrapper.getValue().getFecha_hasta());

        } else {
            binding.fechaHasta.setText(getString(R.string.dia_mes_año));

        }
    }

    //Mñétodo para pintar los chekbox en checked o no checked
    private void pintarCheckBox(List<String> estados) {

        binding.idOpcion1.setChecked(estados.contains(Constantes.Opcion1));

        binding.idOpcion2.setChecked(estados.contains(Constantes.Opcion2));

        binding.idOpcion3.setChecked(estados.contains(Constantes.Opcion3));

        binding.idOpcion4.setChecked(estados.contains(Constantes.Opcion4));

        binding.idOpcion5.setChecked(estados.contains(Constantes.Opcion5));
    }

    private void bind() {

        //TextView fecha desde
        binding.fechaDesde.setOnClickListener(listener -> mostrarDialog(R.id.fechaDesde));


        //TextView fecha hasta
        binding.fechaHasta.setOnClickListener(listener -> mostrarDialog(R.id.fechaHasta));

        //Cerrar Filtros
        binding.botonCerrarFiltro.setOnClickListener(listener -> finish());

        //Eliminar Filtros
        binding.botonEliminarFiltros.setOnClickListener(listener -> {
            filtradoViewModel.limpiarFiltros();
            pintarView();
        });

        //Aceptar filtros
        binding.botonFiltrar.setOnClickListener(listener -> {
            comprobarCheckBox();
            comprobarImporteSeekBar();
            Intent intent = new Intent();
            intent.putExtra(Constantes.WrapperFiltro, filtradoViewModel.getWrapperParseado());
            setResult(1, intent);
            finish();
        });

        //Slider importe
        binding.sliderImporte.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.valorSlider.setText(Util.concatenar(String.valueOf(progress), Constantes.Divisa));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //Método de comprobación del valor del SeekBar
    private void comprobarImporteSeekBar() {
        Objects.requireNonNull(filtradoViewModel.wrapper.getValue()).setImporteFiltro((Math.max(binding.sliderImporte.getProgress(), 0)));
    }

    //Método de comprobación de Checkbox
    private void comprobarCheckBox() {

        comprobarCheckboxIndividual(binding.idOpcion1, Constantes.Opcion1);
        comprobarCheckboxIndividual(binding.idOpcion2, Constantes.Opcion2);
        comprobarCheckboxIndividual(binding.idOpcion3, Constantes.Opcion3);
        comprobarCheckboxIndividual(binding.idOpcion4, Constantes.Opcion4);
        comprobarCheckboxIndividual(binding.idOpcion5, Constantes.Opcion5);

    }

    private void comprobarCheckboxIndividual(CheckBox checkbox, String opcion) {
        if (checkbox.isChecked()) {
            filtradoViewModel.anyadirEstadoFiltro(opcion);
        } else {
            filtradoViewModel.eliminarEstadoFiltro(opcion);
        }
    }

    private void mostrarDialog(int id) {
        DateFragment datePicker = DateFragment.newInstance((datepicker2, year, month, day) -> onDateSelected(day, month, year, id));
        String fechaDesdeTemp = filtradoViewModel.getFecha(Constantes.Clave_1);
        String fechaHastaTemp = filtradoViewModel.getFecha(Constantes.Clave_2);

        if (id == binding.fechaDesde.getId()) {


            if (!(fechaHastaTemp.isEmpty())) {
                datePicker.setMaxDate2(fechaHastaTemp);
            }

            if (!fechaDesdeTemp.isEmpty()) {
                datePicker.setCurrentDate(fechaDesdeTemp);
            }
        }

        if (id == binding.fechaHasta.getId()) {

            if (!(fechaDesdeTemp.isEmpty())) {
                datePicker.setMinDate2(fechaDesdeTemp);
            }

            if (!fechaHastaTemp.isEmpty()) {
                datePicker.setCurrentDate(fechaHastaTemp);
            }
        }

        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    //Método listener de fecha seleccionada en DatePickerDialog
    private void onDateSelected(Integer day, Integer month, Integer year, Integer id) {

        DateTimeFormatter ff = DateTimeFormat.forPattern(Constantes.DefaultPatternFecha);


        month++;

        DateTime fecha = DateTimeFormat.forPattern(Constantes.DefaultPatternFecha)
                .parseDateTime(Util.concatenar((day.toString()), "/", ((month).toString()), "/", (year.toString())));


        if (id == R.id.fechaHasta) {
            Objects.requireNonNull(filtradoViewModel.wrapper.getValue()).setFecha_hasta(ff.print(fecha));
        }

        if (id == R.id.fechaDesde) {
            Objects.requireNonNull(filtradoViewModel.wrapper.getValue()).setFecha_desde(ff.print(fecha));
        }
        pintarFechas();
    }
}