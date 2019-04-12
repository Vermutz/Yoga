package com.example.alekseyignatenko.yoga;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class SavePractice implements Serializable {
    LinkedList<String> Asanalist;
    HashMap<String,Integer> AsanaTime;

    public void SavePractic (LinkedList<String> linkedList){
        Asanalist = linkedList;
    }
    public LinkedList<String> GetPractic (){
        return Asanalist;
    }

    public void SaveAsanaTime (HashMap asanatime){
        AsanaTime = asanatime;
    }

    public HashMap GetAsanaTime (){
        return AsanaTime;
    }
}
