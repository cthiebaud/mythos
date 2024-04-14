package com.cthiebaud.mythos.model;

import static com.cthiebaud.mythos.model.Model.MODEL;

import com.cthiebaud.mythos.model.Model.Actor;

/* 
     mvn exec:java -q -Dexec.mainClass="com.cthiebaud.mythos.model.Main" -Dexec.args="Athena Ulysses" 
*/
public class Main {
    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            Actor actor = MODEL.findActorByName(args[i]).orElse(null);
            if (actor == null) {
                System.out.printf("\n%s: <Not Found!>\n", args[i]);
            } else {
                System.out.printf("\n%s: %s\n", actor.getName(), actor.getHtmlDescription());
            }
            System.out.println();
        }

        var a = MODEL.getRandomActor();
        System.out.printf("\n%s: %s\n", a.getName(), a.getHtmlDescription());
    }
}