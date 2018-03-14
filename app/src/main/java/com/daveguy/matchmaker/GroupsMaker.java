package com.daveguy.matchmaker;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Daveguy on 2016-10-01.
 */

class GroupsMaker {

    private Random r;

    GroupsMaker(){
        r = new Random();
    }
    /*
    Pick Banks of games
     */
    public LinkedList<String[]> getGamePicks(int numBanks, String[] games){
        if(games.length < 4){
            return null;
        }
        LinkedList<String[]> gamePicks = new LinkedList<>();
        fillPicks(gamePicks, games, numBanks);
        return gamePicks;
    }

    private void fillPicks(LinkedList<String[]> picks, String[] games, int numBanks) {
        int[] counts = new int[games.length];

        for(int i = 0; i < numBanks; i++){
            String[] bank = new String[4];
            LinkedList<Integer> picksSoFar = new LinkedList<>();
            int index = 0;
            boolean done = false;
            while(!done){
                int pick = r.nextInt(games.length);
                if(!picksSoFar.contains(pick) && diffOk(pick, counts)){
                    picksSoFar.add(pick);
                    bank[index] = games[pick];
                    counts[pick]++;
                    index++;
                }
                if(index == 4){
                    done = true;
                }
            }
            picks.add(bank);
        }
    }



    private boolean diffOk(int pick, int[] counts) {
        boolean diffOk = true;

        for (int count : counts) {
            if (counts[pick] - count > 2) {
                diffOk = false;
            }
        }
        return diffOk;
    }

    /*
    Pick groups of players
     */
    public LinkedList<String[]> getPlayerGroups(String[] players){
        if(players.length < 3){
            return null;
        }
        LinkedList<String[]> groups = new LinkedList<>();
        pickGroups(groups, players);
        return groups;
    }

    private void pickGroups(LinkedList<String[]> groups,String[] players)
    {
        int numThree = getGroupsOfThree(players.length);
        int numFour = numThree > 0? (players.length / 4 + 1) - numThree : players.length / 4;
        int index = 0;
        for(int i = 0; i < numFour; i++){
            groups.add(new String[]{players[index++], players[index++], players[index++], players[index++]});
        }
        for(int i = 0; i < numThree; i++){
            groups.add(new String[]{players[index++], players[index++], players[index++], "Ghost"});
        }
    }

    //deprecated, groups are no longer random
    private void pickGroupsOld(LinkedList<String[]> groups,String[] players) {
        int[] alreadyPicked = new int[players.length];
        int numThree = getGroupsOfThree(players.length);
        int numFour = numThree > 0? (players.length / 4 + 1) - numThree : players.length / 4;
        for(int i = 0; i < numFour; i++){
            String[] group = pickGroup4(players, alreadyPicked);
            groups.add(group);
        }
        for(int i = 0; i < numThree; i++){
            String[] group = pickGroup3(players, alreadyPicked);
            groups.add(group);
        }
    }

    //deprecated, groups are no longer random
    private String[] pickGroup3(String[] players, int[] alreadyPicked) {
        String[] group = new String[]{null, null,null,"Ghost"};
        int count = 0;
        while (count < 3){
            int pick = r.nextInt(players.length);
            if(alreadyPicked[pick] != 1){
                alreadyPicked[pick] = 1;
                group[count] = players[pick];
                count++;
            }
        }
        return group;
    }

    //deprecated, groups are no longer random
    private String[] pickGroup4(String[] players, int[] alreadyPicked) {
        String[] group = new String[]{null, null,null,null};
        int count = 0;
        while (count < 4){
            int pick = r.nextInt(players.length);
            if(alreadyPicked[pick] != 1){
                alreadyPicked[pick] = 1;
                group[count] = players[pick];
                count++;
            }
        }
        return group;
    }

    private int getGroupsOfThree(int numPlayers) {
        int r = numPlayers % 4;
        int numGroupsThree;
        switch (r){
            case 1:
                numGroupsThree = 3;
                break;
            case 2:
                numGroupsThree = 2;
                break;
            case 3:
                numGroupsThree = 1;
                break;
            default:
                numGroupsThree = 0;
        }

        return numGroupsThree;
    }
}
