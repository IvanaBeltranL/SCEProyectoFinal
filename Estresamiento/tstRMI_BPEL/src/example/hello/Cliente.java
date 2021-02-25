/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//================================================================
// Código del Cliente:
//================================================================


/*
Clase main para estresamiento del WS Almacen. 

*/
package example.hello;
import static java.lang.Math.max;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import wsAlmacen.Product;
public class Cliente 
{
    
    public static void main(String[] args) 
    {
       long lngQuienSoy;
       long sumDeltaT, sumDeltaT2, dtMin = 0,dtMax = 0;
       long lngCuantosMilisFaltan;
       String strCadRes="";
       long i,n,t0,t1,dt;
       int prueba; //Transacción a estresar (tipo de prueba)
       int aleatorio; //Decide si se hace una prueba aleatoria
       String strP= "";
       
       String host = (args.length < 1) ? null : args[0];
       n = (args.length >= 2) ? Long.parseLong(args[1]):20;
        try 
        {
             Registry registry = LocateRegistry.getRegistry(host);
             IServDisparo servDisparo = (IServDisparo) registry.lookup("ServidorDeDisparo");
             lngQuienSoy = servDisparo.quienSoy();
             lngCuantosMilisFaltan = servDisparo.deltaTEnMilis();
             System.out.println("Cliente " + lngQuienSoy + " faltan " + lngCuantosMilisFaltan  + " milisegundos");
             sumDeltaT  = 0;
             sumDeltaT2 = 0;
             Thread.currentThread().sleep(lngCuantosMilisFaltan);
             
             
            wsbpel.TienditaWSDLService service = new wsbpel.TienditaWSDLService();
            wsbpel.TienditaWSDLPortType port = service.getTienditaWSDLPort();

            strP="Proceso general";
            
            String isbns[] = 
            {"8507864512910",
             "8507864512911",
             "8507864512922",
             "8507864512913",
             "8507864512924"
            };
            
            int unidades, cliente;
            for (i = 0; i < n; i++) {
                
                int nIsbm= (int)(Math.ceil(Math.random()*4));
                unidades= (int)(Math.ceil(Math.random()*5));
                cliente=(int)lngQuienSoy;   
                t0 = System.currentTimeMillis();
                  /*
                  =====================================================
                  Código para ejecución de la transacción particular
                  ====================================================
                  */
                strCadRes=tienditaWSDLOperation(isbns[nIsbm],unidades,cliente,port);
                  /*
                  =====================================================
                  Código para ejecución de la transacción particular
                  ====================================================
                  */
                t1 = System.currentTimeMillis();
                dt = t1 - t0;
                sumDeltaT  += dt;
                sumDeltaT2 += dt * dt;
                if( i == 0 )
                {
                    dtMin = dt;
                    dtMax = dt;
                }
                else
                {
                    if( dt < dtMin ) dtMin = dt;
                    if( dt > dtMax ) dtMax = dt;
                }
                System.out.println("Clte " + lngQuienSoy );
                System.out.println("Transacción: " + strP + " \n " + strCadRes);
                }
                System.out.println("Tiempo total: " + sumDeltaT );
              servDisparo.acumula(sumDeltaT, sumDeltaT2, n, dtMax, dtMin);
            }
          catch (Exception e)
          {
              System.err.println("Client exception: " + e.toString());
               e.printStackTrace();
           }
    }    

    private static String tienditaWSDLOperation(java.lang.String isbn, int unidades, int idCliente,wsbpel.TienditaWSDLPortType port ) {
        return port.tienditaWSDLOperation(isbn, unidades, idCliente);
    }
    
    
}
//================================================================

