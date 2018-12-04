/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advancedhangman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Justin/Zachary
 */
public class Controller {
    private View views;
    private ModelBean models;
    private String username, password,x,d, word,diff;
    public Users m;
    public char[] wordblanks;
    private int difficulty, counter=0, wins, loss;
    private char c;
    public History h;
    
    Controller (ModelBean model, View view)
    {
        views = view;
        views.addLoginAL(new openListener());
        views.addRegisterAL(new openListener2());
        views.addConfirmAL(new confirmListener()); 
        views.addDifficultyAL(new difficultyListener());
        views.addLettersAL(new letterListener());
        views.addSdAL(new difficultyBttnListener());
        views.addNewGameAL(new newGameListener());
       
        models = model;
        
    }
    
    class openListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           views.login();
        }
    }
    
    class openListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            views.register();
        }
    }
    
    class confirmListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            x = ((javax.swing.JButton)e.getSource()).getText();
            
            if(x.equals("Confirm "))
            {
                views.saveLogin();
                if (DatabaseBean.testLogin(views.getUsername(), views.getPassword1()) == true)
                {
                    views.continue1();
                    h = DatabaseBean.retrieveHistory();
                    wins = h.getW();
                    loss = h.getL();
                    views.chooseDifficulty();
                }
                else
                {
                    views.mismatch();
                }      
            }
            else
            {
                views.saveRegister();
                if (views.getPassword1().equals(views.getPassword2()))
                {
                    m = new Users(views.getUsername(), views.getPassword1());
                    DatabaseBean.writeUser(m);
                    wins = 0;
                    loss = 0;
                    DatabaseBean.updateWin(wins);
                    DatabaseBean.updateLoss(loss);
                    h = DatabaseBean.retrieveHistory();
                    wins = h.getW();
                    loss = h.getL();
                    views.continue2();
                    views.chooseDifficulty();
                }
                else
                {
                    views.mismatch2();
                }
            } 
        }
    }
    
    class difficultyListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            diff = ((javax.swing.JButton)e.getSource()).getText();
            
            switch (diff) {
                case "Easy":  difficulty = 1; break;
                case "Medium":  difficulty = 2; break;
                case "Hard":  difficulty = 3; break;
            }
            word = DatabaseBean.retrieveWord(difficulty);
            views.updateOutput(displayBlanks(word));
            views.setWinLoss(wins, loss, diff);
            System.out.println(wins + " " + loss);
            views.setUserTab(views.getUsername(),wins, loss);
            views.display();
            
        }
    }
    
    class difficultyBttnListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            views.chooseDifficulty();
        }
    }
    
    public String displayBlanks(String word)
    {
        String x = "";
        wordblanks = new char[word.length()];
        for(int i=0; i< word.length();i++)
        {
            wordblanks[i] = '_';
        }
        String b = new String(wordblanks);
        
        x = b.replace("", " ").trim();
        return x;
    }
    
    class letterListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            d = ((javax.swing.JButton)e.getSource()).getText().toLowerCase();
            c = d.charAt(0);
            int x = c - 97;
            views.disableButton(x);
            verify(word, d);
            
        }
    }
    
      
    class newGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            counter = 0;
            controlReset();
            views.viewreset();
        }
    }
    
    public void verify(String word, String letter)
    {
        boolean b = false;
        char [] myword = word.toCharArray();
        char x = letter.charAt(0);
        for(int i=0; i<myword.length; i++)
        {
            if(x==myword[i])
            {
                wordblanks[i] = letter.charAt(0);
                b = true;
            }
        }
        
        if (b==false)
        {
           views.draw(0);
           counter++;
           views.draw(counter);
           if(counter == 6)
           {
               counter = 0;
               DatabaseBean.updateLoss((loss));
               h = DatabaseBean.retrieveHistory();
               loss = h.getL();
               views.loser(word);
               controlReset(); 
           }
        }
        else{
            displayLetter(wordblanks);
        }
    }
    
    public void displayLetter(char[] x)
    {    
        String b = new String(x);
        if(b.equals(word))
        {
            counter = 0;
            DatabaseBean.updateWin(wins);
            h = DatabaseBean.retrieveHistory();
            wins = h.getW();
            views.updateOutput(b);
            views.winner();
            controlReset();
        }
        else
        {
            b = b.replace("", " ").trim();
            views.updateOutput(b);
        }
    }
    
    public void controlReset()
    {
        h = DatabaseBean.retrieveHistory();
        word = DatabaseBean.retrieveWord(difficulty);
        views.updateOutput(displayBlanks(word));
        views.setWinLoss(wins, loss, diff);
        views.setUserTab(views.getUsername(),h.getW(), h.getL());
        views.display();
    }
    
}
