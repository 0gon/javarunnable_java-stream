package ex.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class StreamProductFolderRead {

	public static void main(String[] args) throws FileNotFoundException {
		  //폴더에 있는 모든 파일 모음  c:\\tmp\\stream
		  Stream<String> st=getStream();
		  int[][] monsList = new int[12][3];
		  //월별 아반테  생산, 판매, 반품 현황
		  
		  String carname = "아반떼";
		   Stream<Product> sp = st.parallel()
				   .map((s) -> { 
					   String[] str = s.split(",");
					   String temp="";
					   try {   temp=str[4];
					   } catch(Exception e) {   temp="";  }
					   return new Product(Integer.parseInt(str[0]),
					     Integer.parseInt(str[1]),
					     str[2],
					     Integer.parseInt(str[3]),
					     temp);}
				   
				   );
		   
		   
				   Map <Integer, Map<Integer, Integer>> 
				   report =
		   					sp
				           .filter((s) -> { return s.getCar().equals(carname); })
				   		   .collect(Collectors.groupingBy(Product::getMonth, 
				   				   Collectors.groupingBy(Product::getCon,						  
						    Collectors.summingInt(Product::getQty)
						   ) 
						   ));
			
				
				   
					
				
					   
			String colname[] = {"월", "생산", "판매", "반품", "재고"};		   
			String[][] arr = new String[12][5];
			JFrame jFrame = new JFrame("월별 현황");
			JTable jtable = new JTable(arr,colname);
			JScrollPane jScollPane = new JScrollPane(jtable);
	       
			jFrame.add(jScollPane);
			jFrame.setSize(400, 300);
			jFrame.setVisible(true);
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
			   for (int mon=1 ; mon <= 12 ; mon++) {
				   arr[mon-1][0]=mon+"";
				  int stock=0;
				 if(report.get(mon)!=null) {
				 for (int con=1; con < 4 ; con++) {
					 if(report.get(mon).get(con)!=null) {
						 stock+=report.get(mon).get(con);
						 arr[mon-1][con]=report.get(mon).get(con)+"";
					
					 }
					 else {
						 arr[mon-1][con-1]=" "; 
					 }
				 }
			
				 }
			
			  	}
			
	          
	      

}
	
	public static Stream<String> getStream() {
		 File file = new File("c:\\tmp\\stream");
		 FileReader fileReader=null; 
		 File[] files = file.listFiles();
		 String[] strArray = { ""};
		 Stream<String> nullstream = Arrays.stream(strArray);
		 Stream<File> filestream = Arrays.stream(files);
		 
		 Stream<String> stream = filestream
				 .filter((f)->!f.isDirectory())
				 .flatMap((f) -> {
						FileReader fr;
						try {
							fr = new FileReader(f);
							BufferedReader br = new BufferedReader(fr);
							Stream<String> st = br.lines();
							return st;
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return nullstream;
						}
					
		 });
		
         return stream;
		
	}
	
}
class Product implements Comparable<Product>{
	 
	 private int month;
	 private int con;
	 private String car;
	 private int qty;
	 String remark;
	 
	 public Product(int month, int con, String car, int qty, String remark) {
	  super();
	  this.month = month;
	  this.con = con;
	  this.car = car;
	  this.qty = qty;
	  this.remark = remark;
	 }
	 public int getMonth() {
	  return month;
	 }
	 public void setMonth(int month) {
	  this.month = month;
	 }
	 public int getCon() {
	  
	  return con;
	 }
	 public void setCon(int con) {
	  this.con = con;
	 }
	 public String getCar() {
	  return car;
	 }
	 
	 public void setCar(String car) {
	  this.car = car;
	 }
	 public int getQty() {
	  return qty;
	 }
	 public void setQty(int qty) {
	  this.qty = qty;
	 }
	 public String getRemark() {
	  return remark;
	 }
	 public void setRemark(String remark) {
	  this.remark = remark;
	 }
	 @Override
	 public String toString() {
	  return month + "월 , 조건 =" + (getCon()==1? "생산" : getCon()==2 ? "판매" : "반품") + ", car=" + car
	    + ", qty=" + getQty() + ", remark=" + remark;
	 }
	 @Override
	 public int compareTo(Product o) {
	  // TODO Auto-generated method stub
	  return car.compareTo(o.car);
	 }
	 
	 
	 
	}