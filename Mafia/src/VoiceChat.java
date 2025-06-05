import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.*;

public class VoiceChat {

    private static final int PORT = 55555;
    private static final int BUFFER_SIZE = 4096;
    private static final int NOISE_THRESHOLD = 175; // Noise gate threshold

    private TargetDataLine microphone;
    private SourceDataLine speakers;

    private DatagramSocket socket;
    private Game game;

    private AtomicBoolean running = new AtomicBoolean(false);

    private Thread sendThread;
    private Thread receiveThread;

    public VoiceChat() {
        // Constructor can be empty or used for initialization if needed
    }

    public VoiceChat(Game game) {
        this.game = game;
    }

    public void start() throws LineUnavailableException, SocketException {
        System.out.println("Starting voice chat...");
        if (running.get()) return;

        running.set(true);

        AudioFormat format = getAudioFormat();
        System.out.println("Audio format: " + format);

        // Setup microphone
        DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(micInfo);
        microphone.open(format);
        microphone.start();
        System.out.println("Microphone opened: " + microphone.getLineInfo());

        // Setup speakers
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        speakers = (SourceDataLine) AudioSystem.getLine(speakerInfo);
        speakers.open(format);
        speakers.start();
        System.out.println("Speakers opened: " + speakers.getLineInfo());

        socket = new DatagramSocket(PORT);

        // Send thread
        sendThread = new Thread(() -> {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (running.get()) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    
                    try {
                        ArrayList<String> ips = game.getIPs(); 
                        if (isBelowThreshold(buffer, bytesRead)) {
                            // Replace audio with silence
                            for (int i = 0; i < bytesRead; i++) buffer[i] = 0;
                        }
                        for (String ip : ips) {
                            if(!ip.equals(Driver.getYourIp())) { // Skip own IP
                                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, InetAddress.getByName(ip), PORT);
                                socket.send(packet);
                                //System.out.println("Sent packet to " + ip + " with size: " + bytesRead);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Receive thread
        receiveThread = new Thread(() -> {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (running.get()) {
                try {
                    socket.receive(packet);
                    //System.out.println("Received packet from " + packet.getAddress().getHostAddress() + " with size: " + packet.getLength());
                    ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                    int count;
                    byte[] playBuffer = new byte[BUFFER_SIZE];
                    while ((count = bais.read(playBuffer)) > 0) {
                        speakers.write(playBuffer, 0, count);
                    }
                } catch (IOException e) {
                    if (running.get()) e.printStackTrace();
                }
            }
        });

        sendThread.start();
        receiveThread.start();
    }

    public void stop() {
        System.out.println("Stopping voice chat...");
        running.set(false);
        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
        if (speakers != null) {
            speakers.stop();
            speakers.close();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        try {
            if (sendThread != null) sendThread.join();
            if (receiveThread != null) receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setGame(Game game) {
        this.game = game;
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    // Noise gate: check if audio volume is below threshold
    private boolean isBelowThreshold(byte[] audioData, int length) {
        long total = 0;
        int samples = length / 2; // 16-bit samples (2 bytes each)
        for (int i = 0; i < length - 1; i += 2) {
            int sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));
            total += Math.abs(sample);
        }
        long average = total / Math.max(samples, 1);
        return average < NOISE_THRESHOLD;
    }
}
