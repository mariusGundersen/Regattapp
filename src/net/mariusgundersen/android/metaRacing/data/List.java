package net.mariusgundersen.android.metaRacing.data;

import java.lang.reflect.Array;

public class List<T extends Object> {
	private T[] list;
	private int index = 0;
	private int size = 0;
	
	@SuppressWarnings("unchecked")
	public List(int length){
		list = (T[]) Array.newInstance(Object.class, length);
	}
	
	public void add(T item){
		list[index] = item;
		if(size <= index) size = index + 1;
		if(size < list.length) index++;
		else index = wrapIndex(index + 1);
	}
	
	public T get(int at){
		return list[wrapIndex(index + at)];
	}
	
	private int wrapIndex(int index){
		while(index >= size) index -= size;
		while(index < 0) index += size;
		return index;
	}
}
