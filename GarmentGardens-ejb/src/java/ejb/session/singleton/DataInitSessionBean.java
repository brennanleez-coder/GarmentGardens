/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
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
        }
        catch(StaffUsernameExistException | UnknownPersistenceException | InputDataValidationException  ex) 
        {
            ex.printStackTrace();
        }
        
    }
    
    
   
}
