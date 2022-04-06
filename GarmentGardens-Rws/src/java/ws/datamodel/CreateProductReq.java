package ws.datamodel;

import entity.ProductEntity;
import java.util.List;



public class CreateProductReq
{
    private String username;
    private String password;
    private ProductEntity productEntity;
    private Long categoryId;
    private List<Long> tagIds;

    
    
    public CreateProductReq()
    {        
    }

    
    
    public CreateProductReq(String username, String password, ProductEntity productEntity, Long categoryId, List<Long> tagIds) 
    {
        this.username = username;
        this.password = password;
        this.productEntity = productEntity;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
    }
    
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
