import java.util.HashMap;

<<<<<<< HEAD
/**
 * BotState
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
=======
>>>>>>> Paths blocking added
public class BotState {
    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int MAX_ROUNDS;

    private int roundNumber;
    private int timebank;
    private String myName;
    private HashMap<String, Player> players;

    private GraphField graphField;

    BotState() {
        this.graphField = new GraphField();
        this.players = new HashMap<>();
    }

    public void setTimebank(int value) {
        this.timebank = value;
    }

    public void setMaxTimebank(int value) {
        this.MAX_TIMEBANK = value;
    }

    public void setTimePerMove(int value) {
        this.TIME_PER_MOVE = value;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setMaxRounds(int value) {
        this.MAX_ROUNDS = value;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getTimebank() {
        return this.timebank;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public GraphField getGraphField() {
        return this.graphField;
    }

    public String getMyName() {
        return this.myName;
    }

    public int getMaxTimebank() {
        return this.MAX_TIMEBANK;
    }

    public int getTimePerMove() {
        return this.TIME_PER_MOVE;
    }

    public int getMaxRound() {
        return this.MAX_ROUNDS;
    }
}
