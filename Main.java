import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MyWait {

    static void myWait(int x) {
        try {
            TimeUnit.MILLISECONDS.sleep(x);
        } catch (Exception e) {
            return;
        }

    }

}

class MyFrame extends JFrame implements KeyListener {
    char keyPress;
    static boolean open;

    MyFrame() {
        this.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

        // throw new UnsupportedOperationException("Unimplemented method
        // 'keyReleased'");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        if (open == false) {
            return;
        }
        this.keyPress = e.getKeyChar();
        System.out.println("a ey is pressed");
        // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    char keyWait() {
        this.keyPress = 0;
        if (open == false) {
            throw new UnsupportedOperationException("Closed Scanner");
        }

        while (this.keyPress == 0) {
            System.out.println("Waiting for Keypress");
        }
        return this.keyPress;
    }

    boolean waitUntil(char key) {
        this.keyPress = 0;
        if (open == false) {
            throw new UnsupportedOperationException("Closed Scanner");
        }

        while (this.keyPress != key) {
            System.out.println("Waiting for Keypress");
        }
        return true;

    }
}

class myPanel extends JPanel {
    int x, y;
    int h, w;
    int x2, y2;
    double slope;

    void update() {
        this.x2 = this.x + this.w;
        this.y2 = this.y + this.h;

        try {
            this.slope = y / x;
        } catch (Exception e) {
            this.slope = 0;
            // TODO: handle exception
        }
        this.setBounds(this.x, this.y, this.w, this.h);
        System.out.printf("%d, %d, %d, %d \n", this.x, this.y, this.w, this.h);
    }

    void step(int x, int y) {
        this.x = x;
        this.y = y;
        update();
    }

    void moveTo(int x, int y, int milli) { // need add sliding effect
        final int yFinal = this.y;
        final int xFinal = this.x;
        double yChange = y - this.y;
        double xChange = x - this.x;

        for (int i = 1; i <= 100; i++) {
            // System.out.println(((double) yChange / 100) * i);
            this.y = yFinal + (int) Math.round(((double) yChange / 100) * i);
            this.x = xFinal + (int) Math.round(((double) xChange / 100) * i);

            MyWait.myWait((int) (milli / 100));
            update();

        }
    }

    myPanel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        // this.addKeyListener(this);
        // this.setBackground(Color.RED);
        // this.setBounds(x, y, x + w, y + h);
        update();

    }
}

class MyCard extends myPanel {
    int value;
    int interact; // this is boolean
    boolean fold = false;
    JLabel label = new JLabel();
    ImageIcon icon = new ImageIcon();

    Color back = Color.RED;
    Color front = Color.WHITE;

    static int hit;
    static int handValue, dealerValue;

    static char keyPress;
    static int open;

    void cardUpdate() {
        this.label.setText(value == 1 ? "A" : String.valueOf(value));
        this.label.setFont(new Font("Serif", Font.PLAIN, 50));
        this.setBackground((fold) ? back : front);
        this.label.setVisible(!fold);
        update();
    }

    void randomCard(int max, ArrayList<ImageIcon> suits) {
        Random rand = new Random();
        this.value = rand.nextInt(max) + 1;
        this.label.setIcon(suits.get(rand.nextInt(suits.size())));

    }

    MyCard(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.add(label);
        // this.label.setHorizontalTextPosition(JLabel.CENTER);
        // this.label.setVerticalTextPosition(JLabel.CENTER);
        // this.label.se

        this.label.setIcon(icon);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        cardUpdate();
        // this.addKeyListener(this);
    }

}

public class Main {

    static void removeArrayList(MyFrame frame, ArrayList<MyCard> hand) {
        while (hand.size() != 0) {
            frame.remove(hand.get(0));
            hand.remove(0);
        }
    }

    static void allignTest(ArrayList<MyCard> test, int border) {
        for (int i = 0; i < test.size(); i++) {
            test.get(i).cardUpdate();
            test.get(i).moveTo((250 * (test.size() - i)), 720 - border, 50);
            System.out.println((i / (test.size() + 1)));
        }

    }

