package com.camelot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.alibaba.fastjson.JSON;

public class Demo {
	private Long id;
	private String name;
	private String src;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public static void main(String[] args) {
		Demo demo= new Demo();
		demo.setId(1l);
		demo.setName("北京"); 
		demo.setSrc("http://localhost:8080/image/abc.jpg"); 
		Demo demo1= new Demo();
		demo1.setId(2L);
		demo1.setName("上海");
		demo1.setSrc("http://localhost:8080/image/abc.jpg"); 
		Demo demo2= new Demo();
		demo2.setId(3L);
		demo2.setName("广州");
		demo2.setSrc("http://localhost:8080/image/abc.jpg"); 
		Demo demo3= new Demo();
		demo3.setId(4L);
		demo3.setName("深圳"); 
		demo3.setSrc("http://localhost:8080/image/abc.jpg"); 
		List<Demo> list = new ArrayList<Demo>();
		list.add(demo);
		list.add(demo1);
		list.add(demo2);
		list.add(demo3);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("url", "http://localhost:8080/getJsonArray?id=");
		System.out.println(JSON.toJSONString(map));
	}
}	
