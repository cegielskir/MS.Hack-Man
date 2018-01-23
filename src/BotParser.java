import java.util.Scanner;


public class BotParser {

    private Scanner scan;
    private BotStarter bot;
    private boolean isFirstTime;

    private BotState currentState;

    BotParser(BotStarter bot) {
        this.scan = new Scanner(System.in);
        this.bot = bot;
        this.currentState = new BotState();
        this.isFirstTime = true;
    }

    /**
     *
     * Run will keep reading output from the engine.
     * Will either update the bot state or get actions.
     */
    void run() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "settings":
                    parseSettings(parts[1], parts[2]);
                    break;
                case "update":
                    if (parts[1].equals("game")) {
                        parseGameData(parts[2], parts[3]);
                    } else {
                        parsePlayerData(parts[1], parts[2], parts[3]);
                    }
                    break;
                case "action":
                    if (parts[1].equals("character")) {  // return character
                        System.out.println(this.bot.getCharacter().toString());
                    } else if (parts[1].equals("move")) {  // return move
                        Move move = this.bot.doMove(this.currentState);
                        if (move != null) {
                            System.out.println(move.toString());
                        } else {
                            System.out.println(MoveType.PASS.toString());
                        }
                    }
                    break;
                default:
                    System.err.println("Unknown command");
                    break;
            }
        }
    }

    /**
     * Parses all the game settings given by the engine
     * @param key Type of setting given
     * @param value Value
     */
    private void parseSettings(String key, String value) {
        try {
            switch(key) {
                case "timebank":
                    int time = Integer.parseInt(value);
                    this.currentState.setMaxTimebank(time);
                    this.currentState.setTimebank(time);
                    break;
                case "time_per_move":
                    this.currentState.setTimePerMove(Integer.parseInt(value));
                    break;
                case "player_names":
                    String[] playerNames = value.split(",");
                    for (String playerName : playerNames) {
                        this.currentState.getPlayers().put(playerName, new Player(playerName));
                    }
                    break;
                case "your_bot":
                    this.currentState.setMyName(value);
                    break;
                case "your_botid":
                    int myId = Integer.parseInt(value);
                    int opponentId = 2 - (myId + 1);
                    this.currentState.getGraphField().setMyId(myId);
                    this.currentState.getGraphField().setOpponentId(opponentId);
                    break;
                case "field_width":
                    this.currentState.getGraphField().setWidth(Integer.parseInt(value));
                    break;
                case "field_height":
                    this.currentState.getGraphField().setHeight(Integer.parseInt(value));
                    break;
                case "max_rounds":
                    this.currentState.setMaxRounds(Integer.parseInt(value));
                    break;
                default:
                    System.err.println(String.format(
                            "Cannot parse settings input with key '%s'", key));
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse settings value '%s' for key '%s'", value, key));
            e.printStackTrace();
        }
    }

    private void parseGameData(String key, String value) {
        try {
            switch(key) {
                case "round":
                    this.currentState.setRoundNumber(Integer.parseInt(value));
                    break;
                case "field":
                    this.currentState.getGraphField().initField();
                    this.currentState.getGraphField().parseFromString(value);
                    if(isFirstTime)  this.currentState.getGraphField().initGraph();
                    //this.currentState.getField().updateGraph();
                    break;
                default:
                    System.err.println(String.format(
                            "Cannot parse game data input with key '%s'", key));
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse game data value '%s' for key '%s'", value, key));
            e.printStackTrace();
        }
    }

    private void parsePlayerData(String playerName, String key, String value) {
        Player player = this.currentState.getPlayers().get(playerName);

        if (player == null) {
            System.err.println(String.format("Could not find player with name %s", playerName));
            return;
        }

        try {
            switch(key) {
                case "bombs":
                    player.setBombs(Integer.parseInt(value));
                    break;
                case "snippets":
                    player.setSnippets(Integer.parseInt(value));
                    break;
                default:
                    System.err.println(String.format(
                            "Cannot parse %s data input with key '%s'", playerName, key));
            }
        } catch (Exception e) {
            System.err.println(String.format(
                    "Cannot parse %s data value '%s' for key '%s'", playerName, value, key));
            e.printStackTrace();
        }
    }
}
