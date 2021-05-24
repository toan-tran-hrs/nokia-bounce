// Source File Name:   Ball.java
// Download by http://www.codefans.net
package com.nokia.mid.appl.boun;

import com.nokia.mid.sound.Sound;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// Referenced classes of package com.nokia.mid.appl.boun:
//            TileCanvas, BounceCanvas

public class Ball
{

    private boolean mDebugCD;
    public int xPos;
    public int yPos;
    public int globalBallX;
    public int globalBallY;
    public int xOffset;
    public int xSpeed;
    public int ySpeed;
    public int direction;
    public int mBallSize;
    public int mHalfBallSize;
    public int respawnX;
    public int respawnY;
    public int respawnSize;
    public int ballState;
    public int jumpOffset;
    public int speedBonusCntr;
    public int gravBonusCntr;
    public int jumpBonusCntr;
    public boolean mGroundedFlag;
    public boolean mCDRubberFlag;
    public boolean mCDRampFlag;
    public int slideCntr;
    public static final byte TRI_TILE_DATA[][] = {
        {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 1
        }, {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            1, 1
        }, {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
            1, 1
        }, {
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 
            1, 1
        }, {
            0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 
            1, 1
        }, {
            0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 
            1, 1
        }, {
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }
    };
    public static final byte SMALL_BALL_DATA[][] = {
        {
            0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 
            0, 0
        }, {
            0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 
            0, 0
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 0
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 0
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 0
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 0
        }, {
            0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 
            0, 0
        }, {
            0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 
            0, 0
        }
    };
    public static final byte LARGE_BALL_DATA[][] = {
        {
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 
            1, 0, 0, 0, 0, 0
        }, {
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 0, 0, 0
        }, {
            0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 0, 0
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 0
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 0
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 1
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 0
        }, {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 0
        }, {
            0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 1, 0, 0
        }, {
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 1, 0, 0, 0
        }, {
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 
            1, 0, 0, 0, 0, 0
        }
    };
    public BounceCanvas mCanvas;
    public Image mBallImage;
    public Image poppedImage;
    public Image largeBallImage;
    public Image smallBallImage;
    private int popCntr;

    public Ball(int i, int j, int k, BounceCanvas bouncecanvas)
    {
        mDebugCD = false;
        xPos = i;
        yPos = j;
        globalBallX = 0;
        globalBallY = 0;
        xSpeed = 0;
        ySpeed = 0;
        xOffset = 0;
        mCanvas = bouncecanvas;
        jumpOffset = 0;
        mGroundedFlag = false;
        mCDRubberFlag = false;
        mCDRampFlag = false;
        popCntr = 0;
        speedBonusCntr = 0;
        gravBonusCntr = 0;
        jumpBonusCntr = 0;
        slideCntr = 0;
        ballState = 0;
        direction = 0;
        mCanvas.setBallImages(this);
        mBallSize = k;
        if(mBallSize == 12)
        {
            mHalfBallSize = 6;
            mBallImage = smallBallImage;
        } else
        {
            mHalfBallSize = 8;
            mBallImage = largeBallImage;
        }
    }

    public void setRespawn(int i, int j)
    {
        respawnX = i;
        respawnY = j;
        respawnSize = mBallSize;
    }

    public void setDirection(int i)
    {
        if(i == 8 || i == 4 || i == 2 || i == 1)
            direction |= i;
    }

    public void releaseDirection(int i)
    {
        if(i == 8 || i == 4 || i == 2 || i == 1)
            direction &= ~i;
    }

    public void resetDirections()
    {
        direction &= 0xfffffff0;
    }

