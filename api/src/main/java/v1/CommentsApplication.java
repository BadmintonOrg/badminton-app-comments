package v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(info = @Info(title = "Badminton app comments API", version = "v1",
        contact = @Contact(email = "rso@fri.uni-lj.si"),
        license = @License(name = "dev"), description = "API for Badminton app comments."),
        servers = @Server(url = "http://20.62.136.0:8080/")) //TO-DO
@ApplicationPath("/v1")
public class CommentsApplication extends Application {

}
