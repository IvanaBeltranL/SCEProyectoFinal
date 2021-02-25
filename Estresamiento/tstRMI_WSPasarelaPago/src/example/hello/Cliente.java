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
import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import wspasarelapago.CustomerOrder;
public class Cliente 
{
    
    public static void main(String[] args) 
    {
        /*
        Parámetros de entrada
        1: host
        2: num transacciones
        3: tipo de prueba
        4: aleatorio, decide se hace transacciones en aleatorio o en todos.
        */
        
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
       aleatorio = args.length>=4? Integer.parseInt(args[3]):0;//Preparar para que sea parámetro
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
             
             //Puerto para WS             
            wspasarelapago.PasarelaPago_Service service = new wspasarelapago.PasarelaPago_Service();
            wspasarelapago.PasarelaPago port = service.getPasarelaPagoPort();
             //Parámetros para testear
             
             strP="Facturar";
             //Fin de parámetros 
             
             for (i = 0; i < n; i++) {
                if (aleatorio==1){
                     prueba=(int)(5*Math.random());
                }
                switch (prueba){
                    case 1: //Prueba para Facturar
                        long idL=8507864512910L+(int)Math.ceil(Math.random()*5);
                        String prod=""+idL;
                        int idC=(int) lngQuienSoy;
                        int cantidad=(int)( Math.ceil(Math.random()*8));
                        t0 = System.currentTimeMillis();
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                        */
                        
                        int idFac=facturar(prod,idC,cantidad,port);
                        /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        t1 = System.currentTimeMillis();
                        strCadRes=idFac>0?"Factura expedida: " + idFac:" El cliente no tiene saldo suficiente";
                          
                    break;
                    
                    case 2: //Prueba para Find
                        
                        strP="Find: ";
                        int idF= (int)Math.ceil(Math.random()*4);
                        wspasarelapago.CustomerOrder element = new wspasarelapago.CustomerOrder();
                        t0 = System.currentTimeMillis();
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        element=find(idF, port); 
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        t1 = System.currentTimeMillis();
                        strCadRes=element.getId()!=null?element.toString():"No existe";
                    break;
                    default:
                        t1=0;
                        t0=0;
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
              servDisparo.acumula(sumDeltaT, sumDeltaT2, n, dtMax, dtMin);
            }
          catch (Exception e)
          {
              System.err.println("Client exception: " + e.toString());
               e.printStackTrace();
           }
    }

    private static int create(wspasarelapago.CustomerOrder entity,wspasarelapago.PasarelaPago port ) {
        return port.create(entity);
    }

    private static CustomerOrder find(int id,wspasarelapago.PasarelaPago port ) {
        return port.find(id);
    }

    private static int facturar(java.lang.String idProducto, int idCliente, int cantidad,wspasarelapago.PasarelaPago port ) {
        return port.facturar(idProducto, idCliente, cantidad);
    }

    

    
    
}
//================================================================

