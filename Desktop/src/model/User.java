package model;

public class User {
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Integer roleid;
	
	public User(Integer id, String name, String email, String password, Integer roleid) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.roleid = roleid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
