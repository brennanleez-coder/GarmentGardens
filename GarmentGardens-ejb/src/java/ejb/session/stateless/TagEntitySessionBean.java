/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TagEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;

/**
 *
 * @author wong
 */
@Stateless
public class TagEntitySessionBean implements TagEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public TagEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public TagEntity createNewTagEntity(TagEntity newTagEntity) throws InputDataValidationException, CreateNewTagException
    {
        Set<ConstraintViolation<TagEntity>>constraintViolations = validator.validate(newTagEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                entityManager.persist(newTagEntity);
                entityManager.flush();

                return newTagEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewTagException("Tag with same name already exist");
                }
                else
                {
                    throw new CreateNewTagException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {                
                throw new CreateNewTagException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<TagEntity> retrieveAllTags()
    {
        Query query = entityManager.createQuery("SELECT t FROM TagEntity t ORDER BY t.name ASC");
        List<TagEntity> tagEntities = query.getResultList();
        
        for(TagEntity tagEntity:tagEntities)
        {            
            tagEntity.getProducts().size();
        }
        
        return tagEntities;
    }
    
    
    
    @Override
    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException
    {
        TagEntity tagEntity = entityManager.find(TagEntity.class, tagId);
        
        if(tagEntity != null)
        {
            return tagEntity;
        }
        else
        {
            throw new TagNotFoundException("Tag ID " + tagId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public void updateTag(TagEntity tagEntity) throws InputDataValidationException, TagNotFoundException, UpdateTagException
    {
        Set<ConstraintViolation<TagEntity>>constraintViolations = validator.validate(tagEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(tagEntity.getTagId()!= null)
            {
                TagEntity tagEntityToUpdate = retrieveTagByTagId(tagEntity.getTagId());
                
                Query query = entityManager.createQuery("SELECT t FROM TagEntity t WHERE t.name = :inName AND t.tagId <> :inTagId");
                query.setParameter("inName", tagEntity.getName());
                query.setParameter("inTagId", tagEntity.getTagId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateTagException("The name of the tag to be updated is duplicated");
                }
                
                tagEntityToUpdate.setName(tagEntity.getName());                               
            }
            else
            {
                throw new TagNotFoundException("Tag ID not provided for tag to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException
    {
        TagEntity tagEntityToRemove = retrieveTagByTagId(tagId);
        
        if(!tagEntityToRemove.getProducts().isEmpty())
        {
            throw new DeleteTagException("Tag ID " + tagId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            entityManager.remove(tagEntityToRemove);
        }                
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TagEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
