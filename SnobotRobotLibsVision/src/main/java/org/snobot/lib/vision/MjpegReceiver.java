package org.snobot.lib.vision;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MjpegReceiver
{
    protected static final Logger sLOGGER = LogManager.getLogger(MjpegReceiver.class);

    private static final int[] START_BYTES = new int[]{ 0xFF, 0xD8 };
    private static final byte[] END_BYTES = "--boundary".getBytes();
    private boolean mRunning;
    private int mReadTimeout;
    private final List<ImageReceiver> mImageRecievers;

    public interface ImageReceiver
    {
        void onImage(byte[] aImage);
    }

    public MjpegReceiver()
    {
        mImageRecievers = new ArrayList<>();
        mReadTimeout = 0;
    }

    public void addImageReceiver(ImageReceiver aImageReceiver)
    {
        mImageRecievers.add(aImageReceiver);
    }

    /**
     * Starts the receiving thread. Thread will run until stop() is called
     * 
     * @param aImageUrl
     *            The URL to connect to
     */
    public void start(String aImageUrl)
    {
        mRunning = true;

        new Thread(new ReceiveThread(aImageUrl), "MjpegReciever").start();
    }

    /**
     * Stops the receiving thread.
     */
    public void stop()
    {
        mRunning = false;
    }

    /**
     * Sets the read timeout for the HTTP Socket to use. This takes affect the
     * next time a connection is made, so if you are already connected nothing
     * will happen until the next time connection is lost
     * 
     * @param aTimeout
     *            The timeout in milliseconds. 0 means infinite
     */
    public void setReadTimeout(int aTimeout)
    {
        mReadTimeout = aTimeout;
    }

    private byte[] parseImage(InputStream aStream, ByteArrayOutputStream aImageBuffer) throws IOException
    {
//        System.out.println("Buffer size : " + stream.available());
        aImageBuffer.reset();
        for (int i = 0; i < START_BYTES.length;)
        {
            int bytes = aStream.read();
            if (bytes == START_BYTES[i])
            {
                i++;
            }
            else
            {
                i = 0;
            }
        }
//        System.out.println("Got start bytes...");

        for (int i = 0; i < START_BYTES.length; ++i)
        {
            aImageBuffer.write(START_BYTES[i]);
        }

        for (int i = 0; i < END_BYTES.length;)
        {
            if (aImageBuffer.size() > 1000000)
            {
                aImageBuffer.close();
                return null;
            }
            int bytes = aStream.read();
            aImageBuffer.write(bytes);
            if (bytes == END_BYTES[i])
            {
                i++;
            }
            else
            {
                i = 0;
            }
        }

        return aImageBuffer.toByteArray();

    }

    private void publishImage(byte[] aImageData)
    {
        for (ImageReceiver recv : mImageRecievers)
        {
            recv.onImage(aImageData);
        }
    }

    private class ReceiveThread implements Runnable
    {
        private final String mImageUrl;
        private boolean mConnected;

        private ReceiveThread(String aImageUrl)
        {
            mImageUrl = aImageUrl;
        }

        @Override
        public void run()
        {
            sLOGGER.log(Level.INFO, "Attempting to connect to camera at " + mImageUrl);
            while (mRunning)
            {
                try
                {
                    ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();
                    InputStream stream = null;

                    URL url = new URL(mImageUrl);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setReadTimeout(mReadTimeout);
                    stream = urlConn.getInputStream();
                    mConnected = true;

                    while (mRunning && mConnected)
                    {
                        byte[] imageData = parseImage(stream, imageBuffer);
                        publishImage(imageData);
                    }
                }
                catch (SocketException ex)
                {
                    mConnected = false;
                    publishImage(null);
                }
                catch (Exception ex)
                {
                    sLOGGER.log(Level.ERROR, "Could not save image", ex);
                }
            }
        }
    }
}
