package com.shiyanlou.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularEx {

	public static void main(String[] args) throws IOException {
		Struct s = new Struct("struct.txt");
		ArrayList<Struct.DataMember> datas = s.getDatas();
		for(int i=0;i<datas.size();i++){
			System.out.println(datas.get(i));
		}
	}
}
