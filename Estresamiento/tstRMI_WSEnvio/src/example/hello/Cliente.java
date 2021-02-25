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
import wsmensajeria.CustomerOrder;
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
       prueba = args.length>=3? Integer.parseInt(args[2]):1;
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
             
             int idPedido;
             

            strP="Prueba: ";
            
            
            wsmensajeria.EnvioPaquetes_Service service = new wsmensajeria.EnvioPaquetes_Service();
            wsmensajeria.EnvioPaquetes port = service.getEnvioPaquetesPort();
            String empresa="TuLibroOnline";
            int res;
            for (i = 0; i < n; i++) {
                switch(prueba){
                    case 1:
                        strP="Hacer envío";
                        idPedido=(int)Math.ceil(Math.random()*10);
                        t0 = System.currentTimeMillis();
                  /*
                  =====================================================
                  Código para ejecución de la transacción particular
                  ====================================================
                  */
                        res=enviarProducto(idPedido,empresa,port);
                  /*
                  =====================================================
                  Código para ejecución de la transacción particular
                  ====================================================
                  */
                        t1 = System.currentTimeMillis();
                        strCadRes+= res>0?"El pedido "+idPedido+" se enviará en "+res+" dias \n":"El pedido "+idPedido+" ya está en proceso de envío \n";
                        break;
                    case 2:
                        strP="Find: ";
                        idPedido=(int)Math.ceil(Math.random()*10);
                        t0 = System.currentTimeMillis();
                  /*
                  =====================================================
                  Código para ejecución de la transacción particular
                  ====================================================
                  */
                        wsmensajeria.CustomerOrder element = find(idPedido,port);
                  /*
                  =====================================================
                  Código para ejecución de la transacción particular
                  ====================================================
                  */
                        t1 = System.currentTimeMillis();
                        strCadRes=element.equals(new wsmensajeria.Customer())?"No existe: ":"ID "+element.getId()+":"+
                                "\t Cliente: "+element.getCustomerId().getId()+
                                "\t Producto: "+element.getProductId().getId()+
                                "\t Precio: "+element.getAmount()+
                                "\t Fecha: "+element.getDateCreated()+
                                "\t Empresa: "+element.getDeliverySystem()+
                                "\t Estado: "+element.getStatus()+
                                "\t Fecha esperada de entrega: "+element.getDeliveryDate()
                                ;
                        break;
                    default:
                        t1=t0=0;
                        break;
                        
                }
                
                
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

    private static int enviarProducto(int idPedido, java.lang.String empresaEnvio,wsmensajeria.EnvioPaquetes port) {
        return port.enviarProducto(idPedido, empresaEnvio);
    }

    private static CustomerOrder find(int id,wsmensajeria.EnvioPaquetes port) {
        return port.find(id);
    }

    
    
    
}
//================================================================

