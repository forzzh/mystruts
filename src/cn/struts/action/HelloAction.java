package cn.struts.action;

public class HelloAction {
	
	private String name;
	
	public String execute() {
		System.out.println("hello " + name);
		return "success";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
