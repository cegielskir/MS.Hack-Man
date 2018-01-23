import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Rafał on 2018-01-22.
 */
public class PredictBug extends Bug{

    public PredictBug(Point point) {
        this.point = point;
    }

    @Override
    public MoveType whereItWillGo(GraphField graphField) {
        graphField.disableGates();
        ArrayList<Point> players = new ArrayList<>();
        players.add(graphField.opponentPosition);
        players.add(graphField.myPosition);
        Target target = graphField.modifiedBFS(players,this.point,false,-1,false);
        ArrayList<Point> theTarget = new ArrayList<>();
        Point predicted ;
        if(target.whatIsTheTarget.equals(graphField.myPosition)) predicted = predicting(graphField.myPosition,graphField.myLastMove.moveType);
        else predicted = predicting(graphField.opponentPosition,graphField.hisLastMove.moveType);
        if(!graphField.graph.containsKey(predicted)) {
            predicted = findTheClosest(graphField,predicted);
        }
        theTarget.add(predicted);
        MoveType returningMovement = graphField.modifiedBFS(theTarget,this.point,false,-1,false).whereIsTheTarget;
        graphField.repairGates();
        return returningMovement;
    }


    private Point predicting(Point point, MoveType moveType){
        if(moveType.equals(MoveType.DOWN)) return new Point(point.x,point.y + 4);
        if(moveType.equals(MoveType.UP)) return new Point(point.x,point.y - 4);
        if(moveType.equals(MoveType.LEFT)) return new Point(point.x-4,point.y);
        if(moveType.equals(MoveType.RIGHT)) return new Point(point.x+4,point.y);
        return point;
    }
}
