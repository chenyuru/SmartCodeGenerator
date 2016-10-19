package com.shiyanlou.example;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shiyanlou.example.Struct.DataMember;

public class Main {

	public static void main(String[] args) throws IOException {
		Struct struct = new Struct("struct.txt");
		String className =struct.getStructName();
		String classNameLow=className.toLowerCase();
		String classNameLows=className.toLowerCase()+"s";
		BufferedReader br=new BufferedReader(new FileReader(new File("array.txt")));
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line+"\n");
		}
		String srccode=sb.toString();
		srccode=srccode.replaceAll("<ClassName>", className);
		srccode=srccode.replaceAll("<ClassNameLows>", classNameLows);
		srccode=srccode.replaceAll("<ClassNameUp>", className.toUpperCase());
		
		for(int i=0;i<struct.getDatas().size();i++){
			srccode =srccode + struct.getCompareFunc(i) +"\n";
			srccode =srccode + struct.getFindFunc(i) +"\n";
			srccode =srccode + struct.getSortFunc(i) +"\n";
			srccode =srccode + struct.getAddFunc() +"\n";
		}
		
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("out.txt")));
		bw.write(srccode);
		br.close();
		bw.close();
	}
	
	
	

}


