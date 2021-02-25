/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsMensajeria;

import entity.CustomerOrder;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import session.CustomerOrderFacade;

/**
 *
 * @author jcsiglerp
 */
@WebService(serviceName = "EnvioPaquetes")
public class EnvioPaquetes {
    
    public static class OrderStatus {
        public static String ENTREGADO = "Entregado";
        public static String EN_CAMINO = "En camino";
    }

    @EJB
    private CustomerOrderFacade ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

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
     * Operacion de un servicio web implementado con JAX-WS
     * que emite la orden de envio de un producto.
     *
     * @param idPedido Identificador de pedido
     * @param empresaEnvio Identificador de empresa
     * @return Tiempo de reparto en dias
     */
    @WebMethod(operationName = "enviarProducto")
    public int enviarProducto(@WebParam(name = "idPedido")
    int idPedido, @WebParam(name = "empresaEnvio")
    String empresaEnvio) {
        Random rnd = new Random();
        int diasParaEntrega = rnd.nextInt(15)+1; //Reparto entre 1 y 15 dias
        
        try {
            CustomerOrder order = this.find(idPedido);
            if (order.getDeliveryDate() != null) {
                Logger.getLogger(EnvioPaquetes.class.getName()).log(Level.SEVERE,"El pedido ya está siendo enviado");
                return -1;
            }
            LocalDate fechaEntrega = LocalDate.now().plusDays(diasParaEntrega);
            order.setDeliveryDate(java.sql.Date.valueOf(fechaEntrega));
            order.setStatus(OrderStatus.EN_CAMINO);
            order.setDeliverySystem(empresaEnvio);
            this.edit(order);
        } catch(Exception e) {
            Logger.getLogger(EnvioPaquetes.class.getName()).log(Level.SEVERE,"El pedido solicitado no existe");
            return -1;
        }
        
        return diasParaEntrega;
    }

    /**
     * Web service operation
     * @param idPedido Identificador de pedido
     * @return Si se marcó correctamente la entrega
     */
    @WebMethod(operationName = "hacerEntrega")
    public boolean hacerEntrega(@WebParam(name = "idPedido") int idPedido) {
        Date fechaEntrega = new Date();
        try {
            CustomerOrder order = this.find(idPedido);
            if (order.getStatus().equals(OrderStatus.ENTREGADO)) {
                Logger.getLogger(EnvioPaquetes.class.getName()).log(Level.SEVERE,"El pedido ya fue entregado antes.");
                return false;
            }
            order.setDeliveryDate(fechaEntrega);
            order.setStatus(OrderStatus.ENTREGADO);
            this.edit(order);
        } catch(Exception e) {
            Logger.getLogger(EnvioPaquetes.class.getName()).log(Level.SEVERE,"El pedido solicitado no existe");
            return false;
        }
        return true;
    }
    
    
    
}