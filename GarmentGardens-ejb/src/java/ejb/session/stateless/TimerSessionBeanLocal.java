/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Local;

/**
 *
 * @author brennanlee
 */
@Local
public interface TimerSessionBeanLocal {
    
    public void myTimer();

    public void beginFlashSales();

    public void endFlashSales();

    public void checkRewardExpiry();

    public void productEntityReorderQuantityCheckTimer();
}
