package com.shiyanlou.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Struct {
	
	private String structName;
	private ArrayList<DataMember> datas = new ArrayList<Struct.DataMember>();
	public Struct(String filename) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(new File(filename)));
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line);
		}
		String src = sb.toString();
		String[] fs =src.split("\\{");
		structName =fs[0].split(" ")[2];
		String inter =fs[1].split("\\}")[0];
		String[] rawDatas =inter.split("\\;\\s");
		Pattern single = Pattern.compile("\\s*(\\w+)\\s+(\\w+);*\\s*");
		Pattern array = Pattern.compile("\\s*(\\w+)\\s+(\\w+)\\[([0-9]+)\\];*\\s*");
		Matcher m;
		for(String str : rawDatas){
			DataMember dm = new DataMember();
			m= single.matcher(str);
			if(m.matches()){
				dm.setDataType(m.group(1));
				dm.setDataName(m.group(2));
				dm.setNum(1);
			}else{
				m =array.matcher(str);
				if(m.matches()){
					dm.setDataType(m.group(1));
					dm.setDataName(m.group(2));
					dm.setNum(Integer.parseInt(m.group(3)));
				}
			}
			datas.add(dm);
		}
	}
	
	public String getCompareFunc(int i) throws IOException{
		DataMember dm = datas.get(i);
		BufferedReader br= null;

		if(dm.getNum()==1){
			br=new BufferedReader(new FileReader(new File("compareSingleTemplate.txt")));
		}else if(dm.getDataType().equals("char")){
			br=new BufferedReader(new FileReader(new File("compareStringTemplate.txt")));
		}else{
			br.close();
			return null;
		}
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line+"\n");
		}
		String src = sb.toString();
		src=src.replaceAll("<ClassName>", structName);
		src=src.replaceAll("<dataName>", dm.getDataName()); 
		br.close();
		return src;
	}
	
	public String getSortFunc(int i) throws IOException{
		DataMember dm = datas.get(i);
		BufferedReader br= null;
		br=new BufferedReader(new FileReader(new File("sort.txt")));
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line+"\n");
		}
		String src = sb.toString();
		src=src.replaceAll("<ClassName>", structName);
		src=src.replaceAll("<ClassNameLows>", structName.toLowerCase()+"s");
		src=src.replaceAll("<ClassNameLow>", structName.toLowerCase());
		src=src.replaceAll("<dataName>", dm.getDataName());
		br.close();
		return src;
		
	}
	
	public String getFindFunc(int i) throws IOException{
		DataMember dm = datas.get(i);
		BufferedReader br= null;

		if(dm.getNum()==1){
			br=new BufferedReader(new FileReader(new File("findBySingle.txt")));
		}else if(dm.getDataType().equals("char")){
			br=new BufferedReader(new FileReader(new File("findByArray.txt")));
		}else{
			br.close();
			return null;
		}
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line+"\n");
		}
		String src = sb.toString();
		src=src.replaceAll("<ClassName>", structName);
		src=src.replaceAll("<ClassNameLows>", structName.toLowerCase()+"s");
		src=src.replaceAll("<ClassNameLow>", structName.toLowerCase());
		src=src.replaceAll("<dataName>", dm.getDataName());
		src=src.replaceAll("<dataType>", dm.getDataType());
		String input =getInputFuncForSingleVar(dm.getDataType(), dm.getDataName(), dm.getNum());
		src=src.replaceAll("<input>", input);
		br.close();
		return src;
	}
	
	public String getAddFunc() throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(new File("add.txt")));
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line+"\n");
		}
		br.close();
		String src = sb.toString();
		src=src.replaceAll("<ClassName>", structName);
		src=src.replaceAll("<ClassNameLows>", structName.toLowerCase()+"s");
		src=src.replaceAll("<ClassNameLow>", structName.toLowerCase());
		StringBuilder input =new StringBuilder();
		for (DataMember dm : datas){
			input.append(getInputFuncForAdd(dm.getDataType(), dm.getDataName(), structName.toLowerCase()));
		}
		src=src.replaceAll("<input>", input.toString());
		return src;
	}
	
	//objname usually is <classnameLow>
	public static String getInputFuncForAdd(String dataType,String dataName,String objName){
		StringBuilder sb =new StringBuilder();
		sb.append("printf(\"\");\n");
		sb.append("scanf(\"%");
		switch(dataType){
		case "char":
			sb.append("s\",");
			break;
		case "int":
			sb.append("d\",&");
			break;
		case "double":
			sb.append("lf\",&");
			break;
		case "float":
			sb.append("f\",&");
			break;
		}
		sb.append(objName+"."+dataName+");\n");
		return sb.toString();
	}
	
	public static String getInputFuncForSingleVar(String dataType,String dataName,int num){
		StringBuilder sb =new StringBuilder();
		if(dataType.equals("char")){
			sb.append("char "+dataName+"["+num+"]"+ ";\n");
			sb.append("printf(\"\");\n");
			sb.append("scanf(\"%");
			sb.append("s\",");
		}else{
			sb.append(dataType+" "+dataName+ ";\n");
			sb.append("printf(\"\");\n");
			sb.append("scanf(\"%");
			switch(dataType){
			case "int":
				sb.append("d\",&");
				break;
			case "double":
				sb.append("lf\",&");
				break;
			case "float":
				sb.append("f\",&");
				break;
			}
			
		}
		sb.append(dataName+");\n");
		return sb.toString();
	}
	
	public String getStructName() {
		return structName;
	}



	public ArrayList<DataMember> getDatas() {
		return datas;
	}
	
	


	class DataMember{
		private String dataType;
		private String dataName;
		private int num;
		public String getDataType() {
			return dataType;
		}
		public String getDataName() {
			return dataName;
		}
		public int getNum() {
			return num;
		}
		public void setDataType(String dataType) {
			this.dataType = dataType;
		}
		public void setDataName(String dataName) {
			this.dataName = dataName;
		}
		public void setNum(int num) {
			this.num = num;
		}
		@Override
		public String toString() {
			return "DataMember [dataType=" + dataType + ", dataName="
					+ dataName + ", num=" + num + "]";
		}
		
	}
}
