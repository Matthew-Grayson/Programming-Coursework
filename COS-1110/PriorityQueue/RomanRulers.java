import java.util.*;
public class RomanRulers implements Comparable<RomanRulers> {
    private int rulerID;
    private String rulerName;
    public RomanRulers(int id, String name) {
        rulerID = id;
        rulerName = name;
    }
    public int getRulerID() {
        return rulerID;
    }
    public String getRulerName() {
        return rulerName;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RomanRulers)) return false;
        RomanRulers theOther = (RomanRulers) obj;
        return this.getRulerID() == theOther.getRulerID();
    }
    public int compareTo(RomanRulers theOther) {
        return Integer.compare(this.rulerID, theOther.rulerID);
    }
    @Override
    public String toString() {
        return "Ruler Succession # " + getRulerID() + "\tRuler Name: " + getRulerName();
    }
}