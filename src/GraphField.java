
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Rafał on 2018-01-18.
 */
public class GraphField extends Field {
    public HashMap<Point, Vertex> graph;
    private HashMap<Point,Integer> hisTarget = new HashMap<>();
    private List<Point> bugs;
    private List<Point> dontGoThere = new ArrayList<>();
    private  static  List<Point> passivePoints;



    public GraphField() {
        super();
        graph = new HashMap<>();
        passivePoints = new ArrayList<>();
        passivePoints.add(new Point(9,8));
        passivePoints.add(new Point(4,4));
        passivePoints.add(new Point(14,4));
        passivePoints.add(new Point(10,10));
    }

    public Move whereShouldIGo(int bombs) {
        if (this.getTickingBombPositions().size() == 0) this.dontGoThere = new ArrayList<>();
        if (this.getTickingBombPositions().size() > 0) {
            if (graph.get(this.myPosition).isTheSameLine(graph.get(this.getTickingBombPositions().get(0)))) {
                this.myLastMove = new Move(runAway(this.getTickingBombPositions().get(0)));
                return this.myLastMove;
            }
			else {
                this.myLastMove = passiveMovement(bombs);
                return this.myLastMove;
            }
        } else{
            this.myLastMove = passiveMovement(bombs);
            return this.myLastMove;
        }

    }


    private MoveType runAway(Point tickingBomb) {
        initInavailablePositions(0);
        this.bugs.addAll(this.dontGoThere);
        Sign sign = runAwayBFS(tickingBomb, this.getMyPosition());
        this.dontGoThere.add(this.getMyPosition());
        return sign.where;


    }


    private void initInavailablePositions(int bombs) {
        this.bugs = new ArrayList<>();
        this.bugs.addAll(this.spawnPoints);
        for (Bug bug : this.enemyPositions) {
            if(bug instanceof PredictBug & !this.graph.get(bug.point).areAdj(this.graph.get(this.myPosition))){
                this.bugs.add(this.graph.get(bug.point).goTo(bug.whereItWillGo(this)).point);
                for(DirectionAndVertex dirAndVer : this.graph.get(bug.point).adjList) {
                        if(bombs > 0 & this.graph.get(this.myPosition).areAdj(dirAndVer.vertex)) this.bugs.add(this.myPosition);
                        this.bugs.add(dirAndVer.vertex.point);
                }
            }
            else {
                if(this.graph.get(bug.point).areAdj(this.graph.get(this.myPosition))){
                    this.bugs.add(bug.point);
                }
                for(DirectionAndVertex dirAndVer : this.graph.get(bug.point).adjList){
                    if(this.snippetPositions.contains(dirAndVer.vertex.point)) this.bugs.add(dirAndVer.vertex.point);
                    if(this.bombPositions.contains(dirAndVer.vertex.point)) this.bugs.add(dirAndVer.vertex.point);
                    if(bombs > 0 & this.graph.get(this.myPosition).areAdj(dirAndVer.vertex)) this.bugs.add(this.myPosition);
                }
                this.bugs.add(this.graph.get(bug.point).goTo(bug.whereItWillGo(this)).point);

            }
        }
    }



    private Move passiveMovement(int bombs) {
        MoveType whereToGo;
        initInavailablePositions(bombs);
        howFarFromOpponentBFS();
        this.bugs.addAll(dontGoThere);
        whereToGo = modifiedBFS(this.snippetPositions,this.myPosition, true,0,true).whereIsTheTarget;
        if (whereToGo.equals(MoveType.PASS) & bombs > 0) return new Move(whereToGo, 2);
        if (bombs > 0) {
            Vertex whereWouldIBe = this.graph.get(this.getMyPosition()).goTo(whereToGo);
            Sign sign = runAwayBFS(whereWouldIBe.point, whereWouldIBe.point);
            List<Point> enemies = new ArrayList<>();
            for(Bug bug : this.enemyPositions) enemies.add(bug.point);
            enemies.add(opponentPosition);
            for (Point point : enemies) {
                if (this.graph.get(point).isTheSameLine(whereWouldIBe)) {
                    this.dontGoThere.add(whereWouldIBe.point);
                    return new Move(whereToGo, sign.howFar);
                }
            }
        }
        if(whereToGo.equals(MoveType.PASS)) return new Move(goAwayFromBugs());
        if (whereToGo != null) return new Move(whereToGo);

        else if (this.myPosition.equals(new Point(0, 7))) return new Move(MoveType.LEFT);
        else if (this.myPosition.equals(new Point(18, 7))) return new Move(MoveType.RIGHT);
        else return new Move(MoveType.PASS);
    }

