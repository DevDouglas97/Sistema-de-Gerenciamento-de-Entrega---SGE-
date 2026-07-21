/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package PanelRotas;

import Model.Rotas;
import Model.Endereco;
import Controller.EnderecoController;
import Controller.RotasController;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Telas.Dashboard;
import javax.swing.SwingUtilities;

/**
 *
 * @author devdouglas
 */
public class VisualizarRota extends javax.swing.JPanel {

    private Rotas rotaAtual = null;
    private final RotasController rotasController = new RotasController();

    /**
     * Creates new form VisualizarRota
     */
    public VisualizarRota() {
        initComponents();
        configurarOpcoesStatus();
    }

    public VisualizarRota(Rotas rota) {
        initComponents();
        this.rotaAtual = rota;

        configurarOpcoesStatus();
        
        if (rota != null) {
            cbAtualizaStatusRota.setSelectedItem(rota.getStatus());

            // Estilização customizada da coluna de status
            tableVisualizaRotas.getColumnModel().getColumn(5).setCellRenderer(new RotasStatusRenderer());

            // Busca os dados complementares do endereço
            String descricaoEndereco = "Não Informada";
            String cidadeUfEndereco = "Não Informada";

            EnderecoController enderecoController = new EnderecoController();
            for (Endereco end : enderecoController.listar()) {
                if (end.getEndereco() != null && end.getEndereco().equalsIgnoreCase(rota.getEnderecoEntrega())) {
                    descricaoEndereco = end.getDescricao();
                    cidadeUfEndereco = end.getCidadeUf();
                    break;
                }
            }

            // Preenche a tabela com os dados da rota selecionada
            DefaultTableModel modelo = (DefaultTableModel) tableVisualizaRotas.getModel();
            modelo.setRowCount(0);

            Object[] linha = new Object[]{
                rota.getPedidoDisplay(),    // Coluna 0
                rota.getCliente(),          // Coluna 1
                descricaoEndereco,          // Coluna 2
                rota.getEnderecoEntrega(),  // Coluna 3
                cidadeUfEndereco,           // Coluna 4
                rota.getStatus()            // Coluna 5 (Status)
            };
            modelo.addRow(linha);
        }
    }

    private void configurarOpcoesStatus() {
        cbAtualizaStatusRota.setModel(new DefaultComboBoxModel<>(new String[] {
            "Pendente", "Em Rota", "Entregue", "Cancelado"
        }));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableVisualizaRotas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cbAtualizaStatusRota = new javax.swing.JComboBox<>();
        SalvarRotabtn = new javax.swing.JButton();
        Cancelarbtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(153, 153, 153));
        setMaximumSize(new java.awt.Dimension(804, 657));
        setMinimumSize(new java.awt.Dimension(804, 657));

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
        jLabel2.setText("GERENCIAMENTO DE ROTAS");

        tableVisualizaRotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pedido", "Cliente", "Descrição", "Endereço", "Cidade/UF", "Status"
            }
        ));
        jScrollPane1.setViewportView(tableVisualizaRotas);

        jLabel1.setText("Atualizar Status:");

        cbAtualizaStatusRota.addActionListener(this::cbAtualizaStatusRotaActionPerformed);

        SalvarRotabtn.setBackground(new java.awt.Color(0, 0, 0));
        SalvarRotabtn.setForeground(new java.awt.Color(255, 255, 255));
        SalvarRotabtn.setText("SALVAR");
        SalvarRotabtn.addActionListener(this::SalvarRotabtnActionPerformed);

        Cancelarbtn.setBackground(new java.awt.Color(0, 0, 0));
        Cancelarbtn.setForeground(new java.awt.Color(255, 255, 255));
        Cancelarbtn.setText("CANCELAR");
        Cancelarbtn.addActionListener(this::CancelarbtnActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(277, 277, 277))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SalvarRotabtn)
                                .addGap(108, 108, 108)
                                .addComponent(Cancelarbtn))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(cbAtualizaStatusRota, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(205, 205, 205))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbAtualizaStatusRota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SalvarRotabtn)
                    .addComponent(Cancelarbtn))
                .addContainerGap(241, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void cbAtualizaStatusRotaActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        // Atualiza dinamicamente a célula de status na JTable conforme o usuário altera a seleção no ComboBox
        if (tableVisualizaRotas.getRowCount() > 0 && cbAtualizaStatusRota.getSelectedItem() != null) {
            String novoStatus = cbAtualizaStatusRota.getSelectedItem().toString();
            tableVisualizaRotas.setValueAt(novoStatus, 0, 5);
        }
    }                                                     

    private void SalvarRotabtnActionPerformed(java.awt.event.ActionEvent evt) {                                               
        if (this.rotaAtual != null) {
            try {
                String novoStatus = cbAtualizaStatusRota.getSelectedItem().toString();
                this.rotaAtual.setStatus(novoStatus);

                // Persiste a alteração no MySQL via Controller
                rotasController.atualizar(this.rotaAtual);

                JOptionPane.showMessageDialog(this, "Status da rota atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Retorna para o painel principal de gerenciamento de rotas
                Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);
                if (dashboard != null) {
                    dashboard.abrirPainel(new GerenciamentoRotas());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar rota: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma rota selecionada para atualização.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }                                              

    private void CancelarbtnActionPerformed(java.awt.event.ActionEvent evt) {                                             
        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(this);
        if (dashboard != null) {
            dashboard.abrirPainel(new GerenciamentoRotas());
        }
    }                                            


    // Variables declaration - do not modify                     
    private javax.swing.JButton Cancelarbtn;
    private javax.swing.JButton SalvarRotabtn;
    private javax.swing.JComboBox<String> cbAtualizaStatusRota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableVisualizaRotas;
    // End of variables declaration                     
}