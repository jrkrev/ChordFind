/****************************************************************
*   ChordFind - an application to determine possible chords     *
*   from a sequence of notes.                                   *
*****************************************************************/
package ChordFind;
import java.lang.StringBuilder;
import java.util.ArrayList;
        
/**
 *
 * @author Josh Revell
 */
public class ChordAnalysis 
{ 
    private boolean isThird;
    private boolean isMinorThird;
    private boolean isMajorThird;
    private boolean isFifth;
    private boolean isFlatFifth;
    private boolean isSharpFifth;
    private boolean isSixth;
    private boolean isSeventh;
    private boolean isMinorSeventh;
    private boolean isMajorSeventh;
    private boolean isNinth;
    private boolean isFlatNinth;
    private boolean isSharpNinth;
    private boolean isEleventh;
    private boolean isSharpEleventh;
    private boolean isThirteenth;
    private int[] intervals = new int[3];
    
    private StringBuilder chordSuffix = new StringBuilder();
    private ArrayList<String> chordResults = new ArrayList<>();
    
    public ChordAnalysis(int root, int note2, int note3, int note4)
    {
        resetAllBooleans();
        calculateIntervalNameBooleans(root, note2, note3, note4);
        analyze(root);
    }
    
    public String getChordResults()
    {
        return chordResults.toString();
    }
    
    private void resetAllBooleans()
    {
        isThird = false;
        isMinorThird = false;
        isMajorThird = false;
        isFifth = false;
        isFlatFifth = false;
        isSharpFifth = false;
        isSixth = false;
        isSeventh = false;
        isMinorSeventh = false;
        isMajorSeventh = false;
        isNinth = false;
        isFlatNinth = false;
        isSharpNinth = false;
        isEleventh = false;
        isSharpEleventh = false;
        isThirteenth = false;    
    }
    
    private void calculateIntervalNameBooleans(int root, int note2, 
                                                int note3, int note4)
    {
        // If the note is 12, it is unselected.
        if (note4 == 12)
            note4 = root;
        if (note3 == 12)
            note3 = root;
        if (note2 == 12)
            note2 = root;
        
        intervals[0] = ChromaticScale.getInterval(root, note2);
        intervals[1] = ChromaticScale.getInterval(root, note3);
        intervals[2] = ChromaticScale.getInterval(root, note4);
              
        for (int count = 0; count < 3; count++)
        {
            if(intervals[count] == 3 || intervals[count] == 4)
            {
                isThird = true;
                if(intervals[count] == 3)
                    isMinorThird = true;
                else
                    isMajorThird = true;
            } 
            
            if(intervals[count] >= 6 && intervals[count] <= 8)
            {
                isFifth = true;
                if (intervals[count] == 6)
                    isFlatFifth = true;
                else
                    if(intervals[count] == 8)
                        isSharpFifth = true;
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
                        isSharpNinth = true;
            }
                      
            if(intervals[count] == 5 | intervals[count] == 6)
            {
                isEleventh = true;
                if(intervals[count] == 6)
                    isSharpEleventh = true;      
            }
                
            if (intervals[count] == 9)
            {
                isThirteenth = true;
                isSixth = true;
            }                   
        }
    }
    
    private void makeTheoryExceptions()
    {
        // Prevent #9 from being mistaken for m3.
        if(isMinorThird && isSharpNinth)
        {
            if (isMajorThird || isMajorSeventh          // Major chords
                    || isMajorThird && isMinorSeventh)  // Dominant chords
            {
                isMinorThird = false;
            }
            else
            {
                isSharpNinth = false;
                if (isNinth && !isFlatNinth || !isFlatNinth)
                    isNinth = false;
            }
        }       
        // Prevent simultaneous #11 and b5 in favour of #11 when there are
        // higher extensions.
        if(isSharpEleventh && isFlatFifth)
        {
            if(isNinth || isThirteenth)
            {
                isFlatFifth = false;
                if (isFifth && !isSharpFifth || !isSharpFifth)
                    isFifth = false;
            }
            else
            {
                isEleventh = false;
                isSharpEleventh = false;
                
            }
        }
        
        if(isThirteenth && isSixth)
        {
            if(isSeventh || isFlatNinth || isSharpNinth || isEleventh
                    || isFlatFifth || isSharpFifth || isNinth && isMinorThird)    
                isSixth = false;
            else
                isThirteenth = false;
            
            // Since there is now a sixth involved, here we adjust for dim7
            // chords.
            if (isMinorThird && isSharpEleventh)
            {
                isEleventh = false;
                isSharpEleventh = false;
                isFlatFifth = true;
            }
                
                
        }
        
    }
    
