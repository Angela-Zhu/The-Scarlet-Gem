import java.awt.*;
import javax.swing.*;
import java.awt.print.*;
import javax.imageio.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
/**
 * This is the 'Main' class for this project. All of the event listeners used
 * in the game are implemented by this class. This class will respond to any buttons pressed
 * in any other classes. This class acts as the "brain" of the project: it does all of the logic,
 * processing and recordkeeping. The other classes are just utility classes that help to store temporary
 * game data (such as the question sets for each country) and display the GUI.
 * 
 * @author Zachary Teper (13 hours) and Angela Zhu (3 hours)
 * @version 1.0 17.05.15
 */
public class ScarletGemMain extends JFrame implements ActionListener, Printable,WindowListener
{
  /**
   * Holds the country that the user is currently in. This value will be reassigned
   * to a new value every time the user completes a level.
   */
  private Country currentCountry;
  /**
   * Holds the number representing the difficulty chosen by the user at the start of the game.
   * 0 represents easy. 1 represents medium and 2 represents hard.
   */
  private int difficulty;
  /**
   * Holds the number of levels that the user has left. In easy mode, this is initialized to 3.
   * In medium mode, this is initialized to 6. In hard mode, this is initialized to 9. When <code>
   * difficulty</code> reaches 0, the game ends.
   * It is initialized to -1 in order to check whether the user started the game or not.
   */
  private int levelsRemaining = -1;
  /**
   * Holds the instances of each country that the player visited. This makes sure that the player will
   * not visit the same country twice.
   */
  private ArrayList <Country> alreadyBeen;
  /**
   * Holds the instances of each country in the game. These are the indexes of each country in this array:
   * <ul>
   * <li>0 - Canada
   * <li>1 - China
   * <li>2 - Mexico
   * <li>3 - USA
   * <li>4 - Portugal
   * <li>5 - Australia
   * <li>6 - Egypt
   * <li>7 - India
   * <li>8 - Russia
   * <li>9 - Japan
   * <li>10 - France
   * <li>11 - England
   * </ul>
   */
  public static final Country[] COUNTRIES=new Country[12];
  /**
   * Holds the instance of the GamePanel class. This is initialized only once per game, and contains the buttons
   * and menus used in the game.
   */
  private GamePanel gamePanel;
  /**
   * Displays the number of levels that the user still has to complete in order to win the game.
   * Will be refreshed every time the user travels to a new country.
   */
  private JLabel levelCounter=new JLabel();
  /**
   * Holds the instance of the MainMenuPanel, which contains the buttons at the start of the game.
   * This is only initialized once, and reappears every time the user completes a level.
   */
  private MainMenuPanel mainMenuPanel;
  /**
   * Holds the instance of the HighScoresViewer which the user can see at any time
   * using the menu option or by pressing Ctrl+E. This is only initialized once.
   */
  private HighScoresViewer highScoresViewer;
  /**
   * Holds the number 1 or 0, depending on which question the user is currently on.
   */
  private int currentQuestion=0;
  /**
   * Displays help .chm file.
   */
  private JMenu helpMenu=new JMenu ("Help");
  /**
   * Menu which contains Save, Print, View High Scores and Exit items.
   */
  private JMenu fileMenu =new JMenu ("File");
  /**
   * Opens the Instructions viewer when the user selects this menu choice.
   */
  private JMenuItem howToPlayItem =new JMenuItem ("How To Play");
  /**
   * Opens the Print dialog when the user selects this menu choice.
   */
  private JMenuItem printItem=new JMenuItem ("Print");
  /**
   * Saves the user's progress when the user selects this menu choice.
   */
  private JMenuItem saveItem=new JMenuItem ("Save");
  /**
   * Closes the game window when the user selects this menu choice.
   */
  private JMenuItem exitItem=new JMenuItem ("Exit");
  /**
   * Opens the High Scores viewer when the user selects this menu choice.
   */
  private JMenuItem highScoresItem=new JMenuItem ("View High Scores");
  /**
   * Opens the .chm help file when the user selects this menu choice.
   */
  private JMenuItem helpItem=new JMenuItem ("Help");
  /**
   * Opens the About dialog when the user selects this menu choice.
   */
  private JMenuItem aboutItem=new JMenuItem ("About");
  /**
   * Holds the user's current score
   */
  private int score=0;
  /**
   * Holds the user's name (When he/she wins the game).
   */
  private String userName="";
  /**
   * Whether the user has saved his/her progress.
   */
  private boolean saved;
  /**
   * Called by the JVM when the window is Deactivated.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowDeactivated(WindowEvent e)
  {
  }
  /**
   * Called by the JVM when the window is Opened.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowOpened (WindowEvent e)
  {
  }
  /**
   * Called by the JVM when the window is Closing.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowClosing (WindowEvent e)
  {
    closeWarning();
  }
  /**
   * Called by the JVM when the window is Activated.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowActivated (WindowEvent e)
  {
  }
  /**
   * Called by the JVM when the window is Deiconified.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowDeiconified (WindowEvent e)
  {
  }
  /**
   * Called by the JVM when the window is Iconified.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowIconified(WindowEvent e)
  {
  }
  /**
   * Called by the JVM when the window is Closed.
   * 
   * @param e WindowEvent the event that occured on the window
   */
  public void windowClosed (WindowEvent e)
  {
  }
  
