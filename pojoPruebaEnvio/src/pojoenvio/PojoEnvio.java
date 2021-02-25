/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojoenvio;

/**
 *
 * @author ruben
 */
public class PojoEnvio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int idPedido=1;
        String empresa="TuLibroOnline";
        int res;
        
        System.out.println("ESTADO INICIAL: ");
        printDB(findAll());
        
        System.out.println("\nEJECUCIÓN WEB SERVICE ...\n");
        
        res=enviarProducto(idPedido,empresa);
        if(res>0)
            System.out.println("El pedido "+idPedido+" se enviará en "+res+" dias \n");
        else
            System.out.println("El pedido "+idPedido+" ya está en proceso de envío \n");
        
        System.out.println("ESTADO FINAL: ");
        printDB(findAll());
    }

    private static java.util.List<wsmensajeria.CustomerOrder> findAll() {
        wsmensajeria.EnvioPaquetes_Service service = new wsmensajeria.EnvioPaquetes_Service();
        wsmensajeria.EnvioPaquetes port = service.getEnvioPaquetesPort();
        return port.findAll();
    }

    private static int enviarProducto(int idPedido, java.lang.String empresaEnvio) {
        wsmensajeria.EnvioPaquetes_Service service = new wsmensajeria.EnvioPaquetes_Service();
        wsmensajeria.EnvioPaquetes port = service.getEnvioPaquetesPort();
        return port.enviarProducto(idPedido, empresaEnvio);
    }
    private static void printDB(java.util.List<wsmensajeria.CustomerOrder> list){
        for (wsmensajeria.CustomerOrder element : list) {
            System.out.println("ID "+element.getId()+":" );
            System.out.println("\t Cliente: "+element.getCustomerId().getId());
            System.out.println("\t Producto: "+element.getProductId().getId());
            System.out.println("\t Precio: "+element.getAmount());
            System.out.println("\t Fecha: "+element.getDateCreated());
            System.out.println("\t Empresa: "+element.getDeliverySystem());
            System.out.println("\t Estado: "+element.getStatus());
            System.out.println("\t Fecha esperada de entrega: "+element.getDeliveryDate());
            
            
        }
    }
}
