import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafał on 2018-01-18.
 */
public class Vertex {

    public List<DirectionAndVertex> adjList;
    public boolean visited;
    public MoveType wayToGo;
    public Point point;
    public Vertex parent; //uzywany do BFSa




    public Vertex (Point point){
        this.adjList = new ArrayList<>();
        this.point = point;

    }

    public Vertex goTo(MoveType moveType){
        for(DirectionAndVertex dirAndVert : this.adjList) {
            if(dirAndVert.direction .equals(moveType)) return dirAndVert.vertex;
        }
        if(moveType.equals(MoveType.PASS)) return this;
        return null;
    }
    public boolean areAdj(Vertex vertex){
        for(DirectionAndVertex dirAndVert : this.adjList) if(dirAndVert.vertex.equals(vertex)) return true;
        return false;
    }


//    private boolean hasHorizontalAdj(){
//        for(DirectionAndVertex dirAndVert : this.adjList) {
//            if(dirAndVert.direction == MoveType.LEFT | dirAndVert.direction == MoveType.RIGHT) return true;
//        }
//        return false;
//    }
//
//    private boolean hasVerticalAdj(){
//        for(DirectionAndVertex dirAndVert : this.adjList) {
//            if(dirAndVert.direction == MoveType.UP | dirAndVert.direction == MoveType.DOWN) return true;
//        }
//        return false;
//    }
//
//    public boolean isCorner(){
//        if( hasHorizontalAdj() & hasVerticalAdj()) return true;
//        else return false;
//    }
    private boolean isInOneLine(Vertex vertex,MoveType moveType){
        if(this == vertex) return true;
        while(vertex.adjList.contains(new DirectionAndVertex(moveType))){
            vertex = vertex.adjList.get(vertex.adjList.indexOf( new DirectionAndVertex(moveType))).vertex;
            if(this == vertex) return true;
        }
        return false;
    }

    public boolean isTheSameLine(Vertex vertex){
        if((this.point.equals(new Point(0,7)) | this.point.equals(new Point(1,7))) &
                (vertex.point.equals(new Point(18,7)) | vertex.point.equals(new Point(17,7)))) return false;
        return isInOneLine(vertex,MoveType.DOWN) |
               isInOneLine(vertex,MoveType.LEFT) |
               isInOneLine(vertex,MoveType.RIGHT)|
               isInOneLine(vertex,MoveType.UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;

        Vertex vertex = (Vertex) o;

        if(this.point.equals(vertex.point)) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        return point != null ? point.hashCode() : 0;
    }
}
