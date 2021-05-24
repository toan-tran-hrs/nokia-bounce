// Download by http://www.codefans.net
// Source File Name:   BounceCanvas.java

package com.nokia.mid.appl.boun;

import com.nokia.mid.sound.Sound;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.*;

// Referenced classes of package com.nokia.mid.appl.boun:
//            TileCanvas, Ball, BounceUI

public class BounceCanvas extends TileCanvas
{

    public int mSplashIndex;
    public Image mSplashImage;
    private int mSplashTimer;
    protected Sound mSoundHoop;
    protected Sound mSoundPickup;
    protected Sound mSoundPop;
    private BounceUI mUI;
    public Ball mBall;
    public int numRings;
    public int numLives;
    public int mScore;
    public int bonusCntrValue;
    public int mLevelDisCntr;
    public boolean mLeaveGame;
    public boolean mOpenExitFlag;
    public boolean mPaintUIFlag;
    public final Font TEXT_FONT = Font.getFont(32, 0, 8);
    public Image mFullScreenBuffer;
    public Graphics mFullScreenGraphics;
    public boolean mClearScreenFlag;
    private boolean mCheat;
    public boolean mInvincible;
    private int mCheatSeq;
    private static final String SPLASH_NAME[] = {
        "/icons/nokiagames.png", "/icons/bouncesplash.png"
    };
    public long mRepaintTime;
    public int mRepaintCount;
    public boolean mIncomingCall;
    private long mLastTimeRepainted;

    public BounceCanvas(BounceUI bounceui, int i)
    {
        super(bounceui.mDisplay);
        mFullScreenGraphics = null;
        mCheat = false;
        mInvincible = false;
        mCheatSeq = 0;
        mRepaintTime = 0L;
        mRepaintCount = 0;
        mIncomingCall = true;
        mLastTimeRepainted = System.currentTimeMillis();
        mUI = bounceui;
        mSoundHoop = loadSound("/sounds/up.ott");
        mSoundPickup = loadSound("/sounds/pickup.ott");
        mSoundPop = loadSound("/sounds/pop.ott");
        try
        {
            mFullScreenBuffer = Image.createImage(128, 128);
        }
        catch(Exception exception) { }
        mSplashIndex = 1;
        try
        {
            mSplashImage = Image.createImage(SPLASH_NAME[mSplashIndex]);
        }
        catch(IOException ioexception)
        {
            mSplashImage = Image.createImage(1, 1);
        }
        start();
    }

    public void resetGame(int i, int j, int k)
    {
        mLevelNum = i;
        numRings = 0;
        numLives = k;
        mScore = j;
        mLeaveGame = false;
        mOpenExitFlag = false;
        createNewLevel();
        mClearScreenFlag = true;
    }

    public void resetGame(int i, int j)
    {
        mLevelNum = mUI.mSavedLevel;
        numRings = mUI.mSavedRings;
        numLives = mUI.mSavedLives;
        mScore = mUI.mSavedScore;
        disposeLevel();
        loadLevel(mLevelNum);
        resetTiles();
        resetSpikes();
        mLevelDisCntr = 120;
        mPaintUIFlag = true;
        if(mUI.mSavedRespawnX != mStartCol && mUI.mSavedRespawnY != mStartRow)
            tileMap[mUI.mSavedRespawnY][mUI.mSavedRespawnX] = (short)(0x8 | tileMap[mUI.mSavedRespawnY][mUI.mSavedRespawnX] & 0x40);
        createBufferFocused(i, j, mUI.mSavedSize, mUI.mSavedXSpeed, mUI.mSavedYSpeed);
        mBall.setRespawn(mUI.mSavedRespawnX, mUI.mSavedRespawnY);
        mBall.speedBonusCntr = mUI.mSavedSpeedBonus;
        mBall.gravBonusCntr = mUI.mSavedGravBonus;
        mBall.jumpBonusCntr = mUI.mSavedJumpBonus;
        mClearScreenFlag = true;
    }

    private void createNewLevel()
    {
        disposeLevel();
        loadLevel(mLevelNum);
        numRings = 0;
        mLevelDisCntr = 120;
        mPaintUIFlag = true;
        createBufferFocused(mStartCol * 12 + (mStartBallSize >> 1), mStartRow * 12 + (mStartBallSize >> 1), mStartBallSize, 0, 0);
        mBall.setRespawn(mStartCol, mStartRow);
        mClearScreenFlag = true;
    }

