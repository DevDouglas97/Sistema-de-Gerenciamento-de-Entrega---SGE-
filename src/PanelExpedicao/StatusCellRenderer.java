/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelExpedicao;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null) {
            String status = value.toString().trim();
            
            if ("Aguardando".equalsIgnoreCase(status)) {
                c.setBackground(new Color(0, 102, 204)); // Azul
                c.setForeground(Color.WHITE);
            } else if ("Em Separação".equalsIgnoreCase(status)) {
                c.setBackground(new Color(255, 204, 0)); // Amarelo limpo/visível
                c.setForeground(Color.BLACK); // Texto preto para dar contraste no amarelo
            } else if ("Separado".equalsIgnoreCase(status)) {
                c.setBackground(new Color(40, 167, 69)); // Verde institucional/sucesso
                c.setForeground(Color.WHITE);
            } else {
                restaurarCoresPadrao(table, isSelected, c);
            }
        } else {
            restaurarCoresPadrao(table, isSelected, c);
        }
        return c;
    }

    private void restaurarCoresPadrao(JTable table, boolean isSelected, Component c) {
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }
    }
}
