package com.marcel.Communication;

import java.util.ArrayList;

public class CheckpointList extends ArrayList<Checkpoint> {

    public Checkpoint getCheckpoint(String name) {
        for (int i = 0; i < size(); ++i) {
            if (get(i).getName().equals(name))
                return get(i);
        }
        return null;
    }
}
