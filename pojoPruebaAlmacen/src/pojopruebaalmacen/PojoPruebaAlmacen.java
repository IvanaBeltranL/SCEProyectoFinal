/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojopruebaalmacen;

/**
 *
 * @author ruben
 */
public class PojoPruebaAlmacen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String isbn="8507864512940";
        int n=100;
        int res;
        System.out.println("ESTADO INICIAL: ");
        printDB(findAll());
        
        System.out.println("\nEJECUCIÓN WEB SERVICE ...\n");
        
        System.out.println("Se apartará "+n+" libros con id "+isbn);
        res=comprobarStock(isbn,n);
        if(res<0){
            System.out.println("El libro no existe en la base de datos");
        }else if (res==0){
            System.out.println("No hay suficiente stock disponible");
        }else{
            System.out.println("Se apartaron "+res+" libros con id "+isbn);
        }
        System.out.println("ESTADO FINAL: ");
        printDB(findAll());
    }

    private static int comprobarStock(java.lang.String isbn, int unidades) {
        wsalmacen.Almacen_Service service = new wsalmacen.Almacen_Service();
        wsalmacen.Almacen port = service.getAlmacenPort();
        return port.comprobarStock(isbn, unidades);
    }

    private static java.util.List<wsalmacen.Product> findAll() {
        wsalmacen.Almacen_Service service = new wsalmacen.Almacen_Service();
        wsalmacen.Almacen port = service.getAlmacenPort();
        return port.findAll();
    }
    private static void printDB(java.util.List<wsalmacen.Product> list){
        for (wsalmacen.Product element : list) {
            System.out.println("ID "+element.getId()+":" );
            System.out.println("\t Titulo: "+element.getName());
            System.out.println("\t Autor: "+element.getAuthor());
            System.out.println("\t Stock: "+element.getStock());
            System.out.println("\t Reservados: "+element.getReserved());
        }
    }
}
