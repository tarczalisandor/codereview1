/*
 * Created on Jan 26, 2012
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.bfutil;

import it.usi.xframe.system.logging.elf.entities.AbstractTechnicalMessage;
import it.usi.xframe.system.logging.elf.entities.ApplicationCode;
import it.usi.xframe.system.logging.elf.entities.MessageCluster;
import it.usi.xframe.system.logging.elf.entities.MessageId;
import it.usi.xframe.system.logging.elf.entities.MessageLevel;
import it.usi.xframe.system.logging.elf.entities.MessageText;
;

/**
 * @author ee00681
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XAS_Message extends AbstractTechnicalMessage {

	public static final ApplicationCode CODE = new ApplicationCode("XAS");

	public XAS_Message(int cluster, int id, String level, String text)
	{
		super(CODE, new MessageCluster(cluster), new MessageId(id), MessageLevel.getMessageLevel(level), new MessageText(text));
	}

	public static final XAS_Message MSG_XAS00001I = new XAS_Message(0, 1, "INFO", "A new XAS message with s={0}, i={1}.");
	public static final XAS_Message MSG_XAS00002I = new XAS_Message(0, 2, "INFO", "Try division a=b:c with a={0}, b={1} c={2}.");
	public static final XAS_Message MSG_XAS00003E = new XAS_Message(0, 3, "ERROR", "the division generate an error a=b:c with a={0}, b={1} c={2}");
	public static final XAS_Message MSG_XAS00004W = new XAS_Message(0, 4, "WARNING", "Recovering from crash, result is set to {0}.");
	public static final XAS_Message MSG_XAS00005I = new XAS_Message(0, 5, "WARNING", "EMIT: Sequential message i={0} type={1}");
}
