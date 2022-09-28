import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SwingFocusTest extends JPanel {

    static JFrame frame;
    JLabel label;
    JCheckBox togglePolicy;
    static MyOwnFocusTraversalPolicy newPolicy;
    public SwingFocusTest() {
        super();
        GridLayout layout = new GridLayout(3, 3);
        setLayout(layout);
        JButton[] buttons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            JButton b = buttons[i];
            b.setText("" + i);
            add(b);
            if (i == 1)
                b.setFocusable(false);
            if (i == 4)
                b.setVisible(false);
            if (i == 7)
                b.setEnabled(false);
        }

        Vector<Component> order = new Vector<>(9);
        order.add(buttons[0]);
        order.add(buttons[3]);
        order.add(buttons[6]);
        order.add(buttons[1]);
        order.add(buttons[4]);
        order.add(buttons[7]);
        order.add(buttons[2]);
        order.add(buttons[5]);
        order.add(buttons[8]);
        newPolicy = new MyOwnFocusTraversalPolicy(order);
        frame.setFocusTraversalPolicy(newPolicy);
    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("SwingFocusTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new SwingFocusTest();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static class MyOwnFocusTraversalPolicy
            extends FocusTraversalPolicy
    {
        Vector<Component> order;

        public MyOwnFocusTraversalPolicy(Vector<Component> order) {
            this.order = new Vector<Component>(order.size());
            this.order.addAll(order);
        }
        public Component getComponentAfter(Container focusCycleRoot,
                                           Component aComponent)
        {
            int idx = (order.indexOf(aComponent) + 1) % order.size();
            return order.get(idx);
        }

        public Component getComponentBefore(Container focusCycleRoot,
                                            Component aComponent)
        {
            int idx = order.indexOf(aComponent) - 1;
            if (idx < 0) {
                idx = order.size() - 1;
            }
            return order.get(idx);
        }

        public Component getDefaultComponent(Container focusCycleRoot) {
            return order.get(0);
        }

        public Component getLastComponent(Container focusCycleRoot) {
            return order.lastElement();
        }

        public Component getFirstComponent(Container focusCycleRoot) {
            return order.get(0);
        }
    }
}
