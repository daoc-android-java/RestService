
package pck;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Component;

/**
 *
 * @author dordonez@ute.edu.ec
 */
@Component
@Path("/")
public class TestController {
    private String DIR;
    
    @GET
    @Path("now")
    @Produces("text/plain")
    public String getDateTime() {
        Date date  = new Date();
        System.out.println("### " + date);
        return date.toString();
    }
    
    @GET
    @Path("{filename}")
    @Produces("application/octet-stream") // ò MediaType.APPLICATION_OCTET_STREAM
    public Response getFile(@PathParam("filename") String filename) {
        try {
            return Response.
                status(200).
                entity(new FileInputStream(new File(DIR + filename))).
                build(); 
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.
                status(403).
                entity("No se puede enviar " + filename).
                build();
        }
    }    
    
    @POST
    @Path("{filename}")
    @Consumes("application/octet-stream") // ò MediaType.APPLICATION_OCTET_STREAM
    public Response postFile(InputStream in, @PathParam("filename") String filename) {    
        try {        
            Files.copy(in, new File(DIR + filename).toPath());
            return Response.status(200).entity("Recibido: " + filename).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(500).entity("No se puede guardar el archivo: " + filename).build();
        }
    }    
    
    @PostConstruct
    public void initialize() {
        File f = new File("./uploaded");
        if(!f.exists()) {
            f.mkdir();
        }
        String currentPath = f.getAbsolutePath();
        DIR = "file:///" + currentPath + "/";
    }
}
