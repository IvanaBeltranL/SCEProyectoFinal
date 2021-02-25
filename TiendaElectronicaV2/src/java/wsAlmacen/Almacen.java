/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsAlmacen;

import entity.Product;
import java.util.ArrayList;
import session.ProductFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author ruben
 */
@WebService(serviceName = "Almacen")
public class Almacen {

    @EJB
    private ProductFacade ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "create")
    //@Oneway
    public long create(@WebParam(name = "entity") Product entity) {
        ejbRef.create(entity);
        long idNvoProd=entity.getId();
        Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, "El id del nuevo Product es {0}", entity.toString());
        
        return idNvoProd;
    }

    @WebMethod(operationName = "edit")
    //@Oneway
    public long edit(@WebParam(name = "entity") Product entity) {
        
        ejbRef.edit(entity);
        long idProd=entity.getId();
        Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, "El id del Product modificado es {0}", entity.toString());
        return idProd;
        
    }

    @WebMethod(operationName = "find")
    public Product find(@WebParam(name = "id") long id) {
        return ejbRef.find(id);
    }

    @WebMethod(operationName = "findAll")
    public List<Product> findAll() {
        return ejbRef.findAll();
    }
    
    /**
     * Web service operation
     */
    //Comprobar si existe el libro, si existe comprobar si hay stock, si hay stock reservar.
    @WebMethod(operationName = "comprobarStock")
    public int comprobarStock(@WebParam(name = "ISBN") String ISBN, @WebParam(name = "unidades") int unidades) {
        //TODO write your implementation code here:
        int res=-1;
        
        try{
            Product prod= find(Long.parseLong(ISBN));
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE,"El stock del producto solicitado es "+prod.getStock());
            if(prod.getStock()>=unidades){
                
                Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE,"El producto solicitado "+prod.toString()+" tiene suficiente stock");
                res=unidades;
                prod.setStock(prod.getStock()-res);
                prod.setReserved(prod.getReserved()+unidades);
                edit(prod);
                prod=find(Long.parseLong(ISBN));
                Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE,"El stock del producto solicitado ahora es "+prod.getStock());
                Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE,"Los libros reservados del producto solicitado ahora son "+prod.getReserved());
            }
            else{
                res=0;
                Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE,"No hay suficiente stock");
            }
        }
        catch(Exception e){
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE,"El producto solicitado no existe");
        }
        return res;
    }
    
    
}
