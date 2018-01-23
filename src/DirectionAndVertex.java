import java.awt.*;

/**
 * Created by Rafał on 2018-01-18.
 */
public class DirectionAndVertex {

    final public MoveType direction;
    final public Vertex vertex;

    public DirectionAndVertex(MoveType direction, Vertex vertex) {
        this.direction = direction;
        this.vertex = vertex;
    }

    public DirectionAndVertex(MoveType direction) {
        this.direction = direction;
        this.vertex = new Vertex(new Point(-1,-1));
    }

    @Override
    public String toString() {
        return "DirectionAndVertex{" +
                "direction=" + direction +
                ", vertex=" + vertex.point +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectionAndVertex)) return false;

        DirectionAndVertex that = (DirectionAndVertex) o;

        if (direction == that.direction) return true;
        else return false;

    }

    @Override
    public int hashCode() {
        int result = direction != null ? direction.hashCode() : 0;
        result = 31 * result + (vertex != null ? vertex.hashCode() : 0);
        return result;
    }
}
