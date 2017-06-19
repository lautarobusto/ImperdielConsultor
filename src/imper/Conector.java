package imper;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conector {

    //esta es la url de la base de datos
    String url = "BaseDatos.db";

    //objeto coneccion para poder empezar a abrir la coneccion
    Connection connect;

    //un prepared statement permite usar variables en la query que quiera hacer en la DB
    PreparedStatement insertSt, createSt;

    public Conector() {
        //  System.out.println("Conectando.....");
        try {

            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
            insertSt = connect.prepareStatement("insert into Productos (CodImperdiel, CodOrigen, Precio, PrecioIva, PrecioCosto, Nombre, Marca, Rubro) values (?,?,?,?,?,?,?,?)");
            if (connect != null) {
                //  System.out.println("Conectado");
            }
        } catch (SQLException ex) {

            System.out.println("No se ha podido conectar a la base de datos \n" + ex.getMessage());

        }
        //System.out.println("Opened database successfully");
    }

    /**
     * public void ConectorEsp() {
     *
     * // System.out.println("Conectando....."); try {
     *
     * connect = DriverManager.getConnection("jdbc:sqlite:" + url); insertSt =
     * connect.prepareStatement("insert into Productos (CodImperdiel, CodOrigen,
     * Precio, PrecioIva, PrecioCosto, Nombre, Marca, Rubro) values
     * (?,?,?,?,?,?,?,?)"); if (connect != null) { //
     * System.out.println("Conectado"); } } catch (SQLException ex) {
     *
     * System.out.println("No se ha podido conectar a la base de datos \n" +
     * ex.getMessage());
     *
     * }
     * //System.out.println("Opened database successfully"); }*
     */
    public void dropAndCreateDB() {
        try {
            PreparedStatement st = this.connect.prepareStatement("DROP TABLE IF EXISTS Productos");
            st.execute();
            st = connect.prepareStatement("CREATE TABLE `Productos` ( `Id` INTEGER, `CodImperdiel` TEXT, `CodOrigen` TEXT, `Precio` REAL, `PrecioIva` REAL, `PrecioCosto` REAL,`Nombre` TEXT, `Marca` TEXT, `Rubro` TEXT, PRIMARY KEY(`Id`) )");
            st.execute();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }

    /**
     * public void dropAndCreateDB() { try { PreparedStatement st =
     * this.connect.prepareStatement("DROP TABLE IF EXISTS Productos");
     * st.execute(); st = connect.prepareStatement("CREATE TABLE `Productos` (
     * `Id` INTEGER, `CodImperdiel` TEXT, `CodOrigen` TEXT, `Precio`
     * REAL,`Nombre` TEXT, `Marca` TEXT, `Rubro` TEXT, PRIMARY KEY(`Id`) )");
     * st.execute();
     *
     * } catch (SQLException ex) { System.out.println(ex.getMessage());
     *
     * }
     * }
     */
    /**
     * metodo para cerrar la conecion a la db
     */
    public void close() {
        try {
            connect.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveProducto(Producto x) {
        try {

            this.insertSt.setString(1, x.getCodigoImp());
            this.insertSt.setString(2, x.getCodigoOrig());
            this.insertSt.setDouble(3, x.getPrecio());
            this.insertSt.setDouble(4, x.getPrecioIva());
            this.insertSt.setDouble(5, x.getPrecioCosto());
            this.insertSt.setString(6, x.getNombre());
            this.insertSt.setString(7, x.getMarca());
            this.insertSt.setString(8, x.getRubro());
            this.insertSt.addBatch();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }

    /**
     * esto es muy importante para la velocidad de insercion, al setear el
     * autocomit a false y luego volverlo a true, me permite forzar la
     * escritura,sin esperar que se llene el batch. si no hago esto demora
     * muchicimo en cargar un solo libro a la db
     */
    public void commit() {
        try {
            connect.setAutoCommit(false);
            this.insertSt.executeBatch();
            connect.setAutoCommit(true);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }

    /*
    de momento el get result trae una consulta elavorada porque. la tabla se esta creando con [palabra]:[Libro] osea solo 2 valores
    pero para cumplir con el requisito de mostrar [cantidad de repeticiones][ palabra][cantidad de libros en la que aparece]
    realizo esta consulta que hace las comparaciones, una vez implementado el hashtable, se va a guardar en la tabla final con el formato que pide
    y la consulta va a ser mucho mas simple. ya que el hashtable va a hacer las operaciones de comparacio onTheFly.
     */
    public ResultSet getResult() {
        try {
            Statement stmt = connect.createStatement();
            /*
                        String str = "select sum(cantidad) as cantidadPalabras, palabra, count(nombreLibro) as CantidadLibros from(\n"
                    + "select   count (palabra) as cantidad, \n"
                    + "   palabra, \n"
                    + "   nombreLibro  \n"
                    + "from libreroTemporal \n"
                    + "group by palabra,nombreLibro\n"
                    + ")\n"
                    + "group by palabra\n"
                    + "order by palabra";
             */
            String str = "select * from Productos";
            ResultSet rst = stmt.executeQuery(str);
            return rst;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public ResultSet getProd(String n) {
        try {
            Statement stmt = connect.createStatement();
            String str = "select * from Productos where nombre=" + "'" + n + "'";
            ResultSet rst = stmt.executeQuery(str);
            return rst;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public ResultSet getMarca() {
        try {
            Statement stmt = connect.createStatement();
            String str = "select Marca from Productos group by Marca";
            ResultSet rst = stmt.executeQuery(str);
            return rst;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public ResultSet getProdM(String m) {
        try {
            Statement stmt = connect.createStatement();

            String str = "select nombre from Productos where marca=" + "'" + m + "'";
            ResultSet rst = stmt.executeQuery(str);
            return rst;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    //agregadp por busto pedro
    public void clearDBTable(String table) {
        try {
            PreparedStatement st = connect.prepareStatement("delete from " + table);
            st.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }
}
