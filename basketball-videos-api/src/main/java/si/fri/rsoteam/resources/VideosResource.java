package si.fri.rsoteam.resources;

import si.fri.rsoteam.lib.dtos.VideoDto;
import si.fri.rsoteam.services.beans.VideosBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideosResource {

    private Logger log = Logger.getLogger(VideosResource.class.getName());

    @Inject
    private VideosBean videosBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getObjects() {
        return Response.ok(videosBean.getAllVideos()).build();
    }

    @GET
    @Path("/{objectId}")
    public Response getEventById(@PathParam("objectId") Integer id) {
        return Response.ok(videosBean.getVideo(id)).build();
    }

    @POST
    public Response createEvent(VideoDto videoDto) {
        return Response.status(201).entity(videosBean.createVideo(videoDto)).build();
    }

    @PUT
    @Path("{objectId}")
    public Response updateEvent(@PathParam("objectId") Integer id, VideoDto eventDto) {
        return Response.status(201).entity(videosBean.updateVideo(eventDto, id)).build();
    }

    @DELETE
    @Path("{objectId}")
    public Response deleteEvent(@PathParam("objectId") Integer id) {
        videosBean.deleteVideo(id);
        return Response.status(204).build();
    }
}