/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojopruebapago;

/**
 *
 * @author ruben
 */
public class PojoPruebaPago {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String isbn="8507864512910";
        int n=1;
        int idCliente=1;
        int res;
        
        System.out.println("ESTADO INICIAL: ");
        printDB(findAll());
        
        System.out.println("\nEJECUCIÓN WEB SERVICE ...\n");
        
        System.out.println("El cliente con id "+idCliente+" comprará "+n+" libros con id "+isbn);
        res=facturar(isbn,idCliente,n);
        if(res<0){
            System.out.println("El cliente no tiene suficiente credito");
        }else{
            System.out.println("Se realizó la factura con el id "+res);
        }
        System.out.println("ESTADO FINAL: ");
        printDB(findAll());
    }

    private static int facturar(java.lang.String idProducto, int idCliente, int cantidad) {
        wspasarelapago.PasarelaPago_Service service = new wspasarelapago.PasarelaPago_Service();
        wspasarelapago.PasarelaPago port = service.getPasarelaPagoPort();
        return port.facturar(idProducto, idCliente, cantidad);
    }

    private static java.util.List<wspasarelapago.CustomerOrder> findAll() {
        wspasarelapago.PasarelaPago_Service service = new wspasarelapago.PasarelaPago_Service();
        wspasarelapago.PasarelaPago port = service.getPasarelaPagoPort();
        return port.findAll();
    }
    
    private static void printDB(java.util.List<wspasarelapago.CustomerOrder> list){
        for (wspasarelapago.CustomerOrder element : list) {
            System.out.println("ID "+element.getId()+":" );
            System.out.println("\t Cliente: "+element.getCustomerId().getId());
            System.out.println("\t Producto: "+element.getProductId().getId());
            System.out.println("\t Cantidad: "+element.getQuantity());
            System.out.println("\t Precio: "+element.getAmount());
            System.out.println("\t Fecha: "+element.getDateCreated());
        }
    }
}
