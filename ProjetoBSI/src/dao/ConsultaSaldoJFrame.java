package dao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import javax.swing.*;
import utils.Conexao;

public class ConsultaSaldoJFrame extends JFrame {

    private Connection connection = Conexao.getConexao();
    private JFormattedTextField campoCpf;

    public ConsultaSaldoJFrame() {
        // Configurações do JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Consulta de Saldo");
        setSize(1000, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 102, 0)); // Define a cor de fundo do painel

        // Adicionando campo CPF formatado
        try {
            campoCpf = new JFormattedTextField(new javax.swing.text.MaskFormatter("###.###.###-##"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        campoCpf.setBounds(20, 20, 200, 30);
        panel.add(campoCpf);

        // Adicionando botão "Consultar Saldo"
        JButton consultarSaldoButton = new JButton("Consultar Saldo");
        consultarSaldoButton.setBounds(20, 70, 150, 30);
        consultarSaldoButton.addActionListener(this::consultarSaldoButtonActionPerformed);
        panel.add(consultarSaldoButton);

        // Adicionando o painel ao frame
        add(panel);
    }

    // Método para consultar o saldo pelo CPF do cliente
    private void consultarSaldoButtonActionPerformed(ActionEvent evt) {
        String cpfCliente = campoCpf.getText(); // Obtém o CPF do campo cpfField
        double saldoCliente = consultarSaldoPorCPF(cpfCliente); // Chama o método para consultar o saldo pelo CPF
        if (saldoCliente != -1) {
            JOptionPane.showMessageDialog(this, "Saldo do cliente: " + saldoCliente);
        } else {
            JOptionPane.showMessageDialog(this, "CPF não encontrado ou saldo não disponível");
        }
    }

    // Método para consultar o saldo pelo CPF do cliente
    private double consultarSaldoPorCPF(String cpf) {
        double saldo = -1; // Inicializa o saldo como -1 para indicar que o CPF não foi encontrado ou saldo não disponível
        try {
            String sql = "SELECT saldo FROM Clientes WHERE cpf = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return saldo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConsultaSaldoJFrame frame = new ConsultaSaldoJFrame();
            frame.setVisible(true);
        });
    }
}
