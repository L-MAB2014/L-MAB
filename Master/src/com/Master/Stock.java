package com.Master;

public class Stock {
	private Node mRoot;
	
	public Stock() {
		mRoot = null;
	}
	
	public String toString() {
		String ret = "";
		for(Node n=mRoot; n!=null; n=n.getNext())
			ret = ret + n.getData() + " ";
		return ret;
	}
	
	public void addFront(String data) {
		mRoot = new Node(data, mRoot);
	}

	public void addBack(String data) {
		if(mRoot == null)
			mRoot = new Node(data, null);
		else {
			Node n=mRoot;
			while(n.mNext!=null)
				n=n.mNext;
			
			n.mNext = new Node(data, null);
		}
	}
	
	public void print() {
		for(Node n=mRoot; n!=null; n=n.getNext())
			System.out.print(n.getData() + "\t");
	}
	
	class Node {
		private String mData;
		private Node mNext;
		
		public Node(String data, Node next) {
			mData = data;
			mNext = next;
		}
		
		public void setNext(Node node) {
			mNext = node;
		}
		
		public String getData() {
			return mData;
		}
		
		public Node getNext() {
			return mNext;
		}
	}

	public void clear() {
		mRoot = null;
	}
}
