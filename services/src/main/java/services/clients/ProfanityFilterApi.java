package services.clients;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.Dependent;
import javax.ws.rs.*;
import java.util.concurrent.CompletionStage;

@Path("containsprofanity")
@RegisterRestClient(configKey="profanity-api")
@Dependent
public interface ProfanityFilterApi {

    @GET
    @ClientHeaderParam(name="Accept",value="text/plain")
    CompletionStage<String> checkProfanity(@QueryParam("text")String comment);
}
