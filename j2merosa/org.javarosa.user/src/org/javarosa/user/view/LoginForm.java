

package org.javarosa.user.view;

import java.io.IOException;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import org.javarosa.core.api.IActivity;
import org.javarosa.core.api.IView;
import org.javarosa.core.util.externalizable.DeserializationException;
import org.javarosa.user.model.User;
import org.javarosa.user.storage.UserRMSUtility;

public class LoginForm extends Form implements IView
{
	   public final Command CMD_CANCEL_LOGIN = new Command("EXIT",Command.SCREEN, 1);
	   public Command loginButtonCommand = new Command("Login", Command.ITEM, 1);

	   private TextField userName;
	   private TextField password;
	   private UserRMSUtility userRMS;
	   private User loggedInUser;
	   public StringItem loginButton;

	   ///these 4 variable is for a rememberMe option that could join the login menu
	   private String rememberMe;
	   private static final String DO_REMEMBER_ME = "DoRememberMe";
       private static final String DONT_REMEMBER_ME = "DontRememberMe";
	   IActivity parent;

	  public LoginForm(IActivity loginActivity, String title) {
		   super(title);
		   parent = loginActivity;

		   userRMS = new UserRMSUtility("LoginMem");

	      if (userRMS.getNumberOfRecords() == 0){

	    	  String usernameVAR= (String)loginActivity.getActivityContext().getElement("username");
	    	  String passwordVAR= (String)loginActivity.getActivityContext().getElement("password");

	    	  loggedInUser = new User (usernameVAR,passwordVAR,User.ADMINUSER);
	    	  userRMS.writeToRMS(loggedInUser);
	      }

		   //get first username from RMS
		   User tempuser = new User();
		   tempuser.setUsername("");

		   if (userRMS.getNumberOfRecords() != 0){
			   	try {
					userRMS.retrieveFromRMS(userRMS.getNextRecordID()-1, tempuser);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DeserializationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		  userName = new TextField("Username:", tempuser.getUsername(), 50, TextField.ANY);
	      password = new TextField("Password:", "", 10, TextField.PASSWORD);
	      this.addCommand(CMD_CANCEL_LOGIN);

	      this.append(userName);
	      append(password);

	      loginButton = new StringItem(null,"LOGIN",Item.BUTTON);
	      this.append(loginButton);
	      loginButton.setDefaultCommand(loginButtonCommand);     // add Command to Item.
	   }


	   public boolean validateUser() {

		   boolean validLogin = false;
		   ///find user in RMS:
		   User discoveredUser = new User();
		   String usernameStr = userName.getString().trim();
		   int index = 1;

		   while (index <= userRMS.getNumberOfRecords() )
		   {
			   try
			   {
				   userRMS.retrieveFromRMS(index, discoveredUser);
			   }
			   catch (IOException ioe) {
				   System.out.println(ioe);
			   }
			   catch (DeserializationException uee) {
				   System.out.println(uee);
			   }
			   if (discoveredUser.getUsername().equalsIgnoreCase(usernameStr))
				   break;

			   index++;
		   }

		   if (discoveredUser.getUsername().equalsIgnoreCase(usernameStr))
			   if (discoveredUser.getPassword().equals(password.getString()))
			   {
				   System.out.println("login valid");
				   validLogin = true;
				   setLoggedInUser(discoveredUser);
			   }

	     return validLogin;

	   }

	   public String getLoggedInUserType() {


		   ///find user in RMS:
		   User discoveredUser = new User();
		   String usernameStr = userName.getString();
		   int index = 1;

		   while (index <= userRMS.getNumberOfRecords() )
		   {
			   try
			   {
				   userRMS.retrieveFromRMS(index, discoveredUser);
			   }
			   catch (IOException ioe) {
				   System.out.println(ioe);
			   }
			   catch (DeserializationException uee) {
				   System.out.println(uee);
			   }
			   if (discoveredUser.getUsername().equalsIgnoreCase(usernameStr))
				   break;

			   index++;
		   }

		   if (discoveredUser.getUsername().equalsIgnoreCase(usernameStr))
			   {
			   System.out.println("found a user: "+discoveredUser.getUsername()+" with type: "+discoveredUser.getUserType());
			   return discoveredUser.getUserType();
			   }

	     return "userNotFound";
	   }

	   //*this method returns an Alert that states that the login was unsuccessful. @see validateUser()*/
	   public javax.microedition.lcdui.Alert tryAgain() {
	     javax.microedition.lcdui.Alert error = new javax.microedition.lcdui.Alert("Login Incorrect", "Please try again", null, AlertType.ERROR);

	     System.out.println("error alert returned");
	     return error;
	   }

	   public javax.microedition.lcdui.Alert successfulLogin() {
		     javax.microedition.lcdui.Alert success = new javax.microedition.lcdui.Alert("Login Successful", "Loading your profile", null, AlertType.CONFIRMATION);
		     return success;
		   }

	public User getLoggedInUser() {
		return loggedInUser;
	}

	private void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	public Object getScreenObject() {
		return this;
	}
}