package ws.restful;

import ejb.session.stateless.CartEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.DisputeEntitySessionBeanLocal;
import ejb.session.stateless.LineItemEntitySessionBeanLocal;
import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.RatingEntitySessionBeanLocal;
import ejb.session.stateless.RewardEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SessionBeanLookup {

    private final String ejbModuleJndiPath;

    public SessionBeanLookup() {
        ejbModuleJndiPath = "java:global/GarmentGardens/GarmentGardens-ejb/";
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

    public UserEntitySessionBeanLocal lookupUserEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (UserEntitySessionBeanLocal) c.lookup(ejbModuleJndiPath + "UserEntitySessionBean!ejb.session.stateless.UserEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public CreditCardEntitySessionBeanLocal lookupCreditCardEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CreditCardEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/CreditCardEntitySessionBean!ejb.session.stateless.CreditCardEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public RewardEntitySessionBeanLocal lookupRewardEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RewardEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/RewardEntitySessionBean!ejb.session.stateless.RewardEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public RatingEntitySessionBeanLocal lookupRatingEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RatingEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/RatingEntitySessionBean!ejb.session.stateless.RatingEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public DisputeEntitySessionBeanLocal lookupDisputeEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DisputeEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/DisputeEntitySessionBean!ejb.session.stateless.DisputeEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public CartEntitySessionBeanLocal lookupCartEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CartEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/CartEntitySessionBean!ejb.session.stateless.CartEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public LineItemEntitySessionBeanLocal lookupLineItemEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (LineItemEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/LineItemEntitySessionBean!ejb.session.stateless.LineItemEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public OrderEntitySessionBeanLocal lookupOrderEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OrderEntitySessionBeanLocal) c.lookup("java:global/GarmentGardens/GarmentGardens-ejb/OrderEntitySessionBean!ejb.session.stateless.OrderEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
