package si.fri.rsoteam;


import com.kumuluz.ee.discovery.annotations.RegisterService;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Videos",
                version = "v1.0.0",
                contact = @Contact(),
                license = @License(name = "none")
        ),
        servers = @Server(url = "http://52.226.192.49/videos"),
        security = @SecurityRequirement(name = "none"))
@RegisterService
@ApplicationPath("/v1")
public class MicroserviceVideos extends Application {

}