    public void createBufferFocused(int i, int j, int k, int l, int i1)
    {
        int j1 = i / 12;
        int k1 = j / 12;
        if(i < 64)
        {
            scrollFlag = false;
            scrollOffset = 64;
            rightDrawEdge = 128;
            leftDrawEdge = 0;
            tileX = 0;
        } else
        if(i > mTileMapWidth * 12 - 64)
        {
            scrollFlag = false;
            scrollOffset = 92;
            rightDrawEdge = 156;
            leftDrawEdge = 28;
            tileX = mTileMapWidth - 13;
        } else
        {
            scrollFlag = true;
            scrollOffset = 0;
            rightDrawEdge = 143;
            leftDrawEdge = 15;
            tileX = j1 - 6;
        }
        divisorLine = 156;
        tileY = (k1 / 7) * 7;
        divTileX = tileX + 13;
        divTileY = tileY;
        int l1 = i - tileX * 12;
        int i2 = j - tileY * 12 - 1;
        mBall = new Ball(l1, i2, k, this);
        mBall.xSpeed = l;
        mBall.ySpeed = i1;
        createNewBuffer();
    }

    public void screenFlip()
    {
        int i = mBall.xPos / 12;
        int j = mBall.xPos - i * 12 - 6;
        cleanBuffer(false);
        if(mBall.yPos < 0)
        {
            tileY -= 7;
            divTileY -= 7;
            mBall.yPos += 84;
        } else
        if(mBall.yPos > 96)
        {
            tileY += 7;
            divTileY += 7;
            mBall.yPos -= 84;
        }
        if(!scrollFlag && tileX - (13 - divisorLine / 12) == 0)
        {
            if(divisorLine < mBall.xPos)
                mBall.xPos = mBall.xPos - divisorLine;
            else
                mBall.xPos = (mBall.xPos - divisorLine) + 156;
            tileX = 0;
            leftDrawEdge = 0;
            rightDrawEdge = 128;
            scrollOffset = 64;
        } else
        if(!scrollFlag)
        {
            tileX = mTileMapWidth - 13;
            leftDrawEdge = 28;
            rightDrawEdge = 156;
            if(mBall.xPos > divisorLine)
                mBall.xPos = 156 - ((divisorLine + 156) - mBall.xPos);
            else
                mBall.xPos = 156 - (divisorLine - mBall.xPos);
            scrollOffset = 92;
        } else
        {
            if(mBall.xPos > divisorLine)
                tileX = ((tileX - 13) + i) - 6;
            else
                tileX = (tileX + i) - 6;
            if(tileX < 0)
            {
                j += tileX * 12;
                tileX = 0;
            } else
            if(tileX > mTileMapWidth - 13 - 1)
            {
                j += (tileX - mTileMapWidth - 13 - 1) * 12;
                tileX = mTileMapWidth - 13 - 1;
            }
            leftDrawEdge = 14 + j;
            rightDrawEdge = 142 + j;
            mBall.xPos = 78 + j;
        }
        divTileX = tileX + 13;
        divisorLine = 156;
        createNewBuffer();
    }

    public void add2Score(int i)
    {
        mScore += i;
        mPaintUIFlag = true;
    }

    public void paint2Buffer()
    {
        if(mFullScreenGraphics == null)
            mFullScreenGraphics = mFullScreenBuffer.getGraphics();
        if(mBall != null)
            mBall.paint(mGameGraphics);
        if(hoopImageList != null)
            for(; !hoopImageList.isEmpty(); hoopYPosList.removeElementAt(0))
            {
                Image image = (Image)hoopImageList.firstElement();
                Integer integer = (Integer)hoopXPosList.firstElement();
                Integer integer1 = (Integer)hoopYPosList.firstElement();
                mGameGraphics.drawImage(image, integer.intValue(), integer1.intValue(), 20);
                hoopImageList.removeElementAt(0);
                hoopXPosList.removeElementAt(0);
            }

        if(mGameBuffer != null)
            if(leftDrawEdge < rightDrawEdge)
            {
                mFullScreenGraphics.drawImage(mGameBuffer, -leftDrawEdge, 0, 20);
            } else
            {
                mFullScreenGraphics.drawImage(mGameBuffer, -leftDrawEdge, 0, 20);
                mFullScreenGraphics.drawImage(mGameBuffer, 156 - leftDrawEdge, 0, 20);
            }
        if(mPaintUIFlag)
        {
            mFullScreenGraphics.setColor(0x853aa);
            mFullScreenGraphics.fillRect(0, 97, 128, 32);
            for(int i = 0; i < numLives; i++)
                mFullScreenGraphics.drawImage(mUILife, 5 + i * (mUILife.getWidth() - 1), 99, 20);

            for(int j = 0; j < mTotalNumRings - numRings; j++)
                mFullScreenGraphics.drawImage(mUIRing, 5 + j * (mUIRing.getWidth() - 4), 112, 20);

            mFullScreenGraphics.setColor(0xfffffe);
            mFullScreenGraphics.setFont(TEXT_FONT);
            mFullScreenGraphics.drawString(zeroString(mScore), 64, 100, 20);
            if(bonusCntrValue != 0)
            {
                mFullScreenGraphics.setColor(0xff9813);
                mFullScreenGraphics.fillRect(1, 128 - (3 * bonusCntrValue) / 30, 5, 128);
            }
            mPaintUIFlag = false;
        }
    }

