package defense_game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserIO {
	User user;
	private FileOutputStream fos = null;
	private ObjectOutputStream oos = null;
	private FileInputStream fis = null;
	private ObjectInputStream ois = null;
	String path;

	public UserIO(User user) {
		this.user = user;
		this.path = "C:/Embedded_KHJ/work/Defense_Game/user";
	}

	public ArrayList<User> ranking() {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		File users_f[] = f.listFiles();
		ArrayList<User> users = new ArrayList<User>();
		for (File u : users_f) {
			try {
				fis = new FileInputStream(u);
				ois = new ObjectInputStream(fis);
				users.add((User) ois.readObject());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					ois.close();
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				if (o1.getScore() >= o2.getScore()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		return users;
	}

	public void save() {
		try {
			fos = new FileOutputStream(path + "/" + user.getNickName());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(user);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
			}
		}

	}
}
