package org.javarosa.clforms;

import javax.microedition.lcdui.Display;

import org.javarosa.clforms.api.Prompt;
import org.javarosa.clforms.api.ResponseEvent;
import org.javarosa.clforms.view.IPrompter;

public class Scripter implements IPrompter {

	private Controller controller;
	
	public void registerController(Controller controller) {
		this.controller = controller;
	}

	public void showPrompt(Prompt prompt) {
		prompt.setValue(getValueFromScript());
		System.out.println(prompt.getLongText());
		controller.processEvent(new ResponseEvent(ResponseEvent.NEXT, -1));
	}


	public String getValueFromScript() {
		return "";
	}

	public void showPrompt(Prompt prompt, int screenIndex, int totalScreens) {
		// TODO Auto-generated method stub
		
	}
	
	public void showError(String title, String message, Display display)
	{
		throw new RuntimeException("Method not supported yet.");
	}
}