    static void stackCard(ArrayList<MyCard> hand, int x, int y) {
        for (int i = 0; i < hand.size(); i++) {
            hand.get(i).cardUpdate();
            hand.get(i).moveTo(x, y, 50);
        }
    }

    static int cardCount(ArrayList<MyCard> hand) { // blackJack Count
        int handValue = 0;
        int aces = 0;
        for (int i = 0; i < hand.size(); i++) {
            aces += hand.get(i).value == 1 ? 1 : 0;
            handValue += hand.get(i).value;
        }
        handValue = (handValue + (aces * 10)) <= 21 ? handValue + (aces * 10) : handValue;

        return handValue;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        ArrayList<MyCard> deck = new ArrayList<MyCard>();
        ArrayList<MyCard> dealer = new ArrayList<MyCard>();
        ArrayList<MyCard> hand = new ArrayList<MyCard>();

        ArrayList<ImageIcon> suits = new ArrayList<ImageIcon>();
        ArrayList<ImageIcon> suitsScaled = new ArrayList<ImageIcon>();
        suits.add(new ImageIcon("spade.png"));
        suits.add(new ImageIcon("club.png"));
        suits.add(new ImageIcon("diamond.png"));
        suits.add(new ImageIcon("heart.png"));

        for (int i = 0; i < 4; i++) {
            ImageIcon temp = suits.get(i);
            suitsScaled.add(new ImageIcon(temp.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
        }

        MyCard gameState = new MyCard(((1280 - 700) / 2), 0, 700, 70);

        MyFrame game = new MyFrame();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setSize(1280, 720);
        game.setLayout(null);
        game.setVisible(true);

        game.add(gameState);

        MyFrame.open = true;
        boolean hold = false;
        boolean reset = true;
        do {
            int yourHand = cardCount(hand);
            int dealerHand = cardCount(dealer);
            gameState.label.setText("Hand: " + yourHand + "  Dealer: " + dealerHand);

            char key = (hold || reset) ? 'a' : game.keyWait();
            game.revalidate();
            if (hold == false && key == 'h') {
                hand.add(new MyCard(0, 0, 200, 250));
                hand.get(hand.size() - 1).randomCard(10, suitsScaled);
                game.add(hand.get(hand.size() - 1));
                yourHand = cardCount(hand);
                allignTest(hand, 300);
            }

            if (key == 's' || hold == true) {
                hold = true;
                dealer.add(new MyCard(0, 0, 200, 250));
                dealer.get(dealer.size() - 1).randomCard(10, suitsScaled);
                game.add(dealer.get(dealer.size() - 1));
                dealerHand = cardCount(dealer);
                MyWait.myWait(1000);
                allignTest(dealer, 300 + 250 + 50);
            }

            gameState.label.setText("Hand: " + yourHand + "  Dealer: " + dealerHand);

            if (yourHand > 21 || dealerHand > 21 || (dealerHand >= yourHand && hold)) {
                String result = new String();
                if (yourHand > 21) {
                    result = "Bust";
                } else if (dealerHand > yourHand && hold) {
                    result = dealerHand > 21 ? "Win" : "Lose";
                } else if (dealerHand == yourHand) {
                    result = "Draw";
                }
                gameState.label.setText("Hand: " + yourHand + "  Dealer: " + dealerHand + " - " + result);

                reset = true;
            }

            /* */
            if (reset) {
                MyWait.myWait(2000);
                stackCard(hand, 1280, 0);
                stackCard(dealer, 1280, 0);
                removeArrayList(game, hand);
                removeArrayList(game, dealer);

                for (int i = 0; i < 2; i++) {
                    hand.add(new MyCard(0, 0, 200, 250));
                    hand.get(i).randomCard(10, suitsScaled);
                    ;
                    game.add(hand.get(i));
                    allignTest(hand, 300);
                }

                dealer.add(new MyCard(0, 0, 200, 250));
                dealer.get(0).randomCard(10, suitsScaled);
                ;
                game.add(dealer.get(0));
                allignTest(dealer, 300 + 250 + 10);

                game.invalidate();
                game.validate();
                game.repaint();
                hold = false;
                reset = false;
            }

        } while (true);
    }

}