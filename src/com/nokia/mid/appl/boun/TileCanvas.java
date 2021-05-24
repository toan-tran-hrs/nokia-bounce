// Download by http://www.codefans.net
// Source File Name:   TileCanvas.java

package com.nokia.mid.appl.boun;

import com.nokia.mid.ui.*;
import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;

// Referenced classes of package com.nokia.mid.appl.boun:
//            Local, Ball

public abstract class TileCanvas extends FullCanvas
{
    protected class GameTimer extends TimerTask
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


    public int tileX;
    public int tileY;
    public int divTileX;
    public int divTileY;
    public int divisorLine;
    public int rightDrawEdge;
    public int leftDrawEdge;
    public boolean scrollFlag;
    public int scrollOffset;
    public int mExitPos;
    protected Image mGameBuffer;
    public Graphics mGameGraphics;
    private Image tileImages[];
    private Image tmpTileImage;
    private Graphics tmpTileImageG;
    Vector hoopImageList;
    Vector hoopXPosList;
    Vector hoopYPosList;
    public int mLevelNum;
    public String mLevelNumStr;
    public String mLevelCompletedStr;
    public boolean mLoadLevelFlag;
    public int mStartCol;
    public int mStartRow;
    public int mStartBallSize;
    public int mExitPosX;
    public int mExitPosY;
    public short tileMap[][];
    public int mTileMapWidth;
    public int mTileMapHeight;
    public int mTotalNumRings;
    public int mNumMoveObj;
    public short mMOTopLeft[][];
    public short mMOBotRight[][];
    public short mMODirection[][];
    public short mMOOffset[][];
    public Image mMOImgPtr[];
    public Graphics mMOImgGraphics[];
    public Image mSpikeImgPtr;
    public Image mUILife;
    public Image mUIRing;
    public int mTopLeftExitTileCol;
    public int mTopLeftExitTileRow;
    public Image mExitTileImage;
    public Image mImgPtr;
    public int mImageOffset;
    public boolean mOpenFlag;
    protected int mWidth;
    protected int mHeight;
    protected Display mDisplay;
    public GameTimer mGameTimer;

    public TileCanvas(Display display)
    {
        mGameGraphics = null;
        mWidth = 0;
        mHeight = 0;
        mGameTimer = null;
        mDisplay = display;
        mWidth = super.getWidth();
        mHeight = super.getHeight();
        scrollFlag = true;
        divisorLine = 156;
        rightDrawEdge = 142;
        leftDrawEdge = 14;
        mGameBuffer = Image.createImage(156, 96);
        tmpTileImage = Image.createImage(12, 12);
        tmpTileImageG = tmpTileImage.getGraphics();
        loadTileImages();
        mLoadLevelFlag = false;
        tileX = 0;
        tileY = 0;
        mExitPos = -1;
        divTileX = tileX + 13;
        divTileY = tileY;
        tileMap = null;
        hoopImageList = new Vector();
        hoopXPosList = new Vector();
        hoopYPosList = new Vector();
    }

    public void loadLevel(int i)
    {
        Object obj = null;
        Object obj1 = null;
        mLoadLevelFlag = false;
        String s = "";
        String as[] = new String[1];
        as[0] = (new Integer(mLevelNum)).toString();
        mLevelNumStr = Local.getText(14, as);
        mLevelCompletedStr = Local.getText(15, as);
        as[0] = null;
        as = null;
        if(i < 10)
            s = "00" + i;
        else
        if(i < 100)
            s = "0" + i;
        try
        {
            InputStream inputstream = getClass().getResourceAsStream("/levels/J2MElvl." + s);
            DataInputStream datainputstream = new DataInputStream(inputstream);
            mStartCol = datainputstream.read();
            mStartRow = datainputstream.read();
            int j = datainputstream.read();
            if(j == 0)
                mStartBallSize = 12;
            else
                mStartBallSize = 16;
            mExitPosX = datainputstream.read();
            mExitPosY = datainputstream.read();
            createExitTileObject(mExitPosX, mExitPosY, tileImages[12]);
            mTotalNumRings = datainputstream.read();
            mTileMapWidth = datainputstream.read();
            mTileMapHeight = datainputstream.read();
            tileMap = new short[mTileMapHeight][mTileMapWidth];
            for(int k = 0; k < mTileMapHeight; k++)
            {
                for(int l = 0; l < mTileMapWidth; l++)
                    tileMap[k][l] = (short)datainputstream.read();

            }

            mNumMoveObj = datainputstream.read();
            if(mNumMoveObj != 0)
                createMovingObj(datainputstream);
            datainputstream.close();
        }
        catch(IOException ioexception) { }
    }

