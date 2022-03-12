/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.MessageOfTheDayEntity;
import entity.StaffEntity;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewProductException;
import util.exception.CreateNewTagException;
import util.exception.InputDataValidationException;
import util.exception.ProductSkuCodeExistException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rilwa
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager em;
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DataInitSessionBean() {
    }
    
        @PostConstruct
    public void postConstruct()
    {
        try
        {
            staffEntitySessionBeanLocal.retrieveStaffByUsername("manager");
        }
        catch(StaffNotFoundException ex)
        {
            initializeData();
        }
//        em.persist(new MessageOfTheDayEntity("test", "This is a test run", new Date()));
//        em.persist(new StaffEntity("manager", "lee", AccessRightEnum.ADMINISTRATOR,"manager","password" ));
    }
    
    private void initializeData() 
    {
        try 
        {
            StaffEntity bigboss = new StaffEntity("manager", "lee", AccessRightEnum.ADMINISTRATOR, "manager", "password");
            staffEntitySessionBeanLocal.createNewStaff(bigboss);
            
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 1", "Message 1", new Date()));
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 2", "Message 2", new Date()));
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 3", "Message 3", new Date()));             
        }
        catch(StaffUsernameExistException | UnknownPersistenceException | InputDataValidationException  ex) 
        {
            ex.printStackTrace();
        }
        
    }
    
    
   
}
