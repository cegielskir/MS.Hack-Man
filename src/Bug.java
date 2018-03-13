import java.awt.*;

/**
 * Created by Rafał on 2018-01-21.
 */
public abstract class Bug {
    public Point point;
    public abstract MoveType whereItWillGo(GraphField graphField);

    protected Point findTheClosest(GraphField graphField, Point point){
        for(int i =1 ; i<15 ; i++){
            if(graphField.graph.containsKey(new Point(point.x +i,point.y))) return new Point(point.x + i,point.y);
            if(graphField.graph.containsKey(new Point(point.x ,point.y+i))) return new Point(point.x ,point.y+i);
            if(graphField.graph.containsKey(new Point(point.x -i,point.y))) return new Point(point.x - i,point.y);
            if(graphField.graph.containsKey(new Point(point.x ,point.y -i))) return new Point(point.x,point.y-i);
            if(graphField.graph.containsKey(new Point(point.x-i ,point.y -i))) return new Point(point.x-i,point.y-i);
            if(graphField.graph.containsKey(new Point(point.x +i,point.y -i))) return new Point(point.x+i,point.y-i);
            if(graphField.graph.containsKey(new Point(point.x +i,point.y +i))) return new Point(point.x+i,point.y+i);
            if(graphField.graph.containsKey(new Point(point.x -i,point.y +i))) return new Point(point.x-i,point.y+i);
        }
        return new Point(9,8);

    }

}
