package com.johnchaves.consultor;

public class VencimientoItem {

    public String fv_tipodoc, fv_numvisa, fv_numitem, fv_saldo, fv_fechaven, fv_bodega;

    public VencimientoItem(String fv_tipodoc, String fv_numvisa, String fv_numitem,
                      String fv_saldo, String fv_fechaven, String fv_bodega)
    {
        this.fv_tipodoc = fv_tipodoc;
        this.fv_numvisa = fv_numvisa;
        this.fv_numitem = fv_numitem;
        this.fv_saldo   = fv_saldo;
        this.fv_fechaven = fv_fechaven;
        this.fv_bodega   = fv_bodega;
    }

    public String getFv_tipodoc() {
        return fv_tipodoc;
    }

    public String getFv_numvisa() {
        return fv_numvisa;
    }

    public String getFv_numitem(){
        return fv_numitem;
    }

    public String getFv_saldo(){
        return fv_saldo;
    }

    public String getFv_fechaven(){
        return fv_fechaven;
    }

    public String getFv_bodega()   {
        return fv_bodega;
    }
}