    public static Image manipulateImage(Image image, int i)
    {
        Image image1 = DirectUtils.createImage(image.getWidth(), image.getHeight(), 0);
        if(image1 == null)
            image1 = Image.createImage(image.getWidth(), image.getHeight());
        Graphics g = image1.getGraphics();
        DirectGraphics directgraphics = DirectUtils.getDirectGraphics(g);
        switch(i)
        {
        case 0: // '\0'
            directgraphics.drawImage(image, 0, 0, 20, 8192);
            break;

        case 1: // '\001'
            directgraphics.drawImage(image, 0, 0, 20, 16384);
            break;

        case 2: // '\002'
            directgraphics.drawImage(image, 0, 0, 20, 24576);
            break;

        case 3: // '\003'
            directgraphics.drawImage(image, 0, 0, 20, 90);
            break;

        case 4: // '\004'
            directgraphics.drawImage(image, 0, 0, 20, 180);
            break;

        case 5: // '\005'
            directgraphics.drawImage(image, 0, 0, 20, 270);
            break;

        default:
            g.drawImage(image, 0, 0, 20);
            break;
        }
        return image1;
    }

    public void createMovingObj(DataInputStream datainputstream)
        throws IOException
    {
        mMOTopLeft = new short[mNumMoveObj][2];
        mMOBotRight = new short[mNumMoveObj][2];
        mMODirection = new short[mNumMoveObj][2];
        mMOOffset = new short[mNumMoveObj][2];
        mMOImgPtr = new Image[mNumMoveObj];
        mMOImgGraphics = new Graphics[mNumMoveObj];
        for(int k = 0; k < mNumMoveObj; k++)
        {
            mMOTopLeft[k][0] = (short)datainputstream.read();
            mMOTopLeft[k][1] = (short)datainputstream.read();
            mMOBotRight[k][0] = (short)datainputstream.read();
            mMOBotRight[k][1] = (short)datainputstream.read();
            mMODirection[k][0] = (short)datainputstream.read();
            mMODirection[k][1] = (short)datainputstream.read();
            int i = datainputstream.read();
            int j = datainputstream.read();
            mMOOffset[k][0] = (short)i;
            mMOOffset[k][1] = (short)j;
        }

        mSpikeImgPtr = Image.createImage(24, 24);
        Graphics g = mSpikeImgPtr.getGraphics();
        g.drawImage(tileImages[46], 0, 0, 20);
        g.drawImage(manipulateImage(tileImages[46], 0), 12, 0, 20);
        g.drawImage(manipulateImage(tileImages[46], 4), 12, 12, 20);
        g.drawImage(manipulateImage(tileImages[46], 1), 0, 12, 20);
        g = null;
    }

    public void disposeLevel()
    {
        for(int i = 0; i < mNumMoveObj; i++)
        {
            mMOImgPtr[i] = null;
            mMOImgGraphics[i] = null;
        }

        mMOImgPtr = null;
        mMOImgGraphics = null;
        tileMap = null;
        Runtime.getRuntime().gc();
    }

    public void updateMovingSpikeObj()
    {
        for(int i = 0; i < mNumMoveObj; i++)
        {
            short word0 = mMOTopLeft[i][0];
            short word1 = mMOTopLeft[i][1];
            int j = mMOOffset[i][0];
            int k = mMOOffset[i][1];
            mMOOffset[i][0] += mMODirection[i][0];
            int j1 = (mMOBotRight[i][0] - word0 - 2) * 12;
            int k1 = (mMOBotRight[i][1] - word1 - 2) * 12;
            if(mMOOffset[i][0] < 0)
                mMOOffset[i][0] = 0;
            else
            if(mMOOffset[i][0] > j1)
                mMOOffset[i][0] = (short)j1;
            if(mMOOffset[i][0] == 0 || mMOOffset[i][0] == j1)
                mMODirection[i][0] = (short)(-mMODirection[i][0]);
            mMOOffset[i][1] += mMODirection[i][1];
            if(mMOOffset[i][1] < 0)
                mMOOffset[i][1] = 0;
            else
            if(mMOOffset[i][1] > k1)
                mMOOffset[i][1] = (short)k1;
            if(mMOOffset[i][1] == 0 || mMOOffset[i][1] == k1)
                mMODirection[i][1] *= -1;
            int l = mMOOffset[i][0];
            int i1 = mMOOffset[i][1];
            if(l < j)
            {
                int l1 = l;
                l = j;
                j = l1;
            }
            if(i1 < k)
            {
                int i2 = i1;
                i1 = k;
                k = i2;
            }
            l += 23;
            i1 += 23;
            j /= 12;
            k /= 12;
            l = l / 12 + 1;
            i1 = i1 / 12 + 1;
            for(int j2 = j; j2 < l; j2++)
            {
                for(int k2 = k; k2 < i1; k2++)
                    tileMap[word1 + k2][word0 + j2] |= 0x80;

            }

        }

    }

