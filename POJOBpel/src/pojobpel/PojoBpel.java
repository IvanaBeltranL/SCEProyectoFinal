/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojobpel;

/**
 *
 * @author Ivana
 */
public class PojoBpel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String response = "";
        int n = args.length;
        if (n > 0) {
            n = Integer.parseInt(args[0]);
        } else {
            n = 10;
        }
        for (int i = 0; i < n; i++) {
            response = tienditaWSDLOperation("8507864512910", 6, 1);
            System.out.println(response);
        }
    }

    private static String tienditaWSDLOperation(java.lang.String isbn, int unidades, int idCliente) {
        org.netbeans.j2ee.wsdl.tienditalibrosonline.descriptorbpel.tienditawsdl.TienditaWSDLService service = new org.netbeans.j2ee.wsdl.tienditalibrosonline.descriptorbpel.tienditawsdl.TienditaWSDLService();
        org.netbeans.j2ee.wsdl.tienditalibrosonline.descriptorbpel.tienditawsdl.TienditaWSDLPortType port = service.getTienditaWSDLPort();
        return port.tienditaWSDLOperation(isbn, unidades, idCliente);
    }

   

       

}
