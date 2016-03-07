package an.dpr.cyclingresultsapi.cdi.interceptor;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@Logged
@Interceptor
public class LoggedInterceptor implements Serializable {
    
    @Inject private Logger log;

    private static final long serialVersionUID = 1L;
    @AroundInvoke
    public Object logMethodEntry(InvocationContext invocationContext)
            throws Exception {
        log.debug("Entering method: "
                + invocationContext.getMethod().getName() + " in class "
                + invocationContext.getMethod().getDeclaringClass().getName());

        Object ret = invocationContext.proceed();
        
        log.debug("Exiting method: "
                + invocationContext.getMethod().getName() + " in class "
                + invocationContext.getMethod().getDeclaringClass().getName());
        
        return ret;
    }

}
