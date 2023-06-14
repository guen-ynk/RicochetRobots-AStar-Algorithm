import java.io.FileInputStream;
import java.util.*;

public class RicochetRobots {

    /**
     * Find the shortest move sequence for the given board situation to the goal state,
     * i.e., the designated robot has reached the target field.
     * The task is accomplished by using breadth-first-search. In order to avoid checking
     * the same situations over and over again, each investigated board is put in a hash set.
     *
     * @param board Initial configuration of the game.
     * @return The partial solution containing the the shortest move sequence to the target
     */
    public static PartialSolution bfsWithHashing(Board board) {
        boolean alpha = false;                                                                  // ist schon eine Lösung gefunden worden ?
        HashSet<Integer> b = new HashSet<>();                                                   // Hashset der Boards .
        LinkedList<PartialSolution> rr = new LinkedList<>();                                    // Liste mit Lösungen.
        PriorityQueue<PartialSolution> P = new PriorityQueue<>();                               // Queue nach moves.size() sortiert, enthält Teillösungen.

        P.add(new PartialSolution(board));
        b.add(board.hashCode());

        PartialSolution s;                                                                      // Aktuelle Teillösung.
        while (P.peek() != null) {                                                              // BFS -> so lange noch "Knoten" unentdeckt sind.
            s = P.poll();                                                                       // Queue-> first ist zu betrachtende Teillösung.

            if (alpha) {                                                                        // Wenn es schon eine Lösung gibt dann schauen wir ob es in der Queue
                boolean ret = false;                                                            // noch mögliche Kandidaten für Lösungen mit weniger Wechseln gibt.
                for (PartialSolution zzz : P) {
                    if (rr.peek().moves.size() >= zzz.moves.size()) {
                        ret = true;
                    }
                }
                if (!ret) {                                                                     // Falls es keine gibt wird aus der Ergebnissliste jene mit den
                    PartialSolution rett = rr.peek();                                           // wenigsten wechseln returnt, sonst geht es weiter im BFS.
                    for (PartialSolution ui : rr) {
                        if (ui.changes < rett.changes && ui.moves.size() == rett.moves.size()) {
                            rett = ui;
                        }
                    }
                    return rett;
                }
            }

            if (s.board.targetReached()) {                                                       // Falls s eine Lösung ist wird es in die Liste gepackt.
                if (!alpha) {
                    alpha = true;                                                                // Und es wird makiert das nun eine Lösung gefunden wurde.
                }
                rr.add(s);
            }

            for (Move moves : s.vMoves()) {
                PartialSolution tem = new PartialSolution(s);                                    // Auf s wird nun in Form neuer Teillös jeder valideMove ausgeführt.
                tem.doMove(moves);
                if (tem.board.targetReached()) {                                                 // Wenn tem Lösung ist wird das vermerkt und tem kommt in in die Liste .
                    if (!alpha) {
                        alpha = true;
                    }
                    rr.add(tem);
                }
                // falls ein move auf das selbe Board weniger züge hat :
                if (b.contains(tem.board.hashCode())) {                                          // Für den Fall das es eine Teillösung mit dem selben Bord aber
                    boolean ts = false;                                                          // aber einer optimaleren Anzahl an Wechseln gibt wird die wenn sie
                                                                                                 // nicht länger als die originale ist in die Queue geaddet.
                    for (PartialSolution rrr : P) {
                        if (tem.board.hashCode() == rrr.board.hashCode() && tem.changes <= rrr.changes && tem.moves.size() <= rrr.moves.size()) {
                            ts = true;
                        }
                    }
                    if (ts) {
                        P.add(tem);
                    }
                }
                if (!b.contains(tem.board.hashCode())) {                                          // Falls es das Board noch nicht geben sollte
                    b.add(tem.board.hashCode());                                                  // wird es makiert und seine Teilös kommt in die Queue.
                    P.add(tem);
                }
            }
        }

        return null;                                                                              // Falls keine Lösung gefunden wird. wird null return.

    }

    public static void printBoardSequence(Board board, Iterable<Move> moveSequence) {
        int moveno = 0;
        for (Move move : moveSequence) {
            board.print();
            System.out.println((++moveno) + ". Move: " + move);
            board.doMove(move);
        }
        board.print();
    }

    public static void main(String[] args) throws java.io.FileNotFoundException {
        System.setIn(new FileInputStream("samples/rrBoard-sample00.txt"));
        // System.setIn(new FileInputStream("samples/rrBoard-sample01.txt")); // 3 8 moves;
        // System.setIn(new FileInputStream("samples/rrBoard-sample02.txt")); // 4--> 2
         //System.setIn(new FileInputStream("samples/rrBoard-sample03.txt")); //8
        Board board = new Board(new Scanner(System.in));
        long start = System.nanoTime();
        PartialSolution sol = bfsWithHashing(board);
        long duration1 = (System.nanoTime() - start) / 1000;
        if (sol == null) {
            System.out.println("Board is unsolvable.");
        } else {
            printBoardSequence(board, sol.moveSequence());
            System.out.println("Found solution with " + sol.moveSequence().size() + " moves:\n" + sol);
            System.out.println("Computing time: " + duration1 / 1000 + " ms");
            System.out.println("Changes : " + sol.changes);
        }
    }
}

