/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.util.ArrayList;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public interface QuestTracker {
    
    abstract void addQuest(Quest quest);
    abstract boolean completeQuest();
    abstract ArrayList<Quest> getQuests();
}
