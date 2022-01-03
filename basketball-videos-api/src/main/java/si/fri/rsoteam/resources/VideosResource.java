package si.fri.rsoteam.resources;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rsoteam.config.ConfigProperties;
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


@ApplicationScoped
@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideosResource {

    @Inject
    private VideosBean videosBean;

    @Inject
    private ConfigProperties configProperties;

    @Context
    protected UriInfo uriInfo;

    @Inject
    @Metric(name = "video_tags_histogram")
    Histogram histogram;

    @GET
    @Operation(summary = "Get list of videos", description = "Returns list of videos.")
    @APIResponses({
            @APIResponse(
                    description = "videos list",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = VideoDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )
    })
    @Log(LogParams.METRICS)
    public Response getVideos() {
        return Response.ok(videosBean.getAllVideos()).build();
    }

    @GET
    @Operation(summary = "Get specific video by id", description = "Returns specific video by id or raises an error.")
    @APIResponses({
            @APIResponse(
                    description = "Successfully returns video",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = VideoDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            ),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @Path("/{objectId}")
    @Log(LogParams.METRICS)
    @Counted(name = "played_video_counter")
    @Metered(name = "played_video_meter")
    public Response getVideoById(@PathParam("objectId") Integer id) {
        if (!configProperties.getBooleanProperty())
            return Response.ok(videosBean.getVideo(id)).build();
        else
            return Response.serverError().build();
    }

    @POST
    @Operation(summary = "Create new video", description = "Creates new video.")
    @APIResponses({
            @APIResponse(
                    description = "Successfully creates video",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = VideoDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "")}
            )
    })
    @Log(LogParams.METRICS)
    @Counted(name = "created_video_counter")
    @Timed(name = "create_video_timer")
    public Response createVideo(VideoDto videoDto) {
        histogram.update(videoDto.tags.size());
        return Response.status(201).entity(videosBean.createVideo(videoDto)).build();
    }

    @PUT
    @Operation(summary = "Update specific video.", description = "Updates specific video by id.")
    @APIResponses({
            @APIResponse(
                    description = "Successfully returns video",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = VideoDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            ),
    })
    @Path("{objectId}")
    @Log(LogParams.METRICS)
    public Response updateVideo(@PathParam("objectId") Integer id, VideoDto eventDto) {
        return Response.status(201).entity(videosBean.updateVideo(eventDto, id)).build();
    }

    @DELETE
    @Operation(summary = "Delete specific video.", description = "Deletes specific video by id.")
    @APIResponses({
            @APIResponse(
                    description = "Successfully deleted video",
                    responseCode = "204",
                    content = @Content(schema = @Schema(implementation = VideoDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            ),
    })
    @Path("{objectId}")
    @Log(LogParams.METRICS)
    public Response deleteEvent(@PathParam("objectId") Integer id) {
        videosBean.deleteVideo(id);
        return Response.status(204).build();
    }
}
