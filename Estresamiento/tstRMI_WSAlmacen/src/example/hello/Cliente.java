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
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import sun.util.calendar.LocalGregorianCalendar.Date;
import wsAlmacen.Product;
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
             wsAlmacen.Almacen_Service service = new wsAlmacen.Almacen_Service();
             wsAlmacen.Almacen port = service.getAlmacenPort();
        
             //Parámetros para testear
             
             strP="Alta";
             String author;
             int idCat=1;
             long id;
             int stock = 1;
             long idL;
             wsAlmacen.Product p = new wsAlmacen.Product(); 
             
             for (i = 0; i < n; i++) {
                if (aleatorio==1){
                     prueba=(int)(5*Math.random());
                }
                switch (prueba){
                    case 1: //Prueba para creates
                        author = "author"+ i;
                        wsAlmacen.Category cat= new wsAlmacen.Category ();  
                        cat.setId(1);
                        cat.setName("scifi");
                        p.setCategoryId(cat);
                        p.setLastUpdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new java.util.GregorianCalendar()));
                        p.setDescription("Descr");
                        p.setPrice(new BigDecimal(123.24));
                        p.setReserved(1);
                        p.setStock(10);
                        idL= new Long(i);
                        p.setAuthor(author);
                        p.setName("Rapido y furioso" + i);
                        t0 = System.currentTimeMillis();
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                        */
                        
                        idL=create(p,port);
                        /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        t1 = System.currentTimeMillis();
                        strCadRes="Alta de " + idL;
                          
                    break;
                    
                    case 2: //Find
                        
                        strP="Find: ";
                        id= 8507864512910L+(int)Math.ceil(Math.random()*5); 
                        
                        t0 = System.currentTimeMillis();
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        p=find(id, port); 
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        t1 = System.currentTimeMillis();
                        strCadRes="Find: " + p.getName() + " author " + p.getAuthor();
                    break;
                    
                    case 3: //Pruebas para edit
                        
                        strP="Cambio: ";
                        id= 8507864512910L+(int)Math.ceil(Math.random()*5); 
                        p= find(id,port);
                        p.setAuthor("author modificado " + i);
                        p.setName("Título modificado");
                        System.out.println("Sin modificar \n Nombre: "+p.getName() 
                                +" Autor: "+ p.getAuthor()
                                +" Nombre:" + p.getName() 
                                +" Descripción:" + p.getDescription());
                        t0 = System.currentTimeMillis();
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        id=edit(p,port);
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        t1 = System.currentTimeMillis();
                        strCadRes="Modificación a: " + id;
                        break;
                        
                    case 4: //Comprobar Stock
                        
                        strP="Comprobar Stock: ";
                        String isbn="8507864512910";
                        stock=stock+1;
                        t0 = System.currentTimeMillis();
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        int res=comprobarStock(isbn,stock,port);
                          /*
                          =====================================================
                          Código para ejecución de la transacción particular
                          ====================================================
                          */
                        t1 = System.currentTimeMillis();
                        if(res<0){
                            strCadRes="El libro no existe en la base de datos";
                        }else if (res==0){
                            strCadRes="No hay suficiente stock disponible";
                        }else{
                            strCadRes="Se apartaron "+res+" libros con id "+isbn;
                        }
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

    private static long create(wsAlmacen.Product entity, wsAlmacen.Almacen port) {
        //wsAlmacen.Almacen_Service service = new wsAlmacen.Almacen_Service();
        //wsAlmacen.Almacen port = service.getAlmacenPort();
        return port.create(entity);
    }

    private static long edit(wsAlmacen.Product entity, wsAlmacen.Almacen port) {
        //wsAlmacen.Almacen_Service service = new wsAlmacen.Almacen_Service();
        //wsAlmacen.Almacen port = service.getAlmacenPort();
        return port.edit(entity);
    }

    private static int comprobarStock(java.lang.String isbn, int unidades,  wsAlmacen.Almacen port) {
        //wsAlmacen.Almacen_Service service = new wsAlmacen.Almacen_Service();
        //wsAlmacen.Almacen port = service.getAlmacenPort();
        return port.comprobarStock(isbn, unidades);
    }

    private static java.util.List<wsAlmacen.Product> findAll( wsAlmacen.Almacen port) {
        //wsAlmacen.Almacen_Service service = new wsAlmacen.Almacen_Service();
        //wsAlmacen.Almacen port = service.getAlmacenPort();
        return port.findAll();
    }

    private static Product find(long id, wsAlmacen.Almacen port) {
        //wsAlmacen.Almacen_Service service = new wsAlmacen.Almacen_Service();
        //wsAlmacen.Almacen port = service.getAlmacenPort();
        return port.find(id);
    }

    
    
}
//================================================================

