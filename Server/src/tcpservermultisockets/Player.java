/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservermultisockets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Player {

    private Socket audioSocket;
    private Socket videoSocket;
    private Socket dataSocket;
    private InputStream videoInputStream;
    private InputStream audioInputStream;
    private InputStream dataInputStream;
    private OutputStream videoOutputStream;
    private OutputStream audioOutputStream;
    private OutputStream dataOutputStream;
    private TCPServerMultiSockets server;

    private boolean closed = false;
    private boolean alive = false;
    private boolean removed = false;
    private boolean talking = false;
    private boolean gameStarted = false;
    private boolean voted = false;
    private boolean empty = true;

    private int id;

    public String role;
    public String time;
    private String IPAdrres;
    private String name;

    void restart() {
        role = null;
        time = null;
        empty = false;
        alive = true;
        talking = false;
        gameStarted = false;
        voted = false;
        closed = false;
    }

    public Player() {

    }

    public Player(Socket socket, TCPServerMultiSockets server) {
        try {
            dataSocket = socket;
            IPAdrres = socket.getInetAddress().toString();
            this.server = server;
            dataInputStream = dataSocket.getInputStream();
            dataOutputStream = dataSocket.getOutputStream();
            startDataReceving();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    synchronized public void start(Socket socket, TCPServerMultiSockets server) {
        try {
            restart();
            dataSocket = socket;
            IPAdrres = socket.getInetAddress().toString();
            this.server = server;
            dataInputStream = dataSocket.getInputStream();
            dataOutputStream = dataSocket.getOutputStream();
            startDataReceving();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }





    public void setAudio(Socket socket) throws IOException {
        audioSocket = socket;
        audioInputStream = (InputStream) audioSocket.getInputStream();
        audioOutputStream = (OutputStream) audioSocket.getOutputStream();
        startAudioReceving();
    }

    public void setVideo(Socket socket) throws IOException {
        videoSocket = socket;
        videoInputStream = (InputStream) videoSocket.getInputStream();
        videoOutputStream = (OutputStream) videoSocket.getOutputStream();
        startVideoReceving();
    }

    private void startAudioReceving() {
        Thread receving = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = new byte[10000];
                try {
                    while (!isClosed()) {
                        if (isAlive()) {
                            if ((isGameStarted() == false || (isGameStarted() == true && isTalking() == true))) {
                                audioInputStream.read(data);
                                server.sendAllAudio(data, role);
                                data = new byte[10000];
                            }
                        }
                    }
                } catch (SocketException ex) {
                    if (isClosed()) {

                    } else {
                        System.out.println("Ошибка. Закрылся сокет аудио у " + getIPAdrres());
                    }
                } catch (Exception ex) {
                    System.out.println("Аудио экзепшн  " + ex.toString());
                } finally {
                    closeAndNoRemove();
                }
            }
        });
        try {
            receving.start();
        } catch (Exception ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startVideoReceving() throws IOException {
        Thread receving;
        receving = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = new byte[10000];
                try {

                    while (!isClosed()) {
                        if (isAlive()) {
                            videoInputStream.read(data);
                            server.sendAllVideo(data, role);
                            data = new byte[10000];
                        }
                    }
                } catch (SocketException ex) {
                    if (isClosed()) {
                    } else {
                        System.out.println("Ошибка. Закрылся сокет видео у " + getIPAdrres());
                    }
                } catch (Exception ex) {
                    System.out.println("Видео экзепшн");
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    closeAndNoRemove();
                }
            }
        });
        try {
            receving.start();
        } catch (Exception ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startDataReceving() throws IOException {
        Thread receving = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data = new byte[10000];
                    while (!isClosed()) {
                        if (isAlive()) {
                            dataInputStream.read(data);
                            if (data[0] == 6) {
                                String[] parsedCommand = parseCommand(data);
                                if (parsedCommand[1].equals("start")) {
                                    voted = true;
                                    server.voteForStart(Integer.parseInt(parsedCommand[0]));//TODO
                                }
                                if (parsedCommand[1].equals("stopTalk")) {
                                    talking = false;
                                    server.nextToTalk(Integer.parseInt(parsedCommand[0]));
                                }
                                if (parsedCommand[1].equals("vote")) {
                                    voted = true;
                                    server.voteFor(Integer.parseInt(parsedCommand[0]));//TODO
                                }
                                if (parsedCommand[1].equals("name")) {
                                    name = parsedCommand[2];
                                }
                            } else {
                                server.sendAllData(data);
                            }
                            data = new byte[10000];
                        }
                    }
                } catch (SocketException ex) {
                    if (isClosed()) {

                    } else {
                        System.out.println("Ошибка. Закрылся сокет данных у " + getIPAdrres());
                    }
                } catch (Exception ex) {
                    System.out.println("Даные экзепшн");
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    closeAndNoRemove();
                }
            }
        });
        try {
            receving.start();
        } catch (Exception ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public OutputStream getAudioOutputStream() throws IOException {
        return audioOutputStream;
    }

    public OutputStream getVideoOutputStream() throws IOException {
        return videoOutputStream;
    }

    public OutputStream getDataOutputStream() throws IOException {
        return dataOutputStream;
    }

    synchronized public void close() {
        if (!isClosed()) {
            closed = true;
            System.out.println("Client " + getIPAdrres() + " has disconnected");
        }
    }

    synchronized void remove() {
        try {
            if (!isRemoved()) {
                removed = true;
                if (this.audioSocket != null) {
                    this.audioSocket.shutdownInput();
                    this.audioSocket.shutdownOutput();
                    this.audioSocket.close();

                }
                if (this.videoSocket != null) {
                    this.videoSocket.shutdownInput();
                    this.videoSocket.shutdownOutput();
                    this.videoSocket.close();

                }
                if (this.dataSocket != null) {
                    this.dataSocket.shutdownInput();
                    this.dataSocket.shutdownOutput();
                    this.dataSocket.close();
                }

                if (this.audioOutputStream != null) {
                    this.audioOutputStream.close();
                }
                if (this.videoOutputStream != null) {
                    this.videoOutputStream.close();
                }
                if (this.audioInputStream != null) {
                    this.audioInputStream.close();
                }
                if (this.videoInputStream != null) {
                    this.videoInputStream.close();
                }

                if (this.dataOutputStream != null) {
                    this.dataOutputStream.close();
                }
                if (this.dataInputStream != null) {
                    this.dataInputStream.close();
                }

                System.gc();
            }
        } catch (SocketException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    synchronized public void closeAndNoRemove() {
        try {
            if (!isClosed()) {
                closed = true;
                server.checkDisconnect(this);
                System.out.println("Client " + getIPAdrres() + " has disconnected");

                if (this.audioSocket != null && !this.audioSocket.isClosed()) {
                    this.audioSocket.shutdownInput();
                    this.audioSocket.shutdownOutput();
                    this.audioSocket.close();

                }
                if (this.videoSocket != null && !this.videoSocket.isClosed()) {
                    this.videoSocket.shutdownInput();
                    this.videoSocket.shutdownOutput();
                    this.videoSocket.close();

                }
                if (this.dataSocket != null && !this.dataSocket.isClosed()) {
                    this.dataSocket.shutdownInput();
                    this.dataSocket.shutdownOutput();
                    this.dataSocket.close();
                }

                if (this.audioOutputStream != null) {
                    this.audioOutputStream.close();
                }
                if (this.videoOutputStream != null) {
                    this.videoOutputStream.close();
                }
                if (this.audioInputStream != null) {
                    this.audioInputStream.close();
                }
                if (this.videoInputStream != null) {
                    this.videoInputStream.close();
                }

                if (this.dataOutputStream != null) {
                    this.dataOutputStream.close();
                }
                if (this.dataInputStream != null) {
                    this.dataInputStream.close();
                }
                empty = true;
                System.gc();
            }
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized private String[] parseCommand(byte[] data) {
        byte[] messageData = getUnmarkedData(data);
        String str = new String(messageData);
        String[] splitStr = str.split(";");
        return splitStr;
    }

    private byte[] getUnmarkedData(byte[] data) {
        int result = 0;
        try {
            byte[] length = new byte[4];
            System.arraycopy(data, 2, length, 0, 4);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(length));

            result = dis.readInt();
            if (result > 10000 || result < 0) {
                return null;
            }
            byte[] unmarkedData = new byte[result];
            System.arraycopy(data, 6, unmarkedData, 0, result);
            return unmarkedData;
        } catch (Exception ex) {
            System.out.println(result + ex.toString() + "Анмаркед окоянный");
        }
        return null;
    }

    synchronized public void setRole(String Role) {
        role = Role;
        gameStarted = true;
        String com = getId() + ";" + role;
        byte[] data = markData(com.getBytes());
        server.sendToOneData(getId(), data);
    }

    synchronized public void setTime(String Time) {
        try {
            wait(500);
            time = Time;
            String com = getId() + ";" + Time;
            byte[] data = markData(com.getBytes());
            server.sendToOneData(getId(), data);
        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized void startVote() {
        String com = getId() + ";startVote";
        byte[] data = markData(com.getBytes());
        server.sendToOneData(getId(), data);
    }

    synchronized private void startTalk() {
        String com = getId() + ";startTalk";
        byte[] data = markData(com.getBytes());
        server.sendToOneData(getId(), data);
    }

    void sendMafia(Integer mafia1) {
        String com = mafia1 + ";mafia";
        byte[] data = markData(com.getBytes());
        server.sendToOneData(getId(), data);
    }

    synchronized public void kill(String Command) {
        alive = false;
        byte[] data = markData((getId() + ";" + Command + ";" + getName()).getBytes());
        server.sendAllData(data);
    }

    synchronized private byte[] markData(byte[] data) {
        byte[] markedData = new byte[data.length + 6];
        markedData[0] = (byte) ((int) (6));
        markedData[1] = (byte) ((int) (-1));
        int length = data.length;
        byte[] bytes = ByteBuffer.allocate(4).putInt(length).array();
        System.arraycopy(bytes, 0, markedData, 2, bytes.length);
        System.arraycopy(data, 0, markedData, bytes.length + 2, data.length);
        return markedData;
    }

    public String getRole() {
        return role;
    }

    synchronized public void allowTalk() {
        try {
            wait(1000);
            talking = true;
            startTalk();
        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @return the removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * @return the talking
     */
    public boolean isTalking() {
        return talking;
    }

    /**
     * @return the gameStarted
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * @return the voted
     */
    public boolean isVoted() {
        return voted;
    }

    /**
     * @return the empty
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the IPAdrres
     */
    public String getIPAdrres() {
        return IPAdrres;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
