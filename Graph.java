import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;



public class Graph extends JPanel
{

    private static int maxX;
    private static int minX;
    private Graphics2D g2;
    private double Step;

    private String Function1;
    private String Function2;
    private String Function3;

    private static Rectangle GraphArea;
    private boolean Grid = false;

    public Graph(String f1, String f2, String f3, double s, boolean g)
    {
        Function1 = f1;
        Function2 = f2;
        Function3 = f3;
        Step = s;
        Grid = g;
    }

    public void setGrid(boolean b)
    {
        Grid = b;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GraphArea = newWindow.getGraphArea();
        g2 = (Graphics2D)g;
        double tempNum;

        //setBackground(Color.darkGray);
        g2.setColor(Color.darkGray);
        GraphArea = new Rectangle(0, 0, getParent().getWidth(), getParent().getHeight());
        g2.draw(GraphArea);


        g2.setStroke(new BasicStroke(3));
        //Draw Y axis
        Point2D.Double p1 = new Point2D.Double(GraphArea.getX() + GraphArea.getWidth() / 2, 0);
        Point2D.Double p2 = new Point2D.Double(GraphArea.getX() + GraphArea.getWidth() / 2, getParent().getHeight());
        Line2D.Double Yaxis = new Line2D.Double(p1, p2);
        g2.draw(Yaxis);

        //Draw X axis
        p1 = new Point2D.Double(0, GraphArea.getY() + GraphArea.getHeight() / 2);
        p2 = new Point2D.Double(getParent().getWidth(), GraphArea.getY() + GraphArea.getHeight() / 2);
        Line2D.Double Xaxis = new Line2D.Double(p1, p2);
        g2.draw(Xaxis);


        //Set MaxX and minX
        tempNum = newWindow.getMult();
        if(tempNum==0)
            tempNum = 1;

        //set total units on graph area
        double totalUnits;
        if(newWindow.getStatMultForX()==0)
            totalUnits = GraphArea.getWidth()/tempNum;
        else {
            totalUnits = getParent().getWidth();
            Step = .1;
        }
        maxX = (int)(totalUnits/2);
        minX = -maxX;
        maxX++;
        minX--;
        //set unit size
        if(!Grid) {
            //for Xaxis
            for (int i = minX; i <= maxX; i++)
                g2.draw(getLine(ColbPoint(i, 5, true, false), ColbPoint(i, -5, true, false)));
            //for Yaxis
            for (int i = minX; i <= maxX; i++)
                g2.draw(getLine(ColbPoint(5, i, false, true), ColbPoint(-5, i, false, true)));
        }else{
            g2.setStroke(new BasicStroke(1));
            //for Xaxis
            for (int i = minX; i <= maxX; i++)
                g2.draw(getLine(ColbPoint(i, getParent().getHeight()/2, true, false), ColbPoint(i, -getParent().getHeight()/2, true, false)));
            //for Yaxis
            for (int i = minX; i <= maxX; i++)
                g2.draw(getLine(ColbPoint(getParent().getWidth()/2, i, false, true), ColbPoint(-getParent().getWidth()/2, i, false, true)));

            g2.setStroke(new BasicStroke(3));
        }

        g2.setColor(Color.BLUE);
        if(newWindow.checkBoxFunction1.isSelected()){
        try {
            drawFunc(Function1);
        } catch (Exception e) {
            e.printStackTrace();
        }}

        g2.setColor(Color.RED);
        if(newWindow.checkBoxFunction2.isSelected()){
        try {
            drawFunc(Function2);
        } catch (Exception e) {
            e.printStackTrace();
        }}

        g2.setColor(Color.green);
        if(newWindow.checkBoxFunction3.isSelected()){
        try {
            drawFunc(Function3);
        } catch (Exception e) {
            e.printStackTrace();
        }}



    }


    public void drawFunc(String f) throws Exception
    {



        for(double i = (double)minX; i <= (double)maxX; i+=Step)
        {
            double tempX = i;
            double tempY = getYFromGivenX(f, i);


            Point2D.Double tempPoint = ColbPoint(tempX, tempY, false, false);
            g2.draw(getLine(tempPoint, tempPoint));
        }
    }

    private Line2D.Double getLine(Point2D.Double p1, Point2D.Double p2)
    {
        return new Line2D.Double(p1,p2);
    }

    private Point2D.Double ColbPoint(double x, double y, boolean StatY, boolean StatX)
    {
        if(StatY) {
            x *= newWindow.getMult();
        }else if(StatX){
            y *= newWindow.getMult();

        }else{

            y*=-newWindow.getMult();

            if(newWindow.getStatMultForX()!=0)
            {
                int statMult = newWindow.getStatMultForX();
                x*=statMult;
            }else{
                x*=newWindow.getMult();
            }

        }

        double theX = ((x+GraphArea.getX())+GraphArea.getWidth()/2);
        double theY = ((y+GraphArea.getY())+GraphArea.getHeight()/2);

        return new Point2D.Double(theX,theY);
        //return new Point2D.Double(x,y);
    }

    private double getYFromGivenX(String function, double x) throws Exception
    {

        function = function.replaceAll("X","x");
        function = function.replaceAll("x", Double.toString(x));

        //String s = (engine.eval(function)).pa();
        //double d = Double.parseDouble(eval(function));

        return eval(function) ;
    }

    private static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                //if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    //else if (func.equals("pow")) x = Math.pow()
                    //else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}