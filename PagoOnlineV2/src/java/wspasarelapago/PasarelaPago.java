/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wspasarelapago;

import entity.CustomerOrder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import session.CustomerFacade;
import session.CustomerOrderFacade;
import session.ProductFacade;

/**
 *
 * @author DPEREZLAN
 */
@WebService(serviceName = "PasarelaPago")
public class PasarelaPago {

    @EJB
    private ProductFacade productFacade;

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private CustomerOrderFacade ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "create")
    //@Oneway
    public int create(@WebParam(name = "entity") CustomerOrder entity) {
        ejbRef.create(entity);
        long idNvoProd=entity.getId();
        Logger.getLogger(PasarelaPago.class.getName()).log(Level.SEVERE, "El id del nuevo Product es {0}", entity.toString());
        return (int)idNvoProd;
    }

    @WebMethod(operationName = "edit")
    @Oneway
    public void edit(@WebParam(name = "entity") CustomerOrder entity) {
        ejbRef.edit(entity);
    }


    @WebMethod(operationName = "find")
    public CustomerOrder find(@WebParam(name = "id") int id) {
        return ejbRef.find(id);
    }

    @WebMethod(operationName = "findAll")
    public List<CustomerOrder> findAll() {
        return ejbRef.findAll();
    }
    

    /**
     * Web service operation
     */
    @WebMethod(operationName = "Facturar")
    public int Facturar(@WebParam(name = "idProducto") String idProducto, @WebParam(name = "idCliente") int idCliente, @WebParam(name = "cantidad") int cantidad) {
        entity.Product prod=productFacade.find(Long.parseLong(idProducto));
        Logger.getLogger(PasarelaPago.class.getName()).log(Level.SEVERE, "El id del nuevo Product es {0}", prod.toString());
        BigDecimal amount=prod.getPrice().multiply(new BigDecimal(cantidad));
        entity.Customer clte=customerFacade.find(idCliente);
        Logger.getLogger(PasarelaPago.class.getName()).log(Level.SEVERE, "El id del nuevo Product es {0}", clte.toString());
        BigDecimal saldo=clte.getCredit();
        int id=-1;
        if(saldo.compareTo(amount) >= 0){
            entity.CustomerOrder orden=new entity.CustomerOrder();
            orden.setAmount(amount);
            orden.setQuantity((short) cantidad);
            orden.setCustomerId(clte);
            orden.setProductId(prod);
            orden.setDateCreated(new Date());
            orden.setStatus("");
            id=create(orden);
            Logger.getLogger(PasarelaPago.class.getName()).log(Level.SEVERE, "El id del nuevo Product es {0}", id);
            clte.setCredit(clte.getCredit().subtract(amount));
            customerFacade.edit(clte);
        }
        else{
            prod.setStock(prod.getStock()+cantidad);
        }
        prod.setReserved(prod.getReserved()-cantidad);
        productFacade.edit(prod);

        
        return id;
    }
    
    
    
    
}