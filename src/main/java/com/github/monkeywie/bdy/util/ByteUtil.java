package com.github.monkeywie.bdy.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteUtil {

  /**
   * 大端序
   */
  public static byte[] numToBtsForBig(long num, int len) {
    byte[] bts = new byte[len];
    for (int i = 0; i < bts.length; i++) {
      bts[bts.length - i - 1] = (byte) ((num >> 8 * i) & 0xFF);
    }
    return bts;
  }

  /**
   * 大端序
   */
  public static byte[] numToBtsForBig(long num) {
    //long 8字节
    return numToBtsForBig(num, 8);
  }

  /**
   * 大端序
   */
  public static long btsToNumForBig(byte[] bts) {
    //int 4字节
    long num = 0;
    for (int i = 0; i < bts.length; i++) {
      num += ((long) (bts[i] & 0xFF)) << 8 * (bts.length - i - 1);
    }
    return num;
  }

  /**
   * 小端序
   */
  public static byte[] numToBtsForSmall(long num, int len) {
    byte[] bts = new byte[len];
    for (int i = 0; i < bts.length; i++) {
      bts[i] = (byte) ((num >> 8 * i) & 0xFF);
    }
    return bts;
  }

  /**
   * 小端序
   */
  public static byte[] numToBtsForSmall(long num) {
    return numToBtsForSmall(num, 8);
  }

  /**
   * 小端序
   */
  public static long btsToNumForSmall(byte[] bts) {
    //int 4字节
    long num = 0;
    for (int i = 0; i < bts.length; i++) {
      num += ((long) (bts[i] & 0xFF)) << 8 * i;
    }
    return num;
  }

  /**
   * 查找buffer中一段字节数的位置
   *
   * @param buffer 待查找的buffer
   * @param btsArr 多个为或的关系
   */
  public static int findBytes(ByteBuffer buffer, byte[]... btsArr) {
    int[] indexArray = new int[btsArr.length];
    while (buffer.hasRemaining()) {
      byte b = buffer.get();
      for (int i = 0; i < btsArr.length; i++) {
        if (indexArray[i] == -1) {
          indexArray[i] = 0;
        }
        byte[] bts = btsArr[i];
        if (b == bts[indexArray[i]]) {
          indexArray[i]++;
          if (indexArray[i] == bts.length) {
            return buffer.position() - bts.length;
          }
        } else {
          indexArray[i] = 0;
        }
      }
    }
    return -1;
  }

  public static long getNextTokenSize(FileChannel fileChannel, long start, long position,
      byte[]... btsArr)
      throws IOException {
    long ret = -1;
    ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
    long startPosition;
    if (start >= 0) {
      startPosition = start;
    } else {
      startPosition = fileChannel.position();
    }
    if (position >= 0) {
      fileChannel.position(position);
    }
    outer:
    while (fileChannel.read(buffer) != -1) {
      buffer.flip();
      int index;
      while ((index = findBytes(buffer, btsArr)) != -1) {
        ret = fileChannel.position() - startPosition - buffer.limit() + index;
        break outer;
      }
      buffer.clear();
    }
    fileChannel.position(startPosition);
    return ret;
  }

  public static boolean matchToken(FileChannel fileChannel, long position, byte[] bts)
      throws IOException {
    return matchToken(fileChannel, -1, position, bts);
  }

  public static boolean matchToken(FileChannel fileChannel, long start, long position,
      byte[]... btsArr)
      throws IOException {
    boolean ret;
    ByteBuffer buffer = ByteBuffer.allocate(btsArr[0].length);
    long rawPosition;
    if (start >= 0) {
      rawPosition = start;
    } else {
      rawPosition = fileChannel.position();
    }
    if (position >= 0) {
      fileChannel.position(position);
    }
    fileChannel.read(buffer);
    buffer.flip();
    ret = findBytes(buffer, btsArr) == 0;
    fileChannel.position(rawPosition);
    return ret;
  }
}
