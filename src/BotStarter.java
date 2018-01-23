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
//        String value = "S,.,E0,x,.,.,.,.,.,.,.,.,.,.,.,x,.,.,S,.,x,.,x,.,x,x,x,x,.,x,x,x,x,.,x,.,x,.,.,x,.,.,.,x,.,.,.,.,.,.,.,x,.,.,.,x,.,.,x,x,x,.,x,.,x,x,x,x,x,.,x,.,x,x,x,.,.,x,.,.,.,x,.,.,.,.,.,.,.,x,.,.,.,x,.,.,.,.,x,.,x,.,x,x,.,x,x,.,x,.,x,.,.,.,x,.,x,x,.,.,.,x,x,.,x,x,.,.,.,x,x,.,x,Gl,.,x,x,.,x,x,x,x,.,x,x,x,x,.,x,x,.,Gr,x,.,x,x,.,.,.,.,.,.,.,.,.,.,.,x,x,.,x,.,.,.,x,.,x,x,x,x,x,x,x,x,x,.,x,P1,.,.,.,x,.,.,.,.,.,.,x,x,x,.,.,.,.,.,.,x,.,.,x,.,x,x,.,x,.,.,.,.,.,x,.,x,x,.,x,.,.,x,.,x,x,.,x,x,x,x,x,x,x,.,x,x,.,x,.,.,x,.,x,x,.,x,.,.,.,.,.,x,.,x,x,P0,x,.,S,.,.,C,.,.,.,.,x,x,x,C,.,.,.,.,.,.,S";
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
//        //field.getGraph().drawGraph();
//            System.out.println(field.whereShouldIGo(0));
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
