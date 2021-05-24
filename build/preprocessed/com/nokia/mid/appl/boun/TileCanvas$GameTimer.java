// Download by http://www.codefans.net
// Source File Name:   TileCanvas.java

package com.nokia.mid.appl.boun;

import java.util.Timer;
import java.util.TimerTask;

// Referenced classes of package com.nokia.mid.appl.boun:
//            TileCanvas

class GameTimer extends TimerTask
{

    TileCanvas parent;
    Timer timer;

    public void run()
    {
        parent.timerTrigger();
    }

    void stop()
    {
        if(timer == null)
        {
            return;
        } else
        {
            cancel();
            timer.cancel();
            timer = null;
            return;
        }
    }

    public GameTimer(TileCanvas tilecanvas1)
    {
        parent = tilecanvas1;
        timer = new Timer();
        timer.schedule(this, 0L, 40L);
    }
}
