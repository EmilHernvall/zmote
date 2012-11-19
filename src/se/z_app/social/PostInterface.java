package se.z_app.social;

import java.util.Date;
/**
 * Interface that speficies the functions that are shared
 * between the the posts and comments to enable easier 
 * Implementation of the base adapter using the information.
 * @author Linus
 *
 */
public interface PostInterface {
	public boolean isPost();
	
	public String getUserName();

	public void setUserName(String userName);

	public String getContent();

	public void setContent(String content);
	
	public Date getDateOfCreation();
	
	public void setDateOfCreation(Date dateOfCreation);
	

}
