package ws.restful;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;



@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application
{
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        
        resources.add(MultiPartFeature.class);
        
        return resources;
    }

    
    
    private void addRestResourceClasses(Set<Class<?>> resources){
        resources.add(ws.restful.CartResource.class);
        resources.add(ws.restful.CategoryResource.class);
        resources.add(ws.restful.CorsFilter.class);
        resources.add(ws.restful.CreditCardResource.class);
        resources.add(ws.restful.DisputeResource.class);
        resources.add(ws.restful.FileResource.class);
        resources.add(ws.restful.MotdResource.class);
        resources.add(ws.restful.ProductResource.class);
        resources.add(ws.restful.RatingResource.class);
        resources.add(ws.restful.RewardResource.class);
        resources.add(ws.restful.StaffResource.class);
        resources.add(ws.restful.TagResource.class);
        resources.add(ws.restful.UserResource.class);
    }    
}
