/**
 * Holds the question String and answer char for each question in the game.
 * Answers and Questions can be accessed at any time by any class.
 * 
 * @author Zachary Teper (1 hour) and Angela Zhu (0 hour)
 */
public class Question {
  /**
   * Holds the answer to the question. Should only be equal to 'A', 'B',
   * 'C', 'D'
   */
  private final char ANSWER;
  /**
   * Holds the question text. Should contain the 
   * values for the answers A, B, C and D.
   */
  private final String QUESTION;
  /**
   * Returns the answer for each question. Should be A, B, C and D.
   * 
   * @return answer char the answer to the question.
   */
  public char getAnswer()
  {
    return ANSWER;
  }
  /**
   * Returns the text associated with the question.
   * 
   * @return question String the question itself, including the four options.
   */
  public String getQuestion ()
  {
    return QUESTION;
  }
  /**
   * Assigns values to the question text and answer char.
   *
   * @param q String the text of the question
   * @param ans char the answer to the quesiton (should 
   * be capital A, B, C or D.)
   */
  public Question (String q,char ans)
  {
    ANSWER=ans;
    QUESTION=q;
  }
  /* ADD YOUR CODE HERE */
  
}
