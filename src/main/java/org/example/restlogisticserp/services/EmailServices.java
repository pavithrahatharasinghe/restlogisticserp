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

    @POST
    @Path("/reset-password") // Endpoint for sending reset password emails
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendResetPasswordEmail(Email emailPayload) {
        System.out.println("Received email payload for reset password: " + emailPayload);

        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
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
            // Hardcoded reset password URL
            String resetLink = "https://yourapp.com/reset-password";

            // Generate email content dynamically for reset password without first and last name
            String emailContent = getResetPasswordEmailTemplate(resetLink);

            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nomalfiever@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailPayload.getToEmail()));
            message.setSubject(emailPayload.getSubject());

            // Set email content as HTML
            message.setContent(emailContent, "text/html; charset=utf-8");

            // Send the email
            Transport.send(message);
            return Response.ok("Reset password email sent successfully!").build();

        } catch (MessagingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to send email: " + e.getMessage()).build();
        }
    }

    // Function to generate the email content without first and last names
    private String getResetPasswordEmailTemplate(String resetLink) {
        // HTML template without first and last name placeholders
        return """
               <!DOCTYPE html>
                       <html lang="en">
                       <head>
                           <meta charset="UTF-8">
                           <meta name="viewport" content="width=device-width, initial-scale=1.0">
                           <title>Reset Your Password - Shipping ERP</title>
                           <style>
                               body {
                                   font-family: 'Arial', sans-serif;
                                   background-color: #dcdcdc;
                                   padding: 20px;
                                   margin: 0;
                               }
                               .email-container {
                                   max-width: 1000px;
                                   margin: 0 auto;
                                   background-color: #ffffff;
                                   padding: 30px;
                                   border-radius: 10px;
                                   box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                               }
                               .header {
                                   padding: 20px;
                                   border-radius: 10px 10px 0 0;
                                   text-align: center;
                                   color: white;
                               }
                               .header h1 {
                                   margin: 0;
                                   font-size: 28px;
                                   letter-spacing: 1px;
                                   font-weight: 600;
                               }
                               .content {
                                   padding: 20px 30px;
                                   color: #333333;
                               }

                               .reset-button {
                                   display: inline-block;
                                   background-color: #003366; /* Dark blue for button */
                                   color: white;
                                   padding: 12px 25px;
                                   font-size: 16px;
                                   border-radius: 5px;
                                   text-decoration: none;
                                   margin: 20px 0;
                                   text-align: center;
                               }
                               .footer {
                                   font-size: 12px;
                                   color: #999999;
                                   text-align: center;
                                   margin-top: 20px;
                               }
                               .footer a {
                                   color: #003366; /* Dark blue for footer links */
                                   text-decoration: none;
                               }
                               .footer hr {
                                   border: none;
                                   border-top: 1px solid #eeeeee;
                                   margin: 20px 0;
                               }
                           </style>
                       </head>
                       <body>
                       <div class="email-container">
                           <div class="header">
                               <h1>Shipping ERP</h1>
                           </div>
                           <div class="content">
                               <h2>Dear Valued Customer,</h2>
                               <p>You recently requested to reset your password. Click the button below to reset your password:</p>
                               <div style="text-align: center;">
                                   <a href="%s" class="reset-button">Reset Password</a>
                               </div>
                               <p>If you did not request a password reset, please ignore this email. Your account remains secure.</p>
                               <p>Thank you for choosing Shipping ERP,<br>The Shipping ERP Team</p>
                           </div>
                           <div class="footer">
                               <hr>
                               <p>If you're having trouble clicking the button, copy and paste the following link into your browser:</p>
                               <p><a href="%s">%s</a></p>
                           </div>
                       </div>
                       </body>
                       </html>
                
                
                
        """.formatted(resetLink, resetLink, resetLink);
    }

}
