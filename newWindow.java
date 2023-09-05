import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class newWindow extends JFrame {

    private static int weigth;
    private static int heigth;
    private static int mult = 10;

    private JTextField txtFunction1;
    public static JCheckBox checkBoxFunction1;
    private JTextField txtFunction2;
    public static JCheckBox checkBoxFunction2;
    private JTextField txtFunction3;
    public static JCheckBox checkBoxFunction3;
    private JTextField txtStep;
    private JTextField txtMinX;
    private JTextField txtMaxX;
    private JLabel lblLifeScroll;
    private JCheckBox checkBoxLifeScroll;
    private static JTextField txtStatMultForX;
    private JTextField txtLayoutSize;
    private JCheckBox checkBoxGrid;



    private JButton btnDrawOutLineOfGraph;


    private JPanel panelCenter;
    private Graph graph;

    private static int biggest = 0;

    private static Rectangle rectangle;

    public newWindow(int w, int h)
    {
        super("Graphing Calculator");
        setSize(w,h);
        //setResizable(false);
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);


        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();

                        //if (biggest < notches) {
                        //    biggest = notches;
                        //    System.out.println(biggest);
                //}
                //boolean b = checkBoxLifeScroll.getState();
                if(checkBoxLifeScroll.isSelected())
                {
                    if(notches<0)
                    {
                        mult+=Math.abs(notches);
                        lblLifeScroll.setText("Resize (" +mult + "): ");

                    }else{
                        mult-=Math.abs(notches);

                        if(mult>0)
                            lblLifeScroll.setText("Resize (" +mult + "): ");

                    }

                    if(mult>0) {
                        panelCenter.removeAll();
                        panelCenter.updateUI();

                        double s = Double.parseDouble(txtStep.getText());
                        boolean g = checkBoxGrid.isSelected();


                        String f1 = txtFunction1.getText();
                        String f2 = txtFunction2.getText();
                        String f3 = txtFunction3.getText();
                        drawGraph(f1, f2, f3 ,s , g);
                        //addFunction();

                        refresh();
                    }else
                        mult=1;
                }
            }
        });

        weigth = w;
        heigth = h;

        JPanel emptyPanelForEast = new JPanel();
        emptyPanelForEast.setLayout(new FlowLayout());
        emptyPanelForEast.setBackground(Color.GRAY);
        add(emptyPanelForEast, BorderLayout.EAST);

        JPanel emptyPanelForTop = new JPanel();
        emptyPanelForTop.setLayout(new FlowLayout());
        emptyPanelForTop.setBackground(Color.GRAY);
        add(emptyPanelForTop, BorderLayout.NORTH);

        JPanel emptyPanelForButtom = new JPanel();
        emptyPanelForButtom.setLayout(new FlowLayout());
        emptyPanelForButtom.setBackground(Color.GRAY);
        add(emptyPanelForButtom, BorderLayout.SOUTH);

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        add(panelLeft, BorderLayout.WEST);

        ///////////////////////////////////
        JPanel panFunctions = new JPanel();
        panFunctions.setLayout(new BoxLayout(panFunctions, BoxLayout.PAGE_AXIS));
        panFunctions.setBackground(Color.gray);

        JPanel panFormula1 = new JPanel();
        panFormula1.setLayout(new FlowLayout());
        panFunctions.add(panFormula1);
        addInPut(panFormula1, "Enter function: Y=",txtFunction1 =  new JTextField(10));
        checkBoxFunction1 = new JCheckBox();
        panFormula1.add(checkBoxFunction1);


        JPanel panFormula2 = new JPanel();
        panFormula2.setLayout(new FlowLayout());
        panFunctions.add(panFormula2);
        addInPut(panFormula2, "Enter function: Y=",txtFunction2 =  new JTextField(10));
        checkBoxFunction2 = new JCheckBox();
        panFormula2.add(checkBoxFunction2);

        JPanel panFormula3 = new JPanel();
        panFormula3.setLayout(new FlowLayout());
        panFunctions.add(panFormula3);
        addInPut(panFormula3, "Enter function: Y=",txtFunction3 =  new JTextField(10));
        checkBoxFunction3 = new JCheckBox();
        panFormula3.add(checkBoxFunction3);

        panelLeft.add(panFunctions);
        ///////////////////////////////

        JPanel panStep = new JPanel();
        panStep.setLayout(new FlowLayout());
        panelLeft.add(panStep);
        addInPut(panStep, "Step: ", txtStep = new JTextField(".001",5));

        JPanel panStatMultForX = new JPanel();
        panStatMultForX.setLayout(new FlowLayout());
        panelLeft.add(panStatMultForX);
        txtStatMultForX = new JTextField("0",2);
        addInPut(panStatMultForX,"Stat. Xaxis: ", txtStatMultForX);

        JPanel panLifeScroll = new JPanel();
        panLifeScroll.setLayout(new FlowLayout());
        panelLeft.add(panLifeScroll);
        lblLifeScroll = new JLabel("Resize (" +mult + ") ");
        panLifeScroll.add(lblLifeScroll);
        checkBoxLifeScroll = new JCheckBox();
        panLifeScroll.add(checkBoxLifeScroll);

        JPanel panLayout = new JPanel();
        panLayout.setLayout(new FlowLayout());
        panelLeft.add(panLayout);
        JLabel label = new JLabel("Grid ");
        checkBoxGrid = new JCheckBox();
        panLayout.add(label);
        panLayout.add(checkBoxGrid);
        panelLeft.add(panLayout);







        btnDrawOutLineOfGraph = new JButton("Graph");
        panelLeft.add(btnDrawOutLineOfGraph);


        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        add(panelCenter, BorderLayout.CENTER);






        thehandler handler = new thehandler();
        btnDrawOutLineOfGraph.addActionListener(handler);
    }

    private void addInPut(JPanel pan, String prompt, JTextField textField)
    {
        JLabel label = new JLabel(prompt);
        pan.add(label);
        pan.add(textField);

    }



    private class thehandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {

            if(event.getSource() == btnDrawOutLineOfGraph)
            {
                panelCenter.removeAll();
                panelCenter.updateUI();

                double s = Double.parseDouble(txtStep.getText());
                boolean g = checkBoxGrid.isSelected();


                String f1 = txtFunction1.getText();
                String f2 = txtFunction2.getText();
                String f3 = txtFunction3.getText();
                drawGraph(f1, f2, f3 ,s , g);
                //addFunction();

                refresh();

            }
        }
    }



    public static int getStatMultForX()
    {
        if(txtStatMultForX.getText() == "")
            return 0;
        return Integer.parseInt(txtStatMultForX.getText());
    }

    public static void setMult(int m)
    {
        mult = m;
    }

    public static int getMult()
    {
        return mult;
    }

    public static int getWeight()
    {
        return weigth;
    }

    public static int getHeigth()
    {
        return heigth;
    }

    public static Rectangle getGraphArea()
    {
        return rectangle;
    }

    /*
    public void addFunction()
    {

        try {
            graph.drawFunc();
            panelCenter.add(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public void drawGraph(String f1, String f2, String f3, double s, boolean g)
    {
        graph = new Graph(f1, f2, f3, s, g);
        panelCenter.add(graph);
    }

    public void refresh()
    {
        //refresh window
        invalidate();
        validate();
        repaint();
    }
}
