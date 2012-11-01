package se.z_app.zmote.epg;


import java.util.Date;
import java.util.LinkedList;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;

public class EPGQuery {
	
	
	public EPG getEPG() {
		return EPGContentHandler.instance().getEPG();
	}	
	
	public Channel getCurrentChannel(){
		return EPGContentHandler.instance().getCurrentChannel();
	}
	
	public Channel getChannel(int nr){
		EPG epg = EPGContentHandler.instance().getEPG();
		return epg.getChannel(nr);
	}
	
	public Program[] searchProgram(String name){
		LinkedList<Program> programs = new LinkedList<Program>();
		EPG epg = EPGContentHandler.instance().getEPG();
		
		for (Channel channel : epg) {
			for (Program program : channel) {
				if(program.getName().toLowerCase().contains(name.toLowerCase()))
					programs.add(program);
			}
		}
		Program programArray[] = new Program[programs.size()];
		programs.toArray(programArray);
		return programArray;
	}
	
	
	public Program[] searchProgram(Date Start, long toleranceInMilliseconds){
		Date min = new Date(System.currentTimeMillis()-toleranceInMilliseconds);
		Date max = new Date(System.currentTimeMillis()+toleranceInMilliseconds);
		
		LinkedList<Program> programs = new LinkedList<Program>();
		EPG epg = EPGContentHandler.instance().getEPG();
		
		for (Channel channel : epg) {
			for (Program program : channel) {
				if(program.getStart().compareTo(max) <= 0 && program.getStart().compareTo(min) >= 0)
					programs.add(program);
			}
		}
		
		Program programArray[] = new Program[programs.size()];
		programs.toArray(programArray);
		
		
		return programArray;
	}
	
	
	
	public Program[] getActivePrograms(){
		Date now = new Date(System.currentTimeMillis());
		
		LinkedList<Program> programs = new LinkedList<Program>();
		EPG epg = EPGContentHandler.instance().getEPG();
		
		for (Channel channel : epg) {
			Program lastProgram = null;
			for (Program program : channel) {
				if(program.getStart().compareTo(now) <= 0)
					lastProgram = program;
				else
					break;
			}
			if(lastProgram != null)
				programs.add(lastProgram);
		}
		Program programArray[] = new Program[programs.size()];
		programs.toArray(programArray);
		
		
		return programArray;
	}
	
	
}
