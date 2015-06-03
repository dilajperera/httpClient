package stripes.rest.client;

public interface HttpClient {

	void login(String username, String password);
	void uploadFile(String rid, String clazz, String field, String filePath);
	void downloadFile(String metaDetail);
	String doPost(String data);
}
