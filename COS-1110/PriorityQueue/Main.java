import java.util.PriorityQueue;
public class Main {
    public static void main(String[] args) {
        PriorityQueue<RomanRulers> theRulers = new PriorityQueue<RomanRulers>();
        theRulers.add(new RomanRulers(1, "Augustus"));
        theRulers.add(new RomanRulers(2, "Tiberius"));
        theRulers.add(new RomanRulers(3, "Caligula"));
        theRulers.add(new RomanRulers(4, "Claudius"));
        theRulers.add(new RomanRulers(5, "Nero"));
        theRulers.add(new RomanRulers(6, "Galba"));
        theRulers.add(new RomanRulers(7, "Otho"));
        theRulers.add(new RomanRulers(8, "Aulus Vitellius"));
        theRulers.add(new RomanRulers(9, "Vespasian"));
        theRulers.add(new RomanRulers(10, "Titus"));
        theRulers.add(new RomanRulers(11, "Domitian"));
        theRulers.add(new RomanRulers(12, "Nerva"));
        while (!theRulers.isEmpty()) {
            System.out.println(theRulers.poll());
        }
    }
}