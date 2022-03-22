/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.MessageOfTheDayEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoleEnum;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewProductException;
import util.exception.CreateNewTagException;
import util.exception.InputDataValidationException;
import util.exception.ProductSkuCodeExistException;
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

    @EJB(name = "TagEntitySessionBeanLocal")
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "ProductEntitySessionBeanLocal")
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

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
    public void postConstruct() {
        try {
            staffEntitySessionBeanLocal.retrieveStaffByUsername("admin");
        } catch (StaffNotFoundException ex) {
            initializeData();

        }
    }

    private void initializeData() {
        try {
            System.out.println("FK LA CB");
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

            TagEntity tagEntityPopular = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("popular"));
            TagEntity tagEntityDiscount = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("discount"));
            TagEntity tagEntityNew = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("new"));

            List<Long> tagIdsPopular = new ArrayList<>();
            tagIdsPopular.add(tagEntityPopular.getTagId());

            List<Long> tagIdsDiscount = new ArrayList<>();
            tagIdsDiscount.add(tagEntityDiscount.getTagId());

            List<Long> tagIdsPopularDiscount = new ArrayList<>();
            tagIdsPopularDiscount.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscount.add(tagEntityDiscount.getTagId());

            List<Long> tagIdsPopularNew = new ArrayList<>();
            tagIdsPopularNew.add(tagEntityPopular.getTagId());
            tagIdsPopularNew.add(tagEntityNew.getTagId());

            List<Long> tagIdsPopularDiscountNew = new ArrayList<>();
            tagIdsPopularDiscountNew.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityDiscount.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityNew.getTagId());
            List<Long> tagIdsEmpty = new ArrayList<>();

            CategoryEntity categoryEntityElectronics = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Electronics", "Electronics"), null);
            CategoryEntity categoryEntityFashions = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Fashions", "Fashions"), null);
            CategoryEntity categoryEntityA = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category A", "Category A"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityB = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category B", "Category B"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityC = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category C", "Category C"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityX = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category X", "Category X"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityY = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category Y", "Category Y"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityZ = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category Z", "Category Z"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityM = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category M", "Category M"), categoryEntityX.getCategoryId());
            CategoryEntity categoryEntityN = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category N", "Category N"), categoryEntityY.getCategoryId());
            CategoryEntity categoryEntityO = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category O", "Category O"), categoryEntityZ.getCategoryId());

            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD001", "Product A1", "Product A1", 100, new BigDecimal("10.00"), true), categoryEntityA.getCategoryId(), tagIdsPopular);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD002", "Product A2", "Product A2", 100, new BigDecimal("25.50"), true), categoryEntityA.getCategoryId(), tagIdsDiscount);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD003", "Product A3", "Product A3", 100, new BigDecimal("15.00"), true), categoryEntityA.getCategoryId(), tagIdsPopularDiscount);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD004", "Product B1", "Product B1", 100, new BigDecimal("20.00"), true), categoryEntityB.getCategoryId(), tagIdsPopularNew);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD005", "Product B2", "Product B2", 100, new BigDecimal("10.00"), true), categoryEntityB.getCategoryId(), tagIdsPopularDiscountNew);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD006", "Product B3", "Product B3", 100, new BigDecimal("100.00"), true), categoryEntityB.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD007", "Product C1", "Product C1", 100, new BigDecimal("35.00"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD008", "Product C2", "Product C2", 100, new BigDecimal("20.05"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD009", "Product C3", "Product C3", 100, new BigDecimal("5.50"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);

            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD011", "Product X2", "Product X2", 100, new BigDecimal("30.50"), true), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD012", "Product X3", "Product X3", 100, new BigDecimal("18.50"), true), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD013", "Product Y1", "Product Y1", 100, new BigDecimal("50.00"), true), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD014", "Product Y2", "Product Y2", 100, new BigDecimal("100.00"), true), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD015", "Product Y3", "Product Y3", 100, new BigDecimal("200.00"), true), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD016", "Product Z1", "Product Z1", 100, new BigDecimal("95.00"), true), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD017", "Product Z2", "Product Z2", 100, new BigDecimal("19.05"), true), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD018", "Product Z3", "Product Z3", 100, new BigDecimal("10.50"), true), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD019", "Product M1", "Product M1", 100, new BigDecimal("5.50"), true), categoryEntityM.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD020", "Product N1", "Product N1", 100, new BigDecimal("4.50"), true), categoryEntityN.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD021", "Product O1", "Product O1", 100, new BigDecimal("7.50"), true), categoryEntityO.getCategoryId(), tagIdsEmpty);
   
        } catch (StaffUsernameExistException | UserUsernameExistException | CreateNewTagException | CreateNewProductException | ProductSkuCodeExistException | CreateNewCategoryException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }

    }

    private void initaliseTagsCategoriesProducts() {
        try {
            TagEntity tagEntityPopular = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("popular"));
            TagEntity tagEntityDiscount = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("discount"));
            TagEntity tagEntityNew = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("new"));

            List<Long> tagIdsPopular = new ArrayList<>();
            tagIdsPopular.add(tagEntityPopular.getTagId());

            List<Long> tagIdsDiscount = new ArrayList<>();
            tagIdsDiscount.add(tagEntityDiscount.getTagId());

            List<Long> tagIdsPopularDiscount = new ArrayList<>();
            tagIdsPopularDiscount.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscount.add(tagEntityDiscount.getTagId());

            List<Long> tagIdsPopularNew = new ArrayList<>();
            tagIdsPopularNew.add(tagEntityPopular.getTagId());
            tagIdsPopularNew.add(tagEntityNew.getTagId());

            List<Long> tagIdsPopularDiscountNew = new ArrayList<>();
            tagIdsPopularDiscountNew.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityDiscount.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityNew.getTagId());
            List<Long> tagIdsEmpty = new ArrayList<>();

            CategoryEntity categoryEntityElectronics = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Electronics", "Electronics"), null);
            CategoryEntity categoryEntityFashions = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Fashions", "Fashions"), null);
            
            // UNDER ELECTRONICS
            CategoryEntity categoryEntityA = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category A", "Category A"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityB = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category B", "Category B"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityC = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category C", "Category C"), categoryEntityElectronics.getCategoryId());
            
            // UNDER FASHIONS
            CategoryEntity categoryEntityX = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category X", "Category X"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityY = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category Y", "Category Y"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityZ = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category Z", "Category Z"), categoryEntityFashions.getCategoryId());
            
            // UNDER FASHIONS => X,Y,Z
            CategoryEntity categoryEntityM = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category M", "Category M"), categoryEntityX.getCategoryId());
            CategoryEntity categoryEntityN = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category N", "Category N"), categoryEntityY.getCategoryId());
            CategoryEntity categoryEntityO = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category O", "Category O"), categoryEntityZ.getCategoryId());

            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD001", "Product A1", "Product A1", 100, new BigDecimal("10.00"), true), categoryEntityA.getCategoryId(), tagIdsPopular);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD002", "Product A2", "Product A2", 100, new BigDecimal("25.50"), true), categoryEntityA.getCategoryId(), tagIdsDiscount);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD003", "Product A3", "Product A3", 100, new BigDecimal("15.00"), true), categoryEntityA.getCategoryId(), tagIdsPopularDiscount);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD004", "Product B1", "Product B1", 100, new BigDecimal("20.00"), true), categoryEntityB.getCategoryId(), tagIdsPopularNew);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD005", "Product B2", "Product B2", 100, new BigDecimal("10.00"), true), categoryEntityB.getCategoryId(), tagIdsPopularDiscountNew);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD006", "Product B3", "Product B3", 100, new BigDecimal("100.00"), true), categoryEntityB.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD007", "Product C1", "Product C1", 100, new BigDecimal("35.00"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD008", "Product C2", "Product C2", 100, new BigDecimal("20.05"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD009", "Product C3", "Product C3", 100, new BigDecimal("5.50"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);

            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD011", "Product X2", "Product X2", 100, new BigDecimal("30.50"), true), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD012", "Product X3", "Product X3", 100, new BigDecimal("18.50"), true), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD013", "Product Y1", "Product Y1", 100, new BigDecimal("50.00"), true), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD014", "Product Y2", "Product Y2", 100, new BigDecimal("100.00"), true), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD015", "Product Y3", "Product Y3", 100, new BigDecimal("200.00"), true), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD016", "Product Z1", "Product Z1", 100, new BigDecimal("95.00"), true), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD017", "Product Z2", "Product Z2", 100, new BigDecimal("19.05"), true), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD018", "Product Z3", "Product Z3", 100, new BigDecimal("10.50"), true), categoryEntityZ.getCategoryId(), tagIdsEmpty);

            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD019", "Product M1", "Product M1", 100, new BigDecimal("5.50"), true), categoryEntityM.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD020", "Product N1", "Product N1", 100, new BigDecimal("4.50"), true), categoryEntityN.getCategoryId(), tagIdsEmpty);
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD021", "Product O1", "Product O1", 100, new BigDecimal("7.50"), true), categoryEntityO.getCategoryId(), tagIdsEmpty);
        
        } catch (CreateNewTagException | CreateNewProductException | ProductSkuCodeExistException | CreateNewCategoryException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }

    }

}
