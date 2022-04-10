package ws.datamodel;

import entity.UserEntity;



public class CreateUserReq
{
    private UserEntity userEntity;


    
    
    public CreateUserReq()
    {        
    }

    
    
    public CreateUserReq(UserEntity userEntity) 
    {
        this.userEntity = userEntity;
    }
    
    
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


}

