package org.graphspace.javaclient.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * A GSGroup stores the details of a group that is understood by GraphSpace.
 * It holds the information about the group such as name and description.
 * It provides methods to define, modify and delete the details of the group.
 * @author rishabh
 *
 */
public class GSGroup{
	
	/**
	 * Name of group
	 */
	String name;
	
	/**
	 * Description of group
	 */
	String description;
	
	/**
	 * Constructor to create a GSGroup without name or description
	 */
	public GSGroup() {
		
	}
	
	/**
	 * Constructor to create a GSGroup with name and description
	 * @param name(String) Name of group 
	 * @param description(String) Description of group
	 */
	public GSGroup(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Get name of the group
	 * @return Name of the group
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get description of the group
	 * @return Description of the group
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Set name of the group
	 * @param name(String) name of the group
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Set description of the group
	 * @param description(String) description of the group
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns json object created from the group
	 * @return Group json object
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> group = new HashMap<String, Object>();
		group.put("name", name);
		group.put("description", description);
		return group;
	}
}