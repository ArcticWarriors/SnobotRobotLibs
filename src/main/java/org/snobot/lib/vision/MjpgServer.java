package org.snobot.lib.vision;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MjpgServer
{
    private static final Logger sLOGGER = LogManager.getLogger("MjpgServer");
    private static final String K_BOUNDARY = "boundary";
    private static final int sDEFAULT_PORT = 5800;

    private static volatile MjpgServer sInst = null;

    private final ArrayList<Connection> mConnections = new ArrayList<>();
    private final Object mLock = new Object();

    private final int mBindPort;
    private final ServerSocket mServerSocket;
    private boolean mRunning;

    public static MjpgServer getInstance()
    {
        return getInstance(sDEFAULT_PORT);
    }

    /**
     * Singleton getter. Lazilly instantiates the server with the given port
     * 
     * @param aPort
     *            The port to bind to
     * @return The singleton instance
     */
    public static MjpgServer getInstance(int aPort)
    {
        if (sInst == null)
        {
            sInst = new MjpgServer(aPort);
        }
        else if (sInst.mBindPort != aPort)
        {
            throw new IllegalArgumentException("Mismatching bind port! Original: " + sInst.mBindPort + ", New: " + aPort);
        }
        return sInst;
    }

    private static class Connection
    {

        private final Socket mSocket;

        private Connection(Socket aSocket)
        {
            mSocket = aSocket;
        }

        public boolean isAlive()
        {
            return !mSocket.isClosed() && mSocket.isConnected();
        }

        public void start()
        {
            try
            {
                sLOGGER.info("Starting a connection!");
                OutputStream stream = mSocket.getOutputStream();
                stream.write(("HTTP/1.0 200 OK\r\n" + "Server: cheezyvision\r\n" + "Cache-Control: no-cache\r\n" + "Pragma: no-cache\r\n"
                        + "Connection: close\r\n" + "Content-Type: multipart/x-mixed-replace;boundary=--" + K_BOUNDARY + "\r\n").getBytes()); // NOPMD
            }
            catch (IOException ex)
            {
                sLOGGER.log(Level.ERROR, "", ex);
            }
        }

        public void writeImageUpdate(byte[] aBuffer)
        {
            if (!isAlive())
            {
                return;
            }
            OutputStream stream = null;
            try
            {
                stream = mSocket.getOutputStream();

                StringBuilder output = new StringBuilder(70);
                output.append("\r\n--") 
                        .append(K_BOUNDARY)
                        .append("\r\nContent-type: image/jpeg\r\nContent-Length: ")
                        .append(aBuffer.length)
                        .append("\r\n\r\n");

                stream.write(output.toString().getBytes());
                stream.write(aBuffer);
                stream.flush();

            }
            catch (IOException ex) // NOPMD
            {
                // There is a broken pipe exception being thrown here I cannot
                // figure out.
            }
        }

    }

    private MjpgServer(int aBindPort)
    {
        mBindPort = aBindPort;
        ServerSocket socket = null;
        try
        {
            socket = new ServerSocket(mBindPort);
            mRunning = true;
            Thread runThread = new Thread(mRunner);
            runThread.start();
        }
        catch (IOException ex)
        {
            sLOGGER.log(Level.ERROR, "", ex);
        }
        finally
        {
            mServerSocket = socket;
        }
    }

    /**
     * Sends the bytes stream to all of the connected sources.
     * 
     * @param aBytes
     *            The bytes
     */
    public void update(byte[] aBytes)
    {
        if (aBytes == null)
        {
            return;
        }

        synchronized (mLock)
        {
            ArrayList<Integer> badIndices = new ArrayList<>(mConnections.size());
            try
            {
                for (int i = 0; i < mConnections.size(); i++)
                {
                    Connection connection = mConnections.get(i);
                    if (connection == null || !connection.isAlive())
                    {
                        badIndices.add(i);
                    }
                    else
                    {
                        connection.writeImageUpdate(aBytes);
                    }
                }
                for (int i : badIndices)
                {
                    mConnections.remove(i);
                }
            }
            catch (Exception ex)
            {
                sLOGGER.log(Level.ERROR, "", ex);
            }
        }
    }

    Runnable mRunner = new Runnable()
    {

        @Override
        public void run()
        {
            while (mRunning)
            {
                try
                {
                    sLOGGER.info("Waiting for connections");
                    Socket socket = mServerSocket.accept();
                    sLOGGER.info("Got a socket: " + socket);
                    Connection connection = new Connection(socket);
                    synchronized (mLock)
                    {
                        mConnections.add(connection);
                    }
                    connection.start();
                }
                catch (IOException ex)
                {
                    sLOGGER.log(Level.ERROR, "", ex);
                }
            }
        }
    };
}
