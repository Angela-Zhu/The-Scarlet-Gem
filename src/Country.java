
import java.awt.*;
/**
 * Data class, which contains all the data for each country in the game.
 * 
 * @author Zachary Teper (0.5 hours) and Angela Zhu (0 hours)
 * @version 1.0 06.09.2015
 */
public class Country {
  /**
   * Holds the easy questions for the country.
   */
  private Question[] easy;
  /**
   * Holds the medium questions for the country.
   */
  private Question[] medium;
  /**
   * Holds the hard questions for the country.
   */
  private Question[] hard;
  /**
   * Holds the name of the country.
   */
  private final String NAME;
  /**
   * Holds the set of clues for the country.
   */
  private String[]clues;
  /**
   * Holds the Image of the country's background.
   */
  private Image background;
  /**
   * Returns a random question of the specified difficulty.
   * 
   * If statements determine what level of difficulty to return.
   * 
   * <b> Local Variables </b>
   * <p>
   * <b> index </b> int the question index which will be returned.
   * 
   * @param difficulty int the difficulty of question to be returned.
   * @return Question a random question of the specified difficulty.
   */
  public Question getRandQuestion (int difficulty)
  {
    int index= (int) (Math.random()*4);
    if (difficulty==0)
      return easy[index];
    else
    {
      if (difficulty==1)
        return medium[index];
    }
    return hard[index];
  }
  /**
   * Returns the ScarletGemMain.COUNTRIES array index of the country.
   * 
   * If statements control return statements
   * 
   * @return int the array index of the country.
   */
  public int getIndex()
  {
    if (NAME.equals("Canada"))
      return 0;
    else if (NAME.equals("China"))
      return 1;
    else if (NAME.equals("USA"))
      return 2;
    else if (NAME.equals("Mexico"))
      return 3;
    else if (NAME.equals("Portugal"))
      return 4;
    else if (NAME.equals("Australia"))
      return 5;
    else if (NAME.equals("Egypt"))
      return 6;
    else if (NAME.equals("India"))
      return 7;
    else if (NAME.equals("Russia"))
      return 8;
    else if (NAME.equals("Japan"))
      return 9;
    else if (NAME.equals("France"))
      return 10;
    //england
    return 11;
  }
  /**
   * Returns the background for the country.
   * 
   * @return Image the background of the country.
   */
  public Image getBackground ()
  {
    return background;
  }
  /**
   * Returns a random clue.
   * 
   * @return String a random clue from the country.
   */
  public String getRandClue()
  {
    return clues [(int)(Math.random()*4)];
  }
  /**
   * Returns the name of the country.
   * 
   * @return String the name of the country.
   */
  public String getName ()
  {
    return NAME;
  }
  /**
   * Returns true only when <code>other</code> has 
   * a name equal to the name of this country.
   * 
   * @param other Country the Country instance to be compared to.
   * @return boolean true only if the two country's names are equal.
   */
  public boolean equals (Country other)
  {
    return other.getName().equals(NAME);
  }
  /**
   * Initializes the Country. 
   * 
   * @param NAME String the name of the country
   * @param a Question[] the easy questions.
   * @param b Question[] the medium questions.
   * @param c Question[] the hard questions.
   * @param back Image the backgoround Image of the country.
   * @param clues String[] the clues for this country.
   */
  public Country (String NAME, Question[]a,Question[]b,
                  Question[]c,Image back, String[]clues)
  {
    this.NAME=NAME;
    easy=a;
    medium=b;
    hard=c;
    background=back;
    this.clues=clues;
  }
  
}
