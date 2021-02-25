/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsrellenardatos;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import session.CustomerFacade;
import session.ProductFacade;

/**
 *
 * @author ruben
 */
@WebService(serviceName = "RellenarDatos")
public class RellenarDatos {

    @EJB
    private ProductFacade productFacade;

    @EJB
    private CustomerFacade customerFacade;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "fillCredit")
    public int fillCredit(@WebParam(name = "idCliente") int idCliente, @WebParam(name = "credit") double credit) {
        //TODO write your implementation code here:
        
        entity.Customer clte=customerFacade.find(idCliente);
        clte.setCredit(clte.getCredit().add(new BigDecimal(credit)));
        int id=clte.getId();
        customerFacade.edit(clte);
        Logger.getLogger(RellenarDatos.class.getName()).log(Level.SEVERE, "El id del Customer al que se le modifico el credito es {0}", id+"");
        return id;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "fillStock")
    public long fillStock(@WebParam(name = "idProduct") String idProduct, @WebParam(name = "stock") int stock) {
        //TODO write your implementation code here:
        entity.Product prod=productFacade.find(Long.parseLong(idProduct));
        prod.setStock(prod.getStock()+stock);
        long id=prod.getId();
        Logger.getLogger(RellenarDatos.class.getName()).log(Level.SEVERE, "El");
        productFacade.edit(prod);
        Logger.getLogger(RellenarDatos.class.getName()).log(Level.SEVERE, "El id del Product que se modifico el stock es {0}", id+"");
        return id;
    }
    
    

    
}