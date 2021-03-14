package joomla2plone.joomla.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = Configuration.TABLE_PREFIX + "viewlevels")
public class ViewLevel {

	@Id
	private Long id;

	private String title;

	private Long ordering;

	private String rules;

	@Transient
	private List<Group> groups;

	@Transient
	public List<Group> getGroups() {
		return groups == null ? groups = new ArrayList<Group>() : groups;
	}

	@Transient
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Transient
	public List<Long> getRulesIds() {
		List<Long> rulesIds = new ArrayList<Long>();

		String[] ids = getRules().replaceAll("\\[", "").replaceAll("\\]", "").split(",");

		for (String id : ids) {
			rulesIds.add(Long.valueOf(id));
		}

		return rulesIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getOrdering() {
		return ordering;
	}

	public void setOrdering(Long ordering) {
		this.ordering = ordering;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		ViewLevel other = (ViewLevel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ViewLevel [id=" + id + ", title=" + title + "]";
	}

}
