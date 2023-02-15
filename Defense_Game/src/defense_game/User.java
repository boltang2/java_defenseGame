package defense_game;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String nickName;
	private int score;
	private int clearTime;

	public User(String nickName, int score, int clearTime) {
		this.nickName = nickName;
		this.score = score;
		this.clearTime = clearTime;
	}

	public String getNickName() {
		return nickName;
	}

	public int getScore() {
		return score;
	}

	public int getClearTime() {
		return clearTime;
	}

}
