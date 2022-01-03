package si.fri.rsoteam.resources;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import si.fri.rsoteam.config.ConfigProperties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;

@RequestScoped
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @Inject
    private ConfigProperties properties;

    @GET
    @Path("/config")
    @Log(LogParams.METRICS)
    public Response test() {
        String response =
                "{" +
                        "\"stringProperty\": \"%s\"," +
                        "\"booleanProperty\": %b," +
                        "\"integerProperty\": %d" +
                        "}";

        response = String.format(
                response,
                properties.getStringProperty(),
                properties.getBooleanProperty(),
                properties.getIntegerProperty());

        return Response.ok(response).build();
    }

    @Inject
    @DiscoverService(value = "basketball-users")
    private Optional<String> userServiceUrl;

    @GET
    @Path("/discovery")
    @Log(LogParams.METRICS)
    public Response discovery() {
        if (userServiceUrl != null && userServiceUrl.isPresent()) {
            String response = userServiceUrl.get();
            return Response.ok(response).build();
        }
        return Response.ok("no users service detected").build();
    }
}