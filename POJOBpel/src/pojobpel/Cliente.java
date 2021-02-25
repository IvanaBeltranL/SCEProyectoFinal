/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//================================================================
// Código del Cliente:
//================================================================
package pojobpel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long lngQuienSoy;
        long sumDeltaT, sumDeltaT2, dtMin = 0, dtMax = 0;
        long lngCuantosMilisFaltan;

        String host = (args.length < 1) ? null : args[0];
        long i, n, t0, t1, dt;
        String response;

/*
Este parámetro recibe la cantidad de solicitudes deseadas; a pesar de que si el 
programa se corrre desde el estresador nunca sucede que args[1] == null
debido a como esta creado el estresador, se deja por si se decide ejecutar 
fuera del estresador
*/
        if (args[1] != null) {
            n = Integer.parseInt(args[1]);
        } else {
            n = 200;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            IServDisparo servDisparo = (IServDisparo) registry.lookup("ServidorDeDisparo");
            lngQuienSoy = servDisparo.quienSoy();
            lngCuantosMilisFaltan = servDisparo.deltaTEnMilis();
            System.out.println("Cliente " + lngQuienSoy + " faltan " + lngCuantosMilisFaltan + " milisegundos");
            sumDeltaT = 0;
            sumDeltaT2 = 0;
            for (i = 0; i < n; i++) {
                t0 = System.currentTimeMillis();
                response = tienditaWSDLOperation("8507864512910", 6, 1);
                t1 = System.currentTimeMillis();
                dt = t1 - t0;
                sumDeltaT += dt;
                sumDeltaT2 += dt * dt;
                if (i == 0) {
                    dtMin = dt;
                    dtMax = dt;
                } else {
                    if (dt < dtMin) {
                        dtMin = dt;
                    }
                    if (dt > dtMax) {
                        dtMax = dt;
                    }
                }
                System.out.println("Clte " + lngQuienSoy + ": " + response);
            }
            servDisparo.acumula(sumDeltaT, sumDeltaT2, n, dtMax, dtMin);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private static String tienditaWSDLOperation(java.lang.String isbn, int unidades, int idCliente) {
        org.netbeans.j2ee.wsdl.tienditalibrosonline.descriptorbpel.tienditawsdl.TienditaWSDLService service = new org.netbeans.j2ee.wsdl.tienditalibrosonline.descriptorbpel.tienditawsdl.TienditaWSDLService();
        org.netbeans.j2ee.wsdl.tienditalibrosonline.descriptorbpel.tienditawsdl.TienditaWSDLPortType port = service.getTienditaWSDLPort();
        return port.tienditaWSDLOperation(isbn, unidades, idCliente);
    }

    
   

    

}
