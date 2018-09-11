package com.mrhan.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串区域匹配关键字工具类
 * @author MrHanHao
 *
 */
public class StringKeyWordUtil {
	
	
	public static StringKeyWordUtil createFileFindKeyWord(String path){
		File f = new File(path);
		
			return createFileFindKeyWord(f);
		
	}
	/**
	 * 根据文本文件创建匹配器
	 * @param f
	 * @return
	 */
	public static StringKeyWordUtil createFileFindKeyWord(File f) {
		if(!f.exists()){
			throw new RuntimeException("文本文件不存在");
		}
		InputStreamReader isr = null;
		BufferedReader br =null;
		try {
			isr = new FileReader(f);
			br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line= null;
			while((line=br.readLine())!=null){
				sb.append(line).append("\n");
			}
			return new StringKeyWordUtil(sb.toString());
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			
				try {
					if(br!=null)
						br.close();
					if(isr!=null)
						isr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return null;
	}
	/**
	 * 字符串匹配区域
	 */
	private String testStrArea;
	/**
	 *字符串长度
	 */
	private int testStrLength;
	/**
	 * 构造函数
	 * @param textStr 匹配字符区域
	 */
	public StringKeyWordUtil(String textStr){
		this.testStrArea = textStr;
		this.testStrLength = textStr.length();
	}
	/**
	 * 返回指定字符在区域中出现的次数
	 * @param txt 匹配字符
	 * @return int 出现次数
	 */
	public int findTextCount(String txt){
		
		List<StringQuery> allQuery = findTestQuery(txt);
		return allQuery.size();
	}

	public List<StringQuery> findTestQuery(String txt) {
		List<StringQuery> allQuery  = new ArrayList<>();
		int row = 1;
		int nowRowIndx=0;
		int lastRowIndex=0;
		int rowindex = 0;
		for(int i=0;i<testStrLength-txt.length();i++){
			StringQuery sq = findStr(i,txt);
			
			if(testStrArea.charAt(i)=='\n'){
				row++;
				rowindex=0;
				lastRowIndex=nowRowIndx;
				nowRowIndx = i;
			}
			rowindex++;
			if(sq!=null){
				sq.setRow(row);
				sq.setRowIndex(rowindex);
				sq.setRowTxt(testStrArea.substring(lastRowIndex,nowRowIndx+1));
				allQuery.add(sq);
				
			}
			
		}
		return allQuery;
	}
	/**
	 * 从指定下表匹配字符
	 * @param index 指定匹配的下表
	 * @param txt 匹配字符串
	 * @return StringQuery 匹配结果！未找到 返回 null
	 */
	private StringQuery findStr(int index,String txt){
		int txtLength = txt.length();//获取匹配字符长度
		int lastfindTextIndex = index+txtLength;//获取所在区域下标
		StringQuery query = new StringQuery(testStrArea,txt,index+1);
		if(lastfindTextIndex>testStrLength){//判断是否越界
			return null;
		}
		if(txtLength==0)
			return null;
		char firstchar = testStrArea.charAt(index);//获取第区域起始位置字符
		char firstTxtChar = txt.charAt(0);//获取第一个字符
		if(txtLength==1){
			if(firstchar==firstTxtChar){
				return query;
			}else
				return null;
		}
		char testChar=0,findChar = 0;//存放 匹配字符 和区域字符
		for(int i=0;i<txtLength;i++){
			testChar = testStrArea.charAt(index+i);
			findChar = txt.charAt(i);
			if(findChar!=testChar)
				return null;
		}
		return query;
	}
	public String getTestStrArea() {
		return testStrArea;
	}
	public int getTestStrLength() {
		return testStrLength;
	}

}
