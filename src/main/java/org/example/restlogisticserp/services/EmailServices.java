package org.example.restlogisticserp.services;

import org.example.restlogisticserp.models.Email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Properties;
@Path("/email") // Base path for email-related endpoints
public class EmailServices {

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendEmail(Email emailPayload) {
        System.out.println("Received email payload: " + emailPayload);

        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); // Enable authentication
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Example for Gmail
        properties.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("nomalfiever@gmail.com", "jooc epsk nxbp yqwg");
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nomalfiever@gmail.com")); // Your Gmail address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailPayload.getToEmail()));
            message.setSubject(emailPayload.getSubject());
            message.setText(emailPayload.getBody());

            // Send the email
            Transport.send(message);
            return Response.ok("Email sent successfully!").build();

        } catch (MessagingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to send email: " + e.getMessage()).build();
        }
    }
}