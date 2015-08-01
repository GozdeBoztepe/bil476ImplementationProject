package bil476ImplementationProject;

import java.util.*;
import java.awt.peer.CanvasPeer;
import java.io.*;

public class Apriori {

	String filePath;
	int loopCount;
	double minSup;
	List<ArrayList<String>> transactions;
	List<String> candidateItems;
	List<String> frequentItems;

	public Apriori(String filePath, double minSup) {
		this.filePath = filePath;
		this.minSup = minSup;
		transactions = new ArrayList<ArrayList<String>>();
		candidateItems = new ArrayList<String>();
		frequentItems = new ArrayList<String>();
		createTransactionSet(filePath);
		loopCount = 0;
	}

	public void createTransactionSet(String filePath) {
		try {
			loopCount++;
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {

				String[] temp = strLine.split(" ");
				ArrayList<String> arrayTemp = new ArrayList<String>();
				for (int i = 0; i < temp.length; i++) {

					if (candidateItems.size() == 0) {
						candidateItems.add(temp[i]);
					}
					if (!candidateItems.contains(temp[i]))
						candidateItems.add(temp[i]);
					arrayTemp.add(temp[i]);
				}
				transactions.add(arrayTemp);

			}
			
			in.close();
			minSup = transactions.size() * minSup;
			countingCandidateItems();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void countingCandidateItems() {
		// System.out.println(minSup);
		System.out.println("Counting Candidate Items " + loopCount);
		frequentItems.clear();
		for (String cndStr : candidateItems) {
			int count = 0;
			if (loopCount == 1) {
				for (ArrayList<String> arr : transactions) {
					if (arr.contains(cndStr))
						count++;
					// for (String str : arr) {
					// if(cndStr.equals(str))
					// count++;
					// }
				}

				if (count >= minSup)
					frequentItems.add(cndStr);
			} else {
				String[] multiTemp = cndStr.split(" ");
				ArrayList<String> tmp = new ArrayList<String>();
				for (int i = 0; i < multiTemp.length; i++) {
					tmp.add(multiTemp[i]);
				}
				boolean flag = false;
				for (ArrayList<String> arr : transactions) {
					for (String mulStr : tmp) {
						if (arr.contains(mulStr))
							flag = true;
						else{
							flag = false;
							break;
							}
					}
					if (flag)
						count++;

					// for (int i = 0; i < multiTemp.length; i++) {
					// if (arr.contains(multiTemp[i]))
					// flag = true;
					// else
					// flag = false;
					// }
					// if(flag){
					// }

				}
				if (count >= minSup)
					if (!frequentItems.contains(cndStr))
						frequentItems.add(cndStr);
				// System.out.println(frequentItems.size());

			}
		}
		for (String stri : frequentItems) {
			System.out.println(stri);
		 }
		System.out.println("Frequent Item Size: "+ frequentItems.size());
		candidateGeneration();
	}

	public void candidateGeneration() {
		System.out.println("Candidate Generation " + loopCount);		
		candidateItems.clear();
		if (loopCount == 1) {
			for (String genStr : frequentItems) {
				for (String frqStr : frequentItems) {
					if (genStr.equals(frqStr))
						continue;
					if (candidateItems.isEmpty())
						candidateItems.add(genStr + " " + frqStr);

					if (!(candidateItems.contains(genStr + " " + frqStr)
							|| candidateItems.contains(frqStr + " " + genStr) || genStr
								.equals(frqStr)))
						candidateItems.add(genStr + " " + frqStr);
				}
			}
		} else {
			for (String genStr : frequentItems) {
				for (String frqStr : frequentItems) {
					if (genStr.equals(frqStr))
						continue;
					String[] genArray = genStr.split(" ");
					String[] frqArray = frqStr.split(" ");
					if(!genArray[0].equals(frqArray[0]))
						continue;
					if(!(candidateItems.isEmpty()) ){
						String tmp = "";
						for (int i = 0; i < frqArray.length; i++) {							
								tmp += genArray[i]+" ";							
						}
						tmp += frqArray[frqArray.length-1];
						if(candidateItems.contains(tmp))
							continue;
					}
					String[] candArray = new String[frqArray.length+1];
					boolean flag = false;
					String candWord = "";
					for (int i = 0; i < frqArray.length; i++) {
						if (i < frqArray.length - 1) {
							if (genArray[i].equals(frqArray[i])) {
								flag = true;
								candArray[i] = frqArray[i];
							} else{
								flag = false;
								break;
							}
						}
						if (i == frqArray.length - 1 && flag) {
							if (!genArray[i].equals(frqArray[i])){
								candArray[i] = frqArray[i];
								candArray[i+1] = genArray[i];
								}
						}
					}
//					for (int i = 0; i < candArray.length; i++) {
//						if (candArray[i] == null) {
//							candArray[i] = "";
//						}
//					}
					if (flag) {
						for (int i = 0; i < candArray.length; i++) {
							if (i == candArray.length - 1 )
								candWord += candArray[i];
							else
								candWord += candArray[i] + " ";
						}
					}

					if (candidateItems.isEmpty() && flag)
						candidateItems.add(candWord);

					if (!(candidateItems.contains(candWord))&& flag)
						candidateItems.add(candWord);
				}
			}

		}
		
		
		System.out.println("Candidate Item Size: "+candidateItems.size());
//		for (String stri : candidateItems) {
//			System.out.println(stri);
//		 }
		loopCount++;
		if(candidateItems.size() == 0)
			System.exit(0);
		countingCandidateItems();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Apriori ap = new Apriori("mushroom.dat", 0.8);

	}

}
