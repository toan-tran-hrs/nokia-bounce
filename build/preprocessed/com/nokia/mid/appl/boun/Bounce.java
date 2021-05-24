// Source File Name:   Bounce.java
// Download by http://www.codefans.net
package com.nokia.mid.appl.boun;

import com.nokia.mid.ui.DeviceControl;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

// Referenced classes of package com.nokia.mid.appl.boun:
//            BounceUI, TileCanvas

public class Bounce extends MIDlet
{

    private BounceUI mUI;
    public static final boolean DEBUG = false;

    public Bounce()
    {
        if(mUI == null)
            mUI = new BounceUI(this);
    }

    protected void startApp()
    {
        DeviceControl.setLights(0, 100);
    }

    protected void pauseApp()
    {
    }

    public void destroyApp(boolean flag)
    {
        if(mUI != null && mUI.mCanvas != null)
        {
            mUI.saveGameData(3);
            mUI.mCanvas.stop();
        }
        Display.getDisplay(this).setCurrent(null);
        mUI = null;
    }
}
