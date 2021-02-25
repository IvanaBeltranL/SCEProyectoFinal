/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proy_final_pojoprueba;

/**
 *
 * @author ruben
 */
public class Proy_Final_POJOprueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int resComprobar;
        resComprobar=comprobarStock("8507864512910",1);
        System.out.println(resComprobar);
        resComprobar=facturar("8507864512910",1,1);
        System.out.println(resComprobar);
        
    }

    private static int facturar(java.lang.String idProducto, int idCliente, int cantidad) {
        wspasarelapago.PasarelaPago_Service service = new wspasarelapago.PasarelaPago_Service();
        wspasarelapago.PasarelaPago port = service.getPasarelaPagoPort();
        return port.facturar(idProducto, idCliente, cantidad);
    }

    private static int comprobarStock(java.lang.String isbn, int unidades) {
        wsalmacen.Almacen_Service service = new wsalmacen.Almacen_Service();
        wsalmacen.Almacen port = service.getAlmacenPort();
        return port.comprobarStock(isbn, unidades);
    }
    
}
