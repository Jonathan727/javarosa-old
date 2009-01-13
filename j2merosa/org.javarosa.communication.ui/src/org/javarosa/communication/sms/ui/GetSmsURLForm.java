package org.javarosa.communication.sms.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import org.javarosa.communication.sms.SmsTransportDestination;
import org.javarosa.core.api.IView;
import org.javarosa.core.services.transport.ITransportDestination;

public class GetSmsURLForm extends Form implements IView {

	public static final Command CMD_OK = new Command("OK", Command.OK, 1);
	public static final Command CMD_BACK = new Command("Back", Command.BACK, 1);
	
	TextField textField;
	
	public GetSmsURLForm(ITransportDestination preload) {
		super("Get New URL");
		// destinationUrl = shell.getAppProperty("destination-file");
		// TODO: put this back in when the property manager is
		// complete again
		textField = new TextField(
				"Please enter destination path + filename",
				((SmsTransportDestination)preload).getSmsAddress(), 140, TextField.ANY);

		append(textField);
		addCommand(CMD_OK);
		addCommand(CMD_BACK);
	}
	
	public String getDestination() {
		return textField.getString();
	}

	public Object getScreenObject() {
		return this;
	}

}