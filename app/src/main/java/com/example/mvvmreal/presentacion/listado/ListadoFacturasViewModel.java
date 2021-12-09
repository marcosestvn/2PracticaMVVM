package com.example.mvvmreal.presentacion.listado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.model.FacturaVO;
import com.example.mvvmreal.presentacion.wrapper.WrapperFiltro;
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
    public MutableLiveData<List<FacturaVO>> facturas;
    private WrapperFiltro wrapperMain;

    public ListadoFacturasViewModel() {
        this.wrapperMain = new WrapperFiltro();
        this.facturas = new MutableLiveData<>();
    }

    public void getFacturas(Context context) {


        GetFacturasUseCase caso;
        caso = new GetFacturasUseCase(new DefaultUseCaseCallbackHandler(), getFacturaRepository(context));


        caso.customize(result -> {
            result.getFacturas().removeIf(facturaVO  -> !filtrar(facturaVO));
            facturas.postValue(result.getFacturas());

        });


        Executors.newCachedThreadPool().execute(caso);


    }

    private FacturaRepositoryInterface getFacturaRepository(Context context) {
        AppDatabase db=AppDatabase.getINSTANCE(context);

        if (isConnected(context)) {
            return new FacturaRepository(db);
        } else {
            return new FacturaRepositoryNoInternet(db);
        }


    }

    public String getWrapperParsedToJson() {
        if (wrapperMain != null) {
            return new Gson().toJson(wrapperMain);
        }
        return null;
    }

    public void getMaxImporte() {
        for (FacturaVO f : Objects.requireNonNull(facturas.getValue())
        ) {
            if (this.wrapperMain.getImporteMaximo() < f.importeOrdenacion)
                this.wrapperMain.setImporteMaximo((int) f.importeOrdenacion + 1);
        }
    }

    public void setWrapperJson(String wrapperJson) {
        this.wrapperMain = new Gson().fromJson(wrapperJson, WrapperFiltro.class);
    }

    //Se realizan las funciones de filtros por cada campo a evaluar y se comparan las flags
    //si alguna flag es falsa eliminamos la factura de nuestra lista
    public Boolean filtrar(FacturaVO facturaEvaluada) {

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

    public boolean isConnected(Context context) {
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
