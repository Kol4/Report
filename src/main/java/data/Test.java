package data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Test {
    private ArrayList<String> infos1;
    private ArrayList<String> infos2;
    private ArrayList<String> infos3;

    public boolean isShowing = true;

    public Test(Consumer<List> listConsumer1, Consumer<List> listConsumer2, Consumer<List> listConsumer3) {

        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }

            JFrame frame = new JFrame("Select work type");
            frame.setLayout(new BorderLayout());
            JPanel jPanel = new JPanel(new GridLayout(1, 1));
            frame.add(jPanel, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            CheckBoxGroup cg1 = new CheckBoxGroup(new String[]{"Turn on/OFF PC", "Reboot PC", "Setup PC", "Setup network", "Helping users", "Setup computer peripherals"}, "Daily");
            CheckBoxGroup cg2 = new CheckBoxGroup(new String[]{"Install OS", "Re-install OS", "Drawing up reports", "Data backup", "Connecting and removing users", "Installing new software"}, "Usual");
            CheckBoxGroup cg3 = new CheckBoxGroup(new String[]{"Onboarding new Admin", "Destroy PC", "Malware", "Fix the coffee machine", "Write a macro in Excel"}, "Unusual");
            jPanel.add(cg1);
            jPanel.add(cg2);
            jPanel.add(cg3);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            JPanel bottomPanel = new JPanel();
            JButton confirmButton = new JButton("Confirm selected");
            bottomPanel.add(confirmButton);
            bottomPanel.setLayout(new FlowLayout());
            frame.add(bottomPanel, BorderLayout.PAGE_END);
            confirmButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    infos1 = cg1.getCheckBox().stream().filter(AbstractButton::isSelected).map(AbstractButton::getText).collect(Collectors.toCollection(ArrayList::new));
                    infos2 = cg2.getCheckBox().stream().filter(AbstractButton::isSelected).map(AbstractButton::getText).collect(Collectors.toCollection(ArrayList::new));
                    infos3 = cg3.getCheckBox().stream().filter(AbstractButton::isSelected).map(AbstractButton::getText).collect(Collectors.toCollection(ArrayList::new));
                    listConsumer1.accept(infos1);
                    listConsumer2.accept(infos2);
                    listConsumer3.accept(infos3);
                    frame.dispose();
                    isShowing = false;
                }
            });

        });
    }

    public class CheckBoxGroup extends JPanel {
        private final JLabel all;
        private final ArrayList<JCheckBox> checkBoxes;


        public CheckBoxGroup(String[] options, String workType) {
            checkBoxes = new ArrayList<>(25);
            setLayout(new BorderLayout());
            JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
            all = new JLabel(workType);
            header.add(all);
            add(header, BorderLayout.NORTH);

            JPanel content = new ScrollablePane(new GridBagLayout());
            content.setBackground(UIManager.getColor("List.background"));
            if (options.length > 0) {

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                gbc.weightx = 1;
                for (int index = 0; index < (options.length - 1); index++) {
                    JCheckBox cb = new JCheckBox(options[index]);
                    cb.setOpaque(false);
                    checkBoxes.add(cb);
                    content.add(cb, gbc);
                }

                JCheckBox cb = new JCheckBox(options[options.length - 1]);
                cb.setOpaque(false);
                checkBoxes.add(cb);
                gbc.weighty = 1;
                content.add(cb, gbc);
            }

            add(new JScrollPane(content));
        }

        public ArrayList<JCheckBox> getCheckBox() {
            return checkBoxes;
        }

        public class ScrollablePane extends JPanel implements Scrollable {

            public ScrollablePane(LayoutManager layout) {
                super(layout);
            }

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(1000, 250);
            }

            @Override
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 32;
            }

            @Override
            public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 32;
            }

            @Override
            public boolean getScrollableTracksViewportWidth() {
                boolean track = false;
                Container parent = getParent();
                if (parent instanceof JViewport) {
                    JViewport vp = (JViewport) parent;
                    track = vp.getWidth() > getPreferredSize().width;
                }
                return track;
            }

            @Override
            public boolean getScrollableTracksViewportHeight() {
                boolean track = false;
                Container parent = getParent();
                if (parent instanceof JViewport) {
                    JViewport vp = (JViewport) parent;
                    track = vp.getHeight() > getPreferredSize().height;
                }
                return track;
            }
        }
    }
}