/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.MessageOfTheDayEntity;
import entity.StaffEntity;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author rilwa
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DataInitSessionBean() {
    }
    
        @PostConstruct
    public void postConstruct()
    {
        em.persist(new MessageOfTheDayEntity("test", "This is a test run", new Date()));
        em.persist(new StaffEntity("manager", "lee", AccessRightEnum.ADMINISTRATOR,"manager","password" ));
    }
   
}
