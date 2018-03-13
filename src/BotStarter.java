


import java.awt.*;
import java.util.Random;

public class BotStarter {

    private Random random;

    private BotStarter() {
        this.random = new Random();
    }

    public CharacterType getCharacter() {
        CharacterType[] characters = CharacterType.values();
        return characters[this.random.nextInt(characters.length)];
    }

    public Move doMove(BotState state) {

        Move myMove = state.getGraphField().whereShouldIGo(state.getPlayers().get(state.getMyName()).getBombs());
        state.getGraphField().setHisLastPosition(state.getGraphField().getOpponentPosition());
        return myMove;
    }

    public static void main(String[] args) {
        try{
        BotParser parser = new BotParser(new BotStarter());
        parser.run();



//        String key = "field";
//        String value ="S,.,.,x,.,.,.,.,.,.,.,.,E1,.,.,x,E2,.,S,.,x,.,x,B,x,x,x,x,C,x,x,x,x,.,x,.,x,.,.,x,.,.,.,x,.,C,E3,.,.,E0,.,x,.,.,.,x,P0,.,x,x,x,.,x,.,x,x,x,x,x,.,x,.,x,x,x,.,.,x,.,.,.,x,.,.,.,.,.,.,.,x,.,.,.,x,.,.,.,.,x,.,x,.,x,x,.,x,x,.,x,.,x,.,.,.,x,.,x,x,.,.,.,x,x,.,x,x,.,.,.,x,x,E2,x,Gl,.,x,x,.,x,x,x,x,.,x,x,x,x,.,x,x,.,Gr,x,.,x,x,.,.,.,C,.,.,.,.,.,.,.,x,x,.,x,.,.,.,x,.,x,x,x,x,x,x,x,x,x,.,x,.,.,.,.,x,P1,.,.,.,.,.,x,x,x,.,.,.,.,.,.,x,.,.,x,.,x,x,.,x,.,.,.,.,.,x,.,x,x,.,x,.,.,x,.,x,x,.,x,x,x,x,x,x,x,.,x,x,.,x,.,.,x,.,x,x,.,x,.,.,.,.,.,x,.,x,x,.,x,.,S,.,.,.,.,.,.,.,x,x,x,.,.,.,.,.,.,.,S";
//

//
//         GraphField field = new GraphField();
//         field.setHeight(15);
//         field.setWidth(19);
//         field.setMyId(0);
//         field.setOpponentId(1);
//        field.initField();
//        field.parseFromString(value);
//        field.initGraph();
//        field.myLastMove=new Move(MoveType.UP);
//        field.hisLastMove = new Move(MoveType.UP);
//        field.lastBugsPositions.add(new Point(16,1));field.lastBugsPositions.add(new Point(17,7));
//        for(Vertex vert : field.graph.values()){
//            System.out.println("Point: " + vert.point);
//            for(Sign sign : vert.closestCrosses) {
//                System.out.println("Where: " + sign.where + " how far: " + sign.howFar );
//            }
//        }

//        System.out.println(field.whereShouldIGo(0));




    } catch ( Exception es) {
            System.err.println(es);
        }
    }
}
