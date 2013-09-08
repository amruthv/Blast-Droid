package com.example.blast2;

import com.google.gson.annotations.SerializedName;

public class Blast {
	@SerializedName("CONTENT")
	public String content;
	@SerializedName("TIME")
	public String time;
	@SerializedName("GPS")
	public String gps;
	@SerializedName("USERID")
	public String username;
	@SerializedName("LOCATION")
	public String location;
	  
	public String toString() {
		return username + ": " + content + " at " + location;
	}
	
	public String formatted() {
//		return "<div style=\"font-weight:bold\">This is bold text</div><div style=\"font-weight:bold\">Div 2</div>";
		return String.format("<div><b> %s </b>: %s </div><div> at %s</div>",username, content, location); 
	}

}