    public boolean collisionDetection(int i, int j)
    {
        byte byte0 = 0;
        if(j < 0)
            byte0 = 12;
        int k = (i - mHalfBallSize) / 12;
        int l = (j - byte0 - mHalfBallSize) / 12;
        globalBallX = i - mHalfBallSize;
        globalBallY = j - mHalfBallSize;
        if(xPos < ((TileCanvas) (mCanvas)).divisorLine)
        {
            globalBallX += ((TileCanvas) (mCanvas)).tileX * 12;
            globalBallY += ((TileCanvas) (mCanvas)).tileY * 12;
        } else
        {
            globalBallX += (((TileCanvas) (mCanvas)).divTileX - 13) * 12 - ((TileCanvas) (mCanvas)).divisorLine;
            globalBallY += ((TileCanvas) (mCanvas)).divTileY * 12;
        }
        int i1 = ((i - 1) + mHalfBallSize) / 12 + 1;
        int j1 = ((j - byte0 - 1) + mHalfBallSize) / 12 + 1;
        boolean flag = true;
        for(int k1 = k; k1 < i1; k1++)
        {
            for(int l1 = l; l1 < j1; l1++)
                if(k1 * 12 > 156)
                    flag = testTile(((TileCanvas) (mCanvas)).tileY + l1, (((TileCanvas) (mCanvas)).tileX + k1) - 13, flag);
                else
                if(xPos < ((TileCanvas) (mCanvas)).divisorLine)
                {
                    flag = testTile(((TileCanvas) (mCanvas)).tileY + l1, ((TileCanvas) (mCanvas)).tileX + k1, flag);
                } else
                {
                    int i2 = ((TileCanvas) (mCanvas)).divTileX - 13 - ((TileCanvas) (mCanvas)).divisorLine / 12;
                    flag = testTile(((TileCanvas) (mCanvas)).divTileY + l1, i2 + k1, flag);
                }

        }

        return flag;
    }

    public void enlargeBall()
    {
        int i = 2;
        mBallSize = 16;
        mHalfBallSize = 8;
        mBallImage = largeBallImage;
        for(boolean flag = false; !flag;)
        {
            flag = true;
            if(collisionDetection(xPos, yPos - i))
                yPos -= i;
            else
            if(collisionDetection(xPos - i, yPos - i))
            {
                xPos -= i;
                yPos -= i;
            } else
            if(collisionDetection(xPos + i, yPos - i))
            {
                xPos += i;
                yPos -= i;
            } else
            if(collisionDetection(xPos, yPos + i))
                yPos += i;
            else
            if(collisionDetection(xPos - i, yPos + i))
            {
                xPos -= i;
                yPos += i;
            } else
            if(collisionDetection(xPos + i, yPos + i))
            {
                xPos += i;
                yPos += i;
            } else
            {
                flag = false;
                i++;
            }
        }

    }

    public void shrinkBall()
    {
        byte byte0 = 2;
        mBallSize = 12;
        mHalfBallSize = 6;
        mBallImage = smallBallImage;
        if(collisionDetection(xPos, yPos + byte0))
            yPos += byte0;
        else
        if(collisionDetection(xPos, yPos - byte0))
            yPos -= byte0;
    }

    public void popBall()
    {
        if(!mCanvas.mInvincible)
        {
            popCntr = 5;
            ballState = 2;
            xOffset = 0;
            mCanvas.numLives--;
            speedBonusCntr = 0;
            gravBonusCntr = 0;
            jumpBonusCntr = 0;
            mCanvas.mPaintUIFlag = true;
            mCanvas.mSoundPop.play(1);
        }
    }

    public void addRing()
    {
        mCanvas.add2Score(500);
        mCanvas.numRings++;
        mCanvas.mPaintUIFlag = true;
    }

