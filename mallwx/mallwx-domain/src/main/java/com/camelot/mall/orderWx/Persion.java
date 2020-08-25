package com.camelot.mall.orderWx;

import java.io.Serializable;

public class Persion implements Serializable{


	private static final long serialVersionUID = -2149985882307908797L;

	private String name,age ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Persion [name=" + name + ", age=" + age + "]";
	}
	
}
