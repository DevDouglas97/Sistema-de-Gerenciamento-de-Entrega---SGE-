/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelRotas;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RotasStatusRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null) {
            String status = value.toString().trim();
            
            if ("Pendente".equalsIgnoreCase(status)) {
                c.setBackground(new Color(220, 53, 69)); // Vermelho
                c.setForeground(Color.WHITE);
            } else if ("Em Rota".equalsIgnoreCase(status)) {
                c.setBackground(new Color(0, 102, 204)); // Azul
                c.setForeground(Color.WHITE);
            } else if ("Entregue".equalsIgnoreCase(status)) {
                c.setBackground(new Color(40, 167, 69)); // Verde
                c.setForeground(Color.WHITE);
            } else if ("Cancelado".equalsIgnoreCase(status)) {
                c.setBackground(Color.DARK_GRAY);
                c.setForeground(Color.WHITE);
            } else {
                restaurarCores(table, isSelected, c);
            }
        } else {
            restaurarCores(table, isSelected, c);
        }
        return c;
    }

    private void restaurarCores(JTable table, boolean isSelected, Component c) {
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }
    }
}
