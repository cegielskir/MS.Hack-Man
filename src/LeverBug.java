import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Rafał on 2018-01-22.
 */
public class LeverBug extends Bug{

    public LeverBug(Point point) {
        this.point = point;
    }

    @Override
    public MoveType whereItWillGo(GraphField graphField) {
        graphField.disableGates();
        ArrayList<Point> listOfPlayers = new ArrayList<>();
        listOfPlayers.add(graphField.myPosition);
        listOfPlayers.add(graphField.opponentPosition);
        Target player = graphField.modifiedBFS(listOfPlayers,this.point,false,-1,false);
        ArrayList<Point> enemies = new ArrayList<>();
        for(Bug bug : graphField.enemies) enemies.add(bug.point);
//        if(enemies.size()!= 1) enemies.remove(this.point);
        Target bug = graphField.modifiedBFS(enemies,player.whatIsTheTarget,false,-1,false);
        ArrayList<Point> theTarget = new ArrayList<>();
        Point target = new Point(player.whatIsTheTarget.x + (player.whatIsTheTarget.x - bug.whatIsTheTarget.x),
                                    player.whatIsTheTarget.y + (player.whatIsTheTarget.y - bug.whatIsTheTarget.y));
        if(!graphField.graph.containsKey(target)) {
            target = findTheClosest(graphField,target);
        }
        theTarget.add(target);
        MoveType returningMovement = graphField.modifiedBFS(theTarget,this.point,false,1,false).whereIsTheTarget;
        return returningMovement;

    }
}
