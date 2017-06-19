package imper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import imper.gui.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class Imper {

    public static void main(String args[]) throws IOException {

        //saveUrl();
        //decompress();
        //clearDB();
        //loadDB();
        initializeGui();

    }

    public static void initializeGui() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Dracula".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImperMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImperMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImperMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImperMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        ImperMain frm = new ImperMain();
        frm.setVisible(true);
    }

    public static void clearDB() throws IOException {
        Conector con = new Conector();
        con.dropAndCreateDB();
    }

    public static void loadDB() throws IOException {
        File setup = new File("Update/setup.exe");
        setup.delete();
        Conector con = new Conector();
        String line;
        ArrayList<String> arrayLines = new ArrayList<String>();

        /**
         * A continuacion agregue un chequeo en el archivo marcas.txt que
         * contiene las marcas usadas en el programam original, supongo que
         * imperdiel toma sus marcas de ahi a pesar de incluir el doble de
         * archivos en la carpeta BaseDatos.
         */
        ArrayList<String> marrayLines = new ArrayList<String>();
        try (InputStream mis = new FileInputStream("Update/BaseDatos/marca.txt");
                InputStreamReader isr = new InputStreamReader(mis, Charset.forName("cp1252"));
                BufferedReader mbr = new BufferedReader(isr);) {
            while ((line = mbr.readLine()) != null) {
                marrayLines.add(line);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Imper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Imper.class.getName()).log(Level.SEVERE, null, ex);
        }

        File folder = new File("Update/BaseDatos/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (marrayLines.contains(file.getName())) {

                arrayLines.removeAll(arrayLines);
                if (file.isFile() && !file.getName().endsWith(".txt")) {
                    try (
                            InputStream fis = new FileInputStream(file.getPath());
                            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("cp1252"));
                            BufferedReader br = new BufferedReader(isr);) {

                        while ((line = br.readLine()) != null) {
                            arrayLines.add(line);

                        }

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Imper.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Imper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    for (int j = 0; j < arrayLines.size(); j = j + 5) {
                        Producto producto = new Producto();
                        String aux[];

                        String d = arrayLines.get(j + 3);
                        d = d.replace(",", ".");
                        d = d.replaceAll("\\s", "");
                        aux = arrayLines.get(j + 4).split("/");
                        String pre = " " + aux[0];
                        producto.setCodigoImp(arrayLines.get(j));
                        producto.setCodigoOrig(arrayLines.get(j + 2));
                        producto.setPrecio(Double.parseDouble(d));
                        producto.setPrecioIva(Double.parseDouble(d));
                        producto.setPrecioCosto(Double.parseDouble(d));
                        producto.setMarca(aux[0]);
                        producto.setRubro(aux[1]);
                        producto.setNombre(arrayLines.get(j + 1).replace(pre, ""));
                        con.saveProducto(producto);

                    }

                }
            }
            file.delete();
        }
        con.commit();

    }

    public static void saveUrl()
            throws MalformedURLException, IOException {

        String urlString = "http://imperdielriocuarto.com.ar/listaprecios/ListadePrecios.exe";
        String filename = "Update/setup.exe";
        File dir = new File("Update");
        dir.mkdir();
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }

    }

    public static void decompress() {
        try {
            //System.out.println("Inicio del Programa");
            Process p = Runtime.getRuntime().exec("run.bat");
            boolean no_exit = true;
            while (no_exit) {
                try {
                    p.exitValue();
                    no_exit = false;
                } catch (IllegalThreadStateException exception) {
                    no_exit = true;
                    //System.out.println("El programa aun no finaliza");
                }
            }
            System.out.println("El programa finalizo");


            /*Inicio de las acciones que siguen a la finalizacion del exe*/
        } catch (IOException e) {
        }
    }

}