    public int findSpikeIndex(int i, int j)
    {
        for(int k = 0; k < mNumMoveObj; k++)
            if(mMOTopLeft[k][0] <= i && mMOBotRight[k][0] > i && mMOTopLeft[k][1] <= j && mMOBotRight[k][1] > j)
                return k;

        return -1;
    }

    public void drawTile(int i, int j, int k, int l)
    {
        if(mGameGraphics == null)
            mGameGraphics = mGameBuffer.getGraphics();
        if((tileMap[j][i] & 0x80) != 0)
            tileMap[j][i] &= 0xff7f;
        int i1 = tileMap[j][i];
        boolean flag = (i1 & 0x40) != 0;
        if(flag)
            i1 &= 0xffffffbf;
        mGameGraphics.setColor(flag ? 0x1060b0 : 0xb0e0f0);
        switch(i1)
        {
        case 11: // '\013'
        case 12: // '\f'
        default:
            break;

        case 1: // '\001'
            mGameGraphics.drawImage(tileImages[0], k, l, 20);
            break;

        case 0: // '\0'
            mGameGraphics.fillRect(k, l, 12, 12);
            break;

        case 2: // '\002'
            mGameGraphics.drawImage(tileImages[1], k, l, 20);
            break;

        case 3: // '\003'
            if(flag)
                mGameGraphics.drawImage(tileImages[6], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[2], k, l, 20);
            break;

        case 4: // '\004'
            if(flag)
                mGameGraphics.drawImage(tileImages[9], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[5], k, l, 20);
            break;

        case 5: // '\005'
            if(flag)
                mGameGraphics.drawImage(tileImages[7], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[3], k, l, 20);
            break;

        case 6: // '\006'
            if(flag)
                mGameGraphics.drawImage(tileImages[8], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[4], k, l, 20);
            break;

        case 7: // '\007'
            mGameGraphics.drawImage(tileImages[10], k, l, 20);
            break;

        case 8: // '\b'
            mGameGraphics.drawImage(tileImages[11], k, l, 20);
            break;

        case 23: // '\027'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[13], k, l, 20);
            add2HoopList(tileImages[14], k, l);
            break;

        case 24: // '\030'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[15], k, l, 20);
            add2HoopList(tileImages[16], k, l);
            break;

        case 15: // '\017'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[17], k, l, 20);
            add2HoopList(tileImages[18], k, l);
            break;

        case 16: // '\020'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[19], k, l, 20);
            add2HoopList(tileImages[20], k, l);
            break;

        case 27: // '\033'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[21], k, l, 20);
            add2HoopList(tileImages[22], k, l);
            break;

        case 28: // '\034'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[23], k, l, 20);
            add2HoopList(tileImages[24], k, l);
            break;

        case 19: // '\023'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[25], k, l, 20);
            add2HoopList(tileImages[26], k, l);
            break;

        case 20: // '\024'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[27], k, l, 20);
            add2HoopList(tileImages[28], k, l);
            break;

        case 21: // '\025'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[31], k, l, 20);
            add2HoopList(tileImages[29], k, l);
            break;

        case 22: // '\026'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[32], k, l, 20);
            add2HoopList(tileImages[30], k, l);
            break;

        case 13: // '\r'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[35], k, l, 20);
            add2HoopList(tileImages[33], k, l);
            break;

        case 14: // '\016'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[36], k, l, 20);
            add2HoopList(tileImages[34], k, l);
            break;

        case 25: // '\031'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[39], k, l, 20);
            add2HoopList(tileImages[37], k, l);
            break;

        case 26: // '\032'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[40], k, l, 20);
            add2HoopList(tileImages[38], k, l);
            break;

        case 17: // '\021'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[43], k, l, 20);
            add2HoopList(tileImages[41], k, l);
            break;

        case 18: // '\022'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[44], k, l, 20);
            add2HoopList(tileImages[42], k, l);
            break;

        case 9: // '\t'
            int j1 = (i - mTopLeftExitTileCol) * 12;
            int l1 = (j - mTopLeftExitTileRow) * 12;
            mGameGraphics.setClip(k, l, 12, 12);
            mGameGraphics.drawImage(mExitTileImage, k - j1, l - l1, 20);
            mGameGraphics.setClip(0, 0, mGameBuffer.getWidth(), mGameBuffer.getHeight());
            mExitPos = ((k - j1) + 12) - 1;
            break;

        case 10: // '\n'
            int k1 = findSpikeIndex(i, j);
            if(k1 == -1)
                break;
            int i2 = (i - mMOTopLeft[k1][0]) * 12;
            int j2 = (j - mMOTopLeft[k1][1]) * 12;
            int k2 = mMOOffset[k1][0] - i2;
            int l2 = mMOOffset[k1][1] - j2;
            if(k2 > -36 && k2 < 12 || l2 > -36 && l2 < 12)
            {
                tmpTileImageG.setColor(0xb0e0f0);
                tmpTileImageG.fillRect(0, 0, 12, 12);
                tmpTileImageG.drawImage(mSpikeImgPtr, k2, l2, 20);
                mGameGraphics.drawImage(tmpTileImage, k, l, 20);
            } else
            {
                mGameGraphics.setColor(0xb0e0f0);
                mGameGraphics.fillRect(k, l, 12, 12);
            }
            break;

        case 29: // '\035'
            mGameGraphics.drawImage(tileImages[45], k, l, 20);
            break;

        case 30: // '\036'
            if(flag)
                mGameGraphics.drawImage(tileImages[61], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[57], k, l, 20);
            break;

        case 31: // '\037'
            if(flag)
                mGameGraphics.drawImage(tileImages[60], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[56], k, l, 20);
            break;

        case 32: // ' '
            if(flag)
                mGameGraphics.drawImage(tileImages[59], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[55], k, l, 20);
            break;

        case 33: // '!'
            if(flag)
                mGameGraphics.drawImage(tileImages[62], k, l, 20);
            else
                mGameGraphics.drawImage(tileImages[58], k, l, 20);
            break;

        case 34: // '"'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[65], k, l, 20);
            break;

        case 35: // '#'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[64], k, l, 20);
            break;

        case 36: // '$'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[63], k, l, 20);
            break;

        case 37: // '%'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[66], k, l, 20);
            break;

        case 39: // '\''
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[50], k, l, 20);
            break;

        case 40: // '('
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(manipulateImage(tileImages[50], 5), k, l, 20);
            break;

        case 41: // ')'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(manipulateImage(tileImages[50], 4), k, l, 20);
            break;

        case 42: // '*'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(manipulateImage(tileImages[50], 3), k, l, 20);
            break;

        case 43: // '+'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(tileImages[51], k, l, 20);
            break;

        case 44: // ','
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(manipulateImage(tileImages[51], 5), k, l, 20);
            break;

        case 45: // '-'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(manipulateImage(tileImages[51], 4), k, l, 20);
            break;

        case 46: // '.'
            mGameGraphics.fillRect(k, l, 12, 12);
            mGameGraphics.drawImage(manipulateImage(tileImages[51], 3), k, l, 20);
            break;

        case 47: // '/'
            mGameGraphics.drawImage(tileImages[52], k, l, 20);
            break;

        case 48: // '0'
            mGameGraphics.drawImage(manipulateImage(tileImages[52], 5), k, l, 20);
            break;

        case 49: // '1'
            mGameGraphics.drawImage(manipulateImage(tileImages[52], 4), k, l, 20);
            break;

        case 50: // '2'
            mGameGraphics.drawImage(manipulateImage(tileImages[52], 3), k, l, 20);
            break;

        case 38: // '&'
            mGameGraphics.drawImage(tileImages[53], k, l, 20);
            break;

        case 51: // '3'
            mGameGraphics.drawImage(tileImages[54], k, l, 20);
            break;

        case 52: // '4'
            mGameGraphics.drawImage(manipulateImage(tileImages[54], 5), k, l, 20);
            break;

        case 53: // '5'
            mGameGraphics.drawImage(manipulateImage(tileImages[54], 4), k, l, 20);
            break;

        case 54: // '6'
            mGameGraphics.drawImage(manipulateImage(tileImages[54], 3), k, l, 20);
            break;
        }
    }

    public void add2HoopList(Image image, int i, int j)
    {
        hoopImageList.addElement(image);
        hoopXPosList.addElement(new Integer(i));
        hoopYPosList.addElement(new Integer(j));
    }

    public void createNewBuffer()
    {
        for(int i = 0; i < 13; i++)
        {
            for(int j = 0; j < 8; j++)
                drawTile(tileX + i, tileY + j, i * 12, j * 12);

        }

    }

    public void cleanBuffer(boolean flag)
    {
        int i = tileX;
        int j = tileY;
        for(int k = 0; k < 13; k++)
        {
            if(k * 12 >= divisorLine && i >= tileX)
                i = divTileX - 13;
            for(int l = 0; l < 8; l++)
            {
                if((tileMap[j][i] & 0x80) != 0)
                {
                    tileMap[j][i] &= 0xff7f;
                    if(flag)
                        drawTile(i, j, k * 12, l * 12);
                }
                j++;
            }

            j = tileY;
            i++;
        }

    }

    public void scrollBuffer(int i, int j, int k)
    {
        if(rightDrawEdge < 0)
            rightDrawEdge += 156;
        if(rightDrawEdge > divisorLine && rightDrawEdge <= divisorLine + 12)
        {
            if(tileX + divisorLine / 12 >= mTileMapWidth)
            {
                leftDrawEdge -= j;
                rightDrawEdge -= j;
                if(rightDrawEdge < 0)
                    rightDrawEdge += 156;
                if(scrollFlag)
                {
                    scrollFlag = false;
                    scrollOffset = rightDrawEdge - 64;
                    if(scrollOffset < k)
                        scrollOffset += 156;
                }
            } else
            {
                if(divisorLine >= 156)
                {
                    divisorLine = 0;
                    tileX += 13;
                }
                if(rightDrawEdge >= 156)
                    rightDrawEdge -= 156;
                int l = divisorLine;
                divisorLine += 12;
                divTileX++;
                for(int j1 = 0; j1 < 8; j1++)
                    drawTile(tileX + l / 12, tileY + j1, l, j1 * 12);

            }
        } else
        if(rightDrawEdge > 156)
            rightDrawEdge -= 156;
        if(leftDrawEdge >= 156)
            leftDrawEdge -= 156;
        if(leftDrawEdge < 0)
            leftDrawEdge += 156;
        if(leftDrawEdge < divisorLine && leftDrawEdge >= divisorLine - 12)
            if(tileX - (13 - divisorLine / 12) <= 0)
            {
                leftDrawEdge -= j;
                rightDrawEdge -= j;
                if(leftDrawEdge >= 156)
                    leftDrawEdge -= 156;
                if(scrollFlag)
                {
                    scrollFlag = false;
                    scrollOffset = (leftDrawEdge + 64) % 156;
                    if(scrollOffset < k)
                        scrollOffset += 156;
                }
            } else
            {
                divisorLine -= 12;
                int i1 = divisorLine;
                divTileX--;
                if(divisorLine <= 0)
                {
                    divisorLine = 156;
                    tileX -= 13;
                }
                for(int k1 = 0; k1 < 8; k1++)
                    drawTile(divTileX - 13, divTileY + k1, i1, k1 * 12);

            }
    }

    void testScroll(int i, int j)
    {
        if(!scrollFlag)
        {
            if(tileX - (13 - divisorLine / 12) <= 0 && i >= scrollOffset && i < scrollOffset + 10)
            {
                scrollFlag = true;
                j = i - scrollOffset;
            }
            if(tileX + divisorLine / 12 >= mTileMapWidth && i <= scrollOffset && i > scrollOffset - 10)
            {
                scrollFlag = true;
                j = i - scrollOffset;
            }
        }
        if(scrollFlag)
        {
            leftDrawEdge += j;
            rightDrawEdge += j;
        }
    }

    public Image createLargeBallImage(Image image)
    {
        Image image1 = DirectUtils.createImage(16, 16, 0);
        if(image1 == null)
            image1 = Image.createImage(16, 16);
        Graphics g = image1.getGraphics();
        DirectGraphics directgraphics = DirectUtils.getDirectGraphics(g);
        g.drawImage(image, -4, -4, 20);
        directgraphics.drawImage(image, 8, -4, 20, 8192);
        directgraphics.drawImage(image, -4, 8, 20, 16384);
        directgraphics.drawImage(image, 8, 8, 20, 180);
        return image1;
    }

    public Image createExitImage(Image image)
    {
        Image image1 = Image.createImage(24, 48);
        Graphics g = image1.getGraphics();
        g.setColor(0xb0e0f0);
        g.fillRect(0, 0, 24, 48);
        g.setColor(0xfc9d9e);
        g.fillRect(4, 0, 16, 48);
        g.setColor(0xe33a3f);
        g.fillRect(6, 0, 10, 48);
        g.setColor(0xc2848e);
        g.fillRect(10, 0, 4, 48);
        g.drawImage(image, 0, 0, 20);
        g.drawImage(manipulateImage(image, 0), 12, 0, 20);
        g.drawImage(manipulateImage(image, 1), 0, 12, 20);
        g.drawImage(manipulateImage(image, 2), 12, 12, 20);
        return image1;
    }

    public void loadTileImages()
    {
        Image image = loadImage("/icons/objects_nm.png");
        tileImages = new Image[67];
        tileImages[0] = extractImage(image, 1, 0);
        tileImages[1] = extractImage(image, 1, 2);
        tileImages[2] = extractImageBG(image, 0, 3, 0xffb0e0f0);
        tileImages[3] = manipulateImage(tileImages[2], 1);
        tileImages[4] = manipulateImage(tileImages[2], 3);
        tileImages[5] = manipulateImage(tileImages[2], 5);
        tileImages[6] = extractImageBG(image, 0, 3, 0xff1060b0);
        tileImages[7] = manipulateImage(tileImages[6], 1);
        tileImages[8] = manipulateImage(tileImages[6], 3);
        tileImages[9] = manipulateImage(tileImages[6], 5);
        tileImages[10] = extractImage(image, 0, 4);
        tileImages[11] = extractImage(image, 3, 4);
        tileImages[12] = createExitImage(extractImage(image, 2, 3));
        tileImages[14] = extractImage(image, 0, 5);
        tileImages[13] = manipulateImage(tileImages[14], 1);
        tileImages[15] = manipulateImage(tileImages[13], 0);
        tileImages[16] = manipulateImage(tileImages[14], 0);
        tileImages[18] = extractImage(image, 1, 5);
        tileImages[17] = manipulateImage(tileImages[18], 1);
        tileImages[19] = manipulateImage(tileImages[17], 0);
        tileImages[20] = manipulateImage(tileImages[18], 0);
        tileImages[22] = extractImage(image, 2, 5);
        tileImages[21] = manipulateImage(tileImages[22], 1);
        tileImages[23] = manipulateImage(tileImages[21], 0);
        tileImages[24] = manipulateImage(tileImages[22], 0);
        tileImages[26] = extractImage(image, 3, 5);
        tileImages[25] = manipulateImage(tileImages[26], 1);
        tileImages[27] = manipulateImage(tileImages[25], 0);
        tileImages[28] = manipulateImage(tileImages[26], 0);
        tileImages[29] = manipulateImage(tileImages[14], 5);
        tileImages[30] = manipulateImage(tileImages[29], 1);
        tileImages[31] = manipulateImage(tileImages[29], 0);
        tileImages[32] = manipulateImage(tileImages[30], 0);
        tileImages[33] = manipulateImage(tileImages[18], 5);
        tileImages[34] = manipulateImage(tileImages[33], 1);
        tileImages[35] = manipulateImage(tileImages[33], 0);
        tileImages[36] = manipulateImage(tileImages[34], 0);
        tileImages[37] = manipulateImage(tileImages[22], 5);
        tileImages[38] = manipulateImage(tileImages[37], 1);
        tileImages[39] = manipulateImage(tileImages[37], 0);
        tileImages[40] = manipulateImage(tileImages[38], 0);
        tileImages[41] = manipulateImage(tileImages[26], 5);
        tileImages[42] = manipulateImage(tileImages[41], 1);
        tileImages[43] = manipulateImage(tileImages[41], 0);
        tileImages[44] = manipulateImage(tileImages[42], 0);
        tileImages[45] = extractImage(image, 3, 3);
        tileImages[46] = extractImage(image, 1, 3);
        tileImages[47] = extractImage(image, 2, 0);
        tileImages[48] = extractImage(image, 0, 1);
        tileImages[49] = createLargeBallImage(extractImage(image, 3, 0));
        tileImages[50] = extractImage(image, 3, 1);
        tileImages[51] = extractImage(image, 2, 4);
        tileImages[52] = extractImage(image, 3, 2);
        tileImages[53] = extractImage(image, 1, 1);
        tileImages[54] = extractImage(image, 2, 2);
        tileImages[55] = extractImageBG(image, 0, 0, 0xffb0e0f0);
        tileImages[56] = manipulateImage(tileImages[55], 3);
        tileImages[57] = manipulateImage(tileImages[55], 4);
        tileImages[58] = manipulateImage(tileImages[55], 5);
        tileImages[59] = extractImageBG(image, 0, 0, 0xff1060b0);
        tileImages[60] = manipulateImage(tileImages[59], 3);
        tileImages[61] = manipulateImage(tileImages[59], 4);
        tileImages[62] = manipulateImage(tileImages[59], 5);
        tileImages[63] = extractImage(image, 0, 2);
        tileImages[64] = manipulateImage(tileImages[63], 3);
        tileImages[65] = manipulateImage(tileImages[63], 4);
        tileImages[66] = manipulateImage(tileImages[63], 5);
        mUILife = extractImage(image, 2, 1);
        mUIRing = extractImage(image, 1, 4);
    }

    public void setBallImages(Ball ball)
    {
        ball.smallBallImage = tileImages[47];
        ball.poppedImage = tileImages[48];
        ball.largeBallImage = tileImages[49];
    }

    public static Image extractImage(Image image, int i, int j)
    {
        return extractImageBG(image, i, j, 0);
    }

    public static Image extractImageBG(Image image, int i, int j, int k)
    {
        Image image1 = DirectUtils.createImage(12, 12, k);
        if(image1 == null)
        {
            image1 = Image.createImage(12, 12);
            Graphics g = image1.getGraphics();
            g.setColor(k);
            g.fillRect(0, 0, 12, 12);
        }
        Graphics g1 = image1.getGraphics();
        g1.drawImage(image, -i * 12, -j * 12, 20);
        return image1;
    }

    public static Image loadImage(String s)
    {
        Image image = null;
        try
        {
            image = Image.createImage(s);
        }
        catch(IOException ioexception) { }
        return image;
    }

    public Image getImage(int i)
    {
        if(i < 67)
            return tileImages[i];
        else
            return null;
    }

    public void createExitTileObject(int i, int j, Image image)
    {
        mTopLeftExitTileCol = i;
        mTopLeftExitTileRow = j;
        mImgPtr = image;
        mExitTileImage = Image.createImage(24, 24);
        mImageOffset = 0;
        repaintExitTile();
        mOpenFlag = false;
    }

    public void repaintExitTile()
    {
        Graphics g = mExitTileImage.getGraphics();
        g.drawImage(mImgPtr, 0, 0 - mImageOffset, 20);
    }

    public void openExit()
    {
        mImageOffset += 4;
        if(mImageOffset >= 24)
        {
            mImageOffset = 24;
            mOpenFlag = true;
        }
        repaintExitTile();
    }

    public static boolean rectCollide(int i, int j, int k, int l, int i1, int j1, int k1, int l1)
    {
        return i <= k1 && j <= l1 && i1 <= k && j1 <= l;
    }

    public abstract void run();

    public synchronized void start()
    {
        if(mGameTimer != null)
        {
            return;
        } else
        {
            mGameTimer = new GameTimer(this);
            return;
        }
    }

    public synchronized void stop()
    {
        if(mGameTimer == null)
        {
            return;
        } else
        {
            mGameTimer.stop();
            mGameTimer = null;
            return;
        }
    }

    protected void timerTrigger()
    {
        run();
    }
}
