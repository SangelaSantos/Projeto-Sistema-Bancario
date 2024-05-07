package view;

import dao.ClienteDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Cliente;

public class ListaClientes extends JFrame {

    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private JButton botaoSalvar;
    private JButton botaoDeletar; // Novo botão para deletar clientes

    private ClienteDAO clienteDAO;

    public ListaClientes(List<Cliente> clientes) {
        clienteDAO = new ClienteDAO();

        setTitle("PUPUNHA PAY");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializar a tabela
        modeloTabela = new DefaultTableModel();
        tabelaClientes = new JTable(modeloTabela);
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Email");
        modeloTabela.addColumn("Sexo");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("Endereço");
        modeloTabela.addColumn("CPF");
        modeloTabela.addColumn("Data de Nascimento");

        // Preencher a tabela com os clientes fornecidos
        for (Cliente cliente : clientes) {
            modeloTabela.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getSexo(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getCpf(),
                cliente.getDatanascimento()
            });
        }

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        add(scrollPane, BorderLayout.CENTER);
        
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adicionar botão "Salvar"
        botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener(e -> {
            int selectedRow = tabelaClientes.getSelectedRow();
            if (selectedRow != -1) {
                Cliente cliente = clientes.get(selectedRow);
                cliente.setNome((String) modeloTabela.getValueAt(selectedRow, 1));
                cliente.setEmail((String) modeloTabela.getValueAt(selectedRow, 2));
                cliente.setSexo((String) modeloTabela.getValueAt(selectedRow, 3));
                cliente.setTelefone((String) modeloTabela.getValueAt(selectedRow, 4));
                cliente.setEndereco((String) modeloTabela.getValueAt(selectedRow, 5));
                cliente.setCpf((String) modeloTabela.getValueAt(selectedRow, 6));
                cliente.setDatanascimento((String) modeloTabela.getValueAt(selectedRow, 7));
                
                clienteDAO.saveOrUpdate(cliente);
                JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!");
            }
        });
        
        // Adicionar botão "Deletar"
        botaoDeletar = new JButton("Deletar");
        botaoDeletar.addActionListener(e -> {
            int selectedRow = tabelaClientes.getSelectedRow();
            if (selectedRow != -1) {
                int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este cliente?", "Confirmação de Deleção", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    Cliente cliente = clientes.get(selectedRow);
                    clienteDAO.delete(cliente);
                    modeloTabela.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso!");
                }
            }
        });
        
        JPanel panelBotoes = new JPanel();
        panelBotoes.add(botaoSalvar);
        panelBotoes.add(botaoDeletar);
        
        add(panelBotoes, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        List<Cliente> clientes = new ClienteDAO().getAll();
        SwingUtilities.invokeLater(() -> {
            new ListaClientes(clientes);
        });
    }
}
