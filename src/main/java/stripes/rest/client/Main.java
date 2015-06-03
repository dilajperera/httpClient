package stripes.rest.client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *@author Dilaj
 *
 */
public class Main 
{
	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args )
    {
    	HttpClientImpl client =  new HttpClientImpl();
       client.login("admin", "admin");
    //client.uploadFile(null, "OData","b_data", "C:/Users/Dilaj/Desktop/123.txt");
  
      // client.doPost(data);

       String data = "{'@rid':'#16:80','bField': 'b_data', 'path': 'C:/Users/Dilaj/Desktop/testDownload'}";
       long startTime = System.currentTimeMillis();
       client.downloadFile(data);
       long endTime = System.currentTimeMillis();

       LOGGER.info("That took " + (endTime - startTime)/1000 + "seconds");
    }
}
