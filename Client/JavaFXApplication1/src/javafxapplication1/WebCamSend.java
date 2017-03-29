/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import ClientLib.Clients.Client;
import ClientLib.GameClient;
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Eduard
 */
public class WebCamSend {
    private Webcam webCam = null;
    boolean stopCamera;
    GameClient client;
    public WebCamSend(GameClient client)
    {
        this.client = client;
    }
    
    public Webcam getWebCam()
    {
        return this.webCam;
    }
    public void setStopCamera(boolean stopCamera)
    {
        this.stopCamera = stopCamera;
    }
    public void initializeWebCam() {

		Task<Void> webCamTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
                                
                                
				webCam = Webcam.getWebcams().get(0);
				webCam.open();

				startWebCamStream();

				return null;
			}
		};

		Thread webCamThread = new Thread(webCamTask);
		webCamThread.setDaemon(true);
		webCamThread.start();
    }
    protected void startWebCamStream() {
                stopCamera = false;
		Task<Void> task = new Task<Void>() {

			@Override
			protected synchronized Void call() throws Exception {

				final AtomicReference<WritableImage> ref = new AtomicReference<>();
				BufferedImage img = null;

				while (!stopCamera) {
					try {
						if ((img = webCam.getImage()) != null) {

							ref.set(SwingFXUtils.toFXImage(img, ref.get()));
                                                        client.sendImage(img);
							img.flush();
                                                        wait(1);
						}
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}

				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

	}
}
