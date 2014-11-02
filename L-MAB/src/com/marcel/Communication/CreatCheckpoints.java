package com.marcel.Communication;

import lejos.nxt.ColorSensor.Color;

public class CreatCheckpoints {

	/**
	 * ERstellt alle Checkpoints und die dazu gehoerigen Verbindungen 
	 * @return Map mit den Checkpoints des Lagers(Map)
	 */
	public static CheckpointList InitializeCheckpoints()
	{
		Checkpoint s1 = new Checkpoint("S1",Color.RED);
		Checkpoint s2 = new Checkpoint("S2",Color.RED);
		Checkpoint s3 = new Checkpoint("S3",Color.RED);
		Checkpoint s4 = new Checkpoint("S4",Color.RED);
		Checkpoint s5 = new Checkpoint("S5",Color.RED);
		Checkpoint s6 = new Checkpoint("S6",Color.RED);
		
		Checkpoint pl1 = new Checkpoint("PL1",Color.RED);
		Checkpoint pl2 = new Checkpoint("PL2",Color.RED);
		Checkpoint pl3 = new Checkpoint("PL3",Color.RED);
		
		Checkpoint c1 = new Checkpoint("C1",Color.YELLOW);
		Checkpoint c2 = new Checkpoint("C2",Color.YELLOW);
		Checkpoint c3 = new Checkpoint("C3",Color.YELLOW);
		
		Checkpoint pf1 = new Checkpoint("PF1",Color.YELLOW);
		Checkpoint pf2 = new Checkpoint("PF2",Color.YELLOW);
		Checkpoint pf3 = new Checkpoint("PF3",Color.YELLOW);
					
		Checkpoint e1 = new Checkpoint("E1",Color.RED);
		Checkpoint e2 = new Checkpoint("E2",Color.RED);
		Checkpoint e3 = new Checkpoint("E3",Color.RED);
		Checkpoint e4 = new Checkpoint("E4",Color.RED);
		
		Checkpoint pu1 = new Checkpoint("PU1",Color.RED);
		Checkpoint pu2 = new Checkpoint("PU2",Color.RED);
		
		Checkpoint cp1 = new Checkpoint("CP1",Color.YELLOW);
		Checkpoint cp2 = new Checkpoint("CP2",Color.YELLOW);
		Checkpoint cp3 = new Checkpoint("CP3",Color.YELLOW);
		Checkpoint cp4 = new Checkpoint("CP4",Color.YELLOW);
		
		Checkpoint p1 = new Checkpoint("P1",Color.YELLOW);
		Checkpoint p2 = new Checkpoint("P2",Color.YELLOW);
		Checkpoint p3 = new Checkpoint("P3",Color.YELLOW);
		Checkpoint p4 = new Checkpoint("P4",Color.YELLOW);
		
		s1.setNextCheckpoints(s2, pl1);
		s1.setPeviousCheckpoints(cp4, null);
		
		s2.setNextCheckpoints(s3, null);
		s2.setPeviousCheckpoints(s1, pl1);
		
		s3.setNextCheckpoints(s4, pl2);
		s3.setPeviousCheckpoints(s2, null);
		
		s4.setNextCheckpoints(s5, null);
		s4.setPeviousCheckpoints(s3, pl2);
		
		s5.setNextCheckpoints(s6, pl3);
		s5.setPeviousCheckpoints(s4, null);
		
		s6.setNextCheckpoints(c1, null);
		s6.setPeviousCheckpoints(s5, pl3);
		
		pl1.setNextCheckpoints(s2, null);
		pl1.setPeviousCheckpoints(s1, null);

		pl2.setNextCheckpoints(s4, null);
		pl2.setPeviousCheckpoints(s3, null);
		
		pl3.setNextCheckpoints(s6, null);
		pl3.setPeviousCheckpoints(s5, null);
		
		c1.setNextCheckpoints(c2, pf1);
		c1.setPeviousCheckpoints(s6, pf1);
		
		c2.setNextCheckpoints(c3, pf2);
		c2.setPeviousCheckpoints(c1, pf2);
		
		c3.setNextCheckpoints(e1, pf3);
		c3.setPeviousCheckpoints(c2, pf3);
		
		pf1.setNextCheckpoints(c1, null);
		pf1.setPeviousCheckpoints(c1, null);
		
		pf2.setNextCheckpoints(c2, null);
		pf2.setPeviousCheckpoints(c2, null);
		
		pf3.setNextCheckpoints(c3, null);
		pf3.setPeviousCheckpoints(c3, null);
		
		e1.setNextCheckpoints(e2, pu1);
		e1.setPeviousCheckpoints(c3, null);
		
		e2.setNextCheckpoints(e3, null);
		e2.setPeviousCheckpoints(e1, pu1);
		
		e3.setNextCheckpoints(e4, pu2);
		e3.setPeviousCheckpoints(e2, null);
		
		e4.setNextCheckpoints(cp1, null);
		e4.setPeviousCheckpoints(e3, pu2);
		
		pu1.setNextCheckpoints(e2, null);
		pu1.setPeviousCheckpoints(e1, null);

		pu2.setNextCheckpoints(e4, null);
		pu2.setPeviousCheckpoints(e3, null);
		
		cp1.setNextCheckpoints(cp2, p1);
		cp1.setPeviousCheckpoints(e4, p1);
		
		cp2.setNextCheckpoints(cp3, p2);
		cp2.setPeviousCheckpoints(cp1, p2);
		
		cp3.setNextCheckpoints(cp4, p3);
		cp3.setPeviousCheckpoints(cp2, p3);
		
		cp4.setNextCheckpoints(s1, p4);
		cp4.setPeviousCheckpoints(cp3, p4);
		
		p1.setNextCheckpoints(cp1, null);
		p1.setPeviousCheckpoints(cp1, null);
		
		p2.setNextCheckpoints(cp2, null);
		p2.setPeviousCheckpoints(cp2, null);
		
		p3.setNextCheckpoints(cp3, null);
		p3.setPeviousCheckpoints(cp3, null);
		
		p4.setNextCheckpoints(cp4, null);
		p4.setPeviousCheckpoints(cp4, null);
						
		CheckpointList map = new CheckpointList();	
		
		map.add(s1);
		map.add(s2);
		map.add(s3);
		map.add(s4);
		map.add(s5);
		map.add(s6);
		
		map.add(pl1);
		map.add(pl2);
		map.add(pl3);
		
		map.add(c1);
		map.add(c2);
		map.add(c3);
		
		map.add(pf1);
		map.add(pf2);
		map.add(pf3);
		
		map.add(e1);
		map.add(e2);
		map.add(e3);
		map.add(e4);
		
		map.add(pu1);
		map.add(pu2);
		
		map.add(cp1);
		map.add(cp2);
		map.add(cp3);
		map.add(cp4);
		
		map.add(p1);
		map.add(p2);
		map.add(p3);
		map.add(p4);			
		
		return map;
	}
	
}
