package joomla2plone.joomla.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = Configuration.TABLE_PREFIX + "users")
public class User {

	@Id
	private Long id;

	private String name;

	private String username;

	private String email;

	private String password;

	private Boolean block;

	private Boolean sendEmail;

	private Date registerDate;

	private Date lastvisitDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getBlock() {
		return block;
	}

	public void setBlock(Boolean block) {
		this.block = block;
	}

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getLastvisitDate() {
		return lastvisitDate;
	}

	public void setLastvisitDate(Date lastvisitDate) {
		this.lastvisitDate = lastvisitDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", email=" + email + ", password=" + password + ", block=" + block + ", sendEmail=" + sendEmail + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((sendEmail == null) ? 0 : sendEmail.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (sendEmail == null) {
			if (other.sendEmail != null)
				return false;
		} else if (!sendEmail.equals(other.sendEmail))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
