/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imper;

/**
 *
 * @author Lautaro
 */
public class Producto {
    private String codigoImp;
    private String codigoOrig;
    private double precio;
    private String marca;
    private String nombre;
    private String rubro;
    public Producto() {
    }

    public Producto(String codigoImp, String codigoOrig, double precio, String marca, String nombre, String rubro) {
        this.codigoImp = codigoImp;
        this.codigoOrig = codigoOrig;
        this.precio = precio;
        this.marca = marca;
        this.nombre = nombre;
        this.rubro = rubro;
    }

    public String getCodigoImp() {
        return codigoImp;
    }

    public void setCodigoImp(String codigoImp) {
        this.codigoImp = codigoImp;
    }

    public String getCodigoOrig() {
        return codigoOrig;
    }

    public void setCodigoOrig(String codigoOrig) {
        this.codigoOrig = codigoOrig;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }
    
}