    public void redirectBall(int i)
    {
        int j = xSpeed;
        switch(i)
        {
        case 35: // '#'
            xSpeed = xSpeed <= -ySpeed ? ySpeed : xSpeed;
            ySpeed = j;
            break;

        case 37: // '%'
            xSpeed = -xSpeed <= ySpeed ? ySpeed : xSpeed;
            ySpeed = j;
            break;

        case 34: // '"'
            xSpeed = xSpeed >= ySpeed ? -ySpeed : xSpeed;
            ySpeed = -j;
            break;

        case 36: // '$'
            xSpeed = xSpeed <= ySpeed ? -ySpeed : xSpeed;
            ySpeed = -j;
            break;

        case 31: // '\037'
            xSpeed = xSpeed <= -ySpeed ? ySpeed >> 1 : xSpeed;
            ySpeed = j;
            break;

        case 33: // '!'
            xSpeed = -xSpeed <= ySpeed ? ySpeed >> 1 : xSpeed;
            ySpeed = j;
            break;

        case 30: // '\036'
            xSpeed = xSpeed >= ySpeed ? -(ySpeed >> 1) : xSpeed;
            ySpeed = -j;
            break;

        case 32: // ' '
            xSpeed = xSpeed <= ySpeed ? -(ySpeed >> 1) : xSpeed;
            ySpeed = -j;
            break;
        }
    }

    public boolean squareCollide(int i, int j)
    {
        int k = j * 12;
        int l = i * 12;
        int i1 = globalBallX - k;
        int j1 = globalBallY - l;
        int k1;
        int l1;
        if(i1 >= 0)
        {
            k1 = i1;
            l1 = 12;
        } else
        {
            k1 = 0;
            l1 = mBallSize + i1;
        }
        int i2;
        int j2;
        if(j1 >= 0)
        {
            i2 = j1;
            j2 = 12;
        } else
        {
            i2 = 0;
            j2 = mBallSize + j1;
        }
        byte abyte0[][];
        if(mBallSize == 16)
            abyte0 = LARGE_BALL_DATA;
        else
            abyte0 = SMALL_BALL_DATA;
        if(l1 > 12)
            l1 = 12;
        if(j2 > 12)
            j2 = 12;
        for(int k2 = k1; k2 < l1; k2++)
        {
            for(int l2 = i2; l2 < j2; l2++)
                if(abyte0[l2 - j1][k2 - i1] != 0)
                    return true;

        }

        return false;
    }

    public boolean triangleCollide(int i, int j, int k)
    {
        int l = j * 12;
        int i1 = i * 12;
        int j1 = globalBallX - l;
        int k1 = globalBallY - i1;
        byte byte0 = 0;
        byte byte1 = 0;
        switch(k)
        {
        case 30: // '\036'
        case 34: // '"'
            byte1 = 11;
            byte0 = 11;
            break;

        case 31: // '\037'
        case 35: // '#'
            byte1 = 11;
            break;

        case 33: // '!'
        case 37: // '%'
            byte0 = 11;
            break;
        }
        int l1;
        int i2;
        if(j1 >= 0)
        {
            l1 = j1;
            i2 = 12;
        } else
        {
            l1 = 0;
            i2 = mBallSize + j1;
        }
        int j2;
        int k2;
        if(k1 >= 0)
        {
            j2 = k1;
            k2 = 12;
        } else
        {
            j2 = 0;
            k2 = mBallSize + k1;
        }
        byte abyte0[][];
        if(mBallSize == 16)
            abyte0 = LARGE_BALL_DATA;
        else
            abyte0 = SMALL_BALL_DATA;
        if(i2 > 12)
            i2 = 12;
        if(k2 > 12)
            k2 = 12;
        for(int l2 = l1; l2 < i2; l2++)
        {
            for(int i3 = j2; i3 < k2; i3++)
                if((TRI_TILE_DATA[Math.abs(i3 - byte1)][Math.abs(l2 - byte0)] & abyte0[i3 - k1][l2 - j1]) != 0)
                {
                    if(!mGroundedFlag)
                        redirectBall(k);
                    return true;
                }

        }

        return false;
    }

