/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservermultisockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class TCPServerMultiSockets {

    private List<Player> Players
            = Collections.synchronizedList(new ArrayList<Player>());
    private List<Integer> votes;
    private ServerSocket dataServerTCP, audioServerTCP, videoServerTCP;
    private boolean isclosed;
    private boolean day = false;
    private boolean firstDay = true;
    private boolean gameStarted = false;

    private int maxAmountPlayers;
    private int voted;
    private int alivePlayers;
    private int mafiaPlayers;
    private int connected;

    public TCPServerMultiSockets(int dataPort, int audioPortm, int videoPort, int _maxAmountPlayers) {
        maxAmountPlayers = _maxAmountPlayers;
        alivePlayers = 0;
        voted = 0;
        connected = 0;
        System.out.println("Сервер запущен");
        for (int i = 0; i < 6; i++) {
            Players.add(new Player());
        }
        CreateSockets(dataPort, audioPortm, videoPort);
        isclosed = false;
        StartThreads();
    }

    private void CreateSockets(int dataPort, int audioPortm, int videoPort) {
        try {
            dataServerTCP = new ServerSocket(dataPort);
            audioServerTCP = new ServerSocket(audioPortm);
            videoServerTCP = new ServerSocket(videoPort);
        } catch (IOException ex) {
            Logger.getLogger(TCPServerMultiSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void StartThreads() {
        final TCPServerMultiSockets server = this;
        Thread dataReceiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isclosed) {
                        Socket socket = dataServerTCP.accept();
                        if (connected != maxAmountPlayers && gameStarted == false) {
                            synchronized (Players) {
                                connected++;
                                Player pl = new Player();
                                for (Player player : Players) {
                                    if (player.isEmpty()) {
                                        pl = player;
                                        pl.start(socket, server);
                                        Players.set(Players.indexOf(player), pl);
                                        break;
                                    }
                                }
                                int id = (int) Players.indexOf(pl);
                                pl.setId(id);
                                byte data = (byte) id;
                                pl.getDataOutputStream().write(data);
                                System.out.println("New client " + pl.getIPAdrres());
                            }
                        }
                    }
                } catch (SocketException ex) {
                    if (isclosed) {
                        System.out.println("Закрытие сокета на приём данных у основного сервера");
                    } else {
                        System.out.println("Ошибка. Закрылся сокет данных у основного сервера");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TCPServerMultiSockets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Thread audioReceiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isclosed) {
                        Socket socket = audioServerTCP.accept();
                        String IPAddress = socket.getInetAddress().toString();
                        synchronized (Players) {
                            for (Player pl : Players) {
                                if (pl.isEmpty() == false && pl.getIPAdrres() != null && pl.getIPAdrres().equals(IPAddress)) {
                                    pl.setAudio(socket);
                                    break;
                                }
                            }
                        }
                    }
                } catch (SocketException ex) {
                    if (isclosed) {
                        System.out.println("Закрытие сокета на приём звука у основного сервера");
                    } else {
                        System.out.println("Ошибка. Закрылся сокет звука у основного сервера");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TCPServerMultiSockets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Thread videoReceiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isclosed) {
                        Socket socket = videoServerTCP.accept();
                        String IPAddress = socket.getInetAddress().toString();
                        synchronized (Players) {
                            for (Player pl : Players) {
                                if (pl.isEmpty() == false && pl.getIPAdrres() != null && pl.getIPAdrres().equals(IPAddress)) {
                                    pl.setVideo(socket);
                                    break;
                                }
                            }
                        }
                    }
                } catch (SocketException ex) {
                    if (isclosed) {
                        System.out.println("Закрытие сокета на приём видео у основного сервера");
                    } else {
                        System.out.println("Ошибка. Закрылся сокет видео у основного сервера");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TCPServerMultiSockets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        dataReceiver.start();
        audioReceiver.start();
        videoReceiver.start();
        System.out.println("Потоки на приём у основного сервера запущены");
    }

    private void setCitizens() {
        synchronized (Players) {
            Iterator<Player> iter = Players.iterator();
            while (iter.hasNext()) {
                Player it = ((Player) iter.next());
                if (it.getRole() != null && !it.getRole().equals("mafia")) {
                    it.setRole("citizen");
                }
            }
        }
    }

    private void start_game() {
        try {
            gameStarted = true;
            sendAllData(markData("-1;start_game".getBytes()));
            synchronized (Players) {
                switch (maxAmountPlayers) {
                    case 2: {
                        Random rand = new Random();
                        int number = rand.nextInt(1);
                        Players.get(number).setRole("mafia");
                        Players.get(1 - number).setRole("citizen");
                        mafiaPlayers = 1;
                        alivePlayers = 2;
                        break;
                    }
                    case 3: {
                        Random rand = new Random();
                        int number = rand.nextInt(2);
                        Players.get(number).setRole("mafia");
                        setCitizens();
                        mafiaPlayers = 1;
                        alivePlayers = 3;
                        break;
                    }
                    case 4: {
                        Random rand = new Random();
                        int number = rand.nextInt(3);
                        Players.get(number).setRole("mafia");
                        setCitizens();
                        mafiaPlayers = 1;
                        alivePlayers = 4;
                        break;
                    }
                    case 5: {
                        Random rand = new Random();
                        int number = rand.nextInt(4);
                        Players.get(number).setRole("mafia");
                        setCitizens();
                        mafiaPlayers = 1;
                        alivePlayers = 5;
                        break;
                    }
                    case 6: {
                        int number = 0;
                        Random rand = new Random();
                        Players.get(rand.nextInt(5)).setRole("mafia");
                        do {
                            number = rand.nextInt(5);
                        } while (Players.get(number).getRole() == null);
                        Players.get(number).setRole("mafia");
                        setCitizens();
                        mafiaPlayers = 2;
                        alivePlayers = 6;
                        break;
                    }
                }
            }
            firstDay = true;
            changeTime();
            startTalking();
        } catch (Exception e) {
            System.out.println("Ошибка при начале игры");
        }
    }

    synchronized private void changeTime() {
        try {
            String timeName;
            votes = new ArrayList<>();
            for (int i = 0; i < maxAmountPlayers; i++) {
                votes.add(0);
            }
            voted = 0; //Как только сменяется день и ночь: новое голсование
            day = !day;
            if (day == true) {
                timeName = "day";
            } else {
                timeName = "night";
            }
            synchronized (Players) {
                Iterator<Player> iter = Players.iterator();
                while (iter.hasNext()) {
                    Player it = ((Player) iter.next());
                    if (!it.isClosed() && !it.isEmpty()) {
                        it.setTime(timeName);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при установке дня/ночи");
        }
    }

    synchronized private void startTalking() {
        int i = 0;
        synchronized (Players) {
            int size = Players.size();
            while (i < size && !Players.get(i).isAlive() && Players.get(i).isEmpty()) {
                i++;
            }
            if (i < size) {
                Players.get(i).allowTalk();
            } else {
                System.out.println("Некому говорить: все погибли или никого нет");
            }
        }
    }

    synchronized public void nextToTalk(int number) {
        try {
            synchronized (Players) {
                int i = number + 1;
                int size = Players.size();
                while (i < size && !Players.get(i).isAlive() && Players.get(i).isEmpty()) {
                    i++;
                }
                if (i < size) {
                    Players.get(i).allowTalk();
                } else if (firstDay) {
                    firstDay = false;
                    changeTime();
                } else {
                    startVote();
                }
            }
        } catch (Exception ex) {
            System.out.println("Ошибка в nexttotalk" + ex.toString());
        }
    }

    private void startVote() {
        try {
            synchronized (Players) {
                Iterator<Player> iter = Players.iterator();
                while (iter.hasNext()) {
                    Player it = ((Player) iter.next());
                    if (!it.isClosed() && it.isAlive() && !it.isEmpty()) {
                        it.startVote();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при начале голосования");
        }
    }

    public synchronized void voteFor(int number) {
        boolean victory = false;
        voted++;
        votes.set(number, votes.get(number) + 1);
        synchronized (Players) {
            if ((voted == alivePlayers && day) || (voted == mafiaPlayers && !day)) {
                int indexMax = findMax();
                indexMax = votes.indexOf(indexMax);
                if (indexMax == -1) {
                    sendAllData(markData("-1;voteFailed".getBytes()));
                } else {
                    alivePlayers--;
                    Player player = Players.get(indexMax);
                    if (player.getRole().equals("mafia")) {
                        mafiaPlayers--;
                    }
                    player.kill("kill");
                    if (checkWin()) {
                        victory = true;
                    }
                }
                if (victory) {
                    restart();
                } else {
                    changeTime();
                }
            }
        }
    }

    void checkDisconnect(Player aThis) {
        synchronized (Players) {
            if (gameStarted) {
                alivePlayers--;
                if (aThis.getRole().equals("mafia")) {
                    mafiaPlayers--;
                }
                if (aThis.isTalking()) {
                    nextToTalk(aThis.getId());
                }
                aThis.kill("disconnected");
                if (checkWin()) {
                    restart();
                }
            } else if (aThis.isVoted()) {
                voted--;
            }
            connected--;
        }
    }

    private boolean checkWin() {
        if (mafiaPlayers >= alivePlayers - mafiaPlayers) {
            sendAllData(markData("-1;mafia_win".getBytes()));
            return true;
        } else if (mafiaPlayers == 0) {
            sendAllData(markData("-1;citizen_win".getBytes()));
            return true;
        }
        return false;
    }

    private void restart() {
        day = false;
        firstDay = true;
        gameStarted = false;

        voted = 0;
        alivePlayers = 0;
        mafiaPlayers = 0;
        connected = 0;
        synchronized (Players) {
            Iterator<Player> iter = Players.iterator();
            while (iter.hasNext()) {
                Player it = ((Player) iter.next());
                if (!it.isEmpty()) {
                    it.restart();
                    connected++;
                }
            }
        }
    }

    synchronized private int findMax() {
        int maxValue = 0;
        int countMaxValue = 0;
        for (Integer value : votes) {
            if (value > maxValue) {
                maxValue = value;
                countMaxValue = 0;
            }
            if (value == maxValue) {
                countMaxValue++;
            }
        }
        if (countMaxValue > 1) {
            return -1;
        } else {
            return maxValue;
        }
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

    synchronized private void close() {
        try {
            isclosed = true;
            if (!dataServerTCP.isClosed()) {
                dataServerTCP.close();
            }
            if (!audioServerTCP.isClosed()) {
                audioServerTCP.close();
            }
            if (!videoServerTCP.isClosed()) {
                videoServerTCP.close();
            }
            synchronized (Players) {
                Iterator<Player> iter = Players.iterator();
                while (iter.hasNext()) {
                    Player it = ((Player) iter.next());
                    if (!it.isClosed()) {
                        it.closeAndNoRemove();
                    }
                }
            }
            wait(200);//Ждём закрытия всех потоков
            System.out.println("Основной сервер закрыт");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    synchronized public void sendAllAudio(byte[] data, String role) {
        try {
            synchronized (Players) {
                Iterator<Player> iter = Players.iterator();
                while (iter.hasNext()) {
                    Player it = ((Player) iter.next());
                    if (it.isEmpty() == false) {
                        if (!it.isClosed() && it.getAudioOutputStream() != null && !it.isEmpty() && (role == null || day == true || (day == false && role.equals("mafia") && it.role.equals("mafia")))) {
                            try {
                                it.getAudioOutputStream().write(data);
                            } catch (SocketException ex) {
                                it.closeAndNoRemove();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Аудио экзепшн " + e.toString());
        }
    }

    synchronized public void sendAllVideo(byte[] data, String role) {
        Iterator<Player> iter;
        Player it;
        boolean mafia;
        boolean citizen;
        try {
            synchronized (Players) {
                iter = Players.iterator();
                while (iter.hasNext()) {
                    it = ((Player) iter.next());
                    if (it.isEmpty() == false) {
                        if (role != null && it.role != null) {
                            mafia = (day == false && it.role.equals(role) && role.equals("mafia"));
                            citizen = (day || mafia);
                        } else {
                            citizen = true;
                        }
                        if (!it.isClosed() && !it.isEmpty() && it.getVideoOutputStream() != null && citizen) {
                            try {
                                it.getVideoOutputStream().write(data);
                            } catch (SocketException ex) {
                                it.closeAndNoRemove();
                                System.out.println("Ошибка при отправке все видео");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Видео экзепшн" + e.toString());
        }
    }

    synchronized public void sendAllData(byte[] data) {
        try {
            synchronized (Players) {
                Iterator<Player> iter = Players.iterator();
                while (iter.hasNext()) {
                    Player it = ((Player) iter.next());
                    if (it.isEmpty() == false) {
                        if (!it.isClosed() && !it.isEmpty() && it.getDataOutputStream() != null) {
                            try {
                                it.getDataOutputStream().write(data);
                            } catch (SocketException ex) {
                                it.closeAndNoRemove();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Даные экзепшн" + e.toString());
        }
    }

    synchronized public void sendToOneData(int number, byte[] data) {
        try {
            synchronized (Players) {
                Player it = Players.get(number);
                if (!it.isClosed() && !it.isEmpty() && it.getDataOutputStream() != null) {
                    try {
                        it.getDataOutputStream().write(data);
                    } catch (SocketException ex) {
                        it.closeAndNoRemove();
                    }
                }
                System.out.println("Сообщение " + new String(data) + " отправлено только " + it.getIPAdrres());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void remove(Player player) {
        synchronized (Players) {
            Players.remove(player);
        }
    }

    void voteForStart(int parseInt) {
        voted++;
        if (voted == maxAmountPlayers) {
            start_game();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Max amount of players(>=2):");
        int Players = 0;
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            Players = sc.nextInt();
        }
        TCPServerMultiSockets server = new TCPServerMultiSockets(16387, 16385, 16386, Players);
    }
}
