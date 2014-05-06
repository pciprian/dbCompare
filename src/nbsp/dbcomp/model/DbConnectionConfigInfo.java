/**
 * 
 */
package nbsp.dbcomp.model;

/**
 * @author pciprian
 *
 */
public class DbConnectionConfigInfo {
	
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String authConnString = "jdbc:mysql://%s:%s/auth";
	private static String charactersConnString = "jdbc:mysql://%s:%s/characters";
	
	private String host;
	private String port;
	private String user;
	private String pass;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getDriverName() {
		return driverName;
	}
	
	public String getAuthDbConnectionUrl() {
		return String.format(authConnString, host, port);
	}
	
	public String getCharactersDbConnectionUrl() {
		return String.format(charactersConnString, host, port);
	}
}
