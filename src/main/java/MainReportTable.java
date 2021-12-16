import data.SelectWorkTypeFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class MainReportTable extends JFrame {
    public static final int ROW_HEIGHT = 35;
    public static final char COMMA = ',';

    private final JButton button = new JButton();

    private MainReportTable() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        setTitle("Отчет о проделанной работе");
        setSize(1500, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);
        String[] columns = {"День недели", "Ежедневные задания", "Обычные задания", "Внеплановые задания"};
        String[][] data = {
            {"Понедельник", ""},
            {"Вторник", ""},
            {"Среда", ""},
            {"Четверг", ""},
            {"Пятница", ""}
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
                    SelectWorkTypeFrame test = new SelectWorkTypeFrame(consumer1, consumer2, consumer3);
                }
            }
        });

        table.setModel(model);
        table.setRowHeight(ROW_HEIGHT);
        table.setCellEditor(new DayCellEditor());
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.lightGray);
        table.getColumn("День недели").setCellRenderer(renderer);
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
                dailyCount = getUsualCount(1, table);
                usualCount = getUsualCount(2, table);
                unusualCount = getUsualCount(3, table);
                HistogramPanel.createAndShowGUI(dailyCount, usualCount, unusualCount);
            }


            private int getUsualCount(int i2, JTable table) {
                int usualCount = 0;
                for (int i = 0; i < table.getModel().getRowCount(); i++) {
                    String usual = (String) table.getModel().getValueAt(i, i2);
                    if (usual != null) {
                        if (!usual.isEmpty()) {
                            usualCount++;
                            for (char ch : usual.toCharArray()) {
                                if (ch == COMMA) {
                                    if (usualCount == 0) {
                                        usualCount = 1;
                                    }
                                    usualCount++;
                                }
                            }
                        }
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
        Component tableTest = new MainReportTable();
        tableTest.setVisible(true);
    }

    public JButton getButton() {
        return button;
    }
}