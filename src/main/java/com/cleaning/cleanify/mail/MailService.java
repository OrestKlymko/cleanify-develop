package com.cleaning.cleanify.mail;


import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.reservation.model.Reservation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class MailService {

	private final JavaMailSender mailSender;

	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendConfirmationReservationMail(String to, User user, Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Your reservation has been confirmed");
		mailMessage.setText("Dear " + user.getFirstName() + ",\n\n" +
				"Your reservation has been confirmed. We are looking forward to seeing you on " + reservation.getDate().format(formatter) + ".\n\n" +
				"Best regards,\n" +
				"Cleanify team");
	}

	public void sendCancellationReservationMail(String to, User user, Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Your reservation has been cancelled");
		mailMessage.setText("Dear " + user.getFirstName() + ",\n\n" +
				"Your reservation on " + reservation.getDate().format(formatter) + " has been cancelled.\n\n" +
				"Best regards,\n" +
				"Cleanify team");
	}

	public void sendMessageNewAppointment(String to, User user, Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("New appointment");
		mailMessage.setText("Dear cleaner" + ",\n\n" +
				"You have a new appointment for "+user.getFirstName()+ "+on " + reservation.getDate().format(formatter) + ".\n\n" +
				"Best regards,\n" +
				"Cleanify team");
	}


}
