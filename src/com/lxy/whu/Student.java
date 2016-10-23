package com.lxy.whu;

import java.io.Serializable;

public class Student implements Serializable ,  Comparable<Student> {
	private int id;
	private String name;
	private int chi;
	private int math;
	private int eng;
	public int sum;
	
	//重写toString方法，使对象的属性一起输出
	@Override
	public String toString()
	{
			return id +"\t" +name + "\t"+chi+"\t"+math+"\t"+ eng+"\t"+sum+ "\r\n";
	}
	//重写compareTo方法，使student 对象按照总分排序
	@Override
	public int compareTo(Student o)
	{
		
		return o.getSum()  - this.getSum();
	}
	
	public Student(String[] a) {
		//将student对象的string属性转化为int类型
		this.id = Integer.valueOf(a[0].trim());
		
		this.name = a[1];
		this.chi = Integer.valueOf(a[2].trim());
		this.math = Integer.valueOf(a[3].trim());
		this.eng = Integer.valueOf(a[4].trim());
		this.sum = Integer.valueOf(a[5].trim());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getChinese() {
		return chi;
	}
	public void setChinese(int chinese) {
		this.chi = chinese;
	}
	public int getMath() {
		return math;
	}
	public void setMath(int math) {
		this.math = math;
	}
	public int getEng() {
		return eng;
	}
	public void setEng(int eng) {
		this.eng = eng;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}


	
}
