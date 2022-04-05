package ws.restful;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class SessionBeanLookup 
{
    private final String ejbModuleJndiPath;
    
    
    
    public SessionBeanLookup()
    {
        ejbModuleJndiPath = "java:global/PointOfSaleSystemV60/PointOfSaleSystemV60-ejb/";
    }
    
    
    
    public StaffEntitySessionBeanLocal lookupStaffEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StaffEntitySessionBeanLocal) c.lookup(ejbModuleJndiPath + "StaffEntitySessionBean!ejb.session.stateless.StaffEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
    public CategoryEntitySessionBeanLocal lookupCategoryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategoryEntitySessionBeanLocal) c.lookup(ejbModuleJndiPath + "CategoryEntitySessionBean!ejb.session.stateless.CategoryEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
    public TagEntitySessionBeanLocal lookupTagEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TagEntitySessionBeanLocal) c.lookup(ejbModuleJndiPath + "TagEntitySessionBean!ejb.session.stateless.TagEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
    public ProductEntitySessionBeanLocal lookupProductEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductEntitySessionBeanLocal) c.lookup(ejbModuleJndiPath + "ProductEntitySessionBean!ejb.session.stateless.ProductEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
    public MessageOfTheDayEntitySessionBeanLocal lookupMessageOfTheDaySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MessageOfTheDayEntitySessionBeanLocal) c.lookup(ejbModuleJndiPath + "MessageOfTheDayEntitySessionBean!ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}