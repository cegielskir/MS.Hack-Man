import java.awt.*;

/**
 * Created by Rafał on 2018-01-22.
 */
public class Target {
    public final MoveType whereIsTheTarget;
    public final Point whatIsTheTarget;

    public Target(MoveType whereIsTheTarget, Point whatIsTheTarget) {
        this.whereIsTheTarget = whereIsTheTarget;
        this.whatIsTheTarget = whatIsTheTarget;
    }
}
