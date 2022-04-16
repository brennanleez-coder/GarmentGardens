package ws.datamodel;

import entity.LineItemEntity;
import entity.UserEntity;



public class RemoveLineItemReq
{
    private UserEntity currentUser;
    private LineItemEntity lineItemToRemove;
    private boolean clearCart;

    
    public RemoveLineItemReq()
    {        
    }

    
    
    public RemoveLineItemReq(UserEntity currentUser, LineItemEntity lineItemToRemove, boolean clearCart) 
    {
        this.currentUser = currentUser;
        this.lineItemToRemove = lineItemToRemove;
        this.clearCart = clearCart;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }


    public boolean getClearCart() {
        return clearCart;
    }

    public void setClearCart(boolean clearCart) {
        this.clearCart = clearCart;
    }

    public LineItemEntity getLineItemToRemove() {
        return lineItemToRemove;
    }

    public void setLineItemToRemove(LineItemEntity lineItemToRemove) {
        this.lineItemToRemove = lineItemToRemove;
    }
}

