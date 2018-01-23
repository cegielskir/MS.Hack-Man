import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Rafał on 2018-01-22.
 */
public class FarChaseBug extends Bug{

    public FarChaseBug(Point point) {
        this.point = point;
    }

    @Override
    public MoveType whereItWillGo(GraphField graphField) {
        ArrayList<Point> listOfPlayers = new ArrayList<>();
        listOfPlayers.add(graphField.opponentPosition);
        listOfPlayers.add(graphField.myPosition);
        graphField.disableGates();
        Target theClosest = graphField.modifiedBFS(listOfPlayers,this.point,false,-1,false);
        listOfPlayers.remove(theClosest.whatIsTheTarget);
        MoveType returningMovement = graphField.modifiedBFS(listOfPlayers,this.point,false,-1,false).whereIsTheTarget;
        graphField.repairGates();
        return returningMovement;

    }
}
