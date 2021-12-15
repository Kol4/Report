import data.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class SimpleTableTest extends JFrame {
    public static final int ROW_HEIGHT = 35;
    public static final char COMMA = ',';

    private final JButton button = new JButton();

    private SimpleTableTest() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        setTitle("Report");
        setSize(1500, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);
        String[] columns = {"Day", "Daily work", "Usual work", "Unusual work"};
        String[][] data = {
            {"Monday", ""},
            {"Tuesday", ""},
            {"Wednesday", ""},
            {"Thursday", ""},
            {"Friday", ""}
        };
        TableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if ((col == 1) || (col == 2) || (col == 3)) {
                    Consumer<List> consumer1 = s -> model.setValueAt(s.stream().collect(Collectors.joining(", ")), table.rowAtPoint(e.getPoint()), 1);
                    Consumer<List> consumer2 = s -> model.setValueAt(s.stream().collect(Collectors.joining(", ")), table.rowAtPoint(e.getPoint()), 2);
                    Consumer<List> consumer3 = s -> model.setValueAt(s.stream().collect(Collectors.joining(", ")), table.rowAtPoint(e.getPoint()), 3);
                    Test test = new Test(consumer1, consumer2, consumer3);
                }
            }
        });

        table.setModel(model);
        table.setRowHeight(ROW_HEIGHT);
        table.setCellEditor(new DayCellEditor());
        Component scrollPane = new JScrollPane(table);
        topPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        JButton reportButton = new JButton("Report");
        reportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int dailyCount = 0;
                int usualCount = 0;
                int unusualCount = 0;
                try {
                    dailyCount = getUsualCount(1, table);
                    usualCount = getUsualCount(2, table);
                    unusualCount = getUsualCount(3, table);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Fill all the days in all the columns!");
                    return;
                }
                if ((dailyCount == 0) || (usualCount == 0) || (unusualCount == 0)) {
                    JOptionPane.showMessageDialog(null, "Fill all the days in all the columns!");
                } else {
                    new HistogramPanel().createAndShowGUI(dailyCount, usualCount, unusualCount);
                }
            }

            private int getUsualCount(int i2, JTable table) throws Exception {
                int usualCount = 0;
                for (int i = 0; i < table.getModel().getRowCount(); i++) {
                    String usual = (String) table.getModel().getValueAt(i, i2);
                    if (usual != null) {
                        if (usual.isEmpty()) {
                            throw new Exception();
                        }
                        usualCount++;
                        for (char ch : usual.toCharArray()) {
                            if (ch == COMMA) {
                                if (usualCount == 0) {
                                    usualCount = 1;
                                }
                                usualCount++;
                            }
                        }
                    } else {
                        throw new Exception();
                    }

                }
                return usualCount;
            }
        });
        bottomPanel.add(reportButton);
        bottomPanel.setLayout(new FlowLayout());
        topPanel.add(bottomPanel, BorderLayout.PAGE_END);
    }


    public static void main(String... args) {
        Component tableTest = new SimpleTableTest();
        tableTest.setVisible(true);
    }

    public JButton getButton() {
        return button;
    }
}