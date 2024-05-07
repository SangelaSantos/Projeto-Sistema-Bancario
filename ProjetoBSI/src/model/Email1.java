/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class Email1 {
    private static String emailFrom = "pupunhapay@gmail.com";
    private static String passwordFrom = "j t r n n w p u y a v m u z i q";
    private String emailTo;
    private String subject;
    private String content;
    private String attachmentPath;

    private Properties properties;
    private Session session;
    private MimeMessage message;

    // Método construtor para inicializar os campos de emailTo, subject, content e attachmentPath
    public Email1(String emailTo, String subject, String content, String attachmentPath) {
        this.emailTo = emailTo;
        this.subject = subject;
        this.content = content;
        this.attachmentPath = attachmentPath;
    }

    // Método para configurar e criar o email
    private void createEmail() {
        properties = new Properties();
        
        // Configurar propriedades do servidor SMTP
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", emailFrom);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");
        
        // Obter a sessão de email
        session = Session.getDefaultInstance(properties);
        
        try {
            // Criar o objeto MimeMessage
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject(subject);
            message.setText(content, "ISO-8859-1", "html");

            // Se houver um caminho de anexo especificado, adicione-o
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                // Criar um DataSource para o anexo
                DataSource source = new FileDataSource(attachmentPath);
                message.setDataHandler(new DataHandler(source));
                message.setFileName(source.getName());
            }
        } catch (AddressException ex) {
            Logger.getLogger(Email1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para enviar o email
    public void sendEmail() {
        createEmail(); // Chamar o método createEmail para configurar o email
        
        try {
            // Obter o transporte SMTP e enviar o email
            Transport transport = session.getTransport("smtp");
            transport.connect(emailFrom, passwordFrom);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            JOptionPane.showMessageDialog(null, "E-mail enviado");
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Email1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // Informações do e-mail
        String emailTo = "sangelaufopa@gmail.com";
        String subject = "ESTE E-MAIL É AUTOMÁTICO, NÃO RESPONDA";
        String content = "Detectamos uma atividade de login suspeita em sua conta. Para garantir a segurança dos seus dados, solicitamos que realize uma verificação adicional.";
        String attachmentPath = "C:\\Users\\Sammy\\OneDrive\\Imagens\\cachorrin.jpg"; // Substitua pelo caminho do seu arquivo de anexo

        // Criar objeto Email com anexo
        Email1 email = new Email1(emailTo, subject, content, attachmentPath);

        // Enviar o e-mail
        email.sendEmail();
    }
}

