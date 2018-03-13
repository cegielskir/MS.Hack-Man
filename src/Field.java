import java.awt.*;
import java.util.ArrayList;


public class Field {

    public String err;
    public int round =0;

    protected final String EMTPY_FIELD = ".";
    protected final String BLOCKED_FIELD = "x";

    private String myId;
    private String opponentId;
    protected int width;
    protected int height;
    public Move myLastMove = null;
    protected Move hisLastMove = null;
    protected Point hisLastPosition = null;

    protected ArrayList<Point> lastBugsPositions;
    protected String[][] field;
    protected Point myPosition;
    protected Point opponentPosition;
    protected ArrayList<Bug> enemies;
    protected ArrayList<Point> enemyPositions;
    protected ArrayList<Point> snippetPositions;
    protected ArrayList<Point> bombPositions;
    protected ArrayList<Point> tickingBombPositions;
    protected ArrayList<Point> spawnPoints;
    protected ArrayList<Point> whereWillBugsGo;

    public Field() {
        whereWillBugsGo = new ArrayList<>();
        this.enemyPositions = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.snippetPositions = new ArrayList<>();
        this.bombPositions = new ArrayList<>();
        this.tickingBombPositions = new ArrayList<>();
        this.spawnPoints = new ArrayList<>();
        this.lastBugsPositions = new ArrayList<>();

    }

    /**
     * Initializes field
     * @throws Exception: exception
     */
    public void initField() throws Exception {
        try {
            this.field = new String[this.height][this.width];
        } catch (Exception e) {
            throw new Exception("Error: trying to initialize field while field "
                    + "settings have not been parsed yet.");
        }
        clearField();
    }

    /**
     * Clears the field
     */
    public void clearField() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.field[y][x] = "";
            }
        }
        whereWillBugsGo.clear();
        this.lastBugsPositions.clear();
        for(Bug bug : this.enemies){
            this.lastBugsPositions.add(bug.point);
        }
        this.enemyPositions.clear();
        this.myPosition = null;
        this.opponentPosition = null;
        this.enemies.clear();
        this.snippetPositions.clear();
        this.bombPositions.clear();
        this.tickingBombPositions.clear();
        this.spawnPoints.clear();
    }

    /**
     * Parses input string from the engine and stores it in
     * this.field. Also stores several interesting points.
     * @param input String input from the engine
     */
    public void parseFromString(String input) {

        String[] cells = input.split(",");
        int x = 0;
        int y = 0;

        for (String cellString : cells) {
            this.field[y][x] = cellString;

            for (String cellPart : cellString.split(";")) {
                switch (cellPart.charAt(0)) {
                    case 'P':
                        parsePlayerCell(cellPart.charAt(1), x, y);
                        break;
                    case 'S':
                        if(cellPart.length() > 1) {
                            if (cellPart.charAt(1) == '1') parseSpawnCell(cellPart.charAt(1),x,y);
                        }
                        break;
                    case 'E':
                        parseEnemyCell(cellPart.charAt(1), x, y);
                        break;
                    case 'B':
                        parseBombCell(cellPart, x, y);
                        break;
                    case 'C':
                        parseSnippetCell(x, y);
                        break;
                }
            }

            if (++x == this.width) {
                x = 0;
                y++;
            }
        }
        if(hisLastPosition != null) hisLastMoveInit();
    }



//    public void updateGraph(){
//        this.graph.updateGraph();
//    }

    /**
     * Stores the position of one of the players, given by the id
     * @param id Player ID
     * @param x X-position
     * @param y Y-position
     */
    private void parsePlayerCell(char id, int x, int y) {
        if (id == this.myId.charAt(0)) {
            this.myPosition = new Point(x, y);
        } else if (id == this.opponentId.charAt(0)) {
            this.opponentPosition = new Point(x, y);
        }
    }

    /**
     * Stores the position of an enemy. The type of enemy AI
     * is also given, but not stored in the starterbot.
     * @param type Type of enemy AI
     * @param x X-position
     * @param y Y-position
     */
    private void parseEnemyCell(char type, int x, int y){
        if(type == '0') this.enemies.add(new ChaseBug(new Point(x,y)));
        if(type == '1') this.enemies.add(new PredictBug(new Point(x,y)));
        if(type == '2') this.enemies.add(new LeverBug(new Point(x,y)));
        if(type == '3') this.enemies.add(new FarChaseBug(new Point(x,y)));
        this.enemyPositions.add(new Point(x,y));
    }

    private void parseSpawnCell(char type,int x, int y) {this.spawnPoints.add(new Point(x,y));}
    /**
     * Stores the position of a bomb that can be collected or is
     * about to explode. The amount of ticks is not stored
     * in this starterbot.
     * @param cell The string that represents a bomb, if only 1 letter it
     *             can be collected, otherwise it will contain a number
     *             2 - 5, that means it's ticking to explode in that amount
     *             of rounds.
     * @param x X-position
     * @param y Y-position
     */
    private void parseBombCell(String cell, int x, int y) {
        if (cell.length() <= 1) {
            this.bombPositions.add(new Point(x, y));
        } else {
            this.tickingBombPositions.add(new Point(x, y));
        }
    }

    private void hisLastMoveInit(){
        if(this.hisLastPosition == null) this.hisLastMove = null;
        if(this.hisLastPosition.x > this.opponentPosition.x) this.hisLastMove  =new Move(MoveType.LEFT);
        if(this.hisLastPosition.x < this.opponentPosition.x) this.hisLastMove  = new Move(MoveType.RIGHT);
        if(this.hisLastPosition.y > this.opponentPosition.y) this.hisLastMove  =new Move(MoveType.DOWN);
        if(this.hisLastPosition.y < this.opponentPosition.y) this.hisLastMove = new Move(MoveType.UP);
        this.hisLastMove = new Move(MoveType.PASS);
    }

    /**
     * Stores the position of a snippet
     * @param x X-position
     * @param y Y-position
     */
    private void parseSnippetCell(int x, int y) {
        this.snippetPositions.add(new Point(x, y));
    }



    public void setMyId(int id) {
        this.myId = id + "";
    }

    public void setOpponentId(int id) {
        this.opponentId = id + "";
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Point getMyPosition() {
        return this.myPosition;
    }

    public Point getOpponentPosition() {
        return this.opponentPosition;
    }

    public ArrayList<Bug> getEnemies() {
        return this.enemies;
    }

    public ArrayList<Point> getSnippetPositions() {
        return this.snippetPositions;
    }

    public ArrayList<Point> getBombPositions() {
        return this.bombPositions;
    }

    public ArrayList<Point> getTickingBombPositions() {
        return this.tickingBombPositions;
    }

    public void setHisLastPosition(Point hisLastPosition) {
        this.hisLastPosition = hisLastPosition;
    }
}