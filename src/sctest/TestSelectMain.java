package sctest;

import javax.smartcardio.*;
import java.util.Iterator;

/**
 * Created by tsechin on 5/27/14.
 */
public class TestSelectMain {
  private static String readerName = null; // "Lenovo Integrated Smart Card Reader 0";
  private static final byte[] cm_aid = {
      (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
      (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00
  };

  public static void main(String[] args) {
    try {
      System.out.println("readers present:");
      CardTerminals cts = TerminalFactory.getDefault().terminals();
      for (Iterator<CardTerminal> it = cts.list().iterator(); it.hasNext(); ) {
        CardTerminal ct = it.next();
        if (null == readerName) readerName = ct.getName(); // choose 1st reader if null
        System.out.println(" - " + ct.getName());
      }

      if (null == readerName) throw new Exception("readerName is null");

      System.out.println("using reader " + readerName);
      CardTerminal ct = cts.getTerminal(readerName);
      Card c = ct.connect("*"); // "*" means any protocol; use "T=0" or "T=1" to specify
      CardChannel chan = c.getBasicChannel();

      CommandAPDU selectCM = new CommandAPDU((byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, cm_aid);
      ResponseAPDU rsp_selectCM = chan.transmit(selectCM);
      System.out.println("select returned " + Integer.toHexString(rsp_selectCM.getSW()));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
