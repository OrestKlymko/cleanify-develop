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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy 'at' HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("🎉 Your Reservation is Confirmed!");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"Thank you for choosing CleanifyBee! 🐝 We're excited to help keep your space sparkling clean.\n\n" +
				"📅 **Appointment Details:**\n" +
				reservation.getDate().format(formatter) + "\n\n" +
				"If you have any questions or need to make changes, feel free to reply to this email or call us anytime.\n\n" +
				"Warm regards,\n" +
				"The CleanifyBee Team 💛");

		mailSender.send(mailMessage);
	}

	public void sendCancellationReservationMail(String to, User user, Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy 'at' HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Your Reservation has been Cancelled");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"We're sorry to inform you that your reservation on " + reservation.getDate().format(formatter) + " has been cancelled. If this was a mistake or you wish to reschedule, please let us know—we're here to help!\n\n" +
				"Wishing you all the best,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void sendMessageNewAppointment(String to, User user, Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy 'at' HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("✨ New Cleaning Appointment Assigned");
		mailMessage.setText("Hello,\n\n" +
				"Great news! You've been assigned a new cleaning appointment.\n\n" +
				"👤 **Client:** " + user.getFirstName() + "\n" +
				"📅 **Date & Time:** " + reservation.getDate().format(formatter) + "\n\n" +
				"Please ensure you're prepared to provide exceptional service. If you have any questions, feel free to reach out to the team.\n\n" +
				"Best regards,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void sendMessageChangeTimeOfReservation(String to, User user, Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy 'at' HH:mm");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("🔄 Your Reservation has been Updated");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"We've successfully updated the time of your reservation.\n\n" +
				"📅 **New Appointment Details:**\n" +
				reservation.getDate().format(formatter) + "\n\n" +
				"If you have any questions or need further assistance, please don't hesitate to contact us.\n\n" +
				"Warm regards,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void successDeleteProfileUser(String to, User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Your Profile has been Deleted");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"We're sorry to see you go. Your profile has been successfully deleted as per your request.\n\n" +
				"If there's anything we could have done better or if you have feedback, we'd love to hear from you.\n\n" +
				"Wishing you all the best,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void sendConfirmationCleaningCommercialBuilding(String to,User user){
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Thank you for choosing CleanifyBee! | Confirmation");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"Thank you for choosing CleanifyBee! 🐝 We're excited to help keep your space sparkling clean.\n\n" +
				"If you have any questions or need to make changes, feel free to reply to this email or call us anytime.\n\n" +
				"Warm regards,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void sendConfirmationOfCompanyCleaningRequestToCall(String to, User user){
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("We will call you soon! | CleanifyBee");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"Thank you for choosing CleanifyBee! 🐝 We will call you soon to confirm the details of your commercial cleaning request.\n\n" +
				"If you have any questions or need to make changes, feel free to reply to this email or call us anytime.\n\n" +
				"Warm regards,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void changeTimeOfCommercialCleaning(String to, User user){
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Your Commercial Cleaning Date has been Changed");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"We're sorry to inform you that your commercial cleaning date has been changed. If this was a mistake or you wish to reschedule, please let us know—we're here to help!\n\n" +
				"Wishing you all the best,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}

	public void cancelCommercialCleaning(String to, User user){
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Your Commercial Cleaning has been Cancelled");
		mailMessage.setText("Hello " + user.getFirstName() + ",\n\n" +
				"We're sorry to inform you that your commercial cleaning has been cancelled. If this was a mistake or you wish to reschedule, please let us know—we're here to help!\n\n" +
				"Wishing you all the best,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}


	public void sendConfirmationOfTodayRegularCleaning(String to,String firstName){
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("We are very excited to clean your space today!");
		mailMessage.setText("Hello " + firstName + ",\n\n" +
				"Your regular cleaning is scheduled for today. Our team will be arriving shortly to make your space sparkle.\n\n" +
				"If you have any questions or need to make changes, feel free to reply to this email or call us anytime.\n\n" +
				"Warm regards,\n" +
				"The CleanifyBee Team 💛");
		mailSender.send(mailMessage);
	}
}
