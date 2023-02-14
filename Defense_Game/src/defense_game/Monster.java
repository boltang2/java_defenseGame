package defense_game;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Monster {
	private String monName;
	private int monHp;
	private int monSpeed;
	private ImageIcon img; // 이미지
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
		this.direction = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // 4방향
		this.curDirecton = new int[] { 0, 0 };
		this.destination = new int[] { 8, 16 };
		this.curIndex = new int[2]; // 현재 해당 몬스터가 가지고 있는 인덱스 값
		this.curYXPixel = new int[2]; // 현재 해당 몬스터가 가지고 있는 좌표값
		this.nextYXPixel = new int[2]; // 해당 몬스터가 가야하는 다음 좌쵸값
		this.keepGoing = false; // 다음 좌표값 까지 이동 중
	}

	// 다음갈 경로 추가
	public void aStar(int y, int x, boolean[][] route) {
		if (!keepGoing) {
			for (int i = 0; i < direction.length; i++) {
				// 4 방향 탑색
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

	// 다음 경로가 벽이거나 이미 지나온 길 인지 검사
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

	// 현재 몬스터가 가지고 있는 위치를 갱신 목적지에 도착하면 다음 경로 설정
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