    public void paint(Graphics g)
    {
        if(mSplashIndex != -1)
        {
            if(mSplashImage != null)
            {
                g.setColor(0);
                g.fillRect(0, 0, mWidth, mHeight);
                g.drawImage(mSplashImage, mWidth >> 1, mHeight >> 1, 3);
            }
        } else
        {
            if(mClearScreenFlag)
            {
                g.setColor(-1);
                g.fillRect(0, 0, mWidth, mHeight);
                mClearScreenFlag = false;
            }
            g.drawImage(mFullScreenBuffer, 0, 0, 20);
            if(mLevelDisCntr != 0)
            {
                g.setColor(0xfffffe);
                g.setFont(TEXT_FONT);
                g.drawString(mLevelNumStr, 44, 84, 20);
            }
        }
    }

    public void run()
    {
        if(mLoadLevelFlag)
        {
            createNewLevel();
            repaint();
            return;
        }
        if(mSplashIndex != -1)
        {
            if(mSplashImage == null || mSplashImage == null)
            {
                mIncomingCall = false;
                mUI.displayMainMenu();
            } else
            if(mSplashTimer > 30)
            {
                mSplashImage = null;
                Runtime.getRuntime().gc();
                switch(mSplashIndex)
                {
                case 0: // '\0'
                    mSplashIndex = 1;
                    try
                    {
                        mSplashImage = Image.createImage(SPLASH_NAME[mSplashIndex]);
                    }
                    catch(IOException ioexception)
                    {
                        mSplashImage = Image.createImage(1, 1);
                    }
                    repaint();
                    break;

                case 1: // '\001'
                    mSplashIndex = -1;
                    mIncomingCall = false;
                    mUI.displayMainMenu();
                    break;
                }
                mSplashTimer = 0;
            } else
            {
                mSplashTimer++;
            }
            repaint();
            return;
        }
        if(mLevelDisCntr != 0)
            mLevelDisCntr--;
        if(mBall.yPos < 0 || mBall.yPos > 96)
        {
            screenFlip();
        } else
        {
            cleanBuffer(true);
            mBall.update();
            testScroll(mBall.xPos, mBall.xOffset);
        }
        if(mBall.ballState == 1)
        {
            if(numLives < 0)
            {
                mUI.checkData();
                stop();
                mUI.gameOver(false);
                return;
            }
            int i = mBall.respawnX;
            int k = mBall.respawnY;
            int i1 = mBall.respawnSize;
            createBufferFocused(mBall.respawnX * 12 + (mBall.respawnSize >> 1), mBall.respawnY * 12 + (mBall.respawnSize >> 1), mBall.respawnSize, 0, 0);
            mBall.respawnX = i;
            mBall.respawnY = k;
            mBall.respawnSize = i1;
        }
        if(mNumMoveObj != 0)
            updateMovingSpikeObj();
        if(numRings == mTotalNumRings)
            mOpenExitFlag = true;
        if(mOpenExitFlag && mExitPos != -1)
        {
            int j = leftDrawEdge;
            int l = rightDrawEdge;
            if(mExitPos <= divisorLine)
            {
                if(leftDrawEdge > divisorLine)
                    j = leftDrawEdge - 156;
                if(rightDrawEdge > divisorLine)
                    l = rightDrawEdge - 156;
            }
            if(mExitPos > divisorLine)
            {
                if(leftDrawEdge < divisorLine)
                    j = leftDrawEdge + 156;
                if(rightDrawEdge < divisorLine)
                    l = rightDrawEdge + 156;
            }
            if(mExitPos >= j && mExitPos <= l)
            {
                if(mOpenFlag)
                {
                    mExitPos = -1;
                    mOpenExitFlag = false;
                } else
                {
                    openExit();
                }
                tileMap[mTopLeftExitTileRow][mTopLeftExitTileCol] |= 0x80;
                tileMap[mTopLeftExitTileRow][mTopLeftExitTileCol + 1] |= 0x80;
                tileMap[mTopLeftExitTileRow + 1][mTopLeftExitTileCol] |= 0x80;
                tileMap[mTopLeftExitTileRow + 1][mTopLeftExitTileCol + 1] |= 0x80;
                cleanBuffer(true);
            }
        }
        bonusCntrValue = 0;
        if(mBall.speedBonusCntr != 0 || mBall.gravBonusCntr != 0 || mBall.jumpBonusCntr != 0)
        {
            if(mBall.speedBonusCntr > bonusCntrValue)
                bonusCntrValue = mBall.speedBonusCntr;
            if(mBall.gravBonusCntr > bonusCntrValue)
                bonusCntrValue = mBall.gravBonusCntr;
            if(mBall.jumpBonusCntr > bonusCntrValue)
                bonusCntrValue = mBall.jumpBonusCntr;
            if(bonusCntrValue % 30 == 0 || bonusCntrValue == 1)
                mPaintUIFlag = true;
        }
        scrollBuffer(mBall.xPos, mBall.xOffset, 16);
        paint2Buffer();
        repaint();
        if(mLeaveGame)
        {
            mLeaveGame = false;
            mOpenExitFlag = false;
            mLoadLevelFlag = true;
            mLevelNum = 1 + mLevelNum;
            add2Score(5000);
            mUI.checkData();
            if(mLevelNum > 11)
            {
                mUI.gameOver(true);
            } else
            {
                mIncomingCall = false;
                mUI.displayLevelComplete();
                repaint();
            }
        }
    }

