package stripes.rest.client;

/**
 *@author Dilaj
 *
 */
public class Main 
{
	

    public static void main( String[] args )
    {
    	HttpClientImpl client =  new HttpClientImpl();
       client.login("admin", "admin");
       //client.uploadFile(null, "OData","b_data", "C:/Users/dperera/Desktop/dilaj.zip");
      // client.doPost(data);
       client.downloadFile("#35:21","b_data","C:/Users/dperera/Desktop/testDownload");
    }
    
    
}
