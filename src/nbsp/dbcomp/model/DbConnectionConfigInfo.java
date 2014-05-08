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
	
	public DbConnectionConfigInfo() {
		this.host = "";
		this.port = "";
		this.user = "";
		this.pass = "";
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		if (host == null) {
			this.host = "";
		} else {
			this.host = host;
		}
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		if (port == null) {
			this.port = "";
		} else {
			this.port = port;
		}
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		if (user == null) {
			this.user = "";
		} else {
			this.user = user;
		}
	}
	
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		if (pass == null) {
			this.pass = "";
		} else {
			this.pass = pass;
		}
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
