package jaxrs.stream.streamingoutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

//Content-Disposition: inline								//to read in browser
//Content-Disposition: attachment							//to download
//Content-Disposition: attachment; filename="filename.ext"	//Otherwise file name is @Path's value

//https://docs.oracle.com/javaee/7/api/javax/ws/rs/core/StreamingOutput.html
//-A type that may be used as a resource method return value or as the entity in a Response when the application wishes to stream the output.
// This is a lightweight alternative to a MessageBodyWriter.
@Path("streamingOutput")
public class StreamingOutputFile {
	
	@GET
	@Path("file")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response streamingOut() {
		
		String name = "alice.txt";
		InputStream resource = getClass().getResourceAsStream("/"+name);
		
		StreamingOutput stream = new StreamingOutput() {
		    @Override
		    public void write(OutputStream os) throws IOException, WebApplicationException {
		    	byte[] buffer = new byte[4096];
		    	int buffreSize;

		    	while ((buffreSize = resource.read(buffer)) != -1)
		    	os.write(buffer, 0, buffreSize);
		    }
		};
		
		return Response.ok(stream)
			.header("Content-Disposition", "inline")
			//.header("Content-Disposition", "attachment; filename="+name)
			.build();
	}
	
	
	@GET
	@Path("file2")
	//@Produces(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response streamingOut1() {
		String name = "alice.txt";
		URL resource = getClass().getResource("/"+name);
		
		StreamingOutput stream = new StreamingOutput() {
		    @Override
		    public void write(OutputStream os) throws IOException, WebApplicationException {
		    	java.nio.file.Path path = Paths.get(URI.create(resource.toExternalForm()));
                byte[] data = Files.readAllBytes(path);
                os.write(data);
                os.flush();
		    }
		};
		
		return Response.ok(stream)
			//.header("Content-Disposition", "inline")
			.header("Content-Disposition", "attachment; filename="+name)
			.build();
	}
}
