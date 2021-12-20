package v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

@RegisterService
@ApplicationPath("/v1")
public class CommentsApplication extends Application {

}
