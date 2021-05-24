// Download by http://www.codefans.net
// Source File Name:   BounceUI.java

package com.nokia.mid.appl.boun;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

// Referenced classes of package com.nokia.mid.appl.boun:
//            BounceCanvas, TileCanvas, Local, Bounce, 
//            Ball

public class BounceUI
    implements CommandListener
{

    public Bounce mMidlet;
    public Display mDisplay;
    public BounceCanvas mCanvas;
    public int mState;
    public int mBestLevel;
    public int mBestScore;
    public boolean mNewBestScore;
    public int mLastScore;
    public byte mSavedValid;
    public byte mSavedLives;
    public byte mSavedRings;
    public byte mSavedLevel;
    public byte mSavedSize;
    public int mSavedScore;
    public int mSavedTileX;
    public int mSavedTileY;
    public int mSavedGlobalBallX;
    public int mSavedGlobalBallY;
    public int mSavedXSpeed;
    public int mSavedYSpeed;
    public int mSavedXPos;
    public int mSavedYPos;
    public int mSavedRespawnX;
    public int mSavedRespawnY;
    public int mSavedSpeedBonus;
    public int mSavedGravBonus;
    public int mSavedJumpBonus;
    public int mSavedTileCount;
    public int mSavedTiles[][];
    public int mSavedSpikeCount;
    public short mSavedSpikeOffset[][];
    public short mSavedSpikeDirection[][];
    private Command mOkayCmd;
    private Command mBackCmd;
    private Command mContinueCmd;
    private List mMainMenu;
    private List mNewGameMenu;
    private Form mTextPage;
    private int mSavedMenuItem;
    private static int MAIN_MENU_CONTINUE = 0;
    private static int MAIN_MENU_NEW_GAME = 1;
    private static int MAIN_MENU_HIGH_SCORE = 2;
    private static int MAIN_MENU_INSTRUCTIONS = 3;
    private static int MAIN_MENU_COUNT = 4;
    private String mMainMenuItems[];

    public BounceUI(Bounce bounce)
    {
        mState = 2;
        mSavedValid = -16;
        mMainMenuItems = new String[MAIN_MENU_COUNT];
        mMidlet = bounce;
        loadGameData();
        mCanvas = new BounceCanvas(this, 1);
        mCanvas.start();
        mDisplay = Display.getDisplay(mMidlet);
        mDisplay.setCurrent(mCanvas);
        mMainMenuItems[MAIN_MENU_CONTINUE] = Local.getText(8);
        mMainMenuItems[MAIN_MENU_NEW_GAME] = Local.getText(16);
        mMainMenuItems[MAIN_MENU_HIGH_SCORE] = Local.getText(12);
        mMainMenuItems[MAIN_MENU_INSTRUCTIONS] = Local.getText(13);
    }

    public void displayMainMenu()
    {
        mMainMenu = new List(Local.getText(10), 3);
        if(mBackCmd == null)
            mBackCmd = new Command(Local.getText(6), 2, 1);
        if(mState == 1 || mSavedValid != -16)
            mMainMenu.append(mMainMenuItems[0], null);
        for(int i = 1; i < mMainMenuItems.length; i++)
            mMainMenu.append(mMainMenuItems[i], null);

        mMainMenu.addCommand(mBackCmd);
        mMainMenu.setCommandListener(this);
        if(mCanvas.mSplashIndex != -1)
        {
            mCanvas.mSplashIndex = -1;
            mCanvas.mSplashImage = null;
        }
        if(mState == 1 || mSavedValid != -16)
            mMainMenu.setSelectedIndex(MAIN_MENU_CONTINUE, true);
        else
            mMainMenu.setSelectedIndex(mSavedMenuItem, true);
        mCanvas.stop();
        mDisplay.setCurrent(mMainMenu);
    }

    public void displayNewGameMenu()
    {
        String as[] = new String[mBestLevel];
        String as1[] = new String[1];
        for(int i = 0; i < mBestLevel; i++)
        {
            as1[0] = String.valueOf(i + 1);
            as[i] = Local.getText(14, as1);
        }

        mNewGameMenu = new List(Local.getText(16), 3, as, null);
        mNewGameMenu.addCommand(mBackCmd);
        mNewGameMenu.setCommandListener(this);
        mDisplay.setCurrent(mNewGameMenu);
    }

    public void displayGame(boolean flag, int i)
    {
        mDisplay.setCurrent(mCanvas);
        if(flag)
            mCanvas.resetGame(i, 0, 3);
        mCanvas.start();
        mState = 1;
    }

    public void displayHighScore()
    {
        mTextPage = new Form(Local.getText(12));
        mTextPage.append(String.valueOf(mBestScore));
        mTextPage.addCommand(mBackCmd);
        mTextPage.setCommandListener(this);
        mDisplay.setCurrent(mTextPage);
    }

    public void displayInstructions()
    {
        mTextPage = new Form(Local.getText(13));
        mTextPage.append(Local.getText(0));
        mTextPage.append(Local.getText(1));
        mTextPage.append(Local.getText(2));
        mTextPage.append(Local.getText(3));
        mTextPage.append(Local.getText(4));
        mTextPage.append(Local.getText(5));
        mTextPage.addCommand(mBackCmd);
        mTextPage.setCommandListener(this);
        mDisplay.setCurrent(mTextPage);
        mTextPage = null;
    }

    public void displayGameOver(boolean flag)
    {
        mCanvas.stop();
        if(mOkayCmd == null)
            mOkayCmd = new Command(Local.getText(19), 4, 1);
        mTextPage = new Form(Local.getText(11));
        if(flag)
            mTextPage.append(Local.getText(7));
        else
            mTextPage.append(Local.getText(11));
        mTextPage.append("\n\n");
        if(mNewBestScore)
        {
            mTextPage.append(Local.getText(17));
            mTextPage.append("\n\n");
        }
        mTextPage.append(String.valueOf(mLastScore));
        mTextPage.addCommand(mOkayCmd);
        mTextPage.setCommandListener(this);
        mDisplay.setCurrent(mTextPage);
        mTextPage = null;
    }

    public void displayLevelComplete()
    {
        mCanvas.stop();
        if(mContinueCmd == null)
            mContinueCmd = new Command(Local.getText(8), 4, 1);
        mTextPage = new Form("");
        mTextPage.append(((TileCanvas) (mCanvas)).mLevelCompletedStr);
        mTextPage.append("\n\n");
        mTextPage.append("" + mLastScore + "\n");
        mTextPage.addCommand(mContinueCmd);
        mTextPage.setCommandListener(this);
        mDisplay.setCurrent(mTextPage);
        mTextPage = null;
    }

    public void commandAction(Command command, Displayable displayable)
    {
        if(command == List.SELECT_COMMAND)
        {
            if(displayable == mNewGameMenu)
            {
                displayGame(true, mNewGameMenu.getSelectedIndex() + 1);
            } else
            {
                String s = mMainMenu.getString(mMainMenu.getSelectedIndex());
                mSavedMenuItem = mMainMenu.getSelectedIndex();
                if(s.equals(mMainMenuItems[MAIN_MENU_CONTINUE]))
                {
                    if(mState == 1)
                        displayGame(false, ((TileCanvas) (mCanvas)).mLevelNum);
                    else
                    if(mSavedValid != -16)
                    {
                        mDisplay.setCurrent(mCanvas);
                        if(mSavedValid == -6)
                            mCanvas.resetGame(mSavedGlobalBallX + (mSavedSize >> 1), mSavedGlobalBallY + (mSavedSize >> 1));
                        else
                            mCanvas.resetGame(mSavedLevel, mSavedScore, mSavedLives);
                        mSavedTiles = null;
                        mCanvas.start();
                        mState = 1;
                    }
                } else
                if(s.equals(mMainMenuItems[MAIN_MENU_NEW_GAME]))
                {
                    if(mState != 4)
                        if(mBestLevel > 1)
                        {
                            displayNewGameMenu();
                        } else
                        {
                            mState = 4;
                            mNewBestScore = false;
                            displayGame(true, 1);
                        }
                } else
                if(s.equals(mMainMenuItems[MAIN_MENU_HIGH_SCORE]))
                    displayHighScore();
                else
                if(s.equals(mMainMenuItems[MAIN_MENU_INSTRUCTIONS]))
                    displayInstructions();
            }
        } else
        if(command == mBackCmd || command == mOkayCmd)
        {
            if(mDisplay.getCurrent() == mMainMenu)
            {
                mMidlet.destroyApp(true);
                mMidlet.notifyDestroyed();
            } else
            {
                displayMainMenu();
            }
        } else
        if(command == mContinueCmd)
            displayGame(false, 0);
    }

    public void loadGameData()
    {
        byte abyte0[] = new byte[1];
        byte abyte2[] = new byte[4];
        byte abyte4[] = new byte[255];
        try
        {
            RecordStore recordstore = RecordStore.openRecordStore("bounceRMS", true);
            if(recordstore.getNumRecords() != 3)
            {
                recordstore.addRecord(abyte0, 0, abyte0.length);
                recordstore.addRecord(abyte2, 0, abyte2.length);
                recordstore.addRecord(abyte4, 0, abyte4.length);
            } else
            {
                byte abyte1[] = recordstore.getRecord(1);
                byte abyte3[] = recordstore.getRecord(2);
                byte abyte5[] = recordstore.getRecord(3);
                ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte1);
                DataInputStream datainputstream = new DataInputStream(bytearrayinputstream);
                mBestLevel = datainputstream.readByte();
                bytearrayinputstream = new ByteArrayInputStream(abyte3);
                datainputstream = new DataInputStream(bytearrayinputstream);
                mBestScore = datainputstream.readInt();
                bytearrayinputstream = new ByteArrayInputStream(abyte5);
                datainputstream = new DataInputStream(bytearrayinputstream);
                mSavedValid = datainputstream.readByte();
                mSavedLives = datainputstream.readByte();
                mSavedRings = datainputstream.readByte();
                mSavedLevel = datainputstream.readByte();
                mSavedSize = datainputstream.readByte();
                mSavedScore = datainputstream.readInt();
                mSavedTileX = datainputstream.readInt();
                mSavedTileY = datainputstream.readInt();
                mSavedGlobalBallX = datainputstream.readInt();
                mSavedGlobalBallY = datainputstream.readInt();
                mSavedXSpeed = datainputstream.readInt();
                mSavedYSpeed = datainputstream.readInt();
                mSavedXPos = datainputstream.readInt();
                mSavedYPos = datainputstream.readInt();
                mSavedRespawnX = datainputstream.readInt();
                mSavedRespawnY = datainputstream.readInt();
                mSavedSpeedBonus = datainputstream.readInt();
                mSavedGravBonus = datainputstream.readInt();
                mSavedJumpBonus = datainputstream.readInt();
                mSavedTileCount = datainputstream.readByte();
                mSavedTiles = new int[mSavedTileCount][3];
                for(int i = 0; i < mSavedTileCount; i++)
                {
                    mSavedTiles[i][0] = datainputstream.readShort();
                    mSavedTiles[i][1] = datainputstream.readShort();
                    mSavedTiles[i][2] = datainputstream.readByte();
                }

                mSavedSpikeCount = datainputstream.readByte();
                mSavedSpikeOffset = new short[mSavedSpikeCount][2];
                mSavedSpikeDirection = new short[mSavedSpikeCount][2];
                for(int j = 0; j < mSavedSpikeCount; j++)
                {
                    mSavedSpikeOffset[j][0] = datainputstream.readShort();
                    mSavedSpikeOffset[j][1] = datainputstream.readShort();
                    mSavedSpikeDirection[j][0] = datainputstream.readShort();
                    mSavedSpikeDirection[j][1] = datainputstream.readShort();
                }

            }
        }
        catch(Exception exception) { }
    }

    public void saveGameData(int i)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        try
        {
            switch(i)
            {
            default:
                break;

            case 1: // '\001'
                dataoutputstream.writeByte(mBestLevel);
                break;

            case 2: // '\002'
                dataoutputstream.writeInt(mBestScore);
                break;

            case 3: // '\003'
                if(mDisplay.getCurrent() == mCanvas && ((TileCanvas) (mCanvas)).mGameTimer != null)
                    dataoutputstream.writeByte(-6);
                else
                if(mCanvas != null && mCanvas.numLives < 0)
                    dataoutputstream.writeByte(-16);
                else
                    dataoutputstream.writeByte(-70);
                dataoutputstream.writeByte(mCanvas.numLives);
                dataoutputstream.writeByte(mCanvas.numRings);
                dataoutputstream.writeByte(((TileCanvas) (mCanvas)).mLevelNum);
                dataoutputstream.writeByte(mCanvas.mBall.mBallSize);
                dataoutputstream.writeInt(mCanvas.mScore);
                dataoutputstream.writeInt(((TileCanvas) (mCanvas)).tileX);
                dataoutputstream.writeInt(((TileCanvas) (mCanvas)).tileY);
                dataoutputstream.writeInt(mCanvas.mBall.globalBallX);
                dataoutputstream.writeInt(mCanvas.mBall.globalBallY);
                dataoutputstream.writeInt(mCanvas.mBall.xSpeed);
                dataoutputstream.writeInt(mCanvas.mBall.ySpeed);
                dataoutputstream.writeInt(mCanvas.mBall.xPos);
                dataoutputstream.writeInt(mCanvas.mBall.yPos);
                dataoutputstream.writeInt(mCanvas.mBall.respawnX);
                dataoutputstream.writeInt(mCanvas.mBall.respawnY);
                dataoutputstream.writeInt(mCanvas.mBall.speedBonusCntr);
                dataoutputstream.writeInt(mCanvas.mBall.gravBonusCntr);
                dataoutputstream.writeInt(mCanvas.mBall.jumpBonusCntr);
                int ai[][] = new int[50][3];
                int j = 0;
                for(int k = 0; k < ((TileCanvas) (mCanvas)).mTileMapHeight; k++)
                {
                    for(int l = 0; l < ((TileCanvas) (mCanvas)).mTileMapWidth; l++)
                    {
                        byte byte0 = (byte)(((TileCanvas) (mCanvas)).tileMap[k][l] & 0xff7f & 0xffffffbf);
                        if(byte0 == 7 || byte0 == 29 || byte0 == 13 || byte0 == 14 || byte0 == 21 || byte0 == 22 || byte0 == 15 || byte0 == 16 || byte0 == 23 || byte0 == 24)
                        {
                            ai[j][0] = k;
                            ai[j][1] = l;
                            ai[j][2] = byte0;
                            j++;
                        }
                    }

                }

                dataoutputstream.writeByte(j);
                for(int i1 = 0; i1 < j; i1++)
                {
                    dataoutputstream.writeShort(ai[i1][0]);
                    dataoutputstream.writeShort(ai[i1][1]);
                    dataoutputstream.writeByte(ai[i1][2]);
                }

                ai = null;
                dataoutputstream.writeByte(((TileCanvas) (mCanvas)).mNumMoveObj);
                for(int j1 = 0; j1 < ((TileCanvas) (mCanvas)).mNumMoveObj; j1++)
                {
                    dataoutputstream.writeShort(((TileCanvas) (mCanvas)).mMOOffset[j1][0]);
                    dataoutputstream.writeShort(((TileCanvas) (mCanvas)).mMOOffset[j1][1]);
                    dataoutputstream.writeShort(((TileCanvas) (mCanvas)).mMODirection[j1][0]);
                    dataoutputstream.writeShort(((TileCanvas) (mCanvas)).mMODirection[j1][1]);
                }

                break;
            }
            RecordStore recordstore = RecordStore.openRecordStore("bounceRMS", true);
            recordstore.setRecord(i, bytearrayoutputstream.toByteArray(), 0, bytearrayoutputstream.size());
        }
        catch(Exception exception) { }
    }

    public void checkData()
    {
        if(((TileCanvas) (mCanvas)).mLevelNum > mBestLevel)
        {
            mBestLevel = Math.min(((TileCanvas) (mCanvas)).mLevelNum, 11);
            saveGameData(1);
        }
        if(mCanvas.mScore > mBestScore)
        {
            mBestScore = mCanvas.mScore;
            mNewBestScore = true;
            saveGameData(2);
        }
        mLastScore = mCanvas.mScore;
    }

    public void gameOver(boolean flag)
    {
        mState = 3;
        mSavedValid = -16;
        mCanvas.mIncomingCall = false;
        displayGameOver(flag);
    }

}
