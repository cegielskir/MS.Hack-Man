import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Rafał on 2018-01-21.
 */
public class ChaseBug extends Bug {

    public ChaseBug(Point point) {
        this.point = point;
    }

    @Override
    public MoveType whereItWillGo(GraphField graphField) {
        ArrayList<Point>  playersPositions = new ArrayList<>();
        playersPositions.add(graphField.myPosition);
        playersPositions.add(graphField.opponentPosition);
        graphField.disableGates();
        MoveType returningMovement = graphField.modifiedBFS(playersPositions,this.point,false,-1,false).whereIsTheTarget;
        graphField.repairGates();
        return returningMovement;
    }
}
