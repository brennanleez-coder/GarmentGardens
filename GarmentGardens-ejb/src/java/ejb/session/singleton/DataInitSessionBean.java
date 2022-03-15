/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.MessageOfTheDayEntity;
import entity.StaffEntity;
import entity.UserEntity;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoleEnum;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserUsernameExistException;

/**
 *
 * @author rilwa
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;

    
    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager em;
    

    public DataInitSessionBean() {
    }
    
        @PostConstruct
    public void postConstruct()
    {
        try
        {
            staffEntitySessionBeanLocal.retrieveStaffByUsername("admin");
        }
        catch(StaffNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData() 
    {
        try 
        {
            StaffEntity admin = new StaffEntity("admin", "lee", AccessRightEnum.ADMINISTRATOR, "admin", "password");
            StaffEntity manager = new StaffEntity("manager", "lee", AccessRightEnum.MANAGER, "manager", "password");
            UserEntity customer = new UserEntity("customer", "lee", "customer@mail.com", "customer", "password", new Date(), "NUS", RoleEnum.CUSTOMER);
            UserEntity seller = new UserEntity("seller", "lee", "seller@mail.com", "seller", "password", new Date(), "NUS", RoleEnum.SELLER);
            
            staffEntitySessionBeanLocal.createNewStaff(admin);
            staffEntitySessionBeanLocal.createNewStaff(manager);
            userEntitySessionBeanLocal.createNewUser(customer);
            userEntitySessionBeanLocal.createNewUser(seller);
            
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 1", "Message 1", new Date()));
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 2", "Message 2", new Date()));
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 3", "Message 3", new Date()));             
        }
        catch(StaffUsernameExistException | UserUsernameExistException | UnknownPersistenceException | InputDataValidationException  ex) 
        {
            ex.printStackTrace();
        }
        
    }
    
    
   
}
