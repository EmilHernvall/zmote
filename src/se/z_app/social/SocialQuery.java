package se.z_app.social;


public class SocialQuery {
	
	/**
	 * Get the services, TODO: Create this generic, hard-coded for the moment
	 * @return An array with the we
	 */
	public SocialService[] getServices(){
		SocialService[] services = new SocialService[1];
		SocialService facebookService = new SocialService();
//		facebookService.setIcon(icon);
//		facebookService.setIconURL(iconURL);
//		facebookService.setID(id);
		facebookService.setName("facebook");
		services[0] = (facebookService);
		return services;
	}
	
}
