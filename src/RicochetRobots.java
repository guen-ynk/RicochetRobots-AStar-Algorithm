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

        HashSet<Integer> b = new HashSet<>();
        Queue<PartialSolution> P = new LinkedList<>();
        P.add(new PartialSolution(board));
        b.add(board.hashCode());
        //System.out.println(b);
        PartialSolution s;
        while (P.peek() != null){
            s = P.poll();
            if(s.board.targetReached()){
                
                return s;
            }
            for (Move  moves: s.vMoves()) {
                PartialSolution tem = new PartialSolution(s);
                tem.doMove(moves);
               // System.out.println(tem.board.hashCode());
                if(tem.board.targetReached()){
                   
                    return tem;
                }
                if(!b.contains(tem.board.hashCode())){
                    b.add(tem.board.hashCode());
                    P.add(tem);
                    //System.out.println(b);
                }
            }



        }

        /* TODO */
        return  null;

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
    // System.setIn(new FileInputStream("samples/rrBoard-sample00.txt")); // 1
        //System.setIn(new FileInputStream("samples/rrBoard-sample01.txt")); // 4
     //System.setIn(new FileInputStream("samples/rrBoard-sample02.txt")); // 4
      System.setIn(new FileInputStream("samples/rrBoard-sample03.txt")); //8
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
        }
    }
}

