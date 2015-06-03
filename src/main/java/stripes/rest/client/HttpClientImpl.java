package stripes.rest.client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Objects;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stripes.rest.client.util.constant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author Dilaj
 *
 */
public class HttpClientImpl implements HttpClient{

	private CloseableHttpClient httpclient = HttpClients.createDefault();
	private static Logger LOGGER = LoggerFactory.getLogger(HttpClientImpl.class);
	
	@Override
	public void login(final String username, final String password) {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(String.format("%s:%s", username,password)));
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setCredentialsProvider(credentialsProvider);
	}
	
	@Override
	public void uploadFile(final String rid, final String clazz, final String field, final String filePath) {

		if(Objects.nonNull(filePath) && Objects.nonNull(filePath) && Objects.nonNull(field)){

			File file = new File(filePath);
			HashMap<String,Object> metaData = new HashMap<>();
			metaData.put(constant.FILE_NAME,file.getName());
			metaData.put(constant.AT_CLASS,clazz);
			metaData.put(constant.BField,field);
			
			if(rid != null){
				metaData.put(constant.AT_RID,rid);
			}
			
			LOGGER.info("File to be uploaded : {} ",toJson(metaData));
			
			String  url = "http://localhost:8080/stripes/binarydata/upload";
			//String  url = "http://dilaj.j.layershift.co.uk/fmanager/binarydata/upload";
				
			HttpPost method = new HttpPost(url);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody(constant.DATA, toJson(metaData), ContentType.APPLICATION_FORM_URLENCODED);
			builder.addBinaryBody(constant.FILE_BEAN, file);
			HttpEntity entity =  builder.build();
			method.setEntity(entity);

			try {
				CloseableHttpResponse response = httpclient.execute(method);
				String _response = IOUtils.toString(response.getEntity().getContent()); 
				LOGGER.info("Response from the server : {} ",_response);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	
	}
	
	@Override
	public void downloadFile(final String metaDetail) {
		//http://dilaj.j.layershift.co.uk/fmanager
		String  url = "http://localhost:8080/stripes/binarydata/download";
		//String  url = "http://dilaj.j.layershift.co.uk/fmanager/binarydata/download";
		HttpPost method = new HttpPost(url);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(constant.DATA, metaDetail, ContentType.APPLICATION_FORM_URLENCODED);
		HttpEntity entity =  builder.build();
		method.setEntity(entity);

		try {
			CloseableHttpResponse response = httpclient.execute(method);
			entity = response.getEntity();
			
			HeaderElement[] hr = response.getFirstHeader(constant.CONTENT_DISPOSITION).getElements();
			String fileName = hr[0].toString().split("=")[1];
			String filePath = fromJson(metaDetail).get(constant.FILE_PATH).toString();
			String downloadPath = String.format(constant.DOWNLOAD_PATH, filePath,fileName);
			OutputStream outstream = new FileOutputStream(new File(downloadPath));
			LOGGER.info("File to be downloaded : {} | Path ; {} ",fileName,downloadPath,filePath);
			entity.writeTo(outstream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * convert string to a json node
	 * @param data
	 * @return
	 */
	private HashMap<String,Object> fromJson(final String data){	
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
			return mapper.readValue(data, typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * return the json string
	 * @param metaData
	 * @return
	 */
	private String toJson(final HashMap<String,Object> metaData){
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(metaData);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}

	/**
	 * execute the post request
	 */
	@Override
	public String doPost(final String data) {
		String  url = "http://localhost:8080/stripes/post";
		HttpPost method = new HttpPost(url);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(constant.DATA, data, ContentType.APPLICATION_FORM_URLENCODED);
		HttpEntity entity =  builder.build();
		method.setEntity(entity);
		
		try {
			CloseableHttpResponse response = httpclient.execute(method);
			String _response = IOUtils.toString(response.getEntity().getContent()); 
			LOGGER.info("Response from the server : {} ",_response);
			return _response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