  /**
   * Processes action events from all of the Buttons and Menus throughout the game.
   * Called by the JVM when a Button or Menu is pressed.
   * If statements control which block of code will be executed based on <code> ae</code>
   * If statement in progress checks if the file contains anything.
   * If statement in progress reading section sets the number of levels remaining.
   * If statement in pause button processing calculates whether to pause or unpause.
   * 
   * While loop in progress reading section keeps reading until it reaches a blank line.
   *
   * Try block in help button section runs execution of .chm file.
   * Try block in progress reading section reads in from file.
   * Try block in about dialog section reads in the background and logo image.
   * 
   * <p>
   * <b> Local Variables</b>
   * <p>
   * <b> job </b> PrinterJob - the printer job to print the high scores.
   * <p>
   * <b> file</b> File - the .chm file to be displayed.
   * <p>
   * <b> about </b> JFrame - the About dialog window.
   * <p>
   * <b> aboutPanel </b> JPanel - the Panel which holds the window components.
   * <p>
   * <b> infoLabel </b> JLabel - contains contact info about the game.
   * <p>
   * <b> background </b> JLabel - holds the background of the About dialog.
   * <p>
   * <b> logo </b> ImageIcon - holds the CakeSoft logo.
   * <p>
   * <b> logoLabel </b> JLabel - holds the ImageIcon specified by <code> logo </code>
   * <p>
   * <b> in </b> BufferedReader - reads in the progress file.
   * <p>
   * <b> next </b> String - holds the value of the next line in the progress file.
   * <p>
   * <b> total </b> int - holds the total number of levels in each difficulty mode.
   * <p>
   * 
   * @param ae ActionEvent - the action which occured.
   */
  public void actionPerformed(ActionEvent ae)
  {
    if (ae.getSource().equals(exitItem))
    {
      closeWarning();
    }
    else if (ae.getSource().equals(highScoresItem))
    {
      new HighScoresViewer();
    }
    else if (ae.getSource().equals(saveItem))
    {
      saveItem.setEnabled (false);
      saved = true;
      save();
    }
    else if (ae.getSource().equals(printItem))
    {
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPrintable(this);
      boolean ok=job.printDialog();
      if (ok)
      {
        try
        {
          job.print();
        }
        catch (PrinterException e)
        {
        }
      }
    }
    else if (ae.getSource().equals(howToPlayItem))
    {
      new InstructionsViewer();
    }
    else if (ae.getSource().equals(helpItem))
    {
      File file = new File("files/help.chm");
      try
      {
        Runtime.getRuntime().exec("HH.EXE ms-its:" + file.getAbsolutePath() + "::/TOPIC_ID.html");
      } catch (IOException e1)
      {
      }
    }
    else if (ae.getSource().equals(aboutItem))
    {
      JFrame about=new JFrame("About");
      JPanel aboutPanel=new JPanel();
      JLabel infoLabel=new JLabel("<html>Game: The Scarlet Gem"+
                                  "<br><br>Project Lead: Zachary Teper"+
                                  "<br><br>Project Representative: Angela Zhu"+
                                  "<br><br>Version: 1.0 06.09.2015"+
                                  "<br><br>Email: zacharyblacktail@gmail.com"+
                                  "<br><br>Phone: 416-223-6075</html>");
      JLabel background=new JLabel();
      ImageIcon logo=new ImageIcon ("pics/CakeSoftInc.png");
      JLabel logoLabel=new JLabel();
      
      about.setSize(400,500);
      about.add(aboutPanel);
      about.setResizable(false);
      about.setVisible(true);
      about.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      aboutPanel.setLayout(null);
      logoLabel.setIcon(logo);
      try
      {
        background.setIcon(new ImageIcon(ImageIO.read(new File("pics/background3.png"))));
      }
      catch (IOException e)
      {
      }
      background.setBounds(0,0,400,500);
      logoLabel.setBounds(0,0,200,60);
      infoLabel.setBounds(100,50,300,400);
      aboutPanel.add(logoLabel);
      aboutPanel.add(infoLabel);
      aboutPanel.add(background);
    }
    else if (ae.getSource().equals(highScoresItem))
    {
      new HighScoresViewer ();
    }
    else if (ae.getSource().equals(mainMenuPanel.getEasyButton()))
    {
      levelsRemaining=2;
      difficulty=0;
      initializeGame();     
    }
    else if (ae.getSource().equals(mainMenuPanel.getMediumButton()))
    {
      levelsRemaining=5;
      difficulty=1;
      initializeGame();
    }
    else if (ae.getSource().equals(mainMenuPanel.getHardButton()))
    {
      levelsRemaining=7;
      difficulty=2;
      initializeGame();     
    }
    else if (ae.getSource().equals(mainMenuPanel.getLoadButton()))
    {
      try
      {
        BufferedReader in=new BufferedReader(new FileReader("files/progress.csi"));
        if ("The Scarlet Gem".equals (in.readLine ()))
        {
          difficulty=Integer.parseInt(in.readLine());
          in.readLine();
          
          String next;
          while(true)
          {
            next=in.readLine();
            if (next.equals(""))
              break;
            alreadyBeen.add(getCountry(next));
          }
          
          
          currentCountry=getCountry(in.readLine());
          in.readLine();
          GameTimer.timeRemaining=(Integer.parseInt(in.readLine()));
          in.readLine();
          currentQuestion=Integer.parseInt(in.readLine());
          int total;
          if (difficulty==0)
            total=4;
          else if (difficulty==1)
            total=7;
          else
            total=10;
          levelsRemaining=total-alreadyBeen.size();
          initializeGame();
        }
        else
        {
          JOptionPane.showMessageDialog(null,"No saved progress found.");
        }
      }
      catch (IOException e)
      {
        JOptionPane.showMessageDialog(null,"File could not be loaded.");
      }
    }
    else if (ae.getSource().equals(gamePanel.getAButton()))
    {
      checkAnswer('A');
    }
    else if (ae.getSource().equals(gamePanel.getBButton()))
    {
      checkAnswer ('B');
    }
    else if (ae.getSource().equals(gamePanel.getCButton()))
    {
      checkAnswer ('C');
    }
    else if (ae.getSource().equals(gamePanel.getDButton()))
    {
      checkAnswer ('D');
    }
    else if (ae.getSource().equals(gamePanel.getPauseButton()))
    {
      if (!gamePanel.getTimer().getPaused())
      {
        fileMenu.setEnabled(true);
        helpMenu.setEnabled(true);
        gamePanel.switchToPause();
      }
      else
      {
        fileMenu.setEnabled(false);
        helpMenu.setEnabled(false);
        gamePanel.unpause();
      }
      
    }
    else
    {
      if (ae.getActionCommand().equals("loss"))
      {
        JOptionPane.showMessageDialog(null,"Sorry, you ran out of time!");
        endGame();
      }
    }
    revalidate();
  }
  /** Returns the country associated with a name
    * 
    * If statements check what the name of the country is and return the result.
    * 
    * @param name String the name of the Country
    * @return Country the instance of the country associated with name
    * @See "ScarletGemMain.COUNTRIES"
    */
  private Country getCountry(String name)
  {
    if (name.equals("Canada"))
      return COUNTRIES[0];
    else if (name.equals("China"))
      return COUNTRIES[1];
    else if (name.equals("USA"))
      return COUNTRIES[2];
    else if (name.equals("Mexico"))
      return COUNTRIES[3];
    else if (name.equals("Portugal"))
      return COUNTRIES[4];
    else if (name.equals("Australia"))
      return COUNTRIES[5];
    else if (name.equals("Egypt"))
      return COUNTRIES[6];
    else if (name.equals("India"))
      return COUNTRIES[7];
    else if (name.equals("Russia"))
      return COUNTRIES[8];
    else if (name.equals("Japan"))
      return COUNTRIES[9];
    else if (name.equals("France"))
      return COUNTRIES[10];
    return COUNTRIES[11];
  }
  /**
   * Asks the users if they want to save before closing the program. 
   * 
   * If statements control processing of user input.
   * 
   * YES==save and close
   * NO==close without saving
   * CANCEL/ESCAPE==resume game without saving
   */
  private void closeWarning()
  {
    if (levelsRemaining != -1 && saved == false)
    {
      int option=JOptionPane.showConfirmDialog(this,
                                               "Do you want to save your progress?",
                                               "Save?",
                                               JOptionPane.YES_NO_CANCEL_OPTION);
      if (option==JOptionPane.YES_OPTION)
      {
        save();
        System.exit(0);
      }
      else
      {
        if (option==JOptionPane.NO_OPTION)
          System.exit(0);
      }
    }
    else
    {
      System.exit (0);
    }
  }
  /**
   * saves the user's current progress to the file "files/progress.csi"
   * 
   * For loop writes out the names of the countries already visited.
   * 
   * Try block writes data to file.
   */
  private void save()
  {
    try
    {
      PrintWriter out=new PrintWriter(new FileWriter("files/progress.csi"));
      out.println ("The Scarlet Gem");
      out.println (difficulty);
      out.println ();
      for (Country s:alreadyBeen)
        out.println(s.getName());
      out.println();
      out.println (currentCountry.getName());
      
      out.println();
      
      out.println (GameTimer.timeRemaining);
      out.println ();
      out.println (currentQuestion);
      out.close();
    }
    catch (IOException e)
    {
      JOptionPane.showMessageDialog(null,"Progress could not be saved.");
    }
  }
  /**
   * sets up the game components and starts the timer.
   */
  private void initializeGame ()
  {
    saveItem.setEnabled (true);
    saved = false;
    fileMenu.setEnabled (false);
    helpMenu.setEnabled (false);
    remove(mainMenuPanel);
    add(levelCounter);
    gamePanel = new GamePanel (difficulty,this);
    add (gamePanel);
    gamePanel.getPauseButton ().addActionListener (this);
    gamePanel.getAButton().addActionListener(this);
    gamePanel.getBButton().addActionListener(this);
    gamePanel.getCButton().addActionListener(this);
    gamePanel.getDButton().addActionListener(this);
    gamePanel.getPauseButton().setEnabled (true);
    gamePanel.getQuestionCounter().setText("Question: "+(currentQuestion+1)+"/"+2);
    showCountryPanel();    
  }
  /**
   * Returns the String representation of the current difficulty.
   * 
   * If statements control return statements.
   * 
   * @return String the String represenation of the difficulty.
   */
  public String difficultyString()
  {
    if (difficulty==0)
      return "Easy";
    else if (difficulty==1)
      return "Medium";
    return "Hard";
  }
  /**
   * displays a random multiple-choice question to the user,
   * and waits for an answer.
   * 
   * If statement checks if the game is won.
   * 
   * Try block waits for half a second before displaying the screen.
   */
  private void showCountryPanel()
  {
    currentQuestion=0;
    gamePanel.getLevelCounter().setText("     You are in: " + currentCountry.getName()+
                                        "                       Difficulty: "+difficultyString());
    gamePanel.getQuestionCounter().setText("Question: "+(currentQuestion+1)+"/"+2);
    gamePanel.getQuestionCounter().setVisible(true);
    try
    {
      Thread.sleep(500);
    }
    catch (InterruptedException e)
    {
    }
    gamePanel.setBackground(currentCountry.getBackground());
    
    
    
    if (levelsRemaining==0)
    {
      gamePanel.timer.setGameWon (true);
      endGame();
    }
    else
    {
      gamePanel.setQuestion(currentCountry.getRandQuestion(difficulty));
      gamePanel.switchToCountry();
      gamePanel.getAButton().setEnabled(true);
      gamePanel.getBButton().setEnabled(true);
      gamePanel.getCButton().setEnabled(true);
      gamePanel.getDButton().setEnabled(true);
      revalidate();
    }
  }
  /**
   * Creates the 3 destination options for the Map screen and displays it.
   * While loops search for COUNTRIES that have not been visited in the game.
   * 
   * Try block waits for half a second before displaying screen.
   * <p>
   * <b> Local variables </b>
   * <p>
   * <b>wrong1</b> Country - holds one of the incorrect destinations.
   * <p>
   * <b>wrong2</b> Country - holds the other incorrect destination.
   */ 
  private void showMapPanel()
  {
    gamePanel.getQuestionCounter().setVisible(false);
    try
    {
      Thread.sleep(500);
    }
    catch (InterruptedException e)
    {
    }
    do
    {
      currentCountry=COUNTRIES[(int)(Math.random()*10)];
    }
    while (alreadyBeen.contains(currentCountry));
    
    Country wrong1;
    Country wrong2;
    do
    {
      wrong1=COUNTRIES[(int)(Math.random()*10)];
    }
    while (alreadyBeen.contains(wrong1)||wrong1.equals(currentCountry));
    
    do
    {
      wrong2=COUNTRIES[(int)(Math.random()*10)];
    }
    while (alreadyBeen.contains(wrong2)||wrong1.equals(wrong2)
             ||wrong2.equals(currentCountry));
    gamePanel.setDestinations(new Country[]{currentCountry,wrong1,wrong2});
    gamePanel.switchToMap();
    gamePanel.getAButton().setEnabled(true);
    gamePanel.getBButton().setEnabled(true);
    gamePanel.getCButton().setEnabled(true);
    repaint();
    
    
  }
  /**
   * Checks if the answer selected by the user is correct. If it is, the next screen is displayed.
   * If not, the choice is eliminated time is deducted from the user's score.
   * 
   * If statement checks if the game panel is at the country stage.
   * If statement checks if the user selected the correct answer.
   * If statement checks if the user selected the correct destination.
   * 
   * While loop continues drawing random questions until it draws one
   * that is not a repetition of the one just answered.
   * 
   * <b> Local Variables</b>
   * <p>
   * <b> temp </b> Question - holds the potential value of the next question.
   * 
   * @param answer char the answer that the user selected
   */
  private void checkAnswer (char answer)
  {
    if (gamePanel.getStage())
    {
      if (gamePanel.getQuestion().getAnswer()==answer)
      {
        score+=30;
        if (currentQuestion==0)
        {
          gamePanel.getAButton().setEnabled(true);
          gamePanel.getBButton().setEnabled(true);
          gamePanel.getCButton().setEnabled(true);
          gamePanel.getDButton().setEnabled(true);
          Question temp=currentCountry.getRandQuestion(difficulty);
          while (temp==gamePanel.getQuestion())
          {
            temp=currentCountry.getRandQuestion(difficulty);
          }
          gamePanel.setQuestion(temp);
          currentQuestion++;
          gamePanel.getQuestionCounter().setText("Question: "+(currentQuestion+1)+"/"+2);
        }
        else
        {
          levelsRemaining--;
          alreadyBeen.add(currentCountry);
          showMapPanel();
        }
      }
      else
      {
        gamePanel.removeWrongAnswer(answer);
        score -= 10;
      }
    }
    else
    {
      if (answer-65==gamePanel.getAnswer())
      {
        score+=30;
        showCountryPanel();
      }
      else
      {
        gamePanel.removeWrongDestination(answer-65);
        score -= 10;
      }
    }
  }
  /**
   * resets game variables, closes the game screen, 
   * and returns to the main menu. Asks user for username, asks if he/she
   * would like to continue.
   * 
   * If statement checks if the game has been won.
   * If statement checks if the username is not <code> null</code>
   * before adding it to High Scores.
   * If statement checks if the difficulty is not Hard.
   * If statement checks if the user wants to continue, and if the difficulty is not Hard.
   * If statement checks if the difficulty is Easy.
   * 
   * <b> Local Variables </b>
   * <p>
   * <b>next </b> int - holds whether the user wants to continue.
   */
  public void endGame()
  {
    remove(gamePanel);
    remove(levelCounter);
    levelsRemaining = -1;
    currentCountry=COUNTRIES[0];
    alreadyBeen=new ArrayList<Country>();
    fileMenu.setEnabled (true);
    helpMenu.setEnabled (true);
    int next = 0;
    if (gamePanel.timer.getGameWon())
    {
      score += GameTimer.timeRemaining;
      userName = JOptionPane.showInputDialog (null, 
                                              "Congratulations, you found the Scarlet Gem! Your score is "+
                                              score+" Please enter your user name!", 
                                              "You won!", JOptionPane.QUESTION_MESSAGE);
      if (userName!=null)
        HighScoresViewer.sort (score, userName, difficulty);
      
      if (difficulty == 0 || difficulty == 1)
        next = JOptionPane.showConfirmDialog(null, "Would you like to continue to the next level?",
                                             "Next?", JOptionPane.YES_NO_OPTION);
      if (next == 0&&difficulty!=2)
      {
        if (difficulty == 0)
        {
          levelsRemaining=5;
          difficulty=1;
        }
        else
        {
          levelsRemaining=7;
          difficulty=2;
        }
        initializeGame();  
      }
      else
      {
        add(mainMenuPanel);
        saveItem.setEnabled (false);
        repaint();
        revalidate();
      }
    }
    else
    {
      add(mainMenuPanel);
      saveItem.setEnabled (false);
      repaint();
      revalidate();
    }
  }
  /**
   * Sends a print command to the printer specified in the dialog box.
   * 
   * If statement checks if the index is greater than 0.
   * 
   * For loops and if statements print names and scores, will break if 
   * the loop reaches a blank line.
   * 
   * Try block reads in the logo.
   * 
   * @param page Graphics the page to be printed
   * @param format PageFormat the format of the page to be printed
   * @param index int the page index to be printed
   * 
   * @return int Returns Printable.PAGE_EXISTS or Printable.NO_SUCH_PAGE
   */
  public int print(Graphics page, PageFormat format, int index)
  {
    if (index>0)
      return NO_SUCH_PAGE;
    page.drawString("The Scarlet Gem",100,100);
    try
    {
      page.drawImage(ImageIO.read(new File("pics/CakeSoft Inc.png"))
                       ,100,150,null);
    }
    catch (IOException e)
    {
    }
    page.drawString("Easy",100,300);
    page.drawString("Medium",300,300);
    page.drawString("Hard",500,300);
    page.drawString("Name",50,350);
    page.drawString("Score",150,350);
    page.drawString("Name",250,350);
    page.drawString("Score",350,350);
    page.drawString("Name",450,350);
    page.drawString("Score",550,350);
    for (int x=0;x<10;x++)
    {
      if (HighScoresViewer.easyNames[x].equals(""))
        break;
      page.drawString(HighScoresViewer.easyNames[x],50,400+x*50);
      if (!HighScoresViewer.easyNames[x].equals(""))
        page.drawString(HighScoresViewer.easyScores[x]+"",150,400+x*50);
    }
    for (int x=0;x<10;x++)
    {
      if (HighScoresViewer.mediumNames[x].equals(""))
        break;
      page.drawString(HighScoresViewer.mediumNames[x],250,400+x*50);
      if (!HighScoresViewer.mediumNames[x].equals(""))
        page.drawString(HighScoresViewer.mediumScores[x]+"",350,400+x*50);
    }
    for (int x=0;x<10;x++)
    {
      if (HighScoresViewer.hardNames[x]==null)
        break;
      page.drawString(HighScoresViewer.hardNames[x],450,400+x*50);
      if (!HighScoresViewer.hardNames[x].equals(""))
        page.drawString(HighScoresViewer.hardScores[x]+"",550,400+x*50);
    }
    
    
    return PAGE_EXISTS;
  }
  /**
   * Displays the splash screen, initializes the game variables and displays the main menu.
   * Adds ActionListeners and KeyListeners to game Objects.
   * 
   * Try block reads in splashscreen image.
   * Try block reads in logo and title image.
   * Try blocks read in backgrounds for each country.
   * <p>
   * <b> Local Variables </b>
   * <p>
   * <b> imageLabel </b> JLabel - holds the splashscreen background.
   * <p>
   * <b> infoLabel </b> JLabel - holds the splashscreen information message.
   * <p>
   * <b>  logoLabel</b> JLabel - holds the company logo.
   * <p>
   * <b> splashImage</b> ImageIcon - holds the splashscreen background image.
   * <p>
   * <b> title </b> JLabel - holds the title image.
   * <p>
   * <b> splashScreen </b> JPanel - holds the panel displayed at the start of the game.
   * <p>
   * <b> canadaEasy </b> Question[] - holds the easy questions for Canada.
   * <p>
   * <b> canadaMedium </b> Question[] - holds the medium questions for Canada.
   * <p>
   * <b> canadaHard </b> Question[] - holds the hard questions for Canada.
   * <p>
   * <b> canada </b> Country - holds the Country instance for Canada.
   * <p>
   * <b> chinaEasy </b> Question[] - holds the easy questions for china.
   * <p>
   * <b> chinaMedium </b> Question[] - holds the medium questions for china.
   * <p>
   * <b> chinaHard </b> Question[] - holds the hard questions for china.
   * <p>
   * <b> china </b> Country - holds the Country instance for china.
   * <p>
   * <b> usaEasy </b> Question[] - holds the easy questions for usa.
   * <p>
   * <b> usaMedium </b> Question[] - holds the medium questions for usa.
   * <p>
   * <b> usaHard </b> Question[] - holds the hard questions for usa.
   * <p>
   * <b> usa </b> Country - holds the Country instance for usa.
   * <p>
   * <b> mexicoEasy </b> Question[] - holds the easy questions for mexico.
   * <p>
   * <b> mexicoMedium </b> Question[] - holds the medium questions for mexico.
   * <p>
   * <b> mexicoHard </b> Question[] - holds the hard questions for mexico.
   * <p>
   * <b> mexico </b> Country - holds the Country instance for mexico.
   * <p>
   * <b> portugalEasy </b> Question[] - holds the easy questions for portugal.
   * <p>
   * <b> portugalMedium </b> Question[] - holds the medium questions for portugal.
   * <p>
   * <b> portugalHard </b> Question[] - holds the hard questions for portugal.
   * <p>
   * <b> portugal </b> Country - holds the Country instance for portugal.
   * <p>
   * <b> australiaEasy </b> Question[] - holds the easy questions for australia.
   * <p>
   * <b> australiaMedium </b> Question[] - holds the medium questions for australia.
   * <p>
   * <b> australiaHard </b> Question[] - holds the hard questions for australia.
   * <p>
   * <b> australia </b> Country - holds the Country instance for australia.
   * <p>
   * <b> egyptEasy </b> Question[] - holds the easy questions for egypt.
   * <p>
   * <b> egyptMedium </b> Question[] - holds the medium questions for egypt.
   * <p>
   * <b> egyptHard </b> Question[] - holds the hard questions for egypt.
   * <p>
   * <b> egypt </b> Country - holds the Country instance for egypt.
   * <p>
   * <b> indiaEasy </b> Question[] - holds the easy questions for india.
   * <p>
   * <b> indiaMedium </b> Question[] - holds the medium questions for india.
   * <p>
   * <b> indiaHard </b> Question[] - holds the hard questions for india.
   * <p>
   * <b> india </b> Country - holds the Country instance for india.
   * <p>
   * <b> russiaEasy </b> Question[] - holds the easy questions for russia.
   * <p>
   * <b> russiaMedium </b> Question[] - holds the medium questions for russia.
   * <p>
   * <b> russiaHard </b> Question[] - holds the hard questions for russia.
   * <p>
   * <b> russia </b> Country - holds the Country instance for russia.
   * <p>
   * <b> japanEasy </b> Question[] - holds the easy questions for japan.
   * <p>
   * <b> japanMedium </b> Question[] - holds the medium questions for japan.
   * <p>
   * <b> japanHard </b> Question[] - holds the hard questions for japan.
   * <p>
   * <b> japan </b> Country - holds the Country instance for japan.
   * <p>
   * <b> franceEasy </b> Question[] - holds the easy questions for france.
   * <p>
   * <b> franceMedium </b> Question[] - holds the medium questions for france.
   * <p>
   * <b> franceHard </b> Question[] - holds the hard questions for france.
   * <p>
   * <b> france </b> Country - holds the Country instance for france.
   * <p>
   * <b> englandEasy </b> Question[] - holds the easy questions for england.
   * <p>
   * <b> englandMedium </b> Question[] - holds the medium questions for england.
   * <p>
   * <b> englandHard </b> Question[] - holds the hard questions for england.
   * <p>
   * <b> england </b> Country - holds the Country instance for england.
   */
  public ScarletGemMain()
  {
    super ("The Scarlet Gem");
    setVisible (true);
    setSize (700,600);
    setResizable(false);
    setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
    requestFocusInWindow();
    
    //set the JFrame icon and read splashImage
    ImageIcon splashImage=null;
    try
    {
      setIconImage (ImageIO.read (new File ("pics/scarlet-gem.png")));
      splashImage=new ImageIcon("pics/scarlet-gem2.png");
    }
    catch (IOException e)
    {
    }
    //show splashscreen
    JLabel imageLabel=new JLabel();
    JLabel infoLabel=new JLabel();
    JLabel logoLabel=new JLabel();
    JLabel title=new JLabel();
    JPanel splashScreen=new JPanel();
    
    try
    {
      logoLabel.setIcon(new ImageIcon(ImageIO.read(new File("pics/CakeSoftInc.png"))));
      title.setIcon(new ImageIcon(ImageIO.read(new File("pics/title.png"))));
    }
    catch (IOException e)
    {
    }
    splashScreen.setLayout(null);
    
    infoLabel.setText("<html>Game: The Scarlet Gem"+
                      "<br><br>Project Lead: Zachary Teper"+
                      "<br><br>Project Representative: Angela Zhu"+
                      "<br><br>Version: 1.0 06.09.2015"+
                      "<br><br>Email: zacharyblacktail@gmail.com"+
                      "<br><br>Phone: 416-223-6075"+
                      "<br><br>The Scarlet Gem is Loading...</html>");
    imageLabel.setIcon(splashImage);
    imageLabel.setBounds(0,0,700,600);
    infoLabel.setBounds(250,150,500,500);
    logoLabel.setBounds(0,0,200,60);
    title.setBounds(200,30,300,220);
    splashScreen.add(logoLabel);
    splashScreen.add(title);
    splashScreen.add(infoLabel);
    splashScreen.add(imageLabel);
    
    add(splashScreen);
    revalidate();
    
    //initialize COUNTRIES
    Question[] canadaEasy=
    {new Question ("<html>What is the capital of Canada?"+
                   "<br> A. Toronto"+
                   "<br> B. Montreal"+
                   "<br> C. Ottawa"+
                   "<br> D. Portugal</html>"
                     ,'C'),
      new Question ("<html>In what part of Canada is <br>Newfoundland located?"+
                    "<br> A. North"+
                    "<br> B. West"+
                    "<br> C. South"+
                    "<br> D. East</html>"
                      ,'D'),
      new Question ("<html>In what part of Canada is Nunavut?"+
                    "<br> A. North"+
                    "<br> B. West"+
                    "<br> C. South"+
                    "<br> D. East</html>"
                      ,'A'),
      new Question ("<html>What continent is Canada part of?"+
                    "<br> A. Europe"+
                    "<br> B. North America"+
                    "<br> C. Africa"+
                    "<br> D. Asia</html>"
                      ,'B'),
      new Question ("<html>What are the two official <br>languages of Canada?"+
                    "<br> A. Spanish & French"+
                    "<br> B. German & English"+
                    "<br> C. English & Spanish"+
                    "<br> D. French & English</html>"
                      ,'D')
    };
    Question[] canadaMedium=
    {new Question ("<html>When was Canada created?"+
                   "<br> A. 1919"+
                   "<br> B. 1407"+
                   "<br> C. 1867"+
                   "<br> D. 1776</html>"
                     ,'C'),
      new Question ("<html>How many countries share <br>a border with Canada?"+
                    "<br> A. 3"+
                    "<br> B. 5"+
                    "<br> C. 2"+
                    "<br> D. 1</html>"
                      ,'D'),
      new Question ("<html>Which metal is most <br>commonly mined in Canada?"+
                    "<br> A. Iron"+
                    "<br> B. Lead"+
                    "<br> C. Gold"+
                    "<br> D. Nickel</html>"
                      ,'A'),
      new Question ("<html>Which is closest to the <br>population of Canada?"+
                    "<br> A. 30 million"+
                    "<br> B. 35 million"+
                    "<br> C. 20 million"+
                    "<br> D. 50 million</html>"
                      ,'B'),
      new Question ("<html>Which is the largest city in Canada"+
                    "<br> A. Montreal"+
                    "<br> B. Vancouver"+
                    "<br> C. St. John's"+
                    "<br> D. Toronto</html>"
                      ,'D')
    };
    Question[] canadaHard=
    {new Question ("<html>Which is closest to the <br>population of Toronto?"+
                   "<br> A. 3 million"+
                   "<br> B. 10 million"+
                   "<br> C. 6 million"+
                   "<br> D. 8 million</html>"
                     ,'C'),
      new Question ("<html>Which is the closest to the <br>length of Yonge St?"+
                    "<br> A. 1000 km"+
                    "<br> B. 1500 km"+
                    "<br> C. 2500 km"+
                    "<br> D. 2000 km</html>"
                      ,'D'),
      new Question ("<html>What is the infant mortality <br>rate of Canada"+
                    "<br> A. 0.47%"+
                    "<br> B. 0.22%"+
                    "<br> C. 0.39%"+
                    "<br> D. 0.62%</html>"
                      ,'A'),
      new Question ("<html>How many Canadian provinces <br>start with the letter N?"+
                    "<br> A. 1"+
                    "<br> B. 3"+
                    "<br> C. 4"+
                    "<br> D. 2</html>"
                      ,'B'),
      new Question ("<html>What proportion of Canada's labour-force <br>is in manufacturing?"+
                    "<br> A. 40%"+
                    "<br> B. 31%"+
                    "<br> C. 67%"+
                    "<br> D. 13%</html>"
                      ,'D')
    };
    try
    {
      Country canada =new Country ("Canada",canadaEasy, canadaMedium,
                                   canadaHard, ImageIO.read (new File ("pics/canada.png")),
                                   new String[]
                                     {""}
      );
      COUNTRIES [0]=canada;
    }
    catch (IOException e)
    {
    }
    //initialize China
    Question[] chinaEasy=
    {new Question ("<html>What is the capital of China?"+
                   "<br> A. Beijing"+
                   "<br> B. Shanghai"+
                   "<br> C. Bangkok"+
                   "<br> D. Tokyo</html>"
                     ,'A'),
      new Question ("<html>Which ocean is off the <br>coast of China?"+
                    "<br> A. Atlantic"+
                    "<br> B. Indian"+
                    "<br> C. Pacific"+
                    "<br> D. Arctic</html>"
                      ,'C'),
      new Question ("<html>In what part of China are the <br>Himalaya Mountains?"+
                    "<br> A. North"+
                    "<br> B. West"+
                    "<br> C. South"+
                    "<br> D. East</html>"
                      ,'B'),
      new Question ("<html>What continent is China part of?"+
                    "<br> A. Europe"+
                    "<br> B. North America"+
                    "<br> C. Africa"+
                    "<br> D. Asia</html>"
                      ,'D'),
      new Question ("<html>What is the main agricultural <br>crop of China?"+
                    "<br> A. Tomatoes"+
                    "<br> B. Rice"+
                    "<br> C. Radishes"+
                    "<br> D. Corn</html>"
                      ,'B')
    };
    Question[] chinaMedium=
    {new Question ("<html>What is the highest point in China?"+
                   "<br> A. Mount Everest"+
                   "<br> B. K2"+
                   "<br> C. Mont Blanc"+
                   "<br> D. Red Mountain</html>"
                     ,'A'),
      new Question ("<html>Which country does NOT <br>border China?"+
                    "<br> A. Mongolia"+
                    "<br> B. Kazakhstan"+
                    "<br> C. Afghanistan"+
                    "<br> D. Georgia</html>"
                      ,'C'),
      new Question ("<html>Which river does NOT flow <br>through China?"+
                    "<br> A. Heilong"+
                    "<br> B. Jordan"+
                    "<br> C. Yellow"+
                    "<br> D. Mekong</html>"
                      ,'B'),
      new Question ("<html>What is the second largest <br>mountain range in China?"+
                    "<br> A. Himalayas"+
                    "<br> B. Tien Shan"+
                    "<br> C. Qilian"+
                    "<br> D. Kunlun</html>"
                      ,'D'),
      new Question ("<html>What is the longest river in China?"+
                    "<br> A. Huang He"+
                    "<br> B. Yangtze"+
                    "<br> C. Songhua"+
                    "<br> D. Huai</html>"
                      ,'B')
    };
    Question[] chinaHard=
    {new Question ("<html>What is the lowest point in China?"+
                   "<br> A. Turpan Pendi"+
                   "<br> B. Hong Kong"+
                   "<br> C. Xi depression"+
                   "<br> D. Bodelle Depression</html>"
                     ,'A'),
      new Question ("<html>What portion of China's <br>population is Buddhist?"+
                    "<br> A. 50%"+
                    "<br> B. 41%"+
                    "<br> C. 18%"+
                    "<br> D. 23%</html>"
                      ,'C'),
      new Question ("<html>What percentage of Chinese <br>export is with Hong Kong?"+
                    "<br> A. 30%"+
                    "<br> B. 17%"+
                    "<br> C. 21%"+
                    "<br> D. 49%</html>"
                      ,'B'),
      new Question ("<html>What year was the People's <br>Republic of China established?"+
                    "<br> A. 1963"+
                    "<br> B. 1412"+
                    "<br> C. 1749"+
                    "<br> D. 1949</html>"
                      ,'D'),
      new Question ("<html>How many provinces does <br>China contain?"+
                    "<br> A. 15"+
                    "<br> B. 23"+
                    "<br> C. 31"+
                    "<br> D. 12</html>"
                      ,'B')
    };
    try
    {
      Country china =new Country ("China",chinaEasy, chinaMedium, chinaHard, ImageIO.read (new File ("pics/china.png")),
                                  new String[]{"The Scarlet Gem is in the country with the largest population in the world."
        ,"The Scarlet Gem is in the counry whose capital is Beijing."
                                    ,"The Scarlet Gem is in the country where bamboo forests, pandas and the Asian Black Bear"+
                                    " can be found.",
                                    "<html>The Scarlet Gem is in the country which was ruled by over 15 different dynasties over the"+
                                    "<br>course of 5000 years.</html>",
                                    "The Scarlet Gem is in the country in which the city of Shanghai is located."
      });
      COUNTRIES [1]=china;
    }
    catch (IOException e)
    {
      
    }
    //initialize USA
    Question[] usaEasy=
    {new Question ("<html>What is the capital of the USA?"+
                   "<br> A. Boston"+
                   "<br> B. New York"+
                   "<br> C. Texas"+
                   "<br> D. Washington D.C.</html>"
                     ,'D'),
      new Question ("<html>In what part of the USA <br>is New Jersey located?"+
                    "<br> A. North"+
                    "<br> B. West"+
                    "<br> C. East"+
                    "<br> D. South</html>"
                      ,'C'),
      new Question ("<html>In which state is San Fransisco located?"+
                    "<br> A. Texas"+
                    "<br> B. California"+
                    "<br> C. Mexico"+
                    "<br> D. Florida</html>"
                      ,'B'),
      new Question ("<html>What continent is the USA part of?"+
                    "<br> A. Europe"+
                    "<br> B. Asia"+
                    "<br> C. Africa"+
                    "<br> D. North America</html>"
                      ,'D'),
      new Question ("<html>What is the official <br>language of the USA?"+
                    "<br> A. English"+
                    "<br> B. Spanish"+
                    "<br> C. French"+
                    "<br> D. Chinese</html>"
                      ,'A')
    };
    Question[] usaMedium=
    {new Question ("<html>How many geographical regions is <br>the USA composed of?"+
                   "<br> A. 2"+
                   "<br> B. 3"+
                   "<br> C. 4"+
                   "<br> D. 5</html>"
                     ,'C'),
      new Question ("<html>Which of the following is NOT <br>a region of the USA?"+
                    "<br> A. South"+
                    "<br> B. North"+
                    "<br> C. West"+
                    "<br> D. Midwest</html>"
                      ,'B'),
      new Question ("<html>Which of the following is NOT a <br>mountain range in the USA?"+
                    "<br> A. Rockies"+
                    "<br> B. Himalayas"+
                    "<br> C. Appalachians"+
                    "<br> D. Cascade</html>"
                      ,'B'),
      new Question ("<html>What is the highest mountain in the USA?"+
                    "<br> A. Mt. St. Elias"+
                    "<br> B. Mt. Sanford"+
                    "<br> C. Mt. Rainier"+
                    "<br> D. Mt. McKinley</html>"
                      ,'D'),
      new Question ("<html>Which river system drains the <br>American Midwest?"+
                    "<br> A. Mississippi"+
                    "<br> B. Colarado"+
                    "<br> C. Red"+
                    "<br> D. Rio Grande</html>"
                      ,'A')
    };
    Question[] usaHard=
    {new Question ("<html>How long is the Mississippi River system?"+
                   "<br> A. 5300 km"+
                   "<br> B. 6853 km"+
                   "<br> C. 5970 km"+
                   "<br> D. 6437 km</html>"
                     ,'C'),
      new Question ("<html>Which is closest to the area of <br>the USA (in square kilometers)?"+
                    "<br> A. 1 Billion"+
                    "<br> B. 9.8 million"+
                    "<br> C. 9.5 million"+
                    "<br> D. 9.7 million</html>"
                      ,'B'),
      new Question ("<html>How tall is Mt. McKinley?"+
                    "<br> A. 4,421 m"+
                    "<br> B. 6,244 m"+
                    "<br> C. 5,489 m"+
                    "<br> D. 6,149 m</html>"
                      ,'D'),
      new Question ("<html>Which is the largest geographical <br>region in the USA?"+
                    "<br> A. Northeast"+
                    "<br> B. Midwest"+
                    "<br> C. West"+
                    "<br> D. South</html>"
                      ,'C'),
      new Question ("<html>What climate is found in the <br>American Midwest?"+
                    "<br> A. Humid continental"+
                    "<br> B. Steppe"+
                    "<br> C. Subtropical"+
                    "<br> D. Marine west coast</html>"
                      ,'A')
    };
    try
    {
      Country usa =new Country ("USA",usaEasy, usaMedium, usaHard, ImageIO.read (new File ("pics/USA.png")),
                                new String[]
                                  {"The Scarlet Gem is in the country in which Chicago is located.",
        "The Scarlet Gem is in the country with the largest military in the world.",
                                    "The Scarlet Gem is in the country whose national flower is the Rose.",
                                    "The Scarlet Gem is in the country whose flag contains stars and stripes.",
                                    "The Scarlet Gem is in the country which contains 50 states."
      });
      COUNTRIES [2]=usa;
    }
    catch (IOException e)
    {
      
    }
    //initialize mexico
    Question[] mexicoEasy=
    {new Question ("<html>What is the capital of Mexico?"+
                   "<br> A. Peru"+
                   "<br> B. Mexico City"+
                   "<br> C. Monterrey"+
                   "<br> D. Cancun</html>"
                     ,'B'),
      new Question ("<html>Which tribal group is <br>indigenous to Mexico?"+
                    "<br> A. Inuit"+
                    "<br> B. Hindi"+
                    "<br> C. Portugal"+
                    "<br> D. Aztec</html>"
                      ,'D'),
      new Question ("<html>Which country is directly <br>north of Mexico?"+
                    "<br> A. USA"+
                    "<br> B. Chile"+
                    "<br> C. Africa"+
                    "<br> D. Arctic</html>"
                      ,'A'),
      new Question ("<html>What continent is Mexico part of?"+
                    "<br> A. Europe"+
                    "<br> B. North America"+
                    "<br> C. Africa"+
                    "<br> D. Asia</html>"
                      ,'B'),
      new Question ("<html>What is the official language of Mexico?"+
                    "<br> A. French"+
                    "<br> B. German"+
                    "<br> C. English"+
                    "<br> D. Spanish</html>"
                      ,'D')
    };
    Question[] mexicoMedium=
    {new Question ("<html>How many countries does Mexico <br>share a border with?"+
                   "<br> A. 4"+
                   "<br> B. 3"+
                   "<br> C. 6"+
                   "<br> D. 2</html>"
                     ,'B'),
      new Question ("<html>Which is the tallest mountain in Mexico?"+
                    "<br> A. Pico de Orizaba"+
                    "<br> B. Socorro"+
                    "<br> C. El Chichon"+
                    "<br> D. Ceburoco</html>"
                      ,'A'),
      new Question ("<html>Which is closest to the <br>population of Mexico City?"+
                    "<br> A. 20 million"+
                    "<br> B. 13 million"+
                    "<br> C. 5 million"+
                    "<br> D. 18 million</html>"
                      ,'A'),
      new Question ("<html>What percentage of Mexico's <br>landmass is water?"+
                    "<br> A. 13.1%"+
                    "<br> B. 2.5%"+
                    "<br> C. 0.3%"+
                    "<br> D. 5.4%</html>"
                      ,'B'),
      new Question ("<html>Which city is not in Mexico?"+
                    "<br> A. Veracruz"+
                    "<br> B. La Paz"+
                    "<br> C. Guadalajara"+
                    "<br> D. Montevideo</html>"
                      ,'D')
    };
    Question[] mexicoHard=
    {new Question ("<html>Approximately how many rivers <br>are in Mexico?"+
                   "<br> A. 30"+
                   "<br> B. 150"+
                   "<br> C. 110"+
                   "<br> D. 70</html>"
                     ,'B'),
      new Question ("<html>How long is the Mexican border with <br>the United States?"+
                    "<br> A. 3000 km"+
                    "<br> B. 6000 km"+
                    "<br> C. 3000 km"+
                    "<br> D. 5000 km</html>"
                      ,'D'),
      new Question ("<html>Which is NOT a resource <br>found in Mexico?"+
                    "<br> A. Iron"+
                    "<br> B. Gold"+
                    "<br> C. Silver"+
                    "<br> D. Copper</html>"
                      ,'A'),
      new Question ("<html>Which is the most active volacno <br>in Mexico?"+
                    "<br> A. Popocatepetl"+
                    "<br> B. Colima"+
                    "<br> C. Barcena"+
                    "<br> D. Tacana</html>"
                      ,'B'),
      new Question ("<html>What year did Europeans first <br>discover Mexico?"+
                    "<br> A. 1431"+
                    "<br> B. 1513"+
                    "<br> C. 1597"+
                    "<br> D. 1521</html>"
                      ,'D')
    };
    try
    {
      Country mexico =new Country ("Mexico",mexicoEasy, mexicoMedium,
                                   mexicoHard, ImageIO.read (new File ("pics/mexico.png")),
                                   new String[]{
        "The Scarlet Gem is in the country which was home to the Maya and Aztec people.",
          "The Scarlet Gem is in the country in which corn was first grown as a crop.",
          "The Scarlet Gem is in the country whose national bird is the Golden Eagle.",
          "The Scarlet Gem is in the southernmost country in North America.",
          "The Scarlet Gem is in the country whose states include Tabasco, Oaxaca and Morelos."
      });
      COUNTRIES [3]=mexico;
    }
    catch (IOException e)
    {
      
    }
    //initialize Portugal
    Question[] portugalEasy=
    {new Question ("<html>What is the capital of Portugal?"+
                   "<br> A. Barcelona"+
                   "<br> B. Lisbon"+
                   "<br> C. Madrid"+
                   "<br> D. Madeira</html>"
                     ,'B'),
      new Question ("<html>Which ocean is off the coast <br>of Portugal?"+
                    "<br> A. Arctic"+
                    "<br> B. Pacific"+
                    "<br> C. Indian"+
                    "<br> D. Atlantic</html>"
                      ,'D'),
      new Question ("<html>Which country is directly <br>east of Portugal?"+
                    "<br> A. Spain"+
                    "<br> B. Chile"+
                    "<br> C. Africa"+
                    "<br> D. Arctic</html>"
                      ,'A'),
      new Question ("<html>What continent is Portugal part of?"+
                    "<br> A. North America"+
                    "<br> B. Europe"+
                    "<br> C. Africa"+
                    "<br> D. Asia</html>"
                      ,'B'),
      new Question ("<html>What is the official <br>language of Portugal?"+
                    "<br> A. French"+
                    "<br> B. German"+
                    "<br> C. English"+
                    "<br> D. Portuguese</html>"
                      ,'D')
    };
    Question[] portugalMedium=
    {new Question ("<html>What is the second largest <br>city in Portugal?"+
                   "<br> A. Lisbon"+
                   "<br> B. Porto"+
                   "<br> C. Aveiro"+
                   "<br> D. Santarem</html>"
                     ,'B'),
      new Question ("<html>What year was the Portuguese <br>Republic established?"+
                    "<br> A. 1944"+
                    "<br> B. 1837"+
                    "<br> C. 1907"+
                    "<br> D. 1910</html>"
                      ,'D'),
      new Question ("<html>What is the main goods-based <br>industry in Portugal?"+
                    "<br> A. Textiles"+
                    "<br> B. Lumber"+
                    "<br> C. Auto parts"+
                    "<br> D. Glassware</html>"
                      ,'A'),
      new Question ("<html>What percentage of the Portuguese <br>labour-force is agriculture-based?"+
                    "<br> A. 5%"+
                    "<br> B. 10%"+
                    "<br> C. 15%"+
                    "<br> D. 2%</html>"
                      ,'B'),
      new Question ("<html>What is the unemployment <br>rate in Portugal?"+
                    "<br> A. 4%"+
                    "<br> B. 10%"+
                    "<br> C. 29%"+
                    "<br> D. 14%</html>"
                      ,'D')
    };
    Question[] portugalHard=
    {new Question ("<html>What is Portugal's main export?"+
                   "<br> A. Wine"+
                   "<br> B. Agricultural products"+
                   "<br> C. Leather"+
                   "<br> D. Pulp and paper</html>"
                     ,'B'),
      new Question ("<html>Which is closest to the size <br>of Madeira (in square kilometers)?"+
                    "<br> A. 300"+
                    "<br> B. 400"+
                    "<br> C. 200"+
                    "<br> D. 800</html>"
                      ,'D'),
      new Question ("<html>Which is the best description of Northern Portugal?"+
                    "<br> A. Mountainous"+
                    "<br> B. Rolling Plains"+
                    "<br> C. Marshy Wetlands"+
                    "<br> D. Flat Desert</html>"
                      ,'A'),
      new Question ("<html>How many UNESCO heritage <br>sites are in Portugal?"+
                    "<br> A. 23"+
                    "<br> B. 15"+
                    "<br> C. 9"+
                    "<br> D. 11</html>"
                      ,'B'),
      new Question ("<html>In which Portuguese city is the <br>Casa da Musica found?"+
                    "<br> A. Lisbon"+
                    "<br> B. Aveiro"+
                    "<br> C. Amadora"+
                    "<br> D. Porto</html>"
                      ,'D')
    };
    try
    {
      Country portugal =new Country ("Portugal",portugalEasy, portugalMedium,
                                     portugalHard, ImageIO.read (new File ("pics/portugal.png")),
                                     new String[]{
        "The Scarlet Gem is in the country which contains the Tagus River.",
          "The Scarlet Gem is in the country in which the Temple of Evora can be found.",
          "The Scarlet Gem is in the country whose national symbol is the Armillary Sphere.",
          "The Scarlet Gem is in the westernmost country in Europe.",
          "The Scarlet Gem is in the country whose districts include Beja, Aveiro and Madeira."
      });
      COUNTRIES [4]=portugal;
    }
    catch (IOException e)
    {
      
    }
    //initialize Australia
    Question[] australiaEasy=
    {new Question ("<html>What is the capital of Australia?"+
                   "<br> A. Sydney"+
                   "<br> B. Canberra"+
                   "<br> C. Brisbane"+
                   "<br> D. New Zealand</html>"
                     ,'B'),
      new Question ("<html>Which ocean is off the west <br>coast of Australia?"+
                    "<br> A. Arctic"+
                    "<br> B. Pacific"+
                    "<br> C. Indian"+
                    "<br> D. Atlantic</html>"
                      ,'C'),
      new Question ("<html>Which country is directly<br> north of Australia?"+
                    "<br> A. Indonesia"+
                    "<br> B. Russia"+
                    "<br> C. Africa"+
                    "<br> D. Mexico</html>"
                      ,'A'),
      new Question ("<html>What continent is Australia part of?"+
                    "<br> A. India"+
                    "<br> B. Australia"+
                    "<br> C. Africa"+
                    "<br> D. Asia</html>"
                      ,'B'),
      new Question ("<html>What is the largest city <br>in Australia?"+
                    "<br> A. New Zealand"+
                    "<br> B. Canberra"+
                    "<br> C. Melbourne"+
                    "<br> D. Sydney</html>"
                      ,'D')
    };
    Question[] australiaMedium=
    {new Question ("<html>What is Australia's most commonly <br>mined substance?"+
                   "<br> A. Silver"+
                   "<br> B. Bauxite"+
                   "<br> C. Tin"+
                   "<br> D. Coal</html>"
                     ,'B'),
      new Question ("<html>What is the second largest <br>city in Australia?"+
                    "<br> A. Sydney"+
                    "<br> B. Canberra"+
                    "<br> C. Melbourne"+
                    "<br> D. Adelaide</html>"
                      ,'C'),
      new Question ("<html>How many states are in Australia?"+
                    "<br> A. 6"+
                    "<br> B. 4"+
                    "<br> C. 3"+
                    "<br> D. 5</html>"
                      ,'A'),
      new Question ("<html>What year did Australia <br>gain independence?"+
                    "<br> A. 1913"+
                    "<br> B. 1901"+
                    "<br> C. 1934"+
                    "<br> D. 1852</html>"
                      ,'B'),
      new Question ("<html>Which constellation is <br>featured on the Australian flag?"+
                    "<br> A. Cygnus"+
                    "<br> B. Ares"+
                    "<br> C. Libra"+
                    "<br> D. Southern Cross</html>"
                      ,'D')
    };
    Question[] australiaHard=
    {new Question ("<html>Where is Tasmania in relation <br>to Australia?"+
                   "<br> A. North"+
                   "<br> B. South"+
                   "<br> C. East"+
                   "<br> D. West</html>"
                     ,'B'),
      new Question ("<html>What is Australia's main export?"+
                    "<br> A. Wheat"+
                    "<br> B. Meat"+
                    "<br> C. Metal"+
                    "<br> D. Coal</html>"
                      ,'D'),
      new Question ("<html>What is Australia's main climate type?"+
                    "<br> A. Semi-arid"+
                    "<br> B. Tropical"+
                    "<br> C. Tundra"+
                    "<br> D. Steppe</html>"
                      ,'A'),
      new Question ("<html>Which is the closest to Australia's <br>Marine Territory (in square kilometers)?"+
                    "<br> A. 1 million"+
                    "<br> B. 8 million"+
                    "<br> C. 7 million"+
                    "<br> D. 3 million</html>"
                      ,'B'),
      new Question ("<html>Which is the longest river <br>in Australia?"+
                    "<br> A. Murchison"+
                    "<br> B. Ashburton"+
                    "<br> C. Victoria"+
                    "<br> D. Murray</html>"
                      ,'D')
    };
    try
    {
      Country australia =new Country ("Australia",australiaEasy, australiaMedium,
                                      australiaHard, ImageIO.read (new File ("pics/australia.png")),
                                      new String[]{
        "The Scarlet Gem is in the country in which Ayers Rock can be found.",
          "The Scarlet Gem is in the country in which the Murray River can be found.",
          "The Scarlet Gem is in the country which is the world's fourth largest producer of wine.",
          "The Scarlet Gem is in the country whose name is the same as its continent's name.",
          "The Scarlet Gem is in the country whose states include New South Wales, Queensland and Victoria."
      });
      COUNTRIES [5]=australia;
    }
    catch (IOException e)
    {
      
    }
    //initialize Egypt
    Question[] egyptEasy=
    {new Question ("<html>What is the capital of Egypt?"+
                   "<br> A. Cairo"+
                   "<br> B. Memphis"+
                   "<br> C. Alexandria"+
                   "<br> D. Suez</html>"
                     ,'A'),
      new Question ("<html>What is the main river in Egypt?"+
                    "<br> A. Congo"+
                    "<br> B. Niger"+
                    "<br> C. Nile"+
                    "<br> D. Jordan</html>"
                      ,'C'),
      new Question ("<html>What is the main terrain <br>type of Egypt?"+
                    "<br> A. Desert"+
                    "<br> B. Tropical"+
                    "<br> C. Arctic"+
                    "<br> D. Mountainous</html>"
                      ,'A'),
      new Question ("<html>What continent is Egypt part of?"+
                    "<br> A. Asia"+
                    "<br> B. Africa"+
                    "<br> C. North America"+
                    "<br> D. Europe</html>"
                      ,'B'),
      new Question ("<html>What is the official <br>language of Egypt?"+
                    "<br> A. English"+
                    "<br> B. French"+
                    "<br> C. German"+
                    "<br> D. Arabic</html>"
                      ,'D')
    };
    Question[] egyptMedium=
    {new Question ("<html>What is the predominant <br>religion in Egypt?"+
                   "<br> A. Muslim"+
                   "<br> B. Catholic"+
                   "<br> C. Buddhist"+
                   "<br> D. Lutheran</html>"
                     ,'A'),
      new Question ("<html>Which is closest to the <br>population of Egypt"+
                    "<br> A. 54 million"+
                    "<br> B. 96 million"+
                    "<br> C. 86 million"+
                    "<br> D. 47 million</html>"
                      ,'C'),
      new Question ("<html>How many regions are in Egypt?"+
                    "<br> A. 27"+
                    "<br> B. 12"+
                    "<br> C. 17"+
                    "<br> D. 31</html>"
                      ,'A'),
      new Question ("<html>What is the main agricultural <br>export of Egypt?"+
                    "<br> A. Rice"+
                    "<br> B. Cotton"+
                    "<br> C. Wheat"+
                    "<br> D. Beans</html>"
                      ,'B'),
      new Question ("<html>What is Egypt's main trading <br>partner?"+
                    "<br> A. India"+
                    "<br> B. Saudi Arabia"+
                    "<br> C. Turkey"+
                    "<br> D. Italy</html>"
                      ,'D')
    };
    Question[] egyptHard=
    {new Question ("<html>What is the length of the Nile River <br>within Egypt?"+
                   "<br> A. 1600 km"+
                   "<br> B. 2400 km"+
                   "<br> C. 1200 km"+
                   "<br> D. 2000 km</html>"
                     ,'A'),
      new Question ("<html>Which oasis is NOT in Egypt?"+
                    "<br> A. Siwah"+
                    "<br> B. Faiyum"+
                    "<br> C. Safsaf"+
                    "<br> D. Baharia</html>"
                      ,'C'),
      new Question ("<html>How many branches does the Nile <br>make at its delta?"+
                    "<br> A. 2"+
                    "<br> B. 6"+
                    "<br> C. 3"+
                    "<br> D. 4</html>"
                      ,'A'),
      new Question ("<html>Which Egyptian town contains <br>the Temple of Amun?"+
                    "<br> A. Cairo"+
                    "<br> B. Siwah"+
                    "<br> C. Giza"+
                    "<br> D. Luxor</html>"
                      ,'B'),
      new Question ("<html>Approximately how large is the <br>Nile delta (in square kilometers)?"+
                    "<br> A. 22000"+
                    "<br> B. 42000"+
                    "<br> C. 15000"+
                    "<br> D. 31000</html>"
                      ,'D')
    };
    try
    {
      Country egypt =new Country ("Egypt",egyptEasy, egyptMedium,
                                  egyptHard, ImageIO.read (new File ("pics/egypt.png")),
                                  new String[]{
        "The Scarlet Gem is in the country which contains the Nile River.",
          "The Scarlet Gem is in the country which contains the Pyramids of Giza.",
          "The Scarlet Gem is in the country in which the Sinai penninsula is located.",
          "The Scarlet Gem is in the country which contains the Valley of Kings and Queens.",
          "The Scarlet Gem is in the country in which the Great Sphinx van be found."
      });
      COUNTRIES [6]=egypt;
    }
    catch (IOException e)
    {
      
    }
    //initialize India
    Question[] indiaEasy=
    {new Question ("<html>What is the capital of India?"+
                   "<br> A. Mumbai"+
                   "<br> B. Bangalore"+
                   "<br> C. New Delhi"+
                   "<br> D. Hyderabad</html>"
                     ,'C'),
      
      new Question ("<html>What is the national currency <br>of India?"+
                    "<br> A. India Dollar"+
                    "<br> B. Yuan"+
                    "<br> C. Euro"+
                    "<br> D. Rupee</html>"
                      ,'D'),
      new Question ("<html>Which colour is NOT featured <br>on the Indian flag?"+
                    "<br> A. Orange (saffron)"+
                    "<br> B. White"+
                    "<br> C. Green"+
                    "<br> D. Yellow</html>"
                      ,'D'),
      new Question ("<html>Which ocean is off the coast <br>of India?"+
                    "<br> A. Indian"+
                    "<br> B. Atlantic"+
                    "<br> C. Pacific"+
                    "<br> D. Arctic</html>"
                      ,'A'),
      new Question ("<html>What is the longest river <br>in India?"+
                    "<br> A. Brahmaputra"+
                    "<br> B. Ganges"+
                    "<br> C. Krishna"+
                    "<br> D. Jamuna</html>"
                      ,'B'),
    };
    Question[] indiaMedium=
    {new Question ("<html>What is the national animal <br>of India"+
                   "<br> A. Asian lion"+
                   "<br> B. Diamond-back python"+
                   "<br> C. Bengal tiger"+
                   "<br> D. Lamprey Eel</html>"
                     ,'C'),
      new Question ("<html>Which country does not <br>directly border India?"+
                    "<br> A. Bangladesh"+
                    "<br> B. Bhutan"+
                    "<br> C. Nepal"+
                    "<br> D. Afghanistan</html>"
                      ,'C'),
      new Question ("<html>How many official <br>languages does India have?"+
                    "<br> A. 16"+
                    "<br> B. 12"+
                    "<br> C. 25"+
                    "<br> D. 8</html>"
                      ,'A'),
      new Question ("<html>What year did India <br>gain independence?"+
                    "<br> A. 1951"+
                    "<br> B. 1947"+
                    "<br> C. 1932"+
                    "<br> D. 1956</html>"
                      ,'B'),
      new Question ("<html>What is the name of the sea directly <br>south of India?"+
                    "<br> A. Arabian Sea"+
                    "<br> B. Caspian Sea"+
                    "<br> C. Timor Sea"+
                    "<br> D. Laccadive Sea</html>"
                      ,'D')
    };
    Question[] indiaHard=
    {new Question ("<html>When is the Indian monsoon season?"+
                   "<br> A. May to July"+
                   "<br> B. October to January"+
                   "<br> C. June to September"+
                   "<br> D. February to April</html>"
                     ,'C'),
      new Question ("<html>What is India's climate type?"+
                    "<br> A. Tundra"+
                    "<br> B. Desert"+
                    "<br> C. Tropical"+
                    "<br> D. Steppe</html>"
                      ,'C'),
      new Question ("<html>How many officially recognized <br>wetlands are in India?"+
                    "<br> A. 71"+
                    "<br> B. 34"+
                    "<br> C. 65"+
                    "<br> D. 93</html>"
                      ,'A'),
      new Question ("<html>What is the main mineral export of India?"+
                    "<br> A. Iron"+
                    "<br> B. Coal"+
                    "<br> C. Jade"+
                    "<br> D. Silver</html>"
                      ,'B'),
      new Question ("<html>What type of tree is founds at <br>Sundarbans in India?"+
                    "<br> A. Cherry"+
                    "<br> B. Almond"+
                    "<br> C. Copperpod"+
                    "<br> D. Mangrove</html>"
                      ,'D')
    };
    try
    {
      Country india =new Country ("India",indiaEasy, indiaMedium,
                                  indiaHard, ImageIO.read (new File ("pics/india.png")),
                                  new String[]{
        "The Scarlet Gem is in the country which contains the Ganges river.",
          "The Scarlet Gem is in the country in which the Ghats mountain range is found.",
          "The Scarlet Gem is in the world's third largest producer of coal.",
          "The Scarlet Gem is in the country in which the Thar desert can be found.",
          "The Scarlet Gem is in the country in which borders Pakistan and Bangladesh."
      });
      COUNTRIES [7]=india;
    }
    catch (IOException e)
    {
      
    }
    //initialize Russia
    Question[] russiaEasy=
    {new Question ("<html>What is the capital of Russia?"+
                   "<br> A. St. Petersburg"+
                   "<br> B. Kazan"+
                   "<br> C. Samara"+
                   "<br> D. Moscow</html>"
                     ,'D'),
      new Question ("<html>What continents is Russia part of?"+
                    "<br> A. Africa & Asia"+
                    "<br> B. Europe & Africa"+
                    "<br> C. Europe & Asia"+
                    "<br> D. Asia & Australia</html>"
                      ,'C'),
      new Question ("<html>Which ocean is off the <br>Northern shore of Russia?"+
                    "<br> A. Arctic"+
                    "<br> B. Atlantic"+
                    "<br> C. Pacific"+
                    "<br> D. Indian</html>"
                      ,'A'),
      new Question ("<html>Which ocean is off the <br>Southeastern shore of Russia?"+
                    "<br> A. Arctic"+
                    "<br> B. Pacific"+
                    "<br> C. Atlantic"+
                    "<br> D. Indian</html>"
                      ,'B'),
      new Question ("<html>Which country does not <br>have a border with Russia?"+
                    "<br> A. China"+
                    "<br> B. Lativa"+
                    "<br> C. Norway"+
                    "<br> D. Syria</html>"
                      ,'D')
    };
    Question[] russiaMedium=
    {new Question ("<html>How many different borders are <br>there between Russia and China?"+
                   "<br> A. 2"+
                   "<br> B. 0"+
                   "<br> C. 1"+
                   "<br> D. 3</html>"
                     ,'A'),
      new Question ("<html>Approximately how large is <br>Russia (in square kilometers)?"+
                    "<br> A. 14 million"+
                    "<br> B. 11 million"+
                    "<br> C. 17 million"+
                    "<br> D. 19 million</html>"
                      ,'C'),
      new Question ("<html>What is Russia's main export?"+
                    "<br> A. Petroleum"+
                    "<br> B. Metals"+
                    "<br> C. Lumber"+
                    "<br> D. Cars</html>"
                      ,'A'),
      new Question ("<html>How many seas are on the <br>shores of Russia?"+
                    "<br> A. 5"+
                    "<br> B. 13"+
                    "<br> C. 9"+
                    "<br> D. 16</html>"
                      ,'B'),
      new Question ("<html>In what part of Russia is <br>Kaliningrad located?"+
                    "<br> A. North"+
                    "<br> B. South"+
                    "<br> C. East"+
                    "<br> D. West</html>"
                      ,'D')
    };
    Question[] russiaHard=
    {new Question ("<html>What another name for Central Russia?"+
                   "<br> A. Siberia"+
                   "<br> B. Moscovia"+
                   "<br> C. Asania"+
                   "<br> D. Centeria</html>"
                     ,'A'),
      new Question ("<html>What is the length of the <br>Ural mountain range"+
                    "<br> A. 1300 km"+
                    "<br> B. 1900 km"+
                    "<br> C. 2400 km"+
                    "<br> D. 2900 km</html>"
                      ,'C'),
      new Question ("<html>Which is the longest <br>river in Europe?"+
                    "<br> A. Volga"+
                    "<br> B. Danube"+
                    "<br> C. Rhine"+
                    "<br> D. Dnieper</html>"
                      ,'A'),
      new Question ("<html>What percentage or Russia is <br>swampland?"+
                    "<br> A. 23%"+
                    "<br> B. 10%"+
                    "<br> C. 15%"+
                    "<br> D. 30%</html>"
                      ,'B'),
      new Question ("<html>What is the average yearly <br>temperature of Russia (in degrees Celsius)?"+
                    "<br> A. 10"+
                    "<br> B. -5"+
                    "<br> C. 0"+
                    "<br> D. 3</html>"
                      ,'D')
    };
    try
    {
      Country russia =new Country ("Russia",russiaEasy, russiaMedium,
                                   russiaHard, ImageIO.read (new File ("pics/russia.png")),
                                   new String[]{
        "The Scarlet Gem is in the largest country in the world.",
          "The Scarlet Gem is in the country which borders 3 oceans.",
          "The Scarlet Gem is in the ocuntry which was once ruled by the Romanov dynasty.",
          "The Scarlet Gem is in the country which is in both Europe and Asia.",
          "The Scarlet Gem is in the ocuntry in which the Ural mountains can be found."
      });
      COUNTRIES [8]=russia;
    }
    catch (IOException e)
    {
      
    }
    //initialize Japan
    Question[] japanEasy=
    {new Question ("<html>What is the capital of Japan?"+
                   "<br> A. Yokohama"+
                   "<br> B. Osaka"+
                   "<br> C. Tokyo"+
                   "<br> D. Nagoya</html>"
                     ,'C'),
      new Question ("<html>What is the main religion of Japan?"+
                    "<br> A. Buddhism"+
                    "<br> B. Christianity"+
                    "<br> C. Shintoism"+
                    "<br> D. Islam</html>"
                      ,'C'),
      new Question ("<html>On which island is Tokyo located?"+
                    "<br> A. Honshu"+
                    "<br> B. Hokkaido"+
                    "<br> C. Shikoku"+
                    "<br> D. Kyushu</html>"
                      ,'A'),
      new Question ("<html>What ocean is Japan located in"+
                    "<br> A. Indian"+
                    "<br> B. Pacific"+
                    "<br> C. Atlantic"+
                    "<br> D. Arctic</html>"
                      ,'B'),
      new Question ("<html>What continent is Japan part of?"+
                    "<br> A. Africa"+
                    "<br> B. Europe"+
                    "<br> C. North America"+
                    "<br> D. Asia</html>"
                      ,'D')
    };
    Question[] japanMedium=
    {new Question ("<html>What percentage of Japan is mountainous?"+
                   "<br> A. 40%"+
                   "<br> B. 20%"+
                   "<br> C. 70%"+
                   "<br> D. 90%</html>"
                     ,'C'),
      new Question ("<html>How many chains of mountains <br>can be found in Japan?"+
                    "<br> A. 5"+
                    "<br> B. 1"+
                    "<br> C. 3"+
                    "<br> D. 2</html>"
                      ,'C'),
      new Question ("<html>When was the last eruption of Mt. Fuji?"+
                    "<br> A. 1707"+
                    "<br> B. 1832"+
                    "<br> C. 1654"+
                    "<br> D. 1953</html>"
                      ,'A'),
      new Question ("<html>What is the name of the plain <br>on which Tokyo is situated?"+
                    "<br> A. Nobi"+
                    "<br> B. Kanto"+
                    "<br> C. Kinai"+
                    "<br> D. Ishikari</html>"
                      ,'B'),
      new Question ("<html>Which sea is directly West of Japan?"+
                    "<br> A. Sea of Okhotsk"+
                    "<br> B. Caspian Sea"+
                    "<br> C. Yellow Sea"+
                    "<br> D. Sea of Japan</html>"
                      ,'D')
    };
    Question[] japanHard=
    {new Question ("<html>What direction does the <br>Oyashio current flow?"+
                   "<br> A. North"+
                   "<br> B. West"+
                   "<br> C. South"+
                   "<br> D. East</html>"
                     ,'C'),
      new Question ("<html>When is the Japanese typhoon season?"+
                    "<br> A. April to May"+
                    "<br> B. November to December"+
                    "<br> C. August to September"+
                    "<br> D. February to March</html>"
                      ,'C'),
      new Question ("<html>What is the hottest temperature <br>ever recorded in Japan (in degrees Celsius)?"+
                    "<br> A. 41"+
                    "<br> B. 38"+
                    "<br> C. 45"+
                    "<br> D. 36</html>"
                      ,'A'),
      new Question ("<html>What direction does the Kuroshio current flow?"+
                    "<br> A. East"+
                    "<br> B. North"+
                    "<br> C. South"+
                    "<br> D. West</html>"
                      ,'B'),
      new Question ("<html>What is the population of Osaka?"+
                    "<br> A. 12 million"+
                    "<br> B. 25 million"+
                    "<br> C. 9 million"+
                    "<br> D. 19 million</html>"
                      ,'D')
    };
    try
    {
      Country japan =new Country ("Japan",japanEasy, japanMedium,
                                  japanHard, ImageIO.read (new File ("pics/japan.png")),
                                  new String[]{
        "The Scarlet Gem is in the country in which Mt. Fuji is located.",
          "The Scarlet Gem is in the country in which Osaka is located.",
          "The Scarlet Gem is in the country which is made up of four main islands.",
          "The Scarlet Gem is in the country which was once ruled by a military shogunate.",
          "The Scarlet Gem is in the country which is directly East of Korea."
      });
      COUNTRIES [9]=japan;
    }
    catch (IOException e)
    {
      
    }
    //initialize France
    Question[] franceEasy=
    {new Question ("<html>What is the capital of France?"+
                   "<br> A. Marceille"+
                   "<br> B. Nice"+
                   "<br> C. Lille"+
                   "<br> D. Paris</html>"
                     ,'D'),
      new Question ("<html>Which continent is France part of?"+
                    "<br> A. Africa"+
                    "<br> B. South America"+
                    "<br> C. Europe"+
                    "<br> D. Asia</html>"
                      ,'C'),
      new Question ("<html>Which river is NOT on the shore of France?"+
                    "<br> A. Volga"+
                    "<br> B. Rhone"+
                    "<br> C. Seine"+
                    "<br> D. Garronne</html>"
                      ,'A'),
      new Question ("<html>Which country does NOT <br>have a border with France?"+
                    "<br> A. Andorra"+
                    "<br> B. England"+
                    "<br> C. Italy"+
                    "<br> D. Spain</html>"
                      ,'B'),
      new Question ("<html>What is the name of the mountain <br>range which seperates France from Spain?"+
                    "<br> A. Alps"+
                    "<br> B. Ural"+
                    "<br> C. Himalayas"+
                    "<br> D. Pyrenees</html>"
                      ,'D')
    };
    Question[] franceMedium=
    {new Question ("<html>Which country is NOT a part of France?"+
                   "<br> A. French Guiana"+
                   "<br> B. New Caledonia"+
                   "<br> C. St. Pierre and Miquelon"+
                   "<br> D. Brazil"
                     ,'D'),
      new Question ("<html>What is the main religion of France?"+
                    "<br> A. Islam"+
                    "<br> B. Hindu"+
                    "<br> C. Catholic"+
                    "<br> D. Lutheran</html>"
                      ,'C'),
      new Question ("<html>What continent is French Guiana part of?"+
                    "<br> A. South America"+
                    "<br> B. North America"+
                    "<br> C. Africa"+
                    "<br> D. Europe</html>"
                      ,'A'),
      new Question ("<html>How many countries share a <br>border with mainland France?"+
                    "<br> A. 5"+
                    "<br> B. 8"+
                    "<br> C. 10"+
                    "<br> D. 6</html>"
                      ,'B'),
      new Question ("<html>What continent is Mayotte part of"+
                    "<br> A. Europe"+
                    "<br> B. North America"+
                    "<br> C. Asia"+
                    "<br> D. Africa</html>"
                      ,'D')
    };
    Question[] franceHard=
    {new Question ("<html>How many countries share a <br>border with French Guiana?"+
                   "<br> A. 5"+
                   "<br> B. 3"+
                   "<br> C. 4"+
                   "<br> D. 2</html>"
                     ,'D'),
      new Question ("<html>What year was the first <br>French republic established?"+
                    "<br> A. 1785"+
                    "<br> B. 1790"+
                    "<br> C. 1792"+
                    "<br> D. 1800</html>"
                      ,'C'),
      new Question ("<html>What is the Easternmost point in France"+
                    "<br> A. Lauterbourg"+
                    "<br> B. Pairs"+
                    "<br> C. Alsace"+
                    "<br> D. Calais</html>"
                      ,'A'),
      new Question ("<html>How many non-EU countries <br>share a border with France?"+
                    "<br> A. 2"+
                    "<br> B. 1"+
                    "<br> C. 0"+
                    "<br> D. 3</html>"
                      ,'B'),
      new Question ("<html>Which best describes the <br>climate of la Reunion?"+
                    "<br> A. Desert"+
                    "<br> B. Tundra"+
                    "<br> C. Steppe"+
                    "<br> D. Tropical</html>"
                      ,'D')
    };
    try
    {
      Country france =new Country ("France",franceEasy,franceMedium, franceHard, ImageIO.read (new File ("pics/france.png")),
                                   new String[]{
        "The Scarlet Gem is in the largest country in Europe.",
          "The Scarlet Gem is in the country which shares a border with Spain and Germany.",
          "The Scarlet Gem is in the country in which the Loire river flows.",
          "The Scarlet Gem is in the first European contry to establish a republic.",
          "The Scarlet Gem is in the country in which Mont Blanc is located."
      });
      COUNTRIES [10]=france;
    }
    catch (IOException e)
    {
      
    }
    //initialize England
    Question[] englandEasy=
    {new Question ("<html>What is the capital of England?"+
                   "<br> A. London"+
                   "<br> B. Leeds"+
                   "<br> C. Birmingham"+
                   "<br> D. Bradford</html>"
                     ,'B'),
      new Question ("<html>Which ocean is off the coast of England?"+
                    "<br> A. Arctic"+
                    "<br> B. Pacific"+
                    "<br> C. Indian"+
                    "<br> D. Atlantic</html>"
                      ,'D'),
      new Question ("<html>Which continent is England a part of?"+
                    "<br> A. Europe"+
                    "<br> B. Asia"+
                    "<br> C. Africa"+
                    "<br> D. South America</html>"
                      ,'A'),
      new Question ("<html>What is the name of the river <br>that flows through London?"+
                    "<br> A. Ply"+
                    "<br> B. Thames"+
                    "<br> C. Danube"+
                    "<br> D. Dee</html>"
                      ,'B'),
      new Question ("<html>What is the Westernmost city in England"+
                    "<br> A. London"+
                    "<br> B. Manchester"+
                    "<br> C. Sheffield"+
                    "<br> D. Cornwall</html>"
                      ,'D')
    };
    Question[] englandMedium=
    {new Question ("<html>What is England's climate type?"+
                   "<br> A. Desert"+
                   "<br> B. Temperate"+
                   "<br> C. Tundra"+
                   "<br> D. Steppe</html>"
                     ,'B'),
      new Question ("<html>Which English city is closest <br>to France?"+
                    "<br> A. London"+
                    "<br> B. Cornwall"+
                    "<br> C. Manchester"+
                    "<br> D. Dover</html>"
                      ,'C'),
      new Question ("<html>What is the longest river <br>in the United Kingdom?"+
                    "<br> A. Severn"+
                    "<br> B. Thames"+
                    "<br> C. Avon"+
                    "<br> D. Wye</html>"
                      ,'A'),
      new Question ("<html>Which is the largest of the <br>English Isles?"+
                    "<br> A. Mersea Island"+
                    "<br> B. Isle of Wight"+
                    "<br> C. Isle of Man"+
                    "<br> D. Isle of Man</html>"
                      ,'B'),
      new Question ("<html>What is the Northernmost <br>city in England?"+
                    "<br> A. London"+
                    "<br> B. Manchester"+
                    "<br> C. Worcester"+
                    "<br> D. Marshall Meadows</html>"
                      ,'D')
    };
    Question[] englandHard=
    {new Question ("<html>Which two countries does <br>Hadrian's Wall seperate?"+
                   "<br> A. Scotland & Ireland"+
                   "<br> B. Scotland & England"+
                   "<br> C. France & England"+
                   "<br> D. France & Ireland</html>"
                     ,'B'),
      new Question ("<html>Which are the heighest <br>mountains in England?"+
                    "<br> A. Mendip Hills"+
                    "<br> B. Pennies"+
                    "<br> C. Cumbrian Mountains"+
                    "<br> D. Shropshire Hills</html>"
                      ,'C'),
      new Question ("<html>What percentage of the United <br>Kingdom is used as grazing land?"+
                    "<br> A. 46%"+
                    "<br> B. 21%"+
                    "<br> C. 63%"+
                    "<br> D. 12%</html>"
                      ,'A'),
      new Question ("<html>Which is the most abundant <br>mineral in England?"+
                    "<br> A. Gold"+
                    "<br> B. Coal"+
                    "<br> C. Silver"+
                    "<br> D. Iron</html>"
                      ,'B'),
      new Question ("<html>Which of the following islands <br>is NOT a British territory?"+
                    "<br> A. Gibraltar"+
                    "<br> B. Montserrat"+
                    "<br> C. Pitcairn Islands"+
                    "<br> D. Corsica</html>"
                      ,'D')
    };
    try
    {
      Country england =new Country ("England",englandEasy, englandMedium, englandHard, ImageIO.read (new File ("pics/england.png")),
                                    new String[]{
        "The Scarlet Gem is in the country where London can be found.",
          "The Scarlet Gem is in the country which was once ruled by Henry IV and Richard II.",
          "The Scarlet Gem is in the country which controlls the Strait of Gibraltar.",
          "The Scarlet Gem is in the largest country in the United Kingdom.",
          "The Scarlet Gem is in the country on the North side of the English Channel."
      });
      COUNTRIES [11]=england;
    }
    catch (IOException e)
    {
      
    }
    try
    {
      Thread.sleep(2000);
    }
    catch (InterruptedException e)
    {
    }
    
    remove (splashScreen);
    
    alreadyBeen=new ArrayList<Country>();
    currentCountry=COUNTRIES[0];
    
    //initialize menus
    JMenuBar menuBar=new JMenuBar();
    add (menuBar);
    setJMenuBar (menuBar);
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    
    
    fileMenu.add(saveItem);
    fileMenu.add(printItem);
    fileMenu.add(highScoresItem);
    fileMenu.add(exitItem);
    
    helpMenu.add(howToPlayItem);
    helpMenu.add(helpItem);
    helpMenu.add(aboutItem);
    
    howToPlayItem.addActionListener(this);
    printItem.addActionListener(this);
    saveItem.addActionListener(this);
    saveItem.setEnabled (false);
    exitItem.addActionListener(this);
    highScoresItem.addActionListener(this);
    
    helpItem.addActionListener(this);
    aboutItem.addActionListener(this);
    
    //load main menu
    mainMenuPanel=new MainMenuPanel();
    add(mainMenuPanel);
    mainMenuPanel.getEasyButton().addActionListener(this);
    mainMenuPanel.getMediumButton().addActionListener(this);
    mainMenuPanel.getHardButton().addActionListener(this);
    mainMenuPanel.getLoadButton().addActionListener(this);
    
    saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                                   Event.CTRL_MASK));
    exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                                   Event.CTRL_MASK));
    howToPlayItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                                                        Event.CTRL_MASK));
    printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                                                    Event.CTRL_MASK));
    aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
                                                    Event.CTRL_MASK));
    helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                                                   Event.CTRL_MASK));
    highScoresItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                                                         Event.CTRL_MASK));
    revalidate();
    
    
    levelCounter=new JLabel(levelsRemaining+"");
    addWindowListener(this);
    revalidate();
    new InstructionsViewer ();
  }
  
}
