package pl.com.revolut.common.utils.impl;

import pl.com.revolut.common.exception.NullParameterException;

public final class ServiceImplUtils {
    private ServiceImplUtils(){}
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServiceImplUtils.class);

    public static void  checkParameters(Object ...paramters) throws NullParameterException {
        for (Object parameter : paramters)
            if(parameter == null)
            {
                logger.error("Parameter is null");
                throw new NullParameterException(new StringBuilder().append("PARAMETER IS NULL").toString());
            }
    }
}
