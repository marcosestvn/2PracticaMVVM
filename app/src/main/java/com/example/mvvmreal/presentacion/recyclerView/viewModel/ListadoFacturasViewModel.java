package com.example.mvvmreal.presentacion.recyclerView.viewModel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.example.mvvmreal.util.Constantes;
import com.example.mvvmreal.domain.handlers.DefaultUseCaseCallbackHandler;
import com.example.mvvmreal.data.model.WrapperFiltro;
import com.example.mvvmreal.data.repository.FacturaRepository;
import com.example.mvvmreal.data.model.Factura;
import com.example.mvvmreal.data.model.RespuestaFactura;
import com.example.mvvmreal.data.repository.FacturaRepositoryNoInternet;
import com.example.mvvmreal.data.database.AppDatabase;
import com.example.mvvmreal.data.database.FacturaDao;
import com.example.mvvmreal.domain.useCase.GetFacturasUseCase;
import com.example.mvvmreal.domain.executor.UseCaseCallBack;
import com.example.mvvmreal.domain.interfaces.FacturaRepositoryInterface;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class ListadoFacturasViewModel extends ViewModel {
    private final Context context;
    public MutableLiveData<List<Factura>> facturas;
    private final Gson converter = new Gson();
    private WrapperFiltro wrapperMain;
    public FacturaRepository repositorio;

    public ListadoFacturasViewModel(Context context) {
        this.context=context;
        this.repositorio = new FacturaRepository();
        this.wrapperMain = new WrapperFiltro();

        getFacturaRepository();

        setFacturas(new MutableLiveData<List<Factura>>());

        getFacturas();

    }

    public void obtenerFacturas() throws IOException {

    }

    public MutableLiveData<List<Factura>> getFacturasViewModel() {
        return this.facturas;
    }

    public void getFacturas() {


        GetFacturasUseCase caso;
        caso = new GetFacturasUseCase(new DefaultUseCaseCallbackHandler(), getFacturaRepository());


        caso.customize(new UseCaseCallBack<RespuestaFactura>() {

            @Override
            public void onResult(RespuestaFactura result) {

                result.facturas.removeIf(factura -> !filtrar(factura));
                facturas.postValue(result.facturas);


                System.out.println("HOLA");
                System.out.println(result.toString());
            }
        });


            Executors.newCachedThreadPool().execute(caso);


    }

    private void setFacturas(MutableLiveData<List<Factura>> facturas) {
        this.facturas = facturas;
    }

    private FacturaRepositoryInterface getFacturaRepository() {

        if(isConnected()){
            return new FacturaRepository();
        }else{

            Factura f1= new Factura("Pagada",21.0,"18/04/1998");
            List<Factura> facturas = new ArrayList<>();
            facturas.add(f1);
            RespuestaFactura r = new RespuestaFactura(facturas,"1");

            AppDatabase db= AppDatabase.getINSTANCE(context);
            FacturaDao dao =db.facturaDao();
            dao.insert(f1);

            return new FacturaRepositoryNoInternet(dao);
        }
        //TODO COMPROBAR SI HAY RED -> CREAR REPO INTERNET O REPO BD


    }


    public String getWrapperParsedToJson() {
        if (wrapperMain != null) {
            return this.converter.toJson(wrapperMain);
        }
        return null;
    }

    public void getMaxImporte() {
        for (Factura f : facturas.getValue()
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
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


}
