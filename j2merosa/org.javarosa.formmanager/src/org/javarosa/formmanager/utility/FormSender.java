package org.javarosa.formmanager.utility;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.javarosa.core.JavaRosaServiceProvider;
import org.javarosa.core.api.IActivity;
import org.javarosa.core.api.IShell;
import org.javarosa.core.api.IView;
import org.javarosa.core.model.instance.DataModelTree;
import org.javarosa.core.model.utils.IDataModelSerializingVisitor;
import org.javarosa.core.services.ITransportManager;
import org.javarosa.core.services.transport.IDataPayload;
import org.javarosa.core.services.transport.ITransportDestination;
import org.javarosa.formmanager.activity.FormTransportActivity;
import org.javarosa.formmanager.view.transport.FormTransportSubmitStatusScreen;
import org.javarosa.formmanager.view.transport.MultiSubmitStatusScreen;

/**
 * Managing sending forms, both single forms, and multiple forms together
 * 
 */
public class FormSender {

	/**
	 * 
	 */
	private IShell shell;
	/**
	 * 
	 */
	private IActivity activity;

	/**
	 * true if many forms will be sent at once
	 * 
	 * TODO eliminate - look at vector to determine single or multiple
	 */
	private boolean multiple;

	/**
	 * The data to be sent when multiple = false
	 * 
	 * TODO: eliminate in favour of using Vector for both multiple and single
	 */
	private DataModelTree data;

	/**
	 * The data to be sent when multiple = true
	 */
	private Vector multiData;

	/**
	 * 
	 */
	private IDataModelSerializingVisitor serializer;
	/**
	 * this is also used as a flag TODO: eliminate its usage as a flag
	 */
	private ITransportDestination destination;

	/**
	 * @param shell
	 * @param activity
	 */
	public FormSender(IShell shell, IActivity activity) {
		this.shell = shell;
		this.activity = activity;

	}

	public void sendData() throws IOException {
		// #debug debug
		System.out.println("Sending data .. multiple=" + multiple);

		if (this.multiple) {

			sendMultiData();

		} else {

			sendSingle();
		}
	}

	/**
	 * @param mainMenu
	 * @throws IOException
	 */
	private void sendSingle() throws IOException {

		if (this.data == null)
			throw new RuntimeException(
					"null data when trying to send single data");

		IDataPayload payload = this.serializer
				.createSerializedPayload(this.data);

		// #debug debug
		System.out.println("Sending single datum, serialized id="
				+ this.data.getId() + " length=" + payload.getLength());

		send(payload, this.data.getId());
		feedbackStatus();
	}

	/**
	 * 
	 */
	private void feedbackStatus() {
		FormTransportSubmitStatusScreen statusScreen = ((FormTransportActivity) this.activity)
				.getView().getSubmitStatusScreen();
		statusScreen.reinit(this.data.getId());
		this.shell.setDisplay((IActivity) this.activity, statusScreen);
	}

	/**
	 * @throws IOException
	 */
	private void sendMultiData() throws IOException {

		MultiSubmitStatusScreen s = ((FormTransportActivity) this.activity)
				.getView().getMultiSubmitStatusScreen();

		boolean noData = (this.multiData == null)
				|| (this.multiData.size() == 0);

		if (noData) {
			s.reinitNodata();
		} else {
			 

			String idsStr = "";
			// #debug debug
			System.out.println("Multi send");
			int[] ids = new int[this.multiData.size()];

			for (int i = 0; i < ids.length; ++i) {
				ids[i] = ((IDataPayload) this.multiData.elementAt(i))
						.getTransportId();
				idsStr += " " + ids[i];
			}

			s.reinit(ids);

			// #debug debug
			System.out.println("ids: " + idsStr);
		}

		shell.setDisplay((IActivity) this.activity, (IView) s);

		if (!noData)
			for (Enumeration en = this.multiData.elements(); en
					.hasMoreElements();) {
				IDataPayload payload = (IDataPayload) en.nextElement();
				send(payload, payload.getTransportId());
			}

	}

	private void send(IDataPayload payload, int id) throws IOException {
		JavaRosaServiceProvider.instance().getTransportManager().enqueue(
				payload, this.destination, getCurrentTransportMethod(), id);
	}

	/**
	 * @return
	 */
	private ITransportDestination getDefaultDestination() {
		ITransportManager tmanager = JavaRosaServiceProvider.instance()
				.getTransportManager();
		int currentMethod = tmanager.getCurrentTransportMethod();
		ITransportDestination d = tmanager
				.getDefaultTransportDestination(currentMethod);
		return d;
	}

	public void setDefaultDestination() {
		this.destination = getDefaultDestination();
	}

	private int getCurrentTransportMethod() {
		return JavaRosaServiceProvider.instance().getTransportManager()
				.getCurrentTransportMethod();

	}

	// ----------- getters and setters
	public DataModelTree getData() {
		return this.data;
	}

	public void setData(DataModelTree data) {
		this.data = data;
	}

	public void setSerializer(IDataModelSerializingVisitor serializer) {
		this.serializer = serializer;
	}

	public ITransportDestination getDestination() {
		return this.destination;
	}

	public void setDestination(ITransportDestination destination) {
		this.destination = destination;
	}

	public boolean isMultiple() {
		return this.multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	// -- multiple
	public Vector getMultiData() {
		return this.multiData;
	}

	public void setMultiData(Vector multiData) {
		this.multiData = multiData;
	}

}