package defense_game;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Towers extends JFrame {
	private String name; // 이름
	private int[] atackSpeed; // 공격속도
	private int[] range; // 범위
	private int[] demage; // 공격력
	private ImageIcon[] img; // 이미지
	private String[] upgradePrice;
	private int level;
	private int[] yX_index;
	private int[][] rangePixel;
	private int sell;
	private boolean atkCooldown;
	private int atkTerm;
	private int changeImg;

	public Towers(String n, int[] as, int[] r, int[] d, ImageIcon[] img, String[] up) {
		// TODO Auto-generated constructor stub
		this.name = n;
		this.atackSpeed = as;
		this.range = r;
		this.demage = d;
		this.img = img;
		this.upgradePrice = up;
		this.level = 1;
		this.yX_index = new int[2];
		this.rangePixel = new int[2][2];
		this.sell = Integer.parseInt(this.upgradePrice[0]) / 2;
		this.atkCooldown = true;
		this.atkTerm = 0;
		this.changeImg = 0;
	}

	public void cooldown() {
		if (atkCooldown) {
			setAtkCooldown();
		} else {
			atkTerm -= atackSpeed[level];
			changeImg += atackSpeed[level];
		}
		if (atkTerm <= 0) {
			atkTerm = 100;
			setAtkCooldown();
		}
		if (changeImg > 20 * atackSpeed[level]) {
			changeImg = 0;
		}
	}

	public int getChangeImg() {
		return changeImg;
	}

	public boolean isAtkCooldown() {
		return atkCooldown;
	}

	public void setAtkCooldown() {
		this.atkCooldown = !this.atkCooldown;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int level) {
		if (level < upgradePrice.length - 1) {
			this.sell += Integer.parseInt(this.upgradePrice[level]) / 2;
		}
	}

	public int[] getYx_index() {
		return yX_index;
	}

	public void setYx_index(int[] yX_index) {
		this.yX_index = yX_index;
	}

	public int[][] getRangePixel() {
		return rangePixel;
	}

	public void setRangePixel(int[] yX_index) {
		int y = yX_index[0] * 50;
		int x = yX_index[1] * 50;
		this.rangePixel[0][0] = y - 50 * range[level];
		this.rangePixel[0][1] = y + 50 * range[level];
		this.rangePixel[1][0] = x - 50 * range[level];
		this.rangePixel[1][1] = x + 50 * range[level];
	}

	public void setLevel(int level) {
		if (level < upgradePrice.length - 1) {
			this.level++;
		}
	}

	public String getName() {
		return name;
	}

	public int[] getAtackSpeed() {
		return atackSpeed;
	}

	public int[] getRange() {
		return range;
	}

	public int[] getDemage() {
		return demage;
	}

	public ImageIcon[] getImg() {
		return img;
	}

	public String[] getUpgradePrice() {
		return upgradePrice;
	}

	public int getLevel() {
		return level;
	}

}
