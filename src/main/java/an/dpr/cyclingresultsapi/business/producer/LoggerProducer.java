package an.dpr.cyclingresultsapi.business.producer;


import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class LoggerProducer {
   
   @Produces Logger createLogger(final InjectionPoint ip){
       return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
   }


}