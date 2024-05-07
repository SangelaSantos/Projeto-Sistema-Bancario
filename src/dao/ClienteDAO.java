package dao;

/*
 * @Sangela
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Cliente;
import utils.Conexao;


public class ClienteDAO {

    private Connection connection = Conexao.getConexao();

    public void save(Cliente cliente) {
    try {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO clientes (nome, sexo, telefone, endereco, email, cpf, datanascimento, senha, saldo) VALUES (?,?,?,?,?,?,?,?,?)");
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getSexo());
        ps.setString(3, cliente.getTelefone());
        ps.setString(4, cliente.getEndereco());
        ps.setString(5, cliente.getEmail());
        ps.setString(6, cliente.getCpf());
        ps.setString(7, cliente.getDatanascimento());
        ps.setString(8, cliente.getSenha());
        ps.setDouble(9, cliente.getSaldo()); // Define o saldo
        ps.execute();
        JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
    } catch (SQLException ex) {
        Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    public void update(Cliente cliente) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE clientes SET  nome=?,sexo=?, telefone=?, endereco=?, email=?, cpf=?, datanascimento=?, senha=? WHERE id=?");
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getSexo());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getEndereco());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getCpf());
            ps.setString(7, cliente.getDatanascimento());
            ps.setString(8, cliente.getSenha());
            ps.setInt(9, cliente.getId());
            ps.executeUpdate();
            
    } catch (SQLException ex) {
        Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    public void saveOrUpdate(Cliente cliente) {
        if (cliente.getId() == 0) {
            save(cliente);
        } else {
            update(cliente);
        }
    }

    public void delete(Cliente cliente) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM clientes WHERE id=?");
            ps.setInt(1, cliente.getId());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso!");
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public PreparedStatement criarConsulta(String cpf, String senha) throws SQLException {
        String consulta = "SELECT COUNT(*) FROM clientes WHERE cpf = ? AND senha = ?";
        PreparedStatement stmt = connection.prepareStatement(consulta);
        stmt.setString(1, cpf);
        stmt.setString(2, senha);
        return stmt;
}
    
    public boolean verificarLogin(String cpf, String senha) {
        try {
            PreparedStatement stmt = criarConsulta(cpf, senha);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                int totalRegistros = resultado.getInt(1);
                return totalRegistros > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
}
    public double consultarSaldoPorCPF(String cpf) {
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
    
    public void depositar(String cpf, double valorDeposito) {
        try {
            String sql = "UPDATE Clientes SET saldo = saldo + ? WHERE cpf = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, valorDeposito);
            ps.setString(2, cpf);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Depósito realizado com sucesso");
            } else {
                System.out.println("Erro ao realizar depósito: CPF não encontrado");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao realizar depósito");
        }
    }



    public List<Cliente> getAll() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clientes");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSexo(rs.getString("sexo"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setDatanascimento(rs.getString("datanascimento"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return clientes;
        }
        return clientes;
    }

    
    
}
