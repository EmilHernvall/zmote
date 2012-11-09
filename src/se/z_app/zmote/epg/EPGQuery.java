package se.z_app.zmote.epg;


import java.util.Date;
import java.util.LinkedList;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;

/**
 * Class that queries the EPG for programs either currently active
 * 	or containing a certain string
 * @author ?
 *
 */
public class EPGQuery {
	
	/**
	 * Get the current EPG
	 * @return the EPG
	 */
	public EPG getEPG() {
		return EPGContentHandler.instance().getEPG();
	}	
	
	/**
	 * Get the current channel
	 * @return the current channel
	 */
	public Channel getCurrentChannel(){
		return EPGContentHandler.instance().getCurrentChannel();
	}
	
	/**
	 * Get a certain channel
	 * @param The number of the channel
	 * @return The channel on that number
	 */
	public Channel getChannel(int nr){
		EPG epg = EPGContentHandler.instance().getEPG();
		return epg.getChannel(nr);
	}
	
	/**
	 * Get an array of programs containing a certain String
	 * @param The string the programs should contain
	 * @return An array of Programs
	 */
	public Program[] searchProgram(String name){
		LinkedList<Program> programs = new LinkedList<Program>();
		EPG epg = EPGContentHandler.instance().getEPG();
		
		for (Channel channel : epg) {
			for (Program program : channel) {
				if(program.getName().toLowerCase().contains(name.toLowerCase())) {
					programs.add(program);
				}
			}
		}
		Program programArray[] = new Program[programs.size()];
		programs.toArray(programArray);
		return programArray;
	}
	
	/**
	 * Get an array of programs running in a certain time interval
	 * @param The time interval
	 * @return An array of Programs
	 */
	public Program[] searchProgram(Date Start, long toleranceInMilliseconds){
		Date min = new Date(System.currentTimeMillis()-toleranceInMilliseconds);
		Date max = new Date(System.currentTimeMillis()+toleranceInMilliseconds);
		
		LinkedList<Program> programs = new LinkedList<Program>();
		EPG epg = EPGContentHandler.instance().getEPG();
		
		for (Channel channel : epg) {
			for (Program program : channel) {
				if(program.getStart().compareTo(max) <= 0 && program.getStart().compareTo(min) >= 0) {
					programs.add(program);
				}
			}
		}
		
		Program programArray[] = new Program[programs.size()];
		programs.toArray(programArray);
		
		
		return programArray;
	}
	
	/**
	 * Get the currently active programs
	 * @return an array of Programs
	 */
	public Program[] getActivePrograms(){
		Date now = new Date(System.currentTimeMillis());
		
		LinkedList<Program> programs = new LinkedList<Program>();
		EPG epg = EPGContentHandler.instance().getEPG();
		
		for (Channel channel : epg) {
			Program lastProgram = null;
			for (Program program : channel) {
				if(program.getStart().compareTo(now) <= 0) {
					lastProgram = program;
				}
				else {
					break;
				}
			}
			if(lastProgram != null) {
				programs.add(lastProgram);
			}
		}
		Program programArray[] = new Program[programs.size()];
		programs.toArray(programArray);
		
		
		return programArray;
	}
	
	
}
