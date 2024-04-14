package com.cthiebaud.mythos.model;

import static com.cthiebaud.mythos.model.Model.MODEL;

import com.cthiebaud.mythos.model.Model.Actor;

/* 
     mvn exec:java -q -Dexec.mainClass="com.cthiebaud.mythos.model.Main" -Dexec.args="Athena Ulysses" 
*/
public class Main {
    public static void main(String[] args) {

        final String fmt = "-\n%s:\n%s\n";

        for (int i = 0; i < args.length; i++) {
            Actor actor = MODEL.findActorByName(args[i]).orElse(null);
            if (actor == null) {
                System.out.printf(fmt, args[i], "<Not Found!>");
            } else {
                System.out.printf(fmt, actor.getName(), actor.getHtmlDescription());
            }
        }
        {
            Actor a = MODEL.getRandomActor();
            System.out.printf(fmt, a.getName(), a.getHtmlDescription());
        }
        {
            MODEL.actorList().stream().forEach((a) -> System.out.printf(fmt, a.getName(), a.getHtmlDescription()));
        }
    }
}