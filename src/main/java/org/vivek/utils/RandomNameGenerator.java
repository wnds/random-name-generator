package org.vivek.utils;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Path("/randomName")
public class RandomNameGenerator {

	private static List<String> inputNameData;
	private static MultiValueMap predictionMap = new MultiValueMap();
	private static Random random = new Random();
	private static Logger logger = Logger.getLogger(RandomNameGenerator.class.getName());

	@GET
	@Produces("text/plain")
	public static String getRandomName() {
		if (inputNameData == null) {
			try {
				downloadAndParseInputData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return generateRandomName();
	}

	private static void downloadAndParseInputData() throws Exception {

		getFile("http://www.skorks.com/wp-content/uploads/2009/07/data.txt");
		parseDataFile();

	}

	private static void parseDataFile() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("data.txt"), "UTF-8"));
		String currentLine;
		inputNameData = new ArrayList<>();
		while ((currentLine = reader.readLine()) != null) {
			inputNameData.addAll(Arrays.asList(StringUtils.split(currentLine, ' ')));
		}
		reader.close();

		for (String name : inputNameData) {
			char[] allChars = name.toCharArray();
			for (int index = 0; index < allChars.length; index++) {
				if ((index + 1) < allChars.length) {
					ArrayList listOfPossibleChars = (ArrayList) predictionMap.get(Character.toLowerCase(allChars[index]));

					if ((listOfPossibleChars != null && !listOfPossibleChars.contains(allChars[index + 1]))
							|| listOfPossibleChars == null)
						predictionMap.put(Character.toLowerCase(allChars[index]), allChars[index + 1]);
				}
			}
		}
	}

	private static String generateRandomName() {

		int randomIndex = random.nextInt(inputNameData.size());
		String nameFromDataFile = inputNameData.get(randomIndex);
		String prefix = StringUtils.substring(nameFromDataFile, 0, 2);

		int nameSize = random.nextInt(7) + 3;
		int index = 0;
		String suffix = "";
		Character key = prefix.charAt(1);
		ArrayList listOfPossibleChars = (ArrayList) predictionMap.get(Character.toLowerCase(key));

		while (index < (nameSize - 2)) {
			suffix += listOfPossibleChars.get((listOfPossibleChars.size() - 1 > 0) ? random.nextInt(listOfPossibleChars.size() - 1) : 0);
			key = suffix.charAt(suffix.length() - 1);
			listOfPossibleChars = (ArrayList) predictionMap.get(Character.toLowerCase(key));
			index++;
		}

		return StringUtils.strip(prefix.concat(suffix));
	}

	private static void getFile(String url) throws Exception {

		InputStream is = null;
		BufferedOutputStream bos = null;
		try {
			URL pageURL = new URL(url);
			HttpURLConnection huc = (HttpURLConnection) pageURL
					.openConnection();
			huc.setRequestMethod("GET");
			HttpURLConnection.setFollowRedirects(true);
			huc.connect();
			int code = huc.getResponseCode();
			if (code == 300) {
				throw new Exception("File does not exist");
			}

			is = huc.getInputStream();

			String fileName = "data.txt";

			bos = new BufferedOutputStream(new FileOutputStream(new File(fileName)));

			byte[] buffer = new byte[8192];

			long countKBRead = 0;
			int count = 0;
			while ((count = is.read(buffer)) != -1) {
				countKBRead += count;
				bos.write(buffer, 0, count);
				System.out.print(".");
			}
			System.out.print("\n");
			logInfo("Read " + countKBRead / 1024 + " KB");
			if (countKBRead == 0) {
				throw new Exception("No bytes read from stream");
			}
			bos.flush();
		} finally {
			if (is != null)
				is.close();
			if (bos != null)
				bos.close();
		}
	}

	private static void logInfo(String log) {
		logger.info(log);
	}
}
