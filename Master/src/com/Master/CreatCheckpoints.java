package com.Master;

import java.util.HashMap;

public class CreatCheckpoints {

    /**
     * ERstellt alle Checkpoints und die dazu gehoerigen Verbindungen
     *
     * @return Map mit den Checkpoints des Lagers(Map)
     */
    public static HashMap<String, Checkpoint> InitializeCheckpoints() {
        Checkpoint s1 = new Checkpoint("S1");
        Checkpoint s2 = new Checkpoint("S2");
        Checkpoint s3 = new Checkpoint("S3");
        Checkpoint s4 = new Checkpoint("S4");
        Checkpoint s5 = new Checkpoint("S5");
        Checkpoint s6 = new Checkpoint("S6");

        Checkpoint pl1 = new Checkpoint("PL1");
        Checkpoint pl2 = new Checkpoint("PL2");
        Checkpoint pl3 = new Checkpoint("PL3");

        Checkpoint c1 = new Checkpoint("C1");
        Checkpoint c2 = new Checkpoint("C2");
        Checkpoint c3 = new Checkpoint("C3");

        Checkpoint pf1 = new Checkpoint("PF1");
        Checkpoint pf2 = new Checkpoint("PF2");
        Checkpoint pf3 = new Checkpoint("PF3");

        Checkpoint e1 = new Checkpoint("E1");
        Checkpoint e2 = new Checkpoint("E2");
        Checkpoint e3 = new Checkpoint("E3");
        Checkpoint e4 = new Checkpoint("E4");

        Checkpoint pu1 = new Checkpoint("PU1");
        Checkpoint pu2 = new Checkpoint("PU2");

        Checkpoint cp1 = new Checkpoint("CP1");
        Checkpoint cp2 = new Checkpoint("CP2");
        Checkpoint cp3 = new Checkpoint("CP3");
        Checkpoint cp4 = new Checkpoint("CP4");

        Checkpoint p1 = new Checkpoint("P1");
        Checkpoint p2 = new Checkpoint("P2");
        Checkpoint p3 = new Checkpoint("P3");
        Checkpoint p4 = new Checkpoint("P4");

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

        HashMap<String, Checkpoint> map = new HashMap<String, Checkpoint>();

        map.put(s1.getName(), s1);
        map.put(s2.getName(), s2);
        map.put(s3.getName(), s3);
        map.put(s4.getName(), s4);
        map.put(s5.getName(), s5);
        map.put(s6.getName(), s6);

        map.put(pl1.getName(), pl1);
        map.put(pl2.getName(), pl2);
        map.put(pl3.getName(), pl3);

        map.put(c1.getName(), c1);
        map.put(c2.getName(), c2);
        map.put(c3.getName(), c3);

        map.put(pf1.getName(), pf1);
        map.put(pf2.getName(), pf2);
        map.put(pf3.getName(), pf3);

        map.put(e1.getName(), e1);
        map.put(e2.getName(), e2);
        map.put(e3.getName(), e3);
        map.put(e4.getName(), e4);

        map.put(pu1.getName(), pu1);
        map.put(pu2.getName(), pu2);

        map.put(cp1.getName(), cp1);
        map.put(cp2.getName(), cp2);
        map.put(cp3.getName(), cp3);
        map.put(cp4.getName(), cp4);

        map.put(p1.getName(), p1);
        map.put(p2.getName(), p2);
        map.put(p3.getName(), p3);
        map.put(p4.getName(), p4);

        return map;
    }

}
