package pl.com.revolut.common.utils.web;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public final class WebUtils {
    private WebUtils(){}

    public static URI generateUri(UriInfo uriInfo , String id){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(id);
        return builder.build();
    }

}
