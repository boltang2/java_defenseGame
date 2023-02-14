package defense_game;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Monster {
	private String monName;
	private int monHp;
	private int monSpeed;
	private ImageIcon img; // �̹���
	private int id = 0;
	private int money;
	private int[][] direction;
	private int[] curDirecton;
	private int[] destination;
	private int[] curIndex;
	private int[] curYXPixel;
	private int[] nextYXPixel;
	private boolean keepGoing;

	private ArrayList<int[]> open = new ArrayList<int[]>();

	public Monster(String mn, int mh, int ms, ImageIcon img, int m) {
		this.monName = mn;
		this.monHp = mh;
		this.monSpeed = ms;
		this.img = img;
		this.money = m;
		this.direction = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // 4����
		this.curDirecton = new int[] { 0, 0 };
		this.destination = new int[] { 8, 16 };
		this.curIndex = new int[2]; // ���� �ش� ���Ͱ� ������ �ִ� �ε��� ��
		this.curYXPixel = new int[2]; // ���� �ش� ���Ͱ� ������ �ִ� ��ǥ��
		this.nextYXPixel = new int[2]; // �ش� ���Ͱ� �����ϴ� ���� ���ݰ�
		this.keepGoing = false; // ���� ��ǥ�� ���� �̵� ��
	}

	// ������ ��� �߰�
	public void aStar(int y, int x, boolean[][] route) {
		if (!keepGoing) {
			for (int i = 0; i < direction.length; i++) {
				// 4 ���� ž��
				int indexY = y + direction[i][0];
				int indexX = x + direction[i][1];
				if (isduplic(indexY, indexX, route)) {
					this.curDirecton = direction[i];
					this.open.add(new int[] { indexY, indexX });
					nextYXPixel = new int[] { indexY * 50, indexX * 50 };
					keepGoing = true;
					break;
				}
			}
		}
	}

	// ���� ��ΰ� ���̰ų� �̹� ������ �� ���� �˻�
	public boolean isduplic(int y, int x, boolean[][] route) {
		try {
			if (route[y][x] == true) {
				return false;
			} else {
				for (int[] el : open) {
					if (el[0] == y && el[1] == x) {
						return false;
					}
				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// ���� ���Ͱ� ������ �ִ� ��ġ�� ���� �������� �����ϸ� ���� ��� ����
	public void printMonsterXY() {
		this.curYXPixel[0] += monSpeed * curDirecton[0];
		this.curYXPixel[1] += monSpeed * curDirecton[1];
		if (curDirecton[0] > 0) {
			if (curDirecton[0] != 0 && curYXPixel[0] >= nextYXPixel[0]) {
				curYXPixel = nextYXPixel;
				curIndex[0] += curDirecton[0];
				keepGoing = false;
			}
		} else if (curDirecton[0] < 0) {
			if (curDirecton[0] != 0 && curYXPixel[0] <= nextYXPixel[0]) {
				curYXPixel = nextYXPixel;
				curIndex[0] += curDirecton[0];
				keepGoing = false;
			}
		} else if (curDirecton[1] > 0) {
			if (curDirecton[1] != 0 && curYXPixel[1] >= nextYXPixel[1]) {
				curYXPixel = nextYXPixel;
				curIndex[1] += curDirecton[1];
				keepGoing = false;
			}
		} else if (curDirecton[1] < 0) {
			if (curDirecton[1] != 0 && curYXPixel[1] <= nextYXPixel[1]) {
				curYXPixel = nextYXPixel;
				curIndex[1] += curDirecton[1];
				keepGoing = false;
			}
		}

	}

	public int getMoney() {
		return money;
	}

	public int[][] getDirection() {
		return direction;
	}

	public int[] getCurDirecton() {
		return curDirecton;
	}

	public int[] getDestination() {
		return destination;
	}

	public int[] getCurYXPixel() {
		return curYXPixel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMonName() {
		return monName;
	}

	public int getMonHp() {
		return monHp;
	}

	public void setMonHp(int attackedDemage) {
		this.monHp -= attackedDemage;
	}

	public int getMonSpeed() {
		return monSpeed;
	}

	public ImageIcon getImg() {
		return img;
	}

	public int[] getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int[] curIndex) {
		this.curIndex = curIndex;
	}
}
