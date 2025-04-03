package com.deflanko.MCCFishingMessages.config;

import com.google.gson.*;
import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.util.*;

public class Config {

    public static final InstanceCreator<Config> INSTANCE_CREATOR = type -> new Config();


    @SuppressWarnings("unused")
    public Config() {
    }

    public Config(Config other) {
        this.boxX = other.boxX;
        this.boxY = other.boxY;
        this.boxWidth = other.boxWidth;
        this.boxHeight = other.boxHeight;
        this.fontSize = other.fontSize;

        if(other.pulledPhrases.isEmpty()){
            setDefaultPrefrences();
        }else {
            this.pulledPhrases = other.pulledPhrases;
        }

        if(other.blockedPhrases.isEmpty()){
            setDefaultBlockedPhrases();
        }else {
            this.blockedPhrases = other.blockedPhrases;
        }
    }

    @SerialEntry
    public int boxX = 0;// Default position
    @SerialEntry
    public int boxY = 30; // Top of screen, below  bar
    @SerialEntry
    public int boxWidth = 330;
    @SerialEntry
    public int boxHeight = 110;
    @SerialEntry
    public float fontSize = 1.0f;
    @SerialEntry
    public List<String> pulledPhrases= new ArrayList<>();
    @SerialEntry
    public List<String> blockedPhrases= new ArrayList<>();


    private void setDefaultPrefrences(){
        pulledPhrases.add("all pots are fully repaired!");
        pulledPhrases.add( "an error occurred whilst catching a fish. please try again or use /bugreport for details of how to report bugs. your consumables will not be consumed.");
        pulledPhrases.add("changed crab pot climate!");
        pulledPhrases.add("crab pot claimed! contents sent to your infinibag.");
        pulledPhrases.add("fishing spot stock replenished!");
        pulledPhrases.add("fishing stock has replenished!");
        pulledPhrases.add(" has run out of uses");
        pulledPhrases.add("repaired crab pots!");
        pulledPhrases.add("research meter ready to claim! check your a.n.g.l.r. panel.");
        pulledPhrases.add("special:");
        pulledPhrases.add("that's not a fishing spot! locate one and cast there.");
        pulledPhrases.add("this spot is depleted, so you can no longer fish here.");
        pulledPhrases.add("triggered:");
        pulledPhrases.add("while active, all islands in this climate receive:");
        pulledPhrases.add("you caught:");
        pulledPhrases.add( "you earned:");
        pulledPhrases.add("you moved too far away from your currently cast spot, and your cast was canceled");
        pulledPhrases.add( "you receive:");
        pulledPhrases.add( "you've discovered a ");
        pulledPhrases.add( "you've reached fishing level");
        pulledPhrases.add( "you've run out of your equipped");
        pulledPhrases.add( "your grotto has become unstable, teleporting you back to safety...");
    }

    private void setDefaultBlockedPhrases(){

    }
}
