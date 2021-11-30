package com.example.mvvmreal.presentacion.recyclerView.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.data.model.WrapperFiltro;
import com.example.mvvmreal.data.repository.FacturaRepository;
import com.example.mvvmreal.data.repository.FacturaRepositoryNoInternet;
import com.example.mvvmreal.domain.handlers.DefaultUseCaseCallbackHandler;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;
import com.example.mvvmreal.domain.useCase.GetFacturasUseCase;
import com.example.mvvmreal.util.Constantes;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;


public class ListadoFacturasViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    public MutableLiveData<List<Factura>> facturas;
    private final Gson converter ;
    private WrapperFiltro wrapperMain;
    private AppDatabase db ;

    public ListadoFacturasViewModel(Context context) {
        this.context = context;
        this.wrapperMain = new WrapperFiltro();
        this.converter= new Gson();
        this.db=AppDatabase.getINSTANCE(context);

        getFacturaRepository();

        setFacturas(new MutableLiveData<>());

        getFacturas();

    }

    public void getFacturas() {


        GetFacturasUseCase caso;
        caso = new GetFacturasUseCase(new DefaultUseCaseCallbackHandler(), getFacturaRepository());


        caso.customize(result -> {

            result.getFacturas().removeIf(factura -> !filtrar(factura));
            facturas.postValue(result.getFacturas());

        });


        Executors.newCachedThreadPool().execute(caso);


    }

    private void setFacturas(MutableLiveData<List<Factura>> facturas) {
        this.facturas = facturas;
    }

    private FacturaRepositoryInterface getFacturaRepository() {

        if (isConnected()) {
            return new FacturaRepository(db);
        } else {
            return new FacturaRepositoryNoInternet(db);
        }


    }

    public String getWrapperParsedToJson() {
        if (wrapperMain != null) {
            return this.converter.toJson(wrapperMain);
        }
        return null;
    }

    public void getMaxImporte() {
        for (Factura f : Objects.requireNonNull(facturas.getValue())
        ) {
            if (this.wrapperMain.getImporteMaximo() < f.importeOrdenacion)
                this.wrapperMain.setImporteMaximo((int) f.importeOrdenacion + 1);
        }
    }

    public void setWrapperJson(String wrapperJson) {
        this.wrapperMain = converter.fromJson(wrapperJson, WrapperFiltro.class);
    }

    //Se realizan las funciones de filtros por cada campo a evaluar y se comparan las flags
    //si alguna flag es falsa eliminamos la factura de nuestra lista
    public Boolean filtrar(Factura facturaEvaluada) {

        Boolean flagImporte = filtrarPorImporte((int) facturaEvaluada.importeOrdenacion);
        Boolean flagEstados = filtrarPorEstados(facturaEvaluada.descEstado);
        Boolean flagFecha = filtrarFechas(facturaEvaluada.fecha);
        return (flagImporte && flagEstados && flagFecha);

    }

    //Se evaluan los tres posibles casos, solo se tenga la fecha desde, la fecha hasta o ambas.
    public Boolean filtrarFechas(String fecha) {

        String fechaDesdeWrapper = wrapperMain.getFecha_desde();
        String fechaHastaWrapper = wrapperMain.getFecha_hasta();
        DateTimeFormatter format = DateTimeFormat.forPattern(Constantes.DefaultPatternFecha);
        DateTime fechaFormateada = format.parseDateTime(fecha);

        //Ambas fechas
        if (!fechaDesdeWrapper.isEmpty() && !fechaHastaWrapper.isEmpty()) {
            DateTime fechaDesdeFormateada = format.parseDateTime(fechaDesdeWrapper);
            DateTime fechaHastaFormateada = format.parseDateTime(fechaHastaWrapper);
            return fechaFormateada.isAfter(fechaDesdeFormateada) && fechaFormateada.isBefore(fechaHastaFormateada);

            //Fecha Desde
        } else if (!fechaDesdeWrapper.isEmpty()) {

            DateTime fechaDesdeFormateada = format.parseDateTime(fechaDesdeWrapper);
            return fechaFormateada.isAfter(fechaDesdeFormateada);

            //Fecha Hasta
        } else if (!fechaHastaWrapper.isEmpty()) {
            DateTime fechaHastaFormateada = format.parseDateTime(fechaHastaWrapper);
            return fechaFormateada.isBefore(fechaHastaFormateada);
        }

        return true;
    }

    //Se evalua si el estado de la factura se encuentra en los estados seleccionados del filtro
    public Boolean filtrarPorEstados(String estadoEvaluado) {
        if (wrapperMain.getEstadosFiltro().isEmpty()) {
            return true;
        }
        return (wrapperMain.getEstadosFiltro()
                .contains(estadoEvaluado));
    }

    //Se evalua si el importe de la factura es inferior al importe seleccionado
    public Boolean filtrarPorImporte(int importeOrdenacionFacturaEvaluada) {
        if (wrapperMain.getImporteFiltro() > 0) {
            return this.wrapperMain.getImporteFiltro() > importeOrdenacionFacturaEvaluada;
        }

        return true;
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


}
