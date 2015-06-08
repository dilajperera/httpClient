package stripes.rest.client;

public interface HttpClient {

	void login(String username, String password);
	void uploadFile(String rid, String clazz, String field, String filePath);
	String doPost(String data);
	void downloadFile(String rid, String field, String downloadPath);
}
