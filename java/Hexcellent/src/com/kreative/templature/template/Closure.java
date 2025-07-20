package com.kreative.templature.template;

import java.util.ArrayList;
import java.util.HashMap;

public class Closure {
	private final Closure parent;
	private final HashMap<String,ClosureItem> items;
	private final ArrayList<ClosureListener> listeners;
	
	public Closure(Closure parent) {
		this.parent = parent;
		this.items = new HashMap<String,ClosureItem>();
		this.listeners = new ArrayList<ClosureListener>();
	}
	
	public void addItem(ClosureItem item) {
		if (item == null) return;
		String key = item.getKey();
		if (key == null) return;
		items.put(key.toLowerCase(), item);
	}
	
	public void removeItem(ClosureItem item) {
		if (item == null) return;
		items.values().remove(item);
	}
	
	public ClosureItem getItem(String key) {
		if (key == null) return null;
		ClosureItem item = items.get(key.toLowerCase());
		if (item != null) return item;
		if (parent == null) return null;
		return parent.getItem(key);
	}
	
	public Object getValue(String key) {
		if (key == null) return null;
		ClosureItem item = items.get(key.toLowerCase());
		if (item != null) return item.getValue();
		if (parent == null) return null;
		return parent.getValue(key);
	}
	
	public void addClosureListener(ClosureListener listener) {
		if (listener != null) listeners.add(listener);
	}
	
	public void removeClosureListener(ClosureListener listener) {
		if (listener != null) listeners.remove(listener);
	}
	
	public void fireValueChanged(ClosureItem item) {
		for (ClosureListener l : listeners) l.valueChanged(item);
		if (parent != null) parent.fireValueChanged(item);
	}
}
