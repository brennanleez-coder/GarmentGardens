/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdvertisementEntitySessionBeanLocal;
import ejb.session.stateless.AdvertiserEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.AdvertisementEntity;
import entity.AdvertiserEntity;
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
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoleEnum;
import util.exception.AdvertiserEntityExistException;
import util.exception.AdvertiserEntityNotFoundException;
import util.exception.CreateNewAdvertisementException;
import util.exception.CreateNewAdvertiserEntityException;
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

    @EJB(name = "AdvertiserEntitySessionBeanLocal")
    private AdvertiserEntitySessionBeanLocal advertiserEntitySessionBeanLocal;

    @EJB(name = "AdvertisementEntitySessionBeanLocal")
    private AdvertisementEntitySessionBeanLocal advertisementEntitySessionBeanLocal;

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
            System.out.println("Initialising database......");
            initialiseStaffCustomersSellers();

            initaliseCategoriesTags();
            initialiseAdvertisersAndAdvertisements();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void initaliseCategoriesTags() throws UnknownPersistenceException, ProductSkuCodeExistException, CreateNewTagException, CreateNewProductException, CreateNewCategoryException, InputDataValidationException {
        messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 1", "Message 1", new Date()));
        messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 2", "Message 2", new Date()));
        messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(new MessageOfTheDayEntity("Title 3", "Message 3", new Date()));

        TagEntity tagEntityPopular = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("Popular"));
        TagEntity tagEntityDiscount = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("Discount"));
        TagEntity tagEntityNew = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("New"));
        TagEntity tagEntityLimited = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("Limited"));

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
        CategoryEntity categoryEntityA = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category A", "Category A"), categoryEntityElectronics);
        CategoryEntity categoryEntityB = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category B", "Category B"), categoryEntityElectronics);
        CategoryEntity categoryEntityC = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category C", "Category C"), categoryEntityElectronics);
        CategoryEntity categoryEntityX = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category X", "Category X"), categoryEntityFashions);
        CategoryEntity categoryEntityY = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category Y", "Category Y"), categoryEntityFashions);
        CategoryEntity categoryEntityZ = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category Z", "Category Z"), categoryEntityFashions);
        CategoryEntity categoryEntityM = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category M", "Category M"), categoryEntityX);
        CategoryEntity categoryEntityN = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category N", "Category N"), categoryEntityY);
        CategoryEntity categoryEntityO = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Category O", "Category O"), categoryEntityZ);

        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD001", "Product A1", "Product A1", 100, new BigDecimal("10.00"), true), categoryEntityA.getCategoryId(), tagIdsPopular);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD002", "Product A2", "Product A2", 100, new BigDecimal("25.50"), true), categoryEntityA.getCategoryId(), tagIdsDiscount);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD003", "Product A3", "Product A3", 100, new BigDecimal("15.00"), true), categoryEntityA.getCategoryId(), tagIdsPopularDiscount);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD004", "Product B1", "Product B1", 100, new BigDecimal("20.00"), true), categoryEntityB.getCategoryId(), tagIdsPopularNew);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD005", "Product B2", "Product B2", 100, new BigDecimal("10.00"), true), categoryEntityB.getCategoryId(), tagIdsPopularDiscountNew);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD006", "Product B3", "Product B3", 100, new BigDecimal("100.00"), true), categoryEntityB.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD007", "Product C1", "Product C1", 100, new BigDecimal("35.00"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD008", "Product C2", "Product C2", 100, new BigDecimal("20.05"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD009", "Product C3", "Product C3", 100, new BigDecimal("5.50"), true), categoryEntityC.getCategoryId(), tagIdsEmpty);

        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD010", "Product M1", "Product M1", 100, new BigDecimal("20.50"), true), categoryEntityM.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD011", "Product M2", "Product M2", 100, new BigDecimal("30.50"), true), categoryEntityM.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD012", "Product M3", "Product M3", 100, new BigDecimal("18.50"), true), categoryEntityM.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD013", "Product N1", "Product N1", 100, new BigDecimal("50.00"), true), categoryEntityN.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD014", "Product N2", "Product N2", 100, new BigDecimal("100.00"), true), categoryEntityN.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD015", "Product N3", "Product N3", 100, new BigDecimal("200.00"), true), categoryEntityN.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD016", "Product O1", "Product O1", 100, new BigDecimal("95.00"), true), categoryEntityO.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD017", "Product O2", "Product O2", 100, new BigDecimal("19.05"), true), categoryEntityO.getCategoryId(), tagIdsEmpty);
        productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD018", "Product O3", "Product O3", 100, new BigDecimal("10.50"), true), categoryEntityO.getCategoryId(), tagIdsEmpty);
    }

    private void initialiseStaffCustomersSellers() throws UserUsernameExistException, StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        StaffEntity admin = new StaffEntity("admin", "lee", AccessRightEnum.ADMINISTRATOR, "admin", "password");
        StaffEntity manager = new StaffEntity("manager", "lee", AccessRightEnum.MANAGER, "manager", "password");
        staffEntitySessionBeanLocal.createNewStaff(admin);
        staffEntitySessionBeanLocal.createNewStaff(manager);
        String[] surnames = {"lim", "tan", "lee", "leck", "wong", "ong", "wei", "lee", "wee", "yong", "leao", "chen", "chee", "chong", "alec", "teo", "lin", "meng", "chua", "eng", "leong"};

        for (int i = 3; i <= 100; i++) {
            int randomInt = getRandom(surnames);
            StaffEntity adminToMake = new StaffEntity("admin " + i, surnames[randomInt], AccessRightEnum.ADMINISTRATOR, "admin" + surnames[randomInt], "password");
            staffEntitySessionBeanLocal.createNewStaff(adminToMake);
        }
        for (int i = 101; i <= 200; i++) {
            int randomInt = getRandom(surnames);
            StaffEntity managerToMake = new StaffEntity("manager " + i, surnames[randomInt], AccessRightEnum.MANAGER, "manager" + surnames[randomInt], "password");
            staffEntitySessionBeanLocal.createNewStaff(managerToMake);
        }

        UserEntity customer = new UserEntity("customer", "lee", "customer@mail.com", "customer", "password", new Date(), "NUS", RoleEnum.CUSTOMER);
        userEntitySessionBeanLocal.createNewUser(customer);
        for (int i = 1; i <= 200; i++) {
            int randomInt = getRandom(surnames);
            UserEntity customerToMake = new UserEntity("customer " + i, surnames[randomInt], "customer" + i + surnames[randomInt] + "@mail.com", "customer " + i, "password", new Date(), "NUS " + i, RoleEnum.CUSTOMER);
            userEntitySessionBeanLocal.createNewUser(customerToMake);
        }

        UserEntity seller = new UserEntity("seller", "lee", "seller@mail.com", "seller", "password", new Date(), "NUS", RoleEnum.SELLER);
        userEntitySessionBeanLocal.createNewUser(seller);
        for (int i = 200; i <= 500; i++) {
            int randomInt = getRandom(surnames);
            UserEntity sellerToMake = new UserEntity("seller " + i, surnames[randomInt], "seller " + i + surnames[randomInt] + "@mail.com", "seller" + surnames[randomInt], "password", new Date(), "NUS " + i, RoleEnum.SELLER);
            userEntitySessionBeanLocal.createNewUser(sellerToMake);
        }

    }

    private void initialiseAdvertisersAndAdvertisements() throws CreateNewAdvertisementException, CreateNewAdvertiserEntityException, AdvertiserEntityNotFoundException, AdvertiserEntityExistException, UnknownPersistenceException, InputDataValidationException {

        String[] surnames = {"lim", "tan", "lee", "leck", "wong", "ong", "wei", "lee", "wee", "yong", "leao", "chen", "chee", "chong", "alec", "teo", "lin", "meng", "chua", "eng", "leong"};

        for (int i = 1; i <= 20; i++) {
            int randomInt = getRandom(surnames);
            AdvertiserEntity advertiser = new AdvertiserEntity("advertiser " + i, "advertiser" + surnames[randomInt], "password", "advertiser" + surnames[randomInt] + "@mail.com");
            advertiserEntitySessionBeanLocal.createNewAdvertiserEntity(advertiser, new ArrayList<>(), new ArrayList<>());
            AdvertisementEntity advertisementEntity = new AdvertisementEntity("sample description");
            advertisementEntitySessionBeanLocal.createNewAdvertiserEntity(advertisementEntity, Long.valueOf(i));
        }

    }

    public int getRandom(String[] arr) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(arr.length);
        return randomIndex;

    }
}
