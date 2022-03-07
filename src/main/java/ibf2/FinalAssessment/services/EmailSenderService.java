package ibf2.FinalAssessment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import static ibf2.FinalAssessment.config.Constants.*;

@Service(EMAIL_SERVICE)
public class EmailSenderService {
  @Autowired
  private JavaMailSender mailSender;

  public void sendEmail(String toEmail, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("eugenengboxiang@gmail.com");
    message.setTo(toEmail);
    message.setText(body);
    message.setSubject(subject);
    mailSender.send(message);
    System.out.println("Mail Sent successfully to " + toEmail);

  }

}