    public boolean thinCollide(int i, int j, int k)
    {
        int l = j * 12;
        int i1 = i * 12;
        int j1 = l + 12;
        int k1 = i1 + 12;
        switch(k)
        {
        case 3: // '\003'
        case 5: // '\005'
        case 9: // '\t'
        case 13: // '\r'
        case 14: // '\016'
        case 17: // '\021'
        case 18: // '\022'
        case 21: // '\025'
        case 22: // '\026'
        case 43: // '+'
        case 45: // '-'
            l += 4;
            j1 -= 4;
            break;

        case 4: // '\004'
        case 6: // '\006'
        case 15: // '\017'
        case 16: // '\020'
        case 19: // '\023'
        case 20: // '\024'
        case 23: // '\027'
        case 24: // '\030'
        case 44: // ','
        case 46: // '.'
            i1 += 4;
            k1 -= 4;
            break;
        }
        return TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, l, i1, j1, k1);
    }

    public boolean edgeCollide(int i, int j, int k)
    {
        int l = j * 12;
        int i1 = i * 12;
        int j1 = l + 12;
        int k1 = i1 + 12;
        boolean flag = false;
        switch(k)
        {
        case 15: // '\017'
        case 19: // '\023'
        case 23: // '\027'
        case 27: // '\033'
            i1 += 6;
            k1 -= 6;
            j1 -= 11;
            flag = TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, l, i1, j1, k1);
            break;

        case 16: // '\020'
        case 20: // '\024'
        case 24: // '\030'
        case 28: // '\034'
            i1 += 6;
            k1 -= 6;
            l += 11;
            flag = TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, l, i1, j1, k1);
            break;

        case 13: // '\r'
        case 17: // '\021'
            l += 6;
            j1 -= 6;
            k1 -= 11;
            flag = TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, l, i1, j1, k1);
            break;

        case 21: // '\025'
        case 25: // '\031'
            k1 = i1;
            i1--;
            l += 6;
            j1 -= 6;
            flag = TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, l, i1, j1, k1);
            break;

        case 14: // '\016'
        case 18: // '\022'
        case 22: // '\026'
        case 26: // '\032'
            l += 6;
            j1 -= 6;
            i1 += 11;
            flag = TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, l, i1, j1, k1);
            break;
        }
        return flag;
    }

    public boolean testTile(int i, int j, boolean flag)
    {
        if(i >= ((TileCanvas) (mCanvas)).mTileMapHeight || i < 0 || j >= ((TileCanvas) (mCanvas)).mTileMapWidth || j < 0)
        {
            flag = false;
            return false;
        }
        if(ballState == 2)
            return false;
        int k = ((TileCanvas) (mCanvas)).tileMap[i][j] & 0x40;
        int l = ((TileCanvas) (mCanvas)).tileMap[i][j] & 0xffffffbf & 0xffffff7f;
        Sound sound = null;
        switch(l)
        {
        case 1: // '\001'
            if(squareCollide(i, j))
                flag = false;
            else
                mCDRampFlag = true;
            break;

        case 2: // '\002'
            if(squareCollide(i, j))
            {
                mCDRubberFlag = true;
                flag = false;
            } else
            {
                mCDRampFlag = true;
            }
            break;

        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
            if(triangleCollide(i, j, l))
            {
                mCDRubberFlag = true;
                flag = false;
                mCDRampFlag = true;
            }
            break;

        case 30: // '\036'
        case 31: // '\037'
        case 32: // ' '
        case 33: // '!'
            if(triangleCollide(i, j, l))
            {
                flag = false;
                mCDRampFlag = true;
            }
            break;

        case 10: // '\n'
            int i1 = mCanvas.findSpikeIndex(j, i);
            if(i1 != -1)
            {
                int j1 = ((TileCanvas) (mCanvas)).mMOTopLeft[i1][0] * 12 + ((TileCanvas) (mCanvas)).mMOOffset[i1][0];
                int k1 = ((TileCanvas) (mCanvas)).mMOTopLeft[i1][1] * 12 + ((TileCanvas) (mCanvas)).mMOOffset[i1][1];
                if(TileCanvas.rectCollide(globalBallX, globalBallY, globalBallX + mBallSize, globalBallY + mBallSize, j1, k1, j1 + 24, k1 + 24))
                {
                    flag = false;
                    popBall();
                }
            }
            break;

        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
            if(thinCollide(i, j, l))
            {
                flag = false;
                popBall();
            }
            break;

        case 7: // '\007'
            mCanvas.add2Score(200);
            ((TileCanvas) (mCanvas)).tileMap[respawnY][respawnX] = 128;
            setRespawn(j, i);
            ((TileCanvas) (mCanvas)).tileMap[i][j] = 136;
            sound = mCanvas.mSoundPickup;
            break;

        case 23: // '\027'
            if(thinCollide(i, j, l))
                if(edgeCollide(i, j, l))
                {
                    flag = false;
                } else
                {
                    addRing();
                    ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x9b | k);
                    ((TileCanvas) (mCanvas)).tileMap[i][j + 1] = (short)(0x9c | k);
                    sound = mCanvas.mSoundHoop;
                }
            break;

        case 15: // '\017'
            if(thinCollide(i, j, l))
                if(mBallSize == 16)
                {
                    flag = false;
                } else
                {
                    if(edgeCollide(i, j, l))
                        flag = false;
                    addRing();
                    ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x93 | k);
                    ((TileCanvas) (mCanvas)).tileMap[i][j + 1] = (short)(0x94 | k);
                    sound = mCanvas.mSoundHoop;
                }
            break;

        case 24: // '\030'
            if(thinCollide(i, j, l))
            {
                if(edgeCollide(i, j, l))
                    flag = false;
                addRing();
                ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x9c | k);
                ((TileCanvas) (mCanvas)).tileMap[i][j - 1] = (short)(0x9b | k);
                sound = mCanvas.mSoundHoop;
            }
            break;

        case 16: // '\020'
            if(thinCollide(i, j, l))
                if(mBallSize == 16)
                {
                    flag = false;
                } else
                {
                    if(edgeCollide(i, j, l))
                        flag = false;
                    addRing();
                    ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x94 | k);
                    ((TileCanvas) (mCanvas)).tileMap[i][j - 1] = (short)(0x93 | k);
                    sound = mCanvas.mSoundHoop;
                }
            break;

        case 21: // '\025'
            if(thinCollide(i, j, l))
            {
                if(edgeCollide(i, j, l))
                    flag = false;
                addRing();
                ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x99 | k);
                ((TileCanvas) (mCanvas)).tileMap[i + 1][j] = (short)(0x9a | k);
                sound = mCanvas.mSoundHoop;
            }
            break;

        case 13: // '\r'
            if(thinCollide(i, j, l))
                if(mBallSize == 16)
                {
                    flag = false;
                } else
                {
                    if(edgeCollide(i, j, l))
                        flag = false;
                    addRing();
                    ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x91 | k);
                    ((TileCanvas) (mCanvas)).tileMap[i + 1][j] = (short)(0x92 | k);
                    sound = mCanvas.mSoundHoop;
                }
            break;

        case 22: // '\026'
            if(thinCollide(i, j, l))
            {
                addRing();
                ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x9a | k);
                ((TileCanvas) (mCanvas)).tileMap[i - 1][j] = (short)(0x99 | k);
                sound = mCanvas.mSoundHoop;
            }
            break;

        case 14: // '\016'
            if(thinCollide(i, j, l))
                if(mBallSize == 16)
                {
                    flag = false;
                } else
                {
                    addRing();
                    ((TileCanvas) (mCanvas)).tileMap[i][j] = (short)(0x92 | k);
                    ((TileCanvas) (mCanvas)).tileMap[i - 1][j] = (short)(0x91 | k);
                    sound = mCanvas.mSoundHoop;
                }
            break;

        case 17: // '\021'
        case 19: // '\023'
        case 20: // '\024'
            if(thinCollide(i, j, l))
                if(mBallSize == 16)
                    flag = false;
                else
                if(edgeCollide(i, j, l))
                    flag = false;
            break;

        case 25: // '\031'
        case 27: // '\033'
        case 28: // '\034'
            if(edgeCollide(i, j, l))
                flag = false;
            break;

        case 18: // '\022'
            if(thinCollide(i, j, l) && mBallSize == 16)
                flag = false;
            break;

        case 9: // '\t'
            if(thinCollide(i, j, l))
                if(((TileCanvas) (mCanvas)).mOpenFlag)
                {
                    mCanvas.mLeaveGame = true;
                    sound = mCanvas.mSoundPickup;
                } else
                {
                    flag = false;
                }
            break;

        case 29: // '\035'
            mCanvas.add2Score(1000);
            if(mCanvas.numLives < 5)
            {
                mCanvas.numLives++;
                mCanvas.mPaintUIFlag = true;
            }
            ((TileCanvas) (mCanvas)).tileMap[i][j] = 128;
            sound = mCanvas.mSoundPickup;
            break;

        case 39: // '\''
        case 40: // '('
        case 41: // ')'
        case 42: // '*'
            flag = false;
            if(mBallSize == 16)
                shrinkBall();
            break;

        case 43: // '+'
        case 44: // ','
        case 45: // '-'
        case 46: // '.'
            if(thinCollide(i, j, l))
            {
                flag = false;
                if(mBallSize == 12)
                    enlargeBall();
            }
            break;

        case 47: // '/'
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
            gravBonusCntr = 300;
            sound = mCanvas.mSoundPickup;
            flag = false;
            break;

        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
            jumpBonusCntr = 300;
            sound = mCanvas.mSoundPickup;
            flag = false;
            break;

        case 38: // '&'
            speedBonusCntr = 300;
            sound = mCanvas.mSoundPickup;
            flag = false;
            break;
        }
        if(sound != null)
            sound.play(1);
        return flag;
    }

    public void update()
    {
        int i = xPos;
        int j = 0;
        int k = 0;
        byte byte0 = 0;
        boolean flag = false;
        if(ballState == 2)
        {
            xOffset = 0;
            popCntr--;
            if(popCntr == 0)
            {
                ballState = 1;
                if(mCanvas.numLives < 0)
                    mCanvas.mLeaveGame = true;
            }
            return;
        }
        int l = xPos / 12;
        int i1 = yPos / 12;
        if(xPos >= 156)
        {
            l = (((TileCanvas) (mCanvas)).tileX + l) - 13;
            i1 = ((TileCanvas) (mCanvas)).tileY + i1;
        } else
        if(xPos < ((TileCanvas) (mCanvas)).divisorLine)
        {
            l = ((TileCanvas) (mCanvas)).tileX + l;
            i1 = ((TileCanvas) (mCanvas)).tileY + i1;
        } else
        {
            l = (((TileCanvas) (mCanvas)).divTileX - 13 - ((TileCanvas) (mCanvas)).divisorLine / 12) + l;
            i1 = ((TileCanvas) (mCanvas)).divTileY + i1;
        }
        if((((TileCanvas) (mCanvas)).tileMap[i1][l] & 0x40) != 0)
        {
            if(mBallSize == 16)
            {
                k = -30;
                j = -2;
                if(mGroundedFlag)
                    ySpeed = -10;
            } else
            {
                k = 42;
                j = 6;
            }
        } else
        if(mBallSize == 16)
        {
            k = 38;
            j = 3;
        } else
        {
            k = 80;
            j = 4;
        }
        if(gravBonusCntr != 0)
        {
            flag = true;
            k *= -1;
            j *= -1;
            gravBonusCntr--;
            if(gravBonusCntr == 0)
            {
                flag = false;
                mGroundedFlag = false;
                k *= -1;
                j *= -1;
            }
        }
        if(jumpBonusCntr != 0)
        {
            if(-1 * Math.abs(jumpOffset) > -80)
                if(flag)
                    jumpOffset = 80;
                else
                    jumpOffset = -80;
            jumpBonusCntr--;
        }
        slideCntr++;
        if(slideCntr == 3)
            slideCntr = 0;
        if(ySpeed < -150)
            ySpeed = -150;
        else
        if(ySpeed > 150)
            ySpeed = 150;
        if(xSpeed < -150)
            xSpeed = -150;
        else
        if(xSpeed > 150)
            xSpeed = 150;
        for(int k1 = 0; k1 < Math.abs(ySpeed) / 10; k1++)
        {
            byte byte1 = 0;
            if(ySpeed != 0)
                byte1 = (byte)((ySpeed >= 0) ? 1 : -1);
            if(collisionDetection(xPos, yPos + byte1))
            {
                yPos += byte1;
                mGroundedFlag = false;
                if(k == -30)
                {
                    int j1 = ((TileCanvas) (mCanvas)).tileY + yPos / 12;
                    if((((TileCanvas) (mCanvas)).tileMap[j1][l] & 0x40) == 0)
                    {
                        ySpeed = ySpeed >> 1;
                        if(ySpeed <= 10 && ySpeed >= -10)
                            ySpeed = 0;
                    }
                }
                continue;
            }
            if(mCDRampFlag && xSpeed < 10 && slideCntr == 0)
            {
                int i2 = 1;
                if(collisionDetection(xPos + i2, yPos + byte1))
                {
                    xPos += i2;
                    yPos += byte1;
                    mCDRampFlag = false;
                } else
                if(collisionDetection(xPos - i2, yPos + byte1))
                {
                    xPos -= i2;
                    yPos += byte1;
                    mCDRampFlag = false;
                }
            }
            if(byte1 > 0 || flag && byte1 < 0)
            {
                ySpeed = (ySpeed * -1) / 2;
                mGroundedFlag = true;
                if(mCDRubberFlag && (direction & 0x8) != 0)
                {
                    mCDRubberFlag = false;
                    if(flag)
                        jumpOffset += 10;
                    else
                        jumpOffset += -10;
                } else
                if(jumpBonusCntr == 0)
                    jumpOffset = 0;
                if(ySpeed < 10 && ySpeed > -10)
                    if(flag)
                        ySpeed = -10;
                    else
                        ySpeed = 10;
                break;
            }
            if(byte1 < 0 || flag && byte1 > 0)
                if(flag)
                    ySpeed = -20;
                else
                    ySpeed = -ySpeed >> 1;
        }

        if(flag)
        {
            if(j == -2 && ySpeed < k)
            {
                ySpeed += j;
                if(ySpeed > k)
                    ySpeed = k;
            } else
            if(!mGroundedFlag && ySpeed > k)
            {
                ySpeed += j;
                if(ySpeed < k)
                    ySpeed = k;
            }
        } else
        if(j == -2 && ySpeed > k)
        {
            ySpeed += j;
            if(ySpeed < k)
                ySpeed = k;
        } else
        if(!mGroundedFlag && ySpeed < k)
        {
            ySpeed += j;
            if(ySpeed > k)
                ySpeed = k;
        }
        if(speedBonusCntr != 0)
        {
            byte0 = 100;
            speedBonusCntr--;
        } else
        {
            byte0 = 50;
        }
        if((direction & 0x2) != 0 && xSpeed < byte0)
            xSpeed += 6;
        else
        if((direction & 0x1) != 0 && xSpeed > -byte0)
            xSpeed -= 6;
        else
        if(xSpeed > 0)
            xSpeed -= 4;
        else
        if(xSpeed < 0)
            xSpeed += 4;
        if(mBallSize == 16 && jumpBonusCntr == 0)
            if(flag)
                jumpOffset += 5;
            else
                jumpOffset += -5;
        if(mGroundedFlag && (direction & 0x8) != 0)
        {
            if(flag)
                ySpeed = 67 + jumpOffset;
            else
                ySpeed = -67 + jumpOffset;
            mGroundedFlag = false;
        }
        int l1 = Math.abs(xSpeed);
        int j2 = l1 / 10;
        for(int k2 = 0; k2 < j2; k2++)
        {
            byte byte2 = 0;
            if(xSpeed != 0)
                byte2 = (byte)((xSpeed >= 0) ? 1 : -1);
            if(collisionDetection(xPos + byte2, yPos))
                xPos += byte2;
            else
            if(mCDRampFlag)
            {
                mCDRampFlag = false;
                byte byte3 = 0;
                if(flag)
                    byte3 = 1;
                else
                    byte3 = -1;
                if(collisionDetection(xPos + byte2, yPos + byte3))
                {
                    xPos += byte2;
                    yPos += byte3;
                } else
                if(collisionDetection(xPos + byte2, yPos - byte3))
                {
                    xPos += byte2;
                    yPos -= byte3;
                } else
                {
                    xSpeed = -(xSpeed >> 1);
                }
            }
        }

        xOffset = xPos - i;
        if(xPos > 156 + mBallSize)
        {
            xPos -= 156;
            if(((TileCanvas) (mCanvas)).scrollOffset - 10 > 156 + mBallSize)
                mCanvas.scrollOffset -= 156;
        }
        if(xPos - mBallSize < 0)
        {
            xPos += 156;
            if(((TileCanvas) (mCanvas)).scrollOffset - mBallSize < 10)
                mCanvas.scrollOffset += 156;
        }
    }

    public void paint(Graphics g)
    {
        if(ballState == 2)
        {
            g.drawImage(poppedImage, xPos - 6, yPos - 6, 20);
            if(xPos > 144)
                g.drawImage(poppedImage, xPos - 156 - 6, yPos - 6, 20);
        } else
        {
            g.drawImage(mBallImage, xPos - mHalfBallSize, yPos - mHalfBallSize, 20);
            if(xPos > 156 - mBallSize)
                g.drawImage(mBallImage, xPos - 156 - mHalfBallSize, yPos - mHalfBallSize, 20);
        }
        dirtyTiles();
    }

    public void dirtyTiles()
    {
        int i = (xPos - mHalfBallSize) / 12;
        int j = (yPos - mHalfBallSize) / 12;
        int k = ((xPos - 1) + mHalfBallSize) / 12 + 1;
        int l = ((yPos - 1) + mHalfBallSize) / 12 + 1;
        if(j < 0)
            j = 0;
        if(l > 8)
            l = 8;
        boolean flag = false;
        boolean flag1 = false;
        for(int k1 = i; k1 < k; k1++)
        {
            for(int l1 = j; l1 < l; l1++)
            {
                int i1;
                int j1;
                if(k1 * 12 >= 156)
                {
                    i1 = (((TileCanvas) (mCanvas)).tileX + k1) - 13;
                    j1 = ((TileCanvas) (mCanvas)).tileY + l1;
                } else
                if(xPos < ((TileCanvas) (mCanvas)).divisorLine)
                {
                    i1 = ((TileCanvas) (mCanvas)).tileX + k1;
                    j1 = ((TileCanvas) (mCanvas)).tileY + l1;
                } else
                {
                    i1 = (((TileCanvas) (mCanvas)).divTileX - 13 - ((TileCanvas) (mCanvas)).divisorLine / 12) + k1;
                    j1 = ((TileCanvas) (mCanvas)).divTileY + l1;
                }
                ((TileCanvas) (mCanvas)).tileMap[j1][i1] |= 0x80;
            }

        }

    }

}
