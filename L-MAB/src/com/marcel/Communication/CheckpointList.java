package com.marcel.Communication;

import java.util.ArrayList;

/**
 * @author Marcel Reich
 * Liste mit allen Checkpunkten
 *
 */
public class CheckpointList extends ArrayList<Checkpoint> {

    /**
     * Sucht anhand eines Names (ID) den passenden Checkpunkt
     * @param name Name (ID) des zu suchenden Checkpunktes
     * @return Gefundener Checkpunkt
     */
    public Checkpoint getCheckpoint(String name) {
        for (int i = 0; i < size(); ++i) {
            if (get(i).getName().equals(name))
                return get(i);
        }
        return null;
    }
}
