package ChordFind;
import java.lang.StringBuilder;
import java.util.ArrayList;
        
/**
 *
 * @author Josh Revell
 */
public class ChordAnalysis 
{
    private int note1, note2, note3, note4;
    public ArrayList<String> chordResults = new ArrayList<String>();
    
    public void analyze(int rNote, int rNote2, int rNote3, int rNote4)
    {
        boolean isThird = false;
            boolean isMinorThird = false;
            boolean isMajorThird = false;
        
        boolean isFifth = false;
            boolean isFlatFifth = false;
            boolean isSharpFifth = false;
        
        boolean isSeventh = false;
            boolean isMinorSeventh = false;
            boolean isMajorSeventh = false;
        
        boolean isNinth = false;
            boolean isFlatNinth = false;
            boolean isSharpNinth = false;
        
        boolean isEleventh = false;
            boolean isSharpEleventh = false;
        
        boolean isThirteenthOrSixth = false;
        
        
        StringBuilder chordSuffix = new StringBuilder();
        
        int[] intervals = new int[3];
        intervals[0] = ChromaticScale.getInterval(rNote, rNote2);
        intervals[1] = ChromaticScale.getInterval(rNote, rNote3);
        intervals[2] = ChromaticScale.getInterval(rNote, rNote4);
        
        // Determine the kinds of intervals in the chord.
        
        for (int count = 0; count < 3; count++)
        {
            if(intervals[count] >= 6 && intervals[count] <= 8)
            {
                isFifth = true;
                if (intervals[count] == 6)
                    isFlatFifth = true;
                else
                    if(intervals[count] == 8)
                        isSharpFifth = true;    // Could be b13.
            }
                
            if(intervals[count] == 3 || intervals[count] == 4)
            {
                isThird = true;
                if(intervals[count] == 3)
                    isMinorThird = true;
                else
                    isMajorThird = true;
            }        
                
            if(intervals[count] == 10 || intervals[count] == 11)
            {
                isSeventh = true;
                if(intervals[count] == 10)
                    isMinorSeventh = true;
                else
                    isMajorSeventh = true;
            } 
            
            if(intervals[count] >= 1 && intervals[count] <= 3)
            {
                isNinth = true;
                
                if (intervals[count] == 1)
                    isFlatNinth = true;           
                else
                    if (intervals[count] == 3)
                    {
                        isSharpNinth = true;
                        // Disallow simultaneous minor third and sharp ninth.
                        // Minor third takes precedence.
                        if (isMinorThird)
                            isNinth = false;
                    }
            }
                      
            if(intervals[count] == 5 | intervals[count] == 6)
            {
                isEleventh = true;
                
                if(intervals[count] == 6)
                {
                    isSharpEleventh = true;
                    // Disallow simultaneous flat fifth and sharp thirteeth.
                    // Flat fifth takes precedence.
                    if (isFlatFifth)
                        isEleventh = false;
                }
            }
                
            if (intervals[count] == 9)
            {
                isThirteenthOrSixth = true;
            }                   
        }
        
        // Determine the name of the chord itself.
        
        // Chords without higher extensions.
        if(!isThirteenthOrSixth&& !isEleventh && !isNinth)
        {
            // Basic chords.
            if(!isSeventh)
            {
                // Power chords & altered power chords
                if(!isThird)
                {
                    if(isFifth)
                    {
                        if(!isFlatFifth && !isSharpFifth)
                        {
                            chordSuffix.append("5");
                        }
                        else 
                            if (isFlatFifth)
                                chordSuffix.append("b5");
                            else
                                chordSuffix.append("#5");
                    }
                    // No chord...
                    else
                        chordSuffix.append("");  
                }
                // Major, minor, dimished, augmented chords without 7ths.
                else
                {
                    if(isMajorThird)
                    {
                        chordSuffix.append("M");
                        if(isFlatFifth)
                            chordSuffix.append("b5");
                        else
                            if(isSharpFifth)
                                chordSuffix.append("#5");
                    }
                    else
                    {
                        // Can't directly append "m"  because of "dim" suffix.
                        if(isFlatFifth)
                            chordSuffix.append("dim");
                        else
                        {
                            chordSuffix.append("m");
                            if(isSharpFifth)
                                chordSuffix.append("#5");
                        }
                    }
                }
                    
            }
            // These are chords with seventh intervals but without
            // any higher extensions on top of them.
            else
            {
                if(isMajorThird && isMinorSeventh)
                    chordSuffix.append("7");
                else 
                    if(isMajorThird)
                        chordSuffix.append("M");
                    else 
                        if(isMinorThird)
                            chordSuffix.append("m");
                      
                if (isMinorThird && isMajorSeventh)
                    chordSuffix.append("M7");
                else
                    if (isMinorThird && isMinorSeventh || 
                            isMajorThird && isMajorSeventh)
                        chordSuffix.append("7");
                
                if(isFlatFifth)
                    chordSuffix.append("b5");
                else
                    if (isSharpFifth)
                        chordSuffix.append("#5");
                
                
                if (!isThird)
                {
                    if (isMinorSeventh)
                        chordSuffix.append("m7|7");
                    if (isMajorSeventh)
                        chordSuffix.append("M7");
                    
                    
                    if (isFifth)
                    {
                        if(isFlatFifth)
                            chordSuffix.append("b5");
                        else
                            if (isSharpFifth)
                                chordSuffix.append("#5");
                    }
                    
                }
                

                    
            }
                
        }
        chordResults.add(ChromaticScale.getNoteName(rNote) + chordSuffix.toString());
    }
    
}
