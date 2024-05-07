package model;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class Email {
    private static String emailFrom = "pupunhapay@gmail.com";
    private static String passwordFrom = "j t r n n w p u y a v m u z i q";
    private String emailTo;
    private String subject;
    private String content;

    private Properties mProperties;
    private Session mSession;
    private MimeMessage mCorreo;
    
    // Método construtor para inicializar os campos de emailTo, subject e content
    public Email(String emailTo, String subject, String content) {
        this.emailTo = emailTo;
        this.subject = subject;
        this.content = content;
    }
    
    private void createEmail() {
        // Inicializar as propriedades
        mProperties = new Properties();
        
        // Configurar propriedades do servidor SMTP
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.user", emailFrom);
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");
        
        // Obter a sessão de email
        mSession = Session.getDefaultInstance(mProperties);
        
        
        try {
            // Criar o objeto MimeMessage
            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(subject);
            mCorreo.setText(content, "ISO-8859-1", "html");
        } catch (AddressException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendEmail() {
        createEmail(); // Chamar o método createEmail para configurar o email
        
        try {
            // Obter o transporte SMTP e enviar o email
            Transport mTransport = mSession.getTransport("smtp");
            mTransport.connect(emailFrom, passwordFrom);
            mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransport.close();
            
            JOptionPane.showMessageDialog(null, "E-mail enviado");
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
    // Informações do e-mail
    String emailTo = "sangelaufopa@gmail.com";
    String subject = "ESTE E-MAIL É AUTOMÁTICO, NÃO RESPONDA";
    String content = "Detectamos uma atividade de login suspeita em sua conta. Para garantir a segurança dos seus dados, solicitamos que realize uma verificação adicional.";

    // Criar objeto Email
    Email email = new Email(emailTo, subject, content);

    // Enviar o e-mail
    email.sendEmail();
}

    
}
