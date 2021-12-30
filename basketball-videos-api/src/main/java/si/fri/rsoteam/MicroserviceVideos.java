package si.fri.rsoteam;


import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService(value = "basketball-videos")
@ApplicationPath("/v1")
public class MicroserviceVideos extends Application {

}