    public void keyPressed(int i)
    {
        if(mSplashIndex != -1)
        {
            mSplashTimer = 31;
            return;
        }
        if(mBall == null)
            return;
label0:
        switch(i)
        {
        case 49: // '1'
            if(!mCheat)
                break;
            mLoadLevelFlag = true;
            if(--mLevelNum < 1)
                mLevelNum = 11;
            break;

        case 51: // '3'
            if(!mCheat)
                break;
            mLoadLevelFlag = true;
            if(++mLevelNum > 11)
                mLevelNum = 1;
            break;

        case 55: // '7'
            if(mCheatSeq == 0 || mCheatSeq == 2)
                mCheatSeq++;
            else
                mCheatSeq = 0;
            break;

        case 56: // '8'
            if(mCheatSeq == 1 || mCheatSeq == 3)
            {
                mCheatSeq++;
                break;
            }
            if(mCheatSeq == 5)
            {
                mSoundHoop.play(1);
                mInvincible = true;
                mCheatSeq = 0;
            } else
            {
                mCheatSeq = 0;
            }
            break;

        case 57: // '9'
            if(mCheatSeq == 4)
            {
                mCheatSeq++;
                break;
            }
            if(mCheatSeq == 5)
            {
                mSoundPop.play(1);
                mCheat = true;
                mCheatSeq = 0;
            } else
            {
                mCheatSeq = 0;
            }
            break;

        case 35: // '#'
            if(mCheat)
                mBall.gravBonusCntr = 300;
            break;

        case -7: 
        case -6: 
            mIncomingCall = false;
            mUI.displayMainMenu();
            break;

        default:
            switch(getGameAction(i))
            {
            case 3: // '\003'
            case 4: // '\004'
            case 7: // '\007'
            default:
                break label0;

            case 1: // '\001'
                mBall.setDirection(8);
                break label0;

            case 6: // '\006'
                mBall.setDirection(4);
                break label0;

            case 2: // '\002'
                mBall.setDirection(1);
                break label0;

            case 5: // '\005'
                mBall.setDirection(2);
                break label0;

            case 8: // '\b'
                break;
            }
            if(mCheat)
                mLeaveGame = true;
            break;
        }
    }

