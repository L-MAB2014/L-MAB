package com.marcel.TestMaster;
import java.util.*;




public class Protokoll {
	
	
	public static String MessageToString(Order order)
	{
//		String m = "";
//		
//		for(int i =0 ; i < message.size(); ++i)
//		{
//			m += message.get(i).getKey()+":"+message.get(i).getValue();
//			
//			if((i+1) < message.size())
//				m+=";";
//		}
//		m +="#";
//		
//		return m;
				
		return String.format("ID:%s;S:%s;E:%s#", order.getId(), order.getStore_place(), order.getExit_place());
	}
	
	public static List StringToMessage(String message)
	{
		List <Message> l = new ArrayList<Message>();
		String[] splitMessage = message.split(";");
		
		for (int i=0; i< splitMessage.length; ++i)
		{
			String[] sm = splitMessage[i].split(":");
			l.add( new Message(sm[0],sm[1]));
		}
		
		return l;
	}
	
	
//	public static void main(String [] args)
//	{
//		List <Message> l = new ArrayList<Message>();
//		l.add(new Message ("AB1","BLAU"));
//		l.add(new Message ("AB2","GRUEN"));
//		l.add(new Message ("AB3","GELB"));
//		l.add(new Message ("AB4","BLAU"));
//		l.add(new Message ("AB5","ROT"));
//		l.add(new Message ("AB6","GRUEN"));
//		l.add(new Message ("AB7","BLAU"));
//		
//		System.out.println(MessageToString(l));
//		
//		String m = "AB1:BLAU;AB2:GRUEN;AB3:GELB;AB4:BLAU;AB5:ROT;AB6:GRUEN;AB7:PINK";
//		l = StringToMessage(m);
//		
//		for(int i =0 ; i < l.size(); ++i)
//		{
//			System.out.println(l.get(i).getKey()+":"+l.get(i).getValue());
//			
//			
//		}
//		
//		
//	}
			

}
