import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class SwingFocusTest extends JPanel implements FocusListener {

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
            buttons[i] = new JButton(String.valueOf(i));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            add(buttons[i]);
            buttons[i].addFocusListener(this);
        }
        buttons[1].setFocusable(false);
        buttons[1].setText("1: Unfocusable");
        buttons[4].setVisible(false);
        buttons[4].setText("4: Invisible");
        buttons[7].setEnabled(false);
        buttons[7].setText("7: Disabled");


        // Add the buttons column by column
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

        Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newForwardKeys = new HashSet<>(forwardKeys);
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);

        Set<AWTKeyStroke> backwardKeys = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newBackwardKeys = new HashSet<>(backwardKeys);
        newBackwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
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
        frame.setSize(900, 900);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(SwingFocusTest::createAndShowGUI);
    }

    @Override
    public void focusGained(FocusEvent e) {
        ((JButton) e.getSource()).setBackground(Color.CYAN);
    }

    @Override
    public void focusLost(FocusEvent e) {
        ((JButton) e.getSource()).setBackground(null);
    }

    public static class MyOwnFocusTraversalPolicy
            extends FocusTraversalPolicy {
        Vector<Component> order;

        public MyOwnFocusTraversalPolicy(Vector<Component> order) {
            this.order = new Vector<>(order.size());
            this.order.addAll(order);
        }

        public Component getComponentAfter(Container focusCycleRoot,
                                           Component aComponent) {
            int idx = (order.indexOf(aComponent) + 1) % order.size();
            while (!order.get(idx).isVisible() || !order.get(idx).isEnabled() || !order.get(idx).isFocusable())
                idx = (idx + 1) % order.size();
            return order.get(idx);
        }

        public Component getComponentBefore(Container focusCycleRoot,
                                            Component aComponent) {
            int idx = order.indexOf(aComponent) - 1;
            if (idx < 0) {
                idx = order.size() - 1;
            }
            while (!order.get(idx).isVisible() || !order.get(idx).isEnabled() || !order.get(idx).isFocusable()) {
                idx--;
                if (idx < 0) {
                    idx = order.size() - 1;
                }
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
