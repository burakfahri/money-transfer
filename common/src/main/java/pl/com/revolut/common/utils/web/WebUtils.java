package pl.com.revolut.common.utils.web;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public final class WebUtils {
    private WebUtils(){}

    /**
     * used for web services while an item created
     * @param uriInfo of the called service
     * @param id of the new item
     * @return the uri of the new item in the system
     */
    public static URI generateUri(UriInfo uriInfo , String id){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(id);
        return builder.build();
    }

}
