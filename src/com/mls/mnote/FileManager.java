package com.mls.mnote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Environment;

public class FileManager {
	final static String path = "/mnotes";

	public static boolean save(String filename, String data) {

		if(!checkDirectory()){
			return false;
		}

		File file = new File(Environment.getExternalStorageDirectory().getPath() + path + "/" + filename);

		if (file.isFile()){

			//Success
		}
		else{
			try {
				if (!file.createNewFile()){
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		try {
			FileWriter writer = new FileWriter(file);

			writer.write(data);

			writer.close();


		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


		return true;

	}

	public static boolean checkDirectory(){
		File dir = new File(Environment.getExternalStorageDirectory().getPath() + path);
		if(dir.exists() && dir.isDirectory()) {
			return true;
		}
		else{
			return dir.mkdir();
		}
	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		//Make sure you close all streams.
		fin.close();        
		return ret;
	}

	public static String load(String filename) {
		try {
			return getStringFromFile(Environment.getExternalStorageDirectory().getPath() + path + "/" + filename);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
