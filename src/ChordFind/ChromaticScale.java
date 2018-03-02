/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChordFind;

/**
 *
 * @author Josh Revell
 */
public class ChromaticScale 
{  
    private static final String[] NOTES = new String[]
        {"A", "A#/Bb", "B", "C", "C#/Db","D", 
        "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"};

    
    public static int getNoteIndex(String note)
    {
        int noteIndex;
        for (noteIndex = 0; noteIndex < 12; noteIndex++)
        {
            if(NOTES[noteIndex] == note)
                break;
        }
        return noteIndex;
    }
    
    public static String getNoteName(int index)
    {
        return NOTES[index];
    }
    
    public static int getInterval(int noteOneIndex, int noteTwoIndex)
    {
        int interval = 0;
        int currentIndex = noteOneIndex;
        for (int count = 0; count < 12; count++)
        {
            interval++;
            currentIndex ++;
            
            if(currentIndex == 12)
                currentIndex = 0;
            
            if(currentIndex == noteTwoIndex)
                break;
            
            
        }
            
        return interval;
    }
}


    

