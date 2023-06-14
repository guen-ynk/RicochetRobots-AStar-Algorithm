import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


/**
 * ParitialSolution provides at least the functionality which is required
 * for the use in searching for solutions of the game in a search tree.
 * It can store a game situation (Board) and a sequence of mooves.
 */
public class PartialSolution {
    LinkedList<Move> moves;
    Board board;
    int changes;



    public PartialSolution(Board board1) {
        this.board = new Board(board1);
        this.moves = new LinkedList<Move>();
        this.changes = 0;
    }

    public PartialSolution(PartialSolution pa) {
        this.board = new Board(pa.board);
        this.moves = new LinkedList<>();
        this.moves.addAll(pa.moves);
        this.changes = pa.changes;
    }

    boolean valid(Move move) {
        return this.board.isPassable(this.board.robots[move.iRobot].location, move.dir);
    }

    
    public Queue<Move> vMoves() {
        //  System.out.println(this.board.validMoves());

        return this.board.validMoves();

    }

    public void doMove(Move move) {
        if(this.moves.size() == 0){
            this.board.doMove(move);
            this.moves.add(move);
            return;
        }
        if(this.moves.get((this.moves.size()-1)).iRobot != move.iRobot){
            this.changes += 1;
        }
        this.board.doMove(move);
        this.moves.add(move);


    }

    /* TODO */
    /* Add object variables, constructors, methods as required and desired.      */

    /**
     * Return the sequence of moves which resulted in this partial solution.
     *
     * @return The sequence of moves.
     */
    public LinkedList<Move> moveSequence() {
        /* TODO */
        return moves;

    }
   
    @Override
    public String toString() {
        String str = "";
        int lastRobot = -1;
        for (Move move : moveSequence()) {
            if (lastRobot == move.iRobot) {
                str += " -> " + move.endPosition;
            } else {
                if (lastRobot != -1) {
                    str += ", ";
                }
                str += "R" + move.iRobot + " -> " + move.endPosition;
            }
            lastRobot = move.iRobot;
        }
        return str;
    }
}
