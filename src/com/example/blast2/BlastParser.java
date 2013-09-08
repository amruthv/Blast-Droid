package com.example.blast2;

import java.net.*;
import java.io.*;
import com.google.gson.*;
public class BlastParser {
	public String address;
	public BlastParser(String url) {
		this.address = url;
	}
	
	public Blast[] parse() throws IOException {
		
        StringBuilder content = new StringBuilder();
        try {
    		URL url = new URL(address);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null)
                content.append(line + "\n");
            bufferedReader.close();
        }
        catch (Exception e) {
        	content.append("failed to get json");
            e.printStackTrace();
        }
         
        String content2 = content.toString();
        System.out.println(content2);

		Gson gson = new Gson();
		Blast[] blast = gson.fromJson(content2.toString(), Blast[].class);
		return blast;
	}
}
