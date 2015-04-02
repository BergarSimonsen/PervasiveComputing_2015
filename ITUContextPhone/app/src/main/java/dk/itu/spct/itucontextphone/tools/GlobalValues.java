package dk.itu.spct.itucontextphone.tools;

import dk.itu.spct.itucontextphone.service.ContextService;

/**
 * Created by bs on 4/2/15.
 */
public class GlobalValues {

    private static GlobalValues instance;
    public static GlobalValues getInstance() {
        if(instance == null)
            instance = new GlobalValues();
        return instance;
    }

    private ContextService service;

    public ContextService getService() {
        return service;
    }

    public void setService(ContextService service) {
        this.service = service;
    }
}