    public void keyReleased(int i)
    {
        if(mBall == null)
            return;
        switch(getGameAction(i))
        {
        case 1: // '\001'
            mBall.releaseDirection(8);
            break;

        case 6: // '\006'
            mBall.releaseDirection(4);
            break;

        case 2: // '\002'
            mBall.releaseDirection(1);
            break;

        case 5: // '\005'
            mBall.releaseDirection(2);
            break;
        }
    }

    public static String zeroString(int i)
    {
        String s;
        if(i < 100)
            s = "0000000";
        else
        if(i < 1000)
            s = "00000";
        else
        if(i < 10000)
            s = "0000";
        else
        if(i < 0x186a0)
            s = "000";
        else
        if(i < 0xf4240)
            s = "00";
        else
        if(i < 0x989680)
            s = "0";
        else
            s = "";
        return s + i;
    }

    protected Sound loadSound(String s)
    {
        byte abyte0[] = new byte[100];
        Sound sound = null;
        DataInputStream datainputstream = new DataInputStream(getClass().getResourceAsStream(s));
        try
        {
            int i = datainputstream.read(abyte0);
            datainputstream.close();
            byte abyte1[] = new byte[i];
            System.arraycopy(abyte0, 0, abyte1, 0, i);
            sound = new Sound(abyte1, 1);
        }
        catch(IOException ioexception)
        {
            sound = new Sound(1000, 500L);
            sound.play(3);
        }
        return sound;
    }

    public void hideNotify()
    {
        if(mIncomingCall)
        {
            if(mBall != null)
                mBall.resetDirections();
            mUI.displayMainMenu();
        }
        mIncomingCall = true;
    }

    public void resetSpikes()
    {
        for(int i = 0; i < mUI.mSavedSpikeCount; i++)
        {
            mMODirection[i][0] = mUI.mSavedSpikeDirection[i][0];
            mMODirection[i][1] = mUI.mSavedSpikeDirection[i][1];
            mMOOffset[i][0] = mUI.mSavedSpikeOffset[i][0];
            mMOOffset[i][1] = mUI.mSavedSpikeOffset[i][1];
        }

        mUI.mSavedSpikeOffset = null;
        mUI.mSavedSpikeDirection = null;
        mUI.mSavedSpikeCount = 0;
    }

    public void resetTiles()
    {
        for(int i = 0; i < mTileMapHeight; i++)
        {
            for(int j = 0; j < mTileMapWidth; j++)
            {
                byte byte0 = (byte)(tileMap[i][j] & 0xff7f & 0xffffffbf);
                switch(byte0)
                {
                case 8: // '\b'
                case 9: // '\t'
                case 10: // '\n'
                case 11: // '\013'
                case 12: // '\f'
                case 17: // '\021'
                case 18: // '\022'
                case 19: // '\023'
                case 20: // '\024'
                case 25: // '\031'
                case 26: // '\032'
                case 27: // '\033'
                case 28: // '\034'
                default:
                    break;

                case 7: // '\007'
                case 29: // '\035'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0 | tileMap[i][j] & 0x40);
                    break;

                case 13: // '\r'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x11 | tileMap[i][j] & 0x40);
                    break;

                case 14: // '\016'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x12 | tileMap[i][j] & 0x40);
                    break;

                case 21: // '\025'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x19 | tileMap[i][j] & 0x40);
                    break;

                case 22: // '\026'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x1a | tileMap[i][j] & 0x40);
                    break;

                case 15: // '\017'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x13 | tileMap[i][j] & 0x40);
                    break;

                case 16: // '\020'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x14 | tileMap[i][j] & 0x40);
                    break;

                case 23: // '\027'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x1b | tileMap[i][j] & 0x40);
                    break;

                case 24: // '\030'
                    if(tileNotSavedAsActive(i, j, byte0))
                        tileMap[i][j] = (short)(0x1c | tileMap[i][j] & 0x40);
                    break;
                }
            }

        }

        mUI.mSavedTiles = null;
        mUI.mSavedTileCount = 0;
    }

    public boolean tileNotSavedAsActive(int i, int j, byte byte0)
    {
        for(int k = 0; k < mUI.mSavedTileCount; k++)
            if(mUI.mSavedTiles[k][0] == i && mUI.mSavedTiles[k][1] == j)
                return false;

        return true;
    }

}
