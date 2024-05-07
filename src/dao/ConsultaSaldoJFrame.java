package dao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import utils.Conexao;

public class ConsultaSaldoJFrame extends JFrame {
    private JFormattedTextField cpfField;
    private JTextField depositoField; // Campo para digitar o valor do depósito
    private JButton consultarSaldoButton;
    private JButton depositarButton;
    private JButton sacarButton;
    
    private Connection connection = Conexao.getConexao();

    public ConsultaSaldoJFrame() {
        // Configurações do JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200); // Aumentei um pouco o tamanho para acomodar o novo campo
        setTitle("Consulta de Saldo");

        // Layout do JFrame
        JPanel panel = new JPanel();
        cpfField = new JFormattedTextField();
        try {
            cpfField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        depositoField = new JTextField(10); // Definindo o tamanho do campo para 10 caracteres
        consultarSaldoButton = new JButton("Consultar Saldo");
        consultarSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarSaldoButtonActionPerformed(e);
            }
        });
        depositarButton = new JButton("Depositar");
        depositarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositar();
            }
        });
        sacarButton = new JButton("Sacar");
        sacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sacar();
            }
        });
        panel.add(new JLabel("CPF do Cliente:"));
        panel.add(cpfField);
        panel.add(new JLabel("Valor do Depósito:")); // Label para indicar o campo de depósito
        panel.add(depositoField); // Adicionando o campo de depósito
        panel.add(consultarSaldoButton);
        panel.add(depositarButton);
        panel.add(sacarButton);
        add(panel);
    }

    // Método para consultar o saldo pelo CPF do cliente
    private void consultarSaldoButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        String cpfCliente = cpfField.getText(); // Obtém o CPF do campo cpfField
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

    private void depositar() {
        try {
            double valorDeposito = Double.parseDouble(depositoField.getText()); // Obtendo o valor do depósito do campo
            String cpfCliente = cpfField.getText();
            
            // Atualizar saldo na tabela Clientes
            String sql = "UPDATE Clientes SET saldo = saldo + ? WHERE cpf = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, valorDeposito);
            ps.setString(2, cpfCliente);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Depósito realizado com sucesso");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao realizar depósito: CPF não encontrado");
            }
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao realizar depósito");
        }
    }

    private void sacar() {
        // Implementação do método sacar
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
             public void run() {
            ConsultaSaldoJFrame frame = new ConsultaSaldoJFrame();
            frame.setLocationRelativeTo(null); // Centraliza o JFrame na tela
            frame.setVisible(true);
        }
        });
    }
}
