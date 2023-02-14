package defense_game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DefenseGame_Frame extends JFrame implements Runnable {
	// 모든 타워 정보 집합
	ArrayList<Towers> towers = new ArrayList<Towers>();
	// 모든 스테이지 정보 집합
	ArrayList<Stage> stages = new ArrayList<Stage>();
	// 현재 스테이지 소환할 몬스터 정보 집합
	HashMap<Integer, Monster> actMonster_info = new HashMap<Integer, Monster>();
	// 현재 전장에 나와있는 크리처 화면 출력 집합
	HashMap<Integer, JPanel> actMonster_display = new HashMap<Integer, JPanel>();
	// 현재 작동하고 있는 타워 정보 집합
	HashMap<String, Towers> actTower_info = new HashMap<String, Towers>();
	// 현재 작동하고 있는 타워 화면 출력 집합
	HashMap<String, JPanel> actTower_display = new HashMap<String, JPanel>();
	// 현재 라운드에서 죽을 경우를 대비한 백업 타워 정보 집합
	HashMap<String, Towers> actTower_info_die = new HashMap<String, Towers>();
	// 현재 라운드에서 죽을 경우를 대비한 백업 타워 화면 출력 집합
	HashMap<String, JPanel> actTower_display_die = new HashMap<String, JPanel>();
	// 필드 정보 집합
	Field fields = new Field();
	// 데이터 베이스
	DataBase db = new DataBase();
	// BGM - 메인
	Bgm MainBGM;
	// BGM - 게임 오버
	Bgm EndBGM;
	// 내가 작동하고 있는 타워의 인덱스와 픽셀값
	int[] towerCoordInfo = new int[5];
	// 내가 작동하고 있는 타워 이름
	String selectedTowerName = "";
	// true : 스테이지 진행 가능 상태 / 스테이지 진행 불가
	boolean isRun = true;
	// false : 카운트 다운 종료 / 카운트 다운 출력 중
	boolean isAlarm = false;
	// false : 일시 정지 비활성화 / 일시 정지 활성화
	boolean isStop = false;
	// true : 게임 진행 / 게임 종료
	boolean isOut = true;
	// true : 현재 라운드 진행 중 / 죽음
	boolean isDie = true;
	// true : 해당 라운드 클리어 / 클리어 전
	boolean isClear = false;
	// true : 게임 실행 / 실행 전
	boolean isStart = false;
	// stages에서 현재 라운드 정보 가져오기 위한 index
	int curStageIndex = 0;
	// 목숨
	int life = 20;
	// 현재 라운드에서 죽을 경우를 대비한 백업
	int life_die;
	// 현재 가지고 있는 돈
	int myMoney = 50;
	// 현재 라운드에서 죽을 경우를 대비한 백업
	int myMoney_die;
	// 이번 라운드에서 처리해야하는 몬스터 수
	int remainMon = -1;
	// 게임 속도
	int[] gameSpeed = { 10, 8, 6, 100, 4 }; // 0 ~ 2 배속 그 외 나머지 설정 값
	// 속도 단위
	int speed_index = 0;
	// 점수
	int score = 0;
	// 깨는데 걸린 시간
	int clearTime = 0;
	// 닉네임
	String nickName = "";
	Container c = getContentPane();
	// 화면 구성 요소 설명은 생성자에 기재
	JLayeredPane frame;
	JLayeredPane stageInfo_lay;
	JLabel stageInfo_img;
	JLabel stName_l;
	JLabel monImg_l;
	JLabel monName_l;
	JLabel monHp_l;
	JLabel stMount_l;
	JLabel remain_l;
	JLabel life_l;
	JLabel money_l;
	JButton startGame_b;
	JButton stopGame_b;
	JButton speedGame_b;
	JPanel nextStage_p;
	JLabel nextStage_l;
	JButton nextStage_b_1;
	JButton nextStage_b_2;
	JButton nextStage_b_3;
	JPanel endStage_p;
	JLabel endStage_l;
	JButton endStage_b_1;
	JButton endStage_b_2;
	JButton endStage_b_3;
	JLayeredPane field;
	JLayeredPane choiceTower_lay;
	JPanel cursor_towerXY_pixel_p = new JPanel();
	JLayeredPane towerInfo_lay = new JLayeredPane();
	JPanel mount_towerXY_pixel_p = new JPanel();
	JLabel warningMsg_l = new JLabel();
	JLabel warningMsg_img = new JLabel();
	JLabel charTower_l = new JLabel();
	JPanel alarmStage_p;
	JLabel alarmStage_l;
	JLabel alarmStage_l_2;
	JPanel newChallenge_op = new JPanel();
	JLabel bgChallenge_l = new JLabel();
	JLabel msgChallenge_l = new JLabel();
	JButton yesChallenge_b = new JButton();
	JButton noChallenge_b = new JButton();
	JButton cancel_b;
	JPanel up_un_p = new JPanel();
	JButton upgrade_b = new JButton();
	JButton unMount_b = new JButton();
	Thread t;

	public DefenseGame_Frame() {
		initialization();
		mainFrame();
		MainBGM = new Bgm("startGame.wav");
		MainBGM.play(-1, -30f);
		Thread runningGame;
		runningGame = new Thread(this);
		runningGame.start();
		setVisible(true);

	} // Defense_Frame

	// MainFrame 설정
	public void mainFrame() {

		// 기본 프레임 생성------------------------------------------------------------------
		setSize(1150, 650);
		setTitle("Defense Game");
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});
		;
		frame = new JLayeredPane();
		frame.setLayout(null);
		frame.setBounds(0, 0, 1150, 650);
		JLabel backgroundFrame_1 = new JLabel(new ImageIcon("imgSet/frame/packMan.gif"));
		backgroundFrame_1.setBounds(240, 20, 630, 80);
		JLabel backgroundFrame_2 = new JLabel();
		backgroundFrame_2.setBounds(0, 0, 1150, 650);
		backgroundFrame_2.setOpaque(true);
		backgroundFrame_2.setBackground(Color.black);
		JLabel backgroundFrame_3 = new JLabel(new ImageIcon("imgSet/frame/logo.gif"));
		backgroundFrame_3.setBounds(20, 10, 200, 100);
		frame.add(backgroundFrame_1, new Integer(0));
		frame.add(backgroundFrame_3, new Integer(0));
		frame.add(backgroundFrame_2, new Integer(0));

		c.add(frame);
		// -----------------------------------------------------------------------------

		// 게임 다시 도전 창 생성---------------------------------------------------------------
		newChallenge_op = new JPanel();
		newChallenge_op.setBounds(425, 250, 300, 225);
		newChallenge_op.setLayout(null);
		bgChallenge_l = new JLabel(new ImageIcon("imgSet/frame/retry.gif"));
		bgChallenge_l.setBounds(0, 0, 300, 165);
		bgChallenge_l.setBorder(new LineBorder(Color.lightGray, 3));
		newChallenge_op.add(bgChallenge_l);
		msgChallenge_l = new JLabel();
		yesChallenge_b = new JButton(new ImageIcon("imgSet/frame/retry_b.gif"));
		yesChallenge_b.setBounds(0, 165, 150, 60);
		yesChallenge_b.setFont(new Font("", Font.BOLD, 15));
		yesChallenge_b.addActionListener(challenge_yes_or_no);
		newChallenge_op.add(yesChallenge_b);
		noChallenge_b = new JButton(new ImageIcon("imgSet/frame/exit_b.gif"));
		noChallenge_b.setBounds(150, 165, 150, 60);
		noChallenge_b.setFont(new Font("", Font.BOLD, 15));
		noChallenge_b.addActionListener(challenge_yes_or_no);
		newChallenge_op.add(noChallenge_b);
		newChallenge_op.setVisible(false);
		frame.add(newChallenge_op, new Integer(2));
		// -----------------------------------------------------------------------------

		// 게임 타이틀 ---------------------------------------------------------------------
		JLabel gameTitle = new JLabel();
		gameTitle.setBounds(20, 20, 200, 80);
		// -----------------------------------------------------------------------------

		// 스테이지 정보 --------------------------------------------------------------------
		stageInfo_lay = new JLayeredPane();
		stageInfo_lay.setLayout(null);
		stageInfo_lay.setBounds(895, 120, 210, 100);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/stageInfo.png"));
		stageInfo_img.setBounds(0, 0, 210, 100);
		stageInfo_lay.add(stageInfo_img, new Integer(0));

		// 스테이지 이름 --------------------------------------------------------------------
		stName_l = new JLabel();
		stName_l.setBounds(5, 5, 100, 20);
		stName_l.setFont(new Font("", Font.BOLD, 15));
		stName_l.setForeground(Color.green);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/stName.png"));
		stageInfo_img.setBounds(5, 5, 100, 20);
		stageInfo_lay.add(stName_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 몬스터 정보 ----------------------------------------------------------------------
		// 이름
		monName_l = new JLabel(" 이름 : ");
		monName_l.setBounds(60, 30, 145, 15);
		monName_l.setFont(new Font("", Font.BOLD, 11));
		monName_l.setForeground(Color.green);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/stMon.png"));
		stageInfo_img.setBounds(60, 30, 145, 15);
		stageInfo_lay.add(monName_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 체력
		monHp_l = new JLabel(" 체력 : ");
		monHp_l.setBounds(60, 55, 70, 15);
		monHp_l.setFont(new Font("", Font.BOLD, 11));
		monHp_l.setForeground(Color.green);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/stStat.png"));
		stageInfo_img.setBounds(60, 55, 70, 15);
		stageInfo_lay.add(monHp_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 이미지
		monImg_l = new JLabel();
		monImg_l.setBounds(7, 32, 46, 36);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/stImg.png"));
		stageInfo_img.setBounds(5, 30, 50, 40);
		stageInfo_lay.add(monImg_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 생성 갯수
		stMount_l = new JLabel(" 갯수 : ");
		stMount_l.setBounds(135, 55, 70, 15);
		stMount_l.setFont(new Font("", Font.BOLD, 11));
		stMount_l.setForeground(Color.green);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/stStat.png"));
		stageInfo_img.setBounds(135, 55, 70, 15);
		stageInfo_lay.add(stMount_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 기타 정보 ----------------------------------------------------------------------
		// 현재 라운드 남아있는 몬스터 수
		remain_l = new JLabel(": ");
		remain_l.setBounds(100, 75, 60, 20);
		remain_l.setFont(new Font("", Font.BOLD, 14));
		remain_l.setForeground(Color.green);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/enemy.png"));
		stageInfo_img.setBounds(75, 75, 60, 20);
		stageInfo_lay.add(remain_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 목숨
		life_l = new JLabel(": ");
		life_l.setBounds(30, 75, 60, 20);
		life_l.setFont(new Font("", Font.BOLD, 14));
		life_l.setForeground(Color.green);
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/life.png"));
		stageInfo_img.setBounds(5, 75, 60, 20);
		stageInfo_lay.add(life_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// 돈
		money_l = new JLabel(": " + myMoney);
		money_l.setBounds(170, 75, 60, 20);
		money_l.setForeground(Color.green);
		money_l.setFont(new Font("", Font.BOLD, 14));
		stageInfo_img = new JLabel(new ImageIcon("imgSet/field/money.png"));
		stageInfo_img.setBounds(145, 75, 60, 20);
		stageInfo_lay.add(money_l, new Integer(1));
		stageInfo_lay.add(stageInfo_img, new Integer(1));
		// -----------------------------------------------------------------------------

		// 전장 창 생성 ---------------------------------------------------------------------
		field = new JLayeredPane();
		field.setLayout(null);
		field.setBounds(20, 120, 850, 450);
		ImageIcon fieldImg = new ImageIcon("imgSet/field/Field.png");
		JLabel backgroundF = new JLabel(fieldImg);
		backgroundF.setBounds(0, 0, 850, 450);
		field.add(backgroundF, new Integer(0));
		// -----------------------------------------------------------------------------

		// 타워 선택 창 생성 ------------------------------------------------------------------
		choiceTower_lay = new JLayeredPane();
		choiceTower_lay.setLayout(null);
		choiceTower_lay.setBounds(895, 285, 210, 285);
		JLabel backgroundC = new JLabel();
		backgroundC.setBounds(0, 0, 210, 285);
		choiceTower_lay.add(backgroundC, new Integer(0));
		// 선택할 타워 창
		JLayeredPane tcPanel = new JLayeredPane();
		tcPanel.setLayout(null);
		tcPanel.setBounds(0, 0, 210, 90);
		JLabel backgroundTc = new JLabel(new ImageIcon("imgSet/field/tcPanel.png"));
		backgroundTc.setBounds(0, 0, 210, 90);
		tcPanel.add(backgroundTc, new Integer(0));
		JLabel board_l = new JLabel();
		board_l.setBounds(5, 5, 200, 80);
		tcPanel.add(board_l, new Integer(1));
		JLabel tcTitle = new JLabel("【 타워 선택 】");
		tcTitle.setFont(new Font("", Font.BOLD, 15));
		tcTitle.setForeground(Color.green);
		tcTitle.setHorizontalAlignment(JLabel.CENTER);
		tcTitle.setBounds(0, 0, 200, 20);
		tcTitle.setHorizontalAlignment(JLabel.CENTER);
		board_l.add(tcTitle, new Integer(1));
		JButton[] tcBtn = new JButton[towers.size()];
		for (int i = 0; i < towers.size(); i++) {
			ImageIcon towerImg_l = towers.get(i).getImg()[1];
			tcBtn[i] = new JButton(towerImg_l);
			tcBtn[i].setActionCommand(towers.get(i).getName());
			tcBtn[i].setBounds(i * 70 + 5, 25, 50, 50);
			tcBtn[i].addActionListener(chooseTower);
			board_l.add(tcBtn[i], new Integer(1));
		}
		choiceTower_lay.add(tcPanel, new Integer(1));
		// -----------------------------------------------------------------------------

		// 다음 스테이지 -> 중지 --------------------------------------------------------------
		nextStage_b_3 = new JButton(new ImageIcon("imgSet/field/next.png"));
		nextStage_b_3.setBounds(130, 5, 20, 20);
		nextStage_b_3.addActionListener(btnNextStage_btn);
		nextStage_b_3.setVisible(false);
		stageInfo_lay.add(nextStage_b_3, new Integer(1));
		// -----------------------------------------------------------------------------

		// 스테이지 속도 조정 ----------------------------------------------------------------
		speedGame_b = new JButton(new ImageIcon("imgSet/field/speed.png"));
		speedGame_b.setBounds(130, 5, 20, 20);
		speedGame_b.addActionListener(btnSpeedGame_btn);
		speedGame_b.setVisible(true);
		stageInfo_lay.add(speedGame_b, new Integer(1));

		// 경고 문구 생성 -------------------------------------------------------------------
		warningMsg_l.setBounds(10, 140, 190, 40);
		warningMsg_l.setFont(new Font("", Font.BOLD, 12));
		warningMsg_l.setForeground(Color.green);
		warningMsg_l.setHorizontalTextPosition(JLabel.CENTER);
		warningMsg_img.setIcon(new ImageIcon("imgSet/field/msg.png"));
		warningMsg_img.setBounds(10, 140, 190, 40);
		// -----------------------------------------------------------------------------

		// 카운트 다운 생성 ------------------------------------------------------------------
		alarmStage_p = new JPanel();
		alarmStage_p.setLayout(null);
		alarmStage_p.setBounds(335, 245, 270, 200);
		alarmStage_l = new JLabel();
		alarmStage_l.setBounds(0, 100, 270, 100);
		alarmStage_l.setBorder(new LineBorder(Color.DARK_GRAY, 1));
		alarmStage_l_2 = new JLabel();
		alarmStage_l_2.setBounds(0, 0, 270, 100);
		alarmStage_l_2.setBorder(new LineBorder(Color.DARK_GRAY, 1));
		alarmStage_p.add(alarmStage_l);
		alarmStage_p.add(alarmStage_l_2);
		alarmStage_p.setVisible(false);
		frame.add(alarmStage_p, new Integer(2));
		// -----------------------------------------------------------------------------

		// 다음스테이지 --------------------------------------------------------------------
		nextStage_p = new JPanel();
		nextStage_p.setLayout(null);
		nextStage_p.setBounds(335, 280, 270, 165);
		nextStage_l = new JLabel(new ImageIcon("imgSet/frame/stageClear.gif"));
		nextStage_l.setBounds(0, 0, 270, 110);
		nextStage_l.setBorder(new LineBorder(Color.DARK_GRAY, 1));
		nextStage_b_1 = new JButton(new ImageIcon("imgSet/frame/stageStop.gif"));
		nextStage_b_1.setBounds(0, 110, 135, 55);
		nextStage_b_1.addActionListener(btnNextStage_btn);
		nextStage_b_2 = new JButton(new ImageIcon("imgSet/frame/stageNext.gif"));
		nextStage_b_2.setBounds(135, 110, 135, 55);
		nextStage_b_2.addActionListener(btnNextStage_btn);
		nextStage_p.add(nextStage_l);
		nextStage_p.add(nextStage_b_1);
		nextStage_p.add(nextStage_b_2);
		nextStage_p.setVisible(false);
		frame.add(nextStage_p, new Integer(2));
		// ----------------------------------------------------------------------------

		// 게임클리어 --------------------------------------------------------------------
		endStage_p = new JPanel();
		endStage_p.setLayout(null);
		endStage_p.setBounds(335, 280, 270, 165);
		endStage_l = new JLabel(new ImageIcon("imgSet/frame/endStage.gif"));
		endStage_l.setBounds(0, 0, 270, 110);
		endStage_l.setBorder(new LineBorder(Color.DARK_GRAY, 1));
		endStage_b_1 = new JButton(new ImageIcon("imgSet/frame/save_b.gif"));
		endStage_b_1.setBounds(0, 110, 135, 55);
		endStage_b_1.addActionListener(btnEndStage_btn);
		endStage_b_2 = new JButton(new ImageIcon("imgSet/frame/exit_b_2.gif"));
		endStage_b_2.setBounds(135, 110, 135, 55);
		endStage_b_2.addActionListener(btnEndStage_btn);
		endStage_b_3 = new JButton(new ImageIcon("imgSet/frame/ranking.gif"));
		endStage_b_3.setBounds(970, 20, 135, 45);
		endStage_b_3.addActionListener(btnEndStage_btn);
		endStage_p.add(endStage_l);
		endStage_p.add(endStage_b_1);
		endStage_p.add(endStage_b_2);
		endStage_p.setVisible(false);
		frame.add(endStage_p, new Integer(2));
		frame.add(endStage_b_3, new Integer(2));
		// ----------------------------------------------------------------------------

		// 시작 중지 버튼 -------------------------------------------------------------------
		startGame_b = new JButton(new ImageIcon("imgSet/field/start.png"));
		startGame_b.setBounds(105, 5, 20, 20);
		startGame_b.addActionListener(btnStartStage_btn);
		stopGame_b = new JButton(new ImageIcon("imgSet/field/stop.png"));
		stopGame_b.setBounds(105, 5, 20, 20);
		stopGame_b.addActionListener(btnStopStage_btn);
		stopGame_b.setVisible(false);
		stageInfo_lay.add(startGame_b, new Integer(1));
		stageInfo_lay.add(stopGame_b, new Integer(1));
		// -----------------------------------------------------------------------------

		// 타워 선택 취소 버튼 ----------------------------------------------------------------
		cancel_b = new JButton(new ImageIcon("imgSet/field/x.png"));
		cancel_b.setActionCommand("취소");
		// -----------------------------------------------------------------------------

		frame.add(gameTitle, new Integer(1));
		frame.add(stageInfo_lay, new Integer(1));
		frame.add(field, new Integer(1));
		frame.add(choiceTower_lay, new Integer(1));

	} // mainFrame

	// btnSpeedGame_btn : 게임 속도 조절
	ActionListener btnSpeedGame_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			speed_index = (speed_index + 1) % 3;
		}
	}; // btnSpeedGame_btn()

	// showTower_cursor : 타워 설치시 마우스 위치를 기점으로 타워 이미지 출력
	MouseMotionListener showTower_cursor = new MouseAdapter() {
		public void mouseDragged(MouseEvent e) { // 마우스 움직일때.
			speed_index = 3;
			int x_pixel = e.getX() - 25;
			int y_pixel = e.getY() - 25;
			field.remove(cursor_towerXY_pixel_p);
			field.remove(mount_towerXY_pixel_p);
			field.remove(cancel_b);
			cursor_towerXY_pixel_p = checkActivation(x_pixel, y_pixel, selectedTowerName);
			field.add(cursor_towerXY_pixel_p, new Integer(1));
			towerInfo_lay.remove(warningMsg_l);
			towerInfo_lay.remove(warningMsg_img);
			repaint();
		}
	}; // showTower_cursor()

	// showTower_info : 타워 클릭시 정보표시(towerInfo_lay)
	ActionListener showTower_info = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String date_key = e.getActionCommand();
			Towers towerInfo = actTower_info.get(date_key);
			int x_pixel = actTower_display.get(date_key).getX();
			int y_pixel = actTower_display.get(date_key).getY();
			choiceTower_lay.remove(towerInfo_lay);
			towerInfo_lay = printTowerInfo(towerInfo, date_key);
			choiceTower_lay.add(towerInfo_lay, new Integer(1));
			up_un_p = new JPanel();
			up_un_p.setLayout(null);
			up_un_p.setBounds(x_pixel + 50, y_pixel, 20, 40);
			upgrade_b = new JButton(new ImageIcon("imgSet/field/up.png"));
			upgrade_b.setActionCommand(date_key);
			upgrade_b.setBounds(0, 0, 20, 20);
			upgrade_b.addActionListener(upgradeTower_btn);
			upgrade_b.addMouseListener(remove_btn);
			unMount_b = new JButton(new ImageIcon("imgSet/field/unMount_b.png"));
			unMount_b.setBounds(0, 20, 20, 20);
			unMount_b.setActionCommand(date_key);
			unMount_b.addActionListener(sellTower_btn);
			unMount_b.addMouseListener(remove_btn);
			// up_un_p.addMouseListener(remove_btn);
			up_un_p.add(upgrade_b);
			up_un_p.add(unMount_b);
			field.add(up_un_p, new Integer(3));

		}
	}; // showTower_info()

	// remove_btn : 커서 벗어 났을 시 업그레이드 , 팔기 버튼 비활성화
	MouseListener remove_btn = new MouseAdapter() {
		public void mouseExited(MouseEvent e) {
			if ((e.getSource() == upgrade_b && e.getY() == 20) || (e.getSource() == unMount_b && e.getY() == -1)) {

			} else {
				field.remove(up_un_p);
			}
			repaint();
		};
	}; // remove_btn

	// mountTower : routeCheck반환 값에 따라 역할 수행
	ActionListener mountTower = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			field.remove(cursor_towerXY_pixel_p);
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
			String tNameLevel = selectedTowerName.split("/")[2].replace(".png", "");
			String tName = tNameLevel.substring(0, tNameLevel.length() - 1);
			int index = -1;
			for (int i = 0; i < towers.size(); i++) {
				if (towers.get(i).getName().equals(tName)) {
					index = i;
					break;
				}
			}
			Towers towerInfo = createTower(index);
			int price = Integer.parseInt(towerInfo.getUpgradePrice()[0]);
			switch (routeCheck(price)) {
			case 0: // 설치가능
				towerInfo.setYx_index(new int[] { towerCoordInfo[2], towerCoordInfo[3] });
				towerInfo.setRangePixel(towerInfo.getYx_index());
				JPanel tower_p = new JPanel();
				JButton tower_b = new JButton(new ImageIcon(selectedTowerName));
				tower_p.setLayout(null);
				tower_p.setBounds(towerCoordInfo[1], towerCoordInfo[0], 50, 50);
				tower_b.setBounds(0, 0, 50, 50);
				tower_b.setActionCommand(f.format(date));
				tower_b.addActionListener(showTower_info);
				tower_p.add(tower_b);
				actTower_info.put(f.format(date), towerInfo);
				actTower_display.put(f.format(date), tower_p);
				field.add(actTower_display.get(f.format(date)), new Integer(1));
				warningMsg_l.setText("설치성공 : " + tName);
				money_l.setText("돈 : " + myMoney);
				break;
			case 1: // 돈부족
				warningMsg_l.setText("설치불가 : 돈 부족");
				break;
			case 2: // 경로 문제
				warningMsg_l.setText("설치불가 : 경로를 막음");
				break;
			default:
				break;
			}
			field.remove(mount_towerXY_pixel_p);
			field.remove(cancel_b);
			field.removeMouseMotionListener(showTower_cursor);
			field.removeMouseListener(mountCheck_btn);
			towerInfo_lay.add(warningMsg_l, new Integer(2));
			towerInfo_lay.add(warningMsg_img, new Integer(2));
			repaint();
		}
	}; // mountTower()

	// routeCheck : 해당 자리에 타워가 설치 될 수 있는지 검사
	// return 0 -> 설치 가능
	// return 1 -> 돈 부족
	// return 2 -> 경로 문제
	public int routeCheck(int price) {
		if (myMoney < price) {
			return 1;
		}
		fields.setIsTower(towerCoordInfo[2], towerCoordInfo[3]);
		fields.setIsPass();
		fields.isPassPoss(0, 0);
		boolean[][] t = fields.getIsTower();
		if (fields.getFastestPass() != null) {
			fields.fixRoute(fields.getIsTower());
			myMoney -= price;
			return 0;
		} else {
			fields.setIsTower(towerCoordInfo[2], towerCoordInfo[3]);
			return 2;
		}

	} // routeCheck()

	// mountCheck_btn : 설치 버튼 클릭시 조건에 따라 타워 설치 or 거부
	MouseListener mountCheck_btn = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			speed_index = (speed_index + 1) % 3;
			int x_pixel = e.getX() - 25;
			int y_pixel = e.getY() - 25;
			towerCoordInfo = checkActivation(x_pixel, y_pixel);
			mount_towerXY_pixel_p.setLayout(null);
			JButton mount_b = new JButton(new ImageIcon("imgSet/field/mount_b.png"));
			mount_b.setActionCommand("설치");
			mount_b.setBounds(0, 0, 20, 20);
			mount_towerXY_pixel_p.add(mount_b);
			if (towerCoordInfo[4] != 0) {
				mount_b.addActionListener(mountTower);
				cancel_b.setBounds(towerCoordInfo[1] + 50, towerCoordInfo[0], 20, 20);
				mount_towerXY_pixel_p.setBounds(towerCoordInfo[1] + 50, towerCoordInfo[0] + 20, 20, 20);
				field.add(mount_towerXY_pixel_p, new Integer(2));
			} else {
				cancel_b.setBounds(x_pixel + 50, y_pixel, 20, 20);
			}
			cancel_b.addActionListener(cancelMount_btn);
			field.add(cancel_b, new Integer(2));
			repaint();
		}
	}; // mountCheck_btn()

	// cancelMount_btn : 클릭시 출력 비활성화
	ActionListener cancelMount_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			choiceTower_lay.remove(towerInfo_lay);
			field.remove(cursor_towerXY_pixel_p);
			field.removeMouseMotionListener(showTower_cursor);
			field.removeMouseListener(mountCheck_btn);
			field.remove(cancel_b);
			field.remove(mount_towerXY_pixel_p);
			repaint();
		}
	}; // cancelMount_btn

	// chooseTower : 설치할 타워 선택
	ActionListener chooseTower = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String towerName = e.getActionCommand();
			String path = "imgSet/towers/" + towerName + "1.png";
			selectedTowerName = path;
			choiceTower_lay.remove(towerInfo_lay);
			towerInfo_lay = printTowerInfo(towerName);
			choiceTower_lay.add(towerInfo_lay, new Integer(1));
			field.removeMouseMotionListener(showTower_cursor);
			field.removeMouseListener(mountCheck_btn);
			field.addMouseMotionListener(showTower_cursor);
			field.addMouseListener(mountCheck_btn);
		}
	}; // chooseTower

	// upgradeTower_btn : 타워 업그레이드
	ActionListener upgradeTower_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			choiceTower_lay.remove(towerInfo_lay);
			String date_key = e.getActionCommand();
			Towers towerInfo = actTower_info.get(date_key);
			int price;
			try {
				price = Integer.parseInt(towerInfo.getUpgradePrice()[towerInfo.getLevel()]);
			} catch (Exception e2) {
				// TODO: handle exception
				price = 0;
			}
			if (myMoney < price) {
				warningMsg_l.setText("업그레이드 불가 : 돈 부족");
				towerInfo_lay = printTowerInfo(towerInfo, date_key);
				towerInfo_lay.add(warningMsg_l, new Integer(2));
				towerInfo_lay.add(warningMsg_img, new Integer(2));
			} else {
				myMoney -= price;
				towerInfo.setSell(towerInfo.getLevel());
				towerInfo.setLevel(towerInfo.getLevel());
				towerInfo.setRangePixel(towerInfo.getYx_index());
				String path = "imgSet/towers/" + towerInfo.getName() + towerInfo.getLevel() + ".png";
				((JButton) actTower_display.get(date_key).getComponent(0)).setIcon(new ImageIcon(path));
				towerInfo_lay = printTowerInfo(towerInfo, date_key);
				money_l.setText("돈 : " + myMoney);
			}
			choiceTower_lay.add(towerInfo_lay, new Integer(1));
		}
	}; // upgradeTower_btn()

	// sellTower_btn : 타워를 전장에서 없애고 내가 지금까지 투자한 절반의 금액을 돌려받음
	ActionListener sellTower_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String date_key = e.getActionCommand();
			Towers towerInfo = actTower_info.get(date_key);
			int[] yX_index = towerInfo.getYx_index();
			myMoney += towerInfo.getSell();
			field.remove(actTower_display.get(date_key));
			actTower_info.remove(date_key);
			actTower_display.remove(date_key);
			fields.setIsTower(yX_index[0], yX_index[1]);
			choiceTower_lay.remove(towerInfo_lay);
			field.remove(up_un_p);
			repaint();
		}
	}; // sellTower_btn()

	// btnNextStage_btn : 다음 스테이지 정보 저장
	ActionListener btnNextStage_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == nextStage_b_1) { // 중지
													// 눌렀을
													// 경우
				nextStage_p.setVisible(false);
				nextStage_b_3.setVisible(true);
			} else { // 중지를 누른후 생성된 다음 스테이지 버튼 누르거나 다이렉트로 다음 스테이지 버튼
				if (e.getSource() == nextStage_b_2) {
					nextStage_p.setVisible(false);
				} else {
					nextStage_b_3.setVisible(false);
				}
				if (isRun && isClear) {
					System.out.println("다음 스테이지 정보");
					if (curStageIndex < stages.size() - 1) {
						curStageIndex++;
						remainMon = stages.get(curStageIndex).getMount();
						life += 5;
					}
					isClear = false;
					isRun = true;
				}
			}
			repaint();
		}
	}; // btnNextStage_btn()

	// btnStartStage_btn : 스테이지 진행
	ActionListener btnStartStage_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isRun) {
				isRun = false;
				isAlarm = true;
				isStart = true;
				isStop = false;
				alarmStage_p.setVisible(true);
				field.remove(up_un_p);
				if (!isDie) {
					isDie = !isDie;
				}
				getStageInfo(isDie);
				startGame_b.setVisible(false);
				stopGame_b.setVisible(true);
				System.out.println("스테이지 시작");
			} else {
				System.out.println("스테이지 진행중");
			}
		}
	}; // btnStartStage_btn()

	// btnStopStage_btn : 스테이지 일시멈춤
	ActionListener btnStopStage_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			isStop = !isStop;
			if (!isAlarm) {
				if (isStop) {
					stopGame_b.setIcon(new ImageIcon("imgSet/field/start.png"));
				} else {
					stopGame_b.setIcon(new ImageIcon("imgSet/field/stop.png"));
				}
			}
		}
	}; // btnStopStage_btn()

	// challenge_yes_or_no : 재도전 희망하는경우 현재 라운드 백업, 그 외 종료
	ActionListener challenge_yes_or_no = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == yesChallenge_b) {
				isDie = false;
				getStageInfo(isDie);
				newChallenge_op.setVisible(false);
				EndBGM.stopMusic();
				MainBGM.play(-1, -30f);
			} else {
				isOut = false;
				dispose();
			}
		}
	};

	// btnEndStage_btn : 모든 스테이지 클리어시 그냥 종료 or 랭킹 등록
	ActionListener btnEndStage_btn = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			User user = null;
			if (e.getSource() == endStage_b_1) { // 랭킹 저장
				nickName = JOptionPane.showInputDialog("id 입력", JOptionPane.OK_CANCEL_OPTION);
				user = new User(nickName, score, clearTime / 1000);
				new UserIO(user).save();
				endStage_p.setVisible(false);
			} else if (e.getSource() == endStage_b_2) { // 그냥 종료
				isOut = false;
				dispose();
			} else if (e.getSource() == endStage_b_3) {
				ArrayList<User> users = new UserIO(user).ranking();
				JFrame ranking = new JFrame("Ranking");
				ranking.setSize(300, 200);
				ranking.setLayout(null);
				ranking.setResizable(false);
				ranking.setLocationRelativeTo(null);
				ranking.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						ranking.dispose();
					}
				});
				for (int i = 0; i < 5; i++) {
					try {
						if (users.get(i).getNickName() != null) {
							String board = "[" + (i + 1) + "]" + "등 : " + users.get(i).getNickName() + " | "
									+ users.get(i).getScore();
							JLabel grade = new JLabel(board);
							grade.setBounds(50, 20 * (i + 1), 200, 20);
							ranking.add(grade);
						}
					} catch (Exception e2) {
					}
				}
				ranking.setVisible(true);
			}
		}
	};

	// showMonster_pixel : 스테이지에 나온 몬스터들 각각의 좌표에 따라 화면에 출력
	public void showMonster_pixel() {
		for (int index_key : actMonster_display.keySet()) {
			field.remove(actMonster_display.get(index_key));
			Monster unit = actMonster_info.get(index_key);
			int y_index = unit.getCurIndex()[0];
			int x_index = unit.getCurIndex()[1];
			int y_pixel = unit.getCurYXPixel()[0];
			int x_pixel = unit.getCurYXPixel()[1];
			String str = ((JLabel) actMonster_display.get(index_key).getComponent(1)).getText();
			String logo = str.split(":")[0].trim();
			int hp = Integer.parseInt(str.split(":")[1].trim());
			if (y_index == 8 && x_index == 16 || hp <= 0) {
				actMonster_display.remove(index_key);
				remainMon--;
				if (hp <= 0) {
					myMoney += stages.get(curStageIndex).getMonster().getMoney();
					score += (curStageIndex + 1);
				}
				if (y_index == 8 && x_index == 16) {
					life--;
				}
			} else {
				((JPanel) actMonster_display.get(index_key)).setBounds(x_pixel + 2, y_pixel + 2, 46, 46);
				field.add(actMonster_display.get(index_key), new Integer(1));
				unit.aStar(y_index, x_index, fields.getRoute());
				unit.printMonsterXY();
			}
		}
		repaint();
	} // showMonster_pixel()

	// createMonster : 스테이지에 몬스터 생성 (actMonster_display)
	public void createMonster(int i) {
		System.out.println(i + "번째 유닛 생성");
		actMonster_info.get(i).setId(i);
		JPanel unit = new JPanel();
		unit.setLayout(null);
		JLabel unitImg = new JLabel(actMonster_info.get(i).getImg());
		unitImg.setBounds(2, 2, 42, 32);
		// unitImg.setBorder(new LineBorder(Color.green, 1));
		JLabel unitHp = new JLabel("HP : " + actMonster_info.get(i).getMonHp());
		unitHp.setBounds(1, 35, 44, 9);
		unitHp.setFont(new Font("", Font.BOLD, 10));
		unitHp.setForeground(Color.green);
		unitHp.setHorizontalAlignment(JLabel.CENTER);
		JLabel bgUnit = new JLabel(new ImageIcon("imgSet/monsters/bgUnit.png"));
		bgUnit.setBounds(0, 0, 46, 46);
		unit.add(unitImg);
		unit.add(unitHp);
		unit.add(bgUnit);
		actMonster_display.put(i, unit);
	} // createMonster()

	// tower_atk_monster : 타워 공격 범위 안에 있을 시 몬스터 공격 후 hp 갱신
	public void tower_atk_monster() {
		for (String date_key : actTower_info.keySet()) { // 모든 설치된 타워 조사
			Towers towerInfo = actTower_info.get(date_key);
			int[][] range_pixel = towerInfo.getRangePixel();
			int level = towerInfo.getLevel();
			int demage = towerInfo.getDemage()[level];
			int attackSpeed = towerInfo.getAtackSpeed()[level];
			String name = towerInfo.getName();
			String path;
			boolean isBgm = true;
			float volume;
			if (name.equals("magic")) {
				volume = -10f;
			} else if (name.equals("cannon")) {
				volume = -10f;
			} else {
				volume = -25f;
			}
			if (towerInfo.isAtkCooldown()) {
				for (int index_key : actMonster_display.keySet()) { // 현재 전장에 있는 모든 몬스터 조사
					String str = ((JLabel) actMonster_display.get(index_key).getComponent(1)).getText();
					String logo = str.split(":")[0].trim();
					int hp = Integer.parseInt(str.split(":")[1].trim());
					int[] monster_pixel = actMonster_info.get(index_key).getCurYXPixel();
					if (isRangeIn(range_pixel, monster_pixel)) {
						hp -= demage;
						path = "imgSet/towers/" + towerInfo.getName() + (towerInfo.getLevel() + 3) + ".png";
						((JButton) actTower_display.get(date_key).getComponent(0)).setIcon(new ImageIcon(path));
						actMonster_info.get(index_key).setMonHp(hp);
						((JLabel) actMonster_display.get(index_key).getComponent(1)).setText(logo + " : " + hp);
						if (isBgm) {
							new Bgm(name + ".wav").play(0, volume);
							isBgm = false;
						}
						if (!name.equals("Cannon")) {
							break;
						}
					}
				} // for inner
			}
			towerInfo.cooldown();
			if (towerInfo.getChangeImg() == 20 * attackSpeed) {
				path = "imgSet/towers/" + towerInfo.getName() + towerInfo.getLevel() + ".png";
				((JButton) actTower_display.get(date_key).getComponent(0)).setIcon(new ImageIcon(path));
			}
		} // for outer

	} // tower_atk_monster()

	// isRangeIn : 해당 타워 공격사거리 범위 안에 해당 몬스터가 존재 하는 여부
	public boolean isRangeIn(int[][] ran, int[] pix) {
		if (ran[0][0] <= pix[0] && ran[0][1] >= pix[0] && ran[1][0] <= pix[1] && ran[1][1] >= pix[1]) {
			return true;
		}
		return false;
	} // isRangeIn()

	// checkActivation : 현재 좌표가 타워설치 가능한 유효 좌표인지 검사
	// 타워 설치가능한 경우 활성화 이미지 삽입 후 커서 좌표 수정
	// 타워 설치가 불가능한 경우 비활성화 이미지 삽입 후 현재 커서 좌표 유지
	public JPanel checkActivation(int x, int y, String str) {
		System.out.println(str);
		int x_index = Math.round((float) x / 50);
		int y_index = Math.round((float) y / 50);
		JPanel towerXY_p = new JPanel();
		JLabel towerXYImg_l;
		towerXY_p.setLayout(null);
		if (x_index < 0 || y_index < 0 || x_index >= fields.getTowerYX()[0].length
				|| y_index >= fields.getTowerYX().length) {
			return towerXY_p;
		}
		int[] coord = fields.getTowerYX()[y_index][x_index];
		boolean isExistTower = fields.getIsTower()[y_index][x_index];
		if (!isExistTower && (coord[0] - y) <= 10 && (coord[0] - y) >= -10 && (coord[1] - x) <= 10
				&& (coord[1] - x) >= -10) {
			towerXY_p.setBounds(coord[1], coord[0], 50, 50);
			towerXYImg_l = new JLabel(new ImageIcon(str));
		} else {
			str = str.replaceAll("towers/", "towers/not");
			towerXY_p.setBounds(x, y, 50, 50);
			towerXYImg_l = new JLabel(new ImageIcon(str));
		}
		towerXYImg_l.setBounds(0, 0, 50, 50);

		towerXY_p.add(towerXYImg_l);
		return towerXY_p;
	} // checkActivation()

	// checkActivation : 타워 설치가 가능한 경우 설치할 좌표 값 리턴
	public int[] checkActivation(int x, int y) {
		int x_index = Math.round((float) x / 50);
		int y_index = Math.round((float) y / 50);
		int[] returnCoord = new int[5];
		if (x_index < 0 || y_index < 0 || x_index >= fields.getTowerYX()[0].length
				|| y_index >= fields.getTowerYX().length) {
			return returnCoord;
		}
		int[] coord = fields.getTowerYX()[y_index][x_index];
		boolean isExistTower = fields.getIsTower()[y_index][x_index];
		if (!isExistTower && (coord[0] - y) <= 10 && (coord[0] - y) >= -10 && (coord[1] - x) <= 10
				&& (coord[1] - x) >= -10) {
			returnCoord[0] = coord[0];
			returnCoord[1] = coord[1];
			returnCoord[2] = y_index;
			returnCoord[3] = x_index;
			returnCoord[4] = 1;
		}
		return returnCoord;
	} // checkActivation()

	// initialization : 데이터베이스에서 값 가져오기
	public void initialization() {
		int towerIndex = db.getTowerName().length;
		for (int i = 0; i < towerIndex; i++) {
			String name = db.getTowerName()[i];
			int[] attackSpeed = db.getTowerAtackSpeed()[i];
			int[] range = db.getTowerRange()[i];
			int[] demage = db.getTowerDemage()[i];
			ImageIcon[] img = db.getTowerImg()[i];
			String[] upgrade = db.getTowerUpgradePrice()[i];
			Towers ts = new Towers(name, attackSpeed, range, demage, img, upgrade);
			towers.add(ts);
		}
		int stageIndex = db.getStageName().length;
		for (int i = 0; i < stageIndex; i++) {
			String name = db.getStageName()[i];
			int mount = db.getStageMount()[i];
			String monsterName = db.getStageMonName()[i];
			int hp = db.getStageMonHp()[i];
			int speed = db.getStageMonSpeed()[i];
			int money = db.getStageMonMoney()[i];
			ImageIcon img = db.getMonImg()[i];
			Stage towerInfo = new Stage(i, name, mount, monsterName, hp, speed, img, money);
			stages.add(towerInfo);
		}
	} // initialization()

	// getStageInfo : 다음 스테이지 버튼 클릭시 스테이지 정보 변경
	// 현재 타워 배치 정보를 저장해 놔야함 <- 죽고 다시 시작할때 필요
	// classify : true -> 일반적인 초기화
	// classify : flase -> 죽고 재도전 할 경우 초기화
	public void getStageInfo(boolean classify) {
		if (classify) { // 일반적인 초기화
			actTower_info_die.clear();
			actTower_display_die.clear();
			for (String date_key : actTower_info.keySet()) {
				actTower_info_die.put(date_key, actTower_info.get(date_key));
				actTower_display_die.put(date_key, actTower_display.get(date_key));
			}
			life_die = life;
			myMoney_die = myMoney;
		} else { // 재도전 할 경우 초기화
			ArrayList<String> returnTower = new ArrayList<String>();
			// 현재 전장에 나와 있는 모든 몬스터 패널 지움
			for (int index_key : actMonster_display.keySet()) {
				field.remove(actMonster_display.get(index_key));
			}
			// 패배 직전 내가 배치한 타워와 라운드 시작시 배치한 타워 비교
			for (String date_key : actTower_display.keySet()) {
				// 만약 내가 라운드 시작시 가지고 있는 타워가 아니였다면
				// 해당 타워가 가지고 있는 좌표를 필드값에서 재설정 후 삭제
				if (actTower_display_die.get(date_key) == null) {
					int[] yX_index = actTower_info.get(date_key).getYx_index();
					fields.setIsTower(yX_index[0], yX_index[1]);
					field.remove(actTower_display.get(date_key));
					returnTower.add(date_key);
				}
			}
			for (String date_key : returnTower) {
				actTower_info.remove(date_key);
				actTower_display.remove(date_key);
			}
			life = life_die;
			myMoney = myMoney_die;
		}
		actMonster_info.clear();
		actMonster_display.clear();
		for (int i = 0; i < stages.get(curStageIndex).getMount(); i++) {
			String name = stages.get(curStageIndex).getMonster().getMonName();
			int hp = stages.get(curStageIndex).getMonster().getMonHp();
			int speed = stages.get(curStageIndex).getMonster().getMonSpeed();
			int money = stages.get(curStageIndex).getMonster().getMoney();
			ImageIcon img = stages.get(curStageIndex).getMonster().getImg();
			Monster monster = new Monster(name, hp, speed, img, money);
			actMonster_info.put(i, monster);
		}
		remainMon = stages.get(curStageIndex).getMount();
		repaint();
	} // getStageInfo()

	// createTower : 타워 한 개체 생성
	public Towers createTower(int i) {
		String name = db.getTowerName()[i];
		int[] attackSpeed = db.getTowerAtackSpeed()[i];
		int[] range = db.getTowerRange()[i];
		int[] demage = db.getTowerDemage()[i];
		ImageIcon[] img = db.getTowerImg()[i];
		String[] upgrade_b = db.getTowerUpgradePrice()[i];
		Towers tower = new Towers(name, attackSpeed, range, demage, img, upgrade_b);
		return tower;
	} // createTower()

	// printTowerInfo : tower_info_lay 생성
	public JLayeredPane printTowerInfo(String towerName) {
		JLayeredPane info_lay = new JLayeredPane();
		info_lay.setLayout(null);
		info_lay.setBounds(0, 90, 210, 195);
		JLabel backgroundInfo_l = new JLabel(new ImageIcon("imgSet/field/info_lay.png"));
		backgroundInfo_l.setBounds(0, 0, 210, 195);
		info_lay.add(backgroundInfo_l, new Integer(0));
		Towers towerInfo = null;
		for (int i = 0; i < towers.size(); i++) {
			if (towers.get(i).getName().equals(towerName)) {
				towerInfo = towers.get(i);
				break;
			}
		}
		if (towerInfo.getName().contains("Cannon")) {
			charTower_l = new JLabel("<html><body>〔범위 공격〕<br>짧은 사거리와 낮은 연사속도</body></html>");
		} else if (towerInfo.getName().equals("Normal")) {
			charTower_l = new JLabel("<html><body>〔단일 공격〕<br>짧은 사거리와 높은 연사속도</body></html>");
		} else if (towerInfo.getName().equals("Magic")) {
			charTower_l = new JLabel("<html><body>〔단일 공격〕<br>높은 사거리와 낮은 연사속도</body></html>");
		}
		charTower_l.setBounds(5, 140, 200, 40);
		charTower_l.setFont(new Font("", Font.BOLD, 12));
		charTower_l.setForeground(Color.green);
		charTower_l.setHorizontalAlignment(JLabel.CENTER);
		JLabel charTower_img = new JLabel(new ImageIcon("imgSet/field/msg.png"));
		charTower_img.setBounds(10, 140, 190, 40);
		info_lay.add(charTower_l, new Integer(1));
		info_lay.add(charTower_img, new Integer(1));
		JLabel board_l = new JLabel();
		board_l.setBounds(5, 5, 200, 130);
		info_lay.add(board_l, new Integer(1));
		JLabel towerImg_l = new JLabel(towerInfo.getImg()[1]);
		towerImg_l.setBounds(7, 12, 46, 46);
		JLabel towerImg_l_2 = new JLabel(new ImageIcon("imgSet/field/towerImg.png"));
		towerImg_l_2.setBounds(5, 10, 50, 50);
		board_l.add(towerImg_l, new Integer(1));
		board_l.add(towerImg_l_2, new Integer(1));
		JLabel[] towerFactor_l = new JLabel[5];
		towerFactor_l[0] = new JLabel("   이름  :  " + towerInfo.getName());
		towerFactor_l[1] = new JLabel("   공속  :  " + towerInfo.getAtackSpeed()[1]);
		towerFactor_l[2] = new JLabel("   범위  :  " + towerInfo.getRange()[1]);
		towerFactor_l[3] = new JLabel("   공격  :  " + towerInfo.getDemage()[1]);
		towerFactor_l[4] = new JLabel("   가격  :  " + towerInfo.getUpgradePrice()[0]);
		JLabel towerFactor_img;
		for (int i = 0; i < 5; i++) {
			towerFactor_l[i].setBounds(70, i * 25 + 10, 125, 20);
			towerFactor_l[i].setFont(new Font("", Font.BOLD, 13));
			towerFactor_l[i].setForeground(Color.green);
			towerFactor_img = new JLabel(new ImageIcon("imgSet/field/towerFactor_img.png"));
			towerFactor_img.setBounds(70, i * 25 + 10, 125, 20);
			board_l.add(towerFactor_l[i], new Integer(1));
			board_l.add(towerFactor_img, new Integer(0));
		}
		return info_lay;
	} // printTowerInfo()

	// printTowerInfo : tower_info_lay 생성
	public JLayeredPane printTowerInfo(Towers tower, String key) {
		JLayeredPane info_lay = new JLayeredPane();
		info_lay.setLayout(null);
		info_lay.setBounds(0, 90, 210, 195);
		JLabel backgroundInfo_l = new JLabel(new ImageIcon("imgSet/field/info_lay.png"));
		backgroundInfo_l.setBounds(0, 0, 210, 195);
		info_lay.add(backgroundInfo_l, new Integer(0));
		JLabel board_l = new JLabel();
		board_l.setBounds(5, 5, 200, 130);
		info_lay.add(board_l, new Integer(1));
		JLabel towerImg_l = new JLabel(tower.getImg()[tower.getLevel()]);
		towerImg_l.setBounds(7, 12, 46, 46);
		board_l.add(towerImg_l, new Integer(1));
		JLabel towerLevel_l = new JLabel("  Level " + tower.getLevel());
		towerLevel_l.setBounds(5, 60, 50, 15);
		towerLevel_l.setFont(new Font("", Font.BOLD, 12));
		towerLevel_l.setForeground(Color.green);
		towerLevel_l.setHorizontalTextPosition(JLabel.CENTER);
		board_l.add(towerLevel_l, new Integer(1));
		JLabel towerImg_l_2 = new JLabel(new ImageIcon("imgSet/field/towerImg2.png"));
		towerImg_l_2.setBounds(5, 10, 50, 65);
		board_l.add(towerImg_l_2, new Integer(1));
		JLabel[] towerFactor_l = new JLabel[5];
		JLabel towerFactor_img;
		towerFactor_l[0] = new JLabel("   이름  :  " + tower.getName());
		towerFactor_l[1] = new JLabel("   공속  :  " + tower.getAtackSpeed()[tower.getLevel()]);
		towerFactor_l[2] = new JLabel("   범위  :  " + tower.getRange()[tower.getLevel()]);
		towerFactor_l[3] = new JLabel("   공격  :  " + tower.getDemage()[tower.getLevel()]);
		towerFactor_l[4] = new JLabel("   비용  :  " + tower.getUpgradePrice()[tower.getLevel()]);
		for (int i = 0; i < 5; i++) {
			towerFactor_l[i].setBounds(70, i * 25 + 10, 125, 20);
			towerFactor_l[i].setFont(new Font("", Font.BOLD, 13));
			towerFactor_l[i].setForeground(Color.green);
			towerFactor_img = new JLabel(new ImageIcon("imgSet/field/towerFactor_img.png"));
			towerFactor_img.setBounds(70, i * 25 + 10, 125, 20);
			board_l.add(towerFactor_l[i], new Integer(1));
			board_l.add(towerFactor_img, new Integer(0));
		}
		return info_lay;
	} // printTowerInfo()

	// printStageInfo : 현재 스테이지 정보 출력
	public void printStageInfo() {
		if (isStart) {
			stName_l.setText(" " + stages.get(curStageIndex).getName());
			monImg_l.setIcon(stages.get(curStageIndex).getMonster().getImg());
			monName_l.setText(" 이름 : " + stages.get(curStageIndex).getMonster().getMonName());
			monHp_l.setText(" 체력 : " + stages.get(curStageIndex).getMonster().getMonHp());
			stMount_l.setText(" 갯수 : " + stages.get(curStageIndex).getMount());
			remain_l.setText(": " + remainMon);
			life_l.setText(": " + life);
		}
		money_l.setText(": " + myMoney);
	} // printStageInfo()

	// gameSet : 라이프가 0이 될시 선택창 생성
	public void gameSet() {
		newChallenge_op.setVisible(true);
	} // gameSet()

	@Override
	public synchronized void run() {
		int count_createMonster = 0;
		int count_alarm = 620;
		int tempo = 0;
		int i = 0;
		int timer = 0;
		boolean isEnd = true;

		while (isOut) {
			try {
				// 어떤 조건이든 전장에 몬스터가 없거나 목숨이 0이면 스테이지 멈춤
				if (remainMon == 0 || life <= 0) {
					i = 0;
					isRun = true;
					if (life <= 0) {
						gameSet();
						if (isEnd) {
							EndBGM = new Bgm("endGame.wav");
							EndBGM.play(-1, -30f);
							MainBGM.stopMusic();
							isEnd = false;
						}
					}
					if (remainMon == 0 && !isClear) {
						if (curStageIndex == stages.size() - 1) { // 모든 스테이지 완료
							endStage_p.setVisible(true);
						} else { // 그렇지 않을 경우
							nextStage_p.setVisible(true);
						}
						isClear = true;
					}
					// 공격 모션 초기화
					for (String date_key : actTower_display.keySet()) {
						Towers towerInfo = actTower_info.get(date_key);
						String path = "imgSet/towers/" + towerInfo.getName() + towerInfo.getLevel() + ".png";
						((JButton) actTower_display.get(date_key).getComponent(0)).setIcon(new ImageIcon(path));
					}
					startGame_b.setVisible(true);
					stopGame_b.setVisible(false);
					speedGame_b.setVisible(false);
				}
				// 스테이지 진행
				t.sleep(gameSpeed[speed_index]);
				if (isRun == false && isAlarm == false && isStop == false) {
					// 일정 주기마다 크리처 생성
					if (i < actMonster_info.size()) {
						count_createMonster += 1;
						if (count_createMonster == 50) {
							createMonster(i);
							i++;
							count_createMonster = 0;
						}
					}
					// 몬스터와 타워 상태 갱신
					tower_atk_monster();
					showMonster_pixel();
				}

				// 시작 버튼 눌렀을때 준비 창 띄우기
				if (isAlarm) {
					if (count_alarm == 600) {
						tempo = speed_index;
						speed_index = 4;
						alarmStage_l_2.setIcon(new ImageIcon("imgSet/frame/stage1_" + (curStageIndex + 1) + ".gif"));
					}
					if (count_alarm % 200 == 0 && count_alarm >= 0) {
						int img_index = count_alarm / 200;
						alarmStage_l.setIcon(new ImageIcon("imgSet/frame/" + img_index + ".gif"));
					} else if (count_alarm == -200) {
						alarmStage_p.setVisible(false);
						speedGame_b.setVisible(true);
						count_alarm = 621;
						isAlarm = false;
						speed_index = tempo;
					}
					count_alarm--;
				}

				// 클리어 시간
				clearTime++;
				printStageInfo();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("게임 종료!");
	} // run()
}
