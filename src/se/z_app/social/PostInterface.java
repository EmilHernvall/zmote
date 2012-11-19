package se.z_app.social;

import java.util.Date;

public interface PostInterface {

	
	public String getUserName();

	public void setUserName(String userName);

	public String getContent();

	public void setContent(String content);
	
	public Date getDateOfCreation();
	
	public void setDateOfCreation(Date dateOfCreation);
	

}