    private MoveType goAwayFromBugs(){
        for(DirectionAndVertex directionAndVertex : this.graph.get(this.myPosition).adjList){
            if(!this.bugs.contains(directionAndVertex.vertex.point) &
                    !this.myLastMove.equals(oppositeDirection(directionAndVertex.direction))) return directionAndVertex.direction;
        }
        for(DirectionAndVertex directionAndVertex : this.graph.get(this.myPosition).adjList) {
        if(!this.myLastMove.equals(oppositeDirection(directionAndVertex.direction))) return directionAndVertex.direction;
        }
        return MoveType.PASS;
    }

    private void fillAdjLists() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.graph.get(new Point(x, y)) != null) {
                    if (isRightPoint(new Point(x, y - 1))) {
                        this.graph.get(new Point(x, y)).adjList.add
                                (new DirectionAndVertex(MoveType.UP,graph.get(new Point(x, y - 1))));
                    }
                    if (isRightPoint(new Point(x, y + 1))) {
                        this.graph.get(new Point(x, y)).adjList.add(
                                new DirectionAndVertex(MoveType.DOWN, graph.get(new Point(x, y + 1))));
                    }
                    if (isRightPoint(new Point(x - 1, y))) {
                        this.graph.get(new Point(x, y)).adjList.add(
                                new DirectionAndVertex(MoveType.LEFT, graph.get(new Point(x - 1, y))));
                    }
                    if (isRightPoint(new Point(x + 1, y))) {
                        this.graph.get(new Point(x, y)).adjList.add(
                                new DirectionAndVertex(MoveType.RIGHT, graph.get(new Point(x + 1, y))));
                    }
                }
            }
        }
        this.graph.get(new Point(0, 7)).adjList.add(new DirectionAndVertex(MoveType.LEFT, this.graph.get(new Point(18, 7))));
        this.graph.get(new Point(18, 7)).adjList.add(new DirectionAndVertex(MoveType.RIGHT, this.graph.get(new Point(0, 7))));
    }


    private boolean isRightPoint(Point point) {
        if (this.graph.get(point) == null) return false;
        else return true;
    }


    public void initGraph() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (!field[y][x].equals("x")) this.graph.put(new Point(x, y), new Vertex(new Point(x, y)));
            }
        }
        fillAdjLists();
    }


    private void prepareGraphToBFS(Point point) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (graph.get(new Point(x, y)) != null) {
                    graph.get(new Point(x, y)).visited = false;
                    graph.get(new Point(x, y)).wayToGo = null;
                    graph.get(new Point(x, y)).parent = null;
                }
            }
        }
        this.graph.get(point).visited = true;
    }

    public void howFarFromOpponentBFS() {
        this.hisTarget.clear();
        Vertex hisTarget = this.graph.get(new Point(9,8));
        int posX = opponentPosition.x;
        int posY = opponentPosition.y;
        Queue<Vertex> queue = new LinkedList<>();
        prepareGraphToBFS(new Point(posX, posY));
        queue.add(this.graph.get(new Point(posX, posY)));
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();
            for (DirectionAndVertex dirAndVer : u.adjList) {
                if (!dirAndVer.vertex.visited) {
                    dirAndVer.vertex.visited = true;
                    if (!this.bugs.contains(dirAndVer.vertex.point)) {
                        dirAndVer.vertex.wayToGo = oppositeDirection(dirAndVer.direction);
                        dirAndVer.vertex.parent = u;
                        if (this.snippetPositions.contains(dirAndVer.vertex.point)) {
                            hisTarget= dirAndVer.vertex;
                            queue = new LinkedList<>();
                            break;
                        }
                        queue.add(dirAndVer.vertex);
                    }
                }
            }
        }
        this.hisTarget.put(hisTarget.point,howFarToTheRoot(hisTarget));
    }

    private int howFarToTheRoot(Vertex vertex) {
		if(vertex == null) return 0;
        if (vertex.parent != null) return 1 + howFarToTheRoot(vertex.parent);
        else return 0;
    }

    private Sign runAwayBFS(Point point, Point whereIAm) {
        Vertex target = null;
        Queue<Vertex> queue = new LinkedList<>();
        prepareGraphToBFS(whereIAm);
        queue.add(this.graph.get(whereIAm));
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();
            for (DirectionAndVertex dirAndVer : u.adjList) {
                if (!dirAndVer.vertex.visited) {
                    dirAndVer.vertex.visited = true;
                    if (!this.bugs.contains(dirAndVer.vertex.point)) {
                        dirAndVer.vertex.wayToGo = dirAndVer.direction;
                        dirAndVer.vertex.parent = u;
                        if (!dirAndVer.vertex.isTheSameLine(this.graph.get(point))) {
                            target = dirAndVer.vertex;
                            queue = new LinkedList<>();
                            break;
                        } else queue.add(dirAndVer.vertex);
                    }
                }
            }
        }
        return (new Sign(repairRoute(null, target), howFarToTheRoot(target)));


    }


    public Target modifiedBFS(List<Point> listOfTargets, Point root, boolean careAboutOpponent,int repeated,boolean careAboutBugs) {
        Vertex target = null;
        Queue<Vertex> queue = new LinkedList<>();
        prepareGraphToBFS(root);
        queue.add(this.graph.get(root));
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();
            for (DirectionAndVertex dirAndVer : u.adjList) {
                if (!dirAndVer.vertex.visited) {
                    dirAndVer.vertex.visited = true;
                    if (!careAboutBugs | !this.bugs.contains(dirAndVer.vertex.point)) {
                        dirAndVer.vertex.wayToGo = dirAndVer.direction;
                        dirAndVer.vertex.parent = u;
                        if (listOfTargets.contains(dirAndVer.vertex.point)) {
                            if (careAboutOpponent & this.hisTarget.containsKey(dirAndVer.vertex.point)){
                                if( this.hisTarget.get(dirAndVer.vertex.point) > howFarToTheRoot(dirAndVer.vertex)) {
                                    target = dirAndVer.vertex;
                                    queue = new LinkedList<>();
                                    break;
                                } else if(listOfTargets.size() == 1) return modifiedBFS(this.bombPositions,root,false,0,true);
                            }
                            else {
                                target = dirAndVer.vertex;
                                queue = new LinkedList<>();
                                break;
                            }
                        }else queue.add(dirAndVer.vertex);
                    }
                }
            }
        }
        Point targetPoint = null;
        if(target == null) {
            if(repeated > -1 & repeated < 3)return modifiedBFS(passivePoints.subList(repeated,repeated + 1),root,false,repeated+1,true);
        }
        else targetPoint=target.point;

        return new Target(repairRoute(null, target),targetPoint);

    }

    private MoveType repairRoute(MoveType moveType, Vertex vertex) {
        if (vertex == null) return MoveType.PASS;
        if (vertex.parent != null) return repairRoute(vertex.wayToGo, vertex.parent);
        else {
            return moveType;
        }
    }


    private MoveType oppositeDirection(MoveType move) {
        if (move == null) return null;
        if (MoveType.DOWN.equals(move)) return MoveType.UP;
        if (MoveType.UP.equals(move)) return MoveType.DOWN;
        if (MoveType.LEFT.equals(move)) return MoveType.RIGHT;
        if (MoveType.RIGHT.equals(move)) return MoveType.LEFT;
        else return null;
    }


    public void disableGates(){
        for(DirectionAndVertex dirAndVert : this.graph.get(new Point(0,7)).adjList){
            if(dirAndVert.vertex.point.equals(new Point(18,7)))
                this.graph.get(new Point(0,7)).adjList.remove(new Point(18,7));
        }
        for(DirectionAndVertex dirAndVert : this.graph.get(new Point(18,7)).adjList){
            if(dirAndVert.vertex.point.equals(new Point(0,7)))
                this.graph.get(new Point(0,7)).adjList.remove(new Point(0,7));
        }
    }

    public void repairGates(){
        this.graph.get(new Point(0,7)).adjList.add(new DirectionAndVertex(MoveType.LEFT,this.graph.get(new Point(18,7))));
        this.graph.get(new Point(18,7)).adjList.add(new DirectionAndVertex(MoveType.RIGHT,this.graph.get(new Point(0,7))));
    }

}