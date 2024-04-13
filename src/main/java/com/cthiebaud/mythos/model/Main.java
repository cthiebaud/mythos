package com.cthiebaud.mythos.model;

import com.cthiebaud.mythos.model.Model.Actor;

/* 
     mvn exec:java -q -Dexec.mainClass="com.cthiebaud.mythos.model.Main" -Dexec.args="Athena Ulysses" 
*/
public class Main {
    public static void main(String[] args) {

        Model model = Model.INSTANCE;

        for (int i = 0; i < args.length; i++) {
            Actor actor = model.findActorByName(args[i]).orElse(null);
            if (actor == null) {
                System.out.printf("\n%s: <Not Found!>\n", args[i]);
            } else {
                System.out.printf("\n%s: %s\n", actor.getName(), actor.getHtmlDescription());
            }
            System.out.println();
        }
    }
}