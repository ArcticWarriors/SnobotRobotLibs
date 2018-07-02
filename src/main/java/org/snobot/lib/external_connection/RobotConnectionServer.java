package org.snobot.lib.external_connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RobotConnectionServer
{
    protected static final Logger sLOGGER = LogManager.getLogger("RobotConnectionServer");

    private ServerSocket mServerSocket;
    private boolean mIsConnected = false;
    private double mLastReceivedMessage = 0;

    private boolean mRunning = true;
    private final ArrayList<ServerThread> mServerThreads = new ArrayList<>();

    /**
     * Constructor.
     * 
     * @param aBindPort
     *            The port the accepting socket will bind to
     * @param aConnectionTimeout
     *            The amount of time that should elapse in seconds between
     *            heartbeats that would indicate the connection has been lost
     */
    public RobotConnectionServer(int aBindPort, double aConnectionTimeout)
    {
        try
        {
            mServerSocket = new ServerSocket(aBindPort);

            new Thread(mConnectionThread, "RobotConnectionServer::ConnectionAcceptor").start();
            new Thread(new AppMaintainanceThread(aConnectionTimeout), "RobotConnectionServer::ConnectionMonitor").start();
        }
        catch (IOException ex)
        {
            sLOGGER.log(Level.ERROR, "", ex);
        }
    }

    public boolean isConnected()
    {
        return mIsConnected;
    }

    protected void send(ByteBuffer aBuffer)
    {
        for (ServerThread thread : mServerThreads)
        {
            if (thread.isAlive())
            {
                thread.send(aBuffer);
            }
        }
    }

    private class ServerThread implements Runnable
    {
        private final Socket mSocket;

        private ServerThread(Socket aSocket)
        {
            mSocket = aSocket;
        }

        public void send(ByteBuffer aMessage)
        {
            if (mSocket != null && mSocket.isConnected())
            {
                try
                {
                    OutputStream os = mSocket.getOutputStream();
                    os.write(aMessage.array());
                }
                catch (IOException ex)
                {
                    sLOGGER.log(Level.ERROR, "Could not send data to socket", ex);
                }
            }
        }

        public boolean isAlive()
        {
            return mSocket != null && mSocket.isConnected() && !mSocket.isClosed();
        }

        @Override
        public void run()
        {
            if (mSocket == null)
            {
                return;
            }
            try
            {
                InputStream is = mSocket.getInputStream();
                byte[] buffer = new byte[2048];
                int read;
                while (mSocket.isConnected() && (read = is.read(buffer)) != -1)
                {
                    double timestamp = getTimestamp();
                    mLastReceivedMessage = timestamp;
                    String messageRaw = new String(buffer, 0, read);
                    String[] messages = messageRaw.split("\n");
                    for (String message : messages)
                    {
                        handleMessage(message, timestamp);
                    }
                }
                sLOGGER.log(Level.INFO, "Socked Disconnected: " + mSocket);
                mServerThreads.remove(this);
            }
            catch (IOException ex)
            {
                // Timeout is OK
                sLOGGER.log(Level.INFO, "Socked Disconnected (timeout): " + mSocket);
                mServerThreads.remove(this);
            }

            try
            {
                mSocket.close();
            }
            catch (IOException ex)
            {
                sLOGGER.log(Level.ERROR, "Could not close socket", ex);
            }
        }
    }

    private final Runnable mConnectionThread = new Runnable()
    {

        @Override
        public void run()
        {
            while (mRunning)
            {
                try
                {
                    Socket acceptSocket = mServerSocket.accept();
                    sLOGGER.log(Level.INFO, "Accepted Socket: " + acceptSocket);

                    ServerThread socket = new ServerThread(acceptSocket);
                    new Thread(socket, "RobotConnectionServer::ServerConnection").start();
                    mServerThreads.add(socket);
                }
                catch (IOException ex)
                {
                    sLOGGER.log(Level.ERROR, "Issue accepting incoming sockets", ex);
                }
                finally
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException ex)
                    {
                        sLOGGER.log(Level.ERROR, "Interrupted", ex);
                    }
                }
            }
        }
    };

    private class AppMaintainanceThread implements Runnable
    {
        /**
         * If the time between the last message and now (in seconds) is greater
         * than this, the connection will be considered disconnected.
         */
        private final double mTimeout;

        /**
         * Time to sleep inbetween loops, in milliseconds.
         */
        private final long mRefreshRate;

        private AppMaintainanceThread(double aTimeoutPeriod)
        {
            this(200, aTimeoutPeriod);
        }

        private AppMaintainanceThread(long aRefreshRate, double aTimeoutPeriod)
        {
            mRefreshRate = aRefreshRate;
            mTimeout = aTimeoutPeriod;
        }

        @Override
        public void run()
        {
            while (true)
            {
                double timestampDiff = getTimestamp() - mLastReceivedMessage;
                
                if (timestampDiff > mTimeout)
                {
                    if (mIsConnected)
                    {
                        onDisconnected();
                    }
                    mIsConnected = false;
                }
                else
                {
                    if (!mIsConnected)
                    {
                        onConnected();
                    }
                    mIsConnected = true;
                }

                try
                {
                    Thread.sleep(mRefreshRate);
                }
                catch (InterruptedException ex)
                {
                    sLOGGER.log(Level.ERROR, "", ex);
                }
            }
        }
    }

    public final void stop()
    {
        mRunning = false;
    }

    /**
     * Called when the connection monitor has determined a connection has been
     * started for the first time.
     */
    public abstract void onConnected();

    /**
     * Called when the connection monitor has determined the connection has been
     * lost.
     */
    public abstract void onDisconnected();

    /**
     * Called when a message has been received. Up to the child class to
     * determine how to parse it and what to do with it.
     * 
     * @param aMessage
     *            The message to parse
     * @param aTimestamp
     *            The timestamp it was received, according to
     *            {@link #getTimestamp}
     */
    public abstract void handleMessage(String aMessage, double aTimestamp);

    /**
     * Gets the current time, in seconds.
     * 
     * @return The time
     */
    protected abstract double getTimestamp();
}
