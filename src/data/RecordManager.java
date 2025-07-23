package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class RecordManager {

  private HashMap<String, Record> data;
  private final String DATA_FILE = "data.txt";

  public RecordManager() {
    data = new HashMap<>();
    readData();
  }

  public ArrayList<Record> getData() {
    ArrayList<Record> result = new ArrayList<>();

    for (Record r : data.values()) {
      result.add(r);
    }

    Collections.sort(result);
    return result;
  }

  public void readData() {
    File file = new File(DATA_FILE);

    if (!file.exists()) {
      try {
	file.createNewFile();
      } catch (IOException ioe) {
	System.err.println("Failed to create data file!");
	ioe.printStackTrace();
	return;
      }
    }

    try (Scanner sc = new Scanner(file)) {
      while (sc.hasNextLine()) {
	String[] parsedData = sc.nextLine().split(" ");

	String playerName = parsedData[0];
	int totalMatches = Integer.parseInt(parsedData[1]);
	int totalScore = Integer.parseInt(parsedData[2]);

	Record r = new Record(playerName, totalScore, totalMatches);

	data.put(playerName, r);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void writeData() {
    File file = new File(DATA_FILE);

    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      for (Record r : data.values()) {
	writer.printf("%s %d %d%n",
		r.getPlayerName(),
		r.getTotalGames(),
		r.getTotalScore()
	);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update(Record r) {
    data.put(r.getPlayerName(), r);
    writeData();
  }
  
  public void update() {
    writeData();
  }
  
  public Record getRecord(String playerName) {
    for (Record r : data.values()) {
      if (r.getPlayerName().equals(playerName)) {
	return r;
      }
    }
    
    return null;
  }
}
