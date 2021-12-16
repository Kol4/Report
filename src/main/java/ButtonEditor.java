import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import java.awt.Component;

class ButtonEditor extends DefaultCellEditor {
    private final MainReportTable simpleTableTest;

    ButtonEditor(MainReportTable simpleTableTest, JCheckBox checkBox) {
        super(checkBox);
        this.simpleTableTest = simpleTableTest;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return simpleTableTest.getButton();
    }
}