    private void analyze(int root)
    {         
        makeTheoryExceptions();
        boolean fail = false;
        String rootName = ChromaticScale.getNoteName(root);

        // Chords without higher extensions.
        if(!isSixth && !isSeventh && !isNinth && !isEleventh && !isThirteenth)
        {
            // Power chords & altered power chords
            if(!isThird)
            {
                if(isFifth)
                if(isFlatFifth || isSharpFifth)
                    appendDimOrAug();
                else
                    appendNaturalFifth();
            }
            // Major, minor, dimished, augmented chords without 7ths.
            else
            {
                if(isMajorThird)
                {
                        if(isSharpFifth)
                            appendDimOrAug();
                        else
                        {
                            appendThird();
                            appendFifth();
                        }
                }
                else
                {
                    if(isFlatFifth)
                        appendDimOrAug();
                    else
                    {
                        appendThird();
                        appendFifth();
                    }
                }
            }
                    
        }
        // These are chords with higher intervals.
        else
        {
            if (isThird)
            {
                if(isMajorThird && isMinorSeventh)
                {
                    appendSeventh();
                    appendFifth();
                }
                else 
                {
                    appendThird();
                    appendSeventh();
                    appendFifth();
                }
            }
            else
            {
                if(isSeventh)
                    appendSeventh(); 
                if(isFifth)
                    appendFifth();                            
            }

            if(isNinth)
            {
                chordSuffix = new StringBuilder();
                appendThird();
                if(!isFlatNinth && !isSharpNinth)
                    appendNinth();
                else
                {
                    appendSeventh();
                    appendNinth();
                }
                appendFifth();           
            }

            if(isEleventh)
            {
                chordSuffix = new StringBuilder();

                if(isMajorThird && isMinorSeventh)
                    appendEleventh();
                else
                {
                    appendThird();
                    appendEleventh();
                }

                if(isFlatNinth || isSharpNinth)
                    appendNinth();

                appendFifth(); 
            }

            if(isSixth || isThirteenth)
            {
                chordSuffix = new StringBuilder();

                if (isMinorThird && (isSharpEleventh || isFlatFifth))
                {
                    appendDimOrAug();
                    appendSeventh();
                }
                else
                { 
                    if(isSixth && isMajorThird)
                    {
                        appendSixth();
                        if(isNinth)
                            appendNinth();        
                    }
                    else
                    {
                        if(isSixth && isMinorThird)
                        {
                            appendThird();
                            appendSixth();
                        }
                        else
                        {

                                if(isMinorThird || isMajorSeventh)
                                    appendThird();
                                appendThirteenth();

                        }
                    }

                    if(isSharpEleventh && !isFlatFifth)
                    appendEleventh();

                    if(isFlatNinth || isSharpNinth)
                        appendNinth();

                    appendFifth();
                }
            }

            if(isMinorSeventh && isMajorSeventh)
            {
                fail = true;          
            }

        }
                
        if(!fail)
            chordResults.add(rootName + chordSuffix.toString());
        else
            chordResults.add("No chord found with root " + rootName);       
    }
    
    private void appendThird()
    {
        if(isMajorThird || !isMajorThird && isMajorSeventh)
            chordSuffix.append("M");
        else
            if(isMinorThird)
                chordSuffix.append("m");
    }
    
    private void appendDimOrAug()
    {
        if(isFlatFifth)
            chordSuffix.append("dim");
        else
            if(isSharpFifth)
                chordSuffix.append("aug");
    }
    
    private void appendFifth()
    {
        if(isFlatFifth)
            chordSuffix.append("b5");
        else
            if(isSharpFifth)
                chordSuffix.append("#5");
    }
    
    private void appendNaturalFifth()
    {    
        chordSuffix.append("5");
    }
    
    private void appendSixth()
    {
        chordSuffix.append("6");
    }
    
    private void appendSeventh()
    {
        if(isMinorThird && isMajorSeventh)
            chordSuffix.append("M7");
        else
            chordSuffix.append("7");
    }
    
    private void appendNinth()
    {
        if(isFlatNinth)
            chordSuffix.append("b9");
        else
            if(isSharpNinth)
                chordSuffix.append("#9");
            else
                chordSuffix.append("9");
    }
    
    private void appendEleventh()
    {                 
        if(isSharpEleventh)
            chordSuffix.append("#11");
        else
            chordSuffix.append("11");     
    }
    
    private void appendThirteenth()
    {
        chordSuffix.append("13");
    }
    
}
