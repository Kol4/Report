import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class HistogramPanel extends JPanel {

    private final JPanel barPanel;
    private final JPanel labelPanel;

    private final List<Bar> bars = new ArrayList<Bar>();

    private HistogramPanel() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());

        int barGap = 10;
        barPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder(compound);

        labelPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        labelPanel.setBorder(new EmptyBorder(5, 10, 0, 10));

        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
    }

    private void addHistogramColumn(String label, int value, Color color) {
        Bar bar = new Bar(label, value, color);
        bars.add(bar);
    }

    private void layoutHistogram() {
        barPanel.removeAll();
        labelPanel.removeAll();

        int maxValue = 0;

        for (Bar bar : bars) {
            maxValue = Math.max(maxValue, bar.getValue());
        }

        for (Bar bar : bars) {
            JLabel label = new JLabel(bar.getValue() + "");
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.TOP);
            label.setVerticalAlignment(SwingConstants.BOTTOM);
            int histogramHeight = 250;
            int barHeight;
            if (bar.getValue() == 0){
                barHeight = 0;
            }else {
                barHeight = (bar.getValue() * histogramHeight) / maxValue;
            }
            int barWidth = 75;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon(icon);
            barPanel.add(label);

            JLabel barLabel = new JLabel(bar.getLabel());
            barLabel.setHorizontalAlignment(SwingConstants.CENTER);
            labelPanel.add(barLabel);
        }
    }

    private class Bar {
        private final String label;
        private final int value;
        private final Color color;

        private Bar(String label, int value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        private String getLabel() {
            return label;
        }

        private int getValue() {
            return value;
        }

        private Color getColor() {
            return color;
        }
    }

    private static class ColorIcon implements Icon {
        private final int shadow = 3;

        private final Color color;
        private final int width;
        private final int height;

        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, height);
            g.setColor(Color.GRAY);
            g.fillRect((x + width) - shadow, y + shadow, shadow, height - shadow);
        }
    }

    static void createAndShowGUI(int daily, int usual, int unusual) {
        HistogramPanel panel = new HistogramPanel();
        panel.addHistogramColumn("Daily", daily, Color.RED);
        panel.addHistogramColumn("Usual", usual, Color.YELLOW);
        panel.addHistogramColumn("Unusual", unusual, Color.CYAN);
        panel.layoutHistogram();

        JFrame frame = new JFrame("Histogram Panel");
        frame.add(panel);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }
}