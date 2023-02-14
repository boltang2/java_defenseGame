package defense_game;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Bgm extends Thread {
	private File BGM;
	private AudioInputStream AIS;
	private Clip C;
	private FloatControl V;

	public Bgm(String music) {
		BGM = new File("bgm/" + music);
	}

	public void play(int loop, float volume) {
		try {
			AIS = AudioSystem.getAudioInputStream(BGM);
			C = AudioSystem.getClip();
			C.open(AIS);
			V = (FloatControl) C.getControl(FloatControl.Type.MASTER_GAIN);
			V.setValue(volume);
			C.start();
			if (loop == -1) {
				C.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				C.loop(loop);
			}
			C.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopMusic() {
		C.stop();
	}

	public void resumeMusic() {
		C.start();
	}

}
