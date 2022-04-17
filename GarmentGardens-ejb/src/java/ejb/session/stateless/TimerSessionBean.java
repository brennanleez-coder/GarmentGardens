/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MessageOfTheDayEntity;
import entity.ProductEntity;
import entity.RewardEntity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InputDataValidationException;

/**
 *
 * @author brennanlee
 */
@Stateless
public class TimerSessionBean implements TimerSessionBeanLocal {

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    @EJB(name = "ProductEntitySessionBeanLocal")
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    @EJB(name = "RewardEntitySessionBeanLocal")
    private RewardEntitySessionBeanLocal rewardEntitySessionBeanLocal;

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager em;

    public TimerSessionBean() {
    }

//    @Schedule(dayOfWeek = "Mon-Fri", hour = "12", info="beginFlashSales") //FOR ACTUAL USE
//    every weekday at 7-8am and 10-11pm start flash sales
    @Schedule(hour = "22", minute = "28", info = "beginFlashSales") // FOR DEMO PURPOSES
    public void beginFlashSales() {
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            System.out.println("BEGIN FLASH SALES: " + timeStamp);
            System.out.println("20% OFF ALL ITEMS, ENDS IN 20 MINUTES");

            List<ProductEntity> allProducts = productEntitySessionBeanLocal.retrieveAllProducts();
            for (ProductEntity product : allProducts) {
                product.setUnitPrice(product.getUnitPrice().multiply(new BigDecimal(0.80)));
            }
            MessageOfTheDayEntity motd = new MessageOfTheDayEntity();
            motd.setMessageDate(new Date());
            motd.setTitle("BEGIN FLASH SALES");
            motd.setMessage("ALL ITEMS ARE 20% OFF, ENDS IN 20 MINUTES");
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(motd);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
//    @Schedule(dayOfWeek = "Mon-Fri", hour = "12", minute = "15,", second="5", info = "endFlashSales") //FOR ACTUAL USE
    @Schedule(hour = "22", minute="29", second="5", info = "endFlashSales") //FOR DEMO PURPOSES
    public void endFlashSales() {
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            System.out.println("END FLASH SALES: " + timeStamp);
            System.out.println("ITEMS WILL BE BACK TO ORIGINAL PRICE");
            List<ProductEntity> allProducts = productEntitySessionBeanLocal.retrieveAllProducts();
            for (ProductEntity product : allProducts) {
                product.setUnitPrice(product.getUnitPrice().multiply(new BigDecimal(1.25)));
            }
            
            MessageOfTheDayEntity motd = new MessageOfTheDayEntity();
            motd.setMessageDate(new Date());
            motd.setTitle("FLASH SALES HAS ENDED");
            motd.setMessage("ALL ITEMS ARE BACK TO NORMAL PRICE");
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(motd);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Schedule(dayOfWeek = "Mon-Fri", minute="1")
    //every weekday at 0000
    public void checkRewardExpiry() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("Checking Reward Validity at: " + timeStamp);
        List<RewardEntity> allRewards = rewardEntitySessionBeanLocal.retrieveAllRewards();
        for (RewardEntity reward : allRewards) {
            Date expiryDate = reward.getExpiryDate();
            Boolean notExpired = expiryDate.after(new Date());
            if (!notExpired) {
                reward.setRewardName(reward.getRewardName().concat("(EXPIRED)"));
                System.out.println("Reward ID: " + reward.getRewardId() + " has expired");
            }
        }
    }

    @Schedule(hour = "*", minute = "*/1", info = "productEntityReorderQuantityCheckTimer")
    public void productEntityReorderQuantityCheckTimer() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.productEntityReorderQuantityCheckTimer(): Timeout at " + timeStamp);

        List<ProductEntity> productEntities = productEntitySessionBeanLocal.retrieveAllProducts();

        for (ProductEntity productEntity : productEntities) {
            if (productEntity.getQuantityOnHand() <= 30) {
                System.out.println("********** Product " + productEntity.getSkuCode() + " requires reordering: QOH = " + productEntity.getQuantityOnHand() + "; RQ = " + 100);
                productEntity.setQuantityOnHand(100);
            }
        }
    }
    
    @Schedule(hour = "*", minute = "*", second="*/5", info = "timer check")
    public void timerCheck() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("** TIMER CHECK: " + timeStamp);
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public void myTimer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
