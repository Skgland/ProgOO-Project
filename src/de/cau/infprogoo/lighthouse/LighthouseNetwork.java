package de.cau.infprogoo.lighthouse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class wraps the network communication with the lighthouse in a simple
 * interface. The network connection is configured upon object creation but
 * needs to manually connect. Afterwards data can be sent to the lighthouse.
 */
public class LighthouseNetwork {

    private String hostname;
    private int    port;
    private Socket sock;
    private String user;
    private String password;

    /**
     * Creates a new lighthouse with default parameters. This is using localhost
     * at port 8000.
     */
    public LighthouseNetwork() {
        this.hostname = "localhost";
        this.port = 8000;
    }

    /**
     * Creates a new lighthouse using the given username and password. The
     * connection is established to localhost at port 8000.
     *
     * @param user The hostname of the server to connect to.
     * @param port The port to be used.
     */
    public LighthouseNetwork(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
     * Creates a new lighthouse connection with the given hostname and port.
     *
     * @param host The hostname of the server to connect to.
     * @param port The port to be used.
     */
    public LighthouseNetwork(String host, int port) {
        this.hostname = host;
        this.port = port;
    }

    /**
     * Creates a new lighthouse connection with the given hostname and port.
     *
     * @param host The hostname of the server to connect to.
     * @param port The port to be used.
     */
    public LighthouseNetwork(String host, int port, String user, String password) {
        this.hostname = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * Establishes the connection to the lighthouse server.
     *
     * @throws IOException          if an I/O error occurs when creating or writing to the socket.
     * @throws UnknownHostException if the IP address of the host could not be determined.
     */
    public void connect() throws IOException {
        sock = new Socket(hostname, port);
        OutputStream stream = sock.getOutputStream();
        stream.write(("POST /LH\r\n").getBytes());
        if(user != null) {
            stream.write(("user: " + user + "\r\n").getBytes());
        }
        if(password != null) {
            stream.write(("passwd: " + password + "\r\n").getBytes());
        }
        stream.write("\r\n".getBytes());
        stream.flush();
    }

    /**
     * Sends a packet of data to the lighthouse server. Usually the data should
     * be a byte array consisting of 1176 bytes. The first three bytes are the
     * red, green and blue color values of the first window. The windows start
     * at the top-left corner. If less bytes are sent, only the first windows
     * are updated. The next transmission starts at the first window again.
     *
     * @param data The data to send
     *
     * @throws IOException if some error occurs during sending of the data.
     */
    public void send(byte[] data) throws IOException {
        OutputStream stream = sock.getOutputStream();
        stream.write((data.length + "\r\n").getBytes());
        stream.write(data);
        stream.write("\r\n".getBytes());
        stream.flush();
    }
}
