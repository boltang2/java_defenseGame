package defense_game;

import javax.swing.ImageIcon;

public class Stage {
	private String name;
	private int mount;
	private Monster monster;

	public Stage(int i, String n, int m, String mn, int mh, int ms, ImageIcon img, int mm) {
		this.name = n;
		this.mount = m;
		this.monster = new Monster(mn, mh, ms, img, mm);

	}

	public String getName() {
		return name;
	}

	public int getMount() {
		return mount;
	}

	public Monster getMonster() {
		return monster;
	}
}
