package com.netsavvies.cyberoam.backend;

public class UserDetails {

	// private variables
	int _priority;
	String _id;
	String _password;
	int _checked;

	// Empty constructor
	public UserDetails() {

	}

	// constructor
	public UserDetails(int _priority, String _id, String _password, int _checked) {
		this._priority = _priority;
		this._id = _id;
		this._password = _password;
		this._checked = _checked;
	}

	// constructor
	public UserDetails(String _id, String _password, int _checked) {
		this._id = _id;
		this._password = _password;
		this._checked = _checked;
	}

	// getting priority
	public int getPriority() {
		return this._priority;
	}

	// setting priority
	public void setPriority(int _priority) {
		this._priority = _priority;
	}

	// getting id
	public String getId() {
		return this._id;
	}

	// setting id
	public void setId(String id) {
		this._id = id;
	}

	// getting password
	public String getPassword() {
		return this._password;
	}

	// setting password
	public void setPassword(String _password) {
		this._password = _password;
	}

	// getting checked
	public int getChecked() {
		return this._checked;
	}

	// setting checked
	public void setChecked(int _checked) {
		this._checked = _checked;
	}
	
	
	public void copy(UserDetails user)
	{
		this._id=user._id;
		this._checked=user._checked;
		this._priority=user._priority;
		this._password=user._password;
	}
}
