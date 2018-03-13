<<<<<<< HEAD
import java.awt.*;
=======



>>>>>>> Paths blocking added
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
        BotParser parser = new BotParser(new BotStarter());
        parser.run();



//        String key = "field";
//        String value = "S;C,.,E1;B,x,.,.,.,.,B,.,.,.,.,.,C,x,.,.,S,.,x,.,x,.,x,x,x,x,.,x,x,x,x,.,x,.,x,.,.,x,.,.,.,x,.,.,.,.,.,.,.,x,.,.,.,x,.,.,x,x,x,.,x,.,x,x,x,x,x,.,x,.,x,x,x,.,P0,x,.,.,.,x,.,.,.,.,.,.,.,x,.,.,.,x,.,.,.,.,x,.,x,.,x,x,.,x,x,.,x,E2,x,.,.,.,x,.,x,x,.,.,.,x,x,.,x,x,.,.,E0,x,x,.,x,Gl,.,x,x,.,x,x,x,x,.,x,x,x,x,.,x,x,.,Gr,x,.,x,x,.,.,.,.,.,.,.,P1,.,.,.,x,x,.,x,.,.,.,x,.,x,x,x,x,x,x,x,x,x,.,x,.,.,.,.,x,.,.,.,.,.,.,x,x,x,.,.,.,.,.,.,x,.,.,x,.,x,x,.,x,.,.,.,.,.,x,.,x,x,.,x,.,.,x,.,x,x,.,x,x,x,x,x,x,x,E3,x,x,.,x,.,.,x,.,x,x,.,x,.,.,.,C,.,x,.,x,x,E2,x,.,S,.,.,.,.,.,.,.,x,x,x,.,.,.,.,.,.,.,S";
//
//        try{
//         GraphField field = new GraphField();
//         field.setHeight(15);
//         field.setWidth(19);
//         field.setMyId(0);
//         field.setOpponentId(1);
//        field.initField();
//        field.parseFromString(value);
//        field.initGraph();
//        field.myLastMove=new Move(MoveType.DOWN);
//        field.hisLastMove = new Move(MoveType.LEFT);
//        field.lastBugsPositions.add(new Point(2,1));
//        System.out.println(field.whereShouldIGo(0));
//
//
//
//    } catch (Exception e) {
//        System.err.println(String.format(
//                "Cannot parse game data value '%s' for key '%s'", value, key));
//        e.printStackTrace();
//    }
    }
}
