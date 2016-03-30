package cn.struts.stack;

import java.util.ArrayList;
import java.util.List;

public class ValueStack {

	private List<Object> list = new ArrayList<Object>();
	
	/**
	 * 弹栈
	 * @return
	 */
	public Object pop() {
		return list.remove(0);
	}
	
	/**
	 * 入栈
	 * @param o
	 */
	public void push(Object o) {
		list.add(0, o);
	}
	
	/**
	 * 取栈顶
	 * @return
	 */
	public Object seek() {
		return list.get(0);
	}
}
