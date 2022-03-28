package ejb.session.stateless;

import entity.MessageOfTheDayEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.MessageOfTheDayNotFoundException;
import util.exception.UpdateMessageOfTheDayException;

@Stateless

public class MessageOfTheDayEntitySessionBean implements MessageOfTheDayEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MessageOfTheDayEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public MessageOfTheDayEntity createNewMessageOfTheDay(MessageOfTheDayEntity newMessageOfTheDayEntity) throws InputDataValidationException {
        Set<ConstraintViolation<MessageOfTheDayEntity>> constraintViolations = validator.validate(newMessageOfTheDayEntity);

        if (constraintViolations.isEmpty()) {
            entityManager.persist(newMessageOfTheDayEntity);
            entityManager.flush();

            return newMessageOfTheDayEntity;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<MessageOfTheDayEntity> retrieveAllMessagesOfTheDay() {
        Query query = entityManager.createQuery("SELECT motd FROM MessageOfTheDayEntity motd ORDER BY motd.motdId ASC");

        return query.getResultList();
    }

    @Override
    public MessageOfTheDayEntity deleteMessageOfTheDay(MessageOfTheDayEntity motd) {
        MessageOfTheDayEntity motdEntityToRemove = entityManager.find(MessageOfTheDayEntity.class, motd.getMotdId());
        entityManager.remove(motdEntityToRemove);
        return motd;
    }

    @Override
    public MessageOfTheDayEntity retrieveMessageOfTheDayById(Long motdId) throws MessageOfTheDayNotFoundException {
        MessageOfTheDayEntity motd = entityManager.find(MessageOfTheDayEntity.class, motdId);
        if (motd != null) {
            return motd;
        } else {
            throw new MessageOfTheDayNotFoundException("Message Of The Day Not Found, ID: " + motdId);
        }
    }

    @Override
    public MessageOfTheDayEntity updateMessageOfTheDay(MessageOfTheDayEntity motd) throws UpdateMessageOfTheDayException, MessageOfTheDayNotFoundException {
        MessageOfTheDayEntity motdToUpdate = retrieveMessageOfTheDayById(motd.getMotdId());
        
        if (motd != null && motd.getMotdId() != null && motdToUpdate != null) {
            

            if (motdToUpdate.getMotdId().equals(motd.getMotdId())) {
                entityManager.merge(motd); 
                return motdToUpdate;
            } else {
                throw new UpdateMessageOfTheDayException("ID of Message Of The Day to be updated does not match the existing record");
            }
        } else {
            throw new MessageOfTheDayNotFoundException("Message Of The Day ID cannot be found");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<MessageOfTheDayEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
