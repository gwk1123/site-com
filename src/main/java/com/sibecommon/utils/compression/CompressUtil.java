package com.sibecommon.utils.compression;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class CompressUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(CompressUtil.class);

    /********************************** Snappy  ***************************************/


//    public static byte[] compressSnappy( Object bt ) throws IOException{
//        return  Snappy.compress(serialize(bt));
//    }
//
//    public static Object unCompressSnappy( Object bt) throws IOException{
//        if(null == bt)
//        { return  null ;}
//        return  unserialize(Snappy.uncompress((byte[])bt)) ;
//    }

    /*********************************** giz **********************************************/

    /**
     * @param bt
     * @return
     * @description 将byte 数组压缩
     */
    public static byte[] compressGIP( Object bt ) {
        //将byte数据读入文件流
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gzipos = null;
        try {
            bos = new ByteArrayOutputStream();
            gzipos = new GZIPOutputStream(bos);
            gzipos.write(serialize(bt));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(gzipos);
            closeStream(bos);
        }
        return bos.toByteArray();
    }

    /**
     * @param bt
     * @return
     * @description 解压缩byte数组
     */
    public static Object unCompressGIP( Object bt) {
        if(null == bt)
        { return  null ;}

        //byte[] unCompress=null;
        ByteArrayOutputStream byteAos = null;
        ByteArrayInputStream byteArrayIn = null;
        GZIPInputStream gzipIn = null;
        try {
            byteArrayIn = new ByteArrayInputStream((byte[])bt);
            gzipIn = new GZIPInputStream(byteArrayIn);
            byteAos = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            int temp = -1;
            while ((temp = gzipIn.read(b)) > 0) {
                byteAos.write(b, 0, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(byteAos);
            closeStream(gzipIn);
            closeStream(byteArrayIn);
        }
        return  unserialize(byteAos.toByteArray());
    }

    /**
     * @param oStream
     * @description 关闭数据流
     */
    public static void closeStream(Closeable oStream) {
        if (null != oStream) {
            try {
                oStream.close();
            } catch (IOException e) {
                oStream = null;//赋值为null,等待垃圾回收
                e.printStackTrace();
            }
        }
    }


    /********************* LZO *********************/
//    public static byte[] compressLZO( Object bt)   {
//        ByteArrayOutputStream os = null ;
//        LzoOutputStream cs = null;
//        byte[] lzoByte = null ;
//        try {
//            LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(
//                LzoAlgorithm.LZO1X, null);
//            os = new ByteArrayOutputStream();
//            cs = new LzoOutputStream(os, compressor);
//            cs.write(serialize(bt));
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            CompressUtil.closeStream(cs);
//            CompressUtil.closeStream(os);
//        }
//        return  os.toByteArray();
//    }
//
//    public static Object uncompressLZO( Object bt)  {
//
//        if(null == bt)
//        { return  null ;}
//        ByteArrayOutputStream baos = null ;
//        ByteArrayInputStream is = null ;
//        LzoInputStream us = null ;
//        try {
//            LzoDecompressor decompressor = LzoLibrary.getInstance()
//                .newDecompressor(LzoAlgorithm.LZO1X, null);
//            baos = new ByteArrayOutputStream();
//            is = new ByteArrayInputStream((byte[]) bt);
//            us = new LzoInputStream(is, decompressor);
//            int count;
//            byte[] buffer = new byte[4096];
//            while ((count = us.read(buffer)) > 0) {
//                baos.write(buffer, 0, count);
//            }
//        }catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            CompressUtil.closeStream(us);
//            CompressUtil.closeStream(is);
//            CompressUtil.closeStream(baos);
//        }
//
//        return unserialize(baos.toByteArray());
//    }



    /********************************** 对象 与 byte ***************************************/
    //序列化
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            closeStream(baos);
            closeStream(oos);
        }
    }

    //返序列化
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null ;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            closeStream(bais);
            closeStream(ois);
        }
    }

}
