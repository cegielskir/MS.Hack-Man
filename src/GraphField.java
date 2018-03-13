
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
    private List<Point> passivePoints;
    private List<Point> gates;
    private List<Point> crosses = new ArrayList<>();



    public GraphField() {
        super();
        graph = new HashMap<>();
        passivePoints = new ArrayList<>();
        passivePoints.add(new Point(9,8));
        passivePoints.add(new Point(4,4));
        passivePoints.add(new Point(14,4));
        passivePoints.add(new Point(10,10));
        gates = new ArrayList<>();
        gates.add(new Point(0,7));
        gates.add(new Point(18,7));
    }


    public void initGraph() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (!field[y][x].equals("x")) this.graph.put(new Point(x, y), new Vertex(new Point(x, y)));
            }
        }
        fillAdjLists();
        detectCrosses();
        howFarToCrosses();
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

    private void detectCrosses(){
        for(Vertex vert : this.graph.values()){
            if(vert.adjList.size() > 2 ) this.crosses.add(vert.point);
        }
    }

    private void howFarToCrosses(){
        for(Point point: this.graph.keySet()){
            if(this.graph.get(point).adjList.size() > 2) this.graph.get(point).closestCrosses.add(new Sign(MoveType.PASS,0));
            else this.graph.get(point).closestCrosses.addAll(crossesBFS(point));
        }
    }

    public Move whereShouldIGo(int bombs) throws IllegalArgumentException {
//        if(this.round == 249) throw new IllegalArgumentException(err);
        this.err = this.err + "Round: " + this.round + "\n";
        this.round++;
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


    private MoveType lastBugMove(Point bugPoint){
        for(Point lastBugPoint : this.lastBugsPositions){
            Vertex lastVert = this.graph.get(lastBugPoint);
            if(lastVert.areAdj(this.graph.get(bugPoint))){
                this.err = this.err+"Last bug point: " + lastBugPoint + "\n";
                return lastVert.whichAdj(this.graph.get(bugPoint));

            }
        }
        return null;
    }

    private void initInavailablePositions(int bombs) {

        this.bugs = new ArrayList<>();
        this.bugs.addAll(this.spawnPoints);
        List<Point> enemyPoints = new ArrayList<>();
        for(Bug bug : this.enemies){
            enemyPoints.add(bug.point);
        }
        for(Bug bug : this.enemies){
            Vertex vert = this.graph.get(bug.point);
            MoveType lastMove = lastBugMove(bug.point);
            this.err = this.err + "Bug: " + bug.point + " his last move: " + lastMove + "\n";
            int i = 4;
            int howFarFromTheClosestBug = howFarFromTheClosest(this.myPosition,enemyPoints);
            i = i + 6 - howFarFromTheClosestBug;
            while( lastMove != null && i >0){
                if(vert.adjList.size() == 2){
                    this.bugs.add(vert.point);
                    lastMove = vert.anotherDir(oppositeDirection(lastMove));
                    vert = vert.goTo(lastMove);
                    if(vert.point.equals(this.myPosition)) i=-10;
                    i--;
                }
                else {
                    List<Point> myPoint = new ArrayList<>();
                    List<Point> bugPoints = new ArrayList<>();
                    for(Bug bugg : this.enemies){
                        bugPoints.add(bugg.point);
                    }
                    myPoint.add(this.myPosition);
                    int my,his;
                    my = howFarFromTheClosest(vert.point,myPoint);
                    his= howFarFromTheClosest(vert.point,bugPoints);
                    if(his <= my) this.bugs.add(vert.point);

                    i = -1;
                }
            }
        }
        for (Bug bug : this.enemies) {
            if(bug instanceof PredictBug){
                List<Point> myPositionInList = new ArrayList<>();
                if(!(this.graph.get(bug.point).adjList.size() > 2) || !(howFarFromTheClosest(bug.point,myPositionInList) < 5))
                for(DirectionAndVertex dirAndVer : this.graph.get(bug.point).adjList){
                    if(!this.lastBugsPositions.contains(dirAndVer.vertex.point)) this.bugs.add(bug.point);

                }
            }
//            if(this.graph.get(bug.point).areAdj(this.graph.get(this.myPosition)) && !this.lastBugsPositions.contains(this.myPosition)){
//                this.bugs.add(bug.point);
//            }
            for(DirectionAndVertex dirAndVer : this.graph.get(bug.point).adjList){
                if(this.snippetPositions.contains(dirAndVer.vertex.point) && !this.lastBugsPositions.contains(dirAndVer.vertex.point)){
                    this.bugs.add(dirAndVer.vertex.point);
                }
                if(this.bombPositions.contains(dirAndVer.vertex.point)&& !this.lastBugsPositions.contains(dirAndVer.vertex.point)){
                    this.bugs.add(dirAndVer.vertex.point);
                }
            }
            Point newBugPoint = this.graph.get(bug.point).goTo(bug.whereItWillGo(this)).point;
            if(!this.lastBugsPositions.contains(newBugPoint)) this.bugs.add(newBugPoint);
            if(this.myPosition.equals(newBugPoint)) this.bugs.add(bug.point);
            this.whereWillBugsGo.add(newBugPoint);


        }
    }

    private int howFarFromTheClosest(Point point,List<Point> listOfTargets) {
        Vertex theTarget = null;
        Queue<Vertex> queue = new LinkedList<>();
        prepareGraphToBFS(point);
        queue.add(this.graph.get(point));
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();
            for (DirectionAndVertex dirAndVer : u.adjList) {
                if (!dirAndVer.vertex.visited) {
                    dirAndVer.vertex.visited = true;
                    dirAndVer.vertex.wayToGo = oppositeDirection(dirAndVer.direction);
                    dirAndVer.vertex.parent = u;
                    if (listOfTargets.contains(dirAndVer.vertex.point)) {
                        theTarget = dirAndVer.vertex;
                        queue = new LinkedList<>();
                        break;
                    }
                    queue.add(dirAndVer.vertex);
                }
            }
        }
        return howFarToTheRoot(theTarget);
    }


    private boolean isBugTotClose(){
        for(DirectionAndVertex dirAndVert : this.graph.get(this.myPosition).adjList){
            if(this.enemyPositions.contains(dirAndVert.vertex.point) && ) return true;
            for(DirectionAndVertex dirAndVert2 : dirAndVert.vertex.adjList){
                if(this.enemyPositions.contains(dirAndVert2.vertex.point)) return true;
            }
        }
        return false;
    }


    private Move passiveMovement(int bombs) {
        MoveType whereToGo;
        initInavailablePositions(bombs);
        howFarFromOpponentBFS();
        this.bugs.addAll(dontGoThere);
        if(isBugTotClose()){
            if(this.myPosition.equals(new Point(0,7))) return new Move(MoveType.LEFT);
            else if(this.myPosition.equals(new Point(18,7))) return new Move(MoveType.RIGHT);
            else return new Move(modifiedBFS(this.gates,this.myPosition,false,0,true).whereIsTheTarget);
        }

        whereToGo = modifiedBFS(this.snippetPositions,this.myPosition, true,0,true).whereIsTheTarget;
        if (bombs > 0 && !whereToGo.equals(MoveType.PASS)) {
            Vertex whereWouldIBe = this.graph.get(this.getMyPosition()).goTo(whereToGo);
            MoveType whereWillHeGo = modifiedBFS(this.snippetPositions,this.opponentPosition,true,0,true).whereIsTheTarget;
            Vertex whereWillHeBe = this.graph.get(this.getOpponentPosition()).goTo(whereWillHeGo);
            Sign hisSign = runAwayBFS(whereWouldIBe.point,whereWillHeBe.point);
            Sign mySign = runAwayBFS(whereWouldIBe.point, whereWouldIBe.point);
            List<Point> enemies = new ArrayList<>();
            for(Bug bug : this.enemies) enemies.add(bug.point);
            if ((this.graph.get(this.getOpponentPosition()).isTheSameLine(whereWouldIBe) && mySign.howFar < hisSign.howFar)){
                this.dontGoThere.add(whereWouldIBe.point);
                return new Move(whereToGo, mySign.howFar);
            }
//            this part is responsible for using bombs on bugs
            else{
                int howManyBugs = 0;
                for(Point point : enemies){
                    if(this.graph.get(point).isTheSameLine(whereWouldIBe)){
                        howManyBugs++;
                        if(howManyBugs > 1) {
                            this.dontGoThere.add(whereWouldIBe.point);
                            return new Move(whereToGo, mySign.howFar);
                        }
                    }
                }
            }
        }
        for(Point point: this.bugs){
            this.err = this.err + "Zablokowany punkt: " + point + "\n";
        }
        if(whereToGo.equals(MoveType.PASS)) return new Move(goAwayFromBugs());
        return new Move(whereToGo);
    }

    private MoveType goAwayFromBugs(){
        this.err = this.err + "I was here \n";
        for(DirectionAndVertex directionAndVertex : this.graph.get(this.myPosition).adjList) {
            if(!this.whereWillBugsGo.contains(directionAndVertex.vertex.point) &&
                !this.enemyPositions.contains(directionAndVertex.vertex.point)) return directionAndVertex.direction;
        }
        for(DirectionAndVertex directionAndVertex : this.graph.get(this.myPosition).adjList){
            if(!this.enemyPositions.contains(directionAndVertex.vertex.point) &&
                    !this.myLastMove.equals(oppositeDirection(directionAndVertex.direction))) return directionAndVertex.direction;
        }
        for(DirectionAndVertex directionAndVertex : this.graph.get(this.myPosition).adjList) {
        if(!this.myLastMove.equals(oppositeDirection(directionAndVertex.direction))) return directionAndVertex.direction;
        }
        for(DirectionAndVertex directionAndVertex : this.graph.get(this.myPosition).adjList) {
            if(!this.enemyPositions.contains(directionAndVertex.vertex.point)) return directionAndVertex.direction;
        }
        this.err = this.err + "And here \n";
        return MoveType.PASS;
    }


    private boolean isRightPoint(Point point) {
        if (this.graph.get(point) == null) return false;
        else return true;
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

    private Sign runAwayBFS(Point bombPoint, Point whereIAm) {
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
                        if (!dirAndVer.vertex.isTheSameLine(this.graph.get(bombPoint))) {
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


    private List<Sign> crossesBFS( Point point) {
        List<Vertex> listOfTargetVertexes = new ArrayList<>();
        Queue<Vertex> queue = new LinkedList<>();
        prepareGraphToBFS(point);
        queue.add(this.graph.get(point));
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();
            for (DirectionAndVertex dirAndVer : u.adjList) {
                if (!dirAndVer.vertex.visited) {
                    dirAndVer.vertex.visited = true;
                    dirAndVer.vertex.wayToGo = dirAndVer.direction;
                    dirAndVer.vertex.parent = u;
                    if (this.crosses.contains(dirAndVer.vertex.point)) {
                        listOfTargetVertexes.add(dirAndVer.vertex);
                    } else queue.add(dirAndVer.vertex);
                }
            }
        }
        List<Sign> listOfSigns = new ArrayList<>();
        for(Vertex target : listOfTargetVertexes){
            listOfSigns.add(new Sign(repairRoute(null, target), howFarToTheRoot(target)));
        }
        return listOfSigns;


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
                    if (!careAboutBugs || !this.bugs.contains(dirAndVer.vertex.point)) {
                        dirAndVer.vertex.wayToGo = dirAndVer.direction;
                        dirAndVer.vertex.parent = u;
                        if (listOfTargets.contains(dirAndVer.vertex.point)) {
                            if (careAboutOpponent && this.hisTarget.containsKey(dirAndVer.vertex.point)){
                                if( this.hisTarget.get(dirAndVer.vertex.point) > howFarToTheRoot(dirAndVer.vertex)) {
                                    target = dirAndVer.vertex;
                                    queue = new LinkedList<>();
                                    break;
                                } else if(listOfTargets.size() == 1) {
                                    return modifiedBFS(this.bombPositions,root,false,0,true);
                                }
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